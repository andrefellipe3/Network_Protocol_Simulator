import java.io.*;
import java.net.*;

public class ClienteUDP {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            String mensagem = "Olá, servidor!";
            byte[] dados = mensagem.getBytes();
            InetAddress enderecoServidor = InetAddress.getByName("localhost");
            int portaServidor = 6999;

            DatagramPacket pacote = new DatagramPacket(dados, dados.length, enderecoServidor, portaServidor);
            socket.send(pacote);

            byte[] buffer = new byte[1024];
            DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);
            socket.receive(pacoteRecebido);

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
