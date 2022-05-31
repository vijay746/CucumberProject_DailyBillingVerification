package pages;

import Base.BaseUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class LoginPage extends BaseUtil {
    By searchText = By.xpath("//input[@placeholder='Search for products, brands and more']");
    By searchButton = By.xpath("//button[@class='vh79eN']");
    By category = By.xpath("//a[@class='_3XS1AH _32ZSYo']");
    By usrname = By.xpath("//*[@id=\"username\"]");
    By usrpaswd = By.xpath("//*[@id=\"password\"]");
    By signInButton = By.xpath("//button/div[contains(text(),'Sign In')]");


    public void enterUsrNameAndpasswd(String usrName, String passwd) {
        getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        findElement(usrname).clear();
        findElement(usrname).sendKeys(usrName);
        findElement(usrpaswd).clear();
        findElement(usrpaswd).sendKeys(passwd);
        findElement(signInButton).click();
    }

    public void navigateToUrl(String url) {
        getDriver().manage().window().maximize();
        getDriver().navigate().to(url);
    }

    By bsnesButton = By.xpath("//li[@role='listitem']/descendant::h2[contains(text(),\"Business\")]");

    public void clickBusiness() throws InterruptedException {
        Thread.sleep(4000);
        findElement(bsnesButton).click();
    }

    By clubSearchBox = By.xpath("//input[@id=\"searchInput\"]") ;
    public void searchClub(String clubNumr){
        findElement(clubSearchBox).sendKeys(clubNumr);
    }

    public void clickClub(String clubNumbr) throws InterruptedException {
        String xpathVal = "//td[@data-abc-id=\"number\" and contains(text(),'"+clubNumbr+"')]";
        By club = By.xpath(xpathVal);
        findElement(club).click();
    }

    By memButton = By.xpath("//a[@data-abc-id=\"navBarLink\" and contains(text(),'Members')]");

    public void clickMemberButton() {
        findElement(memButton).click();
    }

    By srchButton = By.xpath("//input[@id='memberListSearchInput']");
    By backButtonMemberSearch = By.xpath("//*[@class=\"ui-icon icon-arrow-left-thin\"]");
    By srchClearButton = By.xpath("//*[@data-abc-id='memberListSearchInputTooltip']//*[@data-abc-id='iconClose']");
    By memList = By.xpath("//*[@data-abc-id='memberList']");

    public String searchMemberAndGetMemType(String fName, String lName, String clubNum, String agrementNum) {
        int count = getDriver().findElements(memList).size();
        if(count==1){
            findElement(memList).click();
                if(1==getDriver().findElements(srchClearButton).size())
                    clearMemSearchBox();
        }
        clickMemberButton();
        findElement(srchButton).click();
        if(1==getDriver().findElements(memList).size()){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //findElement(srchClearButton).click();
            clearMemSearchBox();
        }

        // if agrementNum i.e. length of memberId is less than 2, search does not give expected result so search with First Name
        if(agrementNum.length() <=2){
            clearMemSearchBox();
            findElement(srchButton).sendKeys(fName);
        }else{
            clearMemSearchBox();
            findElement(srchButton).sendKeys(agrementNum);
        }
        String agrNumXpath = "";
        if(clubNum.length()==3){
            clubNum = "0"+clubNum;
        }
        agrementNum = agrementNum.replace(".0","");
        if(agrementNum.startsWith("B")){
            agrNumXpath = "//td[@data-abc-id='agreement']//span[contains(text(),\"0" +clubNum+""+agrementNum+"\")]";
        }else if(agrementNum.startsWith("0000")){
            // agrNumXpath = "//td[@data-abc-id='agreement']//span[contains(text(),\"00" +clubNum+""+agrementNum+"\")]";
            agrNumXpath= "//*[@data-abc-id='memberNameText' and contains(text(),'"+ fName +"')]";
        }else if(agrementNum.startsWith("000")) {
            agrNumXpath= "//*[@data-abc-id='memberNameText' and contains(text(),'"+ fName +"')]";
        }else if(agrementNum.length()==4){
            agrNumXpath = "//td[@data-abc-id='agreement']//span[contains(text(),\"00" +clubNum+"00"+agrementNum+"\")]";
        }else if(agrementNum.endsWith("B")){
            agrNumXpath = "//td[@data-abc-id='agreement']//span[contains(text(),\"00" +clubNum+""+agrementNum+"\")]";
        }else if(agrementNum.length()<4){
            if(agrementNum.length()==1)
                // agrNumXpath = "//td[@data-abc-id='agreement']//span[contains(text(),\"00" + clubNum + "00000" + agrementNum + "\")]";
                agrNumXpath= "//*[@data-abc-id='memberNameText' and contains(text(),'"+fName+"') and contains(text(),'" +lName+"')]";
            else if(agrementNum.length()==2)
                // agrNumXpath = "//td[@data-abc-id='agreement']//span[contains(text(),\"00" + clubNum + "0000" + agrementNum + "\")]";
                agrNumXpath= "//*[@data-abc-id='memberNameText' and contains(text(),'"+fName+"') and contains(text(),'"+lName+"')]";
            else if(agrementNum.length()==3)
                agrNumXpath = "//td[@data-abc-id='agreement']//span[contains(text(),\"00" + clubNum + "000" + agrementNum + "\")]";
        }else if(agrementNum.length() == 6)
            agrNumXpath = "//td[@data-abc-id='agreement']//span[contains(text(),\"00" + clubNum + "" + agrementNum + "\")]";
        else
        {
            agrNumXpath = "//td[@data-abc-id='agreement']//span[contains(text(),\"00" +clubNum+"0"+agrementNum+"\")]";
        }

        By agrementNumber = By.xpath(agrNumXpath);
        if(getDriver().findElements(agrementNumber).size()>0){
            findElement(agrementNumber).click();
            return "Pass";
        }else{
            return "Fail";// finalMemType;
        }
    }


    public Boolean searchMemberByFirstAndSecondName(String fName, String sName) throws InterruptedException {
        boolean findMem = false;
        int count = getDriver().findElements(backButtonMemberSearch).size();
        if(count==1){
            findElement(backButtonMemberSearch).click();
            //findElement(srchClearButton).click();
            clearMemSearchBox();
        }
        findElement(srchButton).clear();
        findElement(srchButton).sendKeys(fName);
        By membersearchXPath = By.xpath("//*[starts-with(text(), '"+sName +"')  and contains(text(), '"+fName +"')]");
        int memberCount = getDriver().findElements(membersearchXPath).size();
        if(memberCount==1){
            findMem = true;
            findElement(membersearchXPath).click();
        }
        return findMem;
    }


    public void getIntoMemberAccount(){
    }

    public void searchProduct(String product) {
        findElement(searchText).clear();
        findElement(searchText).sendKeys(product);
        findElement(searchButton).click();
    }


    By subscriptiontab = By.xpath("//div[contains(text(),'Subscriptions')]");

    public void clickSubscriptionTab() {
        findElement(subscriptiontab).click();
    }

    By MemberAccountTab = By.xpath("//div[contains(text(),'Member Account')]");
    public void clickMemberAccountTab() {
        findElement(MemberAccountTab).click();
    }

    By paymentsTab = By.xpath("//*[@data-text='Payments']");
    public void clickPaymentsTab() {
        findElement(paymentsTab).click();
    }

    By memStatus = By.xpath("//*[@data-abc-id='memberStatus']");
    public String getMemberStatus() throws InterruptedException {
        Thread.sleep(2000);
        return findElement(memStatus).getText();
    }


    // Get Complete Table Data
    public Map<Integer,List<String>> getTableData(String tableName) {
        String tableXpath = "//table[@data-abc-id='" + tableName + "']";
        WebElement table = findElement(By.xpath(tableXpath));
        List<WebElement> rowsList = table.findElements(By.tagName("tr"));
        List<WebElement> columnsList = null;
        List<String> rowData = new ArrayList<String>();
        Map<Integer,List<String>> tableData = new HashMap<Integer,List<String>>();
        System.out.println();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = 0;
        for (WebElement row : rowsList) {
            columnsList = row.findElements(By.tagName("td"));
            String data = "";
            for (WebElement column : columnsList) {
                // System.out.print(column.getText() + ", ");
                data = column.getText();
                rowData.add(data);
            }
            tableData.put(i,rowData);
            i++;
        }
        return tableData;
    }


    public String dateFormatter(String orgDate) throws Exception{
        DateFormat originalFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("MM-dd-yyyy");
        //Date date = originalFormat.parse("Aug-21-2012");
        Date date = originalFormat.parse(orgDate);
        String formattedDate = targetFormat.format(date);  // 20120821
        return formattedDate;
    }

    public LinkedHashMap<String,List<String>> readExcelFile(String filePath) throws Exception {
        FileInputStream file = new FileInputStream(new File(filePath));
        // Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        // Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);
        Map<String,List<String>> memberDetails = new LinkedHashMap<String,List<String>>();
        int rowcount = sheet.getLastRowNum();
        String data = "";
        System.out.println("Total members for Billing Verification : " + rowcount);
        for (int i = 1; i < rowcount + 1; i++) {
            //Create a loop to get the cell values of a row for one iteration
            Row row = sheet.getRow(i);
            List<String> arrName = new ArrayList<String>();
            String dateToPickFromExcel = datafile.getData("dateToPickFromExcel");
            String dateFromExcel = dateFormatter(row.getCell(5).toString());
            if(dateToPickFromExcel.equalsIgnoreCase(dateFromExcel)){
                // System.out.println(dateToPickFromExcel + " Date Matches " + dateFromExcel );
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    // Create an object reference of 'Cell' class
                    Cell cell = row.getCell(j);
                    // Add all the cell values of a particular row
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            // System.out.print(cell.getNumericCellValue() + "\t");
                            data = String.valueOf(cell.getNumericCellValue());
                            arrName.add(data);
                            break;
                        case STRING:
                            // System.out.print(cell.getStringCellValue() + "\t");
                            data = cell.getStringCellValue();
                            arrName.add(data);
                            break;
                        case _NONE:
                            arrName.add(null);
                            break;
                        case BLANK:
                            arrName.add(null);
                            break;
                    }
                }
                if(filePath.contains("paymentTestData")){
                    memberDetails.put(arrName.get(0),arrName);
                }else{
                    memberDetails.put(arrName.get(3),arrName);
                }
            }else{
                // System.out.println(dateToPickFromExcel + " Date Not Matches " + dateFromExcel );
            }
        }
        System.out.println();
        return (LinkedHashMap<String, List<String>>) memberDetails;
    }

    // By clubClearButton = By.xpath("//*[@id='organizationSwitcherInput']/../i[@aria-label='Clear Icon']");
    By clubClearButton = By.xpath("//*[@data-abc-id='iconClose']");
    public void clearClub(){
        int isClubSelected = getDriver().findElements(clubClearButton).size();
        if(isClubSelected==1)
            findElement(clubClearButton).click();
    }

    public String getFirstPaymentType(String usrDate, String expectedInvAmnt){

        By dataTest = By.xpath("//*[@data-abc-id='createdDate' and contains(text(),'"+usrDate+"')]");
        By status = By.xpath("//*[@data-abc-id='createdDate' and contains(text(),'"+usrDate+"')]/../../../td[@data-abc-id='status']/span");
        By paymentMethod = By.xpath("//*[@data-abc-id='createdDate' and contains(text(),'"+usrDate+"')]/../../../*[@data-abc-id='paymentMethod']/span");
        By amount = By.xpath("//*[@data-abc-id='createdDate' and contains(text(),'"+usrDate+"')]/../../../*[@data-abc-id='amount']/span");
        By postDate = By.xpath("//*[@data-abc-id='createdDate' and contains(text(),'"+usrDate+"')]/../../*[@data-abc-id='extraContent']");
        By invoiceStatus = By.xpath("//*[@data-abc-id='dateSecondary' and contains(text(),'"+usrDate+"')]/../../..//span[@data-abc-id=\"transactionSecondary\"]");
        int recurringPayment = getDriver().findElements(dataTest).size();
        String finalDataTobeVerified = "";
        String getStatus = "", getPaymntId = "", getAmount = "", createdDate = "", postedDate = "";
        if(recurringPayment == 1 ){
            // From Transaction History Table
            getStatus = findElement(status).getText();
            getPaymntId = findElement(paymentMethod).getText();
            getAmount = findElement(amount).getText().replace("$","").replace(",","");
            createdDate = usrDate;
            postedDate = findElement(postDate).getText();
        }
        else
        {
            getDriver().navigate().refresh();
            int invoiceStatusSize = getDriver().findElements(invoiceStatus).size();
            if(invoiceStatusSize == 1){
                getStatus = findElement(invoiceStatus).getText();
            }
        }
        finalDataTobeVerified = getPaymntId +","+ getStatus +","+ ","+ "," + expectedInvAmnt + ","+  getAmount +"," + createdDate+","+ postedDate ;  // getStatementAmount +","+ statusInfo
        return finalDataTobeVerified;
    }

    File file = new File("C:\\Users\\vijaygupta\\Desktop\\VerifiedData.csv");
