package besLib.generalTestMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import com.advantest.itee.tmapi.registeraccess.core.RegisterCache;

import xoc.dsa.DeviceSetupUncheckedException;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.workspace.IWorkspace;

public class PassBinGrade extends TestMethod {
    public static Map<Integer, List<Integer>> sbin_Map;
    @In public String testTablePath;
    @In public String mainSheet;

    @Override
    public void execute() {
        /*********** get Test program string which need to fill in test table ***********/
        String matchString="";
        int startIndex=RegisterCache.getTestprogramName(context).indexOf(".");
        String temp1=RegisterCache.getTestprogramName(context).substring(startIndex+1);
        if(temp1.contains("FT")==true) {
            matchString=temp1.substring(0, temp1.indexOf("FT")-1);
        }
        else if(temp1.contains("QA")==true) {
            matchString=temp1.substring(0, temp1.indexOf("QA")-1);
        }
        if(matchString.equals("")) {
            throw new DeviceSetupUncheckedException("Test program name doesn't meet requirment !");
        }

        System.out.println();
        String testSuiteName_Qualified=context.getTestSuiteName();
        String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
        println("********** "+testSuiteName+" **********");
        System.out.println();
        System.out.println("-----------------------------------------------------------");
        System.out.println("Characters searching in test table: "+matchString);
        System.out.println("-----------------------------------------------------------");
        System.out.println();

        /*********** verify pass bin priority  ***********/
        int priorityBin2= context.binning().binTable().getSoftBin(2).getPriority();
        int priorityBin3= context.binning().binTable().getSoftBin(3).getPriority();
        int priorityBin4= context.binning().binTable().getSoftBin(4).getPriority();
        if( (priorityBin3-priorityBin2)<=0 || (priorityBin4-priorityBin3)<=0 ) {
            throw new DeviceSetupUncheckedException("Error! please check priority of pass bin 2/3/4 in test table");
        }

        /*********** read test table & get required info ***********/
        sbin_Map=new HashMap<>();
        Map<Integer, List<String>> ptd_Map=new HashMap<>();
        File odsFile=new File(IWorkspace.getActiveProjectPath()+"/src/"+testTablePath);
        try {
            SpreadSheet testTable=SpreadSheet.createFromFile(odsFile);
            Sheet sheet_MainTest=testTable.getSheet(mainSheet);
            int targetColumn=100;
            int sbinColumn=0;
            int ptdColumn=0;
            int testSuiteColumn=0;

            for(int row=1;row>=0;row--) {
                for(  int col=0;col<60;col++) {
                    if(sheet_MainTest.getImmutableCellAt(col, row).getTextValue().equals(matchString)) {
                        targetColumn=col;
                    }
                    if(sheet_MainTest.getImmutableCellAt(col, row).getTextValue().equals("Soft Bin")) {
                        sbinColumn=col;
                    }
                    if(sheet_MainTest.getImmutableCellAt(col, row).getTextValue().equals("Test")) {
                        ptdColumn=col;
                    }
                    if(sheet_MainTest.getImmutableCellAt(col, row).getTextValue().equals("Test Suite")) {
                        testSuiteColumn=col;
                    }
                }
            }
            List<Integer> sbin_pass2=new ArrayList<Integer>();
            List<Integer> sbin_pass3=new ArrayList<Integer>();
            List<Integer> sbin_pass4=new ArrayList<Integer>();
            List<String> ptd_pass2 =new ArrayList<String>();
            List<String> ptd_pass3 =new ArrayList<String>();
            List<String> ptd_pass4 =new ArrayList<String>();
            int tempValue=0;
            for(int row=2;row<4000;row++) {
                String cellValue_String=sheet_MainTest.getImmutableCellAt(targetColumn, row).getTextValue();
                int cellValue =0;
                if(cellValue_String.equals("2") || cellValue_String.equals("3") || cellValue_String.equals("4")) {
                    cellValue =  Integer.parseInt(cellValue_String);
                }
                tempValue=0;
                switch (cellValue) {
                case 2:
                    tempValue=Integer.parseInt(sheet_MainTest.getImmutableCellAt(sbinColumn, row).getTextValue());
                    if(!sbin_pass2.contains(tempValue)) {
                        sbin_pass2.add(tempValue );
                    }
                    ptd_pass2.add("Main."+sheet_MainTest.getImmutableCellAt(testSuiteColumn, row).getTextValue()+"."+sheet_MainTest.getImmutableCellAt(ptdColumn, row).getTextValue());
                    break;
                case 3:
                    tempValue=Integer.parseInt(sheet_MainTest.getImmutableCellAt(sbinColumn, row).getTextValue());
                    if(!sbin_pass3.contains(tempValue)) {
                        sbin_pass3.add( tempValue);
                    }
                    ptd_pass3.add("Main."+sheet_MainTest.getImmutableCellAt(testSuiteColumn, row).getTextValue()+"."+ sheet_MainTest.getImmutableCellAt(ptdColumn, row).getTextValue());
                    break;
                case 4:
                    tempValue=Integer.parseInt(sheet_MainTest.getImmutableCellAt(sbinColumn, row).getTextValue());
                    if(!sbin_pass4.contains(tempValue)) {
                        sbin_pass4.add( tempValue);
                    }
                    ptd_pass4.add("Main."+sheet_MainTest.getImmutableCellAt(testSuiteColumn, row).getTextValue()+"."+ sheet_MainTest.getImmutableCellAt(ptdColumn, row).getTextValue());
                    break;
                default:
                    break;
                }
            }
            sbin_Map.put(2, sbin_pass2);
            sbin_Map.put(3, sbin_pass3);
            sbin_Map.put(4, sbin_pass4);
            ptd_Map.put(2, ptd_pass2);
            ptd_Map.put(3, ptd_pass3);
            ptd_Map.put(4, ptd_pass4);

            System.out.format("bin2 flag = sbin %-30s  ptd = %s", sbin_Map.get(2).toString(),ptd_Map.get(2)).println();
            System.out.format("bin3 flag = sbin %-30s  ptd = %s", sbin_Map.get(3).toString(),ptd_Map.get(3)).println();
            System.out.format("bin4 flag = sbin %-30s  ptd = %s", sbin_Map.get(4).toString(),ptd_Map.get(4)).println();

//            System.out.println("sbin (bin2 flag) in testtable= "+ sbin_Map.get(2).toString()+"      ****ptd**** = "+ptd_Map.get(2) );
//            System.out.println("sbin (bin3 flag) in testtable= "+ sbin_Map.get(3).toString()+"      ****ptd**** = "+ptd_Map.get(3) );
//            System.out.println("sbin (bin4 flag) in testtable= "+ sbin_Map.get(4).toString()+"      ****ptd**** = "+ptd_Map.get(4) );
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*********** re-map fail soft bin to target pass soft bin  ***********/
        for(int key:ptd_Map.keySet()) {
            Iterator<String> iter=ptd_Map.get(key).iterator();
            while(iter.hasNext()) {
                String ptd_Testtable=iter.next();
                for(String fullQualifiedName_ptd:context.testProgram().getAllTestDescriptors().keySet()) {
                    if(fullQualifiedName_ptd.equals(ptd_Testtable.replaceAll(" ", ""))) {
                            context.testProgram().getTestDescriptor(ptd_Testtable).setSoftBinId(key);
                            System.out.format("set  %-60s to pass sbin %d ", ptd_Testtable,key).println();
                    }
                }
            }
        }


    }

}
