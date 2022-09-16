package BT1502_pro_tml.DC;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDcVI;
import xoc.dsa.ISetupDcVI.SetupDisconnectMode;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.measurement.IMeasurement;


public class Disconnect extends TestMethod {

    @In public String spec_measurement;
    public IMeasurement measurement;

    @Override
    public void setup() {
        // TODO Auto-generated method stub
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);

//        StaticFields.Set_AC_Off_All(deviceSetup);
//        StaticFields.Set_PMU_Off_All(deviceSetup);

        String testPins = "VBAT+VCORE_0p4+VCORE_0p6+VCORE_0p8+VCODEC+VIO+VANA+VCODEC_HPPA+VPA_BT+VMEM";
        ISetupDcVI dcVI_Pin = deviceSetup.addDcVI(testPins).setConnect(false).setDisconnect(true).setDisconnectMode(SetupDisconnectMode.safe);
        dcVI_Pin.level().setIrange("100 mA").setIclamp("100 mA").setVrange("6 V").setVforce("0 V");
        dcVI_Pin.disconnect("Disconnect_Pins");

        deviceSetup.sequentialBegin();
        {
            deviceSetup.waitCall("5 ms");
            deviceSetup.actionCall("Disconnect_Pins");
            deviceSetup.waitCall("5 ms");
        }
        deviceSetup.sequentialEnd();
        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute() {
        // TODO Auto-generated me
        measurement.execute();
        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println();
            println("**********"+testSuiteName+"**********");

        }

    }
}
