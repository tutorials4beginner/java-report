package other;
import java.util.HashMap;
import java.util.logging.Level;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;

public class ExecuteReport {

	static void executeReport() throws EngineException {
		HashMap<String, Integer> parameters = new HashMap<String, Integer>();

		String name = "Top Count";
		Integer pvalue = new Integer(4);
		parameters.put(name, pvalue);

		IReportEngine engine = null;
		EngineConfig config = null;
		try {

			// Configure the Engine and start the Platform
			config = new EngineConfig();
			config.setEngineHome("C:/birt-runtime-2_1_1/birt-runtime-2_1_1/ReportEngine");
			// set log config using ( null, Level ) if you do not want a log
			// file
			config.setLogConfig("c:/birt/logs", Level.FINE);

			Platform.startup(config);
			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			engine = factory.createReportEngine(config);
			engine.changeLogLevel(Level.WARNING);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Configure the emitter to handle actions and images
		HTMLEmitterConfig emitterConfig = new HTMLEmitterConfig();
		emitterConfig.setActionHandler(new HTMLActionHandler());
		HTMLServerImageHandler imageHandler = new HTMLServerImageHandler();
		emitterConfig.setImageHandler(imageHandler);
		config.getEmitterConfigs().put("html", emitterConfig); //$NON-NLS-1$

		IReportRunnable design = null;

		// Open the report design
		design = engine
				.openReportDesign("C:/test/2.1/executereport/test.rptdesign");

		// Create task to run and render the report,
		IRunAndRenderTask task = engine.createRunAndRenderTask(design);

		// Set Render context to handle url and image locataions
		HTMLRenderContext renderContext = new HTMLRenderContext();
		// Set the Base URL for all actions
		renderContext.setBaseURL("http://localhost/");
		// Tell the Engine to prepend all images with this URL - Note this
		// requires using the HTMLServerImageHandler
		renderContext.setBaseImageURL("http://localhost/myimages");
		// Tell the Engine where to write the images to
		renderContext.setImageDirectory("C:/xampplite/htdocs/myimages");
		// Tell the Engine what image formats are supported. Note you must have
		// SVG in the string
		// to render charts in SVG.
		renderContext.setSupportedImageFormats("JPG;PNG;BMP;SVG");
		HashMap<String, HTMLRenderContext> contextMap = new HashMap<String, HTMLRenderContext>();
		contextMap.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
				renderContext);
		task.setAppContext(contextMap);
		// Set parameters for the report
		task.setParameterValues(parameters);
		// Alternatively set each seperately
		// task.setParameterValue("Top Count", new Integer(12));
		task.validateParameters();

		// Add a scriptable object, which will allow the report developer to put
		// script in the report that references this Java object. eg in script
		// pFilter.myjavamethod()
		// ProcessFilter pf = new ProcessFilter();
		// task.addScriptableJavaObject("pFilter", pf);

		// Set rendering options - such as file or stream output,
		// output format, whether it is embeddable, etc
		HTMLRenderOption options = new HTMLRenderOption();

		// Remove HTML and Body tags
		// options.setEmbeddable(true);

		// Set ouptut location
		options.setOutputFileName("C:/test/2.1/output.html");

		// Set output format
		options.setOutputFormat("html");
		task.setRenderOption(options);

		// run the report and destroy the engine
		// Note - If the program stays resident do not shutdown the Platform or
		// the Engine
		task.run();
		task.close();
		engine.shutdown();
		Platform.shutdown();
		System.out.println("Finished");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			executeReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}