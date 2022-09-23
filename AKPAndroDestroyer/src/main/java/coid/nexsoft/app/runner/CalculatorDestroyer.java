package coid.nexsoft.app.runner;

import static org.testng.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.json.simple.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import coid.nexsoft.app.pageobject.CalcDestroyer;
import coid.nexsoft.app.utils.ExcelReader;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class CalculatorDestroyer {

	private static AndroidDriver<MobileElement> driver;
	private CalcDestroyer calcDestroyer;
	private ExcelReader excelReader;
	private Object [][] dDriven ;
	private int intColumnNums;
	private int intRowNums;
	private int intNumX=0;
	private int intNumNext = 0;
	private int intLoopCalc =0;
	private int intOperator = 0;
	private double doubResultExpected = 0.0;
	private double doubResultActual = 0.0;
	private Random rand ;
	
	
	@BeforeTest
	public void befTest()
	{
		
		try {
			
			rand = new Random();
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("deviceName", "G011A");
			capabilities.setCapability("uuid", "127.0.0.1:21513");
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("platformVersion", "7.1.2");
			capabilities.setCapability("appPackage", "com.google.android.calculator");
			capabilities.setCapability("appActivity", "com.android.calculator2.Calculator");
			
				driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
			
			String excelPath = "./data/DataDriven.xlsx";
			String sheetName = "Sheet1";
			excelReader = new ExcelReader(excelPath, sheetName);
			intRowNums = excelReader.getRowCount();
			intColumnNums = excelReader.getColCount();
			calcDestroyer = new CalcDestroyer(driver);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@DataProvider(name = "DataProviderFirst")
	public Object[][] dataDrivenPost()
	{
		dDriven = new Object[intRowNums][intColumnNums];
		
		Iterator<Row> rX = excelReader.getIter();
		int a=0;
		while(rX.hasNext())
		{
			Row rows = rX.next();
			for(int j=0;j<intColumnNums;j++)
			{
				dDriven[a][j] = excelReader.getCellData(a, j);
				System.out.println(dDriven[a][j]);
			}
			a++;
		}
		return dDriven;		
	}
	
	@Test(priority = 0,dataProvider="DataProviderFirst")
	public void testPost(String x) throws InterruptedException
	{		
		System.out.println("======================================================");
		System.out.println("Loop ke : -->>> " + x);
		intLoopCalc = rand.nextInt(10);
		System.out.println("Banyak proses-->>> " + intLoopCalc);
		intNumX = rand.nextInt(10);
		System.out.println("======================================================");
		for(int i=0;i<intLoopCalc;i++)
		{
			intNumNext = rand.nextInt(10);
			intOperator = rand.nextInt(5);
			
			if(i==0) {
				if(intOperator == 1) {
					doubResultExpected = calcDestroyer.initCalc(intNumX,intNumNext, intOperator);
					System.out.println(intNumX + "  " + "*" + "  " + intNumNext + " = " + doubResultExpected);
				}else if(intOperator == 2) {
					doubResultExpected = calcDestroyer.initCalc(intNumX,intNumNext, intOperator);
					System.out.println(intNumX + "  " + "/" + "  " + intNumNext + " = " + doubResultExpected);
				}else if(intOperator == 3) {
					doubResultExpected = calcDestroyer.initCalc(intNumX,intNumNext, intOperator);
					System.out.println(intNumX + "  " + "+" + "  " + intNumNext + " = " + doubResultExpected);
				}else {
					doubResultExpected = calcDestroyer.initCalc(intNumX,intNumNext, intOperator);
					System.out.println(intNumX + "  " + "-" + "  " + intNumNext + " = " + doubResultExpected);
				}
			}
			else
			{
				if(intOperator == 1) {
					System.out.print(doubResultExpected);
					doubResultExpected = calcDestroyer.getResult(doubResultExpected,intNumNext,intOperator);
					System.out.println( "  " + "*" + "  " + intNumNext + " = " + doubResultExpected);
				}else if(intOperator == 2) {
					System.out.print(doubResultExpected);
					doubResultExpected = calcDestroyer.getResult(doubResultExpected,intNumNext,intOperator);
					System.out.println( "  " + "/" + "  " + intNumNext + " = " + doubResultExpected);
				}else if(intOperator == 3) {
					System.out.print(doubResultExpected);
					doubResultExpected = calcDestroyer.getResult(doubResultExpected,intNumNext,intOperator);
					System.out.println( "  " + "+" + "  " + intNumNext + " = " + doubResultExpected);
				}else {
					System.out.print(doubResultExpected);
					doubResultExpected = calcDestroyer.getResult(doubResultExpected,intNumNext,intOperator);
					System.out.println( "  " + "-" + "  " + intNumNext + " = " + doubResultExpected);
				}
			}
			
//			if(i==0)
//			{
//				
//				doubResultExpected = calcDestroyer.initCalc(intNumX,intNumNext, intOperator);
//				System.out.println(intNumX + "  " + intOperator + "  " + intNumNext + " = " + doubResultExpected);
//				
//			}
//			else
//			{
//				System.out.print(doubResultExpected);
//				doubResultExpected = calcDestroyer.getResult(doubResultExpected,intNumNext,intOperator);
//				System.out.println("  " + intOperator + "  " + intNumNext + "  " + " = " + doubResultExpected);
//			}
			
			System.out.println("======================================================");
		}
		String hasil = calcDestroyer.getTxt();
		System.out.println("Nilai Sebelum di parsing : " + hasil);
		calcDestroyer.doEqualz();
		doubResultActual = calcDestroyer.getTxtResult();
		System.out.println("ACTUAL : "+doubResultActual+" --- EXPECTED : "+doubResultExpected);
		assertEquals(doubResultActual, doubResultExpected);
		calcDestroyer.clear();
		Thread.sleep(500);
		System.out.println();
	}
}