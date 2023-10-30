package patika_clone.Model;

import patika_clone.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Student extends User{
    public Student() {
    }

    public Student(int id, String name, String username, String password, String type) {
        super(id, name, username, password, type);
    }

    public static ArrayList<Patika> getListByPatika(){
        ArrayList<Patika> courseList = new ArrayList<>();
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM own_patika");
            while (rs.next()){
                Patika obj = new Patika(rs.getInt("patika_id"),rs.getString("name"));
                courseList.add(obj);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return courseList;
    }

    public static boolean add(int patika_id,String name){
        String  query = " INSERT INTO own_patika (patika_id,name) VALUES (?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,patika_id);
            pr.setString(2,name);
            return  pr.executeUpdate() !=-1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }
}
