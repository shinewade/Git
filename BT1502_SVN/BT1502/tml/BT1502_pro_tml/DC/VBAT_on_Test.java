package BT1502_pro_tml.DC;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_PS1600_Disconnect;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDcVI;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class VBAT_on_Test extends TestMethod {

    @In public String spec_measurement;
    @In public String spec_measurement_POWKEY_High;

    public IMeasurement measurement;
    public IParametricTestDescriptor ptd_I_vbat_on;

    @Override
    public void setup ()
    {
        IDeviceSetup ds1 = DeviceSetupFactory.createInstance(StaticFields.prog_name);

        BesDsa_PS1600_Disconnect relay_PS1600=new BesDsa_PS1600_Disconnect(ds1);
        if(StaticFields.dev_name=="BGA7273") {
            ds1.importSpec(spec_measurement_POWKEY_High);
        }
        else {
            ds1.importSpec(spec_measurement);
        }

        String activePin_PS1600=relay_PS1600.getPinGroup(context, spec_measurement, "All_Digital");
        activePin_PS1600=activePin_PS1600.replaceAll("POWKEY\\+", "");
        relay_PS1600.disconnectAll_DigInOut(activePin_PS1600, false); //disconnect digital pins to Hiz mode

        ISetupDcVI  dcVi_VBAT=ds1.addDcVI("VSYS").setDisconnect(false);
        dcVi_VBAT.imeas("imeas_VSYS").setIrange("125 uA").setAverages(128).setWaitTime("5 ms").setRestoreIrange(true);

        ds1.sequentialBegin();
              ds1.waitCall("400 ms");
              ds1.actionCall("imeas_VSYS");
        ds1.sequentialEnd();

        measurement.setSetups(ds1);
    }

    @Override
    public void execute () {
        measurement.execute();
        IDcVIResults dpsResult = measurement.dcVI("VSYS").preserveResults();
        MultiSiteDouble offCurrRslt=dpsResult.imeas("imeas_VSYS").getCurrent("VSYS").getElement(0);

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("I_power_down_VSYS[uA] = "+offCurrRslt.multiply(1e6));
            println();
        }

        ptd_I_vbat_on.evaluate(offCurrRslt);
    }
}
