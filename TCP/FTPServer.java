import java.io.*;
import java.net.*;
import javax.swing.JTextArea;

public class FTPServer implements Runnable {
    private int port;
    private JTextArea logArea;

    public FTPServer(int port, JTextArea logArea) {
        this.port = port;
        this.logArea = logArea;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logArea.append("FTP Server inicializou na porta " + port + "\n");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                logArea.append("Novo cliente conectou: " + clientSocket + "\n");
                new Thread(new FTPClientHandler(clientSocket, logArea)).start();
            }
        } catch (IOException e) {
            logArea.append("FTP Server Error: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private static class FTPClientHandler implements Runnable {
        private Socket socket;
        private JTextArea logArea;

        public FTPClientHandler(Socket socket, JTextArea logArea) {
            this.socket = socket;
            this.logArea = logArea;
        }

        @Override
        public void run() {
            try (DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                String fileName = in.readUTF();
                long fileSize = in.readLong();
                logArea.append("Arquivo Recebido: " + fileName + " (" + fileSize + " bytes)\n");

                File file = new File("server_files/" + fileName);
                file.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int read;
                    long remaining = fileSize;
                    while ((read = in.read(buffer, 0, Math.min(buffer.length, (int) remaining))) > 0) {
                        remaining -= read;
                        fos.write(buffer, 0, read);
                    }
                }

                logArea.append("Arquivo recebido: " + fileName + "\n");
                out.writeUTF("Arquivo recebido: " + fileName);

            } catch (IOException e) {
                logArea.append("Client Handler Error: " + e.getMessage() + "\n");
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
