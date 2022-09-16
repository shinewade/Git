package BT1502_pro_tml.DC;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDcVI;
import xoc.dsa.ISetupDcVI.SetupConnectMode;
import xoc.dsa.ISetupDcVI.SetupDisconnectMode;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.dsp.MultiSiteSpectrum;
import xoc.dta.datatypes.dsp.MultiSiteWaveDouble;
import xoc.dta.datatypes.dsp.SpectrumUnit;
import xoc.dta.datatypes.dsp.WindowFunction;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class DCDC_RIPPLE_WAVE_TEST extends TestMethod {

    @In public String spec_measurement;

    public IMeasurement measurement;

    public IParametricTestDescriptor ptd_VANA_RIPPLE_WAVE_VPP  ,
                                     ptd_VCORE_RIPPLE_WAVE_VPP ,
                                     ptd_VCODEC_RIPPLE_WAVE_VPP,
                                     ptd_sigPowerVANA_dBm      ,
                                     ptd_sigPowerVCORE_dBm     ,
                                     ptd_sigPowerVCODEC_dBm    ;

    public int Samples=500;

    @Override
    public void setup() {
        //measurement
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);

        ISetupDcVI DcVI_DGT_VCORE=deviceSetup.addDcVI("VCORE_0p8").setDisconnect(true).setConnectMode(SetupConnectMode.highImpedance).setDisconnectMode(SetupDisconnectMode.hiz);
        DcVI_DGT_VCORE.level().setIrange("100 mA").setVrange("5 V");
        DcVI_DGT_VCORE.measureWaveform("cap_vcore_ripple_wave").setMeasTypeVoltage().setSampleRate("90*1e3 Hz").setSamples(Samples).setIrange("20 mA").setRestoreIrange(true);

        ISetupDcVI DcVI_DGT_VANA=deviceSetup.addDcVI("VANA").setDisconnect(true).setConnectMode(SetupConnectMode.highImpedance).setDisconnectMode(SetupDisconnectMode.hiz);
        DcVI_DGT_VANA.level().setIrange("100 mA").setVrange("5 V");
        DcVI_DGT_VANA.measureWaveform("cap_vana_ripple_wave").setMeasTypeVoltage().setSampleRate("90*1e3 Hz").setSamples(Samples).setIrange("20 mA").setRestoreIrange(true);

        ISetupDcVI DcVI_DGT_VCODEC=deviceSetup.addDcVI("VCODEC").setDisconnect(true).setConnectMode(SetupConnectMode.highImpedance).setDisconnectMode(SetupDisconnectMode.hiz);
        DcVI_DGT_VCODEC.level().setIrange("100 mA").setVrange("5 V");
        DcVI_DGT_VCODEC.measureWaveform("cap_vcodec_ripple_wave").setMeasTypeVoltage().setSampleRate("90*1e3 Hz").setSamples(Samples).setIrange("20 mA").setRestoreIrange(true);

        deviceSetup.sequentialBegin();
            deviceSetup.actionCall("cap_vcore_ripple_wave");
            deviceSetup.actionCall("cap_vana_ripple_wave");
            deviceSetup.actionCall("cap_vcodec_ripple_wave");
        deviceSetup.sequentialEnd();
        measurement.setSetups(deviceSetup);

    }

    @Override
    public void execute() {

        measurement.execute();
        IDcVIResults DPSResult1=measurement.dcVI().preserveResults();
        MultiSiteWaveDouble ripple_wave_VCORE =DPSResult1.measureWaveform("cap_vcore_ripple_wave") .getWaveform("VCORE_0p8").getElement(0);
        MultiSiteWaveDouble ripple_wave_VANA  =DPSResult1.measureWaveform("cap_vana_ripple_wave")  .getWaveform("VANA")    .getElement(0);
        MultiSiteWaveDouble ripple_wave_VCODEC=DPSResult1.measureWaveform("cap_vcodec_ripple_wave").getWaveform("VCODEC")  .getElement(0);

        MultiSiteLong minCountVANA = new MultiSiteLong(0);
        MultiSiteLong maxCountVANA = new MultiSiteLong(0);
        MultiSiteDouble minValueSumVANA   = new MultiSiteDouble(0);
        MultiSiteDouble maxValueSumVANA   = new MultiSiteDouble(0);
        MultiSiteDouble rippleWaveVppVANA = new MultiSiteDouble(0);
        MultiSiteLong minCountVCORE = new MultiSiteLong(0);
        MultiSiteLong maxCountVCORE = new MultiSiteLong(0);
        MultiSiteDouble minValueSumVCORE   = new MultiSiteDouble(0);
        MultiSiteDouble maxValueSumVCORE   = new MultiSiteDouble(0);
        MultiSiteDouble rippleWaveVppVCORE = new MultiSiteDouble(0);
        MultiSiteLong minCountVCODEC = new MultiSiteLong(0);
        MultiSiteLong maxCountVCODEC = new MultiSiteLong(0);
        MultiSiteDouble minValueSumVCODEC   = new MultiSiteDouble(0);
        MultiSiteDouble maxValueSumVCODEC   = new MultiSiteDouble(0);
        MultiSiteDouble rippleWaveVppVCODEC = new MultiSiteDouble(0);
        for(int site:context.getActiveSites())
        {
            for(int i=0; i<Samples; i++)
            {
                if(i>=2 && i<Samples-2)
                {
                    if(
                       ripple_wave_VANA.get(site).getValue(i)>ripple_wave_VANA.get(site).getValue(i-2) &&
                       ripple_wave_VANA.get(site).getValue(i)>ripple_wave_VANA.get(site).getValue(i-1) &&
                       ripple_wave_VANA.get(site).getValue(i)>=ripple_wave_VANA.get(site).getValue(i+1) &&
                       ripple_wave_VANA.get(site).getValue(i)>ripple_wave_VANA.get(site).getValue(i+2)
                      )
                    {
                        maxValueSumVANA.set(site,maxValueSumVANA.get(site)+ripple_wave_VANA.get(site).getValue(i));
                        maxCountVANA.set(site, maxCountVANA.get(site)+1);
                    }
                    if(
                            ripple_wave_VANA.get(site).getValue(i)<ripple_wave_VANA.get(site).getValue(i-2) &&
                            ripple_wave_VANA.get(site).getValue(i)<ripple_wave_VANA.get(site).getValue(i-1) &&
                            ripple_wave_VANA.get(site).getValue(i)<=ripple_wave_VANA.get(site).getValue(i+1) &&
                            ripple_wave_VANA.get(site).getValue(i)<ripple_wave_VANA.get(site).getValue(i+2)
                       )
                     {
                        minValueSumVANA.set(site,minValueSumVANA.get(site)+ripple_wave_VANA.get(site).getValue(i));
                        minCountVANA.set(site, minCountVANA.get(site)+1);
                     }

                    if(
                        ripple_wave_VCORE.get(site).getValue(i)> ripple_wave_VCORE.get(site).getValue(i-2) &&
                        ripple_wave_VCORE.get(site).getValue(i)> ripple_wave_VCORE.get(site).getValue(i-1) &&
                        ripple_wave_VCORE.get(site).getValue(i)>=ripple_wave_VCORE.get(site).getValue(i+1) &&
                        ripple_wave_VCORE.get(site).getValue(i)> ripple_wave_VCORE.get(site).getValue(i+2)
                       )
                     {
                         maxValueSumVCORE.set(site,maxValueSumVCORE.get(site)+ripple_wave_VCORE.get(site).getValue(i));
                         maxCountVCORE.set(site, maxCountVCORE.get(site)+1);
                     }
                     if(
                             ripple_wave_VCORE.get(site).getValue(i)< ripple_wave_VCORE.get(site).getValue(i-2) &&
                             ripple_wave_VCORE.get(site).getValue(i)< ripple_wave_VCORE.get(site).getValue(i-1) &&
                             ripple_wave_VCORE.get(site).getValue(i)<=ripple_wave_VCORE.get(site).getValue(i+1) &&
                             ripple_wave_VCORE.get(site).getValue(i)< ripple_wave_VCORE.get(site).getValue(i+2)
                        )
                      {
                         minValueSumVCORE.set(site,minValueSumVCORE.get(site)+ripple_wave_VCORE.get(site).getValue(i));
                         minCountVCORE.set(site, minCountVCORE.get(site)+1);
                      }

                     if(
                         ripple_wave_VCODEC.get(site).getValue(i)> ripple_wave_VCODEC.get(site).getValue(i-2) &&
                         ripple_wave_VCODEC.get(site).getValue(i)> ripple_wave_VCODEC.get(site).getValue(i-1) &&
                         ripple_wave_VCODEC.get(site).getValue(i)>=ripple_wave_VCODEC.get(site).getValue(i+1) &&
                         ripple_wave_VCODEC.get(site).getValue(i)> ripple_wave_VCODEC.get(site).getValue(i+2)
                        )
                      {
                          maxValueSumVCODEC.set(site,maxValueSumVCODEC.get(site)+ripple_wave_VCODEC.get(site).getValue(i));
                          maxCountVCODEC.set(site, maxCountVCODEC.get(site)+1);
                      }
                      if(
                          ripple_wave_VCODEC.get(site).getValue(i)< ripple_wave_VCODEC.get(site).getValue(i-2) &&
                          ripple_wave_VCODEC.get(site).getValue(i)< ripple_wave_VCODEC.get(site).getValue(i-1) &&
                          ripple_wave_VCODEC.get(site).getValue(i)<=ripple_wave_VCODEC.get(site).getValue(i+1) &&
                          ripple_wave_VCODEC.get(site).getValue(i)< ripple_wave_VCODEC.get(site).getValue(i+2)
                         )
                       {
                          minValueSumVCODEC.set(site,minValueSumVCODEC.get(site)+ripple_wave_VCODEC.get(site).getValue(i));
                          minCountVCODEC.set(site, minCountVCODEC.get(site)+1);
                       }
                }
            }
        }
        rippleWaveVppVANA = maxValueSumVANA.divide(maxCountVANA).subtract(minValueSumVANA.divide(minCountVANA));
        rippleWaveVppVCORE = maxValueSumVCORE.divide(maxCountVCORE).subtract(minValueSumVCORE.divide(minCountVCORE));
        rippleWaveVppVCODEC = maxValueSumVCODEC.divide(maxCountVCODEC).subtract(minValueSumVCODEC.divide(minCountVCODEC));
//        double sampleTime = ripple_wave_VANA.getSampleRate().reciprocal().multiply(ripple_wave_VANA.getSize()).get(context.getActiveSites()[0]);
//        MultiSiteDouble freqVpCountVANA = new MultiSiteDouble(sampleTime).divide(minCountVANA).reciprocal();

        int binCount = 2;
        double binResVANA   = ripple_wave_VANA  .getSampleRate().divide(ripple_wave_VANA  .getSize()).get(context.getActiveSites()[0]);
        double binResVCORE  = ripple_wave_VCORE .getSampleRate().divide(ripple_wave_VCORE .getSize()).get(context.getActiveSites()[0]);
        double binResVCODEC = ripple_wave_VCODEC.getSampleRate().divide(ripple_wave_VCODEC.getSize()).get(context.getActiveSites()[0]);
        MultiSiteSpectrum specVANA  =ripple_wave_VANA  .subtract(ripple_wave_VANA  .min()).setTermination(50).setWindowFunction(WindowFunction.HANNING).spectrum(SpectrumUnit.mW);
        MultiSiteSpectrum specVCORE =ripple_wave_VCORE .subtract(ripple_wave_VCORE .min()).setTermination(50).setWindowFunction(WindowFunction.HANNING).spectrum(SpectrumUnit.mW);
        MultiSiteSpectrum specVCODEC=ripple_wave_VCODEC.subtract(ripple_wave_VCODEC.min()).setTermination(50).setWindowFunction(WindowFunction.HANNING).spectrum(SpectrumUnit.mW);

        for(int i=0;i<3;i++){
            specVANA  .setValue(i, new MultiSiteDouble(0));
            specVCORE .setValue(i, new MultiSiteDouble(0));
            specVCODEC.setValue(i, new MultiSiteDouble(0));
        }
        MultiSiteLong sigPowerIndexVANA   = specVANA.maxIndex();
        MultiSiteLong sigPowerIndexVCORE  = specVCORE.maxIndex();
        MultiSiteLong sigPowerIndexVCODEC = specVCODEC.maxIndex();

        MultiSiteDouble sigPowerVANA_mW    = new MultiSiteDouble();
        MultiSiteDouble sigPowerVCORE_mW   = new MultiSiteDouble();
        MultiSiteDouble sigPowerVCODEC_mW  = new MultiSiteDouble();
        MultiSiteDouble sigPowerVANA_dBm   = new MultiSiteDouble();
        MultiSiteDouble sigPowerVCORE_dBm  = new MultiSiteDouble();
        MultiSiteDouble sigPowerVCODEC_dBm = new MultiSiteDouble();
        for(int site:context.getActiveSites())
        {
            if(sigPowerIndexVANA.get(site)  >=specVANA.getSize(site)  -binCount || sigPowerIndexVANA.get(site)  <binCount)
            {
                sigPowerVANA_mW .set(site,999);
                sigPowerVANA_dBm.set(site,999);
            }
            else
            {
                for(int i = sigPowerIndexVANA.getAsInt(site)-binCount; i <= sigPowerIndexVANA.getAsInt(site)+binCount; i++)
                {
                    if(i > 0 && i < specVANA.getSize(site))
                    {
                        sigPowerVANA_mW.set(site,sigPowerVANA_mW.get(site) + specVANA.getValue(site,i));
                    }
                }
                sigPowerVANA_dBm.set(site,Math.log10(sigPowerVANA_mW.get(site))*10);
            }

            if(sigPowerIndexVCORE.get(site) >=specVCORE.getSize(site) -binCount || sigPowerIndexVCORE.get(site) <binCount)
            {
                sigPowerVCORE_mW .set(site,999);
                sigPowerVCORE_dBm.set(site,999);
            }
            else
            {
                for(int i = sigPowerIndexVCORE.getAsInt(site)-binCount; i <= sigPowerIndexVCORE.getAsInt(site)+binCount; i++)
                {
                    if(i > 0 && i < specVCORE.getSize(site))
                    {
                        sigPowerVCORE_mW.set(site,sigPowerVCORE_mW.get(site) + specVCORE.getValue(site,i));
                    }
                }
                sigPowerVCORE_dBm.set(site,Math.log10(sigPowerVCORE_mW.get(site))*10);
            }

            if(sigPowerIndexVCODEC.get(site)>=specVCODEC.getSize(site)-binCount || sigPowerIndexVCODEC.get(site)<binCount)
            {
                sigPowerVCODEC_mW .set(site,999);
                sigPowerVCODEC_dBm.set(site,999);
            }
            else
            {
                for(int i = sigPowerIndexVCODEC.getAsInt(site)-binCount; i <= sigPowerIndexVCODEC.getAsInt(site)+binCount; i++)
                {
                    if(i > 0 && i < specVCODEC.getSize(site))
                    {
                        sigPowerVCODEC_mW.set(site,sigPowerVCODEC_mW.get(site) + specVANA.getValue(site,i));
                    }
                }
                sigPowerVCODEC_dBm.set(site,Math.log10(sigPowerVCODEC_mW.get(site))*10);
            }
        }
//        if(iSignalIndex>=this.spectrum_I.getSize(site)-this.signalBinCount || iSignalIndex<this.signalBinCount || qSignalIndex>=this.spectrum_Q.getSize(site)-this.signalBinCount || qSignalIndex<this.signalBinCount)
//        {
//            sigPowerVANA_mW    = specVANA  .sum(sigPowerIndexVANA  .subtract(binCount), sigPowerIndexVANA  .add(binCount+1));
//            sigPowerVCORE_mW   = specVCORE .sum(sigPowerIndexVCORE .subtract(binCount), sigPowerIndexVCORE .add(binCount+1));
//            sigPowerVCODEC_mW  = specVCODEC.sum(sigPowerIndexVCODEC.subtract(binCount), sigPowerIndexVCODEC.add(binCount+1));
//            sigPowerVANA_dBm   = sigPowerVANA_mW  .log10().multiply(10);
//            sigPowerVCORE_dBm  = sigPowerVCORE_mW .log10().multiply(10);
//            sigPowerVCODEC_dBm = sigPowerVCODEC_mW.log10().multiply(10);
//        }

        MultiSiteDouble freqVANA   = sigPowerIndexVANA  .multiply(binResVANA);
        MultiSiteDouble freqVCORE  = sigPowerIndexVCORE .multiply(binResVCORE);
        MultiSiteDouble freqVCODEC = sigPowerIndexVCODEC.multiply(binResVCODEC);

//        MultiSiteDouble VPPVANA_mW2V = sigPowerVANA_mW.divide(20.0).sqrt().multiply(1e3);
//        MultiSiteDouble VPPVANA_mW2V = sigPowerVANA_mW.divide(20.0).sqrt().multiply(1e3);

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            ripple_wave_VCORE .plot("ripple_wave_VCORE");
            ripple_wave_VANA  .plot("ripple_wave_VANA");
            ripple_wave_VCODEC.plot("ripple_wave_VCODEC");
            specVANA  .plot("specVANA");
            specVCORE .plot("specVCORE");
            specVCODEC.plot("specVCODEC");

//            println("maxValueSum[V] = "+maxValueSum);
//            println("maxCount = "+maxCount);
//            println("minValueSum[V] = "+minValueSum);
//            println("minCount = "+minCount);
            println("rippleWaveVppVANA[mV]   = "+rippleWaveVppVANA.multiply(1e3));
            println("rippleWaveVppVCORE[mV]  = "+rippleWaveVppVCORE.multiply(1e3));
            println("rippleWaveVppVCODEC[mV] = "+rippleWaveVppVCODEC.multiply(1e3));
//            println("sampleTime = "+sampleTime);
//            println("freqVpCountVANA = "+freqVpCountVANA);
            println();
//            println("sigPowerIndex = "+sigPowerIndex);
//            println("binRes = "+binRes);
            println("sigPowerVANA_dBm = "+sigPowerVANA_dBm);
            println("sigPowerVCORE_dBm = "+sigPowerVCORE_dBm);
            println("sigPowerVCODEC_dBm = "+sigPowerVCODEC_dBm);
            println("freqVANA   = "+freqVANA);
            println("freqVCORE  = "+freqVCORE);
            println("freqVCODEC = "+freqVCODEC);
//            println("VPPVANA_mW2V = "+VPPVANA_mW2V);
            println();
        }

        ptd_VANA_RIPPLE_WAVE_VPP  .evaluate(rippleWaveVppVANA);
        ptd_VCORE_RIPPLE_WAVE_VPP .evaluate(rippleWaveVppVCORE);
        ptd_VCODEC_RIPPLE_WAVE_VPP.evaluate(rippleWaveVppVCODEC);
        ptd_sigPowerVANA_dBm      .evaluate(sigPowerVANA_dBm);
        ptd_sigPowerVCORE_dBm     .evaluate(sigPowerVCORE_dBm);
        ptd_sigPowerVCODEC_dBm    .evaluate(sigPowerVCODEC_dBm);

    }
}
//dB   = 10*log(P1/P2) = 20*log(V1/V2);
//dBm  = 10*log(P/P0) (P0=1mW)   =10*log( (V^2/R)/(V0^2/R) )
//dBuV = 20*log(V/V0) (V0=1uV)
//P    = V^2/R
