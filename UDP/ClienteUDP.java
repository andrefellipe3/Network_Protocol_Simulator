package UDP;
import java.io.*;
import java.net.*;
import java.util.Random;

public class ClienteUDP {
    public static void ClienteUDPStreaming() 
    {
        DatagramSocket socket = null;
        try {
            // Criando um socket UDP
            socket = new DatagramSocket();

            // Informações do servidor
            InetAddress enderecoServidor = InetAddress.getByName("localhost");
            int portaServidor = 6999;

            Random random = new Random();
            byte[] buffer = new byte[1024]; // Simula um pacote de dados de áudio/vídeo

            while (true) 
            {
                // Preenche o buffer com dados aleatórios para simular pacotes de áudio/vídeo
                random.nextBytes(buffer);

                // Criando um pacote UDP
                DatagramPacket pacote = new DatagramPacket(buffer, buffer.length, enderecoServidor, portaServidor);

                // Enviando o pacote para o servidor
                socket.send(pacote);
                System.out.println("Pacote de dados enviado");

                // Aguardando um pequeno intervalo antes de enviar o próximo pacote
                Thread.sleep(100); // 100ms intervalo entre pacotes (10 pacotes por segundo)
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
