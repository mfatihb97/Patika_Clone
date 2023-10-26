package patika_clone.View;

import patika_clone.Helper.Config;
import patika_clone.Helper.DBConnector;
import patika_clone.Helper.Helper;
import patika_clone.Helper.Item;
import patika_clone.Model.Content;
import patika_clone.Model.Course;
import patika_clone.Model.Quiz;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EducatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane panel_quiz;
    private JTable table_eduCourse;
    private JButton button_eduExit;
    private JPanel panel;
    private JTable table_eduContent;
    private JPanel panel_courses;
    private JPanel panel_contents;
    private JScrollPane scroll_courses;
    private JTextField field_header_add;
    private JTextField field_description_add;
    private JTextField field_youtube_add;
    private JScrollPane scroll_quiz;

    private JTextField field_language_add;
    private JButton button_content_add;
    private JTextField field_content_delete;
    private JButton button_content_delete;
    private JTable table_quiz;
    private JPanel panel_quizes;
    private JTextField field_quiz_question;
    private JComboBox combo_description;
    private JButton button_quiz_add;
    private JComboBox combo_content_lang;
    private JPanel panel_search;
    private JTextField field_search_header;
    private JTextField field_search_desc;
    private JButton searchButton;

    private DefaultTableModel model_eduCourse;
    private Object[] row_eduCourse;
    private int user_id;

    private  DefaultTableModel model_eduContent;
    private Object[] row_eduContent;
    private int id;
    private DefaultTableModel model_quiz;
    private Object[] row_quiz;
    private String language;

    public EducatorGUI(int user_id){

        this.user_id = user_id;

        add(wrapper);
        setSize(1000,500);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        model_quiz = new DefaultTableModel();
        Object[] col_quiz = {"ID","Quiz","Content"};
        model_quiz.setColumnIdentifiers(col_quiz);
        row_quiz = new Object[col_quiz.length];
        table_quiz.setModel(model_quiz);
        table_quiz.getTableHeader().setReorderingAllowed(false);
        loadQuiz();
        loadQuizCombo();

        model_eduCourse = new DefaultTableModel();
        Object[] col_eduCourses_list = {"Course Name","Course Language"};
        model_eduCourse.setColumnIdentifiers(col_eduCourses_list);
        row_eduCourse = new Object[col_eduCourses_list.length];
        table_eduCourse.setModel(model_eduCourse);
        table_eduCourse.getTableHeader().setReorderingAllowed(false);
        loadCourses();



        model_eduContent = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column ==0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_eduContents_list = {"ID","Header","Description","Youtube","Language"};
        model_eduContent.setColumnIdentifiers(col_eduContents_list);
        row_eduContent = new Object[col_eduContents_list.length];
        table_eduContent.setModel(model_eduContent);
        table_eduContent.getTableHeader().setReorderingAllowed(false);
        table_eduContent.getColumnModel().getColumn(0).setMaxWidth(60);
        loadContent();
        loadContentLangCombo();

        table_eduContent.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int id = Integer.parseInt(table_eduContent.getValueAt(table_eduContent.getSelectedRow(), 0).toString());
                String header = table_eduContent.getValueAt(table_eduContent.getSelectedRow(), 1).toString();
                String description = table_eduContent.getValueAt(table_eduContent.getSelectedRow(), 2).toString();
                String youtube = table_eduContent.getValueAt(table_eduContent.getSelectedRow(), 3).toString();
                String language = table_eduContent.getValueAt(table_eduContent.getSelectedRow(), 4).toString();
                if (Content.update(id, header, description, youtube,language)) {
                    Helper.showMsg("done");
                    loadContent();
                } else {
                    Helper.showMsg("error");
                }
            }

        });


        button_eduExit.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
        });
        button_content_add.addActionListener(e -> {
            if (field_description_add.getText().isEmpty() || field_header_add.getText().isEmpty() || field_youtube_add.getText().isEmpty()){
                Helper.showMsg("fill");
            }else {
                String header = field_header_add.getText();
                String description = field_description_add.getText();
                String youtube = field_youtube_add.getText();
                String language = combo_content_lang.getSelectedItem().toString();
                if (Content.add(id,user_id,header,description,youtube,language)){
                    Helper.showMsg("done");
                    loadContent();
                    loadQuizCombo();
                    field_header_add.setText(null);
                    field_youtube_add.setText(null);
                    field_description_add.setText(null);
                }else {
                    Helper.showMsg("error");
                }
            }
        });

        button_quiz_add.addActionListener(e -> {
            if (field_quiz_question.getText().isEmpty()|| combo_description.getSelectedItem().toString().isEmpty()){
                Helper.showMsg("fill");
            }else {
                String quiz = field_quiz_question.getText();
                String description = combo_description.getSelectedItem().toString();
                if (Quiz.add(id,user_id,quiz,description)){
                    Helper.showMsg("done");
                    loadQuiz();
                    loadQuizCombo();
                    field_quiz_question.setText(null);
                }else {
                    Helper.showMsg("error");
                }

            }

        });


        button_content_delete.addActionListener(e -> {
            if (Helper.isFieldEmpty(field_content_delete)){
                Helper.showMsg("fill");
            }else {
                int id = Integer.parseInt(field_content_delete.getText());
                if (Content.delete(id)){
                    Helper.showMsg("done");
                    loadContent();
                    field_content_delete.setText(null);
                }else{
                    Helper.showMsg("error");
                }
            }

        });


        searchButton.addActionListener(e -> {
            String header = field_search_header.getText();
            String description = field_search_desc.getText();
            String query = Content.searchQuery(header,description);
            ArrayList<Content> searchContent = Content.searchUserList(query);
            loadContent(Content.searchUserList(query));

        });
    }

    public void loadCourses() {
        ArrayList<Course> courseList = Course.getListByUser(user_id);
        if (courseList.isEmpty()){
            Helper.showMsg("You are not attached to any course!");
        }else {
            for (Course obj : courseList){
                int i=0;
                row_eduCourse[i++] = obj.getName();
                row_eduCourse[i++] = obj.getLanguage();
                model_eduCourse.addRow(row_eduCourse);
            }
       }
    }
    public void loadContent(){
        DefaultTableModel clearModel = (DefaultTableModel) table_eduContent.getModel();
        clearModel.setRowCount(0);
        ArrayList<Content> contentList = Content.getListByUser(user_id,language);
        if (contentList.isEmpty()){
            Helper.showMsg("You don't have any content active!");
        }else {
            for (Content con : contentList){
                int i = 0;
                row_eduContent[i++] = con.getId();
                row_eduContent[i++] = con.getHeader();
                row_eduContent[i++] = con.getDescription();
                row_eduContent[i++] = con.getyLink();
                row_eduContent[i++] = con.getLanguage();
                model_eduContent.addRow(row_eduContent);
            }
        }
    }

    public void loadQuiz(){
        DefaultTableModel clearModel = (DefaultTableModel) table_quiz.getModel();
        clearModel.setRowCount(0);
        ArrayList<Quiz> contentList = Quiz.getListByUser(user_id);
        if (contentList.isEmpty()){
            Helper.showMsg("Please insert a quiz for your contents!");
        }else {
            for (Quiz con : contentList){
                int i = 0;
                row_quiz[i++] = con.getId();
                row_quiz[i++] = con.getQuiz();
                row_quiz[i++] = con.getDescription();
                model_quiz.addRow(row_quiz);
                loadQuizCombo();
            }
        }

    }

    public void loadQuizCombo(){
        combo_description.removeAllItems();
        for(Content obj:Content.getQuiz(user_id)){
            combo_description.addItem(new Item(obj.getId(),obj.getDescription()));

        }
    }

    public void loadContentLangCombo(){
        combo_content_lang.removeAllItems();
        for (Course course : Course.getListByUser(user_id)){
            combo_content_lang.addItem(new Item(course.getId(),course.getLanguage()));
        }
    }

    public void loadContent(ArrayList<Content> list) {
        DefaultTableModel clearModel = (DefaultTableModel) table_eduContent.getModel();
        clearModel.setRowCount(0);
        ArrayList<Content> contentList = Content.getListByUser(user_id,language);
        if (contentList.isEmpty()){
            Helper.showMsg("You don't have any content active!");
        }else {
            for (Content con : list){
                int i = 0;
                row_eduContent[i++] = con.getId();
                row_eduContent[i++] = con.getHeader();
                row_eduContent[i++] = con.getDescription();
                row_eduContent[i++] = con.getyLink();
                row_eduContent[i++] = con.getLanguage();
                model_eduContent.addRow(row_eduContent);
            }
        }
    }

}
