package TCP;
import java.io.*;
import java.net.*;

public class ClienteTCP 
{

    public static void main(String[] args) 
    {
        Socket socket = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        String EnderecoIP = "";
        int porta = 0;
        try {

            // Criando um socket TCP e conectando-se ao servidor na porta 6698
            socket = new Socket(EnderecoIP, porta);

            // Criando streams de entrada e saída para comunicação com o servidor
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            // Caminho do arquivo a ser enviado
            String caminhoArquivo = "./TCP/arquivo.txt";
            File arquivo = new File(caminhoArquivo);

            if (arquivo.exists() && !arquivo.isDirectory())
            {
                FileInputStream fileInput = new FileInputStream(arquivo);

                // Enviando o nome do arquivo para o servidor
                out.writeUTF(arquivo.getName());

                // Enviando o tamanho do arquivo para o servidor
                out.writeLong(arquivo.length());

                // Enviando o conteúdo do arquivo para o servidor
                byte[] buffer = new byte[4096];
                int bytesLidos;
                while ((bytesLidos = fileInput.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesLidos);
                }
                fileInput.close();

                // Recebendo a resposta do servidor
                String respostaServidor = in.readUTF();
                System.out.println("Resposta do servidor: " + respostaServidor);
            } else {
                System.out.println("Arquivo não encontrado ou é um diretório.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
