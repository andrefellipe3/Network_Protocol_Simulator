import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;

public class AudioClient extends JFrame {
    private static final int PORT = 6999;
    private static final int BUFFER_SIZE = 1024; // Reduzido o tamanho do buffer para 1024 bytes
    private boolean running = false;

    public AudioClient() {
        setTitle("Cliente de Chamada de Áudio UDP");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton startButton = new JButton("Iniciar Chamada");
        JButton stopButton = new JButton("Parar Chamada");
        stopButton.setEnabled(false);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                startAudioTransmission();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                stopAudioTransmission();
            }
        });

        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(stopButton);
        add(panel);
        setVisible(true);
    }

    private void startAudioTransmission() {
        running = true;
        new Thread(new AudioSender()).start();
    }

    private void stopAudioTransmission() {
        running = false;
    }

    private class AudioSender implements Runnable 
    {
        public void run() 
        {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket();
                InetAddress serverAddress = InetAddress.getByName("localhost");
                AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);
                microphone.start();

                byte[] buffer = new byte[BUFFER_SIZE];

                while (running) {
                    int bytesRead = microphone.read(buffer, 0, buffer.length);
                    DatagramPacket packet = new DatagramPacket(buffer, bytesRead, serverAddress, PORT);
                    socket.send(packet);
                }

                microphone.stop();
                microphone.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AudioClient());
    }
}
