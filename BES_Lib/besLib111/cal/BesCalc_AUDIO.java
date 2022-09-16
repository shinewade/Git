package besLib.cal;

import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.dsp.MultiSiteSpectrum;
import xoc.dta.datatypes.dsp.MultiSiteWaveDouble;
import xoc.dta.datatypes.dsp.SpectrumUnit;
import xoc.dta.datatypes.dsp.Weighting;
import xoc.dta.datatypes.dsp.WindowFunction;
import xoc.dta.datatypes.dsp.WindowScaling;

/**
 * This class is used to calculate BES Audio parameter
 * @version V1.1
 * @author Ronnie Li, Weng Yongxin
 **/
public class BesCalc_AUDIO{

    private int[] activeSites;
    private int                 num_pts_L;
    private int                 num_pts_R;
    private MultiSiteSpectrum   spec_L         = new MultiSiteSpectrum();
    private MultiSiteSpectrum   spec_R         = new MultiSiteSpectrum();
    private MultiSiteSpectrum   spec_A_L       = new MultiSiteSpectrum();
    private MultiSiteSpectrum   spec_A_R       = new MultiSiteSpectrum();
    private MultiSiteSpectrum   spec_L_dBm     = new MultiSiteSpectrum();
    private MultiSiteSpectrum   spec_R_dBm     = new MultiSiteSpectrum();
    private MultiSiteSpectrum   spec_A_L_dBm   = new MultiSiteSpectrum();
    private MultiSiteSpectrum   spec_A_R_dBm   = new MultiSiteSpectrum();
    private MultiSiteLong       imax_L         = new MultiSiteLong();
    private MultiSiteLong       imax_R         = new MultiSiteLong();
    private MultiSiteDouble     PowSig_L       = new MultiSiteDouble(0);
    private MultiSiteDouble     PowNor_L       = new MultiSiteDouble(0);
    private MultiSiteDouble     PowHarm_L      = new MultiSiteDouble(0);
    private MultiSiteDouble     PowSig_A_L     = new MultiSiteDouble(0);
    private MultiSiteDouble     PowNor_A_L     = new MultiSiteDouble(0);
    private MultiSiteDouble     PowHarm_A_L    = new MultiSiteDouble(0);
    private MultiSiteDouble     PowSig_R       = new MultiSiteDouble(0);
    private MultiSiteDouble     PowNor_R       = new MultiSiteDouble(0);
    private MultiSiteDouble     PowHarm_R      = new MultiSiteDouble(0);
    private MultiSiteDouble     PowSig_A_R     = new MultiSiteDouble(0);
    private MultiSiteDouble     PowNor_A_R     = new MultiSiteDouble(0);
    private MultiSiteDouble     PowHarm_A_R    = new MultiSiteDouble(0);

    private double bin_res_L;
    private double bin_res_R;
    private double FreqExpected_L_Hz   = 1e3;
    private double FreqExpected_R_Hz   = 1e3;

