package BT1502_pro_tml.AUDIO;

import java.util.Map;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDigitizer;
import xoc.dsa.ISetupDigitizer.IMeasureWaveform.SetupInputImpedance;
import xoc.dsa.ISetupProtocolInterface;
import xoc.dsa.ISetupTransactionSeqDef;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.dsp.MultiSiteWaveDouble;
import xoc.dta.datatypes.dsp.MultiSiteWaveDoubleArray;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigitizerResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class AUDIO_DAC_DC_TEST extends TestMethod {

    @In public String spec_measurement;
    @In public String lout_mode;            //LR: -> LOUT_L & LOUT_R OUT
                                            //L: -> LOUT_L OUT
                                            //R: -> LOUT_R OUT

    public IMeasurement measurement;
    public IMeasurement measurement1;

    private String dgt_Ip = "DGT_LP";
    private String dgt_Qp = "DGT_RP";
    private String LOUT   = "DGT_LP+DGT_RP";

    private String DGT_Action="DGT_Measure";

    private double dgtInputRange;
//    private double dgtDcOffset;
    private double dgtSamplingRate;
    private long   dgtNumOfSamples;
    private double dgtLpfValue;

    public IParametricTestDescriptor    ptd_RX_DC_L_imb,
                                        ptd_RX_DC_R_imb;

    @Override
    public void setup() {

        IDeviceSetup deviceSetup1=DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup1.importSpec(spec_measurement);

        ISetupProtocolInterface paInterface = deviceSetup1.addProtocolInterface("I2C_BES", "besLib.pa.I2C_8bit_BES");
        paInterface.addSignalRole("DATA", "I2C_SDA");
        paInterface.addSignalRole("CLK", "I2C_SCL");
        ISetupTransactionSeqDef transDigSrc= paInterface.addTransactionSequenceDef("AUDIO_CURRENT_TEST");
        transDigSrc.addTransaction("DIGITAL_WRITE",0x403000b0,0x00000000);
        transDigSrc.addWait("10 ms");

        deviceSetup1.sequentialBegin();
            deviceSetup1.transactionSequenceCall(transDigSrc);
        deviceSetup1.sequentialEnd();
        measurement1.setSetups(deviceSetup1);



        if(StaticFields.dev_name == "BGA4173")
        {
            lout_mode = "LR";
        }
        if(StaticFields.dev_name == "BGA7273")
        {
            lout_mode = "R";
        }
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);

//        deviceSetup.addUtility("K13+K14").setValue(0);

//        dgtInputRange    = 0.26;                        // [V] 0.26
//        dgtDcOffset      = 0.0;                        // [V] 0.0
//        dgtSamplingRate  = 200*1e3;
//        dgtNumOfSamples  = 1024;
//        dgtLpfValue      = 6*1e3;                      // [Hz]6*1e3;

        dgtInputRange    = 0.23;                        // [V] 0.26
//        dgtDcOffset      = 0.0;                        // [V] 0.0
        dgtSamplingRate  = 200*1e3;
        dgtNumOfSamples  = 1024;
        dgtLpfValue      = 50*1e3;                      // [Hz]

        if(lout_mode == "LR")     {LOUT = "DGT_LP+DGT_RP";}//LOUT_L&LOUT_R
        else if(lout_mode == "L") {LOUT = "DGT_LP";        }//LOUT_L
        else if(lout_mode == "R") {LOUT = "DGT_RP";        }//LOUT_R
        else                      {LOUT = "DGT_LP+DGT_RP";}//LOUT_L&LOUT_R

        ISetupDigitizer dgtSetup = deviceSetup.addDigitizer(LOUT).setConfigOptionDifferential().setConnect(true).setDisconnect(true);
        ISetupDigitizer.IMeasureWaveform dgtCapWave = dgtSetup.measureWaveform(DGT_Action);
        dgtCapWave.setExpectedAmplitude(dgtInputRange)
//                  .setOffset(dgtDcOffset)
//                  .setOffsetNeg(dgtDcOffset)
                  .setSampleRate(dgtSamplingRate)
                  .setSamples(dgtNumOfSamples)
                  .setLowPassFilterBandwidth(dgtLpfValue)
                  .setInputImpedance(SetupInputImpedance.HighZ);

        deviceSetup.sequentialBegin();
            deviceSetup.waitCall("50 ms");
            deviceSetup.actionCall(DGT_Action);
        deviceSetup.sequentialEnd();
        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute() {
        measurement1.execute();

        measurement.execute();
        IDigitizerResults resultsDgt = measurement.digitizer(LOUT).preserveResults();
        Map<String, MultiSiteWaveDoubleArray> waveforms = resultsDgt.measureWaveform(DGT_Action).getWaveform();
        MultiSiteWaveDouble waveform_L = new MultiSiteWaveDouble();
        MultiSiteWaveDouble waveform_R = new MultiSiteWaveDouble();
        MultiSiteDouble mean_L = new MultiSiteDouble();
        MultiSiteDouble mean_R = new MultiSiteDouble();

        if(lout_mode == "LR")
        {
            waveform_L = waveforms.get(dgt_Ip).getElement(0);
            waveform_R = waveforms.get(dgt_Qp).getElement(0);
        }
        else if(lout_mode == "L")
        {
            waveform_L = waveforms.get(dgt_Ip).getElement(0);
            waveform_R = waveforms.get(dgt_Ip).getElement(0);
        }
        else if(lout_mode == "R")
        {
            waveform_L = waveforms.get(dgt_Qp).getElement(0);
            waveform_R = waveforms.get(dgt_Qp).getElement(0);
        }

        mean_L = waveform_L.mean();
        mean_R = waveform_R.mean();

        if(StaticFields.debugMode)//StaticFields.debugMode
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            if(lout_mode == "LR")
            {
                println("**********"+testSuiteName+"**********");
                waveform_L.plot("AUDIO_Depop_TEST_waveform_L");
                waveform_R.plot("AUDIO_Depop_TEST_waveform_R");

                println("mean_L[uV]     = "+mean_L.multiply(1e6));
                println("mean_R[uV]     = "+mean_R.multiply(1e6));
                println();
            }
            else if(lout_mode == "L")
            {
                println("**********"+testSuiteName+" (LOUT_L)**********");
                waveform_L.plot("AUDIO_Depop_TEST_waveform_L");

                println("mean_L[uV]     = "+mean_L.multiply(1e6));
                println();
            }
            else if(lout_mode == "R")
            {
                println("**********"+testSuiteName+" (LOUT_R)**********");
                waveform_R.plot("AUDIO_Depop_TEST_waveform_R");

                println("mean_R[uV]     = "+mean_R.multiply(1e6));
                println();
            }
            else
            {
                println("**********"+testSuiteName+"**********");
                waveform_L.plot("AUDIO_Depop_TEST_waveform_L");
                waveform_R.plot("AUDIO_Depop_TEST_waveform_R");

                println("mean_L[uV]     = "+mean_L.multiply(1e6));
                println("mean_R[uV]     = "+mean_R.multiply(1e6));
                println();
            }
        }

        if(lout_mode=="LR")
        {
            ptd_RX_DC_L_imb.evaluate(mean_L);
            ptd_RX_DC_R_imb.evaluate(mean_R);
        }
        else if(lout_mode=="L")
        {
            ptd_RX_DC_L_imb.evaluate(mean_L);
        }
        else if(lout_mode=="R")
        {
            ptd_RX_DC_R_imb.evaluate(mean_R);
        }
        else
        {
            ptd_RX_DC_L_imb.evaluate(mean_L);
            ptd_RX_DC_R_imb.evaluate(mean_R);
        }
    }
}
