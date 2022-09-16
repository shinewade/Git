package BT1502_pro_tml.BT;

import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import BT1502_pro_tml.Global.StaticFields;
import besLib.cal.BesCalc_BTTX;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupRfMeas.IModPower.SetupWindowFunction;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.MultiSiteLongArray;
import xoc.dta.datatypes.dsp.MultiSiteWaveComplex;
import xoc.dta.measurement.ILocalMeasurement;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IRfMeasResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class BT_TX_DlyCap_Cal extends TestMethod {
    @In public String spec_measurement;
    private String RF_OUT="BT_RF";

    public IMeasurement measurement_bt_cal;
    public ILocalMeasurement measurement_bt_write;

    //Parametric Test Descriptor
    public IParametricTestDescriptor    ptd_dly_cal_data;
    public IParametricTestDescriptor    ptd_dly_cal_passwidth;

    public IParametricTestDescriptor ptd_pwr_LF_0,ptd_pwr_LF_1,ptd_pwr_LF_2,ptd_pwr_LF_3,ptd_pwr_LF_4,ptd_pwr_LF_5,ptd_pwr_LF_6,ptd_pwr_LF_7,ptd_pwr_LF_8,ptd_pwr_LF_9,ptd_pwr_LF_a,ptd_pwr_LF_b,ptd_pwr_LF_c,ptd_pwr_LF_d,ptd_pwr_LF_e,ptd_pwr_LF_f;
    public IParametricTestDescriptor ptd_pwr_MF_0,ptd_pwr_MF_1,ptd_pwr_MF_2,ptd_pwr_MF_3,ptd_pwr_MF_4,ptd_pwr_MF_5,ptd_pwr_MF_6,ptd_pwr_MF_7,ptd_pwr_MF_8,ptd_pwr_MF_9,ptd_pwr_MF_a,ptd_pwr_MF_b,ptd_pwr_MF_c,ptd_pwr_MF_d,ptd_pwr_MF_e,ptd_pwr_MF_f;
    public IParametricTestDescriptor ptd_pwr_HF_0,ptd_pwr_HF_1,ptd_pwr_HF_2,ptd_pwr_HF_3,ptd_pwr_HF_4,ptd_pwr_HF_5,ptd_pwr_HF_6,ptd_pwr_HF_7,ptd_pwr_HF_8,ptd_pwr_HF_9,ptd_pwr_HF_a,ptd_pwr_HF_b,ptd_pwr_HF_c,ptd_pwr_HF_d,ptd_pwr_HF_e,ptd_pwr_HF_f;
    public IParametricTestDescriptor ptd_freq_LF_0,ptd_freq_LF_1,ptd_freq_LF_2,ptd_freq_LF_3,ptd_freq_LF_4,ptd_freq_LF_5,ptd_freq_LF_6,ptd_freq_LF_7,ptd_freq_LF_8,ptd_freq_LF_9,ptd_freq_LF_a,ptd_freq_LF_b,ptd_freq_LF_c,ptd_freq_LF_d,ptd_freq_LF_e,ptd_freq_LF_f;
    public IParametricTestDescriptor ptd_freq_MF_0,ptd_freq_MF_1,ptd_freq_MF_2,ptd_freq_MF_3,ptd_freq_MF_4,ptd_freq_MF_5,ptd_freq_MF_6,ptd_freq_MF_7,ptd_freq_MF_8,ptd_freq_MF_9,ptd_freq_MF_a,ptd_freq_MF_b,ptd_freq_MF_c,ptd_freq_MF_d,ptd_freq_MF_e,ptd_freq_MF_f;
    public IParametricTestDescriptor ptd_freq_HF_0,ptd_freq_HF_1,ptd_freq_HF_2,ptd_freq_HF_3,ptd_freq_HF_4,ptd_freq_HF_5,ptd_freq_HF_6,ptd_freq_HF_7,ptd_freq_HF_8,ptd_freq_HF_9,ptd_freq_HF_a,ptd_freq_HF_b,ptd_freq_HF_c,ptd_freq_HF_d,ptd_freq_HF_e,ptd_freq_HF_f;
    public IParametricTestDescriptor ptd_loleak_LF_0,ptd_loleak_LF_1,ptd_loleak_LF_2,ptd_loleak_LF_3,ptd_loleak_LF_4,ptd_loleak_LF_5,ptd_loleak_LF_6,ptd_loleak_LF_7,ptd_loleak_LF_8,ptd_loleak_LF_9,ptd_loleak_LF_a,ptd_loleak_LF_b,ptd_loleak_LF_c,ptd_loleak_LF_d,ptd_loleak_LF_e,ptd_loleak_LF_f;
    public IParametricTestDescriptor ptd_loleak_MF_0,ptd_loleak_MF_1,ptd_loleak_MF_2,ptd_loleak_MF_3,ptd_loleak_MF_4,ptd_loleak_MF_5,ptd_loleak_MF_6,ptd_loleak_MF_7,ptd_loleak_MF_8,ptd_loleak_MF_9,ptd_loleak_MF_a,ptd_loleak_MF_b,ptd_loleak_MF_c,ptd_loleak_MF_d,ptd_loleak_MF_e,ptd_loleak_MF_f;
    public IParametricTestDescriptor ptd_loleak_HF_0,ptd_loleak_HF_1,ptd_loleak_HF_2,ptd_loleak_HF_3,ptd_loleak_HF_4,ptd_loleak_HF_5,ptd_loleak_HF_6,ptd_loleak_HF_7,ptd_loleak_HF_8,ptd_loleak_HF_9,ptd_loleak_HF_a,ptd_loleak_HF_b,ptd_loleak_HF_c,ptd_loleak_HF_d,ptd_loleak_HF_e,ptd_loleak_HF_f;
    public IParametricTestDescriptor ptd_imgrej_LF_0,ptd_imgrej_LF_1,ptd_imgrej_LF_2,ptd_imgrej_LF_3,ptd_imgrej_LF_4,ptd_imgrej_LF_5,ptd_imgrej_LF_6,ptd_imgrej_LF_7,ptd_imgrej_LF_8,ptd_imgrej_LF_9,ptd_imgrej_LF_a,ptd_imgrej_LF_b,ptd_imgrej_LF_c,ptd_imgrej_LF_d,ptd_imgrej_LF_e,ptd_imgrej_LF_f;
    public IParametricTestDescriptor ptd_imgrej_MF_0,ptd_imgrej_MF_1,ptd_imgrej_MF_2,ptd_imgrej_MF_3,ptd_imgrej_MF_4,ptd_imgrej_MF_5,ptd_imgrej_MF_6,ptd_imgrej_MF_7,ptd_imgrej_MF_8,ptd_imgrej_MF_9,ptd_imgrej_MF_a,ptd_imgrej_MF_b,ptd_imgrej_MF_c,ptd_imgrej_MF_d,ptd_imgrej_MF_e,ptd_imgrej_MF_f;
    public IParametricTestDescriptor ptd_imgrej_HF_0,ptd_imgrej_HF_1,ptd_imgrej_HF_2,ptd_imgrej_HF_3,ptd_imgrej_HF_4,ptd_imgrej_HF_5,ptd_imgrej_HF_6,ptd_imgrej_HF_7,ptd_imgrej_HF_8,ptd_imgrej_HF_9,ptd_imgrej_HF_a,ptd_imgrej_HF_b,ptd_imgrej_HF_c,ptd_imgrej_HF_d,ptd_imgrej_HF_e,ptd_imgrej_HF_f;
    public IParametricTestDescriptor ptd_dly_cal_0,ptd_dly_cal_1,ptd_dly_cal_2,ptd_dly_cal_3,ptd_dly_cal_4,ptd_dly_cal_5,ptd_dly_cal_6,ptd_dly_cal_7,ptd_dly_cal_8,ptd_dly_cal_9,ptd_dly_cal_a,ptd_dly_cal_b,ptd_dly_cal_c,ptd_dly_cal_d,ptd_dly_cal_e,ptd_dly_cal_f;

    private int[] cfg_Cal=new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    private String[] freq_Cal=new String[] {"freq_2402MHz","freq_2441MHz","freq_2480MHz"};

    @Override
    public void setup() {

        /**************** measurement_bt_cal *******************/
        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        //BTTX_Meas_2402
        ds1.addRfMeas(RF_OUT).setConfigModeHighResolution().setConfigOptionSiteInterlacingOn().
            modPower("BTTX_Meas_2402").
            setFrequency("2.402 GHz").
            setExpectedMaxPower("15 dBm").
            setSamples(10000).
            setBandwidthOfInterest("2 MHz").
            setIfFrequency("25 MHz").
            setSampleRate("25 MHz").
            setWindowFunction(SetupWindowFunction.hanning);

        //BTTX_Meas_2441
        ds1.addRfMeas(RF_OUT).setConfigModeHighResolution().setConfigOptionSiteInterlacingOn().
            modPower("BTTX_Meas_2441").
            setFrequency("2.441 GHz").
            setExpectedMaxPower("15 dBm").
            setSamples(10000).
            setBandwidthOfInterest("2 MHz").
            setIfFrequency("25 MHz").
            setSampleRate("25 MHz").
            setWindowFunction(SetupWindowFunction.hanning);

        //BTTX_Meas_2480
        ds1.addRfMeas(RF_OUT).setConfigModeHighResolution().setConfigOptionSiteInterlacingOn().
            modPower("BTTX_Meas_2480").
            setFrequency("2.480 GHz").
            setExpectedMaxPower("15 dBm").
            setSamples(10000).
            setBandwidthOfInterest("2 MHz").
            setIfFrequency("25 MHz").
            setSampleRate("25 MHz").
            setWindowFunction(SetupWindowFunction.hanning);

        //PA
        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement_bt_cal,I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");
        i2c.transactionSequenceBegin("capdelay_mode");
        i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00, 0x00000000);
        i2c.transactionSequenceEnd();

        for(String freq : freq_Cal) {
            for(int cfg : cfg_Cal) {
                switch (freq) {
                case "freq_2402MHz":
                    i2c.transactionSequenceBegin("tx_dly_cap_cal_"+freq+"_cfg_"+cfg_Cal[cfg]);
                        i2c.write(BesI2cAddrType.RF, 0x9e, 0x140c + (cfg_Cal[cfg]<<6) );
                        i2c.waitTime(1e6);
                        i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00, 0x00000000);
                        i2c.waitTime(1e6);
                        i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00, 0xa0000);
                        i2c.waitTime(1e6);
                    i2c.transactionSequenceEnd();
                    ds1.actionCall("BTTX_Meas_2402");
                    break;
                case "freq_2441MHz":
                    i2c.transactionSequenceBegin("tx_dly_cap_cal_"+freq+"_cfg_"+cfg_Cal[cfg]);
                        i2c.write(BesI2cAddrType.RF, 0x9e, 0x140c + (cfg_Cal[cfg]<<6) );
                        i2c.waitTime(1e6);
                        i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00, 0x00000000);
                        i2c.waitTime(1e6);
                        i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00, 0xa0027);
                        i2c.waitTime(1e6);
                    i2c.transactionSequenceEnd();
                    ds1.actionCall("BTTX_Meas_2441");
                    break;
                case "freq_2480MHz":
                    i2c.transactionSequenceBegin("tx_dly_cap_cal_"+freq+"_cfg_"+cfg_Cal[cfg]);
                        i2c.write(BesI2cAddrType.RF, 0x9e, 0x140c + (cfg_Cal[cfg]<<6) );
                        i2c.waitTime(1e6);
                        i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00, 0x00000000);
                        i2c.waitTime(1e6);
                        i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00, 0xa004e);
                        i2c.waitTime(1e6);
                    i2c.transactionSequenceEnd();
                    ds1.actionCall("BTTX_Meas_2480");
                    break;

                default:
                    break;
                }
            }
        }
        measurement_bt_cal.setSetups(ds1);




        /**************** measurement_bt_write *******************/
        IDeviceSetup ds2 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds2.importSpec(spec_measurement);

        //PA
        BesPA_I2C i2c_BT_Write=new BesPA_I2C(ds2, measurement_bt_write,I2cRegAddrBits.RegAddr_8Bits);
        i2c_BT_Write.setSignals("I2C_SCL", "I2C_SDA");

        i2c_BT_Write.transactionSequenceBegin("BT_DlyCap_dynamic_write");
            i2c_BT_Write.write(BesI2cAddrType.RF, 0x9e,"dynWrite_0x9e");
            i2c_BT_Write.waitTime(2e6);//2ms
        i2c_BT_Write.transactionSequenceEnd();

        measurement_bt_write.setSetups(ds2);

    }

    @Override
    public void execute() {

        measurement_bt_cal.execute();
        IRfMeasResults res_RF = measurement_bt_cal.rfMeas(RF_OUT).preserveResults();

        //Get result using besLib class "BesCalc_BTTX"
        int[] activeSites = context.getActiveSites();
        MultiSiteWaveComplex[] spectrum_BTTX_2402=new MultiSiteWaveComplex[cfg_Cal.length];
        MultiSiteWaveComplex[] spectrum_BTTX_2441=new MultiSiteWaveComplex[cfg_Cal.length];
        MultiSiteWaveComplex[] spectrum_BTTX_2480=new MultiSiteWaveComplex[cfg_Cal.length];
        BesCalc_BTTX[] res_BTTX_2402=new BesCalc_BTTX[cfg_Cal.length];
        BesCalc_BTTX[] res_BTTX_2441=new BesCalc_BTTX[cfg_Cal.length];
        BesCalc_BTTX[] res_BTTX_2480=new BesCalc_BTTX[cfg_Cal.length];
        MultiSiteLong[] passflag_Cal_L=new MultiSiteLong[cfg_Cal.length];
        MultiSiteLong[] passflag_Cal_M=new MultiSiteLong[cfg_Cal.length];
        MultiSiteLong[] passflag_Cal_H=new MultiSiteLong[cfg_Cal.length];
        double ATT=0;
        ATT=ATT+StaticFields.att_bt.get(context.getActiveSites()[0]);

        for(String freq : freq_Cal) {
            for(int cfg : cfg_Cal) {
            switch (freq) {
                case "freq_2402MHz":
                    spectrum_BTTX_2402[cfg]=res_RF.modPower("BTTX_Meas_2402").getComplexWaveform(RF_OUT).getElement(cfg);
                    res_BTTX_2402[cfg]=new BesCalc_BTTX(spectrum_BTTX_2402[cfg], 9);
                    res_BTTX_2402[cfg].setAtt(new MultiSiteDouble(ATT));
                    passflag_Cal_L[cfg]=new MultiSiteLong(0);
                    for(int site: activeSites) {
                        if(res_BTTX_2402[cfg].getRfPower_dBm().get(site)>3              && res_BTTX_2402[cfg].getRfPower_dBm().get(site)<15 &&
                           res_BTTX_2402[cfg].getRfFrequency(2402).get(site)> 2401*1e6  && res_BTTX_2402[cfg].getRfFrequency(2402).get(site)< 2403*1e6 &&
                           res_BTTX_2402[cfg].getLoLeakage().get(site)>15               && res_BTTX_2402[cfg].getLoLeakage().get(site)<90 &&
                           res_BTTX_2402[cfg].getImageRejection().get(site)>15          && res_BTTX_2402[cfg].getImageRejection().get(site)<90 )
                        {
                            passflag_Cal_L[cfg].set(site, 1);
                        }
                        else {
                            passflag_Cal_L[cfg].set(site, 0);
                        }
                    }
                    break;

                case "freq_2441MHz":
                    spectrum_BTTX_2441[cfg]=res_RF.modPower("BTTX_Meas_2441").getComplexWaveform(RF_OUT).getElement(cfg);
                    res_BTTX_2441[cfg]=new BesCalc_BTTX(spectrum_BTTX_2441[cfg], 9);
                    res_BTTX_2441[cfg].setAtt(new MultiSiteDouble(ATT));
                    passflag_Cal_M[cfg]=new MultiSiteLong(0);
                    for(int site: activeSites) {
                        if(res_BTTX_2441[cfg].getRfPower_dBm().get(site)>3              && res_BTTX_2441[cfg].getRfPower_dBm().get(site)<15 &&
                           res_BTTX_2441[cfg].getRfFrequency(2441).get(site)> 2440*1e6  && res_BTTX_2441[cfg].getRfFrequency(2441).get(site)< 2442*1e6 &&
                           res_BTTX_2441[cfg].getLoLeakage().get(site)>15               && res_BTTX_2441[cfg].getLoLeakage().get(site)<90 &&
                           res_BTTX_2441[cfg].getImageRejection().get(site)>15          && res_BTTX_2441[cfg].getImageRejection().get(site)<90 )
                        {
                            passflag_Cal_M[cfg].set(site, 1);
                        }
                        else {
                            passflag_Cal_M[cfg].set(site, 0);
                        }
                    }
                    break;

                case "freq_2480MHz":
                    spectrum_BTTX_2480[cfg]=res_RF.modPower("BTTX_Meas_2480").getComplexWaveform(RF_OUT).getElement(cfg);
                    res_BTTX_2480[cfg]=new BesCalc_BTTX(spectrum_BTTX_2480[cfg], 9);
                    res_BTTX_2480[cfg].setAtt(new MultiSiteDouble(ATT));
                    passflag_Cal_H[cfg]=new MultiSiteLong(0);
                    for(int site: activeSites) {
                        if(res_BTTX_2480[cfg].getRfPower_dBm().get(site)>3              && res_BTTX_2480[cfg].getRfPower_dBm().get(site)<15 &&
                           res_BTTX_2480[cfg].getRfFrequency(2480).get(site)> 2479*1e6  && res_BTTX_2480[cfg].getRfFrequency(2480).get(site)< 2481*1e6 &&
                           res_BTTX_2480[cfg].getLoLeakage().get(site)>15               && res_BTTX_2480[cfg].getLoLeakage().get(site)<90 &&
                           res_BTTX_2480[cfg].getImageRejection().get(site)>15          && res_BTTX_2480[cfg].getImageRejection().get(site)<90 )
                        {
                            passflag_Cal_H[cfg].set(site, 1);
                        }
                        else {
                            passflag_Cal_H[cfg].set(site, 0);
                        }
                    }
                    break;

                default:

                    break;
                }
            }
        }


        //calculate pass value
        long[] cfg_cal_pf=new long[32];
        MultiSiteLongArray cfg_cal_pf_d_arrary=new MultiSiteLongArray(32, 0);
        MultiSiteLong passflag_st=new MultiSiteLong(-1);
        MultiSiteLong passflag_sp=new MultiSiteLong(-1);
        MultiSiteLong passflag_data=new MultiSiteLong(-1);
        MultiSiteLong passflag_width=new MultiSiteLong(-1);
        MultiSiteLong cfg_cal_pf_sum=new MultiSiteLong(0);

        for(int site:activeSites) {
            //get L/H/M overall pass/fail
            for(int i=0;i<32;i++) {
                int index_array=0;
                if(i<16) {
                    index_array=i;
                }
                else {
                    index_array=i-16;
                }
                if(passflag_Cal_L[index_array].get(site)==1 && passflag_Cal_M[index_array].get(site)==1 && passflag_Cal_H[index_array].get(site)==1)
                {
                    cfg_cal_pf[i]=1;
                    cfg_cal_pf_d_arrary.setElement(site, index_array, 1);
                }
                else {
                    cfg_cal_pf[i]=0;
                }
            }
            //search start index
            int i=0;
            for(;i<(32-3);i++) {
                if(cfg_cal_pf[i]==0 && cfg_cal_pf[i+1]==1 && cfg_cal_pf[i+2]==1 && cfg_cal_pf[i+3]==1 ) {
                    passflag_st.set(site, i+1);
                    break;
                }
            }
            //search stop index
            for(;i<(32-3);i++) {
                if(cfg_cal_pf[i]==1 && cfg_cal_pf[i+1]==1 && cfg_cal_pf[i+2]==1 && cfg_cal_pf[i+3]==0 ) {
                    passflag_sp.set(site, i+2);
                    break;
                }
            }
            //get pass count
            for(i=0;i<32;i++) {
                cfg_cal_pf_sum.set(site, cfg_cal_pf_sum.get(site)+cfg_cal_pf[i]);
             }
        }

        /**************** USER DEFINE******** *******************/
        for(int site:activeSites) {
            if(cfg_cal_pf_sum.get(site)==32) {
                StaticFields.passflag_cal.set(site,true);
                passflag_data.set(site, 8);
                passflag_width.set(site, 16);
            }
            else if(passflag_st.get(site)>=0 && passflag_st.get(site)<=31 && passflag_sp.get(site)>=0 && passflag_sp.get(site)<=31 ) {
                passflag_data.set(site,(passflag_st.add(passflag_sp).divide(2).get(site)));
                StaticFields.passflag_cal.set(site,true);
                passflag_width.set(site, (passflag_sp.subtract(passflag_st).add(1).get(site)));
               if(passflag_data.get(site)>15) {
                    passflag_data.set(site,passflag_data.subtract(16).get(site));
                }

            }
        }
        /**************** USER DEFINE END ******** *******************/


        //pass to global variables for efuse writing
        for(int site:activeSites) {
            if(passflag_data.get(site)>=0 && passflag_data.get(site)<=15) {
                StaticFields.lBt_CalValue_0x28.set(site,passflag_data.get(site));
            }
            else {
                StaticFields.lBt_CalValue_0x28.set(site,0);
            }
        }

        //update register 0x28 with cal value
        MultiSiteLong cal_write = new MultiSiteLong();
        for(int site:activeSites) {
            cal_write.set(site, ((StaticFields.lBt_CalValue_0x28.get(site))<<6)+(0x140c));
        }
        ProtocolAccess.setDynamicData("dynWrite_0x9e", cal_write);
        ProtocolAccess.updateDynamicData(context, measurement_bt_write);
        measurement_bt_write.execute();


        if(StaticFields.debugMode)
        {
               String testSuiteName_Qualified=context.getTestSuiteName();
               String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
               println("**********"+testSuiteName+"***cal_data**********");
               int i=0;
               for( i=0;i<16;i++) {
                   println("passflag_Cal_L"+Long.toHexString(i)+" = "+passflag_Cal_L[i]);
               }
               for( i=0;i<16;i++) {
                   println("passflag_Cal_M"+Long.toHexString(i)+" = "+passflag_Cal_M[i]);
               }
               for( i=0;i<16;i++) {
                   println("passflag_Cal_H"+Long.toHexString(i)+" = "+passflag_Cal_H[i]);
               }
               for( i=0;i<16;i++) {
                   println("cfg_cal_pf_d_"+Long.toHexString(i)+" = "+cfg_cal_pf_d_arrary.getElement(i));
               }
               println("passflag_st = "+passflag_st);
               println("passflag_sp = "+passflag_sp);
               println("passflag_data = "+passflag_data);
               println("cfg_cal_pf_sum = "+cfg_cal_pf_sum);
               println("lBt_CalValue_0x28 = "+StaticFields.lBt_CalValue_0x28);
           }


        ptd_dly_cal_data.evaluate(passflag_data);
        ptd_dly_cal_passwidth.evaluate(passflag_width);

        //Just record value to STDF
        ptd_dly_cal_0.setLowLimitToPass(); ptd_dly_cal_0.setHighLimitToPass();
        ptd_dly_cal_1.setLowLimitToPass(); ptd_dly_cal_1.setHighLimitToPass();
        ptd_dly_cal_2.setLowLimitToPass(); ptd_dly_cal_2.setHighLimitToPass();
        ptd_dly_cal_3.setLowLimitToPass(); ptd_dly_cal_3.setHighLimitToPass();
        ptd_dly_cal_4.setLowLimitToPass(); ptd_dly_cal_4.setHighLimitToPass();
        ptd_dly_cal_5.setLowLimitToPass(); ptd_dly_cal_5.setHighLimitToPass();
        ptd_dly_cal_6.setLowLimitToPass(); ptd_dly_cal_6.setHighLimitToPass();
        ptd_dly_cal_7.setLowLimitToPass(); ptd_dly_cal_7.setHighLimitToPass();
        ptd_dly_cal_8.setLowLimitToPass(); ptd_dly_cal_8.setHighLimitToPass();
        ptd_dly_cal_9.setLowLimitToPass(); ptd_dly_cal_9.setHighLimitToPass();
        ptd_dly_cal_a.setLowLimitToPass(); ptd_dly_cal_a.setHighLimitToPass();
        ptd_dly_cal_b.setLowLimitToPass(); ptd_dly_cal_b.setHighLimitToPass();
        ptd_dly_cal_c.setLowLimitToPass(); ptd_dly_cal_c.setHighLimitToPass();
        ptd_dly_cal_d.setLowLimitToPass(); ptd_dly_cal_d.setHighLimitToPass();
        ptd_dly_cal_e.setLowLimitToPass(); ptd_dly_cal_e.setHighLimitToPass();
        ptd_dly_cal_f.setLowLimitToPass(); ptd_dly_cal_f.setHighLimitToPass();
        ptd_dly_cal_0.evaluate(cfg_cal_pf_d_arrary.getElement(0));
        ptd_dly_cal_1.evaluate(cfg_cal_pf_d_arrary.getElement(1));
        ptd_dly_cal_2.evaluate(cfg_cal_pf_d_arrary.getElement(2));
        ptd_dly_cal_3.evaluate(cfg_cal_pf_d_arrary.getElement(3));
        ptd_dly_cal_4.evaluate(cfg_cal_pf_d_arrary.getElement(4));
        ptd_dly_cal_5.evaluate(cfg_cal_pf_d_arrary.getElement(5));
        ptd_dly_cal_6.evaluate(cfg_cal_pf_d_arrary.getElement(6));
        ptd_dly_cal_7.evaluate(cfg_cal_pf_d_arrary.getElement(7));
        ptd_dly_cal_8.evaluate(cfg_cal_pf_d_arrary.getElement(8));
        ptd_dly_cal_9.evaluate(cfg_cal_pf_d_arrary.getElement(9));
        ptd_dly_cal_a.evaluate(cfg_cal_pf_d_arrary.getElement(10));
        ptd_dly_cal_b.evaluate(cfg_cal_pf_d_arrary.getElement(11));
        ptd_dly_cal_c.evaluate(cfg_cal_pf_d_arrary.getElement(12));
        ptd_dly_cal_d.evaluate(cfg_cal_pf_d_arrary.getElement(13));
        ptd_dly_cal_e.evaluate(cfg_cal_pf_d_arrary.getElement(14));
        ptd_dly_cal_f.evaluate(cfg_cal_pf_d_arrary.getElement(15));

        for(String freq : freq_Cal) {
            for(int cfg : cfg_Cal) {
            switch (freq) {
                case "freq_2402MHz":
                    switch (cfg) {
                    case 0:
                        ptd_pwr_LF_0.setLowLimitToPass(); ptd_pwr_LF_0.setHighLimitToPass(); ptd_pwr_LF_0.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_0.setLowLimitToPass(); ptd_freq_LF_0.setHighLimitToPass(); ptd_freq_LF_0.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_0.setLowLimitToPass(); ptd_loleak_LF_0.setHighLimitToPass(); ptd_loleak_LF_0.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_0.setLowLimitToPass(); ptd_imgrej_LF_0.setHighLimitToPass(); ptd_imgrej_LF_0.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 1:
                        ptd_pwr_LF_1.setLowLimitToPass(); ptd_pwr_LF_1.setHighLimitToPass(); ptd_pwr_LF_1.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_1.setLowLimitToPass(); ptd_freq_LF_1.setHighLimitToPass(); ptd_freq_LF_1.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_1.setLowLimitToPass(); ptd_loleak_LF_1.setHighLimitToPass(); ptd_loleak_LF_1.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_1.setLowLimitToPass(); ptd_imgrej_LF_1.setHighLimitToPass(); ptd_imgrej_LF_1.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 2:
                        ptd_pwr_LF_2.setLowLimitToPass(); ptd_pwr_LF_2.setHighLimitToPass(); ptd_pwr_LF_2.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_2.setLowLimitToPass(); ptd_freq_LF_2.setHighLimitToPass(); ptd_freq_LF_2.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_2.setLowLimitToPass(); ptd_loleak_LF_2.setHighLimitToPass(); ptd_loleak_LF_2.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_2.setLowLimitToPass(); ptd_imgrej_LF_2.setHighLimitToPass(); ptd_imgrej_LF_2.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 3:
                        ptd_pwr_LF_3.setLowLimitToPass(); ptd_pwr_LF_3.setHighLimitToPass(); ptd_pwr_LF_3.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_3.setLowLimitToPass(); ptd_freq_LF_3.setHighLimitToPass(); ptd_freq_LF_3.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_3.setLowLimitToPass(); ptd_loleak_LF_3.setHighLimitToPass(); ptd_loleak_LF_3.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_3.setLowLimitToPass(); ptd_imgrej_LF_3.setHighLimitToPass(); ptd_imgrej_LF_3.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 4:
                        ptd_pwr_LF_4.setLowLimitToPass(); ptd_pwr_LF_4.setHighLimitToPass(); ptd_pwr_LF_4.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_4.setLowLimitToPass(); ptd_freq_LF_4.setHighLimitToPass(); ptd_freq_LF_4.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_4.setLowLimitToPass(); ptd_loleak_LF_4.setHighLimitToPass(); ptd_loleak_LF_4.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_4.setLowLimitToPass(); ptd_imgrej_LF_4.setHighLimitToPass(); ptd_imgrej_LF_4.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 5:
                        ptd_pwr_LF_5.setLowLimitToPass(); ptd_pwr_LF_5.setHighLimitToPass(); ptd_pwr_LF_5.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_5.setLowLimitToPass(); ptd_freq_LF_5.setHighLimitToPass(); ptd_freq_LF_5.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_5.setLowLimitToPass(); ptd_loleak_LF_5.setHighLimitToPass(); ptd_loleak_LF_5.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_5.setLowLimitToPass(); ptd_imgrej_LF_5.setHighLimitToPass(); ptd_imgrej_LF_5.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 6:
                        ptd_pwr_LF_6.setLowLimitToPass(); ptd_pwr_LF_6.setHighLimitToPass(); ptd_pwr_LF_6.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_6.setLowLimitToPass(); ptd_freq_LF_6.setHighLimitToPass(); ptd_freq_LF_6.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_6.setLowLimitToPass(); ptd_loleak_LF_6.setHighLimitToPass(); ptd_loleak_LF_6.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_6.setLowLimitToPass(); ptd_imgrej_LF_6.setHighLimitToPass(); ptd_imgrej_LF_6.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 7:
                        ptd_pwr_LF_7.setLowLimitToPass(); ptd_pwr_LF_7.setHighLimitToPass(); ptd_pwr_LF_7.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_7.setLowLimitToPass(); ptd_freq_LF_7.setHighLimitToPass(); ptd_freq_LF_7.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_7.setLowLimitToPass(); ptd_loleak_LF_7.setHighLimitToPass(); ptd_loleak_LF_7.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_7.setLowLimitToPass(); ptd_imgrej_LF_7.setHighLimitToPass(); ptd_imgrej_LF_7.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 8:
                        ptd_pwr_LF_8.setLowLimitToPass(); ptd_pwr_LF_8.setHighLimitToPass(); ptd_pwr_LF_8.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_8.setLowLimitToPass(); ptd_freq_LF_8.setHighLimitToPass(); ptd_freq_LF_8.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_8.setLowLimitToPass(); ptd_loleak_LF_8.setHighLimitToPass(); ptd_loleak_LF_8.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_8.setLowLimitToPass(); ptd_imgrej_LF_8.setHighLimitToPass(); ptd_imgrej_LF_8.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 9:
                        ptd_pwr_LF_9.setLowLimitToPass(); ptd_pwr_LF_9.setHighLimitToPass(); ptd_pwr_LF_9.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_9.setLowLimitToPass(); ptd_freq_LF_9.setHighLimitToPass(); ptd_freq_LF_9.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_9.setLowLimitToPass(); ptd_loleak_LF_9.setHighLimitToPass(); ptd_loleak_LF_9.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_9.setLowLimitToPass(); ptd_imgrej_LF_9.setHighLimitToPass(); ptd_imgrej_LF_9.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 10:
                        ptd_pwr_LF_a.setLowLimitToPass(); ptd_pwr_LF_a.setHighLimitToPass(); ptd_pwr_LF_a.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_a.setLowLimitToPass(); ptd_freq_LF_a.setHighLimitToPass(); ptd_freq_LF_a.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_a.setLowLimitToPass(); ptd_loleak_LF_a.setHighLimitToPass(); ptd_loleak_LF_a.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_a.setLowLimitToPass(); ptd_imgrej_LF_a.setHighLimitToPass(); ptd_imgrej_LF_a.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 11:
                        ptd_pwr_LF_b.setLowLimitToPass(); ptd_pwr_LF_b.setHighLimitToPass(); ptd_pwr_LF_b.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_b.setLowLimitToPass(); ptd_freq_LF_b.setHighLimitToPass(); ptd_freq_LF_b.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_b.setLowLimitToPass(); ptd_loleak_LF_b.setHighLimitToPass(); ptd_loleak_LF_b.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_b.setLowLimitToPass(); ptd_imgrej_LF_b.setHighLimitToPass(); ptd_imgrej_LF_b.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 12:
                        ptd_pwr_LF_c.setLowLimitToPass(); ptd_pwr_LF_c.setHighLimitToPass(); ptd_pwr_LF_c.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_c.setLowLimitToPass(); ptd_freq_LF_c.setHighLimitToPass(); ptd_freq_LF_c.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_c.setLowLimitToPass(); ptd_loleak_LF_c.setHighLimitToPass(); ptd_loleak_LF_c.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_c.setLowLimitToPass(); ptd_imgrej_LF_c.setHighLimitToPass(); ptd_imgrej_LF_c.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 13:
                        ptd_pwr_LF_d.setLowLimitToPass(); ptd_pwr_LF_d.setHighLimitToPass(); ptd_pwr_LF_d.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_d.setLowLimitToPass(); ptd_freq_LF_d.setHighLimitToPass(); ptd_freq_LF_d.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_d.setLowLimitToPass(); ptd_loleak_LF_d.setHighLimitToPass(); ptd_loleak_LF_d.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_d.setLowLimitToPass(); ptd_imgrej_LF_d.setHighLimitToPass(); ptd_imgrej_LF_d.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 14:
                        ptd_pwr_LF_e.setLowLimitToPass(); ptd_pwr_LF_e.setHighLimitToPass(); ptd_pwr_LF_e.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_e.setLowLimitToPass(); ptd_freq_LF_e.setHighLimitToPass(); ptd_freq_LF_e.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_e.setLowLimitToPass(); ptd_loleak_LF_e.setHighLimitToPass(); ptd_loleak_LF_e.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_e.setLowLimitToPass(); ptd_imgrej_LF_e.setHighLimitToPass(); ptd_imgrej_LF_e.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 15:
                        ptd_pwr_LF_f.setLowLimitToPass(); ptd_pwr_LF_f.setHighLimitToPass(); ptd_pwr_LF_f.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_LF_f.setLowLimitToPass(); ptd_freq_LF_f.setHighLimitToPass(); ptd_freq_LF_f.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_LF_f.setLowLimitToPass(); ptd_loleak_LF_f.setHighLimitToPass(); ptd_loleak_LF_f.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_LF_f.setLowLimitToPass(); ptd_imgrej_LF_f.setHighLimitToPass(); ptd_imgrej_LF_f.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;

                    default:
                        break;
                    }
                    break;

                case "freq_2441MHz":
                    switch (cfg) {
                    case 0:
                        ptd_pwr_MF_0.setLowLimitToPass(); ptd_pwr_MF_0.setHighLimitToPass(); ptd_pwr_MF_0.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_0.setLowLimitToPass(); ptd_freq_MF_0.setHighLimitToPass(); ptd_freq_MF_0.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_0.setLowLimitToPass(); ptd_loleak_MF_0.setHighLimitToPass(); ptd_loleak_MF_0.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_0.setLowLimitToPass(); ptd_imgrej_MF_0.setHighLimitToPass(); ptd_imgrej_MF_0.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 1:
                        ptd_pwr_MF_1.setLowLimitToPass(); ptd_pwr_MF_1.setHighLimitToPass(); ptd_pwr_MF_1.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_1.setLowLimitToPass(); ptd_freq_MF_1.setHighLimitToPass(); ptd_freq_MF_1.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_1.setLowLimitToPass(); ptd_loleak_MF_1.setHighLimitToPass(); ptd_loleak_MF_1.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_1.setLowLimitToPass(); ptd_imgrej_MF_1.setHighLimitToPass(); ptd_imgrej_MF_1.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 2:
                        ptd_pwr_MF_2.setLowLimitToPass(); ptd_pwr_MF_2.setHighLimitToPass(); ptd_pwr_MF_2.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_2.setLowLimitToPass(); ptd_freq_MF_2.setHighLimitToPass(); ptd_freq_MF_2.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_2.setLowLimitToPass(); ptd_loleak_MF_2.setHighLimitToPass(); ptd_loleak_MF_2.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_2.setLowLimitToPass(); ptd_imgrej_MF_2.setHighLimitToPass(); ptd_imgrej_MF_2.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 3:
                        ptd_pwr_MF_3.setLowLimitToPass(); ptd_pwr_MF_3.setHighLimitToPass(); ptd_pwr_MF_3.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_3.setLowLimitToPass(); ptd_freq_MF_3.setHighLimitToPass(); ptd_freq_MF_3.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_3.setLowLimitToPass(); ptd_loleak_MF_3.setHighLimitToPass(); ptd_loleak_MF_3.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_3.setLowLimitToPass(); ptd_imgrej_MF_3.setHighLimitToPass(); ptd_imgrej_MF_3.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 4:
                        ptd_pwr_MF_4.setLowLimitToPass(); ptd_pwr_MF_4.setHighLimitToPass(); ptd_pwr_MF_4.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_4.setLowLimitToPass(); ptd_freq_MF_4.setHighLimitToPass(); ptd_freq_MF_4.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_4.setLowLimitToPass(); ptd_loleak_MF_4.setHighLimitToPass(); ptd_loleak_MF_4.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_4.setLowLimitToPass(); ptd_imgrej_MF_4.setHighLimitToPass(); ptd_imgrej_MF_4.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 5:
                        ptd_pwr_MF_5.setLowLimitToPass(); ptd_pwr_MF_5.setHighLimitToPass(); ptd_pwr_MF_5.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_5.setLowLimitToPass(); ptd_freq_MF_5.setHighLimitToPass(); ptd_freq_MF_5.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_5.setLowLimitToPass(); ptd_loleak_MF_5.setHighLimitToPass(); ptd_loleak_MF_5.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_5.setLowLimitToPass(); ptd_imgrej_MF_5.setHighLimitToPass(); ptd_imgrej_MF_5.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 6:
                        ptd_pwr_MF_6.setLowLimitToPass(); ptd_pwr_MF_6.setHighLimitToPass(); ptd_pwr_MF_6.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_6.setLowLimitToPass(); ptd_freq_MF_6.setHighLimitToPass(); ptd_freq_MF_6.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_6.setLowLimitToPass(); ptd_loleak_MF_6.setHighLimitToPass(); ptd_loleak_MF_6.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_6.setLowLimitToPass(); ptd_imgrej_MF_6.setHighLimitToPass(); ptd_imgrej_MF_6.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 7:
                        ptd_pwr_MF_7.setLowLimitToPass(); ptd_pwr_MF_7.setHighLimitToPass(); ptd_pwr_MF_7.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_7.setLowLimitToPass(); ptd_freq_MF_7.setHighLimitToPass(); ptd_freq_MF_7.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_7.setLowLimitToPass(); ptd_loleak_MF_7.setHighLimitToPass(); ptd_loleak_MF_7.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_7.setLowLimitToPass(); ptd_imgrej_MF_7.setHighLimitToPass(); ptd_imgrej_MF_7.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 8:
                        ptd_pwr_MF_8.setLowLimitToPass(); ptd_pwr_MF_8.setHighLimitToPass(); ptd_pwr_MF_8.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_8.setLowLimitToPass(); ptd_freq_MF_8.setHighLimitToPass(); ptd_freq_MF_8.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_8.setLowLimitToPass(); ptd_loleak_MF_8.setHighLimitToPass(); ptd_loleak_MF_8.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_8.setLowLimitToPass(); ptd_imgrej_MF_8.setHighLimitToPass(); ptd_imgrej_MF_8.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 9:
                        ptd_pwr_MF_9.setLowLimitToPass(); ptd_pwr_MF_9.setHighLimitToPass(); ptd_pwr_MF_9.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_9.setLowLimitToPass(); ptd_freq_MF_9.setHighLimitToPass(); ptd_freq_MF_9.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_9.setLowLimitToPass(); ptd_loleak_MF_9.setHighLimitToPass(); ptd_loleak_MF_9.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_9.setLowLimitToPass(); ptd_imgrej_MF_9.setHighLimitToPass(); ptd_imgrej_MF_9.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 10:
                        ptd_pwr_MF_a.setLowLimitToPass(); ptd_pwr_MF_a.setHighLimitToPass(); ptd_pwr_MF_a.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_a.setLowLimitToPass(); ptd_freq_MF_a.setHighLimitToPass(); ptd_freq_MF_a.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_a.setLowLimitToPass(); ptd_loleak_MF_a.setHighLimitToPass(); ptd_loleak_MF_a.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_a.setLowLimitToPass(); ptd_imgrej_MF_a.setHighLimitToPass(); ptd_imgrej_MF_a.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 11:
                        ptd_pwr_MF_b.setLowLimitToPass(); ptd_pwr_MF_b.setHighLimitToPass(); ptd_pwr_MF_b.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_b.setLowLimitToPass(); ptd_freq_MF_b.setHighLimitToPass(); ptd_freq_MF_b.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_b.setLowLimitToPass(); ptd_loleak_MF_b.setHighLimitToPass(); ptd_loleak_MF_b.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_b.setLowLimitToPass(); ptd_imgrej_MF_b.setHighLimitToPass(); ptd_imgrej_MF_b.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 12:
                        ptd_pwr_MF_c.setLowLimitToPass(); ptd_pwr_MF_c.setHighLimitToPass(); ptd_pwr_MF_c.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_c.setLowLimitToPass(); ptd_freq_MF_c.setHighLimitToPass(); ptd_freq_MF_c.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_c.setLowLimitToPass(); ptd_loleak_MF_c.setHighLimitToPass(); ptd_loleak_MF_c.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_c.setLowLimitToPass(); ptd_imgrej_MF_c.setHighLimitToPass(); ptd_imgrej_MF_c.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 13:
                        ptd_pwr_MF_d.setLowLimitToPass(); ptd_pwr_MF_d.setHighLimitToPass(); ptd_pwr_MF_d.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_d.setLowLimitToPass(); ptd_freq_MF_d.setHighLimitToPass(); ptd_freq_MF_d.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_d.setLowLimitToPass(); ptd_loleak_MF_d.setHighLimitToPass(); ptd_loleak_MF_d.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_d.setLowLimitToPass(); ptd_imgrej_MF_d.setHighLimitToPass(); ptd_imgrej_MF_d.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 14:
                        ptd_pwr_MF_e.setLowLimitToPass(); ptd_pwr_MF_e.setHighLimitToPass(); ptd_pwr_MF_e.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_e.setLowLimitToPass(); ptd_freq_MF_e.setHighLimitToPass(); ptd_freq_MF_e.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_e.setLowLimitToPass(); ptd_loleak_MF_e.setHighLimitToPass(); ptd_loleak_MF_e.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_e.setLowLimitToPass(); ptd_imgrej_MF_e.setHighLimitToPass(); ptd_imgrej_MF_e.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 15:
                        ptd_pwr_MF_f.setLowLimitToPass(); ptd_pwr_MF_f.setHighLimitToPass(); ptd_pwr_MF_f.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_MF_f.setLowLimitToPass(); ptd_freq_MF_f.setHighLimitToPass(); ptd_freq_MF_f.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_MF_f.setLowLimitToPass(); ptd_loleak_MF_f.setHighLimitToPass(); ptd_loleak_MF_f.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_MF_f.setLowLimitToPass(); ptd_imgrej_MF_f.setHighLimitToPass(); ptd_imgrej_MF_f.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;

                    default:
                        break;
                    }
                break;

                case "freq_2480MHz":
                    switch (cfg) {
                    case 0:
                        ptd_pwr_HF_0.setLowLimitToPass(); ptd_pwr_HF_0.setHighLimitToPass(); ptd_pwr_HF_0.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_0.setLowLimitToPass(); ptd_freq_HF_0.setHighLimitToPass(); ptd_freq_HF_0.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_0.setLowLimitToPass(); ptd_loleak_HF_0.setHighLimitToPass(); ptd_loleak_HF_0.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_0.setLowLimitToPass(); ptd_imgrej_HF_0.setHighLimitToPass(); ptd_imgrej_HF_0.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 1:
                        ptd_pwr_HF_1.setLowLimitToPass(); ptd_pwr_HF_1.setHighLimitToPass(); ptd_pwr_HF_1.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_1.setLowLimitToPass(); ptd_freq_HF_1.setHighLimitToPass(); ptd_freq_HF_1.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_1.setLowLimitToPass(); ptd_loleak_HF_1.setHighLimitToPass(); ptd_loleak_HF_1.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_1.setLowLimitToPass(); ptd_imgrej_HF_1.setHighLimitToPass(); ptd_imgrej_HF_1.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 2:
                        ptd_pwr_HF_2.setLowLimitToPass(); ptd_pwr_HF_2.setHighLimitToPass(); ptd_pwr_HF_2.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_2.setLowLimitToPass(); ptd_freq_HF_2.setHighLimitToPass(); ptd_freq_HF_2.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_2.setLowLimitToPass(); ptd_loleak_HF_2.setHighLimitToPass(); ptd_loleak_HF_2.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_2.setLowLimitToPass(); ptd_imgrej_HF_2.setHighLimitToPass(); ptd_imgrej_HF_2.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 3:
                        ptd_pwr_HF_3.setLowLimitToPass(); ptd_pwr_HF_3.setHighLimitToPass(); ptd_pwr_HF_3.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_3.setLowLimitToPass(); ptd_freq_HF_3.setHighLimitToPass(); ptd_freq_HF_3.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_3.setLowLimitToPass(); ptd_loleak_HF_3.setHighLimitToPass(); ptd_loleak_HF_3.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_3.setLowLimitToPass(); ptd_imgrej_HF_3.setHighLimitToPass(); ptd_imgrej_HF_3.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 4:
                        ptd_pwr_HF_4.setLowLimitToPass(); ptd_pwr_HF_4.setHighLimitToPass(); ptd_pwr_HF_4.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_4.setLowLimitToPass(); ptd_freq_HF_4.setHighLimitToPass(); ptd_freq_HF_4.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_4.setLowLimitToPass(); ptd_loleak_HF_4.setHighLimitToPass(); ptd_loleak_HF_4.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_4.setLowLimitToPass(); ptd_imgrej_HF_4.setHighLimitToPass(); ptd_imgrej_HF_4.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 5:
                        ptd_pwr_HF_5.setLowLimitToPass(); ptd_pwr_HF_5.setHighLimitToPass(); ptd_pwr_HF_5.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_5.setLowLimitToPass(); ptd_freq_HF_5.setHighLimitToPass(); ptd_freq_HF_5.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_5.setLowLimitToPass(); ptd_loleak_HF_5.setHighLimitToPass(); ptd_loleak_HF_5.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_5.setLowLimitToPass(); ptd_imgrej_HF_5.setHighLimitToPass(); ptd_imgrej_HF_5.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 6:
                        ptd_pwr_HF_6.setLowLimitToPass(); ptd_pwr_HF_6.setHighLimitToPass(); ptd_pwr_HF_6.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_6.setLowLimitToPass(); ptd_freq_HF_6.setHighLimitToPass(); ptd_freq_HF_6.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_6.setLowLimitToPass(); ptd_loleak_HF_6.setHighLimitToPass(); ptd_loleak_HF_6.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_6.setLowLimitToPass(); ptd_imgrej_HF_6.setHighLimitToPass(); ptd_imgrej_HF_6.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 7:
                        ptd_pwr_HF_7.setLowLimitToPass(); ptd_pwr_HF_7.setHighLimitToPass(); ptd_pwr_HF_7.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_7.setLowLimitToPass(); ptd_freq_HF_7.setHighLimitToPass(); ptd_freq_HF_7.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_7.setLowLimitToPass(); ptd_loleak_HF_7.setHighLimitToPass(); ptd_loleak_HF_7.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_7.setLowLimitToPass(); ptd_imgrej_HF_7.setHighLimitToPass(); ptd_imgrej_HF_7.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 8:
                        ptd_pwr_HF_8.setLowLimitToPass(); ptd_pwr_HF_8.setHighLimitToPass(); ptd_pwr_HF_8.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_8.setLowLimitToPass(); ptd_freq_HF_8.setHighLimitToPass(); ptd_freq_HF_8.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_8.setLowLimitToPass(); ptd_loleak_HF_8.setHighLimitToPass(); ptd_loleak_HF_8.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_8.setLowLimitToPass(); ptd_imgrej_HF_8.setHighLimitToPass(); ptd_imgrej_HF_8.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 9:
                        ptd_pwr_HF_9.setLowLimitToPass(); ptd_pwr_HF_9.setHighLimitToPass(); ptd_pwr_HF_9.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_9.setLowLimitToPass(); ptd_freq_HF_9.setHighLimitToPass(); ptd_freq_HF_9.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_9.setLowLimitToPass(); ptd_loleak_HF_9.setHighLimitToPass(); ptd_loleak_HF_9.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_9.setLowLimitToPass(); ptd_imgrej_HF_9.setHighLimitToPass(); ptd_imgrej_HF_9.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 10:
                        ptd_pwr_HF_a.setLowLimitToPass(); ptd_pwr_HF_a.setHighLimitToPass(); ptd_pwr_HF_a.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_a.setLowLimitToPass(); ptd_freq_HF_a.setHighLimitToPass(); ptd_freq_HF_a.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_a.setLowLimitToPass(); ptd_loleak_HF_a.setHighLimitToPass(); ptd_loleak_HF_a.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_a.setLowLimitToPass(); ptd_imgrej_HF_a.setHighLimitToPass(); ptd_imgrej_HF_a.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 11:
                        ptd_pwr_HF_b.setLowLimitToPass(); ptd_pwr_HF_b.setHighLimitToPass(); ptd_pwr_HF_b.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_b.setLowLimitToPass(); ptd_freq_HF_b.setHighLimitToPass(); ptd_freq_HF_b.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_b.setLowLimitToPass(); ptd_loleak_HF_b.setHighLimitToPass(); ptd_loleak_HF_b.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_b.setLowLimitToPass(); ptd_imgrej_HF_b.setHighLimitToPass(); ptd_imgrej_HF_b.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 12:
                        ptd_pwr_HF_c.setLowLimitToPass(); ptd_pwr_HF_c.setHighLimitToPass(); ptd_pwr_HF_c.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_c.setLowLimitToPass(); ptd_freq_HF_c.setHighLimitToPass(); ptd_freq_HF_c.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_c.setLowLimitToPass(); ptd_loleak_HF_c.setHighLimitToPass(); ptd_loleak_HF_c.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_c.setLowLimitToPass(); ptd_imgrej_HF_c.setHighLimitToPass(); ptd_imgrej_HF_c.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 13:
                        ptd_pwr_HF_d.setLowLimitToPass(); ptd_pwr_HF_d.setHighLimitToPass(); ptd_pwr_HF_d.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_d.setLowLimitToPass(); ptd_freq_HF_d.setHighLimitToPass(); ptd_freq_HF_d.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_d.setLowLimitToPass(); ptd_loleak_HF_d.setHighLimitToPass(); ptd_loleak_HF_d.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_d.setLowLimitToPass(); ptd_imgrej_HF_d.setHighLimitToPass(); ptd_imgrej_HF_d.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 14:
                        ptd_pwr_HF_e.setLowLimitToPass(); ptd_pwr_HF_e.setHighLimitToPass(); ptd_pwr_HF_e.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_e.setLowLimitToPass(); ptd_freq_HF_e.setHighLimitToPass(); ptd_freq_HF_e.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_e.setLowLimitToPass(); ptd_loleak_HF_e.setHighLimitToPass(); ptd_loleak_HF_e.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_e.setLowLimitToPass(); ptd_imgrej_HF_e.setHighLimitToPass(); ptd_imgrej_HF_e.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;
                    case 15:
                        ptd_pwr_HF_f.setLowLimitToPass(); ptd_pwr_HF_f.setHighLimitToPass(); ptd_pwr_HF_f.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_freq_HF_f.setLowLimitToPass(); ptd_freq_HF_f.setHighLimitToPass(); ptd_freq_HF_f.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_loleak_HF_f.setLowLimitToPass(); ptd_loleak_HF_f.setHighLimitToPass(); ptd_loleak_HF_f.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        ptd_imgrej_HF_f.setLowLimitToPass(); ptd_imgrej_HF_f.setHighLimitToPass(); ptd_imgrej_HF_f.evaluate(res_BTTX_2402[cfg].getRfPower_dBm());
                        break;

                    default:
                        break;
                    }
                    break;

                default:
                    break;
                }
            }
        }

    }
}
