package BT1502_pro_tml.Global;

import xoc.dta.TestMethod;
import xoc.dta.annotations.Out;

public class Set_Version extends TestMethod {

    @Out
    public String TT_Type = "FT01";

    @Override
    public void execute() {
        String ftqa = context.testProgram().variables().getString("FTQA").get();
        String devname = context.testProgram().variables().getString("DEV_NAME").get();
        if (ftqa.equals("FT")) {
            if (devname.equals("WLCSP")) {
                TT_Type = "FT01";
            } else if (devname.equals("BGA7273")) {
                TT_Type = "FT02";
            }
        } else if (ftqa.equals("QA")) {
            if (devname.equals("WLCSP")) {
                TT_Type = "QA01";
            } else if (devname.equals("BGA7273")) {
                TT_Type = "QA02";
            }
        }

    }
}
