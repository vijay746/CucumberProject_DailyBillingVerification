package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import helper.fileMapping;
import pages.LoginPage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginStep {
    LoginPage lpOb = new LoginPage();
    String workingDir = System.getProperty("user.dir");
    fileMapping datafile = new fileMapping(workingDir + "\\config\\Configuration.properties");
    String clubNumbr = null;
    String fname = "";
    String scndname = "";
    String transDate = "";
    String memId = "";
    String expectdAmount = "";
    String finalPgData = "";
    String memberType = "";
    String memStatus = "";

    @Given("^user navigates to ABC product page$")
    public void lgoToProductPage() throws Exception {
        String paymentGateway = datafile.getData("checkAccountData");
        String getBatchData = datafile.getData("getBatchData");
        String lstFourDigit = "",  bnkOrCC = "",  pgAmount = "",  pgAcntHolderName = "",  pgStatus = "",  pgDate = "",  pgDataHeader = "";

        // Code to get the batch data WIP
        if(getBatchData.equalsIgnoreCase("true")){
            String pgUrl = datafile.getData("paymentGatewayUrl");
            lpOb.navigateToUrl(pgUrl);
            String usrName = datafile.getData("usrName");
            String usrPaswd = datafile.getData("usrpaswd");
            lpOb.enterUsrNameAndpasswd(usrName,usrPaswd);
            lpOb.clickBatchModule();
            Map<Integer, List<String>> batchFileData = lpOb.getTableData("fileMonitorTable");
            int noOfBatches = batchFileData.size();
            String merchantName = "";
            for(int i=1; i<=noOfBatches; i++){
                if(i>1){
                    lpOb.clickMainBatch();
                }
                lpOb.clickBatch(i);
                Map<Integer, List<String>> paymentBatchFileData = lpOb.getTableData("paymentBatchMonitorsTableTable");
                int noOfClubsInBatch = paymentBatchFileData.size();
                for(int j=0; j<= noOfClubsInBatch ;j++){
                    lpOb.batchClickClub(j);
                    Map<Integer, List<String>> paymentTransactionsBatchData = lpOb.getTableData("paymentBatchTransactionsMonitorsTableTable");
                    for (Map.Entry<Integer,List<String>> entry1 : paymentTransactionsBatchData.entrySet()) {
                        // memId = entry1.getKey();
                        for( int s=0 ; s<paymentTransactionsBatchData.size() ; s= s+8 ){
                            String accountHolderName = entry1.getValue().get(s);
                            String accountHolderAmount = entry1.getValue().get(s+2);
                            String transStatus = entry1.getValue().get(s+7);
                            System.out.println("account holder name frm batch data is "+accountHolderName+","+accountHolderAmount+","+transStatus);
                        }
                    }
                }
            }
        }


        // Code to verify the 4 digit payment amount on PG url
        if(paymentGateway.equalsIgnoreCase("true")){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now));
            pgDataHeader = "\n\n" + dtf.format(now) + "\nAcnt Num,pgAmount,pgStatus,pgDate,pgAccountName\n";
            lpOb.fileAppendData(pgDataHeader);
            finalPgData = "";
            String acntDetail;
            String pgUrl = datafile.getData("paymentGatewayUrl");
            lpOb.navigateToUrl(pgUrl);
            String usrName = datafile.getData("usrName");
            String usrPaswd = datafile.getData("usrpaswd");
            lpOb.enterUsrNameAndpasswd(usrName,usrPaswd);
            lpOb.clickTransactionModule();
            lpOb.enterBeginDate(datafile.getData("beginDate"));
            lpOb.enterEndDate(datafile.getData("endDate"));
            LinkedHashMap<String,List<String>> getAllPaymentData = lpOb.readExcelFile(datafile.getData("paymentSheet"));
            for (Map.Entry<String,List<String>> entry : getAllPaymentData.entrySet()) {
                memId = entry.getKey();
                acntDetail = entry.getValue().get(0);
                if(!acntDetail.contains("test")) {
                    bnkOrCC = acntDetail.split("\\(")[0].trim();
                    if (bnkOrCC.contains("Bank Account")) {
                        lpOb.clickBankButton();
                    } else {
                        lpOb.clickCreditCardButton();
                    }
                    lstFourDigit = entry.getValue().get(0).split("\\(")[1].trim();
                    lpOb.payment4Nums(lstFourDigit);
                    Map<Integer, List<String>> transactionData = lpOb.getTableData("transactionsTable");
                    if (transactionData.size() == 3) {
                        pgAmount = lpOb.getPgAmount().replace(",", "");
                        pgAcntHolderName = lpOb.getPgAcntHolderName();
                        pgStatus = lpOb.getPgStatus();
                        pgDate = lpOb.getPgDAte();
                    } else {
                        pgAmount = "TBV";
                        pgAcntHolderName = "TBV";
                        pgStatus = "TBV";
                        pgDate = "TBV";
                    }
                    finalPgData = acntDetail + "," + pgAmount + "," + pgStatus + "," + pgDate + "," + pgAcntHolderName + "\n";
                }
                else{
                    finalPgData = "TBV \n";
                }
                lpOb.fileAppendData(finalPgData);
            }
            now = LocalDateTime.now();
            lpOb.fileAppendData("payment validation ended at : "+dtf.format(now));
        }
        else{
            String productUrl = datafile.getData("uiPageUrl");
            lpOb.navigateToUrl(productUrl);
        }
    }

    // Code to verify daily billing verification, RCM Data.
    @When("^user search for a product$")
    public void searchProduct() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        String finalData = dtf.format(now) + "\n\nMemId,MemType,ActiveStatus,Expected invoice amount,clubNum,Club,Member Name,Due Date,Payment Type(Last Four),Invoice Status (PROD),Comments,Amount billed as expected,Expected invoice amount,Payment amount billed on Payment Gateway,,AmountCorrect?,Duplicate Invoice?, \n";
        lpOb.fileAppendData(finalData);
        String usrName = datafile.getData("usrName");
        String usrPaswd = datafile.getData("usrpaswd");
        lpOb.enterUsrNameAndpasswd(usrName,usrPaswd);
        String x = "",clubName = "";
        LinkedHashMap<String,List<String>> getAllMemberData = lpOb.readExcelFile(datafile.getData("uiTestDataSheet"));
        for (Map.Entry<String,List<String>> entry : getAllMemberData.entrySet()) {
            finalData = "";
           x = entry.getValue().get(0).split("\\.")[0];
            if(clubNumbr == null || !clubNumbr.equalsIgnoreCase(x)){
                clubNumbr = entry.getValue().get(0).split("\\.")[0];
                lpOb.clearClub();
                lpOb.clickBusiness();
                lpOb.searchClub(clubNumbr);
                lpOb.clickClub(clubNumbr);
                Thread.sleep(3000);
                lpOb.clickMemberButton();
            }
                memId = entry.getKey().replace(".0","");
                fname = entry.getValue().get(1).split(" ")[0];
                scndname = entry.getValue().get(2);
                transDate = entry.getValue().get(4);
                expectdAmount = entry.getValue().get(6);
                memberType = lpOb.searchMemberAndGetMemType(fname, scndname, clubNumbr, memId);
                if(Objects.equals(memberType, "Pass")){
                    memStatus = lpOb.getMemberStatus();
                    clubName = lpOb.getClubName(clubNumbr);
                    finalData = finalData + memId + "," + memberType + "," + memStatus + "," + expectdAmount + "," + clubNumbr + "," + clubName + "," + fname + " " + scndname + "," + transDate;
                    // if (memberType.equalsIgnoreCase("Primary") && memStatus.equalsIgnoreCase("ACTIVE")) {
                    if (memStatus.equalsIgnoreCase("ACTIVE") || memStatus.equalsIgnoreCase("memStatusTBUpdated")) {
                        // lpOb.clickMemberAccountTab();
                        lpOb.clickPaymentsTab();
                        finalData = finalData + "," + lpOb.getFirstPaymentType(datafile.getData("beginDate"), expectdAmount) + "\n";
                    }else{
                        finalData = finalData  + "\n";
                    }
                    lpOb.fileAppendData(finalData);
                }else{
                    finalData = finalData + memId + "," + memberType + "," + memStatus + "," + expectdAmount + "," + clubNumbr + "," + clubName + "," + fname + " " + scndname + "," + transDate;
                    lpOb.fileAppendData(finalData+ "\n");
                    continue;
                }
        }
        lpOb.fileAppendData("UI VAlidation end at : "+ dtf.format(now) );
    }

    @Then("^right category is shown$")
    public void checkCategory() {
//        String expectedProductCategory = datafile.getData("productCategory");
//        String actualProductCategory = lpOb.getCategory();
//        Assert.assertEquals(actualProductCategory,expectedProductCategory, "Validation for check of searched product category" );
    }

}



