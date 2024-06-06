package UDP;
import java.io.*;
import java.net.*;

public class ServidorUDP {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            // Criando um socket UDP na porta 6999
            socket = new DatagramSocket(6999);

            // Aguardando a chegada de pacotes
            byte[] buffer = new byte[1024];
            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
            System.out.println("Servidor aguardando mensagens...");

            // Recebendo o pacote do cliente
            socket.receive(pacote);

            // Extraindo os dados recebidos e exibindo-os
            String mensagem = new String(pacote.getData(), 0, pacote.getLength());
            System.out.println("Mensagem recebida do cliente: " + mensagem);

            // Preparando a resposta
            String resposta = "Ola, cliente";
            byte[] dadosResposta = resposta.getBytes();

            // Criando um pacote com a resposta e enviando para o cliente
            InetAddress enderecoCliente = pacote.getAddress();
            int portaCliente = pacote.getPort();
            DatagramPacket pacoteResposta = new DatagramPacket(dadosResposta, dadosResposta.length, enderecoCliente, portaCliente);
            socket.send(pacoteResposta);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
