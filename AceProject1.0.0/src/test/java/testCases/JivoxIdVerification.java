package testCases;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;


import io.github.bonigarcia.wdm.WebDriverManager;

public class JivoxIdVerification {	
	private static Logger log = LogManager.getLogger(JivoxIdVerification.class);
	@Test
	public void verifyingJivoxID() throws IOException {
		
		// TODO Auto-generated method stub
				WebDriverManager.firefoxdriver().setup();
				FirefoxOptions opt = new FirefoxOptions();							
				opt.addArguments("--headless");
				WebDriver driver = new FirefoxDriver(opt);
				log.info("Cookies deleted");	
				driver.manage().deleteAllCookies();				
				driver.get("http://qa64.jivox.com/tags/tagrocketb/");
				Long startTime=System.currentTimeMillis();
				FileInputStream fs = new FileInputStream( System.getProperty("user.dir")+"//DataFiles//Tags_US_N_A_Lubriderm_2023_DM Addressable Display_US_Lubriderm_DCM Tracking_MTK-TTD_031423.xls");
				ArrayList<String> dimJivoxID=new ArrayList<String>();
					//Creating a workbook 
					HSSFWorkbook workbook = new HSSFWorkbook(fs);
					HSSFSheet sheet = workbook.getSheetAt(0);
					  int rowCount = sheet.getPhysicalNumberOfRows()-sheet.getFirstRowNum();			 
					 	//Create a loop over all the rows of excel file to read it
					  	log.info("Loop to get cell values in a row");
						  int j=24;
						    for (int i = 11; i < rowCount+1; i++) {						  
						        HSSFRow currentrow = sheet.getRow(i);
						        if(currentrow.getCell(j)!= null) {
						        	//Create a loop to get cell values in a row						        	
						            String value1= currentrow.getCell(j).getStringCellValue();
						        	driver.findElement(By.xpath("//textarea[@id='input']")).sendKeys(value1);
						        	String jivoxId=driver.findElement(By.xpath("//td[@id='placementId']")).getText();
						        	String dimension=driver.findElement(By.xpath("//td[@id='unexpanded_dim']")).getText();
						        	String secureMsg=driver.findElement(By.xpath("//li[@class='message']")).getText();
						        	//String secureMsg=driver.findElement(By.xpath("//ul[@id='commonErrors']/li[@class='message'][2]")).getText();
						        	String dimSecMsg=dimension.concat("|"+ secureMsg);
						        	String verifiedJivoxDim=jivoxId.concat("|"+dimension);				        	
						        	dimJivoxID.add(verifiedJivoxDim);
						        	System.out.println(verifiedJivoxDim);
						        	log.info("JivoxID mapped in arraylist");
						        	//creating blank template
						        	Actions action = new Actions(driver);	        	
						        	action.moveToElement(driver.findElement(By.xpath("//ul[@id='template_option']"))).click(driver.findElement(By.xpath("//li[normalize-space()='Blank Template']"))).build().perform();
						        	
						        }
						    }
					
						log.info("Naming the cell");			    
					    int row = 10;	
					    int cellID = 26;
					    HSSFRow row1 = sheet.getRow(row);
					    row1.createCell(cellID).setCellValue("Dim_JivoxID");
					    int rowno= row+1;
					    
					    for (Object obj : dimJivoxID) {
					    	HSSFRow row2 = sheet.getRow(rowno++);					    	
			                Cell cell = row2.createCell(cellID);
			                cell.setCellValue((String)obj);
			            }				
						FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir")+"//DataFiles//Tags_US_N_A_Lubriderm_2023_DM Addressable Display_US_Lubriderm_DCM Tracking_MTK-TTD_031423.xls");
						log.info("ArrayList data written in excel");
						workbook.write(fos);				
						workbook.close();
						log.info("Workbook closed");
						Long endTime=System.currentTimeMillis();
						System.out.println((endTime-startTime)/60000+"min");
						//driver.quit();
			}



}
