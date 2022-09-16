package BT1502_pro_tml.AUDIO;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_PMIC;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigInOutActionResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class AUDIO_Micbias_TEST extends TestMethod {

    @In public String spec_measurement;

    public IMeasurement measurement;

    public IParametricTestDescriptor    ptd_V_VMIC1,
                                        ptd_V_VMIC2,
                                        ptd_V_VMIC3,
                                        ptd_V_VMIC5,
                                        ptd_V_VCM_CAP,
                                        ptd_V_SAR_VREFP;

    @Override
    public void setup() {
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement);

        new BesDsa_PMIC(deviceSetup).vMeas_PS1600_DigInOut_Hiz("VMIC1", false, "Vm_VMIC1");
        new BesDsa_PMIC(deviceSetup).vMeas_PS1600_DigInOut_Hiz("VMIC2", false, "Vm_VMIC2");
        new BesDsa_PMIC(deviceSetup).vMeas_PS1600_DigInOut_Hiz("VCM_CAP", false, "Vm_VCM_CAP");
        new BesDsa_PMIC(deviceSetup).vMeas_PS1600_DigInOut_Hiz("SAR_VREFP", false, "Vm_SAR_VREFP");

        deviceSetup.actionCall("Vm_VMIC1");
        deviceSetup.actionCall("Vm_VMIC2");
        deviceSetup.actionCall("Vm_VCM_CAP");
        deviceSetup.actionCall("Vm_SAR_VREFP");
        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute() {

        measurement.execute();
        IDigInOutActionResults digInoutRes=measurement.digInOut().preserveActionResults();
        MultiSiteDouble V_VMIC1 = new MultiSiteDouble();
        MultiSiteDouble V_VMIC2 = new MultiSiteDouble();
        MultiSiteDouble V_VCM_CAP   = new MultiSiteDouble();
        MultiSiteDouble sar_vref = new MultiSiteDouble();
        V_VMIC1=digInoutRes.vmeas("Vm_VMIC1").getVoltage("VMIC1").getElement(0);
        V_VMIC2=digInoutRes.vmeas("Vm_VMIC2").getVoltage("VMIC2").getElement(0);
        V_VCM_CAP  =digInoutRes.vmeas("Vm_VCM_CAP").getVoltage("VCM_CAP").getElement(0);
        sar_vref      =digInoutRes.vmeas("Vm_SAR_VREFP").getVoltage("SAR_VREFP").getElement(0);

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("ptd_V_VMIC1   = "+V_VMIC1);
            println("ptd_V_VMIC2   = "+V_VMIC2);
            println("ptd_V_VCM_CAP = "+V_VCM_CAP);
            println();
        }

        ptd_V_VMIC1.evaluate(V_VMIC1);
        ptd_V_VMIC2.evaluate(V_VMIC2);
        ptd_V_VCM_CAP.evaluate(V_VCM_CAP);
//        ptd_V_SAR_VREFP.evaluate(sar_vref);
    }
}
