package socket;

import model.ITStudent;
import util.XMLUtil;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class ConsumerSocketClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        new ConsumerSocketClient().start();
    }

    public void start() {
        System.out.println("[CLIENT] Consumer Socket Client running...");

        while (true) {
            try (Socket socket = new Socket(HOST, PORT)) {

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                int fileNumber = dis.readInt();
                int xmlLength = dis.readInt();
                String xmlContent = dis.readUTF();

                // Save XML to file
                File f = new File("received_student" + fileNumber + ".xml");
                Files.write(f.toPath(), xmlContent.getBytes());

                // Read student from XML
                ITStudent student = XMLUtil.readStudentFromXML(f);

                // Output
                System.out.println("\n=== RECEIVED student" + fileNumber + " ===");
                System.out.println(student);

                // Delete after processing
                f.delete();

                // Send ACK
                dos.writeUTF("Received student" + fileNumber);
                dos.flush();

                Thread.sleep(2000); // simulate slower consumer (optional)

            } catch (Exception e) {
                System.out.println("[CLIENT] Waiting for server...");
                try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
            }
        }
    }
}
