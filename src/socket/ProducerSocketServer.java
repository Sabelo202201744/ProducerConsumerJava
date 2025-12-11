package socket;

import model.ITStudent;
import util.XMLUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ProducerSocketServer {

    private static final int PORT = 5000;
    private final Random rand = new Random();
    private final String[] programmes = {"BSc IT", "Diploma IT", 
    "BSc Computer Science", "MSc IT"};
    private final String[] coursePool = {
            "Programming I", "Data Structures", "Operating Systems", "Databases",
            "Networks", "Software Eng", "Security", "AI", "Web Tech", "Mobile Dev"
    };

    public static void main(String[] args) {
        new ProducerSocketServer().start();
    }

    public void start() {
        System.out.println("[SERVER] Producer Socket Server started on port " + PORT);

        int fileNum = 1;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                System.out.println("[SERVER] Waiting for consumer to connect...");
                Socket socket = serverSocket.accept();
                System.out.println("[SERVER] Consumer connected.");

                // generate student
                ITStudent s = generateRandomStudent();

                // convert to XML string
                File temp = new File("temp.xml");
                XMLUtil.writeStudentToXML(s, temp);

                String xml = new String(java.nio.file.Files.readAllBytes(temp.toPath()));
                temp.delete();

                // send file number + XML
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                dos.writeInt(fileNum);          // send file number
                dos.writeInt(xml.length());     // send length
                dos.writeUTF(xml);              // send XML content
                dos.flush();

                System.out.println("[SERVER] Sent student" + fileNum + ".xml to consumer");

                // wait for ACK
                String ack = dis.readUTF();
                System.out.println("[SERVER] ACK from consumer: " + ack);

                fileNum = fileNum % 10 + 1;
                socket.close();

                Thread.sleep(1500); // produce every second 
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ITStudent generateRandomStudent() {
        String name = randomName();
        String id = String.format("%08d", rand.nextInt(100_000_000));
        String prog = programmes[rand.nextInt(programmes.length)];
        int numCourses = 4 + rand.nextInt(3);
        List<String> pool = new ArrayList<>(Arrays.asList(coursePool));
        Collections.shuffle(pool, rand);

        Map<String,Integer> cm = new LinkedHashMap<>();
        for (int i = 0; i < numCourses; i++) {
            cm.put(pool.get(i), rand.nextInt(101));
        }

        return new ITStudent(name, id, prog, cm);
    }

    private String randomName() {
        String[] first = {"Thabo","Nandi","Sipho","Lerato","Anele",
        "Kabelo","Mpho","Sanele","Zanele","Tumi"};
        String[] last = {"Dlamini","Motsa","Nkosi","Matsebula","Mamba",
        "Ndlovu","Mkhize","Hlophe"};
        return first[rand.nextInt(first.length)] + " " + last[rand.nextInt(last.length)];
    }
}
