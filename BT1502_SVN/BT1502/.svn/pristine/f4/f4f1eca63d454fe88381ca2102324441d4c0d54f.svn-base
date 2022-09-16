//package bt1501p_tml.BT;
//
//import bt1501p_tml.Global.StaticFields;
//import xoc.dsa.DeviceSetupFactory;
//import xoc.dsa.IDeviceSetup;
//import xoc.dsa.ISetupRfMeas;
//import xoc.dsa.ISetupRfMeas.IModPower;
//import xoc.dsa.ISetupRfMeas.IModPower.SetupWindowFunction;
//import xoc.dta.TestMethod;
//import xoc.dta.annotations.In;
//import xoc.dta.datatypes.MultiSiteBoolean;
//import xoc.dta.datatypes.MultiSiteDouble;
//import xoc.dta.datatypes.MultiSiteLong;
//import xoc.dta.datatypes.dsp.MultiSiteSpectrum;
//import xoc.dta.datatypes.dsp.MultiSiteWaveComplex;
//import xoc.dta.datatypes.dsp.SpectrumUnit;
//import xoc.dta.measurement.IMeasurement;
//import xoc.dta.resultaccess.IRfMeasResults;
//import xoc.dta.testdescriptor.IParametricTestDescriptor;
//
//
//public class BT_TX_20dB_BW_ACP_TEST extends TestMethod {
//
//    public IMeasurement measurement;
//
//    public IParametricTestDescriptor    ptd_BT_TX_20dB_Peak,
//                                        ptd_BT_TX_ACPR_0,
//                                        ptd_BT_TX_ACPR_M3,
//                                        ptd_BT_TX_ACPR_M2,
//                                        ptd_BT_TX_ACPR_P2,
//                                        ptd_BT_TX_ACPR_P3,ptd_BT_TX_ACPR_0p9M,ptd_BT_TX_ACPR_0p64M,ptd_BT_TX_ACPR_40k_140k_dcdctone_noise,ptd_BT_TX_ACPR_50K_5M_noise;
//
//    private String RF_OUT="BT_RF";
//    private String actionName="measFreq";
//    @In public int freq_idx ;//0:FREQL
//                            //1:FREQM
//                            //2:FREQH
//  @In public String spec_measurement;
//
//    @Override
//    public void setup() {
//        IDeviceSetup deviceSetup=DeviceSetupFactory.createInstance(StaticFields.prog_name);
//
//        ISetupRfMeas setup_meas=deviceSetup.addRfMeas(RF_OUT).setConfigModeHighResolution().setConfigOptionSiteInterlacingOn();//16 bit, 250Msps Max
//        IModPower modActionFs=setup_meas.modPower(actionName).
//                                        setFrequency("2.402 GHz").
//                                        setExpectedMaxPower("20 dBm").
//                                        setSamples(10000).
//                                        setBandwidthOfInterest("10 MHz").
//                                        setIfFrequency("25 MHz").//12.5 MHz at SMT7
//                                        setSampleRate("25 MHz").
//                                        setWindowFunction(SetupWindowFunction.hanning);
//
//        deviceSetup.setSequentialSyncAccuracy(0.001);
//        deviceSetup.actionCall(modActionFs);
//        measurement.setSetups(deviceSetup);
//    }
//
//
//    @Override
//    public void update() {
//        if(freq_idx==0)//FREQL
//        {
//            measurement.rfMeas(RF_OUT).modPower(actionName).setFrequency(2.402e9);
//        }
//        else if(freq_idx==1)//FREQM
//        {
//            measurement.rfMeas(RF_OUT).modPower(actionName).setFrequency(2.441e9);//441e9
//        }
//        else if(freq_idx==2)//FREQH
//        {
//            measurement.rfMeas(RF_OUT).modPower(actionName).setFrequency(2.480e9);
//        }
//
//    }
//
//    @Override
//    public void execute() {
//        measurement.execute();
//        IRfMeasResults rfResult = measurement.rfMeas(RF_OUT).preserveResults();
//
//        MultiSiteWaveComplex waveform = rfResult.modPower(actionName).getComplexWaveform(RF_OUT).getElement(0);
//        MultiSiteSpectrum spectrum = waveform.spectrum(SpectrumUnit.dBm);
//        MultiSiteSpectrum spectrum_mW = waveform.spectrum(SpectrumUnit.mW);
//
//        MultiSiteLong iBinCenter = new MultiSiteLong();
//        MultiSiteLong iBinChecker = new MultiSiteLong();
//        MultiSiteLong upBin = new MultiSiteLong();
//        MultiSiteLong lowBin = new MultiSiteLong();
//        MultiSiteDouble dmax = new MultiSiteDouble();
//        int[] activeSites = context.getActiveSites();
//
//        iBinCenter = spectrum.maxIndex();
//        dmax = spectrum.max();
////        println("dmax=  "+dmax);
//        double BinResolution =(spectrum.getSampleRate().get(activeSites[0])/spectrum.getSize().get(activeSites[0]))/1000;//KHz
//
//        MultiSiteLong imax_960k =new MultiSiteLong();
//        MultiSiteLong imax_640k =new MultiSiteLong();
//        MultiSiteDouble PWR_Rf=new MultiSiteDouble();
//        MultiSiteDouble PWR_Rf_960k=new MultiSiteDouble();
//        MultiSiteDouble PWR_Rf_640k=new MultiSiteDouble();
//        MultiSiteDouble PWR_Rf_4p8M=new MultiSiteDouble();
//        MultiSiteDouble PWR_Rf_noise=new MultiSiteDouble();
//
//        MultiSiteDouble PWR_Lo=new MultiSiteDouble();
//        MultiSiteDouble PWR_Imag=new MultiSiteDouble();
//        MultiSiteLong extractSize_Power=new MultiSiteLong(8);
//        MultiSiteLong extractSize_Power_0p64M=new MultiSiteLong(20);
//        MultiSiteLong extractSize_Power_4p8M=new MultiSiteLong(40);
//        MultiSiteLong extractSize_Power_noise=new MultiSiteLong(1980);
//        try {
//            imax_960k=iBinCenter.subtract((long)(960/BinResolution));
//            PWR_Rf_960k=spectrum_mW.extractValues(imax_960k.subtract(4),extractSize_Power).sum();
//            imax_640k=iBinCenter.add((long)(640/BinResolution));
//            PWR_Rf_640k=spectrum_mW.extractValues(imax_640k.subtract(10),extractSize_Power_0p64M).sum();
//            PWR_Rf_4p8M=spectrum_mW.extractValues(iBinCenter.add(16),extractSize_Power_4p8M).sum();//Rf Bin + 40k ~140k;
//            PWR_Rf_noise=spectrum_mW.extractValues(iBinCenter.add(20),extractSize_Power_noise).sum();//Rf Bin + 50k ~ 5M;
//            PWR_Rf=spectrum_mW.extractValues(iBinCenter.subtract(4),extractSize_Power).sum();
//            PWR_Rf_4p8M=PWR_Rf_4p8M.log10().multiply(10).add(StaticFields.att_bt);
//            PWR_Rf_noise=PWR_Rf_noise.log10().multiply(10).add(StaticFields.att_bt);
//            PWR_Rf=PWR_Rf.log10().multiply(10).add(StaticFields.att_bt);
//            PWR_Rf_960k=PWR_Rf_960k.log10().multiply(10).add(StaticFields.att_bt);
//            PWR_Rf_640k=PWR_Rf_640k.log10().multiply(10).add(StaticFields.att_bt);
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//
//
//
//        MultiSiteBoolean upBin_Stop = new MultiSiteBoolean(false);
//        MultiSiteBoolean lowBin_Stop = new MultiSiteBoolean(false);
//
//        for(int site: activeSites){
//            /*  search the up limit */
//            for(int i = spectrum.getSize(activeSites[0])-10;i>0;i--){
//                if(spectrum.getValue(site,i)-dmax.get(site)>-20  && spectrum.getValue(site,i-1)-dmax.get(site)>-20 && spectrum.getValue(site,i-2)-dmax.get(site)>-20){
//                    if(upBin_Stop.get(site).equals(false)){
//                        upBin.set(site,i);
//                        upBin_Stop.set(site,true);
//                    }
//                }
//            }
//            /*  search the low limit */
//            for(int i=0;i<spectrum.getSize(activeSites[0]);i++){
//                if(spectrum.getValue(site,i)-dmax.get(site)>-20 && spectrum.getValue(site,i+1)-dmax.get(site)>-20 && spectrum.getValue(site,i+2)-dmax.get(site)>-20){
//                    if(lowBin_Stop.get(site).equals(false)) {
//                        lowBin.set(site,i);
//                        lowBin_Stop.set(site,true);
//                    }
//                }
//            }
//        }
//
//        MultiSiteDouble bandwidth_20dB = new MultiSiteDouble();
//        bandwidth_20dB = (upBin.subtract(lowBin)).multiply(BinResolution);
//
//        MultiSiteDouble dPWR_0_1M_temp =  new MultiSiteDouble();
//        MultiSiteDouble dPWR_P2_1M_temp = new MultiSiteDouble();
//        MultiSiteDouble dPWR_P3_1M_temp = new MultiSiteDouble();
//        MultiSiteDouble dPWR_M2_1M_temp = new MultiSiteDouble();
//        MultiSiteDouble dPWR_M3_1M_temp = new MultiSiteDouble();
//
//        MultiSiteDouble dPWR_0_1M =  new MultiSiteDouble();
//        MultiSiteDouble dPWR_P2_1M = new MultiSiteDouble();
//        MultiSiteDouble dPWR_P3_1M = new MultiSiteDouble();
//        MultiSiteDouble dPWR_M2_1M = new MultiSiteDouble();
//        MultiSiteDouble dPWR_M3_1M = new MultiSiteDouble();
//
//        MultiSiteLong modSize = new MultiSiteLong();
//        modSize.set((long)(1000/BinResolution+0.5));
//        iBinChecker = iBinCenter.add((long)(2500/BinResolution)).add((long)0.5);
//
//        for (int site:activeSites) {
//            if (iBinChecker.get(site)>=10000) {
//                  iBinCenter.set(site,10000);
//            }
//        }
//
//        try {
//            dPWR_0_1M_temp  = spectrum_mW.extractValues(iBinCenter.subtract((long)(500/BinResolution)).add((long)0.5), modSize).sum();
//            dPWR_P2_1M_temp = spectrum_mW.extractValues(iBinCenter.add((long)(1500/BinResolution)).add((long)0.5), modSize).sum();
//            dPWR_P3_1M_temp = spectrum_mW.extractValues(iBinCenter.add((long)(2500/BinResolution)).add((long)0.5), modSize).sum();
//            dPWR_M2_1M_temp = spectrum_mW.extractValues(iBinCenter.subtract((long)(2500/BinResolution)).add((long)0.5), modSize).sum();
//            dPWR_M3_1M_temp = spectrum_mW.extractValues(iBinCenter.subtract((long)(3500/BinResolution)).add((long)0.5), modSize).sum();
//
//            dPWR_0_1M =  (dPWR_0_1M_temp).log10().multiply(10).add(StaticFields.att_bt);
//            dPWR_P2_1M = ((dPWR_P2_1M_temp).log10().multiply(10)).add(StaticFields.att_bt).subtract(dPWR_0_1M);
//            dPWR_P3_1M = ((dPWR_P3_1M_temp).log10().multiply(10)).add(StaticFields.att_bt).subtract(dPWR_0_1M);
//            dPWR_M2_1M = ((dPWR_M2_1M_temp).log10().multiply(10)).add(StaticFields.att_bt).subtract(dPWR_0_1M);
//            dPWR_M3_1M = ((dPWR_M3_1M_temp).log10().multiply(10)).add(StaticFields.att_bt).subtract(dPWR_0_1M);
//        } catch (Exception e) {
//            System.out.println("Error in extracting 20dB BW ACP : " + e);
//            dPWR_0_1M = new MultiSiteDouble(999);
//            dPWR_P2_1M = new MultiSiteDouble(999);
//            dPWR_P3_1M = new MultiSiteDouble(999);
//            dPWR_M2_1M = new MultiSiteDouble(999);
//            dPWR_M3_1M = new MultiSiteDouble(999);
//        }
//        StaticFields.BT_TX_PWR.set(dPWR_0_1M);
//        StaticFields.BT_TX_ACPR_0p96M.set(PWR_Rf.subtract(PWR_Rf_960k));
//
//        String testSuiteName_Qualified=context.getTestSuiteName();
//        String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
//        if(StaticFields.debugMode)
//        {
//            println("**********"+testSuiteName+"**********");
//            waveform.plot(testSuiteName+"_waveform");
//            spectrum.plot(testSuiteName+"_Spectrum");
////            println("BT_TX_20dB_Peak (KHz) = "+bandwidth_20dB);
////            println("Low_BIN = "+lowBin);
////            println("Up_BIN = "+upBin);
////            println("BinResolution_K = "+BinResolution);
////            println("BT_TX_ACPR_0 (dBm) = "+dPWR_0_1M);
////            println("BT_TX_ACPR_4p8M = "+PWR_Rf_4p8M);
//            println("BT_TX_ACPR_noise = "+PWR_Rf_noise);
//            println("BT_TX_ACPR_PWR = "+PWR_Rf);
//            println("BT_TX_ACPR_0p9M = "+(PWR_Rf.subtract(PWR_Rf_960k)));
//            println("BT_TX_ACPR_0p64M = "+(PWR_Rf.subtract(PWR_Rf_640k)));
//            println();
//        }
////        StaticFields.BT_TX_ACPR_0p96M.set(60);
//        ptd_BT_TX_ACPR_0p9M.evaluate(PWR_Rf.subtract(PWR_Rf_960k));
//        ptd_BT_TX_ACPR_0p64M.evaluate(PWR_Rf.subtract(PWR_Rf_640k));
//        ptd_BT_TX_ACPR_40k_140k_dcdctone_noise.evaluate(PWR_Rf_4p8M);
//        if( (testSuiteName.equals("BT_TX_ACP_FREQL"))||(testSuiteName.equals("BT_TX_ACP_FREQM"))||(testSuiteName.equals("BT_TX_ACP_FREQH")) )
//        {
//            ptd_BT_TX_ACPR_50K_5M_noise.evaluate(PWR_Rf_noise);
//        }
//    }
//}


