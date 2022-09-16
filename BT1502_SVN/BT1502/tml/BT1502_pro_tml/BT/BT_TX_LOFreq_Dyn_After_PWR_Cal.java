
package BT1502_pro_tml.BT;

import BT1502_pro_tml.Global.StaticFields;
import besLib.cal.BesCalc_BTTX;
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

public class BT_TX_LOFreq_Dyn_After_PWR_Cal extends TestMethod {
    @In public String spec_measurement;
    @In public double freq;

    public IMeasurement measurement;

    public IParametricTestDescriptor    ptd_pwr,
                                        ptd_freq,
                                        ptd_loleak,
                                        ptd_imgrej,
                                        ptd_noise,
                                        ptd_pwrDelta_640K_l,
                                        ptd_pwrDelta_960K_r;

    private String RF_OUT="BT_RF";


    @Override
    public void setup() {

        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        ISetupRfMeas setup_meas=ds1.addRfMeas(RF_OUT).setConfigModeHighResolution().setConfigOptionSiteInterlacingOn();
        setup_meas. modPower("BTTX_Meas").
                    setFrequency(freq*1e6).
                    setExpectedMaxPower("10 dBm").
                    setBandwidthOfInterest("2 MHz").
                    setSamples(10000).
                    setIfFrequency("25 MHz").
                    setSampleRate("25 MHz").
                    setWindowFunction(SetupWindowFunction.hanning);

        ds1.sequentialBegin();
            ds1.setSequentialSyncAccuracy(0.001);
        ds1.actionCall("BTTX_Meas");
        ds1.sequentialEnd();
        measurement.setSetups(ds1);

    }

    @Override
    public void execute() {
        measurement.execute();

        IRfMeasResults rfResult=measurement.rfMeas(RF_OUT).preserveResults();
        MultiSiteWaveComplex tx_freq=rfResult.modPower("BTTX_Meas").getComplexWaveform(RF_OUT).getElement(0);

        BesCalc_BTTX bt_tx=new BesCalc_BTTX(tx_freq, 9);
        double ATT=0;
        ATT=ATT+StaticFields.att_bt.get(context.getActiveSites()[0]);
        bt_tx.setAtt(new MultiSiteDouble(ATT));

        if(freq==2402) {
            StaticFields.BTTX_PWR_L.set(bt_tx.getRfPower_dBm());
        }
        else if(freq==2480) {
            StaticFields.BTTX_PWR_H.set(bt_tx.getRfPower_dBm());
        }
        else if(freq==2441) {
            StaticFields.BTTX_PWR_M.set(bt_tx.getRfPower_dBm());
        }

        String testSuiteName_Qualified=context.getTestSuiteName();
        String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
        //For BTTX guard band
        MultiSiteDouble PWR_Noise=new MultiSiteDouble(0);
        if( freq==2490 || freq==2392) {
            MultiSiteDouble PWR_Noise_L= bt_tx.getSpecificBinPower_mW( bt_tx.getRfBinIndex().subtract(400), bt_tx.getRfBinIndex().subtract(200));
            MultiSiteDouble PWR_Noise_R= bt_tx.getSpecificBinPower_mW( bt_tx.getRfBinIndex().add(200), bt_tx.getRfBinIndex().add(400));
            PWR_Noise  = (PWR_Noise_L.add(PWR_Noise_R)).log10().multiply(10).add(ATT);
            ptd_noise.evaluate(PWR_Noise);
        }
        else {
            //fsig - (flo-5fbb) 640K
            long binIndex_640k=(long)(640/bt_tx.getBinResolution());
            MultiSiteDouble PWR_640K_L= bt_tx.getSpecificBinPower_mW( bt_tx.getRfBinIndex().subtract(binIndex_640k+4), bt_tx.getRfBinIndex().subtract(binIndex_640k-4));
            PWR_640K_L=PWR_640K_L.log10().multiply(10).add(ATT);

            //fsig - (flo+7fbb) 960K
            long binIndex_960k=(long)(960/bt_tx.getBinResolution());
            MultiSiteDouble PWR_960K_R= bt_tx.getSpecificBinPower_mW( bt_tx.getRfBinIndex().add(binIndex_960k-4), bt_tx.getRfBinIndex().add(binIndex_960k+4));
            PWR_960K_R=PWR_960K_R.log10().multiply(10).add(ATT);

            if(testSuiteName.contains("_After_PWR_Cal")) {//BT_TX_LOFreqL_After_PWR_Cal
                ptd_pwrDelta_640K_l.evaluate(bt_tx.getRfPower_dBm().subtract(PWR_640K_L));
                ptd_pwrDelta_960K_r.evaluate(bt_tx.getRfPower_dBm().subtract(PWR_960K_R));
            }

            ptd_loleak.evaluate(bt_tx.getLoLeakage());
            ptd_imgrej.evaluate(bt_tx.getImageRejection());
        }

        ptd_pwr.evaluate(bt_tx.getRfPower_dBm());
        ptd_freq.evaluate(bt_tx.getRfFrequency(freq));


        if(StaticFields.debugMode)
        {

            println("**********"+testSuiteName+"**********");
            tx_freq.plot(testSuiteName+"_waveform");
            bt_tx.getSpectrum_dBm().plot(testSuiteName+"_Spectrum");

            println("TX_LO_PWR (dBm) = "+bt_tx.getRfPower_dBm());
            println("Plo (dBm) = "+bt_tx.getLoPower_dBm());
            println("Pim (dBm) = "+bt_tx.getImagPower_dBm());
            println("TX_LO_FREQ (MHz) = "+bt_tx.getRfFrequency(freq).divide(1e6));
            println("Flo (MHz) = "+bt_tx.getLoFrequency(freq).divide(1e6));
            println("Fim (MHz) = "+bt_tx.getImagFrequency(freq).divide(1e6));
            println("TX_LO_LO (dB) = "+bt_tx.getLoLeakage());
            println("TX_LO_IMG (dB) = "+bt_tx.getImageRejection());
            println("PWR_Noise_2390/2490(dBm) =  "+PWR_Noise);
            println();
        }

    }
}
