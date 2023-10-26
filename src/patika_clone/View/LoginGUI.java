package patika_clone.View;

import patika_clone.Helper.Config;
import patika_clone.Helper.Helper;
import patika_clone.Model.Operator;
import patika_clone.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends  JFrame{
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    public  JTextField field_user_username_pb;
    public JPasswordField field_user_password_pb;
    private JButton button_login;


    public LoginGUI(){
        add(wrapper);
        setSize(500,500);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        setResizable(false);

        button_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(field_user_username_pb)|| Helper.isFieldEmpty(field_user_password_pb)){
                Helper.showMsg("fill");
            }else {
                User u = User.getFetch(field_user_username_pb.getText(),field_user_password_pb.getText());
                if (u == null){
                    Helper.showMsg("Wrong username or password !");
                }else {
                    switch (u.getType()){
                        case "operator":
                        OperatorGUI opGUI = new OperatorGUI((Operator) u,u.getId());
                            break;
                        case "educator":
                            EducatorGUI edGUI = new EducatorGUI(u.getId());
                            break;
                        case "student":
                            StudenGUI stGUI = new StudenGUI(u.getId());
                            break;
                    }
                    dispose();
                }

            }

        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI login = new LoginGUI();
    }

    public JTextField getField_user_username_pb() {
        return field_user_username_pb;
    }

    public void setField_user_username_pb(JTextField field_user_username_pb) {
        this.field_user_username_pb = field_user_username_pb;
    }

    public JPasswordField getField_user_password_pb() {
        return field_user_password_pb;
    }

    public void setField_user_password_pb(JPasswordField field_user_password_pb) {
        this.field_user_password_pb = field_user_password_pb;
    }
}
