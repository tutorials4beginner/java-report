import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Main3 {
  public static void main(String[] argv) throws Exception {
    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

    ResultSet rs = DriverManager.getConnection("jdbc:odbc:employee_xls")
        .createStatement().executeQuery("Select * from [Sheet1$]");

    ResultSetMetaData rsmd = rs.getMetaData();
    int numberOfColumns = rsmd.getColumnCount();

    System.out.println("No of cols " + numberOfColumns);
    while (rs.next()) {
      for (int i = 1; i <= numberOfColumns; i++) {
        String columnValue = rs.getString(i);
        System.out.println(columnValue);
      }
    }
    rs.close();
  }
}