import concurrency.BoundedBuffer;
import concurrency.Producer;
import concurrency.Consumer;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final int CAPACITY = 10;
        BoundedBuffer buffer = new BoundedBuffer(CAPACITY);

        File sharedDir = new File("shared");

        Producer producer = new Producer(buffer, sharedDir);
        Consumer consumer = new Consumer(buffer, sharedDir);

        Thread pThread = new Thread(producer, "ProducerThread");
        Thread cThread = new Thread(consumer, "ConsumerThread");

        pThread.start();
        cThread.start();

        System.out.println("Producer and Consumer started. Press ENTER to stop...");
        try (Scanner sc = new Scanner(System.in)) {
            sc.nextLine();
        }

        // request stop
        System.out.println("Stopping threads...");
        producer.stop();
        consumer.stop();
        pThread.interrupt();
        cThread.interrupt();

        try {
            pThread.join();
            cThread.join();
        } catch (InterruptedException ignored) {}

        System.out.println("Done.");
    }
}
