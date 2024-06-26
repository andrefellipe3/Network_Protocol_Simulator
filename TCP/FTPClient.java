import java.io.*;
import java.net.*;
import javax.swing.JTextArea;

public class FTPClient {
    private String ip;
    private int port;
    private JTextArea logArea;

    public FTPClient(String ip, int port, JTextArea logArea) {
        this.ip = ip;
        this.port = port;
        this.logArea = logArea;
    }

    public void sendFile(String filePath) {
        new Thread(() -> {
            try (Socket socket = new Socket(ip, port);
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 FileInputStream fis = new FileInputStream(filePath)) {

                File file = new File(filePath);
                out.writeUTF(file.getName());
                out.writeLong(file.length());

                logArea.append("Enviando arquivo: " + file.getName() + " (" + file.length() + " bytes)\n");

                byte[] buffer = new byte[4096];
                int read;
                while ((read = fis.read(buffer)) > 0) {
                    out.write(buffer, 0, read);
                }

                logArea.append("Arquivo enviado: " + file.getName() + "\n");
                String response = in.readUTF();
                logArea.append("Resposta do servidor: " + response + "\n");

            } catch (IOException e) {
                logArea.append("FTP Client Error: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }).start();
    }
}
