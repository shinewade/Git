package BT1502_pro_tml.DC;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_PS1600_Disconnect;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDcVI;
import xoc.dsa.ISetupDigInOut;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class Power_on_Test extends TestMethod {

    @In public String spec_measurement;
    public IMeasurement measurement;

    public IParametricTestDescriptor ptd_I_power_on;

    @Override
    public void setup  ()
    {
        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        //disconnect other digital pins
        BesDsa_PS1600_Disconnect relay_PS1600=new BesDsa_PS1600_Disconnect(ds1);
        String activePin_PS1600=relay_PS1600.getPinGroup(context, spec_measurement, "All_Digital");

        String disconnectPin_PS1600=activePin_PS1600.replaceAll("POWKEY\\+", "");
        relay_PS1600.disconnectAll_DigInOut(disconnectPin_PS1600, false); //disconnect digital pins to Hiz mode

        //POWKEY
        ISetupDigInOut digInOut_POWKEY=ds1.addDigInOut("POWKEY").setConnect(true).setDisconnect(true);
        digInOut_POWKEY.protection().setDisconnectPulldownState(false);
        digInOut_POWKEY.vforce("Vf_POWKEY_0V").setForceValue("0 V").setIclamp("40 mA");
        digInOut_POWKEY.vforce("Vf_POWKEY_1p8V").setForceValue("1.8 V").setIclamp("40 mA");


        //VBAT
        ISetupDcVI  dcVi_VBAT=ds1.addDcVI("VSYS").setDisconnect(false);
        dcVi_VBAT.imeas("OnCurrent").setIrange("100 mA").setAverages(128).setRestoreIrange(true);
//            dcVi_VBAT.imeas("OnCurrent").setUngangMode(SetupUngangMode.fast).setIrange("100 mA").setAverages(128).setRestoreIrange(true);

        ds1.sequentialBegin();

            ds1.actionCall("Vf_POWKEY_0V");
            ds1.waitCall("5 ms");
            ds1.actionCall("Vf_POWKEY_1p8V");
            ds1.waitCall("20 ms");
            ds1.actionCall("Vf_POWKEY_0V");
            ds1.waitCall("200 ms");

            ds1.actionCall("OnCurrent");
        ds1.sequentialEnd();
        measurement.setSetups(ds1);

    }

    @Override
    public void execute () {
        measurement.execute();
        IDcVIResults DPSResult = measurement.dcVI("VSYS").preserveResults();
        MultiSiteDouble onCurrRslt = DPSResult.imeas("OnCurrent").getCurrent("VSYS").getElement(0);

        String testSuiteName_Qualified=context.getTestSuiteName();
        String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
        if(StaticFields.debugMode)
        {
            println("**********"+testSuiteName+"**********");
            println("onCurrRslt[mA] = "+onCurrRslt.multiply(1e3));
            println();
        }

        if(testSuiteName.equals("Power_on_Test")) {
            ptd_I_power_on.evaluate(onCurrRslt);
        }
    }
}
