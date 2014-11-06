import java.io.*;
import java.net.Socket;
import java.net.ServerSocket; 
import java.util.ArrayList;
import java.util.Collections;

public class Worker {
	
    public static void main(String[] args) {	
		try {
			ServerSocket server = new ServerSocket(12567); 
			while (true) {
			   Socket client = server.accept();
			   System.out.println("Connect manager successfully..");
			   new Thread(new WorkerThread(client)).start();
			}
		} catch (Exception e) {
			System.out.println("Worker Receiving error: " + e);
		} 
	}

}
