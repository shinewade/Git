package besLib.cal;

import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.dsp.Line;
import xoc.dta.datatypes.dsp.MultiSiteSpectrum;
import xoc.dta.datatypes.dsp.MultiSiteWaveDouble;
import xoc.dta.datatypes.dsp.MultiSiteWaveLong;
import xoc.dta.datatypes.dsp.SpectrumUnit;
import xoc.dta.datatypes.dsp.WindowFunction;

/**
 * This class is used to calculate PLL frequency
 * @version V1.0
 * @author Weng Yongxin, Ronnie Li
 **/
public class BesCalc_PLL {
    /**
     * Enum type for property of frequency calibration model.
     */
    public static enum freqCalMode{
        FFT,
        LinearFit,
        EdgeCount,
    }
    /**
     * calculate frequency with WSMX DGT captured waveform in MultiSiteWaveDouble data type
     * @param rawData vector data captured per bit in MultiSiteWaveLong data type
     * @param freqCalMode freqCalMode
     *        <br>The options are: <i>FFT,LinearFit,EdgeCount</i>...  <br>
     * @return frequency value in MultiSiteDouble data type in MHz unit
     */
    public MultiSiteDouble freqCal_DGT(MultiSiteWaveDouble rawData, freqCalMode freqCalMode)
    {
        int[] activeSites = rawData.getActiveSites();
        MultiSiteDouble frequency_MHz=new MultiSiteDouble();
        double period_us=0;
        period_us=rawData.getSampleRate().divide(1e6).reciprocal().get(activeSites[0]);

        if(freqCalMode.toString().compareTo("FFT")==0) {
            MultiSiteSpectrum spec_raw=rawData.setWindowFunction(WindowFunction.HANNING).spectrum(SpectrumUnit.V);
            MultiSiteDouble bin_res=rawData.getSampleRate().divide(spec_raw.getSize());//unit Hz

            MultiSiteSpectrum spec = spec_raw.extractValues(new MultiSiteLong(0), new MultiSiteLong(200).divide(bin_res).toMultiSiteLong());

            for(int site:activeSites)
            {
                //remove DC component
                if (spec.getSize(site)>2) {
                    spec.setValue(site, 0, 0);
                    spec.setValue(site, 1, 0);
                }
                else {
                   System.out.println("/************************************************/");
                   System.out.println("Warning: Site "+site+" Spectrum data is too small!");
                   System.out.println("/************************************************/");
                }
                int max_index=spec.get(site).maxIndex();
                int max_index_plus_one=max_index+1;
                max_index_plus_one=(max_index_plus_one<spec.getSize(site))?max_index_plus_one:max_index_plus_one-1;
                //interpolating equations
                double dR,interpol_index;
                if( (max_index>0) && (spec.getValue(site, max_index-1)>spec.getValue(site, max_index_plus_one)) ){
                    dR=spec.getValue(site, max_index-1)/spec.getValue(site, max_index);
                    interpol_index=max_index+(1.0-2.0*dR)/(1.0+dR);
                }
                else {
                    dR=spec.getValue(site, max_index_plus_one)/spec.getValue(site, max_index);
                    interpol_index=max_index-(1.0-2.0*dR)/(1.0+dR);
                }
                double freq_MHz_site=interpol_index/(period_us*rawData.getSize(site));
                frequency_MHz.set(site, freq_MHz_site);
            }
        }
        else if(freqCalMode.toString().compareTo("LinearFit")==0) {
            MultiSiteWaveLong rising_edge=new MultiSiteWaveLong((rawData.getSize(activeSites[0])));
            int i,j;
            for(int site:activeSites) {
                for(i=0,j=0;i<rawData.getSize(site)-1;i++) {
                    if(rawData.getValue(site, i)<rawData.getValue(site, i+1)) {
                        rising_edge.setValue(site, j++, i);
                    }
                }
                Line result1=new Line();
                try {
                    result1=rising_edge.get(site).extractValues(0, j).regressionLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                double freq_MHz_site=1/(period_us*result1.getSlope());
                frequency_MHz.set(site, freq_MHz_site);
            }
        }
        else if(freqCalMode.toString().compareTo("EdgeCount")==0) {
            int array_size=rawData.getSize(activeSites[0]);
            for(int site:activeSites) {
                int i=0,j=0;
                int first_edge=0,last_edge=1;
                int low=0;
                int cycle_count=0;
                if(rawData.getValue(site, 0)==0) { //first edge is rising edge
                    for(j=1;j<array_size;j++) {
                        if(rawData.getValue(site, j)>0) {
                            first_edge=j;
                            low=0;
                            break;
                        }
                    }
                    for(i=++j;i<array_size;i++) {
                        if(rawData.getValue(site, i)==0) {
                            low=1;
                        }
                        else {
                            if(low!=0) {
                                cycle_count++;
                                last_edge=i;
                            }
                            low=0;
                        }
                    }
                }
                else { //first edge is falling edge
                    for(j=1;j<array_size;j++) {
                        if(rawData.getValue(site, j)==0) {
                            first_edge=j;
                            low=0;
                            break;
                        }
                    }
                    for(i=++j;i<array_size;i++) {
                        if(rawData.getValue(site, i)>0) {
                            low=1;
                        }
                        else {
                            if(low!=0) {
                                cycle_count++;
                                last_edge=i;
                            }
                            low=0;
                        }
                    }
                }
                double freq_calc=0;
                if(cycle_count==0) {
                    freq_calc=-1.0;
                }
                else {
                    freq_calc=cycle_count/((last_edge-first_edge)*period_us);
                }
                frequency_MHz.set(site, freq_calc);
            }
        }
        return frequency_MHz;
    }


      /**
       * calculate frequency with digital captured data in MultiSiteWaveLong data type
       * @param rawData vector data captured per bit in MultiSiteWaveLong data type
       * @param period_us sample period in us unit
       * @param freqCalMode freqCalMode
       *        <br>The options are: <i>FFT,LinearFit,EdgeCount</i>...  <br>
       * @return frequency value in MultiSiteDouble data type in MHz unit
       */
      public MultiSiteDouble freqCal(MultiSiteWaveLong rawData, double period_us, freqCalMode freqCalMode)
      {
          int[] activeSites = rawData.getActiveSites();
          MultiSiteDouble frequency_MHz=new MultiSiteDouble();

          if(freqCalMode.toString().compareTo("FFT")==0) {
              MultiSiteSpectrum spec=rawData.setWindowFunction(WindowFunction.HANNING).spectrum(SpectrumUnit.V);
              for(int site:activeSites)
              {
                  //remove DC component
                  if (spec.getSize(site)>2) {
                      spec.setValue(site, 0, 0);
                      spec.setValue(site, 1, 0);
                  }
                  else {
                     System.out.println("/************************************************/");
                     System.out.println("Warning: Site "+site+" Spectrum data is too small!");
                     System.out.println("/************************************************/");
                  }
                  int max_index=spec.get(site).maxIndex();
                  int max_index_plus_one=max_index+1;
                  max_index_plus_one=(max_index_plus_one<spec.getSize(site))?max_index_plus_one:max_index_plus_one-1;
                  //interpolating equations
                  double dR,interpol_index;
                  if( (max_index>0) && (spec.getValue(site, max_index-1)>spec.getValue(site, max_index_plus_one)) ){
                      dR=spec.getValue(site, max_index-1)/spec.getValue(site, max_index);
                      interpol_index=max_index+(1.0-2.0*dR)/(1.0+dR);
                  }
                  else {
                      dR=spec.getValue(site, max_index_plus_one)/spec.getValue(site, max_index);
                      interpol_index=max_index-(1.0-2.0*dR)/(1.0+dR);
                  }
                  double freq_MHz_site=interpol_index/(period_us*rawData.getSize(site));
                  frequency_MHz.set(site, freq_MHz_site);
              }
          }
          else if(freqCalMode.toString().compareTo("LinearFit")==0) {
              MultiSiteWaveLong rising_edge=new MultiSiteWaveLong((rawData.getSize(activeSites[0])));
              int i,j;
              for(int site:activeSites) {
                  for(i=0,j=0;i<rawData.getSize(site)-1;i++) {
                      if(rawData.getValue(site, i)<rawData.getValue(site, i+1)) {
                          rising_edge.setValue(site, j++, i);
                      }
                  }
                  Line result1=new Line();
                  try {
                      result1=rising_edge.get(site).extractValues(0, j).regressionLine();
                  } catch (Exception e) {
                     e.printStackTrace();
                  }
                  double freq_MHz_site=1/(period_us*result1.getSlope());
                  frequency_MHz.set(site, freq_MHz_site);
              }
          }
          else if(freqCalMode.toString().compareTo("EdgeCount")==0) {
              int array_size=rawData.getSize(activeSites[0]);
              for(int site:activeSites) {
                  int i=0,j=0;
                  int first_edge=0,last_edge=1;
                  int low=0;
                  int cycle_count=0;
                  if(rawData.getValue(site, 0)==0) { //first edge is rising edge
                      for(j=1;j<array_size;j++) {
                          if(rawData.getValue(site, j)>0) {
                              first_edge=j;
                              low=0;
                              break;
                          }
                      }
                      for(i=++j;i<array_size;i++) {
                          if(rawData.getValue(site, i)==0) {
                              low=1;
                          }
                          else {
                              if(low!=0) {
                                  cycle_count++;
                                  last_edge=i;
                              }
                              low=0;
                          }
                      }
                  }
                  else { //first edge is falling edge
                      for(j=1;j<array_size;j++) {
                          if(rawData.getValue(site, j)==0) {
                              first_edge=j;
                              low=0;
                              break;
                          }
                      }
                      for(i=++j;i<array_size;i++) {
                          if(rawData.getValue(site, i)>0) {
                              low=1;
                          }
                          else {
                              if(low!=0) {
                                  cycle_count++;
                                  last_edge=i;
                              }
                              low=0;
                          }
                      }
                  }
                  double freq_calc=0;
                  if(cycle_count==0) {
                      freq_calc=-1.0;
                  }
                  else {
                      freq_calc=cycle_count/((last_edge-first_edge)*period_us);
                  }
                  frequency_MHz.set(site, freq_calc);
              }
          }
          return frequency_MHz;
      }

}
