package besLib.generalTestMethod;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class Read_LB_ID extends TestMethod {
    public IParametricTestDescriptor ptd_Board_id;
    @In public String BoardID = "0";
    @In public boolean Write_BoardID=false;

    @Override
    public void execute() {
        String testSuiteName_Qualified=context.getTestSuiteName();
        String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
        println("**********"+testSuiteName+"**********");

        //Write to LB EEPROM
        if(Write_BoardID){
            context.dutBoard().writeIdString(0x0,BoardID);
            System.out.println("Written string '" + BoardID + "' to DUT board EEPROM.");
        }

        //Read LB EEPROM
        String Read_BoardID=context.dutBoard().readIdString();
        if(Read_BoardID.compareTo("")==0) {
            Read_BoardID="-999";
            System.out.println("Read LB ID Fail !!!");
        }
        MultiSiteLong boardID_long=new MultiSiteLong();
        boardID_long.set(Integer.parseInt(Read_BoardID));

//        if(StaticFields.debugMode)
//        {
//            println("Board_id = "+boardID_long);
//            println();
//        }
        StaticFields.LBID.set(boardID_long);
        ptd_Board_id.evaluate(StaticFields.LBID);
    }

}
