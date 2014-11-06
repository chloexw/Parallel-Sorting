import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Manager {
	//the file arrayList used for merge sort
	private ArrayList<File> fileList;
	private long chunkSize;
	private int chunks=500;
	private File file;
	private long lineSize = 100;//4kb
	private int CAPACITY=chunks;
	private Comparator<BufferFile> bcomp;
	private int outputSize=1024*1024;
	private ExecutorService executor;
	
	public Manager(File largeFile) {
		bcomp = new Comparator<BufferFile>() {
			public int compare(BufferFile f1, BufferFile f2) {
				return f1.peek().compareTo(f2.peek());
			}
		};
		file = largeFile;
		long fileSize = file.length();//bytes
		chunkSize = fileSize/chunks;//kb
		fileList = new ArrayList<File>();
	}
	
	public void cutAndSort() throws IOException {
		//store sorted files;
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			//sorted lines
			ArrayList<String> tmpList = new ArrayList<String>();
		    String line = "";
		    boolean socketSort = false;
		    long sizeCount=0;
		    executor = Executors.newFixedThreadPool(chunks);
		    while ((line = br.readLine()) != null) {
		    	if (sizeCount + lineSize <= chunkSize) {
		    		tmpList.add(line);
		    		sizeCount += lineSize;
		    	} else {
		    		//sort the list
		    		//ArrayList<String> newTmp = new ArrayList<String>(tmpList);
		    		Runnable thread = new ManagerThread(tmpList, socketSort, fileList);
		    		executor.execute(thread);
		    		socketSort = !socketSort;
		    		tmpList.clear();
		    		sizeCount = lineSize;
		    		tmpList.add(line);
		    	}
		    }
		    if (tmpList.size() > 0) {
		    	//ArrayList<String> newTmp = new ArrayList<String>(tmpList);
		    	Runnable thread = new ManagerThread(tmpList, socketSort, fileList);
		    	tmpList.clear();
	    		executor.execute(thread);
		    }
		    executor.shutdown();
		    while (!executor.isTerminated()) {
	        }
		    System.out.println("Threads ended..");
		} catch(Exception e) {
			System.out.println("Error occured when cutting files: " + e.getMessage());
		} finally {
			br.close();
		}
	}
	
	public int mergeSortedFiles(File output) throws IOException {
		PriorityQueue<BufferFile> hp = new PriorityQueue<BufferFile>(CAPACITY, bcomp);
		for (File f: fileList) {
			hp.add(new BufferFile(f));
		}
		int count = 0;
		//set an output buffer;
		BufferedWriter bw = new BufferedWriter(new FileWriter(output), outputSize);
		System.out.println("Heap size: " + hp.size());
		try {
			while (!hp.isEmpty()) {
				BufferFile curr = hp.poll();
				String test = curr.pop();
				bw.write(test);
				bw.newLine();
				count ++;
				if (!curr.isEmpty()) hp.add(curr);
				else {
					curr.deleteFile();
					curr.close();
				}
			}
		} finally {
			bw.close();
		}
		return count;
	}
		
	public static void main(String[] args) throws IOException {
		Manager m = new Manager(new File(args[0]));
		//cut file 
		m.cutAndSort();
		File ff = new File("finalFile.txt"); 
		int count = m.mergeSortedFiles(ff);
		System.out.println("Merged " + count + " lines.");	
		System.out.println("File Path: " + ff.getAbsolutePath());
	}	
}
