import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import TCP.ClienteTCP;
import UDP.ClienteUDP;

public class Interface 
{
    private static File selectedFile;  // Variável para armazenar o arquivo selecionado

    public static void main(String[] args) {
        // Cria a janela
        JFrame frame = new JFrame("Interface Cliente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Cria o painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Cria um painel para os campos de entrada
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // Adiciona o painel de entrada ao painel principal
        panel.add(inputPanel, BorderLayout.CENTER);

        // Cria uma área de texto
        JTextArea textArea = new JTextArea();
        panel.add(new JScrollPane(textArea), BorderLayout.SOUTH);

        // Cria um painel para os botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Cria o ComboBox para seleção do protocolo
        String[] protocols = { "TCP", "UDP" };
        JComboBox<String> protocolComboBox = new JComboBox<>(protocols);
        protocolComboBox.setSelectedIndex(-1); // Define o ComboBox sem seleção inicial
        buttonPanel.add(protocolComboBox);

        // Cria o botão de envio
        JButton submitButton = new JButton("Enviar");
        submitButton.setVisible(false); // Esconde o botão inicialmente
        buttonPanel.add(submitButton);

        // Cria o botão de upload de arquivo
        JButton uploadButton = new JButton("Selecionar Arquivo");
        uploadButton.setVisible(false); // Esconde o botão inicialmente
        buttonPanel.add(uploadButton);

         // Cria o botão de Simular Streaming
         JButton UDPStreamingButton = new JButton("Simular Streaming de Audio/Video");
         UDPStreamingButton.setVisible(false); // Esconde o botão inicialmente
         buttonPanel.add(UDPStreamingButton);

        // Adiciona o painel de botões ao painel principal
        panel.add(buttonPanel, BorderLayout.NORTH);

        // Adiciona ação ao botão de upload de arquivo
        uploadButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                String userDir = System.getProperty("user.dir");
                String targetDir = userDir + File.separator;
                fileChooser.setCurrentDirectory(new File(targetDir));

                int result = fileChooser.showOpenDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    // Pega o arquivo selecionado
                    selectedFile = fileChooser.getSelectedFile();
                    // Exibe o caminho do arquivo na área de texto
                    textArea.append("Arquivo selecionado: " + selectedFile.getAbsolutePath() + "\n");
                }
            }
        });

        // Adiciona ação ao botão de envio
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    String selectedProtocol = (String) protocolComboBox.getSelectedItem();
                    if ("TCP".equals(selectedProtocol)) {
                        ClienteTCP.EnviarArquivoTCP(selectedFile);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Nenhum arquivo selecionado!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        

          // Adiciona ação ao botão de envio
          UDPStreamingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {

                    String selectedProtocol = (String) protocolComboBox.getSelectedItem();
                    if ("UDP".equals(selectedProtocol)) 
                    {
                        ClienteUDP.ClienteUDPStreaming();
                    }

            }
        });

        // Adiciona ação ao ComboBox para mostrar/esconder os botões
        protocolComboBox.addActionListener(new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String selectedProtocol = (String) protocolComboBox.getSelectedItem();
                boolean isTCPSelected = "TCP".equals(selectedProtocol);
                uploadButton.setVisible(isTCPSelected);
                submitButton.setVisible(isTCPSelected);
                boolean isUDPSelected = "UDP".equals(selectedProtocol);
                UDPStreamingButton.setVisible(isUDPSelected);
            }
        });

        // Adiciona o painel principal à janela
        frame.add(panel);

        // Torna a janela visível
        frame.setVisible(true);
    }
}
