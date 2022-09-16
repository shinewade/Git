package besLib.cal;

import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.dsp.MultiSiteSpectrum;
import xoc.dta.datatypes.dsp.MultiSiteSpectrumComplex;
import xoc.dta.datatypes.dsp.MultiSiteWaveComplex;
import xoc.dta.datatypes.dsp.MultiSiteWaveDouble;
import xoc.dta.datatypes.dsp.SpectrumUnit;
import xoc.dta.datatypes.dsp.WindowFunction;
import xoc.dta.datatypes.dsp.WindowScaling;

/**
 * This class is used to calculate BES WIFI RX parameter
 * @version V1.1
 * @author Ronnie Li, Weng Yongxin
 **/
public class BesCalc_WIFIRX{

    private int[] activeSites;
    private MultiSiteWaveDouble capWaveI = new MultiSiteWaveDouble();
    private MultiSiteWaveDouble capWaveQ = new MultiSiteWaveDouble();
    private MultiSiteDouble   fs_I      = new MultiSiteDouble();//SampleRate
    private MultiSiteLong     n_I       = new MultiSiteLong();//samples number
    private MultiSiteDouble   bin_res_I = new MultiSiteDouble();//binResolution
    private MultiSiteSpectrum spectrum_I     = new MultiSiteSpectrum();
    private MultiSiteSpectrum spectrum_Q     = new MultiSiteSpectrum();
    private MultiSiteSpectrum spectrum_I_dBm = new MultiSiteSpectrum();
    private MultiSiteSpectrum spectrum_Q_dBm = new MultiSiteSpectrum();
    private MultiSiteSpectrum Spectrum_Real      = new MultiSiteSpectrum();
    private MultiSiteSpectrum Spectrum_Imaginary = new MultiSiteSpectrum();
    private MultiSiteSpectrum amplitude_IQ       = new MultiSiteSpectrum();
    private MultiSiteSpectrum spectrum_IQ        = new MultiSiteSpectrum();
    private MultiSiteWaveComplex capWaveComplexIQ =new MultiSiteWaveComplex();
    private MultiSiteSpectrumComplex spectrumComplex_IQ = new MultiSiteSpectrumComplex();
    private MultiSiteDouble   PowSig_I  = new MultiSiteDouble();
    private MultiSiteDouble   PowSig_Q  = new MultiSiteDouble();
    private MultiSiteDouble   PowSig_IQ = new MultiSiteDouble();
    private MultiSiteDouble   PowNor_I  = new MultiSiteDouble();
    private MultiSiteDouble   PowNor_Q  = new MultiSiteDouble();
    private MultiSiteDouble   PowNor_IQ = new MultiSiteDouble();
    private MultiSiteLong     imax_i    = new MultiSiteLong();
    private MultiSiteLong     imax_q    = new MultiSiteLong();
    private MultiSiteLong     imax_iq   = new MultiSiteLong();
    private double  noiseStartFrequency;
    private double  noiseStopFrequency;
    private int     signalBinCount;

