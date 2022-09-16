package BT1502_pro_tml.AUDIO;

import java.util.Map;

import BT1502_pro_tml.Global.StaticFields;
import besLib.cal.BesCalc_AUDIO;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupAwg;
import xoc.dsa.ISetupDigitizer;
import xoc.dsa.ISetupDigitizer.IMeasureWaveform.IDigitalFilter.SetupFilterType;
import xoc.dsa.ISetupDigitizer.IMeasureWaveform.SetupInputImpedance;
import xoc.dsa.ISetupWaveformSampleBased;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.dsp.MultiSiteWaveDouble;
import xoc.dta.datatypes.dsp.MultiSiteWaveDoubleArray;
import xoc.dta.measurement.ILocalMeasurement;
import xoc.dta.resultaccess.IDigitizerResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class AUDIO_ADDA_1K_ANC_FFFB_TEST extends TestMethod {

    @In public String spec_measurement;

    @In public String lout_mode;            //LR : -> LOUT_L & LOUT_R OUT
                                            //L  : -> LOUT_L OUT
                                            //R  : -> LOUT_R OUT

    @In public String loading_mode;         //NO :no loading;
                                            //YES:loading;

    @In public String mic_in_relay;         //MIC1;
                                            //MIC2;
                                            //MIC3;
                                            //MIC4;
                                            //MIC5;
    @In public double awgAmplitude;

    public ILocalMeasurement measurement;
    public ILocalMeasurement measurement1;
//    public IMeasurement measurement_relayOff;

    public IParametricTestDescriptor    ptd_Audio_MIC_SNR_L,
                                        ptd_Audio_MIC_THD_L,
                                        ptd_Audio_MIC_PWR_L,
                                        ptd_Audio_MIC_SNR_R,
                                        ptd_Audio_MIC_THD_R,
                                        ptd_Audio_MIC_PWR_R;

    public IParametricTestDescriptor    ptd_Audio_MIC_NOISE_VRMS_A_L,
                                        ptd_Audio_MIC_NOISE_VRMS_A_R;

    private String LOUT   = "DGT_LP+DGT_RP";
    private String MIC ;

    private String AWG_Action1="AWG_Source";
    private String DGT_Action1="DGT_Measure_ADDA";
    private String DGT_Action2="DGT_Measure_Noise_Floor";

    private double awgSampRate;
//    private double awgAmplitude;
    private double awgDcOffset;
    private double awgBandWidth;
    private double dgtInputRange;
//    private double dgtDcOffset;
    private double dgtSamplingRate;
    private long   dgtNumOfSamples;
    private double dgtLpfValue;
    private double dgtCutOffFreq1;
    private long   dgtNumFilterTap;

    private long data_0x40300084;
    private long data_0x40300088;

    @Override
    public void setup() {
        // define AWG DGT pin resource
        if(StaticFields.dev_name == "BGA7273")
        {
            lout_mode = "R";
            LOUT   = "DGT_RP";
            MIC = "AWG_MIC1_P";
        }
        else if (StaticFields.dev_name == "WLCSP") {
            lout_mode = "LR";
            LOUT   = "DGT_LP+DGT_RP";
            MIC = "AWG_MIC1_P+AWG_MIC2_P";
//            lout_mode = "L";
//            LOUT   = "DGT_LP";
//            MIC = "AWG_MIC1_P";
        }

        IDeviceSetup deviceSetup=DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement);
        deviceSetup.addDigInOut("VMIC1+VMIC2").setConnect(false).setDisconnect(true).setDisconnectModeHiz();

        if(mic_in_relay == "MIC1")
        {
            data_0x40300084 = 0x00400020;
            data_0x40300088 = 0x00400022;
            if(StaticFields.dev_name == "BGA7273") {
                deviceSetup.addUtility("K18+K23").setValue(0);
            }
            else if (StaticFields.dev_name == "WLCSP") {
                deviceSetup.addUtility("K4+K5").setValue(0);
            }
        }
        else if(mic_in_relay == "MIC2")
        {
            data_0x40300084 = 0x00400022;
            if(StaticFields.dev_name == "BGA7273") {
            }
            else if (StaticFields.dev_name == "WLCSP") {
                deviceSetup.addUtility("K1").setValue(0);
            }
        }
        else if(mic_in_relay == "MIC3")
        {
            data_0x40300084 = 0x00400024;
            deviceSetup.addUtility("K4").setValue(1);
            deviceSetup.addUtility("K6").setValue(0);
            if(StaticFields.dev_name == "BGA7273") {
            }
            else if (StaticFields.dev_name == "WLCSP") {
                deviceSetup.addUtility("K4").setValue(0);
                deviceSetup.addUtility("K5").setValue(1);
            }
        }
        else if(mic_in_relay == "MIC4")
        {
            data_0x40300084 = 0x00400026;//0x002e1f3f 0x00400026
            if(StaticFields.dev_name == "BGA7273") {
            }
            else if (StaticFields.dev_name == "WLCSP") {
                deviceSetup.addUtility("K1").setValue(1);
            }
        }
        else if(mic_in_relay == "MIC5")
        {
            data_0x40300084 = 0x00400028;
            if(StaticFields.dev_name == "BGA7273") {
                deviceSetup.addUtility("K23").setValue(0);
                deviceSetup.addUtility("K18").setValue(1);
            }
            else if (StaticFields.dev_name == "WLCSP") {
                deviceSetup.addUtility("K4").setValue(1);
            }
        }

            BesPA_I2C i2c=new BesPA_I2C(deviceSetup, measurement1, I2cRegAddrBits.RegAddr_8Bits);
            i2c.setSignals("I2C_SCL", "I2C_SDA");
            i2c.transactionSequenceBegin("AUDIO_ADDA_TEST");
                i2c.write(BesI2cAddrType.DIGITAL,0x40300084,data_0x40300084);   //codec psc
//                i2c.write(BesI2cAddrType.DIGITAL,0x40300088,data_0x40300088);   //codec psc
                i2c.waitTime(10e6);
            i2c.transactionSequenceEnd();

        measurement.setSetups(deviceSetup);



        IDeviceSetup deviceSetup1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);

        // BS
        // Standard AWG setup for BS
        awgSampRate      = 186.186*1e3;                  // [Hz]
        awgDcOffset      = 0.0;
        awgAmplitude     = 0.4;    //  0.4 0.25  0.38                // [V]
        awgBandWidth     = 1*1e3;                     // [Hz]

        // Standard DGT setup for BS
        dgtInputRange    = 1.6;  //1.3                      // [V]
