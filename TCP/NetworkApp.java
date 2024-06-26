import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class NetworkApp extends JFrame {
    private JTextArea logArea;
    private JTextField ipField;
    private JTextField portField;
    private JButton startFTPServerButton;
    private JButton startFTPClientButton;
    private JButton sendFileButton;
    private FTPClient ftpClient;
    private FTPServer ftpServer;

    public NetworkApp() {
        setTitle("FTP Aplicacao");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        JPanel controlPanel = new JPanel(new BorderLayout());
        ipField = new JTextField("localhost");
        portField = new JTextField("6698");
        JPanel addressPanel = new JPanel(new GridLayout(1, 4));
        addressPanel.add(new JLabel("IP:"));
        addressPanel.add(ipField);
        addressPanel.add(new JLabel("Port:"));
        addressPanel.add(portField);

        startFTPServerButton = new JButton("Inicializando FTP Server");
        startFTPClientButton = new JButton("Inicializando FTP Client");
        sendFileButton = new JButton("Enviar Arquivo");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(startFTPServerButton);
        buttonPanel.add(startFTPClientButton);
        buttonPanel.add(sendFileButton);

        controlPanel.add(addressPanel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        startFTPServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFTPServer();
            }
        });

        startFTPClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFTPClient();
            }
        });

        sendFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendFile();
            }
        });
    }

    private void startFTPServer() {
        logArea.append("Inicializando FTP Server...\n");
        int port = Integer.parseInt(portField.getText());
        ftpServer = new FTPServer(port, logArea);
        new Thread(ftpServer).start();
    }

    private void startFTPClient() {
        logArea.append("Inicializando FTP Cliente...\n");
        String ip = ipField.getText();
        int port = Integer.parseInt(portField.getText());
        ftpClient = new FTPClient(ip, port, logArea);
    }

private void sendFile() {
    if (ftpClient != null) {
        JFileChooser fileChooser = new JFileChooser();
        
        // Define o diretório inicial como o diretório de trabalho atual
        String userDir = System.getProperty("user.dir");
        fileChooser.setCurrentDirectory(new File(userDir));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            ftpClient.sendFile(filePath);
        }
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NetworkApp app = new NetworkApp();
            app.setVisible(true);
        });
    }
}
