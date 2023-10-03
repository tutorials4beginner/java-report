import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main2 {
  public static void main(String[] args) throws Exception {
    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
    Statement stmt = DriverManager.getConnection("jdbc:odbc:employee").createStatement();
    ResultSet rs = stmt
        .executeQuery("select lastname, firstname, id from [Sheet1$]");
    while (rs.next()) {
      String lname = rs.getString(1);
      String fname = rs.getString(2);
      int id = rs.getInt(3);
      System.out.println(fname + " " + lname + "  id : " + id);
    }
    rs.close();
    stmt.close();
  }
}