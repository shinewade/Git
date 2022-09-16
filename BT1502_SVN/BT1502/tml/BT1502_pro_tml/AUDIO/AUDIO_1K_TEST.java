package BT1502_pro_tml.AUDIO;

import java.util.Map;

import BT1502_pro_tml.Global.StaticFields;
import besLib.cal.BesCalc_AUDIO;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDigitizer;
import xoc.dsa.ISetupDigitizer.IMeasureWaveform.IDigitalFilter.SetupFilterType;
import xoc.dsa.ISetupDigitizer.IMeasureWaveform.SetupInputImpedance;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.dsp.MultiSiteWaveDouble;
import xoc.dta.datatypes.dsp.MultiSiteWaveDoubleArray;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigitizerResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class AUDIO_1K_TEST extends TestMethod {

    @In public String lout_mode;            //LR: -> LOUT_L & LOUT_R OUT
                                            //L:  -> LOUT_L OUT
                                            //R:  -> LOUT_R OUT

    @In public String gain_mode;            //0:Mic Gain =7;
                                            //1:Mic Gain =4;
                                            //2:Mic Gain =0;

    @In public String loading_mode;         //NO :no loading;
                                            //YES:loading;


    public IMeasurement measurement;
    public IParametricTestDescriptor    ptd_A1K_SNR_L ,
                                        ptd_A1K_THD_L ,
                                        ptd_A1K_THDN_L,
                                        ptd_A1K_PWR_L ,
                                        ptd_A1K_SNR_R ,
                                        ptd_A1K_THD_R ,
                                        ptd_A1K_THDN_R,
                                        ptd_A1K_PWR_R ,
                                        ptd_A1K_LR_IMB;

    private String LOUT   = "DGT_LP+DGT_RP";

    private String DGT_Action="DGT_Measure";

    private double dgtInputRange;
    //private double dgtDcOffset;
    private double dgtSamplingRate;
    private long   dgtNumOfSamples;
    private double dgtLpfValue;
    private double dgtCutOffFreq1;
    private long   dgtNumFilterTap;

    @Override
    public void setup() {
        if(StaticFields.dev_name == "BGA7273")
        {
            lout_mode = "R";
            LOUT   = "DGT_RP";
        }
        else if (StaticFields.dev_name == "WLCSP") {
            lout_mode = "LR";
            LOUT   = "DGT_LP+DGT_RP";
        }

        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);

        // Standard DGT setup for BS
        dgtInputRange    = 2;  //1.3                      // [V]
        //dgtDcOffset      = 0.0;                         // [V]
        dgtSamplingRate  = 90.90354*1e3*8 ; // 90.90354 90.91242*1e3*8  89.5230138*1e3*8   // [Hz]98.4733719*1e3 ==> 26M  //90.90909091
        dgtNumOfSamples  = 1024*8*2;
        dgtLpfValue      = 30*1e3;                      // [Hz]

        // DGT Digital Filter setup for BS
        dgtCutOffFreq1   = 30*1e3;                      // [Hz]
        dgtNumFilterTap  = 32;

        ISetupDigitizer dgtSetup1=deviceSetup.addDigitizer(LOUT).setConfigOptionDifferential().setConnect(true).setDisconnect(true);
        dgtSetup1.measureWaveform(DGT_Action)
                 .setSampleRate(dgtSamplingRate)
                 .setExpectedAmplitude(dgtInputRange)
                 .setSamples(dgtNumOfSamples)
                 .setLowPassFilterBandwidth(dgtLpfValue)
                 .setInputImpedance(SetupInputImpedance.HighZ)
                 .digitalFilter()
                 .setFilterType(SetupFilterType.lowPass)
                 .setCutOffFrequency1(dgtCutOffFreq1)
                 .setNumFilterTaps(dgtNumFilterTap);

        deviceSetup.sequentialBegin();
            deviceSetup.waitCall("5 ms");
            deviceSetup.actionCall(DGT_Action);
        deviceSetup.sequentialEnd();
        measurement.setSetups(deviceSetup);

    }

    @Override
    public void execute() {
        measurement.execute();

        IDigitizerResults resultsDgt = measurement.digitizer(LOUT).preserveResults();
        Map<String, MultiSiteWaveDoubleArray> waveforms = resultsDgt.measureWaveform(DGT_Action).getWaveform();

        MultiSiteWaveDouble waveform_P0_L=new MultiSiteWaveDouble();
        MultiSiteWaveDouble waveform_P0_R=new MultiSiteWaveDouble();

        if(lout_mode=="R") {
            waveform_P0_L = waveforms.get("DGT_RP").getElement(0);
            waveform_P0_R = waveforms.get("DGT_RP").getElement(0);
        }
        else if(lout_mode=="L") {
            waveform_P0_L = waveforms.get("DGT_LP").getElement(0);
            waveform_P0_R = waveforms.get("DGT_LP").getElement(0);
        }
        else if(lout_mode=="LR") {
            waveform_P0_L = waveforms.get("DGT_LP").getElement(0);
            waveform_P0_R = waveforms.get("DGT_RP").getElement(0);
        }
        int[] activeSites = context.getActiveSites();

        MultiSiteLong num_pts =new MultiSiteLong();
        MultiSiteWaveDouble waveform_L = new MultiSiteWaveDouble();
        MultiSiteWaveDouble waveform_R = new MultiSiteWaveDouble();

        num_pts = waveform_P0_L.getSize().divide(2);
        int num_pts_int = (int)(num_pts.get(activeSites[0])+0);
        waveform_L = waveform_P0_L.resize(num_pts);
        waveform_R = waveform_P0_R.resize(num_pts);

        for(int i=0;i<num_pts.get(activeSites[0]);i++) {
            waveform_L.setValue(i, (waveform_P0_L.getValue(i).add(waveform_P0_L.getValue(i+num_pts_int))).divide(2));
            waveform_R.setValue(i, (waveform_P0_R.getValue(i).add(waveform_P0_R.getValue(i+num_pts_int))).divide(2));
        }

        releaseTester();
        BesCalc_AUDIO AUDIO_1K = new BesCalc_AUDIO(waveform_L, waveform_R);

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println();
            println("**********"+testSuiteName+"**********");
            waveform_L.plot("Audio_1K_L_wave");
            waveform_R.plot("Audio_1K_R_wave");
            waveform_P0_L      .plot(testSuiteName+"_1K_waveform_P0_L_Wave");
            waveform_P0_R      .plot(testSuiteName+"_1K_waveform_P0_R_Wave");
            AUDIO_1K.getSpectrum_L_dBm()      .plot(testSuiteName+"_pre_spec_L_Wave");
            AUDIO_1K.getSpectrum_R_dBm()     .plot(testSuiteName+"_pre_spec_R_Wave");

            println("PWR L= "+AUDIO_1K.get1KPower_A_L_dBm()+" dBm");
            println("PWR R= "+AUDIO_1K.get1KPower_A_R_dBm()+" dBm");

            println("FREQ L= "+AUDIO_1K.getFrequency_L().divide(1000)+" KHz");
            println("FREQ R= "+AUDIO_1K.getFrequency_R().divide(1000)+" KHz");

            println("Noise PWR L= "+AUDIO_1K.getNoisePower_L_dBm()+" dBm");
            println("Noise PWR R= "+AUDIO_1K.getNoisePower_R_dBm()+" dBm");

            println("Audio_SNR_L= "+AUDIO_1K.getSNR_A_L()+" dB");
            println("Audio_THD_L= "+AUDIO_1K.getTHD_L()+" dB");
            println("Audio_THDN_L= "+AUDIO_1K.getTHDN_L()+" dB");

            println("Audio_SNR_R= "+AUDIO_1K.getSNR_A_R()+" dB");
            println("Audio_THD_R= "+AUDIO_1K.getTHD_R()+" dB");
            println("Audio_THDN_R= "+AUDIO_1K.getTHDN_R()+" dB");
        }

        if(lout_mode=="R") {
            ptd_A1K_SNR_R .evaluate(AUDIO_1K.getSNR_A_R());
            ptd_A1K_THD_R .evaluate(AUDIO_1K.getTHD_R());
            ptd_A1K_THDN_R.evaluate(AUDIO_1K.getTHDN_R());
            ptd_A1K_PWR_R .evaluate(AUDIO_1K.get1KPower_A_R_dBm());
        }
        else if(lout_mode=="L") {
            ptd_A1K_SNR_L .evaluate(AUDIO_1K.getSNR_A_L());
            ptd_A1K_THD_L .evaluate(AUDIO_1K.getTHD_L());
            ptd_A1K_THDN_L.evaluate(AUDIO_1K.getTHDN_L());
            ptd_A1K_PWR_L .evaluate(AUDIO_1K.get1KPower_A_L_dBm());
        }
        else if(lout_mode=="LR") {
            ptd_A1K_SNR_L .evaluate(AUDIO_1K.getSNR_A_L());
            ptd_A1K_THD_L .evaluate(AUDIO_1K.getTHD_L());
            ptd_A1K_THDN_L.evaluate(AUDIO_1K.getTHDN_L());
            ptd_A1K_PWR_L .evaluate(AUDIO_1K.get1KPower_A_L_dBm());
            ptd_A1K_SNR_R .evaluate(AUDIO_1K.getSNR_A_R());
            ptd_A1K_THD_R .evaluate(AUDIO_1K.getTHD_R());
            ptd_A1K_THDN_R.evaluate(AUDIO_1K.getTHDN_R());
            ptd_A1K_PWR_R .evaluate(AUDIO_1K.get1KPower_A_R_dBm());
        }
    }

}
