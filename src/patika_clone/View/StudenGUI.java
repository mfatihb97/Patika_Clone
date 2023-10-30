package patika_clone.View;

import patika_clone.Helper.Config;
import patika_clone.Helper.Helper;
import patika_clone.Model.Course;
import patika_clone.Model.Patika;
import patika_clone.Model.Student;
import patika_clone.Model.StudentCourse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class StudenGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane pane_student;
    private JPanel panel_patika;
    private JScrollPane scroll_patika;
    private JTable table_stu_patika;
    private JPanel panel_your_patika;
    private JScrollPane scroll_your_patika;
    private JTable table_own_patika;
    private JTextField field_choose_patika;
    private JButton addButton;
    private JTable table_own_course;
    private DefaultTableModel model_stu_patika;
    private Object[] row_stu_patika;
    private int id;
    private DefaultTableModel model_own_patika;
    private Object[] row_own_patika;

    private DefaultTableModel model_own_courses;
    private Object[] row_own_courses;

    public StudenGUI(int user_id) {
        add(wrapper);
        setSize(500, 500);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        setResizable(false);

        model_stu_patika = new DefaultTableModel();
        Object[] col_stu_patika = {"ID", "Patika"};
        model_stu_patika.setColumnIdentifiers(col_stu_patika);
        row_stu_patika = new Object[col_stu_patika.length];
        table_stu_patika.setModel(model_stu_patika);
        table_stu_patika.getTableHeader().setReorderingAllowed(false);
        table_stu_patika.getColumnModel().getColumn(0).setMaxWidth(60);
        loadPatika();

        model_own_patika = new DefaultTableModel();
        Object[] col_own_patika = {"ID", "Patika"};
        model_own_patika.setColumnIdentifiers(col_own_patika);
        row_own_patika = new Object[col_own_patika.length];
        table_own_patika.setModel(model_own_patika);
        table_own_patika.getTableHeader().setReorderingAllowed(false);
        table_own_patika.getColumnModel().getColumn(0).setMaxWidth(60);
        loadOwnPatika();

        model_own_courses = new DefaultTableModel();
        Object[] col_own_courses = {"ID", "Patika ID","Course Name","Patika Name"};
        model_own_courses.setColumnIdentifiers(col_own_courses);
        row_own_courses = new Object[col_own_courses.length];
        table_own_course.setModel(model_own_courses);
        table_own_course.getTableHeader().setReorderingAllowed(false);
        table_own_course.getColumnModel().getColumn(0).setMaxWidth(60);
        loadOwnCourses();


        addButton.addActionListener(e -> {

            if (field_choose_patika.getText().isEmpty()){
                Helper.showMsg("fill");
            }else {
                int ID = Integer.parseInt(field_choose_patika.getText());
                String name = String.valueOf(Patika.getFetch(ID));
                if (Student.add(ID,name)){
                    Helper.showMsg("done");
                    loadOwnPatika();
                    field_choose_patika.setText(null);
                }
                StudentCourse.addCourse(StudentCourse.getFetch(ID));
                loadOwnCourses();
            }
        });
    }

    private void loadOwnCourses() {

        DefaultTableModel clearModel = (DefaultTableModel) table_own_course.getModel();
        clearModel.setRowCount(0);
        ArrayList<Course> courses = StudentCourse.getListByCourse();

        for (Course path : courses) {
            int i = 0;
            row_own_courses[i++] = path.getId();
            row_own_courses[i++] = path.getPatika_id();
            row_own_courses[i++] = path.getName();
            row_own_courses[i++] = path.getLanguage();
            model_own_courses.addRow(row_own_courses);
        }

    }

    public void loadOwnPatika() {
        DefaultTableModel clearModel = (DefaultTableModel) table_own_patika.getModel();
        clearModel.setRowCount(0);
        ArrayList<Patika> patika = Student.getListByPatika();

        for (Patika path : patika) {
            int i = 0;
            row_own_patika[i++] = path.getId();
            row_own_patika[i++] = path.getName();
            model_own_patika.addRow(row_own_patika);
        }


    }

    public void loadPatika() {
        DefaultTableModel clearModel = (DefaultTableModel) table_stu_patika.getModel();
        clearModel.setRowCount(0);
        ArrayList<Patika> patika = Patika.getListByUser();
        if (patika.isEmpty()) {
            Helper.showMsg("There is no path to choose :( Please contact with Patika to ask adding new courses :(");
        } else {
            for (Patika path : patika) {
                int i = 0;
                row_stu_patika[i++] = path.getId();
                row_stu_patika[i++] = path.getName();
                model_stu_patika.addRow(row_stu_patika);
            }
        }

    }
}
