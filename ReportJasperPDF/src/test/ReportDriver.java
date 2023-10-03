package test;


import java.sql.*;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.JasperReport;
import java.io.OutputStream;

/**
 * Driver program to connect to a database and to view a jasper report (.jrxml)
 * @author Oguzhan Topsakal
 * @since 23 March 2006
 *
 * Required jar files to run this class:
 * 1. jasperreports-1.2.0.jar
 * 2. classes12.jar (for Oracle JDBC connection)
 * 3. commons-beanutils-1.5.jar
 * 4. commons-collections-2.1.jar
 * 5. commons-digester-1.7.jar
 * 6. commons-logging-1.0.2.jar
 *
 */

public class ReportDriver {
    
    
    /**
     * Constructor for ReportDriver
     */
    public ReportDriver() {
        
    }
    
    /**
     * Takes 3 parameters: databaseName, userName, password
     * and connects to the database.
     * @param databaseName holds database name,
     * @param userName holds user name
     * @param password holds password to connect the database,
     * @return Returns the JDBC connection to the database
     */
    public static Connection connectDB(String databaseName, String userName, String password) {
        Connection jdbcConnection = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            jdbcConnection = DriverManager.getConnection(databaseName,userName,password);
        }catch(Exception ex) {
            String connectMsg = "Could not connect to the database: " + ex.getMessage() + " " + ex.getLocalizedMessage();
            System.out.println(connectMsg);
            ex.printStackTrace();
        }
        return jdbcConnection;
    }
    
    /**
     * Takes 4 parameters: databaseName, userName, password, reportFileLocation
     * and connects to the database and prepares and views the report.
     * @param databaseName holds database name,
     * @param userName holds user name
     * @param password holds password to connect the database,
     * @param reportFile holds the location of the Jasper Report file (.jrxml)
     */
    public static void runReport(String databaseName, String userName, String password,String reportFile) {
        try{
            JasperDesign jasperDesign = JRXmlLoader.load(reportFile);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            Connection jdbcConnection = connectDB(databaseName, userName, password);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, jdbcConnection);
            //JasperViewer.viewReport(jasperPrint);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/kamalesh/test.pdf");
            System.out.println("Completed....");
        }catch(Exception ex) {
            String connectMsg = "Could not create the report " + ex.getMessage() + " " + ex.getLocalizedMessage();
            System.out.println(connectMsg);
        }
    }
    
    /**
     * Uses runReport method to connect to the database and to prepare and view the report.
     * @param args Takes 4 arguments as an input: databaseName, userName, password, reportFileLocation
     * args[0] holds database name,
     * args[1] holds user name
     * args[2] holds password to connect the database,
     * args[3] holds the location of the Jasper Report file (.jrxml)
     */
    public static void main(String[] args) {
     /*  if (args.length == 4) {
            String databaseName = args[0] ;
            String userName = args[1];
            String password = args[2];
            String reportFile = args[3];*/
    	try {
			String databaseName = "jdbc:mysql://localhost:3306/myjasper" ;
			String userName = "root";
			String password = "";
			String reportFile = "new_report.jrxml";
			    runReport(databaseName, userName, password, reportFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      /*  }else{
            System.out.println("Usage:");
            System.out.println("java ReportDriver databaseName userName password reportFileLocation");
        }*/
        return;
        
    }
}
