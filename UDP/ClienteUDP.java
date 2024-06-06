package UDP;
import java.io.*;
import java.net.*;

public class ClienteUDP {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            // Criando um socket UDP
            socket = new DatagramSocket();

            // Preparando os dados a serem enviados
            String mensagem = "Olá, servidor!";
            byte[] dados = mensagem.getBytes();
            InetAddress enderecoServidor = InetAddress.getByName("localhost");
            int portaServidor = 6999;

            // Criando um pacote UDP
            DatagramPacket pacote = new DatagramPacket(dados, dados.length, enderecoServidor, portaServidor);

            // Enviando o pacote para o servidor
            socket.send(pacote);

            // Recebendo a resposta do servidor
            byte[] buffer = new byte[1024];
            DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);
            socket.receive(pacoteRecebido);

            // Exibindo a resposta recebida do servidor
            String resposta = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());
            System.out.println("Resposta do servidor: " + resposta);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
