package BT1502_pro_tml.DC;

import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class Iddq_Test extends TestMethod {
    @In public String spec_measurement;
    public IMeasurement measurement;
    public IMeasurement measurement1;
    public IMeasurement measurement2;
    private String pattern1;

    public IParametricTestDescriptor ptd_iddq_max      ;
    public IParametricTestDescriptor ptd_iddq_max_index;
    public IParametricTestDescriptor ptd_iddq_min      ;
    public IParametricTestDescriptor ptd_iddq_min_index;
    public IParametricTestDescriptor ptd_iddq_mean     ;

    @In public double vforceValue = 0.65;

    @Override
    public void setup() {
//        // TODO Auto-generated method stub
//        IDeviceSetup deviceSetup0 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
//        deviceSetup0.importSpec(spec_measurement);
//
//        List<String> signalsList=context.inspection().initialSpec("configuration.Groups").getSignalGroup("CONT_neg");
//        String signals=String.join("+", signalsList).replace("+I2C_SCL", "").replace("+I2C_SDA", "");//.replace("+VBAT_SENSE", "");
//        if(StaticFields.dev_name == "BGA7273")
//        {
//            signals = signals.replace("+POWKEY", "");
//        }
//        StaticFields.Set_AC_Off(deviceSetup0,signals);
//
//        //power supply by dps128 channel
//        ISetupDcVI  dcVi_VCORE_0=deviceSetup0.addDcVI("VCORE_0p8").setDisconnect(false).setDisconnectModeHiz();
//        dcVi_VCORE_0.level().setIrange("100 mA").setVrange("6 V").setVforce(vforceValue).setIclamp("100 mA");
//
//        //close LDO output of pin VCORE_0p8
//        ISetupProtocolInterface paInterface = deviceSetup0.addProtocolInterface("I2C_BES", "besLib.pa.I2C_8bit_BES");
//        paInterface.addSignalRole("DATA","I2C_SDA");
//        paInterface.addSignalRole("CLK", "I2C_SCL");
//        ISetupTransactionSeqDef transDigSrc= paInterface.addTransactionSequenceDef("ldo_vcore_off");
//        //Switch VCORE to 0.6V.
//        if(StaticFields.pmic_name == "BT1803")
//        {
//            transDigSrc.addTransaction("PMU_WRITE",0x46,0x5858);
//        }
//        if(StaticFields.pmic_name == "BT1805")
//        {
//            transDigSrc.addTransaction("PMU_WRITE",0x46,0x5858);
//        }
//        if(StaticFields.pmic_name == "BT1806")
//        {
//            transDigSrc.addTransaction("PMU_WRITE",0x46,0x3030);
//        }
//        transDigSrc.addWait("5 ms");
//        transDigSrc.addTransaction("PMU_INTERN_WRITE",0x14,0x4000);
//        transDigSrc.addTransaction("RF_WRITE"        ,0xc0,0x0200);
//        transDigSrc.addTransaction("XTAL_WRITE"      ,0x0d,0x0082);
//
//        deviceSetup0.sequentialBegin();
//            deviceSetup0.transactionSequenceCall(transDigSrc);
//            deviceSetup0.waitCall("2 ms");
//        deviceSetup0.sequentialEnd();
//
//        measurement.setSetups(deviceSetup0);
//
//
//
//        IDeviceSetup deviceSetup1 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
//        deviceSetup1.importSpec(spec_measurement);
//
//        StaticFields.Set_AC_Off(deviceSetup1,signals);
//
//        //DC event action
//        ISetupDcVI  dcVi_VCORE_1=deviceSetup1.addDcVI("VCORE_0p8").setDisconnect(false).setDisconnectModeHiz();
//        dcVi_VCORE_1.level().setIrange("100 mA").setVrange("6 V").setVforce(vforceValue).setIclamp("100 mA");
//        ISetupAction action1=dcVi_VCORE_1.imeas("imeas_VCORE").setIrange("125 uA").setAverages(16).setWaitTime("5 ms").setSafeDownRangingAuto().setRestoreIrange(true);
//        dcVi_VCORE_1.disconnect("disconnect_VCORE");
//
////        //Insert action to pattern
////        pattern1="vectors.pattern_dft.BT1600_verA_20211029_prod_iddq__rev1p0_ser";
////        ISetupPattern pat1=deviceSetup1.getPatternRef(pattern1,0,108482 );
////        pat1.addActionsAtName("IddqTestPoint",ActionAnchoring.INTERRUPTIVE, action1);
////
////        deviceSetup1.sequentialBegin();
////            deviceSetup1.waitCall("2 ms");
////            deviceSetup1.patternCall(pat1);
////        deviceSetup1.sequentialEnd();
//
//        measurement1.setSetups(deviceSetup1);
//
//
//        IDeviceSetup deviceSetup2 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
//        deviceSetup2.importSpec(spec_measurement);
//
//        //power supply by dps128 channel
//        ISetupDcVI  dcVi_VCORE_2=deviceSetup2.addDcVI("VCORE_0p8").setDisconnect(true).setDisconnectModeHiz();
//        dcVi_VCORE_2.level().setIrange("100 mA").setVrange("6 V").setVforce(vforceValue).setIclamp("100 mA");
//        dcVi_VCORE_2.disconnect("disconnect_VCORE");
//
//        //open LDO output of pin VCORE_0p8
//        ISetupProtocolInterface paInterface2 = deviceSetup2.addProtocolInterface("I2C_BES", "besLib.pa.I2C_8bit_BES");
//        paInterface2.addSignalRole("DATA","I2C_SDA");
//        paInterface2.addSignalRole("CLK", "I2C_SCL");
//        ISetupTransactionSeqDef transDigSrc2= paInterface2.addTransactionSequenceDef("ldo_vcore_on");
//        transDigSrc2.addTransaction("PMU_INTERN_WRITE",0x14,0x0000);
//        if(StaticFields.pmic_name == "BT1806")
//        {
//            transDigSrc2.addTransaction("PMU_WRITE",0x15,0x53d4);
//            transDigSrc2.addTransaction("PMU_WRITE",0x0d,0x1514);
//        }
//
//        deviceSetup2.sequentialBegin();
//            deviceSetup2.waitCall("2 ms");
//            deviceSetup2.transactionSequenceCall(transDigSrc2);
//            deviceSetup2.waitCall("2 ms");
//            deviceSetup2.actionCall("disconnect_VCORE");
//            deviceSetup2.waitCall("2 ms");
//        deviceSetup2.sequentialEnd();
//
//        measurement2.setSetups(deviceSetup2);
    }

    @Override
    public void execute() {
//        // TODO Auto-generated method stub
//        measurement.execute();
//
//        measurement1.execute();
//        IDcVIResults DPSResult = measurement1.dcVI("VCORE_0p8").preserveResults();
//
//        measurement2.execute();
//
//
//        MultiSiteDouble[] iddq = DPSResult.imeas("imeas_VCORE").getCurrent("VCORE_0p8").getElements();
//        MultiSiteDouble iddq_max       = new MultiSiteDouble();
//        MultiSiteDouble iddq_min       = new MultiSiteDouble();
//        MultiSiteDouble iddq_sum       = new MultiSiteDouble(0);
//        MultiSiteDouble iddq_mean      = new MultiSiteDouble();
//        MultiSiteLong   iddq_max_index = new MultiSiteLong();
//        MultiSiteLong   iddq_min_index = new MultiSiteLong();
//        iddq_max.set(iddq[0]);
//        iddq_min.set(iddq[0]);
//        for(int i=0;i<iddq.length-1;i++) {
////            println("iddq["+i+"]= "+iddq[i].multiply(1e3)+" mA");
//            for(int site : context.getActiveSites())
//            {
//                if(iddq[i+1].get(site)>iddq_max.get(site))
//                {
//                    iddq_max.set(site, iddq[i+1].get(site));
//                    iddq_max_index.set(site, i+1);
//                }
//                if(iddq[i+1].get(site)<iddq_min.get(site))
//                {
//                    iddq_min.set(site, iddq[i+1].get(site));
//                    iddq_min_index.set(site, i+1);
//                }
//            }
//            iddq_sum = iddq_sum.add(iddq[i]);
//        }
//        iddq_mean = iddq_sum.divide(iddq.length);
//
//        if(StaticFields.debugMode)
//        {
//            println("iddq_min= "+iddq_min);
//            println("iddq_max= "+iddq_max);
//            println("iddq_min_index= "+iddq_min_index);
//            println("iddq_max_index= "+iddq_max_index);
//            println("iddq_sum= "+iddq_sum);
//            println("iddq_length= "+iddq.length);
//            println("iddq_mean= "+iddq_mean);
//        }
//
//        ptd_iddq_max      .evaluate(iddq_max);
//        ptd_iddq_max_index.evaluate(iddq_max_index);
//        ptd_iddq_min      .evaluate(iddq_min);
//        ptd_iddq_min_index.evaluate(iddq_min_index);
//        ptd_iddq_mean     .evaluate(iddq_mean);
    }

}
