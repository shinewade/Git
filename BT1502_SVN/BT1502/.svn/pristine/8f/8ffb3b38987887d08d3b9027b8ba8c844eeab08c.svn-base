package besLib.generalTestMethod;

import java.util.ArrayList;
import java.util.List;

import xoc.dta.TestMethod;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class AlarmCheck_Efuse extends TestMethod {

    public IParametricTestDescriptor ptd_check_alarm;

    @Override
    public void execute() {
        List<Integer>softBinList = new ArrayList<Integer>();
        MultiSiteDouble alarmValue = new MultiSiteDouble(1);
        for (int site : context.getActiveSites()) {
            softBinList = context.binning().getBinList(site, true);
            List<Integer> softBinList_Temp=new ArrayList<Integer>();
            softBinList_Temp.addAll(softBinList);

            //remove possible pass bin2
            if(softBinList_Temp.contains(2)) {
                softBinList_Temp.remove(Integer.valueOf(2));
            }
            //remove possible pass bin3
            if(softBinList_Temp.contains(3)) {
                softBinList_Temp.remove(Integer.valueOf(3));
            }
            //remove possible pass bin4
            if(softBinList_Temp.contains(4)) {
                softBinList_Temp.remove(Integer.valueOf(4));
            }
            //judge pass/fail
            if (softBinList_Temp.isEmpty()) {
                alarmValue.set(site, -1);
            }
            else {
              alarmValue.set(site, 1);
            }

//            if(StaticFields.debugMode)
            {
                String testSuiteName_Qualified=context.getTestSuiteName();
                String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
                println("**********"+testSuiteName+"**********");
                println("site= "+site+" CurrentBinList= "+softBinList_Temp.toString());
                println("site= "+site+" alarmValue= "+alarmValue.get(site));
                println();
            }
        }

        ptd_check_alarm.setTestText("AlarmCheck");
        ptd_check_alarm.setHighLimit(0).setLowLimit(-2).setTestName("AlarmCheck").setTestNumber(999);
        ptd_check_alarm.evaluate(alarmValue);


    }

}
