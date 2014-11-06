import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.Socket; 

public class ManagerThread implements Runnable {
	private static final String IP="localhost";
	private int PORT=12567;
	private ArrayList<String> unsorted;
	private ArrayList<File> fileList;
	private boolean socketOrNot;
	
	public ManagerThread(ArrayList<String> unsorted, boolean socket, ArrayList<File> fileList) {
		this.unsorted = new ArrayList<String>(unsorted);
		socketOrNot = socket;
		this.fileList = fileList;
	}
	
	public void run() {
		if (socketOrNot) {
			//send to worker to sort
			try {
				socket(unsorted);
			} catch (Exception e) {
				System.out.println("Socket sort error: " + e.getMessage());
			}
		} else {
			//sort here
			Comparator<String> comp = new Comparator<String>() {
				public int compare(String s1, String s2) {
					return s1.compareTo(s2);
				}
			};
			try {
				Collections.sort(unsorted, comp);
				File tmp = writeSortedFile(unsorted);
				fileList.add(tmp);
				System.out.println("Write tmp file to: "+ tmp.getAbsolutePath());
			} catch (Exception e) {
				System.out.println("Local sort error: " + e.getMessage());
			}
		}
	}
	
	public File writeSortedFile(ArrayList<String> sortedList) throws IOException {
		//write to tmpFile
		File newTmpFile = File.createTempFile("sortAndSave", "tmp");
		newTmpFile.deleteOnExit();
		BufferedWriter br = new BufferedWriter(new FileWriter(newTmpFile));
		try {
			for (String line: sortedList) {
				br.write(line);
				br.newLine();
			}
			
		} catch (Exception e) {
			System.out.println("sort and save error: " + e.getMessage());
		} finally {
			br.close();
		}
		return newTmpFile;
	}
	
	public void socket(ArrayList<String> orig) {
	    ArrayList<String> receive = new ArrayList<String>();
		Socket socket = null;
		try {
			socket = new Socket(IP, PORT);
			//send message
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); 
			out.writeObject(orig);   
			//get data
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream()); 
			receive = (ArrayList<String>) input.readObject(); 
			File tmp = writeSortedFile(unsorted);
			fileList.add(tmp);
			System.out.println("Write tmp file to: "+ tmp.getAbsolutePath());
			out.close(); 
            input.close(); 
            socket.close();
		} catch (Exception e) {
			System.out.println("Manager Connection error: " + e.getMessage());
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					socket = null;
					System.out.println("Manager Error: " + e.getMessage());
				}
			}
		}
	}

}
