package concurrency;

import model.ITStudent;
import util.XMLUtil;

import java.io.File;

public class Consumer implements Runnable {
    private final BoundedBuffer buffer;
    private final File sharedDir;
    private volatile boolean running = true;

    public Consumer(BoundedBuffer buffer, File sharedDir) {
        this.buffer = buffer;
        this.sharedDir = sharedDir;
        if (!sharedDir.exists()) sharedDir.mkdirs();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                int fileNumber = buffer.get(); // blocks until available
                String fname = "student" + fileNumber + ".xml";
                File f = new File(sharedDir, fname);

                if (!f.exists()) {
                    System.out.println("[Consumer] Expected file " + fname +
                    " does not exist. Skipping.");
                } else {
                    ITStudent s = XMLUtil.readStudentFromXML(f); // unwrap
                    // print student details and pass/fail
                    System.out.println("[Consumer] Consumed " + fname + ":");
                    System.out.println(s.toString());

                    // clear/delete file
                    boolean deleted = f.delete();
                    if (!deleted) {
                        // try truncating
                        java.io.FileWriter fw = new java.io.FileWriter(f, false);
                        fw.write("");
                        fw.close();
                    }
                    System.out.println("[Consumer] Cleared " + fname + " and removed " +
                    fileNumber + " from buffer (buffer size now: " + buffer.size() + ")");
                }

                // simulate processing time
                Thread.sleep(900);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("[Consumer] stopped.");
    }
}
