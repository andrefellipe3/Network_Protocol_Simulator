import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.util.Arrays;

public class AudioServer extends JFrame {
    private static final int PORT = 6596;
    private static final int BUFFER_SIZE = 1024; // Reduzido o tamanho do buffer para 1024 bytes
    private AudioPanel audioPanel;

    public AudioServer() {
        setTitle("Servidor de Áudio UDP");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        audioPanel = new AudioPanel();
        add(audioPanel);
        setVisible(true);

        new Thread(new AudioReceiver()).start();
    }

    private class AudioReceiver implements Runnable {
        public void run() {
            DatagramSocket socket = null;
            SourceDataLine audioOut = null;
            try {
                socket = new DatagramSocket(PORT);
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                audioOut = (SourceDataLine) AudioSystem.getLine(info);
                audioOut.open(format);
                audioOut.start();

                System.out.println("Servidor de áudio aguardando mensagens...");

                while (true) {
                    socket.receive(packet);
                    byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
                    audioOut.write(data, 0, packet.getLength());
                    System.out.println("Pacote recebido com " + packet.getLength() + " bytes");
                    audioPanel.updateAudioData(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (audioOut != null) {
                    audioOut.drain();
                    audioOut.close();
                }
                if (socket != null) {
                    socket.close();
                }
            }
        }
    }

    private class AudioPanel extends JPanel {
        private byte[] audioData = new byte[BUFFER_SIZE];

        public void updateAudioData(byte[] data) {
            audioData = data;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.GREEN);
            int middle = getHeight() / 2;
            for (int i = 0; i < audioData.length - 2; i += 2) {
                int x1 = i * getWidth() / (audioData.length / 2);
                int y1 = middle + (audioData[i] + audioData[i + 1]) / 2;
                int x2 = (i + 1) * getWidth() / (audioData.length / 2);
                int y2 = middle + (audioData[i + 2] + audioData[i + 3]) / 2;
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AudioServer());
    }
}
