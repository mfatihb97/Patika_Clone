package patika_clone;

import javax.swing.*;
import java.awt.*;

public class Example extends JFrame {
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    private JTextField field_user_name;
    private JPasswordField field_password;
    private JButton button_login;


    public Example(){

        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            if ("Nimbus".equals(info.getName())){
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        setContentPane(wrapper);
        setSize(400,400);
        setTitle("Patika Clone");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width)/2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height)/2;
        setLocation(x,y);
        setVisible(true);
        button_login.addActionListener(e -> {
            if (field_user_name.getText().length()==0 || field_password.getText().length()==0){
                JOptionPane.showMessageDialog(null,"Please fill the blanks!","Error!",JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }


}
