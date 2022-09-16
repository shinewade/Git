package BT1502_pro_tml.DC;

import java.util.HashMap;
import java.util.Map;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_OS_Func;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteBoolean;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.measurement.ILocalMeasurement;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigInOutPassFailResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class Continuity_walking_z extends TestMethod {

    @In
    public String spec_measurement;

    public ILocalMeasurement measurement;
    public IMeasurement measurement_relayOff;

    public IParametricTestDescriptor ptd_OS_Neg_Func;

    private String utiGroup;
    private String signals;

    @Override
    public void setup() {
        // measurement_relayOff
        IDeviceSetup ds1_relayOff = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        if (StaticFields.dev_name == "BGA7273") {
            ds1_relayOff.addUtility("K7+K32+K33+K35+K37").setValue(0);
        } else if (StaticFields.dev_name == "WLCSP") {
            ds1_relayOff.addUtility("K7+K25+K26+K39+K41").setValue(0);
        }
        measurement_relayOff.setSetups(ds1_relayOff);

        // measurement
        IDeviceSetup deviceSetupFunc = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetupFunc.importSpec(spec_measurement);

        if (StaticFields.dev_name == "BGA7273") {
            deviceSetupFunc.addUtility("K7+K32+K33+K35+K37").setValue(0);
        } else if (StaticFields.dev_name == "WLCSP") {
            deviceSetupFunc.addUtility("K7+K25+K26+K39+K41").setValue(0);
        }

        BesDsa_OS_Func os = new BesDsa_OS_Func(deviceSetupFunc, measurement);
        // [1] setPinGroup
        os.setPinGroup(context, "configuration.Groups", "All_Digital");
        // [2] removePin (PSRAM & Flash0 & VCHG_R)
        if (StaticFields.dev_name == "BGA7273") {
            if (StaticFields.dev_ssv == "A")
        {
                os.removePin("FLASH0_CLK").removePin("FLASH0_CS").removePin("FLASH0_HOLD")
                .removePin("FLASH0_SI").removePin("FLASH0_SO").removePin("FLASH0_WP");
        }
        }
        os.removePin("PSRAM200M_IO_ADQ0").removePin("PSRAM200M_IO_ADQ1")
                .removePin("PSRAM200M_IO_ADQ2").removePin("PSRAM200M_IO_ADQ3")
                .removePin("PSRAM200M_IO_ADQ4").removePin("PSRAM200M_IO_ADQ5")
                .removePin("PSRAM200M_IO_ADQ6").removePin("PSRAM200M_IO_ADQ7")
                .removePin("PSRAM200M_IO_DQS").removePin("PSRAM200M_TX_CEB")
                .removePin("PSRAM200M_TX_DM").removePin("PSRAM201M_TX_CLKN")
                .removePin("PSRAM202M_TX_CLKP").removePin("VCHG_R");
        signals = os.getPinGroup();
        // [3] addWaitCycle
        if (StaticFields.dev_name == "BGA7273") {

            os.addWaitCycle("VCM_CAP", 1000);// 2.5 us per cycle
            os.addWaitCycle("ADC0", 1000);// 2.5 us per cycle
            os.addWaitCycle("ADC1", 1000);// 2.5 us per cycle
            os.addWaitCycle("ADC2", 1000);// 2.5 us per cycle
            os.addWaitCycle("ADC3", 1000);// 2.5 us per cycle
            os.addWaitCycle("VMIC1", 1000);// 2.5 us per cycle
            os.addWaitCycle("VMIC2", 1000);// 2.5 us per cycle
            os.addWaitCycle("ADC_VREF", 1000);// 2.5 us per cycle
            os.addWaitCycle("VUSB", 1000);// 2.5 us per cycle
            os.addWaitCycle("SAR_VREFP", 1000);// 2.5 us per cycle
            os.addWaitCycle("DSI_CKP", 3000);// 2.5 us per cycle
            os.addWaitCycle("DSI_DP0", 3000);// 2.5 us per cycle
            os.addWaitCycle("DSI_CKN", 3000);// 2.5 us per cycle
            os.addWaitCycle("DSI_DN0", 3000);// 2.5 us per cycle
        } else if (StaticFields.dev_name == "WLCSP") {
            os.addWaitCycle("ADC_VREF", 1000);// 2.5 us per cycle
            os.addWaitCycle("VUSB", 1000);// 2.5 us per cycle
            os.addWaitCycle("VMIC1", 1000);// 2.5 us per cycle
            os.addWaitCycle("VMIC2", 1000);// 2.5 us per cycle
            os.addWaitCycle("VCM_CAP", 1000);// 2.5 us per cycle
            os.addWaitCycle("ADC0", 1000);// 2.5 us per cycle
            os.addWaitCycle("ADC2", 1000);// 2.5 us per cycle
            os.addWaitCycle("SAR_VREFP", 1000);// 2.5 us per cycle
            os.addWaitCycle("VOUT_1P5", 3000);// 2.5 us per cycle
        }
        // [4] genWalkingZPattern
        os.genWalkingZPattern();
        // [5] genOperatingSequence
        os.genOperatingSequence();
        measurement.setSetups(deviceSetupFunc);

    }

    @Override
    public void execute() {
        measurement_relayOff.execute();

        measurement.digInOut().result().capture().setEnabled(false);
        measurement.digInOut().result().callPassFail().setEnabled(true);
        measurement.execute();
        IDigInOutPassFailResults Res = measurement.digInOut(signals).preservePassFailResults();
        Map<String, MultiSiteBoolean> res_Func = Res.hasComparePassed();

        Map<String, MultiSiteDouble> res_Func_new = new HashMap<String, MultiSiteDouble>();

        if (StaticFields.debugMode) {
            String testSuiteName_Qualified = context.getTestSuiteName();
            String testSuiteName = testSuiteName_Qualified
                    .substring(1 + testSuiteName_Qualified.lastIndexOf("."));
            println("**********" + testSuiteName + "**********");
        }
        for (String PinName : res_Func.keySet()) {
            if (StaticFields.debugMode) {
                println(PinName + " " + res_Func.get(PinName));
            }
            MultiSiteDouble temp = new MultiSiteDouble(0);
            for (int site : context.getActiveSites()) {
                if (res_Func.get(PinName).get(site)) {
                    temp.set(site, 1);
                }
            }
            res_Func_new.put(PinName, temp);
        }

        ptd_OS_Neg_Func.evaluate(res_Func_new, measurement);

    }
}
