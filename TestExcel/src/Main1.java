import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main1 {
  public static Connection getConnection() throws Exception {
    String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
    String url = "jdbc:odbc:excelDB";
    String username = "";
    String password = "";
    Class.forName(driver); // load JDBC-ODBC driver
    return DriverManager.getConnection(url, username, password);
  }

  public static void main(String args[]) throws Exception {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    conn = getConnection();
    stmt = conn.createStatement();
    String excelQuery = "insert into [Sheet1$](FirstName, LastName) values('A', 'K')";
    stmt.executeUpdate(excelQuery);

    rs.close();
    stmt.close();
    conn.close();
  }
}