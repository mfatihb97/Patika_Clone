package patika_clone.View;

import patika_clone.Helper.Config;
import patika_clone.Helper.Helper;
import patika_clone.Helper.Item;
import patika_clone.Model.Course;
import patika_clone.Model.Operator;
import patika_clone.Model.Patika;
import patika_clone.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {

    private  JPanel wrapper;
    private JTabbedPane button_eduExit;
    private JLabel label_welcome;
    private JPanel panel_top;
    private JButton button_exit;
    private JPanel panel_users;
    private JScrollPane scroll_userList;
    private JTable table_userList;
    private JPanel panel_user_form;
    private JTextField field_user_name;
    private JTextField field_user_username;
    private JLabel user_pass;
    private JTextField field_user_pass;
    private JComboBox cmb_reg_type;
    private JButton button_user_add;
    private JTextField field_user_id;
    private JButton button_user_delete;
    private JTextField field_search_name;
    private JTextField field_search_username;
    private JComboBox combo_search_user;
    private JButton button_search_user;
    private JPanel panel_patikalist;
    private JScrollPane scroll_patika_list;
    private JTable table_patika_list;
    private JPanel panel_patika_add;
    private JTextField field_patika_name;
    private JButton button_patika_add;
    private JPanel panel_user;
    private JPanel panel_courses;
    private JScrollPane scroll_course;
    private JTable table_courses;
    private JPanel panel_course_add;
    private JTextField field_course_name;
    private JTextField field_course_lang;
    private JComboBox combo_course_patika;
    private JComboBox combo_educators;
    private JButton button_course_add;
    private final Operator operator;
    private DefaultTableModel model_user_list;
    private Object[] row_user_list;

    private DefaultTableModel model_patika_list;
    private Object[]  row_patika_list;
    private JPopupMenu patikaMenu;
    private DefaultTableModel model_course_list;
    private Object[] row_course_list;
    private DefaultTableModel model_eduCourses;
    private Object[] row_eduCourses;
    private int user_login_id;



    public OperatorGUI(Operator operator,int user_login_id){
        this.operator = operator;
        this.user_login_id = user_login_id;
        Helper.setLayout();
        add(wrapper);
        setSize(1000,500);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        label_welcome.setText("Welcome : "+ operator.getName());


        //ModelUserlist
        model_user_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column==0){
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_user_list = {"ID","Name-Surname","Username","Password","Register Type"};
        model_user_list.setColumnIdentifiers(col_user_list);
        row_user_list = new Object[col_user_list.length];
        loadUserModel();
        table_userList.setModel(model_user_list);
        table_userList.getTableHeader().setReorderingAllowed(false);

        table_userList.getSelectionModel().addListSelectionListener(e -> {
            try{
                String selected_id = table_userList.getValueAt(table_userList.getSelectedRow(),0).toString();
                field_user_id.setText(selected_id);
            }catch (Exception exception){

            }

        });

        table_userList.getModel().addTableModelListener(e -> {
            if (e.getType()==TableModelEvent.UPDATE){
                int user_id =Integer.parseInt(table_userList.getValueAt(table_userList.getSelectedRow(),0).toString());
                String user_name = table_userList.getValueAt(table_userList.getSelectedRow(),1).toString();
                String user_username = table_userList.getValueAt(table_userList.getSelectedRow(),2).toString();
                String user_pass = table_userList.getValueAt(table_userList.getSelectedRow(),3).toString();
                String user_type = table_userList.getValueAt(table_userList.getSelectedRow(),4).toString();

                if (User.update(user_id,user_name,user_username,user_pass,user_type)){
                    Helper.showMsg("add");
                }
                loadUserModel();
                loadEducatorCombo();
                loadCourses();
            }

        });

        // PatikaList
        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Update");
        JMenuItem deleteMenu = new JMenuItem("Delete");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int select_id = Integer.parseInt(table_patika_list.getValueAt(table_patika_list.getSelectedRow(),0).toString());
            UpdatePatikaGUI updateGUI = new UpdatePatikaGUI(Patika.getFetch(select_id));
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {

                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourses();
                }
            });

        });

        deleteMenu.addActionListener(e -> {
            if (Helper.confirm("sure")){
                int select_id = Integer.parseInt(table_patika_list.getValueAt(table_patika_list.getSelectedRow(),0).toString());
                if (Patika.delete(select_id)){
                    Helper.showMsg("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadEducatorCombo();
                    loadCourses();
                }else {
                    Helper.showMsg("error");
                }
            }

        });

        model_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID","Patika name"};
        model_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();
        loadPatikaCombo();
        loadEducatorCombo();

        table_patika_list.setModel(model_patika_list);
        table_patika_list.setComponentPopupMenu(patikaMenu);
        table_patika_list.getTableHeader().setReorderingAllowed(false);
        table_patika_list.getColumnModel().getColumn(0).setMaxWidth(60);

        table_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = table_patika_list.rowAtPoint(point);
                table_patika_list.setRowSelectionInterval(selected_row,selected_row);

            }
        });

       //Course list

        model_course_list = new DefaultTableModel();
        Object[] col_courseList = {"ID","Course Name","Programming Language","Patika","Educator"};
        model_course_list.setColumnIdentifiers(col_courseList);
        row_course_list = new Object[col_courseList.length];
        loadCourses();
        loadEducatorCombo();
        table_courses.setModel(model_course_list);
        table_courses.getColumnModel().getColumn(0).setMaxWidth(75);
        table_courses.getTableHeader().setReorderingAllowed(false);
        loadPatikaCombo();
        loadEducatorCombo();


        // ## CourseList
        button_user_add.addActionListener(e -> {
            if(Helper.isFieldEmpty(field_user_name) || Helper.isFieldEmpty(field_user_username) || Helper.isFieldEmpty(field_user_pass)){
                Helper.showMsg("fill");

            }else {
                String name = field_user_name.getText();
                String username = field_user_username.getText();
                String password = field_user_pass.getText();
                String type = cmb_reg_type.getSelectedItem().toString();
                if (User.add(name,username,password,type)){
                    Helper.showMsg("add");
                    loadUserModel();
                    loadEducatorCombo();
                    field_user_name.setText(null);
                    field_user_username.setText(null);
                    field_user_pass.setText(null);
                }else {
                    Helper.showMsg("error");
                }
            }
        });
        button_user_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(field_user_id)){
                    Helper.showMsg("fill");
                }else {
                    if (Helper.confirm("sure")){
                        int user_id = Integer.parseInt(field_user_id.getText());
                        if (User.delete(user_id)){
                            Helper.showMsg("Selected ID deleted from the list!");
                            loadUserModel();
                            loadEducatorCombo();
                            loadCourses();
                            field_user_id.setText(null);
                        }else {
                            Helper.showMsg("error");
                        }
                    }
                }
            }
        });
        button_search_user.addActionListener(e -> {
            String name = field_search_name.getText();
            String username = field_search_username.getText();
            String type = combo_search_user.getSelectedItem().toString();
            String query = User.searchQuery(name,username,type);
            ArrayList<User> searchUser = User.searchUserList(query);
            loadUserModel(searchUser);

        });
        button_exit.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
        });
        button_patika_add.addActionListener(e -> {

            if (Helper.isFieldEmpty(field_patika_name)){
                Helper.showMsg("fill");

            }else {
                if (Patika.add(field_patika_name.getText())){
                    Helper.showMsg("add");
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadEducatorCombo();
                    field_patika_name.setText(null);
                }else {
                    Helper.showMsg("error");
                }

            }

        });
        button_course_add.addActionListener(e -> {
            Item patikaItem = (Item) combo_course_patika.getSelectedItem();
            Item userItem = (Item) combo_educators.getSelectedItem();
            if (Helper.isFieldEmpty(field_course_name)||Helper.isFieldEmpty(field_course_lang)){
                Helper.showMsg("fill");
            }else {
                if (Course.add(userItem.getKey(),patikaItem.getKey(),field_course_name.getText(),field_course_lang.getText())){
                    Helper.showMsg("done");
                    loadCourses();
                    field_course_lang.setText(null);
                    field_course_name.setText(null);
                }else  {
                    Helper.showMsg("error");
                }
            }


        });
    }

    private void loadCourses() {
        DefaultTableModel clearModel = (DefaultTableModel) table_courses.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Course obj : Course.getList()){
            i=0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLanguage();
            row_course_list[i++] = obj.getPatika().getName();
            row_course_list[i++] = obj.getEducator().getName();
            model_course_list.addRow(row_course_list);
        }

    }

    private void loadPatikaModel() {

        DefaultTableModel clearModel = (DefaultTableModel) table_patika_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Patika obj : Patika.getList()){
            i=0;
            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();
            model_patika_list.addRow(row_patika_list);
        }

    }

    public void loadUserModel(){
        DefaultTableModel clearModel = (DefaultTableModel) table_userList.getModel();
        clearModel.setRowCount(0);

        for (User obj : User.getList()){
            row_user_list[0]=obj.getId();
            row_user_list[1]=obj.getName();
            row_user_list[2]=obj.getUsername();
            row_user_list[3]=obj.getPassword();
            row_user_list[4]=obj.getType();
            model_user_list.addRow(row_user_list);
        }

    }

    public void loadPatikaCombo(){
        combo_course_patika.removeAllItems();
        for (Patika obj : Patika.getList()){
            combo_course_patika.addItem(new Item(obj.getId(),obj.getName()));
        }
    }

    public void loadEducatorCombo(){
        combo_educators.removeAllItems();
        for(User obj : User.getList()){
            if (obj.getType().equals("educator")){
                combo_educators.addItem(new Item(obj.getId(),obj.getName()));
            }
        }
    }
    public void loadUserModel(ArrayList<User> list){
        DefaultTableModel clearModel = (DefaultTableModel) table_userList.getModel();
        clearModel.setRowCount(0);

        for (User obj : list){
            row_user_list[0]=obj.getId();
            row_user_list[1]=obj.getName();
            row_user_list[2]=obj.getUsername();
            row_user_list[3]=obj.getPassword();
            row_user_list[4]=obj.getType();
            model_user_list.addRow(row_user_list);
        }

    }
}
