package other;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
 
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IHTMLRenderOption;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
 
import com.samaxes.webreport.entities.WebReportEntity;
import com.samaxes.webreport.presentation.BirtEngine;
 
/**
* Actions related to the reports generation.
*
* @author Samuel Santos
* @version $Revision$
*/
public class WebReportActionBean implements ActionBean {
 
   private ActionBeanContext context;
 
   public ActionBeanContext getContext() {
       return context;
   }
 
   public void setContext(ActionBeanContext context) {
       this.context = context;
   }
 
   /**
    * Generates the html report.
    *
    * @return forward to the jsp page
    * @throws EngineException when opening report design or reunning the report
    * @throws SemanticException when changing properties of DesignElementHandle
    */
   @DefaultHandler
   public Resolution htmlReport() throws EngineException, SemanticException {
       ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
 
       // set output options
       IHTMLRenderOption options = new HTMLRenderOption();
       options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
       options.setOutputStream(reportOutput);
       options.setEmbeddable(true);
       options.setBaseImageURL(context.getRequest().getContextPath() + "/images");
       options.setImageDirectory(context.getServletContext().getRealPath("/images"));
       options.setImageHandler(new HTMLServerImageHandler());
       options.setMasterPageContent(false);
 
       generateReport(options);
 
       return new StreamingResolution("text/html", new ByteArrayInputStream(reportOutput.toByteArray()));
   }
 
   /**
    * Generates the PDF report.
    *
    * @return PDF file with the report output
    * @throws EngineException when opening report design or reunning the report
    * @throws SemanticException when changing properties of DesignElementHandle
    */
   public Resolution pdfReport() throws EngineException, SemanticException {
       ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
 
       // set output options
       IPDFRenderOption options = new PDFRenderOption();
       options.setOutputFormat(PDFRenderOption.OUTPUT_FORMAT_PDF);
       options.setOutputStream(reportOutput);
 
       generateReport(options);
 
       return new StreamingResolution("application/pdf", new ByteArrayInputStream(reportOutput.toByteArray()))
               .setFilename("WebReport.pdf");
   }
 
   /**
    * Generates the Excel report.
    *
    * @return Excel file with the report output
    * @throws EngineException when opening report design or reunning the report
    * @throws SemanticException when changing properties of DesignElementHandle
    */
   public Resolution xlsReport() throws EngineException, SemanticException {
       ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
 
       // set output options
       IRenderOption options = new RenderOption();
       options.setOutputFormat("xls");
       options.setOutputStream(reportOutput);
 
       generateReport(options);
 
       return new StreamingResolution("application/vnd.ms-excel", new ByteArrayInputStream(reportOutput.toByteArray()))
               .setFilename("WebReport.xls");
   }
 
   /**
    * Generates the Word report.
    *
    * @return Excel file with the report output
    * @throws EngineException when opening report design or reunning the report
    * @throws SemanticException when changing properties of DesignElementHandle
    */
   public Resolution docReport() throws EngineException, SemanticException {
       ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
 
       // set output options
       IRenderOption options = new RenderOption();
       options.setOutputFormat("doc");
       options.setOutputStream(reportOutput);
 
       generateReport(options);
 
       return new StreamingResolution("application/vnd.ms-word", new ByteArrayInputStream(reportOutput.toByteArray()))
               .setFilename("WebReport.doc");
   }
 
   /**
    * Generates the Powerpoint report.
    *
    * @return Excel file with the report output
    * @throws EngineException when opening report design or reunning the report
    * @throws SemanticException when changing properties of DesignElementHandle
    */
   public Resolution pptReport() throws EngineException, SemanticException {
       ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
 
       // set output options
       IRenderOption options = new RenderOption();
       options.setOutputFormat("ppt");
       options.setOutputStream(reportOutput);
 
       generateReport(options);
 
       return new StreamingResolution("application/vnd.ms-powerpoint", new ByteArrayInputStream(reportOutput
               .toByteArray())).setFilename("WebReport.ppt");
   }
 
   /**
    * Generates the Postscript report.
    *
    * @return Excel file with the report output
    * @throws EngineException when opening report design or reunning the report
    * @throws SemanticException when changing properties of DesignElementHandle
    */
   public Resolution psReport() throws EngineException, SemanticException {
       ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
 
       // set output options
       IRenderOption options = new RenderOption();
       options.setOutputFormat("postscript");
       options.setOutputStream(reportOutput);
 
       generateReport(options);
 
       return new StreamingResolution("application/postscript", new ByteArrayInputStream(reportOutput.toByteArray()))
               .setFilename("WebReport.ps");
   }
 
   /**
    * Generates the report output.
    *
    * @return the report output
    * @throws EngineException when opening report design or reunning the report
    * @throws SemanticException when changing properties of DesignElementHandle
    */
   private void generateReport(IRenderOption options) throws EngineException, SemanticException {
       // this list simulates a call to the application business logic
       List<webReportEntity> webReportEntities = new ArrayList<webReportEntity>();
       webReportEntities.add(new WebReportEntity(new Double(2), "Product1"));
       webReportEntities.add(new WebReportEntity(new Double(4), "Product2"));
       webReportEntities.add(new WebReportEntity(new Double(7), "Product3"));
 
       // get the engine
       IReportEngine birtReportEngine = BirtEngine.getBirtEngine();
 
       // open the report design
       IReportRunnable design = birtReportEngine.openReportDesign(context.getServletContext().getRealPath("/reports")
               + "/webReport.rptdesign");
 
       // create task to run and render report
       IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);
       @SuppressWarnings("unchecked")
       Map<string, Object> appContext = task.getAppContext();
       DesignElementHandle reportChart = task.getReportRunnable().getDesignHandle().getModuleHandle().findElement(
               "reportChart");
 
       appContext.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, WebReportActionBean.class.getClassLoader());
       appContext.put("webReportEntities", webReportEntities);
       reportChart.setProperty("dataSet", "Scripted Data Set"); // change the chart dataset
 
       task.setLocale(context.getLocale());
       task.setRenderOption(options);
       task.setAppContext(appContext);
 
       // run report
       task.run();
       task.close();
   }
}