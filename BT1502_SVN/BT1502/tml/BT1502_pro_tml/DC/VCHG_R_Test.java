package BT1502_pro_tml.DC;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class VCHG_R_Test extends TestMethod {

    public IMeasurement measurement;

    public IParametricTestDescriptor ptd_VCHG_R_Test;

    @Override
    public void setup  ()
    {
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);

        deviceSetup.addDcVI("VCHG_R").vforce("Vforce_VCHG_R").setForceValue("4.80 V").setIrange("40 mA").setVrange("5.00 V").setIclamp("40 mA");
        deviceSetup.addDcVI("VCHG_R").disconnect("Disconnect_VCHG_R");
        deviceSetup.addDcVI("VSYS").imeas("OnCurrent").setIrange("100 mA").setAverages(128).setRestoreIrange(true);
        deviceSetup.addDcVI("VSYS").level().setIrange("500 mA").setVrange("6 V");

        deviceSetup.sequentialBegin();
            deviceSetup.actionCall("Vforce_VCHG_R");
            deviceSetup.waitCall("50 ms");
            deviceSetup.actionCall("OnCurrent");
            deviceSetup.waitCall("2 ms");
            deviceSetup.actionCall("Disconnect_VCHG_R");
        deviceSetup.sequentialEnd();
        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute ()
    {
        measurement.execute();

        IDcVIResults DPSResult = measurement.dcVI("VSYS").preserveResults();

        MultiSiteDouble onCurrRslt = DPSResult.imeas("OnCurrent").getCurrent("VSYS").getElement(0);

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("onCurrRslt[mA] = "+onCurrRslt.multiply(1e3));
            println();
        }

        ptd_VCHG_R_Test.evaluate(onCurrRslt);
    }
}










//package BT1502_pro_tml.DC;
//
//import java.util.List;
//
//import BT1502_pro_tml.Global.StaticFields;
//import xoc.dsa.DeviceSetupFactory;
//import xoc.dsa.IDeviceSetup;
//import xoc.dsa.ISetupDigInOut;
//import xoc.dta.TestMethod;
//import xoc.dta.annotations.In;
//import xoc.dta.datatypes.MultiSiteDouble;
//import xoc.dta.measurement.IMeasurement;
//import xoc.dta.resultaccess.IDcVIResults;
//import xoc.dta.testdescriptor.IParametricTestDescriptor;
//
//
//public class VCHG_R_Test extends TestMethod {
//
//    public IMeasurement measurement;
//    @In public String spec_measurement;
//
//    public IParametricTestDescriptor ptd_VCHG_R_Test;
//
//    @Override
//    public void setup  ()
//    {
//        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);
//        deviceSetup.importSpec(spec_measurement);
//
//        List<String> signalsList=context.inspection().initialSpec("configuration.Groups").getSignalGroup("CONT_neg");
//        String signals=String.join("+", signalsList);
//        signals = signals.replace("+VCHG_R", "");
//        StaticFields.Set_AC_Off(deviceSetup,signals);
//
//        ISetupDigInOut digInOut=deviceSetup.addDigInOut("VCHG_R").setDisconnect(true);
//        digInOut.frontendConnection("Disconnect_VCHG_R").setDigitalFrontendDisable();
//        digInOut.vforce("Vforce_VCHG_R").setForceValue("4.80 V").setIrange("40 mA").setVrange("5.00 V").setIclamp("40 mA");
//
//        deviceSetup.addDcVI("VSYS").imeas("OnCurrent").setIrange("100 mA").setAverages(16).setRestoreIrange(true);
//        deviceSetup.addDcVI("VSYS").level().setIrange("500 mA").setVrange("6 V");
//
//        deviceSetup.sequentialBegin();
//            deviceSetup.actionCall("Vforce_VCHG_R");
//            deviceSetup.actionCall("Disconnect_VCHG_R");
//            deviceSetup.waitCall("100 ms");
//            deviceSetup.actionCall("OnCurrent");
//        deviceSetup.sequentialEnd();
//        measurement.setSetups(deviceSetup);
//    }
//
//    @Override
//    public void execute ()
//    {
//        measurement.execute();
//
//        IDcVIResults DPSResult = measurement.dcVI("VSYS").preserveResults();
//
//        MultiSiteDouble onCurrRslt = DPSResult.imeas("OnCurrent").getCurrent("VSYS").getElement(0);
//
//        if(StaticFields.debugMode)
//        {
//            String testSuiteName_Qualified=context.getTestSuiteName();
//            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
//            println("**********"+testSuiteName+"**********");
//            println("onCurrRslt[mA] = "+onCurrRslt.multiply(1e3));
//            println();
//        }
//
//        ptd_VCHG_R_Test.evaluate(onCurrRslt);
//    }
//}
