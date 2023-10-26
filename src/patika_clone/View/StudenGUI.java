package patika_clone.View;

import patika_clone.Helper.Config;
import patika_clone.Helper.Helper;
import patika_clone.Model.Patika;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class StudenGUI  extends  JFrame{
    private JPanel wrapper;
    private JTabbedPane pane_student;
    private JPanel panel_patika;
    private JScrollPane scroll_patika;
    private JTable table_stu_patika;
    private JPanel panel_your_patika;
    private JScrollPane scroll_your_patika;
    private JTable table1;
    private DefaultTableModel model_stu_patika;
    private Object[] row_stu_patika;
    private int id;

    public StudenGUI(int user_id){
        add(wrapper);
        setSize(500,500);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        setResizable(false);

        model_stu_patika = new DefaultTableModel();
        Object[] col_stu_patika = {"ID","Patika"};
        model_stu_patika.setColumnIdentifiers(col_stu_patika);
        row_stu_patika = new Object[col_stu_patika.length];
        table_stu_patika.setModel(model_stu_patika);
        table_stu_patika.getTableHeader().setReorderingAllowed(false);
        table_stu_patika.getColumnModel().getColumn(0).setMaxWidth(60);
        loadPatika();
    }

    public void loadPatika(){
        DefaultTableModel clearModel = (DefaultTableModel) table_stu_patika.getModel();
        clearModel.setRowCount(0);
        ArrayList<Patika> patika = Patika.getListByUser();
        if (patika.isEmpty()){
            Helper.showMsg("There is no path to choose :( Please contact with Patika to ask adding new courses :(");
        }else {
            for (Patika path : patika){
                int i = 0;
                row_stu_patika[i++] = path.getId();
                row_stu_patika[i++] = path.getName();
                model_stu_patika.addRow(row_stu_patika);

            }
        }

    }
}
