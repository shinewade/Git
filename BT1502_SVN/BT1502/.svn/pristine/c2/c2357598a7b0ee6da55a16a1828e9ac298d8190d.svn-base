package BT1502_pro_tml.DC;

import java.util.Map;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_OS_PMU;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDoubleArray;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class Continuity_PMU extends TestMethod {

    @In public String spec_measurement;

    public IMeasurement measurement;


    public IParametricTestDescriptor ptd_OS_Neg_Pmu;

    private String signals;

    @Override
    public void setup() {

        //measurement
        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        if(StaticFields.dev_name == "BGA7273") {
            ds1.addUtility("K7+K32+K33+K35+K37").setValue(0);
          }
         else if (StaticFields.dev_name == "WLCSP") {
             ds1.addUtility("K7+K25+K26+K39+K41").setValue(0);
         }


        BesDsa_OS_PMU os=new BesDsa_OS_PMU(ds1, measurement);
        //[1] setPinGroup
        os.setPinGroup(context, "configuration.Groups", "All_Digital");
        //[2] removePin (PSRAM & Flash0 & VCHG_R)
        if (StaticFields.dev_name == "BGA7273") {
            if (StaticFields.dev_ssv == "A")
        {
                os.removePin("FLASH0_CLK").removePin("FLASH0_CS").removePin("FLASH0_HOLD")
                .removePin("FLASH0_SI").removePin("FLASH0_SO").removePin("FLASH0_WP");
        }
        }
        os.removePin("PSRAM200M_IO_ADQ0").removePin("PSRAM200M_IO_ADQ1").removePin("PSRAM200M_IO_ADQ2").removePin("PSRAM200M_IO_ADQ3").
        removePin("PSRAM200M_IO_ADQ4").removePin("PSRAM200M_IO_ADQ5").removePin("PSRAM200M_IO_ADQ6").removePin("PSRAM200M_IO_ADQ7").
        removePin("PSRAM200M_IO_DQS").removePin("PSRAM200M_TX_CEB").removePin("PSRAM200M_TX_DM").removePin("PSRAM201M_TX_CLKN").removePin("PSRAM202M_TX_CLKP").
        removePin("VCHG_R");
        signals=os.getPinGroup();
     //   System.out.println("signals = "+signals);
        //[3] set iForce and vMeas parameter
//        os.setiForceValue(-200e-6);
        os.setSettlingTime(0.5e-3);
        os.setWaitTime(2e-3);
        //[4] specialPin
        if (StaticFields.dev_name == "BGA7273") {
            os.specialPin("DSI_CKP", -700*1e-6, 1e-3, 2, -1.5,2*1e-3,5*1e-3);
            os.specialPin("DSI_CKN", -700*1e-6, 1e-3, 2, -1.5,2*1e-3,5*1e-3);
            os.specialPin("DSI_DP0", -700*1e-6, 1e-3, 2, -1.5,2*1e-3,5*1e-3);
            os.specialPin("DSI_DN0", -700*1e-6, 1e-3, 2, -1.5,2*1e-3,5*1e-3);
            os.specialPin("SAR_VREFP", -700*1e-6, 1e-3, 2, -1.5,2*1e-3,5*1e-3);

        }
        else if (StaticFields.dev_name == "WLCSP")
        {
            os.specialPin("SAR_VREFP", -700*1e-6, 1e-3, 2, -1.5,2*1e-3,5*1e-3);
        }
        //[5] genSSF_OS_PMU
        os.genSSF_OS_PMU();



        measurement.setSetups(ds1);



    }

    @Override
    public void execute()
    {
        measurement.execute();
        IDcVIResults Res=measurement.dcVI(signals).preserveResults();
        Map<String, MultiSiteDoubleArray> Res_PMU=Res.vmeas("").getVoltage();
//        measurement_relayOff.execute();

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println();
            println("**********"+testSuiteName+"**********");
            for(String PinName:Res_PMU.keySet()) {
                println("Res_OS_PMU "+PinName+Res_PMU.get(PinName).getElement(0));
            }
            println();
        }

        ptd_OS_Neg_Pmu.evaluate(Res_PMU,0,measurement);
    }
}
