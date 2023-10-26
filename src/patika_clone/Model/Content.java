package patika_clone.Model;

import patika_clone.Helper.DBConnector;
import patika_clone.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Content {
    private int id;
    private String header;
    private String description;
    private String yLink;
    private String language;
    private User educator;
    private int user_id;

    public Content(int id,int user_id, String header, String description, String yLink,String language) {
        this.id = id;
        this.header = header;
        this.description = description;
        this.yLink = yLink;
        this.language = language;
        this.user_id = User.getFetchInt(user_id);
    }

    public Content() {

    }

    public int getUser_id() {
        return user_id;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        this.educator = educator;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getyLink() {
        return yLink;
    }

    public void setyLink(String yLink) {
        this.yLink = yLink;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static ArrayList<Content> getListByUser(int user_id,String language){
        ArrayList<Content> courseList = new ArrayList<>();
        Content obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM content WHERE user_id ="+user_id);
            while (rs.next()){
                int contentID = rs.getInt("id");
                int ID = rs.getInt("user_id");
                String header = rs.getString("header");
                String description =rs.getString("description");
                String yLink = rs.getString("yLink");
                String lang = rs.getString("language");
                obj = new Content(contentID,ID,header,description,yLink,lang);
                courseList.add(obj);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return courseList;
    }
    public static boolean add(int id,int user_id,String header, String description, String yLink,String language){
        String query = "INSERT INTO content (id,user_id,header,description,yLink,language) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            pr.setInt(2, user_id);
            pr.setString(3,header);
            pr.setString(4,description);
            pr.setString(5,yLink);
            pr.setString(6,language);
            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public static boolean delete(int id){
        String query = "DELETE FROM content WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public static boolean update(int id,String header,String description,String yLink,String language){
        String query = "UPDATE content SET header=?,description=?,yLink=?,language=? WHERE id=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,header);
            pr.setString(2,description);
            pr.setString(3,yLink);
            pr.setString(4,language);
            pr.setInt(5,id);
            return  pr.executeUpdate() !=-1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    public static Content getFetch(int id){
        Content obj = null;
        String sql = "SELECT*FROM content WHERE id = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(sql);
            pr.setInt(1,id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new Content();
                obj.setId(rs.getInt("id"));
                obj.setUser_id(rs.getInt("user_id"));
                obj.setHeader(rs.getString("header"));
                obj.setDescription(rs.getString("description"));
                obj.setyLink(rs.getString("yLink"));
                obj.setLanguage(rs.getString("language"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  obj;
    }

    public static ArrayList<Content> searchUserList(String query){
        ArrayList<Content> contentList = new ArrayList<>();
        Content obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                obj = new Content();
                obj.setId(rs.getInt("id"));
                obj.setHeader(rs.getString("header"));
                obj.setDescription(rs.getString("description"));
                obj.setyLink(rs.getString("yLink"));
                obj.setLanguage(rs.getString("language"));
                contentList.add(obj);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contentList;
    }

    public static String searchQuery(String header  ,String description){
        String query = "SELECT * FROM content WHERE header LIKE '%{{header}}%' AND description LIKE '%{{description}}%'";
        query = query.replace("{{header}}",header);
        query = query.replace("{{description}}",description);
        return query;
    }

    public static ArrayList<Content> getQuiz(int user_id){
        ArrayList<Content> contentList= new ArrayList<>();
        String query = "SELECT * FROM content WHERE user_id=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,user_id);
            ResultSet rs = pr.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                int ID =rs.getInt("user_id");
                String header   = rs.getString("header");
                String description = rs.getString("description");
                String yLink = rs.getString("yLink");
                String language = rs.getString("language");
                Content obj = new Content(id,ID,header,description,yLink,language);
                contentList.add(obj);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return contentList;
    }



}
