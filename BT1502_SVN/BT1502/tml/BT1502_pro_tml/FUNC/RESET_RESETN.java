package BT1502_pro_tml.FUNC;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDigInOut;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.measurement.IMeasurement;


public class RESET_RESETN extends TestMethod {

    @In public String spec_measurement;
    @In public String pattern1;
    @In public double LevSpec_VBAT = 4.0;

    public IMeasurement measurement;

    @Override
    public void setup()
    {
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement);


        //POWKEY
        ISetupDigInOut digInOut_RESETN=deviceSetup.addDigInOut("RESETN").setConnect(true).setDisconnect(true);
        digInOut_RESETN.protection().setDisconnectPulldownState(false);
        digInOut_RESETN.vforce("Vf_RESETN_0V").setForceValue("0 V").setIclamp("40 mA");
        digInOut_RESETN.vforce("Vf_RESETN_1p8V").setForceValue("1.8 V").setIclamp("40 mA");

        deviceSetup.sequentialBegin();
            deviceSetup.actionCall("Vf_RESETN_0V");
            deviceSetup.waitCall("5 ms");
            deviceSetup.actionCall("Vf_RESETN_1p8V");
            deviceSetup.waitCall("50 ms");
            deviceSetup.actionCall("Vf_RESETN_0V");
            deviceSetup.waitCall("100 ms");
        deviceSetup.sequentialEnd();
        measurement.setSetups(deviceSetup);
    }

    @Override
    public void update ()
    {
        measurement.spec().setVariable("Vbat", LevSpec_VBAT);
    }

    @Override
    public void execute ()
    {

        measurement.execute();

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("measurement_VBAT_LevSpec  [V] = "+measurement.dcVI("VSYS").level().getVforce());
            println();
        }
    }
}
