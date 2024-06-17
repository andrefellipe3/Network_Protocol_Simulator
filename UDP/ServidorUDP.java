package UDP;
import java.io.*;
import java.net.*;

public class ServidorUDP 
{
    public static void ServidorTesteUDP() 
    {
        DatagramSocket socket = null;
        try 
        {
            // Criando um socket UDP
            socket = new DatagramSocket(6999);
            System.out.println("Servidor UDP aguardando pacotes de dados...");

            byte[] buffer = new byte[1024];
            while (true) 
            {
                // Recebendo o pacote do cliente
                DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacoteRecebido);

                // Processando o pacote recebido
                System.out.println("Pacote de dados recebido do cliente");

                // Enviando uma confirmação de recebimento ao cliente
                String resposta = "Pacote recebido com sucesso!";
                byte[] respostaBytes = resposta.getBytes();
                DatagramPacket pacoteResposta = new DatagramPacket(respostaBytes, respostaBytes.length, 
                                                                   pacoteRecebido.getAddress(), pacoteRecebido.getPort());
                socket.send(pacoteResposta);
            }
        } catch (IOException e) 
        {
            e.printStackTrace();
        } finally {
            if (socket != null) 
            {
                socket.close();
            }
        }
    }
}
