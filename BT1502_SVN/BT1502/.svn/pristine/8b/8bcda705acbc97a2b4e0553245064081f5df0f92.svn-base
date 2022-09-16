package BT1502_pro_tml.DC;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class VBAT_CUEERNT_TEST extends TestMethod {

    @In public String spec_measurement;

    public IMeasurement measurement;

    public IParametricTestDescriptor ptd_I_power_on;

    @Override
    public void setup  ()
    {
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement);

        deviceSetup.addDcVI("VSYS").imeas("OnCurrent").setIrange("100 mA").setAverages(16).setRestoreIrange(true);

        deviceSetup.sequentialBegin();
            deviceSetup.waitCall("10 ms");
            deviceSetup.actionCall("OnCurrent");
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

        ptd_I_power_on.evaluate(onCurrRslt);
    }
}
