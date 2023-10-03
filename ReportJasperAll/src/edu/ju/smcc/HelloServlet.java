package edu.ju.smcc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * 
 * @author Sunirmal Khatua
 *
 */
public class HelloServlet extends HttpServlet {
	public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException{
		String type=req.getParameter("type");
		String databaseName = "jdbc:mysql://localhost:3306/smmvs" ;
		String userName = "smmvs";
		String password = "smmvs";
		Connection conn = null;
		try{
			String reportFile = getServletContext().getRealPath("/")+"new_report.jrxml";
			System.out.println(reportFile);
            JasperDesign jasperDesign = JRXmlLoader.load(reportFile);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            conn = connectDB(databaseName, userName, password);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);
            ServletOutputStream out = resp.getOutputStream();
            JRExporter exporter = null;
            if(type.equals("pdf")){
            	resp.setContentType("application/pdf");
            	exporter = new JRPdfExporter();
            }else if(type.equals("rtf")){
            	resp.setContentType("application/rtf");
                exporter = new JRRtfExporter();
            }else if(type.equals("html")){
            	resp.setContentType("text/html");
                exporter = new JRHtmlExporter();
            }
           
            /*resp.setContentType("application/csv");
            JRExporter exporter = new JRCsvExporter();*/
            
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
            exporter.exportReport();
            out.flush();
            out.close();
        }catch (JRException e){
	    	// display stack trace in the browser
	    	StringWriter stringWriter = new StringWriter();
	    	PrintWriter printWriter = new PrintWriter(stringWriter);
	    	e.printStackTrace(printWriter);
	    	resp.setContentType("text/plain");
	    	resp.getOutputStream().print(stringWriter.toString());
    	}catch(Exception ex) {
            String connectMsg = "Could not create the report " + ex.getMessage() + " " + ex.getLocalizedMessage();
            System.out.println(connectMsg);
        }finally{
	    	//close the connection.
	    	if(conn != null){
	    		try { 
	    			conn.close(); 
	    		}catch (Exception ignored) {}
	    	}
	    }
	}
	public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException{
		doGet(req,resp);
	}
	
	
	
    public static Connection connectDB(String databaseUrl, String userName, String password) {
        Connection jdbcConnection = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            jdbcConnection = DriverManager.getConnection(databaseUrl,userName,password);
        }catch(Exception ex) {
            String connectMsg = "Could not connect to the database: " + ex.getMessage() + " " + ex.getLocalizedMessage();
            System.out.println(connectMsg);
            ex.printStackTrace();
        }
        return jdbcConnection;
    }
}