//        dgtDcOffset      = 0.0;                         // [V]
        dgtSamplingRate  = 93.093*1e3*8 ; //    89.5230138*1e3*8   // [Hz]98.4753719*1e3    93.093  90.902229*1e3*8
        dgtNumOfSamples  = 1024*8*2;
        dgtLpfValue      = 30*1e3;                      // [Hz]
        // DGT Digital Filter setup for BS
        dgtCutOffFreq1   = 20*1e3;                      // [Hz] 30*1e3;
        dgtNumFilterTap  = 32;

        //waveform setup
        ISetupWaveformSampleBased awg_wave=deviceSetup1.addWaveformSampleBased("sinewave_1khz_AWG").setDataFile("waveforms/AUDIO_WAVE_1K_M_11_N_2048_AWG.cwf").setSampleRate(awgSampRate);

        ISetupAwg awgSetup1=deviceSetup1.addAwg(MIC).setConfigOptionDifferential().setConnect(true).setDisconnect(true);//.setKeepAlive(true);
        awgSetup1.  sourceWaveform(AWG_Action1).
                    setWaveform(awg_wave).
                    setRepeatInfinite(true).
                    setAmplitude(awgAmplitude).
                    setOffset(awgDcOffset).
//                    setOptimizationDistortion().
                    setOptimizationNoiseFloor().
                    setBandwidth(awgBandWidth);

        ISetupDigitizer dgtSetup1=deviceSetup1.addDigitizer(LOUT).setConfigOptionDifferential().setConnect(true).setDisconnect(true);
        dgtSetup1.measureWaveform(DGT_Action1)
                 .setSampleRate(dgtSamplingRate)
                 .setExpectedAmplitude(dgtInputRange)
