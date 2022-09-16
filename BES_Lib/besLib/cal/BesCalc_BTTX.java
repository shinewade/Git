package besLib.cal;

import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.dsp.MultiSiteSpectrum;
import xoc.dta.datatypes.dsp.MultiSiteWaveComplex;
import xoc.dta.datatypes.dsp.SpectrumUnit;

/**
 * This class is used to calculate BES BT TX parameter
 * @version V1.1
 * @author Weng Yongxin, Ronnie Li
 **/
public class BesCalc_BTTX  {

    private double binResolution;
    private int signalBinCount;
    private int[] activeSites;
    private MultiSiteWaveComplex btWaveComplex = new MultiSiteWaveComplex();
    private MultiSiteSpectrum tx_spectrum_dBm=new MultiSiteSpectrum();
    private MultiSiteSpectrum tx_spectrum_mW=new MultiSiteSpectrum();
    private MultiSiteLong rfBinIndex=new MultiSiteLong();
    private MultiSiteLong loBinIndex=new MultiSiteLong();
    private MultiSiteLong imagBinIndex=new MultiSiteLong();
    private MultiSiteDouble powerRf=new MultiSiteDouble();
    private MultiSiteDouble powerLo=new MultiSiteDouble();
    private MultiSiteDouble powerImag=new MultiSiteDouble();
    private MultiSiteDouble att=new MultiSiteDouble(0);
    private boolean isLoRight = true;



