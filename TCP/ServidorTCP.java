package TCP;
import java.io.*;
import java.net.*;

public class ServidorTCP {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        DataInputStream in = null;
        DataOutputStream out = null;

        try {
            // Criando um servidor TCP na porta 6698
            serverSocket = new ServerSocket(6698);
            System.out.println("Servidor aguardando conexão na porta 6698...");

            // Aceitando conexão de um cliente
            clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado.");

            // Criando streams de entrada e saída para comunicação com o cliente
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            // Lendo o nome do arquivo enviado pelo cliente
            String nomeArquivo = in.readUTF();

            // Lendo o tamanho do arquivo
            long tamanhoArquivo = in.readLong();
            File arquivoRecebido = new File("./" + nomeArquivo);
            FileOutputStream fileOutput = new FileOutputStream(arquivoRecebido);

            // Lendo o conteúdo do arquivo enviado pelo cliente e salvando em um arquivo local
            byte[] buffer = new byte[4096];
            int bytesLidos;
            long totalBytesLidos = 0;
            while (totalBytesLidos < tamanhoArquivo && (bytesLidos = in.read(buffer)) != -1) {
                fileOutput.write(buffer, 0, bytesLidos);
                totalBytesLidos += bytesLidos;
            }
            fileOutput.close();

            // Enviando confirmação para o cliente
            out.writeUTF("Arquivo recebido com sucesso.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