    /**
     * Constructor
     * Do FFT and necessary process for parameter calculation in class function
     *
     * @param waveform_L raw data captured by WSMX HR DGT from LOUT_L
     * @param waveform_R raw data captured by WSMX HR DGT from LOUT_R
     */
    public BesCalc_AUDIO(MultiSiteWaveDouble waveform_L, MultiSiteWaveDouble waveform_R) {
        this.activeSites = waveform_L.getActiveSites();

        int tone_1k_coh=7;
        int tone_thd_coh=2;
        int thd_cut=20;

        num_pts_L = waveform_L.getSize(activeSites[0])/2;
        num_pts_R = waveform_R.getSize(activeSites[0])/2;

        bin_res_L = waveform_L.getSampleRate(activeSites[0])/waveform_L.getSize(activeSites[0]);//Hz per sample
        bin_res_R = waveform_R.getSampleRate(activeSites[0])/waveform_R.getSize(activeSites[0]);//Hz per sample

        spec_L_dBm = waveform_L.setTermination(50).setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).spectrum(SpectrumUnit.dBm);
        spec_R_dBm = waveform_R.setTermination(50).setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).spectrum(SpectrumUnit.dBm);
        spec_A_L_dBm = waveform_L.frequencyWeighting(Weighting.A).setTermination(50).setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).spectrum(SpectrumUnit.dBm);
        spec_A_R_dBm = waveform_R.frequencyWeighting(Weighting.A).setTermination(50).setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).spectrum(SpectrumUnit.dBm);
        spec_L = waveform_L.setTermination(50).setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).spectrum(SpectrumUnit.mW);
        spec_R = waveform_R.setTermination(50).setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).spectrum(SpectrumUnit.mW);
        spec_A_L = waveform_L.frequencyWeighting(Weighting.A).setTermination(50).setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).spectrum(SpectrumUnit.mW);
        spec_A_R = waveform_R.frequencyWeighting(Weighting.A).setTermination(50).setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).spectrum(SpectrumUnit.mW);

        //remove DC component
        for(int i=0;i<4;i++){
            spec_L.setValue(i,new MultiSiteDouble(0));
            spec_R.setValue(i,new MultiSiteDouble(0));
            spec_A_L.setValue(i,new MultiSiteDouble(0));
            spec_A_R.setValue(i,new MultiSiteDouble(0));
        }

        //search max bin in 0.5KHz~1.5KHz (when FreqExpected = 1KHz)
        imax_L=spec_L.extractValues((int)(FreqExpected_L_Hz*0.5/bin_res_L), (int)(FreqExpected_L_Hz/bin_res_L)).maxIndex().add((int)(FreqExpected_L_Hz*0.5/bin_res_L));
        imax_R=spec_R.extractValues((int)(FreqExpected_R_Hz*0.5/bin_res_R), (int)(FreqExpected_R_Hz/bin_res_R)).maxIndex().add((int)(FreqExpected_R_Hz*0.5/bin_res_R));

        for(int site:activeSites)        {
            int imax_L_int=(imax_L.getAsInt(site));
            int imax_R_int=(imax_R.getAsInt(site));

            if(imax_L_int+tone_1k_coh>=this.spec_L.getSize(site) || imax_L_int<tone_1k_coh) {
                PowSig_L.set(site,999);
                PowSig_A_L.set(site,999);
            }
            else if(imax_R_int+tone_1k_coh>=this.spec_R.getSize(site) || imax_R_int<tone_1k_coh) {
                PowSig_R.set(site,999);
                PowSig_A_R.set(site,999);
            }
            else{
                PowSig_L.set(site,spec_L.get(site).sum(imax_L_int-tone_1k_coh, imax_L_int+tone_1k_coh+1));
                PowSig_A_L.set(site,spec_A_L.get(site).sum(imax_L_int-tone_1k_coh, imax_L_int+tone_1k_coh+1));
                PowSig_R.set(site,spec_R.get(site).sum(imax_L_int-tone_1k_coh, imax_L_int+tone_1k_coh+1));
                PowSig_A_R.set(site,spec_A_R.get(site).sum(imax_L_int-tone_1k_coh, imax_L_int+tone_1k_coh+1));
            }

            // 20KHz harmonic power
            for( int i=2; i<=thd_cut;i++)            {
                if((i*imax_L_int+tone_thd_coh)< (num_pts_L/2))                {
                    PowHarm_L.set(site,PowHarm_L.get(site)+spec_L.get(site).sum(i*imax_L_int-tone_thd_coh, i*imax_L_int+tone_thd_coh+1));
                    PowHarm_A_L.set(site,PowHarm_A_L.get(site)+spec_A_L.get(site).sum(i*imax_L_int-tone_thd_coh, i*imax_L_int+tone_thd_coh+1));
                }
                if((i*imax_R_int+tone_thd_coh)<(num_pts_R/2))                {
                    PowHarm_R.set(site,PowHarm_R.get(site)+spec_R.get(site).sum(i*imax_R_int-tone_thd_coh, i*imax_R_int+tone_thd_coh+1));
                    PowHarm_A_R.set(site,PowHarm_A_R.get(site)+spec_A_R.get(site).sum(i*imax_R_int-tone_thd_coh, i*imax_R_int+tone_thd_coh+1));
                }
            }

            // 20KHz noise power
            PowNor_L.set(site,spec_L.get(site).sum(3, spec_L.frequencyToBin(20e3).getAsInt(site)+tone_thd_coh+1));
            PowNor_R.set(site,spec_R.get(site).sum(3, spec_L.frequencyToBin(20e3).getAsInt(site)+tone_thd_coh+1));
            PowNor_A_L.set(site,spec_A_L.get(site).sum(3, spec_L.frequencyToBin(20e3).getAsInt(site)+tone_thd_coh+1));
            PowNor_A_R.set(site,spec_A_R.get(site).sum(3, spec_L.frequencyToBin(20e3).getAsInt(site)+tone_thd_coh+1));

        }

    }

    /**
     * get spectrum getBinResolution of LOUTL.
     * @return Bin Resolution in unit Hz.
     */
    public double getBinResolution_L() {
        return this.bin_res_L;
    }

    /**
     * get spectrum getBinResolution of LOUTR.
     * @return Bin Resolution in unit Hz.
     */
    public double getBinResolution_R() {
        return this.bin_res_R;
    }

    /**
     * get waveform_L Frequency in unit Hz.
     * @return waveform_L Frequency in unit Hz.
     */
    public MultiSiteDouble getFrequency_L() {
        return imax_L.multiply(bin_res_L);
    }

    /**
     * get waveform_R Frequency in unit Hz.
     * @return waveform_R Frequency in unit Hz.
     */
    public MultiSiteDouble getFrequency_R() {
        return imax_R.multiply(bin_res_R);
    }

    /**
     * get waveform_L spectrum in dBm.
     * @return waveform_L spectrum in unit dBm.
     */
    public MultiSiteSpectrum getSpectrum_L_dBm() {
        return this.spec_L_dBm;
    }

    /**
     * get waveform_R spectrum in dBm.
     * @return waveform_R spectrum in unit dBm.
     */
    public MultiSiteSpectrum getSpectrum_R_dBm() {
        return this.spec_R_dBm;
    }

    /**
     * get waveform_L spectrum Weighting A in dBm.
     * @return waveform_L spectrum Weighting A in unit dBm.
     */
    public MultiSiteSpectrum getSpectrum_A_L_dBm() {
        return this.spec_A_L_dBm;
    }

    /**
     * get waveform_R spectrum Weighting A in dBm.
     * @return waveform_R spectrum Weighting A in unit dBm.
     */
    public MultiSiteSpectrum getSpectrum_A_R_dBm() {
        return this.spec_A_R_dBm;
    }

    /**
     * get waveform_L spectrum in mW.
     * @return waveform_L spectrum in unit mW.
     */
    public MultiSiteSpectrum getSpectrum_L_mW() {
        return this.spec_L;
    }

    /**
     * get waveform_R spectrum in mW.
     * @return waveform_R spectrum in unit mW.
     */
    public MultiSiteSpectrum getSpectrum_R_mW() {
        return this.spec_R;
    }

    /**
     * get waveform_L spectrum Weighting A in mW.
     * @return waveform_L spectrum Weighting A in unit mW.
     */
    public MultiSiteSpectrum getSpectrum_A_L_mW() {
        return this.spec_A_L;
    }

    /**
     * get waveform_R spectrum Weighting A in mW.
     * @return waveform_R spectrum Weighting A in unit mW.
     */
    public MultiSiteSpectrum getSpectrum_A_R_mW() {
        return this.spec_A_R;
    }

    /**
     * calculate Audio 1K signal L power.
     * @return Audio 1K signal L power in unit mW.
     */
    public MultiSiteDouble get1KPower_L_mW() {
        return this.PowSig_L;
    }

    /**
     * calculate Audio 1K signal R power.
     * @return Audio 1K signal R power in unit mW.
     */
    public MultiSiteDouble get1KPower_R_mW() {
        return this.PowSig_R;
    }

    /**
     * calculate Audio 1K signal L power  Weighting A.
     * @return Audio 1K signal L power Weighting A in unit mW.
     */
    public MultiSiteDouble get1KPower_A_L_mW() {
        return this.PowSig_A_L;
    }

    /**
     * calculate Audio 1K signal R power Weighting A.
     * @return Audio 1K signal R power Weighting A in unit mW.
     */
    public MultiSiteDouble get1KPower_A_R_mW() {
        return this.PowSig_A_R;
    }

    /**
     * calculate waveform_L 20KHz total harmonic power.
     * @return waveform_L 20KHz total harmonic power in unit mW.
     */
    public MultiSiteDouble getHarmPower_L_mW() {
        return this.PowHarm_L;
    }

    /**
     * calculate waveform_R 20KHz total harmonic power.
     * @return waveform_R 20KHz total harmonic power in unit mW.
     */
    public MultiSiteDouble getHarmPower_R_mW() {
        return this.PowHarm_R;
    }

    /**
     * calculate waveform_L 20KHz total harmonic power Weighting A.
     * @return waveform_L 20KHz total harmonic power Weighting A in unit mW.
     */
    public MultiSiteDouble getHarmPower_A_L_mW() {
        return this.PowHarm_A_L;
    }

    /**
     * calculate waveform_R 20KHz total harmonic power Weighting A.
     * @return waveform_R 20KHz total harmonic power Weighting A in unit mW.
     */
    public MultiSiteDouble getHarmPower_A_R_mW() {
        return this.PowHarm_A_R;
    }

    /**
     * calculate waveform_L 20KHz noise power.
     * @return waveform_L 20KHz noise power in unit mW.
     */
    public MultiSiteDouble getNoisePower_L_mW() {
        return PowNor_L.subtract(PowSig_L).subtract(PowHarm_L);
    }

    /**
     * calculate waveform_R 20KHz noise power.
     * @return waveform_R 20KHz noise power in unit mW.
     */
    public MultiSiteDouble getNoisePower_R_mW() {
        return PowNor_R.subtract(PowSig_R).subtract(PowHarm_R);
    }

    /**
     * calculate waveform_L 20KHz noise power Weighting A.
     * @return waveform_L 20KHz noise power Weighting A in unit mW.
     */
    public MultiSiteDouble getNoisePower_A_L_mW() {
        return PowNor_A_L.subtract(PowSig_A_L).subtract(PowHarm_A_L);
    }

    /**
     * calculate waveform_R 20KHz noise power Weighting A.
     * @return waveform_R 20KHz noise power Weighting A in unit mW.
     */
    public MultiSiteDouble getNoisePower_A_R_mW() {
        return PowNor_A_R.subtract(PowSig_A_R).subtract(PowHarm_A_R);
    }

    /**
     * calculate Audio 1K signal L power.
     * @return Audio 1K signal L power in unit dBm.
     */
    public MultiSiteDouble get1KPower_L_dBm() {
        return this.PowSig_L.log10().multiply(10);
    }

    /**
     * calculate Audio 1K signal R power.
     * @return Audio 1K signal R power in unit dBm.
     */
    public MultiSiteDouble get1KPower_R_dBm() {
        return this.PowSig_R.log10().multiply(10);
    }

    /**
     * calculate Audio 1K signal L/R power imbalances.
     * @return Audio 1K signal L/R power imbalances.
     */
    public MultiSiteDouble get1KPower_LR_IMB() {
        return this.PowSig_L.divide(PowSig_R).log10().multiply(10);
    }

    /**
     * calculate Audio 1K signal L power  Weighting A.
     * @return Audio 1K signal L power Weighting A in unit dBm.
     */
    public MultiSiteDouble get1KPower_A_L_dBm() {
        return this.PowSig_A_L.log10().multiply(10);
    }

    /**
     * calculate Audio 1K signal R power Weighting A.
     * @return Audio 1K signal R power Weighting A in unit dBm.
     */
    public MultiSiteDouble get1KPower_A_R_dBm() {
        return this.PowSig_A_R.log10().multiply(10);
    }

    /**
     * calculate waveform_L 20KHz total harmonic power.
     * @return waveform_L 20KHz total harmonic power in unit dBm.
     */
    public MultiSiteDouble getHarmPower_L_dBm() {
        return this.PowHarm_L.log10().multiply(10);
    }

    /**
     * calculate waveform_R 20KHz total harmonic power.
     * @return waveform_R 20KHz total harmonic power in unit dBm.
     */
    public MultiSiteDouble getHarmPower_R_dBm() {
        return this.PowHarm_R.log10().multiply(10);
    }

    /**
     * calculate waveform_L 20KHz total harmonic power Weighting A.
     * @return waveform_L 20KHz total harmonic power Weighting A in unit dBm.
     */
    public MultiSiteDouble getHarmPower_A_L_dBm() {
        return this.PowHarm_A_L.log10().multiply(10);
    }

    /**
     * calculate waveform_R 20KHz total harmonic power Weighting A.
     * @return waveform_R 20KHz total harmonic power Weighting A in unit dBm.
     */
    public MultiSiteDouble getHarmPower_A_R_dBm() {
        return this.PowHarm_A_R.log10().multiply(10);
    }

    /**
     * calculate waveform_L 20KHz noise power.
     * @return waveform_L 20KHz noise power in unit dBm.
     */
    public MultiSiteDouble getNoisePower_L_dBm() {
        return PowNor_L.subtract(PowSig_L).subtract(PowHarm_L).log10().multiply(10);
    }

    /**
     * calculate waveform_R 20KHz noise power.
     * @return waveform_R 20KHz noise power in unit dBm.
     */
    public MultiSiteDouble getNoisePower_R_dBm() {
        return PowNor_R.subtract(PowSig_R).subtract(PowHarm_R).log10().multiply(10);
    }

    /**
     * calculate waveform_L 20KHz noise power Weighting A.
     * @return waveform_L 20KHz noise power Weighting A in unit dBm.
     */
    public MultiSiteDouble getNoisePower_A_L_dBm() {
        return PowNor_A_L.subtract(PowSig_A_L).subtract(PowHarm_A_L).log10().multiply(10);
    }

    /**
     * calculate waveform_R 20KHz noise power Weighting A.
     * @return waveform_R 20KHz noise power Weighting A in unit dBm.
     */
    public MultiSiteDouble getNoisePower_A_R_dBm() {
        return PowNor_A_R.subtract(PowSig_A_R).subtract(PowHarm_A_R).log10().multiply(10);
    }

    /**
     * calculate waveform_L SNR.
     * @return waveform_L SNR.
     */
    public MultiSiteDouble getSNR_L() {
        return (PowSig_L.log10().multiply(10)).subtract(PowNor_L.subtract(PowSig_L).subtract(PowHarm_L).log10().multiply(10));
    }

    /**
     * calculate waveform_R SNR.
     * @return waveform_R SNR.
     */
    public MultiSiteDouble getSNR_R() {
        return (PowSig_R.divide(PowNor_R.subtract(PowSig_R).subtract(PowHarm_R))).log10().multiply(10);
    }


    /**
     * calculate waveform_L SNR Weighting A.
     * @return waveform_L SNR Weighting A.
     */
    public MultiSiteDouble getSNR_A_L() {
        return (PowSig_A_L.divide(PowNor_A_L.subtract(PowSig_A_L).subtract(PowHarm_A_L))).log10().multiply(10);
    }

    /**
     * calculate waveform_R SNR Weighting A.
     * @return waveform_R SNR Weighting A.
     */
    public MultiSiteDouble getSNR_A_R() {
        return (PowSig_A_R.divide(PowNor_A_R.subtract(PowSig_A_R).subtract(PowHarm_A_R))).log10().multiply(10);
    }

    /**
     * calculate waveform_L THD.
     * @return waveform_L THD.
     */
    public MultiSiteDouble getTHD_L() {
        return (PowHarm_L.divide(PowSig_L)).log10().multiply(10);
    }

    /**
     * calculate waveform_R THD.
     * @return waveform_R THD.
     */
    public MultiSiteDouble getTHD_R() {
        return (PowHarm_R.divide(PowSig_R)).log10().multiply(10);
    }

    /**
     * calculate waveform_L THD_A.
     * @return waveform_L THD_A.
     */
    public MultiSiteDouble getTHD_A_L() {
        return PowHarm_A_L.divide(PowSig_A_L).log10().multiply(10);
    }

    /**
     * calculate waveform_R THD_A.
     * @return waveform_R THD_A.
     */
    public MultiSiteDouble getTHD_A_R() {
        return PowHarm_A_R.divide(PowSig_A_R).log10().multiply(10);
    }

    /**
     * calculate waveform_L THDN.
     * @return waveform_L THDN.
     */
    public MultiSiteDouble getTHDN_L() {
        return PowNor_L.subtract(PowSig_L).divide(PowSig_L).log10().multiply(10);
    }

    /**
     * calculate waveform_R THDN.
     * @return waveform_R THDN.
     */
    public MultiSiteDouble getTHDN_R() {
        return PowNor_R.subtract(PowSig_R).divide(PowSig_R).log10().multiply(10);
    }

    /**
     * calculate waveform_L THDN_A.
     * @return waveform_L THDN_A.
     */
    public MultiSiteDouble getTHDN_A_L() {
        return PowNor_A_L.subtract(PowSig_A_L).divide(PowSig_A_L).log10().multiply(10);
    }

    /**
     * calculate waveform_R THDN_A.
     * @return waveform_R THDN_A.
     */
    public MultiSiteDouble getTHDN_A_R() {
        return PowNor_A_R.subtract(PowSig_A_R).divide(PowSig_A_R).log10().multiply(10);
    }

    /**
     * calculate 20KHz noise floor power in unit uVrms.
     * @return waveform_L 20KHz noise floor in unit uVrms.
     */
    public MultiSiteDouble getNoiseFloor_L_uVrms() {
        return PowNor_L.divide(20.0).sqrt().multiply(1e6);
    }

    /**
     * calculate 20KHz noise floor power in unit uVrms.
     * @return waveform_R 20KHz noise floor in unit uVrms.
     */
    public MultiSiteDouble getNoiseFloor_R_uVrms() {
        return PowNor_R.divide(20.0).sqrt().multiply(1e6);
    }

    /**
     * calculate 20KHz noise floor power in unit uVrms with Weighting A.
     * @return waveform_L 20KHz noise floor with Weighting A in unit uVrms.
     */
    public MultiSiteDouble getNoiseFloor_A_L_uVrms() {
        return PowNor_A_L.divide(20.0).sqrt().multiply(1e6);
    }

    /**
     * calculate 20KHz noise floor power in unit uVrms with Weighting A.
     * @return waveform_R 20KHz noise floor with Weighting A in unit uVrms.
     */
    public MultiSiteDouble getNoiseFloor_A_R_uVrms() {
        return PowNor_A_R.divide(20.0).sqrt().multiply(1e6);
    }

    /**
     * calculate the power in unit mW with Weighting A of specific bin range,usually used to calculate the noise floor.
     * @param startBin start bin index in spectrum
     * @param stopBin stop bin index in spectrum
     * @return Power of specific bin range in unit mW.
     */
    public MultiSiteDouble getSpecificBinRangePower_A_L_mW(MultiSiteLong startBin, MultiSiteLong stopBin) {
        MultiSiteDouble noiseFloor_A_L_mW=new MultiSiteDouble(0);

        for(int site:activeSites)
        {
            try
            {
                noiseFloor_A_L_mW.set(site, spec_A_L.get(site).sum(startBin.getAsInt(site), stopBin.subtract(startBin).getAsInt(site)));
            }
            catch (Exception e)
            {
             e.printStackTrace();
            }
        }

        return noiseFloor_A_L_mW;
    }

    /**
     * calculate the power in unit mW with Weighting A of specific bin range,usually used to calculate the noise floor.
     * @param startBin start bin index in spectrum
     * @param stopBin stop bin index in spectrum
     * @return Power of specific bin range in unit mW.
     */
    public MultiSiteDouble getSpecificBinRangePower_A_R_mW(MultiSiteLong startBin, MultiSiteLong stopBin) {
        MultiSiteDouble noiseFloor_A_R_mW=new MultiSiteDouble(0);

        for(int site:activeSites)
        {
            try
            {
                noiseFloor_A_R_mW.set(site, spec_A_R.get(site).sum(startBin.getAsInt(site), stopBin.subtract(startBin).getAsInt(site)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return noiseFloor_A_R_mW;
    }

    /**
     * calculate the power in unit dBm with Weighting A of specific bin range,usually used to calculate the noise floor.
     * @param startBin start bin index in spectrum
     * @param stopBin stop bin index in spectrum
     * @return Power of specific bin range in unit dBm.
     */
    public MultiSiteDouble getSpecificBinRangePower_A_L_dBm(MultiSiteLong startBin, MultiSiteLong stopBin) {
        MultiSiteDouble noiseFloor_A_L_dBm=new MultiSiteDouble(0);

        for(int site:activeSites)
        {
            try
            {
                noiseFloor_A_L_dBm.set(site, Math.log10(spec_A_L.get(site).sum(startBin.getAsInt(site), stopBin.subtract(startBin).getAsInt(site)))*10);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return noiseFloor_A_L_dBm;
    }

    /**
     * calculate the power in unit dBm with Weighting A of specific bin range,usually used to calculate the noise floor.
     * @param startBin start bin index in spectrum
     * @param stopBin stop bin index in spectrum
     * @return Power of specific bin range in unit dBm.
     */
    public MultiSiteDouble getSpecificBinRangePower_A_R_dBm(MultiSiteLong startBin, MultiSiteLong stopBin) {
        MultiSiteDouble noiseFloor_A_R_dBm=new MultiSiteDouble(0);

        for(int site:activeSites)
        {
            try
            {
                noiseFloor_A_R_dBm.set(site, Math.log10(spec_A_R.get(site).sum(startBin.getAsInt(site), stopBin.subtract(startBin).getAsInt(site)))*10);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return noiseFloor_A_R_dBm;
    }

    /**
     * calculate the power in unit uVrms with Weighting A of specific bin range,usually used to calculate the noise floor.
     * @param startBin start bin index in spectrum
     * @param stopBin stop bin index in spectrum
     * @return Power of specific bin range in unit uVrms.
     */
    public MultiSiteDouble getSpecificBinRangePower_A_L_uVrms(MultiSiteLong startBin, MultiSiteLong stopBin) {
        MultiSiteDouble noiseFloor_A_L_uVrms=new MultiSiteDouble(0);

        for(int site:activeSites)
        {
            try
            {
                noiseFloor_A_L_uVrms.set(site, Math.sqrt(spec_A_L.get(site).sum(startBin.getAsInt(site), stopBin.subtract(startBin).getAsInt(site))/20) *1e6);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return noiseFloor_A_L_uVrms;
    }

    /**
     * calculate the power in unit uVrms with Weighting A of specific bin range,usually used to calculate the noise floor.
     * @param startBin start bin index in spectrum
     * @param stopBin stop bin index in spectrum
     * @return Power of specific bin range in unit uVrms.
     */
    public MultiSiteDouble getSpecificBinRangePower_A_R_uVrms(MultiSiteLong startBin, MultiSiteLong stopBin) {
        MultiSiteDouble noiseFloor_A_R_uVrms=new MultiSiteDouble(0);
        try
        {
            for(int site:activeSites)
            {
                noiseFloor_A_R_uVrms.set(site, Math.sqrt(spec_A_R.get(site).sum(startBin.getAsInt(site), stopBin.subtract(startBin).getAsInt(site))/20) *1e6);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return noiseFloor_A_R_uVrms;
    }

    /**
     * calculate the power in unit mW of specific bin range,usually used to calculate the noise floor.
     * @param startBin start bin index in spectrum
     * @param stopBin stop bin index in spectrum
     * @return Power of specific bin range in unit mW.
     */
    public MultiSiteDouble getSpecificBinRangePower_L_mW(MultiSiteLong startBin, MultiSiteLong stopBin) {
        MultiSiteDouble noiseFloor_L_mW=new MultiSiteDouble(0);

        for(int site:activeSites)
        {
            try
            {
                noiseFloor_L_mW.set(site, spec_L.get(site).sum(startBin.getAsInt(site), stopBin.subtract(startBin).getAsInt(site)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return noiseFloor_L_mW;
    }

    /**
     * calculate the power in unit mW of specific bin range,usually used to calculate the noise floor.
     * @param startBin start bin index in spectrum
     * @param stopBin stop bin index in spectrum
     * @return Power of specific bin range in unit mW.
     */
    public MultiSiteDouble getSpecificBinRangePower_R_mW(MultiSiteLong startBin, MultiSiteLong stopBin) {
        MultiSiteDouble noiseFloor_R_mW=new MultiSiteDouble(0);

        for(int site:activeSites)
        {
            try
            {
                noiseFloor_R_mW.set(site, spec_R.get(site).sum(startBin.getAsInt(site), stopBin.subtract(startBin).getAsInt(site)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return noiseFloor_R_mW;
    }

    /**
     * calculate the power in unit dBm of specific bin range,usually used to calculate the noise floor.
     * @param startBin start bin index in spectrum
     * @param stopBin stop bin index in spectrum
     * @return Power of specific bin range in unit dBm.
     */
    public MultiSiteDouble getSpecificBinRangePower_L_dBm(MultiSiteLong startBin, MultiSiteLong stopBin) {
        MultiSiteDouble noiseFloor_L_dBm=new MultiSiteDouble(0);

        for(int site:activeSites)
        {
            try
            {
                noiseFloor_L_dBm.set(site, Math.log10(spec_L.get(site).sum(startBin.getAsInt(site), stopBin.subtract(startBin).getAsInt(site)))*10);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return noiseFloor_L_dBm;
    }

    /**
     * calculate the power in unit dBm of specific bin range,usually used to calculate the noise floor.
     * @param startBin start bin index in spectrum
     * @param stopBin stop bin index in spectrum
     * @return Power of specific bin range in unit dBm.
     */
    public MultiSiteDouble getSpecificBinRangePower_R_dBm(MultiSiteLong startBin, MultiSiteLong stopBin) {
        MultiSiteDouble noiseFloor_R_dBm=new MultiSiteDouble(0);

        for(int site:activeSites)
        {
            try
            {
                noiseFloor_R_dBm.set(site, Math.log10(spec_R.get(site).sum(startBin.getAsInt(site), stopBin.subtract(startBin).getAsInt(site)))*10);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return noiseFloor_R_dBm;
    }

    /**
     * calculate the power in unit uVrms of specific bin range,usually used to calculate the noise floor.
     * @param startBin start bin index in spectrum
     * @param stopBin stop bin index in spectrum
     * @return Power of specific bin range in unit uVrms.
     */
    public MultiSiteDouble getSpecificBinRangePower_L_uVrms(MultiSiteLong startBin, MultiSiteLong stopBin) {
        MultiSiteDouble noiseFloor_L_uVrms=new MultiSiteDouble(0);

        for(int site:activeSites)
        {
            try
            {
                noiseFloor_L_uVrms.set(site, Math.sqrt(spec_L.get(site).sum(startBin.getAsInt(site), stopBin.subtract(startBin).getAsInt(site))/20) *1e6);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return noiseFloor_L_uVrms;
    }

    /**
     * calculate the power in unit uVrms of specific bin range,usually used to calculate the noise floor.
     * @param startBin start bin index in spectrum
     * @param stopBin stop bin index in spectrum
     * @return Power of specific bin range in unit uVrms.
     */
    public MultiSiteDouble getSpecificBinRangePower_R_uVrms(MultiSiteLong startBin, MultiSiteLong stopBin) {
        MultiSiteDouble noiseFloor_R_uVrms=new MultiSiteDouble(0);
        try
        {
            for(int site:activeSites)
            {
                noiseFloor_R_uVrms.set(site, Math.sqrt(spec_R.get(site).sum(startBin.getAsInt(site), stopBin.subtract(startBin).getAsInt(site))/20) *1e6);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return noiseFloor_R_uVrms;
    }

}
