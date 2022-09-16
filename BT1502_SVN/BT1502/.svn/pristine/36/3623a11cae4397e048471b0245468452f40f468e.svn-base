package BT1502_pro_tml.DC;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDigInOut;
import xoc.dsa.ISetupProtocolInterface;
import xoc.dsa.ISetupTransactionSeqDef;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigInOutActionResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class LED1_TEST extends TestMethod {

    @In public String spec_measurement;

    public IMeasurement measurement;

    public IParametricTestDescriptor ptd_LED1_hi;
    public IParametricTestDescriptor ptd_LED1_lo;

    public String trCallName1 = "";
    public String trCallName2 = "";

    @Override
    public void setup()
    {
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement);

        ISetupProtocolInterface paInterface = deviceSetup.addProtocolInterface("I2C_BES", "besLib.pa.I2C_8bit_BES");
        paInterface.addSignalRole("DATA", "I2C_SDA");
        paInterface.addSignalRole("CLK", "I2C_SCL");
        ISetupTransactionSeqDef transDigSrc1 = paInterface.addTransactionSequenceDef("LED1_TEST_1");
        ISetupTransactionSeqDef transDigSrc2 = paInterface.addTransactionSequenceDef("LED1_TEST_2");
        ISetupTransactionSeqDef transDigSrc3 = paInterface.addTransactionSequenceDef("LED1_TEST_3");

//        transDigSrc1.addParameter(Type.UnsignedLong, Direction.OUT, "rd_pmu_0x68_1");
//        transDigSrc1.addParameter(Type.UnsignedLong, Direction.OUT, "rd_pmu_0x68_2");
//        transDigSrc2.addParameter(Type.UnsignedLong, Direction.OUT, "rd_pmu_0x68_3");

        if(StaticFields.pmic_name == "BT1800")
        {
            transDigSrc1.addTransaction("PMU_WRITE",0x00,0xa000);//back to page0
            transDigSrc1.addTransaction("PMU_WRITE",0x3A,0xCCEC);
            transDigSrc1.addTransaction("PMU_WRITE",0x00,0xa010);//page1
            transDigSrc1.addTransaction("PMU_WRITE",0x09,0x8020);
            transDigSrc1.addTransaction("PMU_WRITE",0x00,0xa000);//back to page0

            transDigSrc2.addTransaction("PMU_WRITE",0x00,0xa010);//page1
            transDigSrc2.addTransaction("PMU_WRITE",0x09,0x0020);
            transDigSrc2.addTransaction("PMU_WRITE",0x00,0xa000);//back to page0

            transDigSrc3.addTransaction("PMU_WRITE",0x3A,0xECEC);
        }
        if(StaticFields.pmic_name == "BT1802")
        {
            transDigSrc1.addTransaction("PMU_WRITE",0x00,0xa000);//back to page0
            transDigSrc1.addTransaction("PMU_WRITE",0x3A,0xCCEC);
            transDigSrc1.addTransaction("PMU_WRITE",0x00,0xa010);//page1
            transDigSrc1.addTransaction("PMU_WRITE",0x09,0x8020);
            transDigSrc1.addTransaction("PMU_WRITE",0x00,0xa000);//back to page0

            transDigSrc2.addTransaction("PMU_WRITE",0x00,0xa010);//page1
            transDigSrc2.addTransaction("PMU_WRITE",0x09,0x0020);
            transDigSrc2.addTransaction("PMU_WRITE",0x00,0xa000);//back to page0

            transDigSrc3.addTransaction("PMU_WRITE",0x3A,0xECEC);
        }
        if(StaticFields.pmic_name == "BT1803" || StaticFields.pmic_name == "BT1805")
        {
//            transDigSrc1.addTransaction("PMU_READ" ,0x68,"rd_pmu_0x68_1");
            transDigSrc1.addTransaction("PMU_WRITE",0x3A,0xC8C8);
            transDigSrc1.addTransaction("PMU_WRITE",0x6F,0x0806);
            transDigSrc1.addTransaction("PMU_WRITE",0x68,0x8020);
//            transDigSrc1.addTransaction("PMU_READ" ,0x68,"rd_pmu_0x68_2");
            transDigSrc2.addTransaction("PMU_WRITE",0x68,0x0020);
//            transDigSrc2.addTransaction("PMU_READ" ,0x68,"rd_pmu_0x68_3");
            transDigSrc3.addTransaction("PMU_WRITE",0x3A,0xECEC);
        }

        if(StaticFields.pmic_name == "BT1800" || StaticFields.pmic_name == "BT1802")
        {
            ISetupDigInOut DigInOut_LED1 = deviceSetup.addDigInOut("LED1").setConnect(true).setDisconnect(true);
            DigInOut_LED1.iforce("Iforce_10mA_LED1_sink"  ).setForceValue("-10 mA").setIrange("40 mA").setVrange("1.00 V").setVclampLow("0.2 V" ).setVclampHigh("0.5 V");//?????????????????????
            DigInOut_LED1.vmeas("Iforce_10mA_vmeas_LED1_sink"  ).setAverages(16).setWaitTime("10 ms");
            DigInOut_LED1.iforce("Iforce_10mA_LED1_source").setForceValue("10 mA" ).setIrange("40 mA").setVrange("1.00 V").setVclampLow("-0.2 V").setVclampHigh("0.5 V");//?????????????????????
            DigInOut_LED1.vmeas("Iforce_10mA_vmeas_LED1_source").setAverages(16).setWaitTime("10 ms");
        }
        if(StaticFields.pmic_name == "BT1803" || StaticFields.pmic_name == "BT1805")
        {
            ISetupDigInOut DigInOut_LED1 = deviceSetup.addDigInOut("LED1").setConnect(true).setDisconnect(true);
            DigInOut_LED1.iforce("Iforce_10mA_LED1_sink"  ).setForceValue("-10 mA").setIrange("40 mA").setVrange("1.00 V").setVclampLow("0.2 V" ).setVclampHigh("0.5 V");//?????????????????????
            DigInOut_LED1.vmeas("Iforce_10mA_vmeas_LED1_sink"  ).setAverages(16).setWaitTime("10 ms");
            DigInOut_LED1.iforce("Iforce_10mA_LED1_source").setForceValue("10 mA" ).setIrange("40 mA").setVrange("1.00 V").setVclampLow("-0.2 V").setVclampHigh("0.5 V");//?????????????????????
            DigInOut_LED1.vmeas("Iforce_10mA_vmeas_LED1_source").setAverages(16).setWaitTime("10 ms");
        }

        deviceSetup.sequentialBegin();
            trCallName1 = deviceSetup.transactionSequenceCall(transDigSrc1);
            deviceSetup.actionCall("Iforce_10mA_LED1_sink");
            deviceSetup.actionCall("Iforce_10mA_vmeas_LED1_sink");
            trCallName2 = deviceSetup.transactionSequenceCall(transDigSrc2);
            deviceSetup.actionCall("Iforce_10mA_LED1_source");
            deviceSetup.actionCall("Iforce_10mA_vmeas_LED1_source");
            deviceSetup.transactionSequenceCall(transDigSrc3);
        deviceSetup.sequentialEnd();
        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute ()
    {
        measurement.execute();
        IDigInOutActionResults Result_led1=measurement.digInOut().preserveActionResults();
        MultiSiteDouble LED1_sink  =Result_led1.vmeas("Iforce_10mA_vmeas_LED1_sink").getVoltage("LED1").getElement(0);
        MultiSiteDouble LED1_source=Result_led1.vmeas("Iforce_10mA_vmeas_LED1_source").getVoltage("LED1").getElement(0);

//        IProtocolInterfaceResults paResult_BES = measurement.protocolInterface("I2C_BES").preserveResults();
//        ITransactionSequenceResults trseqResult_BES1 = paResult_BES.transactSeq(trCallName1,0);
//        ITransactionSequenceResults trseqResult_BES2 = paResult_BES.transactSeq(trCallName2,0);
//
//        MultiSiteLong rd_pmu_0x68_1 = trseqResult_BES1.getValueAsLong("rd_pmu_0x68_1");
//        MultiSiteLong rd_pmu_0x68_2 = trseqResult_BES1.getValueAsLong("rd_pmu_0x68_2");
//        MultiSiteLong rd_pmu_0x68_3 = trseqResult_BES2.getValueAsLong("rd_pmu_0x68_3");
//
//        for(int site:context.getActiveSites())
//        {
//            println("rd_pmu_0x68_1 = 0x"+Long.toHexString(rd_pmu_0x68_1.get(site))+" [site "+site+"]");
//            println("rd_pmu_0x68_2 = 0x"+Long.toHexString(rd_pmu_0x68_2.get(site))+" [site "+site+"]");
//            println("rd_pmu_0x68_3 = 0x"+Long.toHexString(rd_pmu_0x68_3.get(site))+" [site "+site+"]");
//        }

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("LED1_sink   [V]: "+LED1_sink);
            println("LED1_source [V]: "+LED1_source);
            println();
        }

        ptd_LED1_hi.evaluate(LED1_sink);
        ptd_LED1_lo.evaluate(LED1_source);
    }
}
