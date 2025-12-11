package concurrency;

import model.ITStudent;
import util.XMLUtil;

import java.io.File;
import java.util.*;

public class Producer implements Runnable {
    private final BoundedBuffer buffer;
    private final File sharedDir;
    private final Random rand = new Random();
    private final String[] programmes = {"BSc IT", "Diploma IT", "BSc Computer Science",
     "MSc IT"};
    private final String[] coursePool = {
        "Programming I", "Data Structures", "Operating Systems", "Databases",
        "Networks", "Software Eng", "Security", "AI", "Web Tech", "Mobile Dev"
    };
    private volatile boolean running = true;

    public Producer(BoundedBuffer buffer, File sharedDir) {
        this.buffer = buffer;
        this.sharedDir = sharedDir;
        if (!sharedDir.exists()) sharedDir.mkdirs();
    }

    public void stop() {
        running = false;
    }

    private ITStudent generateRandomStudent() {
        String name = randomName();
        String id = String.format("%08d", rand.nextInt(100_000_000));
        String prog = programmes[rand.nextInt(programmes.length)];
        int numCourses = 4 + rand.nextInt(3); // 4-6 courses
        List<String> pool = new ArrayList<>(Arrays.asList(coursePool));
        Collections.shuffle(pool, rand);
        Map<String,Integer> cm = new LinkedHashMap<>();
        for (int i = 0; i < numCourses; i++) {
            cm.put(pool.get(i), rand.nextInt(101)); // 0-100
        }
        return new ITStudent(name, id, prog, cm);
    }

    private String randomName() {
        String[] first = {"Thabo","Nandi","Sipho","Lerato","Anele","Kabelo",
        "Mpho","Sanele","Zanele","Tumi"};
        String[] last = {"Dlamini","Motsa","Nkosi","Matsebula","Mamba","Ndlovu",
        "Mkhize","Hlophe"};
        return first[rand.nextInt(first.length)] + " " + last[rand.nextInt(last.length)];
    }

    @Override
    public void run() {
        int nextFileNumber = 1; // cycle 1..10
        while (running) {
            try {
                ITStudent s = generateRandomStudent();
                String fname = "student" + nextFileNumber + ".xml";
                File out = new File(sharedDir, fname);

                // write XML (wrap)
                XMLUtil.writeStudentToXML(s, out);

                // put integer corresponding to file to buffer
                buffer.put(nextFileNumber);
                System.out.println("[Producer] produced " + fname + " and inserted " + 
                nextFileNumber + " to buffer (buffer size now: " + buffer.size() + ")");

                // move to next file number 1..10
                nextFileNumber = nextFileNumber % 10 + 1;

                // sleep a bit to simulate production time
                Thread.sleep(700 + rand.nextInt(800));
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("[Producer] stopped.");
    }
}
