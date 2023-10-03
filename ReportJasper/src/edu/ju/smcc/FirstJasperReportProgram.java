package edu.ju.smcc;

/** 
 * FirstJasperReportProgram.java 
 */  
import java.sql.Connection;  
import java.sql.DriverManager;  
  
import net.sf.jasperreports.engine.JasperCompileManager;  
import net.sf.jasperreports.engine.JasperFillManager;  
import net.sf.jasperreports.engine.JasperPrint;  
import net.sf.jasperreports.engine.JasperReport;  
import net.sf.jasperreports.engine.design.JasperDesign;  
import net.sf.jasperreports.engine.xml.JRXmlLoader;  
import net.sf.jasperreports.view.JasperViewer;  
  
/** 
 * @author www.javaworkspace.com 
 *  
 */  
public class FirstJasperReportProgram {  
  
    public static Connection getConnection() {  
        Connection jdbcConnection = null;  
        try {  
            Class.forName("com.mysql.jdbc.Driver");  
            jdbcConnection = DriverManager.getConnection(  
                    "jdbc:mysql://localhost:3306/myjasper", "root",  
                    "");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return jdbcConnection;  
    }  
  
    /** 
     * @param reportFile 
     */  
    public static void runReport(String reportFile) {  
        try {  
            JasperDesign jasperDesign = JRXmlLoader.load(reportFile);  
            JasperReport jasperReport = JasperCompileManager  
                    .compileReport(jasperDesign);  
            Connection jdbcConnection = getConnection();  
            JasperPrint jasperPrint = JasperFillManager.fillReport(  
                    jasperReport, null, jdbcConnection);  
            JasperViewer.viewReport(jasperPrint);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public static void main(String[] args) {  
        String reportFile = "reports/Report1.jrxml";  
        runReport(reportFile);  
    }  
}  