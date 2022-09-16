package BT1502_pro_tml.BT;

import BT1502_pro_tml.Global.StaticFields;
import besLib.cal.BesCalc_BTTXModchar;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupRfMeas;
import xoc.dsa.ISetupRfMeas.IModPower.SetupWindowFunction;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.dsp.MultiSiteWaveComplex;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IRfMeasResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class BT_TX_ModChar_DF1 extends TestMethod {

    @In public String spec_measurement;

    public IMeasurement measurement1;
    public IParametricTestDescriptor    ptd_TX_CHAR_df1;

    private String RF_OUT="BT_RF";
    public static MultiSiteDouble dFRQ_Delta1_F1_Global=new MultiSiteDouble();

    @Override
    public void setup() {
        //measurement1
        IDeviceSetup deviceSetup1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup1.importSpec(spec_measurement);
        deviceSetup1.setSequentialSyncAccuracy(0.001);

        ISetupRfMeas setup_meas=deviceSetup1.addRfMeas(RF_OUT).setConfigModeHighResolution().setConfigOptionSiteInterlacingOn();//16 bit, 250Msps Max
        setup_meas. modPower("BTTX_Meas").
                    setFrequency("2.40181 GHz").
                    setExpectedMaxPower("20 dBm").
                    setSamples(2000).
                    setBandwidthOfInterest("2 MHz").
                    setIfFrequency("12.5 MHz").
                    setSampleRate("20 MHz").
                    setWindowFunction(SetupWindowFunction.hanning);

        deviceSetup1.actionCall("BTTX_Meas");
        measurement1.setSetups(deviceSetup1);
    }

    @Override
    public void execute() {
        measurement1.execute();
        IRfMeasResults rfResult = measurement1.rfMeas(RF_OUT).preserveResults();
        MultiSiteWaveComplex waveform_F1 = rfResult.modPower("BTTX_Meas").getComplexWaveform(RF_OUT).getElement(0);
        waveform_F1.plot("f0");
        BesCalc_BTTXModchar bt_tx=new BesCalc_BTTXModchar(waveform_F1, besLib.cal.BesCalc_BTTXModchar.payloadSeq.F0);

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("dFRQ_Delta1_F1 = "+bt_tx.getDf1().divide(1e3));
            println();
        }
        dFRQ_Delta1_F1_Global.set(bt_tx.getDf1());
        ptd_TX_CHAR_df1.evaluate(bt_tx.getDf1().divide(1e3));

    }

}


