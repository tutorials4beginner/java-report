package other;

import java.io.IOException;
import java.util.ArrayList; 

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignConfig; 
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;

import com.ibm.icu.util.ULocale;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Use the Design Engine API (model api) to design a report instead of the 
 * BIRT designer.  This class will generate a similar report as 
 * derby_sample.rptdesign that was created using the Eclipse BIRT designer.
 */
public class DerbySampleDE
{
	ReportDesignHandle designHandle = null;
	ElementFactory designFactory = null;
	StructureFactory structFactory = null;	

	public static SessionHandle setup()
	{

		//Configure the Engine and start the Platform
		DesignConfig config = new DesignConfig( );
		config.setProperty("BIRT_HOME", System.getProperty("BIRT_HOME"));
		IDesignEngine engine = null;
		try{
			Platform.startup( config );
			IDesignEngineFactory factory = (IDesignEngineFactory) Platform
			.createFactoryObject( IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY );
			engine = factory.createDesignEngine( config );

		}catch( Exception ex){
			ex.printStackTrace();
		}		
		SessionHandle session = engine.newSessionHandle( ULocale.ENGLISH ) ;
		return session;
	}

	public static ReportDesignHandle createReport(SessionHandle session)
	{
		// create a design or a template
		ReportDesignHandle reportDesignHandle = session.createDesign();

		return reportDesignHandle;
	}



