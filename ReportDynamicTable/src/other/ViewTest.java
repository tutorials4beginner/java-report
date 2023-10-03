package other;
import java.util.HashMap;
import org.eclipse.birt.report.engine.api.*;

public class ViewTest {

	public static void main(String[] args) {
		// Variables used to control BIRT Engine instance
		EngineConfig conf = null;
		ReportEngine eng = null;
		IReportRunnable design = null;
		IRunAndRenderTask task = null;
		HTMLRenderContext renderContext = null;
		HashMap contextMap = null;
		HTMLRenderOption options = null;

		// Now, setup the BIRT engine configuration. The Engine Home is
		// hardcoded
		// here, this is probably better set in an environment variable or in
		// a configuration file. No other options need to be set
		conf = new EngineConfig();
		conf.setEngineHome("C:/birt_runtime/birt-runtime-2_1_0/ReportEngine");

		// Create new Report engine based off of the configuration
		eng = new ReportEngine(conf);

		// With our new engine, lets try to open the report design
		try {
			design = eng
					.openReportDesign("C:/birt_runtime/birt-runtime-2_1_0/ReportEngine/samples/hello_world.rptdesign");
		} catch (Exception e) {
			System.err
					.println("An error occured during the opening of the report file!");
			e.printStackTrace();
			System.exit(-1);
		}

		// With the file open, create the Run and Render task to run the report
		task = eng.createRunAndRenderTask(design);

		// Set Render context to handle url and image locataions, and apply to
		// the
		// task
		renderContext = new HTMLRenderContext();
		renderContext.setImageDirectory("image");
		contextMap = new HashMap();
		contextMap.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
				renderContext);
		task.setAppContext(contextMap);

		// This will set the output file location, the format to rener to, and
		// apply to the task
		options = new HTMLRenderOption();
		options.setOutputFileName("c:/temp/output.html");
		options.setOutputFormat("html");
		task.setRenderOption(options);

		// Cross our fingers and hope everything is set
		try {
			task.run();
		} catch (Exception e) {
			System.err.println("An error occured while running the report!");
			e.printStackTrace();
			System.exit(-1);
		}

		// Yeah, we finished. Now destroy the engine and let the garbage
		// collector
		// do its thing
		System.out.println("All went well. Closing program!");
		eng.destroy();
	}
}