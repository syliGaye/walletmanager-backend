package org.sylvestre.projects.wallet.backend;

import org.springframework.scheduling.annotation.EnableAsync;
import org.sylvestre.projects.wallet.backend.application.bootstrap.BootstrapCommon;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.sylvestre.projects.wallet.backend.dao.domains.WmRoleDomain;

@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"org.sylvestre.projects"})
@EnableTransactionManagement
public class BackendWalletManagerApplication extends SpringBootServletInitializer {

	@Bean
	public CommandLineRunner initDatabase(
			WmRoleDomain roleDomain,
			JdbcTemplate jdbcTemplate
	) {
		return args -> {
			new BootstrapCommon().loadDatabase(roleDomain);

			/**
			//Create a Workbook instance and load an Excel file
			Workbook workbook = new Workbook();
			workbook.loadFromFile("src/main/resources/data/PLANNING_RMO.xlsx");

			//Set worksheets to fit to width when converting
			workbook.getConverterSetting().setSheetFitToWidth(true);

			//Get the first worksheet
			Worksheet worksheet = workbook.getWorksheets().get(0);

			//Convert to PDF and save the resulting document to a specified path
			worksheet.saveToPdf("src/main/resources/data/WorksheetToPdf.pdf");
			**/






			/**
			//First we read the Excel file in binary format into FileInputStream
			FileInputStream input_document = new FileInputStream(new File("src/main/resources/data/Test.xlsx"));
			// Read workbook into HSSFWorkbook
			XSSFWorkbook my_xls_workbook = new XSSFWorkbook(input_document);
			// Read worksheet into HSSFSheet
			XSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
			// To iterate over the rows
			Iterator<Row> rowIterator = my_worksheet.iterator();
			//We will create output PDF document objects at this point
			Document iText_xls_2_pdf = new Document();
			PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream("src/main/resources/data/Excel2PDF_Output.pdf"));
			iText_xls_2_pdf.open();
			//we have two columns in the Excel sheet, so we create a PDF table with two columns
			//Note: There are ways to make this dynamic in nature, if you want to.
			PdfPTable my_table = new PdfPTable(2);
			//We will use the object below to dynamically add new data to the table
			PdfPCell table_cell;
			//Loop through rows.
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next(); //Fetch CELL
					switch(cell.getCellType()) { //Identify CELL type
						//you need to add more code here based on
						//your requirement / transformations
						case Cell.CELL_TYPE_STRING:
							//Push the data from Excel to PDF Cell
							table_cell=new PdfPCell(new Phrase(cell.getStringCellValue()));
							//feel free to move the code below to suit to your needs
							my_table.addCell(table_cell);
							break;
					}
					//next line
				}

			}
			//Finally add the table to PDF document
			iText_xls_2_pdf.add(my_table);
			iText_xls_2_pdf.close();
			//we created our pdf file..
			input_document.close(); //close xls

			System.out.println("Je suis ici");
			**/
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendWalletManagerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
