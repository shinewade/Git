package BT1502_pro_tml.Global;

import xoc.dta.TestMethod;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class SETFLAG_PASSB extends TestMethod {

    public IParametricTestDescriptor ptd_PASSB_TXPOWERL;
    public IParametricTestDescriptor ptd_PASSB_TXPOWERH;

    @Override
    public void execute() {

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
//            println("PASSB_FLAG = "+StaticFields.PASSB_FLAG);
            println();
        }

//        ptd_PASSB_BT_RX_SWITCH_TO_HIGH_VOLTAGE.evaluate(StaticFields.);
//        ptd_PASSB_RAMRUN_RESAMPLE_X4_ADDA_TEST.evaluate(StaticFields.PASSB_RAMRUN_RESAMPLE_X4_ADDA_TEST);
//        ptd_PASSB_RAMRUN_SRAM_FLASH_TEST      .evaluate(StaticFields.PASSB_RAMRUN_SRAM_FLASH_TEST);
////        ptd_PASSB_BT_TX_DCO                   .evaluate(StaticFields.PASSB_BT_TX_DCO);//remove 20220621
//        ptd_PASSB_BT_TX_VCO                   .evaluate(StaticFields.PASSB_BT_TX_VCO);
//        ptd_PASSB_DSI                         .evaluate(StaticFields.PASSB_DSI);
//        ptd_PASSB_DSI_PLL                     .evaluate(StaticFields.PASSB_DSI_PLL);
//        ptd_SetBin_PassB.evaluate(StaticFields.PASSB_FLAG);
        ptd_PASSB_TXPOWERL.evaluate(StaticFields.BTTX_PWR_L);
        ptd_PASSB_TXPOWERH.evaluate(StaticFields.BTTX_PWR_L);


    }

}