    /**
     * Constructor
     * Do FFT and necessary process for parameter calculation in class function
     *
     * @param capWaveI raw data I captured by AGPIO WSMX HSDGT
     * @param capWaveQ raw data Q captured by AGPIO WSMX HSDGT
     * @param signalBinCount odd bin number to calculate spectrum power integration
     * @param noiseStartFrequency start frequency of noise floor
     * @param noiseStopFrequency stop frequency of noise floor
     */
    public BesCalc_WIFIRX(MultiSiteWaveDouble capWaveI, MultiSiteWaveDouble capWaveQ, int signalBinCount, double noiseStartFrequency, double noiseStopFrequency) {
        this.activeSites = this.capWaveI.getActiveSites();
        this.capWaveI = capWaveI;
        this.capWaveQ = capWaveQ;
        this.signalBinCount = signalBinCount;
        this.noiseStartFrequency = noiseStartFrequency;
        this.noiseStopFrequency   = noiseStopFrequency;
        this.fs_I = this.capWaveI.getSampleRate();
        this.n_I  = this.capWaveI.getSize();
        this.bin_res_I.set(this.fs_I.divide(this.n_I));

        this.spectrum_I = this.capWaveI.setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).spectrum(SpectrumUnit.mW);
        this.spectrum_Q = this.capWaveQ.setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).spectrum(SpectrumUnit.mW);
        this.spectrum_I_dBm = this.capWaveI.setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).spectrum(SpectrumUnit.dBm);
        this.spectrum_Q_dBm = this.capWaveQ.setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).spectrum(SpectrumUnit.dBm);

        //complex waveform
        capWaveComplexIQ =new MultiSiteWaveComplex(this.capWaveI,this.capWaveQ);
        this.capWaveComplexIQ.setSampleRate(this.fs_I);

        this.spectrumComplex_IQ=this.capWaveComplexIQ.setWindowFunction(WindowFunction.HANNING).setWindowScaling(WindowScaling.SCALE_FOR_AMPLITUDE).fft();  //DSP_FFT(cData1,spec_cData1,HANNING) SMT7
        this.Spectrum_Real=this.spectrumComplex_IQ.getReal().multiply(this.spectrumComplex_IQ.getReal());
        this.Spectrum_Imaginary=this.spectrumComplex_IQ.getImaginary().multiply(this.spectrumComplex_IQ.getImaginary());
        this.amplitude_IQ=(this.Spectrum_Real.add(this.Spectrum_Imaginary)).sqrt(); //DSP_RECT_POL(spec_cData1, amplitude, phase) SMT7
        this.spectrum_IQ=this.amplitude_IQ.multiply(this.amplitude_IQ);             //DSP_MUL_VEC(amplitude,amplitude,spec_iq) SMT7
        this.spectrum_IQ.set(this.spectrum_IQ.divide(50));                          //DSP_MUL_SCL(0.02,spec_iq,spec_iq) SMT7
        this.spectrum_IQ.set(this.spectrum_IQ.multiply(1e3));                       //W -> mW

        //remove DC component
        for(int i=0;i<3;i++){
            this.spectrum_I.setValue(i, new MultiSiteDouble(0));
            this.spectrum_Q.setValue(i, new MultiSiteDouble(0));
            this.spectrum_IQ.setValue(i, new MultiSiteDouble(0));
        }


        if(this.noiseStopFrequency<=this.noiseStartFrequency)
        {
            new Exception("error! noiseStopFrequency should be grater than noiseStartFrequency ").printStackTrace();
//            this.noiseStartFrequency=noiseStopFrequency;
//            this.noiseStopFrequency=noiseStartFrequency;
        }
        if(this.noiseStartFrequency<0 || this.noiseStopFrequency<0)
        {
            new Exception("error! noiseStartFrequency or noiseStopFrequency should be equal or grater than 0 Hz ").printStackTrace();
//            this.noiseStartFrequency = 0;
        }
        if((this.noiseStartFrequency/this.bin_res_I.get(activeSites[0])) > (this.n_I.get(activeSites[0])/2) || (this.noiseStopFrequency/this.bin_res_I.get(activeSites[0])) > (this.n_I.get(activeSites[0])/2) )
        {
            new Exception("error! noiseStartFrequency or noiseStopFrequency should be equal or less than nyquist frequency ").printStackTrace();
//            this.noiseStopFrequency=this.n_I.get(activeSites[0])/2;
        }

        this.imax_i = this.spectrum_I.extractValues(this.bin_res_I.reciprocal().multiply(this.noiseStartFrequency).toMultiSiteLong(), this.bin_res_I.reciprocal().multiply(this.noiseStopFrequency-this.noiseStartFrequency).toMultiSiteLong()).maxIndex().add(this.bin_res_I.reciprocal().multiply(this.noiseStartFrequency).toMultiSiteLong());
        this.imax_q = this.spectrum_Q.extractValues(this.bin_res_I.reciprocal().multiply(this.noiseStartFrequency).toMultiSiteLong(), this.bin_res_I.reciprocal().multiply(this.noiseStopFrequency-this.noiseStartFrequency).toMultiSiteLong()).maxIndex().add(this.bin_res_I.reciprocal().multiply(this.noiseStartFrequency).toMultiSiteLong());
        this.imax_iq = this.spectrum_IQ.extractValues(this.bin_res_I.reciprocal().multiply(this.noiseStartFrequency).toMultiSiteLong(), this.bin_res_I.reciprocal().multiply(this.noiseStopFrequency-this.noiseStartFrequency).toMultiSiteLong()).maxIndex().add(this.bin_res_I.reciprocal().multiply(this.noiseStartFrequency).toMultiSiteLong());

        for(int site:this.activeSites)
        {
            int iSignalIndex = 0;
            int qSignalIndex = 0;
            int iqSignalIndex = 0;
            iSignalIndex  = this.imax_i.getAsInt(site);
            qSignalIndex  = this.imax_q.getAsInt(site);
            iqSignalIndex = this.imax_iq.getAsInt(site);

                if(iSignalIndex>=this.spectrum_I.getSize(site)-this.signalBinCount || iSignalIndex<this.signalBinCount || qSignalIndex>=this.spectrum_Q.getSize(site)-this.signalBinCount || qSignalIndex<this.signalBinCount)
                {
                    this.PowSig_I.set(site, 999);
                    this.PowNor_I.set(site, 999);
                    this.PowSig_Q.set(site, 999);
                    this.PowNor_Q.set(site, 999);
                    this.PowSig_IQ.set(site,999);
                    this.PowNor_IQ.set(site, 999);
                }
                else
                {
                    //Power_I
                    for(int i = iSignalIndex-this.signalBinCount; i <= iSignalIndex+this.signalBinCount ; i ++) {
                        if(i > 0 && i < this.spectrum_I.getSize(site)) {
                            this.PowSig_I.set(site,this.PowSig_I.get(site) + this.spectrum_I.getValue(site,i));
                        }
                    }
                    //Power_Q
                    for(int i = qSignalIndex-this.signalBinCount; i <= qSignalIndex+this.signalBinCount ; i ++) {
                        if(i > 0 && i < this.spectrum_Q.getSize(site)) {
                            this.PowSig_Q.set(site,this.PowSig_Q.get(site) + this.spectrum_Q.getValue(site,i));
                        }
                    }
                    //Power_IQ
                    for(int i = iqSignalIndex-this.signalBinCount; i <= iqSignalIndex+this.signalBinCount ; i ++) {
                        if(i > 0 && i < this.spectrum_IQ.getSize(site)) {
                            this.PowSig_IQ.set(site,this.PowSig_IQ.get(site) + this.spectrum_IQ.getValue(site,i));
                        }
                    }
                    //Power_Noise_I
                    try
                    {
                        for(int i = (int)(this.noiseStartFrequency/this.bin_res_I.get(site)) ; i <=(int)(this.noiseStopFrequency/this.bin_res_I.get(site)) ; i ++) {
                            this.PowNor_I.set(site,this.PowNor_I.get(site)+this.spectrum_I.getValue(site,i));
                        }
                    }catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        System.out.println("error, please check noiseStartFrequency or noiseStopFrequency bin!");
                    }

                    //Power_Noise_Q
                    try
                    {
                        for(int j = (int)(this.noiseStartFrequency/this.bin_res_I.get(site)) ; j <=(int)(this.noiseStopFrequency/this.bin_res_I.get(site)) ; j ++) {
                            this.PowNor_Q.set(site,this.PowNor_Q.get(site)+this.spectrum_Q.getValue(site,j));
                        }
                    }catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        System.out.println("error, please check noiseStartFrequency or noiseStopFrequency bin!");
                    }
                    //Power_Noise_IQ
                    try
                    {
                        for(int i = (int)(this.noiseStartFrequency/this.bin_res_I.get(site)) ; i <=(int)(this.noiseStopFrequency/this.bin_res_I.get(site)) ; i ++) {
                            this.PowNor_IQ.set(site,this.PowNor_IQ.get(site)+this.spectrum_IQ.getValue(site,i));
                        }
                    }catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        System.out.println("error, please check noiseStartFrequency or noiseStopFrequency bin!");
                    }
                }
            }
    }

    /**
     * get complex waveform IQ.
     * @return MultiSiteWaveComplex waveform IQ.
     */
    public MultiSiteWaveComplex getComplexWaveform_IQ() {
        return this.capWaveComplexIQ;
    }

    /**
     * get spectrum I in dBm.
     * @return spectrum I in unit dBm.
     */
    public MultiSiteSpectrum getSpectrum_I_dBm() {
        return this.spectrum_I_dBm;
    }

    /**
     * get spectrum Q in dBm.
     * @return spectrum Q in unit dBm.
     */
    public MultiSiteSpectrum getSpectrum_Q_dBm() {
        return this.spectrum_Q_dBm;
    }

    /**
     * get complex spectrum IQ in dBm.
     * @return complex spectrum IQ in unit dBm.
     */
    public MultiSiteSpectrumComplex getComplexSpectrum_IQ_dBm() {
        return this.spectrumComplex_IQ;
    }

    /**
     * get spectrum I in mW.
     * @return spectrum I in unit mW.
     */
    public MultiSiteSpectrum getSpectrum_I_mW() {
        return this.spectrum_I;
    }

    /**
     * get spectrum Q in mW.
     * @return spectrum Q in unit mW.
     */
    public MultiSiteSpectrum getSpectrum_Q_mW() {
        return this.spectrum_Q;
    }

    /**
     * get complex spectrum IQ in mW.
     * @return complex spectrum IQ in unit mW.
     */
    public MultiSiteSpectrum getSpectrum_IQ_mW() {
        return this.spectrum_IQ;
    }

    /**
     * calculate signal I power.
     * @return RX I power in unit mW.
     */
    public MultiSiteDouble getRXPower_I_mW() {
        return this.PowSig_I;
    }

    /**
     * calculate signal Q power.
     * @return RX Q power in unit mW.
     */
    public MultiSiteDouble getRXPower_Q_mW() {
        return this.PowSig_Q;
    }

    /**
     * calculate signal IQ power.
     * @return RX IQ power in unit mW.
     */
    public MultiSiteDouble getRXPower_IQ_mW() {
        return this.PowSig_IQ;
    }

    /**
     * calculate signal I power.
     * @return RX I power in unit dBm.
     */
    public MultiSiteDouble getRXPower_I_dBm() {
        return this.PowSig_I.log10().multiply(10);
    }

    /**
     * calculate signal Q power.
     * @return RX Q power in unit dBm.
     */
    public MultiSiteDouble getRXPower_Q_dBm() {
        return this.PowSig_Q.log10().multiply(10);
    }

    /**
     * calculate signal IQ power.
     * @return RX IQ power in unit dBm.
     */
    public MultiSiteDouble getRXPower_IQ_dBm() {
        return this.PowSig_IQ.log10().multiply(10);
    }

    /**
     * calculate signal IQ_IMB.
     * @return RX IQ imbalances.
     */
    public MultiSiteDouble getRXPower_IQ_IMB() {
        return (this.PowSig_I.divide(this.PowSig_Q)).log10().multiply(10);
    }

    /**
     * calculate signal I SNR.
     * @return RX I SNR.
     */
    public MultiSiteDouble getSNR_I() {
        return (this.PowSig_I.divide(this.PowNor_I.subtract(this.PowSig_I)).log10().multiply(10));
    }

    /**
     * calculate signal Q SNR.
     * @return RX Q SNR.
     */
    public MultiSiteDouble getSNR_Q() {
        return (this.PowSig_Q.divide(this.PowNor_Q.subtract(this.PowSig_Q)).log10().multiply(10));
    }

    /**
     * calculate signal IQ SNR.
     * @return RX IQ SNR.
     */
    public MultiSiteDouble getSNR_IQ() {
        return (this.PowSig_IQ.divide(this.PowNor_IQ.subtract(this.PowSig_IQ)).log10().multiply(10));
    }

    /**
     * calculate signal I frequency.
     * @return RX signal I frequency in unit KHz.
     */
    public MultiSiteDouble getFrequency_I() {
        return this.imax_i.multiply(this.bin_res_I);
    }

    /**
     * calculate signal Q frequency.
     * @return RX signal Q frequency in unit KHz.
     */
    public MultiSiteDouble getFrequency_Q() {
        return this.imax_q.multiply(this.bin_res_I);
    }

}