    /**
     * Constructor
     * Do FFT and necessary process for parameter calculation in class function
     *
     * Attention!!!
     * Constructor time consumption abnormal, using releaseTester() function to hidden upload if the result not use in next test suite !!!
     *
     * @param btWaveComplex raw data captured by WSRF digitizer
     * @param signalBinCount odd bin number to calculate spectrum power integration
     * @param isLoRight LO is in the right of RF Signal or not
     */
    public BesCalc_BTTX(MultiSiteWaveComplex btWaveComplex, int signalBinCount,boolean isLoRight) {
        this.isLoRight = isLoRight;
        this.btWaveComplex.set(btWaveComplex);
        this.signalBinCount=signalBinCount;
        this.tx_spectrum_mW=this.btWaveComplex.spectrum(SpectrumUnit.mW);
        this.tx_spectrum_dBm=this.btWaveComplex.spectrum(SpectrumUnit.dBm);
        this.activeSites=this.btWaveComplex.getActiveSites();
        this.binResolution=(this.btWaveComplex.getSampleRate().get(this.activeSites[0]))/(this.btWaveComplex.getSize(this.activeSites[0]))/1000;//KHz
        this.rfBinIndex=this.tx_spectrum_mW.maxIndex();
        //calculate RF,LO,IMAG
        for(int site:activeSites)
        {
            try
            {
                if(this.isLoRight == true){
                    //LO spectrum BW: Frf + 120 KHz ~ Frf +200 KHz
                    this.loBinIndex  .set(site, this.rfBinIndex.getAsInt(site) + (int)(120/this.binResolution) + this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site)+(int)(120/this.binResolution),(int)(80/this.binResolution)).maxIndex());
                    //Imagine spectrum BW: Frf + 280 KHz ~ Frf + 360 KHz
                    this.imagBinIndex.set(site, this.rfBinIndex.getAsInt(site) + (int)(280/this.binResolution) + this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site)+(int)(280/this.binResolution),(int)(80/this.binResolution)).maxIndex());
                }
                else{
                    //LO spectrum BW: Frf - 200 KHz ~ Frf - 120 KHz
                    this.loBinIndex  .set(site, this.rfBinIndex.getAsInt(site) - (int)(200/this.binResolution) + this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site)-(int)(200/this.binResolution),(int)(80/this.binResolution)).maxIndex());
                    //Imagine spectrum BW: Frf - 360 KHz ~ Frf - 280 KHz
                    this.imagBinIndex.set(site, this.rfBinIndex.getAsInt(site) - (int)(360/this.binResolution) + this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site)-(int)(360/this.binResolution),(int)(80/this.binResolution)).maxIndex());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("site "+site+" Spectrum abnormal, please check it!");
            }

            try
            {
                this.powerRf  .set(site, this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex  .getAsInt(site)-(this.signalBinCount-1)/2 ,this.signalBinCount).sum());
                this.powerLo  .set(site, this.tx_spectrum_mW.get(site).extractValues(this.loBinIndex  .getAsInt(site)-(this.signalBinCount-1)/2 ,this.signalBinCount).sum());
                this.powerImag.set(site, this.tx_spectrum_mW.get(site).extractValues(this.imagBinIndex.getAsInt(site)-(this.signalBinCount-1)/2 ,this.signalBinCount).sum());
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("site "+site+"getRfPowerl/getLoPowerl/getImagPowerl error, please check Spectrum or extract bin!");
            }
        }
    }





    /**
     * Constructor, default LO is in the right of RF signal
     * Do FFT and necessary process for parameter calculation in class function
     *
     * @param btWaveComplex raw data captured by WSRF digitizer
     * @param signalBinCount bin number to calculate the integral of spectrum power
     */
    public BesCalc_BTTX(MultiSiteWaveComplex btWaveComplex, int signalBinCount) {
        this.btWaveComplex.set(btWaveComplex);
        this.signalBinCount=signalBinCount;
        this.tx_spectrum_mW=this.btWaveComplex.spectrum(SpectrumUnit.mW);
        this.tx_spectrum_dBm=this.btWaveComplex.spectrum(SpectrumUnit.dBm);
        this.activeSites=this.btWaveComplex.getActiveSites();
        this.binResolution=(this.btWaveComplex.getSampleRate().get(this.activeSites[0]))/(this.btWaveComplex.getSize(this.activeSites[0]))/1000;//KHz
        this.rfBinIndex=this.tx_spectrum_mW.maxIndex();
        //calculate RF,LO,IMAG
        for(int site:activeSites)
        {
            try
            {
                if(this.isLoRight == true){
                    //LO spectrum BW: Frf + 120 KHz ~ Frf +200 KHz
                    this.loBinIndex  .set(site, this.rfBinIndex.getAsInt(site) + (int)(120/this.binResolution) + this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site)+(int)(120/this.binResolution),(int)(80/this.binResolution)).maxIndex());
                    //Imagine spectrum BW: Frf + 280 KHz ~ Frf + 360 KHz
                    this.imagBinIndex.set(site, this.rfBinIndex.getAsInt(site) + (int)(280/this.binResolution) + this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site)+(int)(280/this.binResolution),(int)(80/this.binResolution)).maxIndex());
                }
                else{
                    //LO spectrum BW: Frf - 200 KHz ~ Frf - 120 KHz
                    this.loBinIndex  .set(site, this.rfBinIndex.getAsInt(site) - (int)(200/this.binResolution) + this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site)-(int)(200/this.binResolution),(int)(80/this.binResolution)).maxIndex());
                    //Imagine spectrum BW: Frf - 360 KHz ~ Frf - 280 KHz
                    this.imagBinIndex.set(site, this.rfBinIndex.getAsInt(site) - (int)(360/this.binResolution) + this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site)-(int)(360/this.binResolution),(int)(80/this.binResolution)).maxIndex());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("site "+site+" Spectrum abnormal, please check it!");
            }

            try
            {
                this.powerRf  .set(site, this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex  .getAsInt(site)-(this.signalBinCount-1)/2 ,this.signalBinCount).sum());
                this.powerLo  .set(site, this.tx_spectrum_mW.get(site).extractValues(this.loBinIndex  .getAsInt(site)-(this.signalBinCount-1)/2 ,this.signalBinCount).sum());
                this.powerImag.set(site, this.tx_spectrum_mW.get(site).extractValues(this.imagBinIndex.getAsInt(site)-(this.signalBinCount-1)/2 ,this.signalBinCount).sum());
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("site "+site+"getRfPowerl/getLoPowerl/getImagPowerl error, please check Spectrum or extract bin!");
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
     * calculate LO bin index.
     * @return LO bin index.
     */
    public MultiSiteLong getLoBinIndex() {
        return this.loBinIndex;
    }

    /**
     * calculate IMAG bin index.
     * @return IMAG bin index.
     */
    public MultiSiteLong getImagBinIndex() {
        return this.imagBinIndex;
    }

    /**
     * calculate RF signal power.
     * @return RF signal power in unit dBm.
     */
    public MultiSiteDouble getRfPower_dBm() {
        return this.powerRf.log10().multiply(10).add(this.att);
    }

    /**
     * calculate LO signal power.
     * @return LO signal power in unit dBm.
     */
    public MultiSiteDouble getLoPower_dBm() {
        return this.powerLo.log10().multiply(10).add(this.att);
    }

    /**
     * calculate Imagine signal power.
     * @return Imagine signal power in unit dBm.
     */
    public MultiSiteDouble getImagPower_dBm() {
        return this.powerImag.log10().multiply(10).add(this.att);
    }

    /**
     * calculate RF to LO ratio.
     * @return RF to LO ratio in unit dB.
     */
    public MultiSiteDouble getLoLeakage() {
        return (this.powerRf.divide(this.powerLo)).log10().multiply(10);
    }

    /**
     * calculate RF to Imagine ratio.
     * @return RF to Imagine ratio in unit dB.
     */
    public MultiSiteDouble getImageRejection() {
        return  (this.powerRf.divide(this.powerImag)).log10().multiply(10);
    }

    /**
     * calculate RF signal frequency.
     * @param freq_RFChannel_MHz Current RF channel frequency in MHz.
     * @return RF signal frequency in unit Hz.
     */
    public MultiSiteDouble getRfFrequency(double freq_RFChannel_MHz ) {
        MultiSiteDouble freqRf=new MultiSiteDouble(0);
        for(int site:activeSites)
        {
            try
            {
                freqRf.set(site,freq_RFChannel_MHz-(this.tx_spectrum_mW.getSize(site)/2-this.rfBinIndex.getAsInt(site))*this.binResolution/1e3);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return freqRf.multiply(1e6);
    }

    /**
     * calculate LO signal frequency.
     * @param freq_RFChannel_MHz Current RF channel frequency in MHz.
     * @return LO signal frequency in unit Hz.
     */
    public MultiSiteDouble getLoFrequency(double freq_RFChannel_MHz) {
        MultiSiteDouble freqLo=new MultiSiteDouble(0);
        for(int site:activeSites)
        {
            try
            {
                freqLo.set(site,freq_RFChannel_MHz-(this.tx_spectrum_mW.getSize(site)/2-this.loBinIndex.getAsInt(site))*this.binResolution/1e3);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return freqLo.multiply(1e6);
    }

    /**
     * calculate Imagine signal frequency.
     * @param freq_RFChannel_MHz Current RF channel frequency in MHz.
     * @return Imagine signal frequency in unit Hz.
     */
    public MultiSiteDouble getImagFrequency(double freq_RFChannel_MHz) {
        MultiSiteDouble freqImag=new MultiSiteDouble(0);
        for(int site:activeSites)
        {
            try
            {
                freqImag.set(site,freq_RFChannel_MHz-(this.tx_spectrum_mW.getSize(site)/2-this.imagBinIndex.getAsInt(site))*this.binResolution/1e3);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return freqImag.multiply(1e6);
    }

     /**
      * calculate SNR of RF Signal with specified bandwidth.
      * @param half_BandWidth Specified frequency bandwidth in half which mid-position is RF signal frequency
      * @return SNR in unit dB.
      */
      public MultiSiteDouble getRfSNR(double half_BandWidth) {
          MultiSiteDouble powerBW=new MultiSiteDouble();
          for(int site:activeSites) {
              try {
                  int half_BandWidthBin= (int) (half_BandWidth/this.binResolution);
                  powerBW.set(site, this.tx_spectrum_mW.get(site).extractValues( this.rfBinIndex.getAsInt(site)-half_BandWidthBin , half_BandWidthBin*2+1).sum());
              }
              catch (Exception e) {
                  e.printStackTrace();
                  System.out.println("site "+site+" Spectrum abnormal, please check it!");
              }
          }
          MultiSiteDouble powerNoise=new MultiSiteDouble(0);
          powerNoise.set(powerBW.subtract(this.powerRf));
          return  (this.powerRf.divide(powerNoise)).log10().multiply(10);
     }

      /**
       * calculate the max tone power with user specified frequency band in unit dBm.
       * @param freq_kHz   Target frequency with reference to RF signal in spectrum, frequency unit is in KHz.
       * @param freqBand_KHz Frequency band which mid-position is signal target frequency
       * @param isRightOfRFSignal Target frequency tone is in the right of RF Signal or not.
       * @return The max tone power in unit dBm near the specified frequency (kHz).
       */
      public MultiSiteDouble getSpecificFreqRangeMaxTonePower_dBm(double freq_kHz ,double freqBand_KHz, boolean isRightOfRFSignal) {
          MultiSiteDouble SpecificFreqPower=new MultiSiteDouble(0);
          MultiSiteLong maxIndex = new MultiSiteLong(0);
          for(int site:activeSites)
          {
              try
              {
                  //spectrum BW: freq_kHz - freqBand_KHz/2 KHz ~ freq_kHz + freqBand_KHz/2 KHz
                  if(isRightOfRFSignal)
                  {
                      maxIndex.set(site, this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site)+(int)((freq_kHz-freqBand_KHz/2)/this.binResolution) , (int)(freqBand_KHz/this.binResolution)).maxIndex());
                      maxIndex.set(site, this.rfBinIndex.getAsInt(site)+(int)((freq_kHz-freqBand_KHz/2)/this.binResolution)+maxIndex.getAsInt(site));
                  }
                  else
                  {
                      maxIndex.set(site, this.tx_spectrum_mW.get(site).extractValues(this.rfBinIndex.getAsInt(site)-(int)((freq_kHz+freqBand_KHz/2)/this.binResolution) , (int)(freqBand_KHz/this.binResolution)).maxIndex());
                      maxIndex.set(site, this.rfBinIndex.getAsInt(site)-(int)((freq_kHz+freqBand_KHz/2)/this.binResolution)+maxIndex.getAsInt(site));
                  }
                  try
                  {
                      SpecificFreqPower.set(site, this.tx_spectrum_mW.get(site).extractValues(maxIndex.getAsInt(site)-(this.signalBinCount-1)/2 ,this.signalBinCount).sum());
                  }
                  catch (Exception e)
                  {
                      e.printStackTrace();
                  }
              }
              catch (Exception e)
              {
                  e.printStackTrace();
              }
          }
          return SpecificFreqPower.log10().multiply(10).add(this.att);
      }

      /**
       * calculate the power of max tone in specified bin range in unit mW excludes ATT.
       * @param startBin start bin index in spectrum
       * @param stopBin stop bin index in spectrum
       * @return The max tone power in specified bin range in unit mW .
       */
      public MultiSiteDouble getSpecificBinRangeMaxTonePower_mW(MultiSiteLong startBin, MultiSiteLong stopBin) {
          MultiSiteDouble maxTonePower=new MultiSiteDouble(0);
          MultiSiteLong maxIndex = new MultiSiteLong(0);
          for(int site:activeSites)
          {
              try
              {
                  maxIndex.set(site, this.tx_spectrum_mW.get(site).extractValues(startBin.getAsInt(site) ,stopBin.getAsInt(site)-startBin.getAsInt(site)).maxIndex());
                  maxTonePower.set(site, this.tx_spectrum_mW.get(site).extractValues(maxIndex.getAsInt(site)-(this.signalBinCount-1)/2 ,this.signalBinCount).sum());
              }
              catch (Exception e)
              {
                  e.printStackTrace();
              }
          }
          return maxTonePower;
      }

      /**
       * calculate specific bin power in unit mW excludes ATT.
       * @param startBin start bin index in spectrum
       * @param stopBin stop bin index in spectrum
       * @return specific bin power in unit mW .
       */
      public MultiSiteDouble getSpecificBinPower_mW(MultiSiteLong startBin, MultiSiteLong stopBin) {
          MultiSiteDouble SpecificBinPower=new MultiSiteDouble(0);
          for(int site:activeSites)
          {
              try
              {
                  SpecificBinPower.set(site, this.tx_spectrum_mW.get(site).extractValues(startBin.getAsInt(site) ,stopBin.getAsInt(site)-startBin.getAsInt(site)).sum());
              }
              catch (Exception e)
              {
                  e.printStackTrace();
              }
          }
          return SpecificBinPower;
      }


}
