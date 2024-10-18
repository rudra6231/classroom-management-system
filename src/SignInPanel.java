import Authorization.Auth;
import SHA512.PasswordHasher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignInPanel extends JPanel implements ActionListener {
    PlaceholderTextField email, password;
    JButton enter;
    SignInPanel(){
        this.setBounds(0, 0, 450, 600);
        this.setBackground(Color.decode("#787878"));
        this.setLayout(null);

        // sign in or sign up Label
        JLabel sign = new JLabel("SIGN IN");
        sign.setBounds(145, 150, 200, 50);
        sign.setFont(new Font("Dialog", Font.BOLD, 30));
        sign.setForeground(Color.WHITE);
        this.add(sign);

        // Form input fields
        email = new PlaceholderTextField("EMAIL");
        email.setBounds(65, 230, 310, 30);
        password = new PlaceholderTextField("PASSWORD");
        password.setBounds(65, 280, 310, 30);
        this.add(email);
        this.add(password);

        // Form Join button
        enter = new JButton("JOIN");
        enter.setBounds(125, 400, 200, 35);
        enter.setFont(new Font("SansSerif", Font.BOLD, 18));
        enter.setBackground(Color.decode("#02CE8F"));
        enter.setForeground(Color.WHITE);
        enter.addActionListener(this);
        this.add(enter);

        // button to take initial focus, so that textfield placeholders can show
        JButton focusInitialButton = new JButton();
        focusInitialButton.setBounds(450, 0, 0, 0);
        focusInitialButton.requestFocusInWindow();
        this.add(focusInitialButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // ENTER BUTTON IS PRESSED
        if(e.getSource() == enter){
            String emailVal = email.getText();
            String passwordVal = password.getText();


            // Check if fields are empty
            if(emailVal.trim().isEmpty() || passwordVal.trim().isEmpty() || emailVal.equals("EMAIL") || passwordVal.equals("PASSWORD")){
                JOptionPane.showMessageDialog(null, "You left a text field empty!", "Submission Error", JOptionPane.ERROR_MESSAGE);
            }else{

                // Query hashed password and salt
                DBConnection db = new DBConnection();
                String passwordRet = db.getPasswordInfo(emailVal);
                Auth auth = db.getTeacherData(emailVal, passwordVal);



                if(passwordVal.equals(passwordRet)){
                    // Dispose Window
                    Window[] windows = Window.getWindows();
                    for(Window window : windows){
                        window.dispose();
                    }
                    // Open Main Window
                    Boolean isNotConfig = db.isNotConfigured();
                    new MainFrame(auth, isNotConfig);
                    if(isNotConfig){
                        new IntStrat(auth);
                    }
                    JOptionPane.showMessageDialog(null, "You have successfully Signed In.", null, JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "Email or Password is wrong try again!", "Authentication Error", JOptionPane.ERROR_MESSAGE);
                }
            };
        }
    }
}
