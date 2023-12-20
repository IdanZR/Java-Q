import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EncryptionApp extends JFrame {

    private File selectedFile;
    private JFileChooser fileChooser;
    private JButton chooseFileButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JRadioButton algorithm1RadioButton;
    private JRadioButton algorithm2RadioButton;
    private JRadioButton algorithm3RadioButton;

    public EncryptionApp() {
        setTitle("Encryption App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(54, 57, 63));
        JLabel headerLabel = new JLabel("Encryption App");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(255, 255, 255));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel bodyPanel = new JPanel();
        bodyPanel.setBackground(new Color(44, 47, 51));
        bodyPanel.setLayout(new GridLayout(4, 1));
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        algorithm1RadioButton = createRadioButton("Algorithm 1");
        algorithm2RadioButton = createRadioButton("Algorithm 2");
        algorithm3RadioButton = createRadioButton("Algorithm 3");

        ButtonGroup algorithmGroup = new ButtonGroup();
        algorithmGroup.add(algorithm1RadioButton);
        algorithmGroup.add(algorithm2RadioButton);
        algorithmGroup.add(algorithm3RadioButton);

        bodyPanel.add(algorithm1RadioButton);
        bodyPanel.add(algorithm2RadioButton);
        bodyPanel.add(algorithm3RadioButton);

        chooseFileButton = createButton("Choose File");
        encryptButton = createButton("Encrypt");
        decryptButton = createButton("Decrypt");

        bodyPanel.add(chooseFileButton);
        bodyPanel.add(encryptButton);
        bodyPanel.add(decryptButton);
        add(bodyPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(54, 57, 63));
        JLabel footerLabel = new JLabel("Encryption App");
        footerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        footerLabel.setForeground(new Color(72, 69, 69));
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        // Add action listeners
        chooseFileButton.addActionListener(e -> chooseFile());
        encryptButton.addActionListener(e -> {
            try {
                encrypt();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        decryptButton.addActionListener(e -> {
            try {
                decrypt();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        algorithm1RadioButton.addMouseListener(new RadioButtonHoverListener());
        algorithm2RadioButton.addMouseListener(new RadioButtonHoverListener());
        algorithm3RadioButton.addMouseListener(new RadioButtonHoverListener());
    }

    private JRadioButton createRadioButton(String text) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(new Font("Arial", Font.PLAIN, 16));
        radioButton.setForeground(new Color(255, 255, 255));
        radioButton.setBackground(new Color(44, 47, 51));
        radioButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return radioButton;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(new Color(0, 0, 0));
        button.setBackground(new Color(114, 137, 218));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void chooseFile() {
        fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "File selected: " + selectedFile.getName());
        } else {
            JOptionPane.showMessageDialog(this, "File selection canceled.");
        }
    }

    private void encrypt() throws FileNotFoundException {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please select a file.");
            return;
        }

        if (algorithm1RadioButton.isSelected()) {
            KnightTour2.encryptFile(selectedFile, "easyEncrypt");
        } else if (algorithm2RadioButton.isSelected()) {
            Graph.encryptFile(selectedFile, "easyEncrypt");
        } else if (algorithm3RadioButton.isSelected()) {
            NonDecreasingSeq.encryptFile(selectedFile, "easyEncrypt");
        } else {
            JOptionPane.showMessageDialog(this, "Please select an algorithm.");
        }
    }

    private void decrypt() throws IOException {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please select a file.");
            return;
        }

        if (algorithm1RadioButton.isSelected()) {
            KnightTour2.decryptFile(selectedFile,"easyDecrypt");
        } else if (algorithm2RadioButton.isSelected()) {
            Graph.decryptFile(selectedFile,"easyDecrypt");
        } else if (algorithm3RadioButton.isSelected()) {
            NonDecreasingSeq.decryptFile(selectedFile,"easyDecrypt");
        } else {
            JOptionPane.showMessageDialog(this, "Please select an algorithm.");
        }
    }

    private class RadioButtonHoverListener extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            radioButton.setForeground(new Color(114, 137, 218));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            radioButton.setForeground(new Color(255, 255, 255));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EncryptionApp().setVisible(true);
        });
    }
}