//*********************************************************ACP dbc code********************************************
package BT1502_pro_tml.BT;

import BT1502_pro_tml.Global.StaticFields;
import besLib.cal.BesCalc_BTTX;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupProtocolInterface;
import xoc.dsa.ISetupRfMeas;
import xoc.dsa.ISetupRfMeas.IModPower;
import xoc.dsa.ISetupRfMeas.IModPower.SetupWindowFunction;
import xoc.dsa.ISetupTransactionSeqDef;
import xoc.dsa.ISetupTransactionSeqDef.Direction;
import xoc.dsa.ISetupTransactionSeqDef.Type;
import xoc.dsa.SetupVariableType;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.dsp.MultiSiteSpectrum;
import xoc.dta.datatypes.dsp.MultiSiteWaveComplex;
import xoc.dta.datatypes.dsp.SpectrumUnit;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IProtocolInterfaceResults;
import xoc.dta.resultaccess.IRfMeasResults;
import xoc.dta.resultaccess.ITransactionSequenceResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class BT_TX_20dB_BW_ACP_TEST extends TestMethod {

    public IMeasurement measurement,measurement_pre,measurement00;

    public IParametricTestDescriptor    ptd_BT_TX_20dB_Peak,
                                        ptd_BT_TX_ACPR_0,
                                        ptd_BT_TX_ACPR_M3,
                                        ptd_BT_TX_ACPR_M2,
                                        ptd_BT_TX_ACPR_P2,
                                        ptd_BT_TX_ACPR_P3,ptd_BT_TX_ACPR_0p9M,ptd_BT_TX_ACPR_0p64M,ptd_BT_TX_ACPR_40k_140k_dcdctone_noise,ptd_BT_TX_ACPR_50K_5M_noise;

    private String RF_OUT="BT_RF";
    @In public String spec_measurement;
    public String trCallName1 = "";

    private String actionName="measFreq";
    @In public int freq_idx ;//0:FREQL
                            //1:FREQM
                            //2:FREQH

    @Override
    public void setup() {
        IDeviceSetup deviceSetup_0 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup_0.importSpec(spec_measurement);

        ISetupProtocolInterface paInterface0 = deviceSetup_0.addProtocolInterface("I2C_GRP_BES", "setups.I2C.I2C_BES");
        paInterface0.addSignalRole("DATA","I2C_SDA");
        paInterface0.addSignalRole("CLK", "I2C_SCL");
        ISetupTransactionSeqDef transDigSrc0= paInterface0.addTransactionSequenceDef("TX_PWR_CAL");
        transDigSrc0.addParameter(Type.UnsignedLong, Direction.OUT, "data_0x8f");
        transDigSrc0.addParameter(Type.UnsignedLong, Direction.OUT, "data_0x91");
        transDigSrc0.addTransaction("RF_READ",0x8f,"data_0x8f");
        transDigSrc0.addTransaction("RF_READ",0x91,"data_0x91");//

        deviceSetup_0.sequentialBegin();
            trCallName1 = deviceSetup_0.transactionSequenceCall(transDigSrc0);
        deviceSetup_0.sequentialEnd();
        measurement00.setSetups(deviceSetup_0);



        IDeviceSetup deviceSetup0 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup0.importSpec(spec_measurement);


        deviceSetup0.addVariable(SetupVariableType.UnsignedLong, "spec_data_0x8f","0");
        deviceSetup0.addVariable(SetupVariableType.UnsignedLong, "spec_data_0x91","0");

        ISetupProtocolInterface paInterface1 = deviceSetup0.addProtocolInterface("I2C_BES", "setups.I2C.I2C_BES");
        paInterface1.addSignalRole("DATA","I2C_SDA");
        paInterface1.addSignalRole("CLK", "I2C_SCL");
        ISetupTransactionSeqDef transDigSrc1= paInterface1.addTransactionSequenceDef("TX_DC_PWR");
        transDigSrc1.addParameter(Type.UnsignedLong, Direction.IN, "trSqe_data_0x8f");
        transDigSrc1.addParameter(Type.UnsignedLong, Direction.IN, "trSqe_data_0x91");
        transDigSrc1.addTransaction("RF_WRITE",0x8f,"trSqe_data_0x8f");
        transDigSrc1.addTransaction("RF_WRITE",0x91,"trSqe_data_0x91");


        deviceSetup0.sequentialBegin();
            deviceSetup0.transactionSequenceCall(transDigSrc1,"trSqe_data_0x8f=spec_data_0x8f","trSqe_data_0x91=spec_data_0x91");
        deviceSetup0.sequentialEnd();
        measurement_pre.setSetups(deviceSetup0);



        IDeviceSetup deviceSetup=DeviceSetupFactory.createInstance(StaticFields.prog_name);

        ISetupRfMeas setup_meas=deviceSetup.addRfMeas(RF_OUT).setConfigModeHighResolution().setConfigOptionSiteInterlacingOn();//16 bit, 250Msps Max
        IModPower modActionFs=setup_meas.modPower(actionName).
                                        setFrequency("2.402 GHz").
                                        setExpectedMaxPower("20 dBm").
                                        setSamples(10000).
                                        setBandwidthOfInterest("10 MHz").
                                        setIfFrequency("25 MHz").//12.5 MHz at SMT7
                                        setSampleRate("25 MHz").
                                        setWindowFunction(SetupWindowFunction.hanning);

        deviceSetup.setSequentialSyncAccuracy(0.001);
        deviceSetup.actionCall(modActionFs);
        measurement.setSetups(deviceSetup);
    }


    @Override
    public void update() {
        if(freq_idx==0)//FREQL
        {
            measurement.rfMeas(RF_OUT).modPower(actionName).setFrequency(2.402e9);
        }
        else if(freq_idx==1)//FREQM
        {
            measurement.rfMeas(RF_OUT).modPower(actionName).setFrequency(2.441e9);//441e9
        }
        else if(freq_idx==2)//FREQH
        {
            measurement.rfMeas(RF_OUT).modPower(actionName).setFrequency(2.480e9);
        }

    }

    @Override
    public void execute() {


        measurement00.execute();
        IProtocolInterfaceResults paResult_BES1 = measurement00.protocolInterface("I2C_GRP_BES").preserveResults();
        ITransactionSequenceResults trseqResult_BES1 = paResult_BES1.transactSeq(trCallName1,0);

        MultiSiteLong data_0x8f = trseqResult_BES1.getValueAsLong("data_0x8f");
        MultiSiteLong data_0x91 = trseqResult_BES1.getValueAsLong("data_0x91");
//        println("data_0x8f=  0x"+Long.toHexString(data_0x8f.get(context.getActiveSites()[0])));
//        println("data_0x91=  0x"+Long.toHexString(data_0x91.get(context.getActiveSites()[0])));




        measurement.execute();
        IRfMeasResults rfResult = measurement.rfMeas(RF_OUT).preserveResults();

        MultiSiteWaveComplex waveform = rfResult.modPower(actionName).getComplexWaveform(RF_OUT).getElement(0);
        MultiSiteSpectrum spectrum = waveform.spectrum(SpectrumUnit.dBm);
        MultiSiteSpectrum spectrum_mW = waveform.spectrum(SpectrumUnit.mW);

        MultiSiteLong iBinCenter = new MultiSiteLong();
        MultiSiteLong iBinChecker = new MultiSiteLong();
        int[] activeSites = context.getActiveSites();

        iBinCenter = spectrum.maxIndex();
        double BinResolution =(spectrum.getSampleRate().get(activeSites[0])/spectrum.getSize().get(activeSites[0]))/1000;//KHz

        MultiSiteLong imax_300k =new MultiSiteLong();
        MultiSiteLong imax_960k =new MultiSiteLong();
        MultiSiteLong imax_640k =new MultiSiteLong();
        MultiSiteDouble PWR_Rf=new MultiSiteDouble();
        MultiSiteDouble PWR_Rf_300k=new MultiSiteDouble();
        MultiSiteDouble PWR_Rf_960k=new MultiSiteDouble();
        MultiSiteDouble PWR_Rf_640k=new MultiSiteDouble();
        MultiSiteDouble PWR_Rf_4p8M=new MultiSiteDouble();
        MultiSiteDouble PWR_Rf_noise=new MultiSiteDouble();

        MultiSiteDouble PWR_Lo=new MultiSiteDouble();
        MultiSiteDouble PWR_Imag=new MultiSiteDouble();
        MultiSiteLong extractSize_Power=new MultiSiteLong(8);
        MultiSiteLong extractSize_Power_0p64M=new MultiSiteLong(20);
        MultiSiteLong extractSize_Power_4p8M=new MultiSiteLong(40);
        MultiSiteLong extractSize_Power_noise=new MultiSiteLong(1980);
        try {
//            imax_960k=iBinCenter.add((long)(960/BinResolution));
//            PWR_Rf_960k=spectrum_mW.extractValues(imax_960k.subtract(4),extractSize_Power).sum();
            imax_300k=iBinCenter.add((long)(300/BinResolution));// 300K
            PWR_Rf_300k=spectrum_mW.extractValues(imax_300k.subtract(4),extractSize_Power).sum();
            imax_960k=iBinCenter.subtract((long)(960/BinResolution));
            PWR_Rf_960k=spectrum_mW.extractValues(imax_960k.subtract(4),extractSize_Power).sum();
            imax_640k=iBinCenter.add((long)(640/BinResolution));
            PWR_Rf_640k=spectrum_mW.extractValues(imax_640k.subtract(10),extractSize_Power_0p64M).sum();
            PWR_Rf_4p8M=spectrum_mW.extractValues(iBinCenter.add(16),extractSize_Power_4p8M).sum();//Rf Bin + 40k ~140k;
            PWR_Rf_noise=spectrum_mW.extractValues(iBinCenter.add(20),extractSize_Power_noise).sum();//Rf Bin + 50k ~ 5M;
            PWR_Rf=spectrum_mW.extractValues(iBinCenter.subtract(4),extractSize_Power).sum();
            PWR_Rf_4p8M=PWR_Rf_4p8M.log10().multiply(10).add(StaticFields.att_bt);
            PWR_Rf_noise=PWR_Rf_noise.log10().multiply(10).add(StaticFields.att_bt);
            PWR_Rf=PWR_Rf.log10().multiply(10).add(StaticFields.att_bt);
            PWR_Rf_300k=PWR_Rf_300k.log10().multiply(10).add(StaticFields.att_bt);
            PWR_Rf_960k=PWR_Rf_960k.log10().multiply(10).add(StaticFields.att_bt);
            PWR_Rf_640k=PWR_Rf_640k.log10().multiply(10).add(StaticFields.att_bt);
        } catch (Exception e) {
            // TODO: handle exception
        }

//        StaticFields.BT_TX_ACPR_0p96M.set(PWR_Rf.subtract(PWR_Rf_960k));

        String testSuiteName_Qualified=context.getTestSuiteName();
        String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
        if(StaticFields.debugMode)
        {
            println("**********"+testSuiteName+"**********");
            waveform.plot(testSuiteName+"_waveform");
            spectrum.plot(testSuiteName+"_Spectrum");
            println("BT_TX_ACPR_noise = "+PWR_Rf_noise);
            println("PWR_Rf_4p8M = "+PWR_Rf_4p8M);
            println("BT_TX_ACPR_PWR = "+PWR_Rf);
            println("BT_TX_ACPR_0p3M = "+(PWR_Rf.subtract(PWR_Rf_300k)));
            println("BT_TX_ACPR_0p9M = "+(PWR_Rf.subtract(PWR_Rf_960k)));
            println("BT_TX_ACPR_0p64M = "+(PWR_Rf.subtract(PWR_Rf_640k)));
            println();
        }
        long binIndex_960k=(long)(960/BinResolution);
        long binIndex_640k=(long)(640/BinResolution);

        MultiSiteDouble judge_PWR_0p96M=new MultiSiteDouble();//PWR_Rf.subtract(PWR_Rf_960k)
        MultiSiteDouble judge_PWR_0p64M=new MultiSiteDouble();//PWR_Rf.subtract(PWR_Rf_960k)
        if (StaticFields.dev_ver=="B") {
            for (int site:context.getActiveSites()) {

                if ( (PWR_Rf.get(site)-PWR_Rf_300k.get(site)>40))// judge 300K power, while 300K dBc pass
                {
                    if (( (PWR_Rf.subtract(PWR_Rf_640k).get(site)<48)||(PWR_Rf.subtract(PWR_Rf_960k).get(site)<58)  )) {//failed
                      measurement00.execute();
                      IProtocolInterfaceResults paResult_BES2 = measurement00.protocolInterface("I2C_GRP_BES").preserveResults();
                      ITransactionSequenceResults trseqResult_BES2 = paResult_BES2.transactSeq(trCallName1,0);

                      MultiSiteLong data_0x8f_post = trseqResult_BES2.getValueAsLong("data_0x8f");
                      MultiSiteLong data_0x91_post = trseqResult_BES2.getValueAsLong("data_0x91");

//                      println();
//                      println("site: "+site+"  data_0x8f_post=  0x"+Long.toHexString(data_0x8f_post.get(context.getActiveSites()[0])));
//                      println("site: "+site+"  data_0x91_post=  0x"+Long.toHexString(data_0x91_post.get(context.getActiveSites()[0])));
//                      println();

                        measurement_pre.spec().setVariable(site, "spec_data_0x8f", 0xff42);
                        measurement_pre.spec().setVariable(site, "spec_data_0x91", 0x8320);
                        measurement_pre.execute();

//                        measurement00.execute();
//                        IProtocolInterfaceResults paResult_BES3 = measurement00.protocolInterface("I2C_GRP_BES").preserveResults();
//                        ITransactionSequenceResults trseqResult_BES3 = paResult_BES3.transactSeq(trCallName1,0);
//
//                        MultiSiteLong data_0x8f_post_1 = trseqResult_BES3.getValueAsLong("data_0x8f");
//                        MultiSiteLong data_0x91_post_1 = trseqResult_BES3.getValueAsLong("data_0x91");
//                        println();
//                        println("site: "+site+"  data_0x8f_po_1=  0x"+Long.toHexString(data_0x8f_post_1.get(context.getActiveSites()[0])));
//                        println("site: "+site+"  data_0x91_p0_1=  0x"+Long.toHexString(data_0x91_post_1.get(context.getActiveSites()[0])));
//                        println();


                        measurement.execute();
                        IRfMeasResults rfResult2=measurement.rfMeas(RF_OUT).preserveResults();
                        MultiSiteWaveComplex tx_freq2=rfResult2.modPower(actionName).getComplexWaveform(RF_OUT).getElement(0);

                        BesCalc_BTTX bt_tx2=new BesCalc_BTTX(tx_freq2, 9);
                        bt_tx2.setAtt(StaticFields.att_bt);
                        //fsig - (flo+5fbb) 640K
                        MultiSiteDouble PWR_640K2= bt_tx2.getSpecificBinPower_mW( bt_tx2.getRfBinIndex().add(binIndex_640k-10), bt_tx2.getRfBinIndex().add(binIndex_640k+10));
                        PWR_640K2=PWR_640K2.log10().multiply(10).add(StaticFields.att_bt);

                        //fsig - (flo-7fbb) 960K
                        MultiSiteDouble PWR_960K2= bt_tx2.getSpecificBinPower_mW( bt_tx2.getRfBinIndex().subtract(binIndex_960k+4), bt_tx2.getRfBinIndex().subtract(binIndex_960k-4));
                        PWR_960K2=PWR_960K2.log10().multiply(10).add(StaticFields.att_bt);

//                        println();
//                        bt_tx2.getSpectrum_dBm().plot(testSuiteName+"step2_BTTX_Spectrum");
//                        println("BT_TX_ACPR_0p9M_2 = "+(bt_tx2.getRfPower_dBm().subtract(PWR_960K2)));
//                        println("BT_TX_ACPR_0p64M_2 = "+(bt_tx2.getRfPower_dBm().subtract(PWR_640K2)));
//                        println();
                      if ( (bt_tx2.getRfPower_dBm().subtract(PWR_960K2).get(site)<58) || (bt_tx2.getRfPower_dBm().subtract(PWR_640K2).get(site)<48))
                      {

                            measurement_pre.spec().setVariable(site, "spec_data_0x8f", 0xff42);
                            measurement_pre.spec().setVariable(site, "spec_data_0x91", 0x8b30);
                            measurement_pre.execute();

                            measurement.execute();
                            IRfMeasResults rfResult3=measurement.rfMeas(RF_OUT).preserveResults();
                            MultiSiteWaveComplex tx_freq3=rfResult3.modPower(actionName).getComplexWaveform(RF_OUT).getElement(0);

                            BesCalc_BTTX bt_tx3=new BesCalc_BTTX(tx_freq3, 9);
                            bt_tx3.setAtt(StaticFields.att_bt);
                            //fsig - (flo+5fbb) 640K
                            MultiSiteDouble PWR_640K3= bt_tx3.getSpecificBinPower_mW( bt_tx3.getRfBinIndex().add(binIndex_640k-10), bt_tx3.getRfBinIndex().add(binIndex_640k+10));
                            PWR_640K3=PWR_640K3.log10().multiply(10).add(StaticFields.att_bt);

                            //fsig - (flo-7fbb) 960K
                            MultiSiteDouble PWR_960K3= bt_tx3.getSpecificBinPower_mW( bt_tx3.getRfBinIndex().subtract(binIndex_960k+4), bt_tx3.getRfBinIndex().subtract(binIndex_960k-4));
                            PWR_960K3=PWR_960K3.log10().multiply(10).add(StaticFields.att_bt);



//                            println();
//                            bt_tx3.getSpectrum_dBm().plot(testSuiteName+"step3_BTTX_Spectrum");
//                            println("BT_TX_ACPR_0p9M_3 = "+(bt_tx3.getRfPower_dBm().subtract(PWR_960K3)));
//                            println("BT_TX_ACPR_0p64M_3 = "+(bt_tx3.getRfPower_dBm().subtract(PWR_640K3)));
//                            println();

                            if ( (bt_tx3.getRfPower_dBm().subtract(PWR_960K3).get(site)<58) || (bt_tx3.getRfPower_dBm().subtract(PWR_640K3).get(site)<48))
                            {
//                              ptd_BT_TX_ACPR_0p9M.setLowLimitToFail();
//                              ptd_BT_TX_ACPR_0p9M.setHighLimitToFail();
//                              ptd_BT_TX_ACPR_0p64M.setLowLimitToFail();
//                              ptd_BT_TX_ACPR_0p64M.setHighLimitToFail();
                                judge_PWR_0p96M.set(site,bt_tx3.getRfPower_dBm().subtract(PWR_960K3).get(site));
                                judge_PWR_0p64M.set(site,bt_tx3.getRfPower_dBm().subtract(PWR_640K3).get(site));

//                                ptd_BT_TX_ACPR_0p9M.evaluate(bt_tx3.getRfPower_dBm().subtract(PWR_960K3));
//                                ptd_BT_TX_ACPR_0p64M.evaluate(bt_tx3.getRfPower_dBm().subtract(PWR_640K3));
                            }
                            else {
    //                            println("mark3=pass!");
                                if(freq_idx==0) {
                                    //                                {
//                                    StaticFields.ACP_Marker.set(site, 0x4);
//                                }
                                    judge_PWR_0p96M.set(site,bt_tx3.getRfPower_dBm().subtract(PWR_960K3).get(site));
                                }
                                judge_PWR_0p64M.set(site,bt_tx3.getRfPower_dBm().subtract(PWR_640K3).get(site));
//                                ptd_BT_TX_ACPR_0p9M.evaluate(bt_tx3.getRfPower_dBm().subtract(PWR_960K3));
//                                ptd_BT_TX_ACPR_0p64M.evaluate(bt_tx3.getRfPower_dBm().subtract(PWR_640K3));
                              //pass mark3
                            }


                      }
                      else {//pass
////                      println("mark1=pass!");
//                          measurement_pre.spec().setVariable(site, "spec_data_0x8f", 0xff42);//default
//                          measurement_pre.spec().setVariable(site, "spec_data_0x91", 0x8220);//defult
//                          measurement_pre.execute();
//                          if(freq_idx==0)//FREQL
//                          {
//                              StaticFields.ACP_Marker.set(site, 0x2);
//                          }
//                      //pass mark1
                          judge_PWR_0p96M.set(site,bt_tx2.getRfPower_dBm().subtract(PWR_960K2).get(site));
                          judge_PWR_0p64M.set(site,bt_tx2.getRfPower_dBm().subtract(PWR_640K2).get(site));
//                          ptd_BT_TX_ACPR_0p9M.evaluate(bt_tx2.getRfPower_dBm().subtract(PWR_960K2));
//                          ptd_BT_TX_ACPR_0p64M.evaluate(bt_tx2.getRfPower_dBm().subtract(PWR_640K2));
//                          ptd_BT_TX_ACPR_0p9M.setLowLimitToPass();
//                          ptd_BT_TX_ACPR_0p9M.setHighLimitToPass();
//                          ptd_BT_TX_ACPR_0p64M.setLowLimitToPass();
//                          ptd_BT_TX_ACPR_0p64M.setHighLimitToPass();
                      }

                    }
                    else {
//                        measurement_pre.spec().setVariable(site, "spec_data_0x8f", 0xff42);//default
//                        measurement_pre.spec().setVariable(site, "spec_data_0x91", 0x8220);//defult
//                        measurement_pre.execute();
                        judge_PWR_0p96M.set(site,PWR_Rf.subtract(PWR_Rf_960k).get(site));
                        judge_PWR_0p64M.set(site,PWR_Rf.subtract(PWR_Rf_640k).get(site));
//                        ptd_BT_TX_ACPR_0p9M.evaluate(PWR_Rf.subtract(PWR_Rf_960k));
//                        ptd_BT_TX_ACPR_0p64M.evaluate(PWR_Rf.subtract(PWR_Rf_640k));
//                        ptd_BT_TX_ACPR_0p9M.setLowLimitToPass();
//                        ptd_BT_TX_ACPR_0p9M.setHighLimitToPass();
//                        ptd_BT_TX_ACPR_0p64M.setLowLimitToPass();
//                        ptd_BT_TX_ACPR_0p64M.setHighLimitToPass();
                    }


            }
            else {//300k failed
                measurement_pre.spec().setVariable(site, "spec_data_0x8f", 0xff62);//0xff42
                measurement_pre.spec().setVariable(site, "spec_data_0x91", 0x8220);//0x8220
                measurement_pre.execute();
//
                measurement.execute();
                IRfMeasResults rfResult1=measurement.rfMeas(RF_OUT).preserveResults();
                MultiSiteWaveComplex tx_freq1=rfResult1.modPower(actionName).getComplexWaveform(RF_OUT).getElement(0);

                BesCalc_BTTX bt_tx=new BesCalc_BTTX(tx_freq1, 9);
                bt_tx.setAtt(StaticFields.att_bt);
//                bt_tx.getSpectrum_dBm().plot("bt_tx");
                //fsig - (flo+5fbb) 640K
                MultiSiteDouble PWR_640K1= bt_tx.getSpecificBinPower_mW( bt_tx.getRfBinIndex().add(binIndex_640k-10), bt_tx.getRfBinIndex().add(binIndex_640k+10));
                PWR_640K1=PWR_640K1.log10().multiply(10).add(StaticFields.att_bt);

                //fsig - (flo-7fbb) 960K
                MultiSiteDouble PWR_960K1= bt_tx.getSpecificBinPower_mW( bt_tx.getRfBinIndex().subtract(binIndex_960k+4),bt_tx.getRfBinIndex().subtract(binIndex_960k-4));
                PWR_960K1=PWR_960K1.log10().multiply(10).add(StaticFields.att_bt);

//                println();
//                bt_tx.getSpectrum_dBm().plot(testSuiteName+"step1_BTTX_Spectrum");
//                println("BT_TX_ACPR_0p9M_1 = "+(bt_tx.getRfPower_dBm().subtract(PWR_960K1)));
//                println("BT_TX_ACPR_0p64M_1 = "+(bt_tx.getRfPower_dBm().subtract(PWR_640K1)));
//                println();
                  if ( (bt_tx.getRfPower_dBm().subtract(PWR_960K1).get(site)<58) || (bt_tx.getRfPower_dBm().subtract(PWR_640K1).get(site)<48))
                  {//faailed
//                        ptd_BT_TX_ACPR_0p9M.setLowLimitToFail();
//                        ptd_BT_TX_ACPR_0p9M.setHighLimitToFail();
//                        ptd_BT_TX_ACPR_0p64M.setLowLimitToFail();
//                        ptd_BT_TX_ACPR_0p64M.setHighLimitToFail();
                      judge_PWR_0p96M.set(site,bt_tx.getRfPower_dBm().subtract(PWR_960K1).get(site));
                      judge_PWR_0p64M.set(site,bt_tx.getRfPower_dBm().subtract(PWR_640K1).get(site));

//                        ptd_BT_TX_ACPR_0p9M.evaluate(bt_tx.getRfPower_dBm().subtract(PWR_960K1));
//                        ptd_BT_TX_ACPR_0p64M.evaluate(bt_tx.getRfPower_dBm().subtract(PWR_640K1));
                  }
//                  else {// marker 1
//////                  println("mark1=pass!");
//                      if(freq_idx==0)//FREQL
//                      {
//                          StaticFields.ACP_Marker.set(site, 0x1);
//                      }
//                      judge_PWR_0p96M.set(site,bt_tx.getRfPower_dBm().subtract(PWR_960K1).get(site));
//                      judge_PWR_0p64M.set(site,bt_tx.getRfPower_dBm().subtract(PWR_640K1).get(site));
//
//                }

            }

            }

//          measurement_pre.spec().setVariable("spec_data_0x8f", 0xff42);//0xff42
//          measurement_pre.spec().setVariable("spec_data_0x91", 0x8220);//0x8220
//          measurement_pre.execute();

            ptd_BT_TX_ACPR_0p9M.evaluate(judge_PWR_0p96M);
            ptd_BT_TX_ACPR_0p64M.evaluate(judge_PWR_0p64M);
        }
        else {
            judge_PWR_0p96M.set(PWR_Rf.subtract(PWR_Rf_960k));
            judge_PWR_0p64M.set(PWR_Rf.subtract(PWR_Rf_640k));
            ptd_BT_TX_ACPR_0p9M.evaluate(PWR_Rf.subtract(PWR_Rf_960k));
            ptd_BT_TX_ACPR_0p64M.evaluate(PWR_Rf.subtract(PWR_Rf_640k));
        }
//        println("StaticFields.ACP_Marker=  "+StaticFields.ACP_Marker);
//        StaticFields.ACP_Marker.set(0x2);
        ptd_BT_TX_ACPR_40k_140k_dcdctone_noise.evaluate(PWR_Rf.subtract(PWR_Rf_4p8M));//dBc
        if( (testSuiteName.equals("BT_TX_ACP_FREQL"))||(testSuiteName.equals("BT_TX_ACP_FREQM"))||(testSuiteName.equals("BT_TX_ACP_FREQH")) )
        {
            ptd_BT_TX_ACPR_50K_5M_noise.evaluate(PWR_Rf.subtract(PWR_Rf_noise));
        }
   }
}
