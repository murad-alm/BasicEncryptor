import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Murad Al Moadamani
 */
public class GUI extends JFrame {
    private final int WIDTH = 650;
    private final int HEIGHT = 450;
    private JButton encryptButton;
    private JPanel plaintextPanel;
    private JPanel cipherPanel;
    private JPanel buttonsPanel;
    private JTextArea plainTextField;
    private JTextArea cipherField;
    private JComboBox<String> comboBox;
    private JLabel background;
    private Border border;
    private ImageIcon icon;
    private JLabel labelMessage;
    private JLabel labelCipher;
    private JButton encryptFileButton;
    private JButton decryptFileButton;

    public GUI (){
        new JFrame();
        initComponents();
        this.add(background);
        this.setIconImage(icon.getImage());
        this.setTitle("Basic Encryptor");
        this.pack();
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * This method is responsible for initializing all the components fot the GUI.
     */
    private void initComponents() {
        border = BorderFactory.createLineBorder(Color.WHITE);
        encryptButton = setUpJButton("Encrypt");
        encryptFileButton = setUpJButton("Encrypt File");
        decryptFileButton = setUpJButton("Decrypt File");
        icon = new ImageIcon(this.getClass().getClassLoader().getResource("icon.png"));
        plainTextField = setUpTextArea();
        cipherField = setUpTextArea();
        setUpActionListeners();
        setUpComboBox();

        try {
            BufferedImage img = ImageIO.read(this.getClass().getClassLoader().getResource("2.jpg"));
            Image scaledImage = img.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
            background = new JLabel(new ImageIcon(scaledImage));
        }
        catch (IOException e){e.printStackTrace();}

        labelMessage = new JLabel("Enter a Message to Encrypt");
        labelMessage.setForeground(Color.WHITE);
        labelCipher = new JLabel("Encrypted Message");
        labelCipher.setForeground(Color.WHITE);

        setUpJPanels();

        background.setLayout(new GridLayout(3, 1, 5, 1));
        background.add(plaintextPanel);
        background.add(buttonsPanel);
        background.add(cipherPanel);
    }

    /**
     * This method is responsible for initializing the textAreas for the GUI.
     * @return JTextArea
     */
    private JTextArea setUpTextArea(){
        JTextArea textArea = new JTextArea("", 5, 35);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setCaretColor(Color.WHITE);
        textArea.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10,10,10,10)));
        textArea.setForeground(Color.WHITE);
        textArea.setOpaque(false);
        return textArea;
    }

    /**
     * This method is responsible for initializing the buttons for the GUI.
     * @return JTextButton
     */
    private JButton setUpJButton(String label){
        JButton btn = new JButton(label);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(border);
        btn.setPreferredSize(new Dimension(90, 35));
        btn.setForeground(Color.WHITE);
        return btn;
    }

    /**
     * This method will add the ActtionListeners to the components of the GUI.
     */
    private void setUpActionListeners(){
        encryptButton.addActionListener(e -> {
            try{
                if(plainTextField.getText().equals("")){
                    JOptionPane.showMessageDialog(this, "Please enter a message to encrypt!");
                    cipherField.setText("");
                }
                else {
                    if (comboBox.getSelectedIndex() == 0) {
                        setCipherTextField(new DES().encrypt(plainTextField.getText()));
                    } else if (comboBox.getSelectedIndex() == 1) {
                        setCipherTextField(new AES().encrypt(plainTextField.getText()));
                    } else if (comboBox.getSelectedIndex() == 2) {
                        setCipherTextField(new RSA().encrypt(plainTextField.getText()));
                    }
                }
            }catch(Exception exception){exception.printStackTrace();}
        });

        encryptFileButton.addActionListener(e -> {
            try{
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(this);
                new FileEncryptor().encrypt(fileChooser.getSelectedFile()); //TODO: comboBox integration to choose algorithm
                System.out.println("File Encrypted Successfully!");
            }catch(Exception exception){exception.printStackTrace();}
        });

        decryptFileButton.addActionListener(e -> {
            try{
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(this);
                new FileDecryptor().decrypt(fileChooser.getSelectedFile()); //TODO: comboBox integration to choose algorithm
                System.out.println("File Decrypted Successfully!");
            }catch(Exception exception){exception.printStackTrace();}
        });
    }

    /**
     * This method is responsible for the setting up the look of the JComboBox component
     */
    private void setUpComboBox (){
        comboBox = new JComboBox<>(new String[]{"DES", "AES", "RSA"});
        comboBox.setUI(new BasicComboBoxUI(){
            @SuppressWarnings({"serial"})
            @Override
            protected ComboPopup createPopup() {
                return new BasicComboPopup(comboBox) {
                    {
                        this.setBorder(border);
                    }
                };
            }
            @Override
            protected JButton createArrowButton() {
                //---arrow button
                JButton result = new JButton();
                result.setBackground(new Color(0,0,0,200));
                result.setForeground(Color.WHITE);
                return result;
            }
        });
        comboBox.setBorder(border);
        comboBox.setBackground(new Color(0,0,0,0));
        comboBox.setOpaque(false);
        comboBox.setForeground(Color.BLACK);
        comboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel result = new JLabel(value);
            result.setOpaque(false);
            result.setBackground(new Color(0,0,0,0));
            result.setForeground(Color.BLACK);
            return result;
        });
        comboBox.setBounds(5, 35, 140, 25);
    }

    /**
     * This method is responsible for setting up the JPanels in the UI.
     */
    private void setUpJPanels(){
        plaintextPanel = new JPanel();
        cipherPanel = new JPanel();
        buttonsPanel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();

        plaintextPanel.setLayout (new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        plaintextPanel.add(labelMessage, gbc);
        gbc.gridy++;
        plaintextPanel.add(plainTextField, gbc);
        plaintextPanel.setOpaque(false);

        buttonsPanel.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 0.2;
        gbc.weightx = 0.01;
        buttonsPanel.add(encryptButton, gbc);
        gbc.gridx++;
        buttonsPanel.add(comboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        buttonsPanel.add(encryptFileButton, gbc);
        gbc.gridx++;
        buttonsPanel.add(decryptFileButton, gbc);
        buttonsPanel.setOpaque(false);

        cipherPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        cipherPanel.add(labelCipher, gbc);
        gbc.gridy++;
        cipherPanel.add(cipherField, gbc);
        cipherPanel.setOpaque(false);
    }

    /**
     * This method will set the text of the cipherTextField.
     * @param cipher the encrypted message String.
     */
    private void setCipherTextField(final String cipher){
        cipherField.setText(cipher);
    }
}
