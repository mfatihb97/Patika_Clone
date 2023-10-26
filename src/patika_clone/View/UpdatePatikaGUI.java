package patika_clone.View;

import patika_clone.Helper.Config;
import patika_clone.Helper.Helper;
import patika_clone.Model.Patika;

import javax.swing.*;

public class UpdatePatikaGUI extends JFrame {
    private  JPanel wrapper;
    private Patika patika;
    private  JButton button_update;
    private JTextField field_patika_name;

    public UpdatePatikaGUI(Patika patika) {
        this.patika = patika;
        add(wrapper);
        setSize(300,150);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        field_patika_name.setText(patika.getName());
        button_update.addActionListener(e -> {

            if (Helper.isFieldEmpty(field_patika_name)){
                Helper.showMsg("fill");
            }else {
                if (Patika.update(patika.getId(),field_patika_name.getText())){
                    Helper.showMsg("add");
                }
                dispose();
            }
        });
    }

}
