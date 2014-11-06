import java.util.*;
import java.io.*;

public class BufferFile {
	
	private BufferedReader bfd;
	private String peak;
	private int size = 1024;
	private File primaryFile;
	private boolean isEmpty=false;
	
	public BufferFile(File f) throws IOException {
		bfd = new BufferedReader(new FileReader(f), size);
		primaryFile = f;
		peak = "";
		reload();
	}
	
	public void reload() throws IOException {
		if ((peak = bfd.readLine()) == null) {
			isEmpty = true;
		}
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}
	
	public String peek() {
		if (isEmpty()) return null;
		return peak;
	}
	
	public String pop() throws IOException {
		String res = peak;
		reload();
		return res;
	}
	
	public void close() throws IOException {
		bfd.close();
	}
	
	public void deleteFile() {
		primaryFile.delete();
	}

}
