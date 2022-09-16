package BT1502_pro_tml.BT;

import java.util.Map;

import BT1502_pro_tml.Global.StaticFields;
import besLib.cal.BesCalc_BTRX;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDcVI;
import xoc.dsa.ISetupDigitizer;
import xoc.dsa.ISetupDigitizer.IMeasureWaveform.SetupInputImpedance;
import xoc.dsa.ISetupRfStim;
import xoc.dsa.ISetupRfStim.ICw.SetupOptimization;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteDoubleArray;
import xoc.dta.datatypes.dsp.MultiSiteWaveDouble;
import xoc.dta.datatypes.dsp.MultiSiteWaveDoubleArray;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.resultaccess.IDigitizerResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class BT_RX_LOFreq_Dyn extends TestMethod {

    @In public String spec_measurement;
    @In public double  rxPWR;
    @In public int  freq;
    @In public String rxMode;

    public IMeasurement measurement1;
    public IMeasurement measurement2;
    public IMeasurement measurementRly_Off;

    public IParametricTestDescriptor    ptd_PowSig_I,
                                        ptd_PowSig_Q,
                                        ptd_PowSig_IQ,
                                        ptd_RX_SNR_I,
                                        ptd_RX_SNR_Q,
                                        ptd_RX_SNR_IQ,
                                        ptd_RX_SNR_IMB,
                                        ptd_RX_IQ_IMB,
                                        ptd_RX_DC_IP,
                                        ptd_RX_DC_IN,
                                        ptd_RX_DC_QP,
                                        ptd_RX_DC_QN,
                                        ptd_RX_DC_IP_IN,
                                        ptd_RX_DC_QP_QN,
                                        ptd_FreqI,
                                        ptd_FreqQ;
    //Pin Map
    private String RF_IN = "BT_RF";
    private String dgt_Qp = "DGT_QP";
    private String dgt_Qn = "DGT_QN";
    private String dgt_Ip = "DGT_IP";
    private String dgt_In = "DGT_IN";

    @Override
    public void setup() {

        IDeviceSetup deviceSetup_rlyOff =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup_rlyOff.importSpec(spec_measurement);
        deviceSetup_rlyOff.addUtility("K7").setValue(0);
        measurementRly_Off.setSetups(deviceSetup_rlyOff);


        /************** measurement1 ****************/
        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        ds1.addUtility("K7").setValue(1);

        //CW setup
        double InLoss = 0.65; //Stimulus PathLoss
        double freq_deviation=5e5;//Hz
        ISetupRfStim setup_stim=ds1.addRfStim(RF_IN).setConfigModeSingleSource().setDisconnect(true);//.setConfigOptionSiteInterlacingOn()

        if (StaticFields.dev_name == "BGA7273") {
            if (freq == 2402) {
                setup_stim.cw("BT_RX_Stim").setOptimization(SetupOptimization.noiseFloor).setFrequency(2.4019875e9 - freq_deviation).setPower(rxPWR + InLoss);
            } else if (freq == 2441) {
                setup_stim.cw("BT_RX_Stim").setOptimization(SetupOptimization.noiseFloor).setFrequency(2.4409875e9 - freq_deviation).setPower(rxPWR + InLoss);
            } else if (freq == 2480) {
                setup_stim.cw("BT_RX_Stim").setOptimization(SetupOptimization.noiseFloor).setFrequency(2.4799875e9 - freq_deviation).setPower(rxPWR + InLoss);
            }

        }
        else if (StaticFields.dev_name == "WLCSP") {
            if (freq == 2402) {
                setup_stim.cw("BT_RX_Stim").setOptimization(SetupOptimization.noiseFloor).setFrequency(2.401905e9 - freq_deviation).setPower(rxPWR + InLoss);
            } else if (freq == 2441) {
                setup_stim.cw("BT_RX_Stim").setOptimization(SetupOptimization.noiseFloor).setFrequency(2.440905e9 - freq_deviation).setPower(rxPWR + InLoss);
            } else if (freq == 2480) {
                setup_stim.cw("BT_RX_Stim").setOptimization(SetupOptimization.noiseFloor).setFrequency(2.479905e9 - freq_deviation).setPower(rxPWR + InLoss);
            }
        }
        //Standard DGT setup
        double dgtInputRange   = 0.8 ;          // [V]  0.3Vpp
        double dgtDcOffset     = 0.0 ;          // [V]
        double dgtSamplingRate = 10.22*1e6*4;   // [Msps] modified in 20210716
        int    dgtNumOfSamples = 2048*8 ;       // Modified in 20210716
        double dgtLpfValue     = 15*1e6;        // [Hz]//50M 15*1e6
//        //DGT Digital Fileter setup
//        double dgtCutOffFreq1  = 1.5*1e6;       // [Hz]
//        int    dgtNumFilterTap = 32;
        //Digitizer setup
        ISetupDigitizer digSetup = ds1.addDigitizer(dgt_Qp+"+"+dgt_Ip).setConfigOptionDifferential().setConnect(true);
        digSetup.measureWaveform("BT_RX_Meas")
            .setExpectedAmplitude(dgtInputRange)
            .setOffset(dgtDcOffset)
            .setSampleRate(dgtSamplingRate)
            .setSamples(dgtNumOfSamples)
            .setLowPassFilterBandwidth(dgtLpfValue)
            .setInputImpedance(SetupInputImpedance.R100kOhm)//10k
            .setCenterGnd(true);

        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement1, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        ds1.parallelBegin();
            //parallel seq1
            ds1.sequentialBegin();
                ds1.actionCall("BT_RX_Stim");
            ds1.sequentialEnd();

            //parallel seq2
            ds1.sequentialBegin();
                i2c.transactionSequenceBegin("BT_RX_ADDA_TEST");
                if(rxMode=="Passive")
                {
//                    [kong bin]
                            i2c.write(BesI2cAddrType.RF,0x55,0x0000);
                            i2c.write(BesI2cAddrType.RF,0x5c,0xa1b0);
                            i2c.write(BesI2cAddrType.RF,0x5d,0x0c34);
                            i2c.write(BesI2cAddrType.RF,0x9c,0x0041);
                            i2c.write(BesI2cAddrType.RF,0x9d,0x0404);
                            i2c.write(BesI2cAddrType.RF,0x9f,0x0100);
                            i2c.write(BesI2cAddrType.RF,0xa0,0x1908);
                            i2c.write(BesI2cAddrType.RF,0xa2,0x468c);
                            i2c.write(BesI2cAddrType.RF,0xc4,0x3c81);
                            i2c.write(BesI2cAddrType.RF,0x00,0xa010);
                            i2c.write(BesI2cAddrType.RF,0x18,0x000f);//0705: 0x000d -> 0x000f
                            i2c.write(BesI2cAddrType.RF,0x48,0x0041);
                            i2c.write(BesI2cAddrType.RF,0x00,0xa000);

//                            [addaloop]
                            i2c.write(BesI2cAddrType.RF,0x51,0x67BF);//lna passive
                            i2c.write(BesI2cAddrType.RF,0xbf,0x3002);//lna ic
                            i2c.write(BesI2cAddrType.RF,0xce,0x00b5);//lna_ldo
                            i2c.write(BesI2cAddrType.RF,0x2c,0x26f7);// i2v max gain    20220707 261f -> 26f7
                            i2c.write(BesI2cAddrType.RF,0x28,0xbe10);//i2v_corner
                            i2c.write(BesI2cAddrType.RF,0x2b,0x0900);//flt max gain
                            i2c.write(BesI2cAddrType.RF,0x27,0x7604);//flt bw        20220707 1e04 -> 7604
                            i2c.write(BesI2cAddrType.RF,0x81,0x0009);//i2v rin
                            i2c.write(BesI2cAddrType.RF,0x4a,0x0000);//zero if
                            i2c.write(BesI2cAddrType.RF,0x0f,0x0001);//pa pd
                            i2c.write(BesI2cAddrType.RF,0x11,0x4040);//pad pd
                            i2c.write(BesI2cAddrType.RF,0x91,0x8608);//txflt tst en
                            i2c.write(BesI2cAddrType.RF,0xa6,0x5eea);//0x1eea adda 0x1e6a filter  20220707 1eea -> 5eea
                            i2c.write(BesI2cAddrType.RF,0x95,0x0032);
                            i2c.waitTime(10e6);

//                                //[addaloop]
//                                i2c.write(BesI2cAddrType.RF,0x0051,0x27ff);//lna hg
//                                i2c.write(BesI2cAddrType.RF,0x00bf,0x3002);//lna ic
//                                i2c.write(BesI2cAddrType.RF,0x00ce,0x00b5);//lna_ldo
//                                i2c.write(BesI2cAddrType.RF,0x002c,0x261F);// i2v max gain
//                                i2c.write(BesI2cAddrType.RF,0x0028,0xbe10);//i2v_corner
//                                i2c.write(BesI2cAddrType.RF,0x002b,0x0f00);//flt max gain
//                                i2c.write(BesI2cAddrType.RF,0x0027,0x1e04);//flt bw
//                                i2c.write(BesI2cAddrType.RF,0x0081,0x0009);//i2v rin
//                                i2c.write(BesI2cAddrType.RF,0x004a,0x0000);//zero if
//                                i2c.write(BesI2cAddrType.RF,0x000f,0x0001);//pa pd
//                                i2c.write(BesI2cAddrType.RF,0x0011,0x4040);//pad pd
//                                i2c.write(BesI2cAddrType.RF,0x0091,0x8608);//txflt tst en
//                                i2c.write(BesI2cAddrType.RF,0x0027,0x7604);
//                        //        i2c.write(BesI2cAddrType.RF,0x00,0xa010);
//                                i2c.write(BesI2cAddrType.RF,0x0144,0x5025);
//                                i2c.write(BesI2cAddrType.RF,0x0146,0x401b);
//                        //        i2c.write(BesI2cAddrType.RF,0x00,0xa000);
//                                i2c.write(BesI2cAddrType.RF,0x00a6,0x1eea);
//                                i2c.write(BesI2cAddrType.RF,0x0095,0x0032);
//                                i2c.waitTime(1000e6);

                                //[core init nansu]
                                i2c.write(BesI2cAddrType.DIGITAL,0x40085038,0xCAFE0000);
                                i2c.write(BesI2cAddrType.DIGITAL,0x400800CC,0x000000DD);
                                i2c.write(BesI2cAddrType.DIGITAL,0x40080004,0x13e8018c);
                                i2c.write(BesI2cAddrType.DIGITAL,0x4008004C,0x048C151F);
                                i2c.waitTime(10e6);
                        //        //[ss]
                                i2c.write(BesI2cAddrType.DIGITAL,0xD0350214,0x00000000);
                                i2c.waitTime(10e6);

                                //[adda loop back]
                                i2c.write(BesI2cAddrType.DIGITAL,0xd0350218,0x00000013);
                                i2c.write(BesI2cAddrType.DIGITAL,0xd0350254,0x0000000A);
                                i2c.write(BesI2cAddrType.DIGITAL,0xd0350024,0x0000000B);
                                i2c.write(BesI2cAddrType.DIGITAL,0xd0350028,0x00800000);
                                i2c.write(BesI2cAddrType.DIGITAL,0xd0350244,0x175E0000);
                                i2c.write(BesI2cAddrType.DIGITAL,0xd0350240,0x0000A825);
                                i2c.waitTime(100e6);
                }
                if(freq==2402)
                    {
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x0); //turn off rx
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x800A002E);//2448
                        i2c.waitTime(0.5e6);
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x0);
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x800A0000);
                        i2c.waitTime(0.5e6);
                    }
                    else if(freq==2441)
                    {
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x0); //turn off rx
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x800A002E);//2448
                        i2c.waitTime(0.5e6);
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x0);
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x800A0027);
                        i2c.waitTime(0.5e6);
                    }
                    else if(freq==2448)
                    {
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x0); //turn off rx
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x800A0027);//2441
                        i2c.waitTime(0.5e6);
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x0); //turn off rx
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x800A002E);//2448
                        i2c.waitTime(0.5e6);
                    }
                    else if(freq==2480)//2480
                    {
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x0); //turn off rx
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x800A002E);//2448
                        i2c.waitTime(0.5e6);
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x0);
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x800A004e);
                        i2c.waitTime(0.5e6);
                    }
                    else
                    {
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x0); //turn off rx
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x800A002E);//2448
                        i2c.waitTime(0.5e6);
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x0);
                        i2c.write(BesI2cAddrType.DIGITAL,0xD0220C00,0x800A0000);
                        i2c.waitTime(0.5e6);
                    }
                i2c.transactionSequenceEnd();
    //            ds1.waitCall("10 ms");
                ds1.actionCall("BT_RX_Meas");
            ds1.sequentialEnd();

        ds1.parallelEnd();

        measurement1.setSetups(ds1);


        IDeviceSetup ds2 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds2.importSpec(spec_measurement);
        //WSMX PMU IFVM
        ISetupDcVI dcVi_DGT= ds2.addDcVI(dgt_Qp+"+"+dgt_Qn+"+"+dgt_Ip+"+"+dgt_In).setConnect(true);
        dcVi_DGT.iforce("dgtIF").setForceValue("0 uA").setIrange("10 uA").setVclampHigh("2.5 V").setVclampLow("-1.0 V");
        dcVi_DGT.vmeas("dgtVM").setWaitTime("1 ms");

        ds2.sequentialBegin();
            ds2.actionCall("dgtIF");
            ds2.waitCall("10 ms");
            ds2.actionCall("dgtVM");
        ds2.sequentialEnd();
        measurement2.setSetups(ds2);
    }


    @Override
    public void execute() {

        measurement1.execute();
        IDigitizerResults res1_DGT = measurement1.digitizer(dgt_Qp+"+"+dgt_Ip).preserveResults();
        Map<String, MultiSiteWaveDoubleArray> waveforms = res1_DGT.measureWaveform("BT_RX_Meas").getWaveform();
        MultiSiteWaveDoubleArray capWaveArrayI = waveforms.get(dgt_Ip);
        MultiSiteWaveDoubleArray capWaveArrayQ = waveforms.get(dgt_Qp);
        MultiSiteWaveDouble capWaveI = capWaveArrayI.getElement(0).setTermination(50);
        MultiSiteWaveDouble capWaveQ = capWaveArrayQ.getElement(0).setTermination(50);

        measurement2.execute();
        IDcVIResults res1_DcVI = measurement2.dcVI(dgt_Qp+"+"+dgt_Qn+"+"+dgt_Ip+"+"+dgt_In).preserveResults();
        Map<String, MultiSiteDoubleArray> voltageValue =  res1_DcVI.vmeas("dgtVM").getVoltage();
        MultiSiteDouble DC_IP = voltageValue.get(dgt_Ip).getElement(0);
        MultiSiteDouble DC_IN = voltageValue.get(dgt_In).getElement(0);
        MultiSiteDouble DC_QP = voltageValue.get(dgt_Qp).getElement(0);
        MultiSiteDouble DC_QN = voltageValue.get(dgt_Qn).getElement(0);

        measurementRly_Off.execute();

        BesCalc_BTRX bt_rx=new BesCalc_BTRX(capWaveQ, capWaveI, 8, 0.15e6, 0.65e6);

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            capWaveI.plot(testSuiteName+"_I");
            capWaveQ.plot(testSuiteName+"_Q");

            println("PowSig_I = "+bt_rx.getRXPower_I_dBm());
            println("PowSig_Q = "+bt_rx.getRXPower_Q_dBm());
            println("PowSig_IQ = "+bt_rx.getRXPower_IQ_dBm());

            println("RX_SNR_I = "+bt_rx.getSNR_I());
            println("RX_SNR_Q = "+bt_rx.getSNR_Q());
//            println("RX_SNR_IQ = "+bt_rx.getSNR_IQ());
//            println("RX_IQ_IMB = "+bt_rx.getRXPower_IQ_IMB());

            println("RX_Freq_I[KHz] = "+bt_rx.getFrequency_I().divide(1e3));
            println("RX_Freq_Q[KHz] = "+bt_rx.getFrequency_Q().divide(1e3));

            println("RX_DC_IP = "+DC_IP);
            println("RX_DC_IN = "+DC_IN);
            println("RX_DC_QP = "+DC_QP);
            println("RX_DC_QN = "+DC_QN);
            println("RX_DC_IP_IN = "+DC_IP.subtract(DC_IN).abs());
            println("RX_DC_QP_QN = "+DC_QP.subtract(DC_QN).abs());

            println();
        }

        if(rxMode=="Active") {
            ptd_RX_DC_IP.evaluate(DC_IP);
            ptd_RX_DC_IN.evaluate(DC_IN);
            ptd_RX_DC_QP.evaluate(DC_QP);
            ptd_RX_DC_QN.evaluate(DC_QN);
            ptd_RX_DC_IP_IN.evaluate(DC_IP.subtract(DC_IN).abs());
            ptd_RX_DC_QP_QN.evaluate(DC_QP.subtract(DC_QN).abs());

            ptd_PowSig_IQ.evaluate(bt_rx.getRXPower_IQ_dBm());
            ptd_RX_IQ_IMB.evaluate(bt_rx.getRXPower_IQ_IMB());

            ptd_RX_SNR_IQ.evaluate(bt_rx.getSNR_IQ());
            ptd_RX_SNR_IMB.evaluate(bt_rx.getSNR_I().subtract(bt_rx.getSNR_Q()));
        }


        ptd_PowSig_I.evaluate( bt_rx.getRXPower_I_dBm());
        ptd_PowSig_Q.evaluate( bt_rx.getRXPower_Q_dBm());

        ptd_RX_SNR_I.evaluate(bt_rx.getSNR_I());
        ptd_RX_SNR_Q.evaluate(bt_rx.getSNR_Q());

        ptd_FreqI.evaluate(bt_rx.getFrequency_I().divide(1e3));
        ptd_FreqQ.evaluate(bt_rx.getFrequency_Q().divide(1e3));

    }
}