	public static void main( String[] args )
	{
		try
		{
			String qry = "select  SAMP.DEPARTMENT.DEPTNAME,SAMP.EMPLOYEE.FIRSTNME,SAMP.EMPLOYEE.LASTNAME,SAMP.EMPLOYEE.JOB,CHAR(SAMP.EMPLOYEE.HIREDATE) as Hiredate,SAMP.EMPLOYEE.SALARY,SAMP.EMPLOYEE.BONUS,SAMP.EMPLOYEE.COMM"+
			" from SAMP.EMPLOYEE, SAMP.DEPARTMENT where SAMP.DEPARTMENT.DEPTNO = SAMP.EMPLOYEE.WORKDEPT order by SAMP.DEPARTMENT.DEPTNAME";
			String dataSourceName = "Sample Derby";
			String dataSetName ="Sample Derby Data Set";

			SessionHandle session = setup();
			ReportDesignHandle reportDesignHandle = createReport(session);

			addDataSource(reportDesignHandle,dataSourceName,"org.apache.derby.jdbc.EmbeddedDriver","jdbc:derby:C:/birt/demo/sample","APP","PWD");
			buildDataSet(reportDesignHandle,dataSetName,dataSourceName,qry);

			ElementFactory elementFactory = reportDesignHandle.getElementFactory();

			// Create Styles.
			StyleHandle labelStyle = elementFactory.newStyle( "headerLabel" );
			labelStyle.setProperty( StyleHandle.FONT_WEIGHT_PROP,
					DesignChoiceConstants.FONT_WEIGHT_BOLD );
			labelStyle.setProperty( StyleHandle.FONT_FAMILY_PROP, "Andalus" );
			labelStyle.setProperty( StyleHandle.COLOR_PROP, "red" );
			labelStyle.setProperty( StyleHandle.BACKGROUND_COLOR_PROP,"aqua");
			labelStyle.setProperty( StyleHandle.FONT_SIZE_PROP,"larger" );
			labelStyle.setProperty( StyleHandle.COLOR_PROP, "red" );//$NON-NLS-1$
			reportDesignHandle.getStyles( ).add( labelStyle );


			StyleHandle detailStyle = elementFactory.newStyle( "detailStyle" );
			detailStyle.setProperty( StyleHandle.FONT_WEIGHT_PROP,
					DesignChoiceConstants.FONT_WEIGHT_BOLD );
			detailStyle.setProperty( StyleHandle.BACKGROUND_COLOR_PROP,"#C0C0C0");
			detailStyle.setProperty( StyleHandle.TEXT_ALIGN_PROP,"center");
			PropertyHandle highlightRules = detailStyle.getPropertyHandle("highlightRules");


			HighlightRule rules = StructureFactory.createHighlightRule();
			rules.setOperator("eq");
			rules.setProperty("backgroundColor","#80FFFF");
			rules.setTestExpression("Total.runningCount() % 2");
			rules.setValue1("0");
			highlightRules.addItem(rules);
			reportDesignHandle.getStyles( ).add( detailStyle );
			StyleHandle dateStyle = elementFactory.newStyle( "dateStyle" );
			dateStyle.setDateTimeFormat("DD_MM_YYYY");
			dateStyle.setDateTimeFormatCategory("Medium Date");
			//dateStyle.setD
			reportDesignHandle.getStyles( ).add( dateStyle );


			TextItemHandle text = elementFactory.newTextItem("text");
			text.setContent("Employee Report");
			reportDesignHandle.getComponents().add(text);

			// create a new table in the report with 5 columns
			TableHandle table = elementFactory.newTableItem( "table", 5 );
			table.setWidth( "100%" );

			// associate the dataset for this table
			table.setDataSet( reportDesignHandle.findDataSet(dataSetName ) );

			// columns that are retrieved as part of the query in dataset
			String[] cols ={
					"DEPTNAME",
					"FIRSTNME",
					"LASTNAME",
					"JOB",
					"HIREDATE",
					"SALARY",
					"BONUS",
					"COMM"
			};

			// columns that are used in the report
			String[][] reportCols ={
					{"DEPTNAME","Dept Name"},
					{"LASTNAME","Last Name"},
					{"JOB","Job"},
					{"HIREDATE","Hire Date"},
					{"Total Income","Total Income"}
			};

			// Set Column Bindings
			PropertyHandle computedSet = table.getColumnBindings( ); 
			ComputedColumn  cs1 = null;

			// DEPTNAME expression.
			cs1 = StructureFactory.createComputedColumn();
			cs1.setName(reportCols[0][0]);
			cs1.setExpression("var newOutput = dataSetRow[\"DEPTNAME\"].substr(0,1);"+
					"var input = dataSetRow[\"DEPTNAME\"].substr(1).toLowerCase();"+
					"var j;"+
					"for(i = 0;i<=input.length; i++) {"+
					"if(input.charAt(i)==\" \") {"+
					"newOutput = newOutput + input.charAt(i) + input.charAt(i+1).toUpperCase();"+
					"j = i + 1;"+
			"}else {if(i!=j) {newOutput = newOutput + input.charAt(i);}}}newOutput;");
			computedSet.addItem(cs1);

			for( int i=1; i < cols.length; i++){
				cs1 = StructureFactory.createComputedColumn();
				cs1.setName(cols[i]);
				cs1.setExpression("dataSetRow[\"" + cols[i] + "\"]");
				computedSet.addItem(cs1);
			}

			// Computed Column
			cs1 = StructureFactory.createComputedColumn();
			cs1.setName("Total Income");
			cs1.setExpression("row[\"SALARY\"]+row[\"BONUS\"]+row[\"COMM\"]");
			computedSet.addItem(cs1);

			// get the table header
			RowHandle tableheader = (RowHandle) table.getHeader( ).get( 0 );

			// associate the columns in the header row.
			for( int i=0; i < reportCols.length; i++){
				LabelHandle label1 = elementFactory.newLabel( reportCols[i][1]);
				label1.setStyleName("headerLabel");
				label1.setText(reportCols[i][1]);
				CellHandle cell = (CellHandle) tableheader.getCells( ).get( i );
				cell.getContent( ).add( label1 );
			}

			// HighlightRules
			HighlightRule incomeRule = StructureFactory.createHighlightRule();
			incomeRule.setOperator("le");
			incomeRule.setProperty("backgroundColor","#FF0000");
			incomeRule.setProperty("fontStyle","italic");

			incomeRule.setTestExpression("row[\"Total Income\"]");
			incomeRule.setValue1("25000");
			
			// table detail
			RowHandle tabledetail = (RowHandle) table.getDetail( ).get( 0 );
			for( int i=0; i < reportCols.length; i++){
				CellHandle cell = (CellHandle) tabledetail.getCells( ).get( i );
				DataItemHandle data = elementFactory.newDataItem( "data_"+reportCols[i][0] );
				data.setResultSetColumn(reportCols[i][0]);
				if(reportCols[i][0].equalsIgnoreCase("LASTNAME") ||
						reportCols[i][0].equalsIgnoreCase("Total Income")	)
				{
					cell.getPropertyHandle("highlightRules").addItem(incomeRule);

				}
				if(reportCols[i][0].equalsIgnoreCase("HIREDATE"))
				{
					data.setStyleName("dateStyle");
					cell.setStyleName("dateStyle");
				}

				cell.getContent( ).add( data );
			}

			tabledetail.setStyleName("detailStyle");


			// format the date element , ie the fourth column.

			// Create a label
			LabelHandle label =  reportDesignHandle.getElementFactory().newLabel(null);

			// Set the properties for the heading
			label.setStringProperty("fontSize", "24pt");
			label.setStringProperty("fontWeight", "bold");
			label.setStringProperty("marginTop", "2pt");
			label.setStringProperty("marginBottom", "5pt");
			label.setStringProperty("textAlign", "center");
			label.setStringProperty("color","blue");
			// The text property
			label.setText("Employee Report 2");

			// get the body of the report
			SlotHandle body = reportDesignHandle.getBody();

			// add the report caption label to the body of the report
			body.add(label);

			// add the table 
			reportDesignHandle.getBody( ).add( table );
			reportDesignHandle.saveAs( "c:/BIRT/demo/derbysamplede.rptdesign" ); //$NON-NLS-1$
			reportDesignHandle.close( );

		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		catch ( SemanticException e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * Build data source. 
	 * @throws SemanticException
	 */
	public static void addDataSource(ReportDesignHandle hdl,String dsName,String driver,String url,String user, String pwd) 
	throws SemanticException
	{
		OdaDataSourceHandle dsHdl = (OdaDataSourceHandle)hdl.findDataSource(dsName);
		if (dsHdl == null )
		{
			dsHdl = hdl.getElementFactory().newOdaDataSource(
					dsName, "org.eclipse.birt.report.data.oda.jdbc" );
			// add the newly created data source
			hdl.getDataSources().add(dsHdl);
		}

		// update the properties of the datasource
		dsHdl.setProperty( "odaDriverClass",driver);
		dsHdl.setProperty( "odaURL", url);
		dsHdl.setProperty( "odaUser", user);
		dsHdl.setProperty( "odaPassword", pwd );

	}

	void print(String msg)
	{
		System.out.println(msg);
	}

	/**
	 * Build the query ( data set to use in the report).
	 */
	static void buildDataSet(ReportDesignHandle hdl,String dataSetName,String dataSourceName,String qry) 
	throws SemanticException
	{
		OdaDataSetHandle dataSetHandle = hdl.getElementFactory().newOdaDataSet( dataSetName,"org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet" );
		dataSetHandle.setDataSource( dataSourceName );
		dataSetHandle.setQueryText( qry );

		PropertyHandle computedSet = dataSetHandle.getPropertyHandle( OdaDataSetHandle.COMPUTED_COLUMNS_PROP );
		// Structure of the computed Column.
		ComputedColumn computedCol = StructureFactory.createComputedColumn();//"ComputedColumns");
		computedCol.setName("Total Income");
		computedCol.setExpression("row[\"SALARY\"]+row[\"BONUS\"]+row[\"COMM\"]");
		computedCol.setDataType("decimal");
		computedSet.addItem( computedCol );

		hdl.getDataSets( ).add( dataSetHandle );
	}

}
