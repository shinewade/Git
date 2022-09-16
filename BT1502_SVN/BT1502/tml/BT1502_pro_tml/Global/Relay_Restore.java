package BT1502_pro_tml.Global;

import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.measurement.IMeasurement;

public class Relay_Restore extends TestMethod {

     public IMeasurement measurement;
     private String utiGroup1;

     @Override
    public void setup() {
        // TODO Auto-generated method stub
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        if(StaticFields.dev_name == "BGA7273")
        {
            utiGroup1="K22+K7";
        }
        else if (StaticFields.dev_name == "WLCSP") {
            utiGroup1="K7";
        }
        deviceSetup.addUtility(utiGroup1).setValue(0);

        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
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
