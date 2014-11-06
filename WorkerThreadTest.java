import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WorkerThreadTest implements Runnable {
     
	private int threadName;
	private ArrayList<String> list;
	private ArrayList<Integer> tmp;
	public WorkerThreadTest(int i, ArrayList<String> list, ArrayList<Integer> tmp) {
		threadName = i;
		this.list = list;
		this.tmp = tmp;
	}
	
	public void run() {
		 try {
			list.add("index: " + threadName);
			System.out.println("Run main thread " + threadName);
			System.out.println(tmp.size());
			writeFile(threadName+"");
		 } catch (Exception e) {
			 System.out.println("Error on sub thread 2: " + e.getMessage());
		 }	
	}
	
	public void writeFile(String filename) throws IOException {
		File temp = File.createTempFile("filename", ".tmp"); 
		temp.deleteOnExit();
		BufferedWriter br = new BufferedWriter(new FileWriter(temp));
		br.write("This is index " + filename);
		br.close();
		System.out.println("Thread: " + filename + " path: " +temp.getAbsolutePath());
	}
	
}