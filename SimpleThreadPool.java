import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class SimpleThreadPool {
 
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        for (int i=0; i<9; i++) tmp.add(i);
        for (int i = 0; i < 10; i++) {
            Runnable worker = new WorkerThreadTest(i, list, tmp);
            executor.execute(worker);
          }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
        for (int i=0; i<10; i++) {
        	System.out.println(list.get(i));
        }
    }
 
}
