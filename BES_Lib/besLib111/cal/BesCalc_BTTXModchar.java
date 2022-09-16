package besLib.cal;

import xoc.dta.datatypes.MultiSiteBoolean;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.dsp.MultiSiteWaveComplex;
import xoc.dta.datatypes.dsp.MultiSiteWaveDouble;

/**
 * This class is used to calculate BES BT TX Modulation Characteristic parameter
 * @version V1.1
 * @author Weng Yongxin, Ronnie Li
 **/
public class BesCalc_BTTXModchar {

    private int[] activeSites;
    private MultiSiteWaveComplex btWaveComplex = new MultiSiteWaveComplex();
    private MultiSiteDouble df1=new MultiSiteDouble(0);
    private MultiSiteDouble df2=new MultiSiteDouble(0);
    private MultiSiteDouble df2_Min=new MultiSiteDouble(0);
    /**
     * Enum type for BT TX ModChar.
     */
    public static enum payloadSeq{
        F0,
        AA,
    }

    /**
     * Constructor function
     * @param btWaveComplex raw data captured by WSRF digitizer
     * @param payloadSeq Enum type for BT TX ModChar
     */
    public BesCalc_BTTXModchar(MultiSiteWaveComplex btWaveComplex, payloadSeq payloadSeq) {
        this.btWaveComplex.set(btWaveComplex);
        this.activeSites=this.btWaveComplex.getActiveSites();
        int samplesPerSymbol=(int)(btWaveComplex.getSampleRate(this.activeSites[0])/1e6); //data rate=1 Mbit/s
        int samplesPerWord=8*samplesPerSymbol;
        int sampleSize=btWaveComplex.getSize(activeSites[0]);

        MultiSiteWaveDouble phase=new MultiSiteWaveDouble(sampleSize);
        MultiSiteWaveDouble phaseContinous=new MultiSiteWaveDouble(sampleSize);
        MultiSiteWaveDouble gfsk=new MultiSiteWaveDouble(sampleSize);
        for(int site:activeSites) {
            int cycle=0;
            for(int i=0;i<btWaveComplex.getSize(activeSites[0]);i++) {
                phase.setValue(site, i, Math.atan2(btWaveComplex.getValue(site, i).getImaginary(), btWaveComplex.getValue(site, i).getReal()));
                if(i==0) {
                    phaseContinous.setValue(site, i, phase.getValue(site, i));
                }
                else {
                    if(phase.getValue(site, i-1)>1 && phase.getValue(site, i)<-1) {
                        cycle=cycle+1;
                    }
                    else if(phase.getValue(site, i-1)<-1 && phase.getValue(site, i)>1) {
                        cycle=cycle-1;
                    }
                }
                phaseContinous.setValue(site, i, phase.getValue(site, i)+cycle*2*Math.PI);
            }
        }
//        phase.plot("phase");
//        phaseContinous.plot("phaseContinous");
        for(int site:activeSites) {
            for(int i=0;i<btWaveComplex.getSize(site);i++) {
                if(i!=btWaveComplex.getSize(site)-1) {
                    gfsk.setValue(site, i, (phaseContinous.getValue(site, i+1)-phaseContinous.getValue(site, i))*btWaveComplex.getSampleRate(site)/2/Math.PI);
                }
            }
        }
//        gfsk.plot("gfsk");
        MultiSiteDouble sumFreq=new MultiSiteDouble();
        MultiSiteDouble averageFreq =new MultiSiteDouble();
        MultiSiteWaveDouble gfskMod=new MultiSiteWaveDouble(sampleSize-1);

        sumFreq=gfsk.sum();
        averageFreq=sumFreq.divide(btWaveComplex.getSize(activeSites[0])-1);
        gfskMod=gfsk.subtract(averageFreq);

        MultiSiteLong dataKeep=new MultiSiteLong();
        MultiSiteBoolean dataKeepBreak=new MultiSiteBoolean(true);

        for(int site:activeSites) {
            for(int i=0;i<(btWaveComplex.getSize(site)-samplesPerSymbol*2+1);i++) {
                if( gfskMod.getValue(site, i)>0.0 &&
                    gfskMod.getValue(site, i+1)>0.0 &&
                    gfskMod.getValue(site, i+samplesPerSymbol-3)>0.0 &&
                    gfskMod.getValue(site, i+samplesPerSymbol-2)>0.0 &&
                    gfskMod.getValue(site, i+samplesPerSymbol)<0.0 &&
                    gfskMod.getValue(site, i+samplesPerSymbol+1)<0.0 &&
                    gfskMod.getValue(site, i+2*samplesPerSymbol-3)<0.0 &&
                    gfskMod.getValue(site, i+2*samplesPerSymbol-2)<0.0 )
                {
                    if(dataKeepBreak.get(site)==true) {
                        dataKeepBreak.set(site, false);
                        dataKeep.set(site, i);
                    }

                }
            }
        }


        if(payloadSeq == BesCalc_BTTXModchar.payloadSeq.F0) {
            MultiSiteWaveDouble temp=new MultiSiteWaveDouble(samplesPerWord);
            MultiSiteDouble sumFreq2=new MultiSiteDouble(0);
            MultiSiteDouble freqDelta=new MultiSiteDouble();
            int edge1=0;
            for(int site:activeSites) {
                double avgDev=0;
                double avg1Dev=0;
                double avg2Dev=0;
                double avg3Dev=0;
                double avg4Dev=0;

                edge1=(int)(dataKeep.get(site)+samplesPerSymbol);
                if(edge1< (btWaveComplex.getSize(site)-8*samplesPerWord)) {
                    for(int j=0;j<8;j++) {
                        temp.set(site, gfskMod.get(site).extractValues(edge1+samplesPerWord*j, samplesPerWord));
                        avgDev=gfskMod.get(site).extractValues(edge1+samplesPerWord*j,samplesPerWord).mean();
                        avg1Dev=temp.get(site).extractValues(samplesPerSymbol*1, samplesPerSymbol).mean();
                        avg2Dev=temp.get(site).extractValues(samplesPerSymbol*2, samplesPerSymbol).mean();
                        avg3Dev=temp.get(site).extractValues(samplesPerSymbol*5, samplesPerSymbol).mean();
                        avg4Dev=temp.get(site).extractValues(samplesPerSymbol*6, samplesPerSymbol).mean();

                        sumFreq2.set(site, sumFreq2.get(site)+Math.abs(avg1Dev-avgDev));
                        sumFreq2.set(site, sumFreq2.get(site)+Math.abs(avg2Dev-avgDev));
                        sumFreq2.set(site, sumFreq2.get(site)+Math.abs(avg3Dev-avgDev));
                        sumFreq2.set(site, sumFreq2.get(site)+Math.abs(avg4Dev-avgDev));
                    }
                    freqDelta.set(site, sumFreq2.get(site)/(4.0*8.0));
                }
                else {
                    freqDelta.set(site, -1);
                }
            }
            this.df1.set(freqDelta);
        }
        else if(payloadSeq==BesCalc_BTTXModchar.payloadSeq.AA) {
            MultiSiteWaveDouble temp=new MultiSiteWaveDouble(samplesPerWord);
            MultiSiteWaveDouble tempMod=new MultiSiteWaveDouble(samplesPerWord);
            MultiSiteWaveDouble tempModAbs=new MultiSiteWaveDouble(samplesPerWord);
            MultiSiteWaveDouble tempMod2=new MultiSiteWaveDouble(samplesPerWord);

            MultiSiteDouble sumFreq2=new MultiSiteDouble(0);
            MultiSiteDouble freqDelta=new MultiSiteDouble();
            MultiSiteDouble freqMin=new MultiSiteDouble(300000);
            int edge1=0;
            for(int site:activeSites) {
                double avgDev=0;
                edge1=(int)(dataKeep.get(site)+samplesPerSymbol);
                if(edge1< (btWaveComplex.getSize(site)-8*samplesPerWord)) {
                    for(int j=0;j<8;j++) {
                        temp.set(site, gfskMod.get(site).extractValues(edge1+samplesPerWord*j, samplesPerWord));
                        avgDev=gfskMod.get(site).extractValues(edge1+samplesPerWord*j,samplesPerWord).mean();
                        tempMod=temp.subtract(avgDev);
                        tempModAbs=tempMod.abs();
                        for(int i=0;i<8;i++) {
                            tempMod2.set(site, tempModAbs.get(site).extractValues(samplesPerSymbol*i, samplesPerSymbol));
                            double maxValue=tempMod2.get(site).max();
                            sumFreq2.set(site, sumFreq2.get(site)+maxValue);
                            if(freqMin.get(site)>Math.abs(maxValue)) {
                                freqMin.set(site, Math.abs(maxValue));
                            }
                        }
                    }
                    freqDelta.set(site, sumFreq2.get(site)/(8.0*8.0));
                }
                else {
                    freqDelta.set(site, -1);
                }
            }
            this.df2.set(freqDelta);
            this.df2_Min.set(freqMin);
        }
    }

    /**
     * get parameter df1.
     * @return df1 in unit Hz.
     */
    public MultiSiteDouble getDf1() {
        return this.df1;
    }

    /**
     * get parameter df2.
     * @return df2 in unit Hz.
     */
    public MultiSiteDouble getDf2() {
        return this.df2;
    }

    /**
     * get parameter df2_Min.
     * @return df2_Min in unit Hz.
     */
    public MultiSiteDouble getDf2_Min() {
        return this.df2_Min;
    }


}
