package patika_clone.Model;

import patika_clone.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Quiz {
    private int id;
    private int user_id;
    private String quiz;
    private String description;
    private Content content;
    public Quiz(int id, int user_id, String quiz, String description) {
        this.id = id;
        this.user_id = user_id;
        this.quiz = quiz;
        this.description = description;
        this.content = Content.getFetch(user_id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ArrayList<Quiz> getListByUser(int user_id){
        ArrayList<Quiz> courseList = new ArrayList<>();
        Quiz obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM quiz WHERE user_id ="+user_id);
            while (rs.next()){
                int id = rs.getInt("id");
                int userID =rs.getInt("user_id");
                String quiz = rs.getString("quiz");
                String description = rs.getString("description");
                obj = new Quiz(id,userID,quiz,description);
                courseList.add(obj);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return courseList;
    }

    public static boolean add(int id, int user_id,String quiz,String description){
        String  query = " INSERT INTO quiz (id, user_id,quiz,description) VALUES (?,?,?,?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            pr.setInt(2,user_id);
            pr.setString(3,quiz);
            pr.setString(4,description);
            return  pr.executeUpdate() !=-1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

}
