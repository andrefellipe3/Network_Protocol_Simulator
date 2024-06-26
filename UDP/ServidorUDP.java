import java.io.*;
import java.net.*;

public class ServidorUDP {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(6999);
            byte[] buffer = new byte[1024];
            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
            System.out.println("Servidor UDP aguardando mensagens...");

            while (true) {
                socket.receive(pacote);

                String mensagem = new String(pacote.getData(), 0, pacote.getLength());
                System.out.println("Mensagem recebida do cliente: " + mensagem);

                String resposta = "Olá, cliente";
                byte[] dadosResposta = resposta.getBytes();

                InetAddress enderecoCliente = pacote.getAddress();
                int portaCliente = pacote.getPort();
                DatagramPacket pacoteResposta = new DatagramPacket(dadosResposta, dadosResposta.length, enderecoCliente, portaCliente);
                socket.send(pacoteResposta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
