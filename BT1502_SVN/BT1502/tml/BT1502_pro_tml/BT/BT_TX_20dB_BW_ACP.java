package BT1502_pro_tml.BT;

import BT1502_pro_tml.Global.OffsetCompensation;
import BT1502_pro_tml.Global.OffsetCompensation.OffsetType;
import BT1502_pro_tml.Global.StaticFields;
import besLib.cal.BesCalc_BTTXACPR;
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

public class BT_TX_20dB_BW_ACP extends TestMethod {

    @In public String spec_measurement;
    public IMeasurement measurement1;
    public IParametricTestDescriptor    ptd_BT_TX_20dB_Peak,
                                        ptd_BT_TX_ACPR_0,
                                        ptd_BT_TX_ACPR_M3,
                                        ptd_BT_TX_ACPR_M2,
                                        ptd_BT_TX_ACPR_P2,
                                        ptd_BT_TX_ACPR_P3;

    private String RF_OUT="BT_RF";

    @Override
    public void setup() {
        IDeviceSetup deviceSetup1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup1.importSpec(spec_measurement);
        deviceSetup1.setSequentialSyncAccuracy(0.001);

        ISetupRfMeas setup_meas=deviceSetup1.addRfMeas(RF_OUT).setConfigModeHighResolution().setConfigOptionSiteInterlacingOn();//16 bit, 250Msps Max
        setup_meas. modPower("BTTX_Meas").
                    setFrequency("2.40196 GHz").
                    setExpectedMaxPower("15 dBm").
                    setSamples(10000).
                    setBandwidthOfInterest("10 MHz").
                    setIfFrequency("25 MHz").
                    setSampleRate("25 MHz").
                    setWindowFunction(SetupWindowFunction.hanning);

        deviceSetup1.actionCall("BTTX_Meas");
        measurement1.setSetups(deviceSetup1);
    }

    @Override
    public void execute() {
        measurement1.execute();
        IRfMeasResults rfResult = measurement1.rfMeas(RF_OUT).preserveResults();
        MultiSiteWaveComplex waveform = rfResult.modPower("BTTX_Meas").getComplexWaveform(RF_OUT).getElement(0);

        BesCalc_BTTXACPR bt_tx=new BesCalc_BTTXACPR(waveform);
        double ATT=0;
        ATT=ATT+StaticFields.att_bt.get(context.getActiveSites()[0]);
        MultiSiteDouble offset= new OffsetCompensation().getOffsetValue(StaticFields.LBID,OffsetType.BT_TX_Power);
        bt_tx.setAtt(new MultiSiteDouble(ATT).add(offset));//10dB ATT + site offset + LB offset

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            waveform.plot(testSuiteName+"_waveform");
            bt_tx.getSpectrum_dBm().plot(testSuiteName+"_Spectrum");
            println("BT_TX_20dB_Peak (KHz) = "+bt_tx.getRfBandwidth_20dB().divide(1e3));//KHz
            println("BT_TX_ACPR_0 (dBm) = "+bt_tx.getRfChannelPower_dBm());
            println("BT_TX_ACPR_M3 = "+bt_tx.getACPR_M3());
            println("BT_TX_ACPR_M2 = "+bt_tx.getACPR_M2());
            println("BT_TX_ACPR_P2 = "+bt_tx.getACPR_P2());
            println("BT_TX_ACPR_P3 = "+bt_tx.getACPR_P3());
            println();
        }
        ptd_BT_TX_20dB_Peak.evaluate(bt_tx.getRfBandwidth_20dB().divide(1e3));
        ptd_BT_TX_ACPR_0.evaluate(bt_tx.getRfChannelPower_dBm());
        ptd_BT_TX_ACPR_M3.evaluate(bt_tx.getACPR_M3());
        ptd_BT_TX_ACPR_M2.evaluate(bt_tx.getACPR_M2());
        ptd_BT_TX_ACPR_P2.evaluate(bt_tx.getACPR_P2());
        ptd_BT_TX_ACPR_P3.evaluate(bt_tx.getACPR_P3());



    }

}