//                 .setOffset(dgtDcOffset)
                 .setSamples(dgtNumOfSamples)
                 .setLowPassFilterBandwidth(dgtLpfValue)
                 .setInputImpedance(SetupInputImpedance.HighZ)
                 .digitalFilter()
                 .setFilterType(SetupFilterType.lowPass)
                 .setCutOffFrequency1(dgtCutOffFreq1)
                 .setNumFilterTaps(dgtNumFilterTap);

        dgtSetup1.measureWaveform(DGT_Action2)
                 .setSampleRate(dgtSamplingRate)
                 .setExpectedAmplitude(dgtInputRange)
//                 .setOffset(dgtDcOffset)
                 .setSamples(1024*8*4)
                 .setLowPassFilterBandwidth(dgtLpfValue)
                 .setInputImpedance(SetupInputImpedance.HighZ)
                 .digitalFilter()
                 .setFilterType(SetupFilterType.lowPass)
                 .setCutOffFrequency1(dgtCutOffFreq1)
                 .setNumFilterTaps(dgtNumFilterTap);

        deviceSetup1.sequentialBegin();

            deviceSetup1.waitCall("15 ms");
            deviceSetup1.actionCall(DGT_Action2);

            deviceSetup1.parallelBegin();
                deviceSetup1.sequentialBegin();
                    deviceSetup1.waitCall("5 ms");
                    deviceSetup1.actionCall(AWG_Action1);
                deviceSetup1.sequentialEnd();

                deviceSetup1.sequentialBegin();
                    deviceSetup1.waitCall("20 ms");
                    deviceSetup1.actionCall(DGT_Action1);
                deviceSetup1.sequentialEnd();
            deviceSetup1.parallelEnd();
        deviceSetup1.sequentialEnd();
        measurement1.setSetups(deviceSetup1);
    }
    @Override
    public void execute() {

        measurement.execute();

        MultiSiteWaveDouble waveform_L = new MultiSiteWaveDouble();
        MultiSiteWaveDouble waveform_R = new MultiSiteWaveDouble();
        MultiSiteWaveDouble waveform_L_nf = new MultiSiteWaveDouble();
        MultiSiteWaveDouble waveform_R_nf = new MultiSiteWaveDouble();
        MultiSiteLong num_pts =new MultiSiteLong();
        measurement1.execute();
        IDigitizerResults resultsDgt = measurement1.digitizer(LOUT).preserveResults();
        Map<String, MultiSiteWaveDoubleArray> waveforms = resultsDgt.measureWaveform(DGT_Action1).getWaveform();
        Map<String, MultiSiteWaveDoubleArray> waveforms_nf = resultsDgt.measureWaveform(DGT_Action2).getWaveform();

        if(lout_mode == "LR")
        {
            waveform_L = waveforms.get("DGT_LP").getElement(0);
            waveform_R = waveforms.get("DGT_RP").getElement(0);
            waveform_L_nf = waveforms_nf.get("DGT_LP").getElement(0);
            waveform_R_nf = waveforms_nf.get("DGT_RP").getElement(0);
        }
        else if(lout_mode == "L")
        {
            waveform_L = waveforms.get("DGT_LP").getElement(0);
            waveform_R = waveforms.get("DGT_LP").getElement(0);
            waveform_L_nf = waveforms_nf.get("DGT_LP").getElement(0);
            waveform_R_nf = waveforms_nf.get("DGT_LP").getElement(0);
        }
        else if(lout_mode == "R")
        {
            waveform_L = waveforms.get("DGT_RP").getElement(0);
            waveform_R = waveforms.get("DGT_RP").getElement(0);
            waveform_L_nf = waveforms_nf.get("DGT_RP").getElement(0);
            waveform_R_nf = waveforms_nf.get("DGT_RP").getElement(0);
        }

        num_pts = waveform_L.getSize().divide(2);
        int num_pts_int = (int)(num_pts.get(context.getActiveSites()[0]) + 0);
        MultiSiteWaveDouble waveform_P0_L = new MultiSiteWaveDouble(num_pts);
        MultiSiteWaveDouble waveform_P0_R = new MultiSiteWaveDouble(num_pts);
        for(int i=0;i<num_pts.get(context.getActiveSites()[0]);i++) {
            waveform_P0_L.setValue(i, (waveform_L.getValue(i).add(waveform_L.getValue(i+num_pts_int))).divide(2));
            waveform_P0_R.setValue(i, (waveform_R.getValue(i).add(waveform_R.getValue(i+num_pts_int))).divide(2));
//            waveform_P0_L.setValue(i, waveform_L.getValue(i));
//            waveform_P0_R.setValue(i, waveform_R.getValue(i));
        }
        waveform_P0_L.setSampleRate(waveform_L.getSampleRate());
        waveform_P0_R.setSampleRate(waveform_R.getSampleRate());

        releaseTester();
        BesCalc_AUDIO AUDIO_ADDA_1K = new BesCalc_AUDIO(waveform_P0_L, waveform_P0_R);
        BesCalc_AUDIO AUDIO_MIC_NF = new BesCalc_AUDIO(waveform_L_nf, waveform_R_nf);

        if(lout_mode == "LR")
        {
            ptd_Audio_MIC_SNR_L.evaluate(AUDIO_ADDA_1K.getSNR_L());
            ptd_Audio_MIC_THD_L.evaluate(AUDIO_ADDA_1K.getTHD_L());
            ptd_Audio_MIC_SNR_R.evaluate(AUDIO_ADDA_1K.getSNR_R());
            ptd_Audio_MIC_THD_R.evaluate(AUDIO_ADDA_1K.getTHD_R());
            ptd_Audio_MIC_PWR_L.evaluate(AUDIO_ADDA_1K.get1KPower_L_dBm());
            ptd_Audio_MIC_PWR_R.evaluate(AUDIO_ADDA_1K.get1KPower_R_dBm());
//            ptd_Audio_MIC_NOISE_VRMS_A_L.evaluate(AUDIO_MIC_NF.getNoiseFloor_A_L_uVrms());
//            ptd_Audio_MIC_NOISE_VRMS_A_R.evaluate(AUDIO_MIC_NF.getNoiseFloor_A_R_uVrms());
        }
        else if(lout_mode == "L")
        {
            ptd_Audio_MIC_SNR_L.evaluate(AUDIO_ADDA_1K.getSNR_L());
            ptd_Audio_MIC_THD_L.evaluate(AUDIO_ADDA_1K.getTHD_L());
            ptd_Audio_MIC_PWR_L.evaluate(AUDIO_ADDA_1K.get1KPower_L_dBm());
            ptd_Audio_MIC_NOISE_VRMS_A_L.evaluate(AUDIO_MIC_NF.getNoiseFloor_A_L_uVrms());
        }
        else if(lout_mode == "R")
        {
            ptd_Audio_MIC_SNR_R.evaluate(AUDIO_ADDA_1K.getSNR_R());
            ptd_Audio_MIC_THD_R.evaluate(AUDIO_ADDA_1K.getTHD_R());
            ptd_Audio_MIC_PWR_R.evaluate(AUDIO_ADDA_1K.get1KPower_R_dBm());
//            ptd_Audio_MIC_NOISE_VRMS_A_R.evaluate(AUDIO_MIC_NF.getNoiseFloor_A_R_Vrms());
        }

        if(StaticFields.debugMode)//StaticFields.debugMode
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            if(lout_mode == "LR")
            {
                println("**********"+testSuiteName+"**********");
                waveform_P0_L.plot(testSuiteName+"_BS_waveform_L");
                waveform_P0_R.plot(testSuiteName+"_BS_waveform_R");
                AUDIO_ADDA_1K.getSpectrum_A_L_dBm().plot(testSuiteName+"_BS_spectrum_A_L_dBm");
                AUDIO_ADDA_1K.getSpectrum_A_R_dBm().plot(testSuiteName+"_BS_spectrum_A_R_dBm");
                waveform_L_nf.plot(testSuiteName+"_NF_waveform_L");
                waveform_R_nf.plot(testSuiteName+"_NF_waveform_R");
//                println("FREQ_L      = "+AUDIO_ADDA_1K.getFrequency_L()+" KHZ");
//                println("FREQ_R      = "+AUDIO_ADDA_1K.getFrequency_R()+" KHZ");
                println("NOSIE_PWR_A_L = "+AUDIO_ADDA_1K.getNoisePower_A_L_dBm()+" dBm");
                println("NOSIE_PWR_A_R = "+AUDIO_ADDA_1K.getNoisePower_A_R_dBm()+" dBm");

                println("Audio_THDN_A_L= "+AUDIO_ADDA_1K.getTHDN_A_L()+" dB");
                println("Audio_THDN_A_R= "+AUDIO_ADDA_1K.getTHDN_A_R()+" dB");

                println("Audio_SNR_A_L= "+AUDIO_ADDA_1K.getSNR_A_L()+" dB");
                println("Audio_SNR_A_R= "+AUDIO_ADDA_1K.getSNR_A_R()+" dB");

                println("Audio_THD_A_L= "+AUDIO_ADDA_1K.getTHD_A_L()+" dB");
                println("Audio_THD_A_R= "+AUDIO_ADDA_1K.getTHD_A_R()+" dB");

                println("NOSIE_A_L_uVrms = "+AUDIO_MIC_NF.getNoiseFloor_A_L_uVrms()+" uVrms");
                println("NOSIE_A_R_uVrms = "+AUDIO_MIC_NF.getNoiseFloor_A_R_uVrms()+" uVrms");
                println();
            }
            else if(lout_mode == "L")
            {
                println("**********"+testSuiteName+" (LOUT_L)**********");
                waveform_P0_L.plot(testSuiteName+"_BS_waveform_L");
                AUDIO_ADDA_1K.getSpectrum_L_dBm()  .plot(testSuiteName+"_BS_spectrum_L_dBm");
                AUDIO_ADDA_1K.getSpectrum_A_L_dBm().plot(testSuiteName+"_BS_spectrum_A_L_dBm");
                waveform_L_nf.plot(testSuiteName+"_NF_waveform_L");
                AUDIO_MIC_NF.getSpectrum_L_dBm()  .plot(testSuiteName+"_NF_spectrum_L_dBm");
                AUDIO_MIC_NF.getSpectrum_A_L_dBm().plot(testSuiteName+"_NF_spectrum_A_L_dBm");

                println("PWR L       = "+AUDIO_ADDA_1K.get1KPower_L_dBm()+" dBm");
                println("FREQ_L      = "+AUDIO_ADDA_1K.getFrequency_L()+" KHZ");
                println("NOSIE_PWR_L = "+AUDIO_ADDA_1K.getNoisePower_L_dBm()+" dBm");
                println("Audio_THDN_L= "+AUDIO_ADDA_1K.getTHDN_L()+" dB");
                println("Audio_SNR_L= "+AUDIO_ADDA_1K.getSNR_L()+" dB");
                println("Audio_THD_L= "+AUDIO_ADDA_1K.getTHD_L()+" dB");
                println("NOSIE_PWR_A_L = "+AUDIO_ADDA_1K.getNoisePower_A_L_dBm()+" dBm");
                println("Audio_THDN_A_L= "+AUDIO_ADDA_1K.getTHDN_A_L()+" dB");
                println("Audio_SNR_A_L= "+AUDIO_ADDA_1K.getSNR_A_L()+" dB");
                println("Audio_THD_A_L= "+AUDIO_ADDA_1K.getTHD_A_L()+" dB");
                println("NOSIE_A_L_uVrms = "+AUDIO_MIC_NF.getNoiseFloor_A_L_uVrms()+" uVrms");
                println();
            }
            else if(lout_mode == "R")
            {
                println("**********"+testSuiteName+" (LOUT_R)**********");
                waveform_R.plot(testSuiteName+"_BS_waveform_R");
                waveform_P0_R.plot(testSuiteName+"_BS_waveform_R");
                AUDIO_ADDA_1K.getSpectrum_R_dBm()  .plot(testSuiteName+"_BS_spectrum_R_dBm");
                AUDIO_ADDA_1K.getSpectrum_A_R_dBm().plot(testSuiteName+"_BS_spectrum_A_R_dBm");
                waveform_R_nf.plot(testSuiteName+"_NF_waveform_R");
                println("PWR R       = "+AUDIO_ADDA_1K.get1KPower_R_dBm()+" dBm");
                println("FREQ_R      = "+AUDIO_ADDA_1K.getFrequency_R()+" KHZ");
                println("NOSIE_PWR_R = "+AUDIO_ADDA_1K.getNoisePower_R_dBm()+" dBm");
                println("Audio_THDN_R= "+AUDIO_ADDA_1K.getTHDN_R()+" dB");
                println("Audio_SNR_R= "+AUDIO_ADDA_1K.getSNR_R()+" dB");
                println("Audio_THD_R= "+AUDIO_ADDA_1K.getTHD_R()+" dB");
                println("NOSIE_PWR_A_R = "+AUDIO_ADDA_1K.getNoisePower_A_R_dBm()+" dBm");
                println("Audio_THDN_A_R= "+AUDIO_ADDA_1K.getTHDN_A_R()+" dB");
                println("Audio_SNR_A_R= "+AUDIO_ADDA_1K.getSNR_A_R()+" dB");
                println("Audio_THD_A_R= "+AUDIO_ADDA_1K.getTHD_A_R()+" dB");
                println("NOSIE_A_R_uVrms = "+AUDIO_MIC_NF.getNoiseFloor_A_R_uVrms()+" uVrms");
                println();
            }
        }

//        measurement_relayOff.execute();
    }
}
//                                        AUDIO AD/DA LOOP
//                                       ___________________
//                                      |                   |
//   _________                     -----| MIC1              |                       _________
//            |                         |                   |                      |
//       AWG1 |-----               -----| MIC2       LOUT_L |-----            -----| DGT1
//   A        |                         |        D          |                      |        A
//   T        |        Routing     -----| MIC3   U          |       Routing        |        T
//   E        |                         |        T          |                      |        E
//       AWG2 |-----               -----| MIC4       LOUT_R |-----            -----| DGT2
//   _________|                         |                   |                      |_________
//                                 -----| MIC5              |
//                                      |___________________|
//Vp   = Vpp / 2
//Vrms = Vp / 1.414
//dBV  = 20 * log(Vrms)
//dBu  = 20 * log(Vrms / 0.7746)
//W    = (Vrms * Rdbmref / (Rzout + Rdbmref))2 / Rwref
//dBm  = 10 * log((Vrms * Rdbref / (Rzout + Rdbmref))2 * 1000 / Rdbmref
//dBr  = dBV - 20 * log(Vdbrref)

