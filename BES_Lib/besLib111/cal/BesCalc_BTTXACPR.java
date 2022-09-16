package besLib.cal;

import xoc.dta.datatypes.MultiSiteBoolean;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.dsp.MultiSiteSpectrum;
import xoc.dta.datatypes.dsp.MultiSiteWaveComplex;
import xoc.dta.datatypes.dsp.SpectrumUnit;

/**
 * This class is used to calculate BES BT TX ACPR and 20dB bandWidth parameter
 * @version V1.1
 * @author Sun Wenju, Weng Yongxin
 **/
public class BesCalc_BTTXACPR  {

    private  double binResolution;
    private int binSize_1MHz;
    private int[] activeSites;
    private MultiSiteWaveComplex btWaveComplex = new MultiSiteWaveComplex();
    private MultiSiteSpectrum tx_spectrum_dBm=new MultiSiteSpectrum();
    private MultiSiteSpectrum tx_spectrum_mW=new MultiSiteSpectrum();
    private MultiSiteLong rfBinIndex=new MultiSiteLong();
    private MultiSiteDouble powerBandwidth_20dB=new MultiSiteDouble();
    private MultiSiteDouble powerRfChannel=new MultiSiteDouble();
    private MultiSiteDouble att=new MultiSiteDouble(0);
    /**
     * Constructor
     * Do FFT and necessary process for parameter calculation in class function
     *
     * @param btWaveComplex raw data captured by WSRF digitizer
     */
    public BesCalc_BTTXACPR(MultiSiteWaveComplex btWaveComplex) {
        this.btWaveComplex.set(btWaveComplex);
        this.tx_spectrum_mW=this.btWaveComplex.spectrum(SpectrumUnit.mW);
        this.tx_spectrum_dBm=this.btWaveComplex.spectrum(SpectrumUnit.dBm);
        this.activeSites=this.btWaveComplex.getActiveSites();
        this.binResolution=(this.btWaveComplex.getSampleRate().get(this.activeSites[0]))/(this.btWaveComplex.getSize(this.activeSites[0]))/1000;//KHz
        this.rfBinIndex=this.tx_spectrum_mW.maxIndex();
        //specify RF bin index for abnormal spectrum
        for(int site:activeSites) {
            if(this.rfBinIndex.get(site)<1600 || this.rfBinIndex.get(site)>8000 ) {
                this.rfBinIndex.set(site, 1600);
            }
        }

        //calculate ACPR
        MultiSiteLong upBin = new MultiSiteLong();
        MultiSiteLong lowBin = new MultiSiteLong();
        MultiSiteBoolean upBin_Stop = new MultiSiteBoolean(false);
        MultiSiteBoolean lowBin_Stop = new MultiSiteBoolean(false);
        for(int site: activeSites){
            try {
                /*  search the up limit */
                for(int i = this.tx_spectrum_dBm.getSize(activeSites[0])-10;i>0;i--){
                    if(this.tx_spectrum_dBm.getValue(site,i)-tx_spectrum_dBm.getValue(this.rfBinIndex).get(site)>-20 && this.tx_spectrum_dBm.getValue(site,i-1)-tx_spectrum_dBm.getValue(this.rfBinIndex).get(site)>-20 && this.tx_spectrum_dBm.getValue(site,i-2)-tx_spectrum_dBm.getValue(this.rfBinIndex).get(site)>-20){
                        if(upBin_Stop.get(site).equals(false)){
                            upBin.set(site,i);
                            upBin_Stop.set(site,true);
                        }
                    }
                }
                /*  search the low limit */
                for(int i=0;i<this.tx_spectrum_dBm.getSize(activeSites[0]);i++){
                    if(this.tx_spectrum_dBm.getValue(site,i)-tx_spectrum_dBm.getValue(this.rfBinIndex).get(site)>-20 && this.tx_spectrum_dBm.getValue(site,i+1)-tx_spectrum_dBm.getValue(this.rfBinIndex).get(site)>-20 && this.tx_spectrum_dBm.getValue(site,i+2)-tx_spectrum_dBm.getValue(this.rfBinIndex).get(site)>-20){
                        if(lowBin_Stop.get(site).equals(false)) {
                            lowBin.set(site,i);
                            lowBin_Stop.set(site,true);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        this.powerBandwidth_20dB = (upBin.subtract(lowBin)).multiply(this.binResolution);
        this.binSize_1MHz= (int)((1000.0/this.binResolution)+0.5);
        for(int site: activeSites){
            try {
                this.powerRfChannel.set(site,this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site) -( (int)(500.0/this.binResolution+0.5) ), this.binSize_1MHz).sum());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * set value of attenuation in dBm.
     * @param att ATT value on LoadBoard
     */
    public void setAtt(MultiSiteDouble att) {
        this.att.set(att);
    }

    /**
     * get spectrum in dBm.
     * @return spectrum in unit dBm.
     */
    public MultiSiteSpectrum getSpectrum_dBm() {
        return this.tx_spectrum_dBm;
    }

    /**
     * get spectrum getBinResolution.
     * @return Bin Resolution in unit KHz.
     */
    public double getBinResolution() {
        return this.binResolution;
    }

    /**
     * calculate RF signal bin index.
     * @return RF signal bin index.
     */
    public MultiSiteLong getRfBinIndex() {
        return this.rfBinIndex;
    }

    /**
     * calculate RF 20 dB bandwidth.
     * @return RF 20 dB bandwidth in unit Hz.
     */
     public MultiSiteDouble getRfBandwidth_20dB() {
         return this.powerBandwidth_20dB.multiply(1e3);
     }

    /**
     * calculate RF channel power between 1 MHz bandwidth.
     * @return RF channel power between 1 MHz bandwidth in unit dBm.
     */
    public MultiSiteDouble getRfChannelPower_dBm() {
        return this.powerRfChannel.log10().multiply(10).add(this.att);
    }

    /**
     * calculate ACPR P2 ratio.
     * @return ACPR P2 ratio in unit dB.
     */
    public MultiSiteDouble getACPR_P2() {
        MultiSiteDouble powerP2=new MultiSiteDouble();
        for(int site:activeSites) {
            try {
                powerP2.set(site,this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site) + ( (int)(1500.0/this.binResolution+0.5)), this.binSize_1MHz).sum());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (powerP2.divide(this.powerRfChannel)).log10().multiply(10);
    }

    /**
     * calculate ACPR P3 ratio.
     * @return ACPR P3 ratio in unit dB.
     */
    public MultiSiteDouble getACPR_P3() {
        MultiSiteDouble powerP3=new MultiSiteDouble();
        for(int site:activeSites) {
            try {
                powerP3.set(site,this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site) + ( (int)(2500.0/this.binResolution+0.5)), this.binSize_1MHz).sum());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (powerP3.divide(this.powerRfChannel)).log10().multiply(10);
    }

    /**
     * calculate ACPR M2 ratio.
     * @return ACPR M2 ratio in unit dB.
     */
    public MultiSiteDouble getACPR_M2() {
        MultiSiteDouble powerM2=new MultiSiteDouble();
        for(int site:activeSites) {
            try {
                powerM2.set(site, this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site) - ( (int)(2500.0/this.binResolution+0.5)), this.binSize_1MHz).sum());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (powerM2.divide(this.powerRfChannel)).log10().multiply(10);
    }

    /**
     * calculate ACPR M3 ratio.
     * @return ACPR M3 ratio in unit dB.
     */
    public MultiSiteDouble getACPR_M3() {
        MultiSiteDouble powerM3=new MultiSiteDouble();
        for(int site:activeSites) {
            try {
                powerM3.set(site, this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site) - ( (int)(3500.0/this.binResolution+0.5)),this.binSize_1MHz).sum());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (powerM3.divide(this.powerRfChannel)).log10().multiply(10);
    }



}
