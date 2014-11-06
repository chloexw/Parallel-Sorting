import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;  
import java.util.ArrayList;
import java.util.Collections;
public class WorkerThread implements Runnable {
	
	private Socket client = null;
	
	public WorkerThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
		   ObjectInputStream input = new ObjectInputStream(client.getInputStream()); 
	       ArrayList<String> clientMessage = (ArrayList<String>) input.readObject();
           if (clientMessage != null) {
        	 //sort the arrayList
	          Collections.sort(clientMessage);
	          //send the sorted file;
	          ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream()); 
			  out.writeObject(clientMessage);
			  out.close();
           }   
		   input.close();
		   client.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