//    String rsltFilePath = datafile.getData("resultFile");
//    File file = new File(rsltFilePath);

    public void fileAppendData(String data) throws IOException {
        FileWriter fr = new FileWriter(file, true);
        BufferedWriter br = new BufferedWriter(fr);
        br.write(data);
        br.close();
        fr.close();
    }

    By pgTransaction = By.xpath("//*[@data-abc-id='pgTransactionsUiModuleIcon']");
    public void clickTransactionModule(){
        findElement(pgTransaction).click();
    }

    By batchTransaction = By.xpath("//*[@data-abc-id='pgBatchMonitoringUiModuleIcon']");
    public void clickBatchModule(){
        findElement(batchTransaction).click();
    }

    By startDate = By.xpath("//*[@data-abc-id=\"transactionsDateRangeStart\"]//input");
    public void enterBeginDate(String date){
        findElement(startDate).click();
        findElement(startDate).sendKeys(Keys.BACK_SPACE);
        findElement(startDate).sendKeys(Keys.BACK_SPACE);
        findElement(startDate).sendKeys(Keys.BACK_SPACE);
        findElement(startDate).sendKeys(Keys.BACK_SPACE);
        findElement(startDate).sendKeys(Keys.BACK_SPACE);
        findElement(startDate).sendKeys(Keys.BACK_SPACE);
        findElement(startDate).sendKeys(Keys.BACK_SPACE);
        findElement(startDate).sendKeys(Keys.BACK_SPACE);
        findElement(startDate).clear();
        findElement(startDate).sendKeys(date);
    }

    By endDate = By.xpath("//*[@data-abc-id=\"transactionsDateRangeEnd\"]//input");
    public void enterEndDate(String date){
        findElement(endDate).click();
        findElement(endDate).sendKeys(Keys.BACK_SPACE);
        findElement(endDate).sendKeys(Keys.BACK_SPACE);
        findElement(endDate).sendKeys(Keys.BACK_SPACE);
        findElement(endDate).sendKeys(Keys.BACK_SPACE);
        findElement(endDate).sendKeys(Keys.BACK_SPACE);
        findElement(endDate).sendKeys(Keys.BACK_SPACE);
        findElement(endDate).sendKeys(Keys.BACK_SPACE);
        findElement(endDate).sendKeys(Keys.BACK_SPACE);
        findElement(endDate).sendKeys(date);
    }


    By bnkButton = By.xpath("//*[@data-value=\"Bank Account\"]");
    public void clickBankButton(){
        findElement(bnkButton).click();
    }
    By creditCardButton = By.xpath("//*[@data-value=\"Credit Card\"]");
    public void clickCreditCardButton(){
        findElement(creditCardButton).click();
    }
    By fourNums = By.xpath("//input[@id=\"lastFour\"]");
    public void  payment4Nums(String num) throws InterruptedException {
        findElement(fourNums).click();
        findElement(fourNums).sendKeys(Keys.BACK_SPACE);
        findElement(fourNums).sendKeys(Keys.BACK_SPACE);
        findElement(fourNums).sendKeys(Keys.BACK_SPACE);
        findElement(fourNums).sendKeys(Keys.BACK_SPACE);
        findElement(fourNums).sendKeys(num);
        Thread.sleep(3000);
    }
    By pgAmount = By.xpath("//table[@data-abc-id=\"transactionsTable\"]//td[@data-abc-id='amount']");
    public String getPgAmount(){
        return findElement(pgAmount).getText();
    }
    By pgAccountHolderName = By.xpath("//table[@data-abc-id=\"transactionsTable\"]//td[@data-abc-id='accountHolderName']");
    public String getPgAcntHolderName(){
        return findElement(pgAccountHolderName).getText();
    }
    By pgStatus = By.xpath("//table[@data-abc-id=\"transactionsTable\"]//td[@data-abc-id='status']");
    public String getPgStatus(){
        return findElement(pgStatus).getText();
    }
    By pgDate = By.xpath("//table[@data-abc-id=\"transactionsTable\"]//td[@data-abc-id='effectiveDate']");
    public String getPgDAte(){
        return findElement(pgDate).getText().replace(","," - ");
    }
    public String getClubName(String clubNumber){
        switch(clubNumber){
            case "520": return "TAN 24-7 FREMONT";
            case "2000": return	"JAMES A. BOTTIN";
            case "2002": return	"BOTTIN FAMILY REAL ESTATE LLC";
            case "2908": return	"HARVEY'S GYM OF FRANKLIN";
            case "3683": return	"MG SPORTS AND FITNESS";
            case "3706": return	"New Life Fitness Center";
            case "3726": return	"JUNGLE GYM FITNESS";
            case "3948": return	"NEW ENGLAND FIT AND MMA";
            case "3959": return "LIFE FITNESS PROS";
            case "4150": return	"Pilgers Womens Bootcamp";
            case "4572": return	"THE FITNESS ZONE";
            case "4613": return	"BAM ADVENTURE BOOT CAMP";
            case "5319": return	"ROCK CITY MMA";
            case "5342": return	"CATOCTIN CROSSFIT PURCELLVILLE";
            case "5552": return	"CONFLUENCE CLUB WORKS MXMETRIC";
            case "5810": return	"ELITE COMBAT ACADEMY";
            case "5855": return	"PAGELAND FITNESS AND WELLNESS";
            case "6060": return	"THE PUMPHOUSE HENDERSONVILLE";
            case "7095": return	"UNITED KARATE STUDIO";
            case "7722": return	"MUSCLES AND CURVES GYM";
            case "7875": return	"TKI FAMILY MARTIAL ARTS";
            case "4099": return "Old Skool Fight Sports Fitness";
            case "7541": return "Titus Strength";
            case "6019" : return "LADIES FITNESS AND WELLNESS MB";
            case "8730" : return "PLAINFIELD GYM AND TAN";
            case "9971" : return "Evendo LLC";
            case "4300" : return "XTREME PHYSIQUE";
            case "7997" : return "CONTOURS EXPRESS";
            case "2928"	: return "REVOLUTION MIXED MARTIAL ARTS";
            case "8205"	: return "CABARRUS PROF FIREFIGHTER ASSN";
            case "3204" : return "BRUTAL IRON GYM";
            case "3505" : return "TEXAS ROWING CENTER";
            case "8429" : return "LELIA PATTERSON CENTER";
            case "5206" : return "MAXD OUT FITNESS";
            case "7504" : return "TAMPA SPORTS ACADEMY";
            case "9533" : return "E'VILLE FITNESS";
            case "2558" : return "121 FITNESS";
            case "3250" : return "FOUST FAMILY FITNESS";
            case "3770" : return "ACERO GYM";
            case "8609" : return  "NEW YORK SPORT AND FITNESS";
            case "8743" : return  "I AM FITNESS";
            case "7941" : return  "RAINIER HEALTH AND FITNESS";
            case "7218" : return  "THREE RIVERS ATHLETIC CLUB";
            case "30234" : return  "GRAND VALLEY STRENGTH AND FIT";

            default:
                return	"Update the club Name in CodeBase";
        }
    }
    // Batch Data fetch Code
    By batchDataStartDate = By.xpath("//*[@data-abc-id='filesModifiedDateRangeStart']//input");
    public void batchBeginDate(String date){
        findElement(batchDataStartDate).click();
        findElement(batchDataStartDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataStartDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataStartDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataStartDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataStartDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataStartDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataStartDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataStartDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataStartDate).sendKeys(date);
    }
    By batchDataEndDate = By.xpath("//*[@data-abc-id='filesModifiedDateRangeEnd']//input");
    public void batchEndDate(String date){
        findElement(batchDataEndDate).click();
        findElement(batchDataEndDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataEndDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataEndDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataEndDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataEndDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataEndDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataEndDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataEndDate).sendKeys(Keys.BACK_SPACE);
        findElement(batchDataEndDate).sendKeys(date);
    }

    public void clickBatch(int batchNo){
        By batchNoXpath = By.xpath("//tr[@data-abc-id='fileMonitorRow']["+ batchNo +"]");
        findElement(batchNoXpath).click();
    }

    public void batchClickClub(int clubPos){
        By clubNoXpath = By.xpath("//tr[@data-abc-id='paymentBatchMonitorsTableRow']["+ clubPos +"]");
        findElement(clubNoXpath).click();
    }
    public void clickMainBatch(){
        By mainBatch = By.xpath("//a[@data-abc-id='batchFilesMonitoringBreadcrumb']");
        findElement(mainBatch).click();
    }
    By merchantName = By.xpath("//*[@data-abc-id='batchInfo']//p");
    public String getClubName(){
        return findElement(merchantName).getText();
    }
    public WebElement findElement(By xp){
        int timeout = Integer.valueOf(datafile.getData("Timeout"));
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
       // wait.until(ExpectedConditions.visibilityOfElementLocated(xp));
        return getDriver().findElement(xp);
    }
    // function clears the member search in RCM App
    public void clearMemSearchBox(){
        try{
            findElement(srchClearButton).click();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}