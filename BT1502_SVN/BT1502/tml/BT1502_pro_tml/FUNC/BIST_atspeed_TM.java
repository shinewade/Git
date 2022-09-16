package BT1502_pro_tml.FUNC;

import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import BT1502_pro_tml.Global.StaticFields;
import besLib.cal.BesCalc_DFT;
import besLib.dsa.BesDsa_DFT;
import besLib.dsa.BesDsa_DigitalCapture;
import besLib.dsa.BesDsa_DigitalCapture.CaptureEdge;
import besLib.dsa.BesDsa_PMIC;
import besLib.dsa.BesDsa_PMIC.V93K_Resource;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDcVI;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IFunctionalTestDescriptor;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class BIST_atspeed_TM extends TestMethod {

    @In
    public String spec_measurement;
    @In
    public String spec_measurement1;
    @In
    public String ComparePinList;
    @In
    public double period_ns;
    @In
    public double strobe_ns;

    public IMeasurement measurement;
    public IMeasurement measurement_Capture;
    public IMeasurement measurement_forceV,      measurement_CLK,  measurement_CLK_2,measurement_CLK_3,measurement_CLK_4;
    public IMeasurement measurement1;

    public IParametricTestDescriptor ptd_Bist_AtSpeed;
    public IFunctionalTestDescriptor ftd;
    private boolean dcdc_meas=false;//true: measure DCDC output


    @Override
    public void setup() {
        /************ measurement_ForceV ************/
        IDeviceSetup ds_ForceV = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds_ForceV.importSpec(spec_measurement);

        ISetupDcVI VCORE = ds_ForceV.addDcVI("VCORE_0p8").setConnect(true).setDisconnect(false);
        ISetupDcVI VANA = ds_ForceV.addDcVI("VANA").setConnect(true).setDisconnect(false);
        ISetupDcVI VCODEC = ds_ForceV.addDcVI("VCODEC").setConnect(true).setDisconnect(false);

        VCORE.vforce("force_0P9").setForceValue("1.1 V").setIclamp("100 mA");
        VCORE.level().setIrange("100 mA").setVrange("2 V");


        VANA.vforce("force_1p3").setForceValue("1.5 V").setIclamp("100 mA");
        VANA.level().setIrange("100 mA").setVrange("2 V");

        VCODEC.vforce("force_1p8").setForceValue("2 V").setIclamp("100 mA");
        VCODEC.level().setIrange("100 mA").setVrange("6 V");


        ds_ForceV.sequentialBegin();
        ds_ForceV.actionCall("force_0P9");
        ds_ForceV.actionCall("force_1p3");
        ds_ForceV.actionCall("force_1p8");
        ds_ForceV.sequentialEnd();

        measurement_forceV.setSetups(ds_ForceV);



        /************ measurement_Mode ************/
        IDeviceSetup ds_Mode = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds_Mode.importSpec(spec_measurement);

        BesPA_I2C i2c = new BesPA_I2C(ds_Mode, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");
        // operating sequence
        i2c.transactionSequenceBegin("BIST_atspeed");

        i2c.write(BesI2cAddrType.RF, 0x122,0X0700); //  0X122,0X0700£»
        i2c.write(BesI2cAddrType.RF, 0x115,0X0000); //  0X115,0X0000£»//freword_high
        i2c.write(BesI2cAddrType.RF, 0x114,0XD000); //  0X114,0XD000£»//freword_mid
        i2c.write(BesI2cAddrType.RF, 0x113,0X0000); //  0X113,0X0000£»//freword_low
        i2c.write(BesI2cAddrType.RF, 0x111,0X00D1); //  0X111,0X00D1£»//rstn=0
        i2c.write(BesI2cAddrType.RF, 0x111,0X00D3); //  0X111,0X00D3£»//rstn=1


                                                                  //
        i2c.waitTime(15e6);
        i2c.write(BesI2cAddrType.ANA,0x01AD, 0x0002);//
        i2c.write(BesI2cAddrType.ANA,0x016D, 0x8140);
        i2c.write(BesI2cAddrType.ANA,0x01A8, 0x6700);//
        i2c.write(BesI2cAddrType.ANA,0x01A2, 0x0000);//
        i2c.write(BesI2cAddrType.ANA,0x01A1, 0xD000);//
        i2c.write(BesI2cAddrType.ANA,0x01A0, 0x0000);//
        i2c.write(BesI2cAddrType.ANA,0x0198, 0x00D1);
        i2c.write(BesI2cAddrType.ANA,0x0198, 0x00D3);
        i2c.waitTime(15e6);





        i2c.write(BesI2cAddrType.RF, 0x55, 0x0000);
        i2c.write(BesI2cAddrType.RF, 0x5c, 0xa1b0);
        i2c.write(BesI2cAddrType.RF, 0x5d, 0x0c34);
        i2c.write(BesI2cAddrType.RF, 0x9c, 0x0041);
        i2c.write(BesI2cAddrType.RF, 0x9d, 0x0404);
        i2c.write(BesI2cAddrType.RF, 0x9f, 0x0100);
        i2c.write(BesI2cAddrType.RF, 0xa0, 0x1908);
        i2c.write(BesI2cAddrType.RF, 0xa2, 0x468c);
        i2c.write(BesI2cAddrType.RF, 0xc4, 0x3c81);
        i2c.write(BesI2cAddrType.RF, 0x118, 0x000d);
        i2c.write(BesI2cAddrType.RF, 0x148, 0x0041);
        i2c.write(BesI2cAddrType.RF, 0x51, 0x27ff);
        i2c.write(BesI2cAddrType.RF, 0xbf, 0x3002);    //0XBF,0X3002
        i2c.write(BesI2cAddrType.RF, 0xce, 0x00b5);
        i2c.write(BesI2cAddrType.RF, 0x2c, 0x265e);   //0x261F>>>0x265e
        i2c.write(BesI2cAddrType.RF, 0x28, 0xbe10);   //be10
        i2c.write(BesI2cAddrType.RF, 0x2b, 0x0f00);
        i2c.write(BesI2cAddrType.RF, 0x27, 0X7604);  //1e04>>>0X27 0X7604
        i2c.write(BesI2cAddrType.RF, 0x81, 0x0009);
        i2c.write(BesI2cAddrType.RF, 0x4a, 0x0000);
        i2c.write(BesI2cAddrType.RF, 0x0f, 0x0001);
        i2c.write(BesI2cAddrType.RF, 0x11, 0x4040);  //4040
        i2c.write(BesI2cAddrType.RF, 0x91, 0x8608);
        i2c.write(BesI2cAddrType.RF, 0xa6, 0x1eea);
        i2c.write(BesI2cAddrType.RF, 0x95, 0x0032);
        i2c.write(BesI2cAddrType.RF, 0X18, 0X3948);
        i2c.write(BesI2cAddrType.RF, 0XF, 0X3D);
        i2c.waitTime(15e6);

        i2c.write(BesI2cAddrType.RF, 0x002c, 0x265E);// txf gain  0X2C,0X245E --> 0X265E
        i2c.write(BesI2cAddrType.RF, 0x00cb, 0x00f0);// pad i
        i2c.write(BesI2cAddrType.RF, 0x00c1, 0x1b00);// pad gain
        i2c.write(BesI2cAddrType.RF, 0x0049, 0x9384);// ind v
        i2c.write(BesI2cAddrType.RF, 0x0147, 0x3055);// pa i
        i2c.write(BesI2cAddrType.RF, 0x00CC, 0x803f);// pa i
        i2c.write(BesI2cAddrType.RF, 0x00CA, 0x0F00);// pa bias
        i2c.write(BesI2cAddrType.RF, 0x0160, 0x068C);
        i2c.write(BesI2cAddrType.RF, 0x015C, 0x35F7);
        i2c.write(BesI2cAddrType.RF, 0x004C, 0x780F); // drv cap
        i2c.write(BesI2cAddrType.RF, 0x0051, 0x035e);
        i2c.write(BesI2cAddrType.RF, 0x00bf, 0X3002);   //1002>>>>3002
        i2c.write(BesI2cAddrType.RF, 0x00c5, 0x0043);
        i2c.write(BesI2cAddrType.RF, 0x0122, 0x0700);
        i2c.write(BesI2cAddrType.RF, 0x002b, 0x0e00);// flt max gain
        i2c.write(BesI2cAddrType.RF, 0x0081, 0x0008);// i2v rin
        i2c.write(BesI2cAddrType.RF, 0x0092, 0x8390);// tmx bias
        i2c.write(BesI2cAddrType.RF, 0x0027, 0x7604);// flt bw
        i2c.write(BesI2cAddrType.RF, 0x0146, 0x461b);// adc cplx
        i2c.write(BesI2cAddrType.RF, 0x00ce, 0x00B5);
        i2c.write(BesI2cAddrType.RF, 0x0028, 0xbe10);// i2v_corner 3e10>>>be10
        i2c.write(BesI2cAddrType.RF, 0x0090, 0x3808);// dwa
        i2c.write(BesI2cAddrType.RF, 0x0162, 0x1807);
        i2c.write(BesI2cAddrType.RF, 0x0041, 0x01cf);// rx0
        i2c.write(BesI2cAddrType.RF, 0x0042, 0x01cf);
        i2c.write(BesI2cAddrType.RF, 0x0043, 0x024f);
        i2c.write(BesI2cAddrType.RF, 0x0044, 0x01cc);
        i2c.write(BesI2cAddrType.RF, 0x0045, 0x01c4);
        i2c.write(BesI2cAddrType.RF, 0x0046, 0x05b4);
        i2c.write(BesI2cAddrType.RF, 0x0047, 0x0da4);
        i2c.write(BesI2cAddrType.RF, 0x0048, 0x0d84);
        i2c.write(BesI2cAddrType.RF, 0x0039, 0xe03f);// rx1
        i2c.write(BesI2cAddrType.RF, 0x003A, 0xe018);
        i2c.write(BesI2cAddrType.RF, 0x003B, 0x6048);
        i2c.write(BesI2cAddrType.RF, 0x003C, 0x6048);
        i2c.write(BesI2cAddrType.RF, 0x003D, 0x6088);
        i2c.write(BesI2cAddrType.RF, 0x003E, 0x6088);//
        i2c.write(BesI2cAddrType.RF, 0x003F, 0x6088);//
        i2c.write(BesI2cAddrType.RF, 0x0040, 0x6088);//
        i2c.write(BesI2cAddrType.RF, 0x012c, 0x003e);// rx2
        i2c.write(BesI2cAddrType.RF, 0x012d, 0x026f);//
        i2c.write(BesI2cAddrType.RF, 0x012e, 0x003e);//
        i2c.write(BesI2cAddrType.RF, 0x012f, 0x003e);//
        i2c.write(BesI2cAddrType.RF, 0x0130, 0x003e);//
        i2c.write(BesI2cAddrType.RF, 0x0131, 0x003e);//
        i2c.write(BesI2cAddrType.RF, 0x0132, 0x003e);//
        i2c.write(BesI2cAddrType.RF, 0x0133, 0x003e);//
        i2c.write(BesI2cAddrType.RF, 0x0084, 0x9294);//
        i2c.write(BesI2cAddrType.RF, 0x0085, 0x0024); //
        i2c.write(BesI2cAddrType.RF, 0x00d1, 0xa200);
        i2c.write(BesI2cAddrType.RF, 0x00d9, 0x0307);
        i2c.write(BesI2cAddrType.RF, 0x0188, 0x003f);
        i2c.write(BesI2cAddrType.RF, 0x005f, 0x01cf);// BLE
        i2c.write(BesI2cAddrType.RF, 0x0060, 0x01cf);
        i2c.write(BesI2cAddrType.RF, 0x0061, 0x024f);
        i2c.write(BesI2cAddrType.RF, 0x0062, 0x01cc);
        i2c.write(BesI2cAddrType.RF, 0x0063, 0x01c4);
        i2c.write(BesI2cAddrType.RF, 0x0064, 0x05b4);
        i2c.write(BesI2cAddrType.RF, 0x0065, 0x0da4);
        i2c.write(BesI2cAddrType.RF, 0x0066, 0x0d84);
        i2c.write(BesI2cAddrType.RF, 0x0067, 0xe03f);//
        i2c.write(BesI2cAddrType.RF, 0x0068, 0xe018);
        i2c.write(BesI2cAddrType.RF, 0x0069, 0x6048);
        i2c.write(BesI2cAddrType.RF, 0x006a, 0x6048);
        i2c.write(BesI2cAddrType.RF, 0x006b, 0x6088);
        i2c.write(BesI2cAddrType.RF, 0x006c, 0x6088);
        i2c.write(BesI2cAddrType.RF, 0x006d, 0x6088);
        i2c.write(BesI2cAddrType.RF, 0x006e, 0x6088);
        i2c.write(BesI2cAddrType.RF, 0x013c, 0x003e);//
        i2c.write(BesI2cAddrType.RF, 0x013d, 0x026f);
        i2c.write(BesI2cAddrType.RF, 0x013e, 0x003e);
        i2c.write(BesI2cAddrType.RF, 0x013f, 0x003e);
        i2c.write(BesI2cAddrType.RF, 0x0140, 0x003e);
        i2c.write(BesI2cAddrType.RF, 0x0141, 0x003e);
        i2c.write(BesI2cAddrType.RF, 0x0142, 0x003e);
        i2c.write(BesI2cAddrType.RF, 0x0143, 0x003e);
        i2c.write(BesI2cAddrType.RF, 0x0082, 0x9294);
        i2c.write(BesI2cAddrType.RF, 0x0083, 0x0024);
        i2c.write(BesI2cAddrType.RF, 0x0119, 0x0818);
        i2c.write(BesI2cAddrType.RF, 0x011a, 0x0818);
        i2c.write(BesI2cAddrType.RF, 0x011b, 0x0818);
        i2c.write(BesI2cAddrType.RF, 0x011c, 0x0dc0);
        i2c.write(BesI2cAddrType.RF, 0X0091, 0X8237);
        i2c.write(BesI2cAddrType.RF, 0x0001, 0x0101);
        i2c.write(BesI2cAddrType.RF, 0x000a, 0x0c01);
        i2c.write(BesI2cAddrType.RF, 0x002e, 0x54aa);
        i2c.write(BesI2cAddrType.RF, 0x002d, 0x3B20);
        i2c.write(BesI2cAddrType.RF, 0x18, 0x3948);
        i2c.write(BesI2cAddrType.RF, 0xf, 0x003d);
        i2c.write(BesI2cAddrType.RF, 0xc3, 0x0004);
        i2c.waitTime(15e6);
//        i2c.read(BesI2cAddrType.PMUIntern, 0x14,"readValue_0x14");//0x4000 ldo, 0x4e79 dcdc //add wuhan

//        i2c.write(BesI2cAddrType.PMUIntern, 0x14,0x4e79);//0x4000 ldo, 0x4e79 dcdc //add wuhan
//        i2c.read(BesI2cAddrType.PMUIntern, 0x14,"readValue_0x14");//0x4000 ldo, 0x4e79 dcdc //add wuhan
        i2c.waitTime(15e6); // 15ms
        i2c.transactionSequenceEnd();

        if(dcdc_meas) {

            new BesDsa_PMIC(ds_Mode).vMeas_DcVI_Hiz("VCORE_0p8", true, "Vm_VCORE", V93K_Resource.DPS128);
            new BesDsa_PMIC(ds_Mode).vMeas_DcVI_Hiz("VANA", true, "Vm_VANA", V93K_Resource.DPS128);
            new BesDsa_PMIC(ds_Mode).vMeas_DcVI_Hiz("VCODEC", true, "Vm_VCODEC", V93K_Resource.DPS128);

            ds_Mode.waitCall("10 ms");
            ds_Mode.actionCall("Vm_VCORE");
            ds_Mode.waitCall("10 ms");
            ds_Mode.actionCall("Vm_VANA");
            ds_Mode.waitCall("10 ms");
            ds_Mode.actionCall("Vm_VCODEC");
        }

        measurement.setSetups(ds_Mode);

        String pattern1 = "vectors.pattern_dft.bt1502x_verA_20220812_Spilit_Controller_prod_MemoryBist_P1_rev0p1_ascii";
        /********************* measurement1 ***********************/
        BesDsa_DFT besDsa_DFT1 = new BesDsa_DFT(StaticFields.prog_name);
        // pattern run
        IDeviceSetup ds1 = besDsa_DFT1.patternRun(pattern1, measurement1, spec_measurement1);
        // pattern run result configuration
        besDsa_DFT1.configPatternRun(ds1, ComparePinList, 1149370);


        /************ measurement_Mode_CLK ************/
      IDeviceSetup ds_Mode_CLK = DeviceSetupFactory.createInstance(StaticFields.prog_name);
      ds_Mode_CLK.importSpec(spec_measurement);

      BesPA_I2C i2c_1 = new BesPA_I2C(ds_Mode_CLK, measurement_CLK, I2cRegAddrBits.RegAddr_8Bits);
      i2c_1.setSignals("I2C_SCL", "I2C_SDA");
      // operating sequence
      i2c_1.transactionSequenceBegin("BIST_clk");



      i2c_1.write(BesI2cAddrType.DIGITAL,0x40086008,0xFFFF3FFF); //
      i2c_1.write(BesI2cAddrType.DIGITAL,0x4008001C,0x00040207); //
      i2c_1.write(BesI2cAddrType.DIGITAL,0x4000005c,0x00021401); //
      i2c_1.write(BesI2cAddrType.DIGITAL,0x4000008c,0x00004780); //
      i2c_1.write(BesI2cAddrType.DIGITAL,0x40000060,0x0005FF26);
      i2c_1.write(BesI2cAddrType.DIGITAL,0x400000a8,0x00002002); //

      i2c_1.write(BesI2cAddrType.DIGITAL,0x40000070,0x00080000);
      i2c_1.write(BesI2cAddrType.DIGITAL,0x400800a0,0x00001001);
      i2c_1.write(BesI2cAddrType.DIGITAL,0x40085018,0x000002F0);
      i2c_1.write(BesI2cAddrType.DIGITAL,0x40080004,0x03E00181);
                                                                //
      i2c_1.waitTime(15e6);
      i2c_1.transactionSequenceEnd();
      measurement_CLK.setSetups(ds_Mode_CLK);
      /************ measurement_Mode_CLK ************/
    IDeviceSetup ds_Mode_CLK_2 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
    ds_Mode_CLK_2.importSpec(spec_measurement);

    BesPA_I2C i2c_11 = new BesPA_I2C(ds_Mode_CLK_2, measurement_CLK_2, I2cRegAddrBits.RegAddr_8Bits);
    i2c_11.setSignals("I2C_SCL", "I2C_SDA");
    // operating sequence
    i2c_11.transactionSequenceBegin("BIST_clk_2");



    i2c_11.write(BesI2cAddrType.DIGITAL,0x40086008,0xFFFF3FFF); //
    i2c_11.write(BesI2cAddrType.DIGITAL,0x4008001c,0x00040207); //
    i2c_11.write(BesI2cAddrType.DIGITAL,0x4000005c,0x0002E401); //
    i2c_11.write(BesI2cAddrType.DIGITAL,0x40000100,0x000010C9); //
    i2c_11.write(BesI2cAddrType.DIGITAL,0x40000070,0x00090000);
    i2c_11.write(BesI2cAddrType.DIGITAL,0x400800a0,0x00001002); //
    i2c_11.write(BesI2cAddrType.DIGITAL,0x40085018,0x000002F0);





    i2c_11.waitTime(15e6);
    i2c_11.transactionSequenceEnd();
    measurement_CLK_2.setSetups(ds_Mode_CLK_2);


    /************ measurement_Mode_CLK ************/
  IDeviceSetup ds_Mode_CLK_3 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
  ds_Mode_CLK_3.importSpec(spec_measurement);

  BesPA_I2C i2c_111 = new BesPA_I2C(ds_Mode_CLK_3, measurement_CLK_3, I2cRegAddrBits.RegAddr_8Bits);
  i2c_111.setSignals("I2C_SCL", "I2C_SDA");
  // operating sequence
  i2c_111.transactionSequenceBegin("BIST_clk_3");



  i2c_111.write(BesI2cAddrType.DIGITAL,0x40085038,0xCAFE0000); //
  i2c_111.write(BesI2cAddrType.DIGITAL,0x400800CC,0x000000DD); //
  i2c_111.write(BesI2cAddrType.DIGITAL,0x40080004,0x13e8018c); //
  i2c_111.write(BesI2cAddrType.DIGITAL,0x4008004C,0x048C151F); //
  i2c_111.write(BesI2cAddrType.DIGITAL,0xD0350214,0x00000000);
  i2c_111.write(BesI2cAddrType.DIGITAL,0x40086008,0xFFFF3FFF); //
  i2c_111.write(BesI2cAddrType.DIGITAL,0x4008001c,0x00040205);
  i2c_111.write(BesI2cAddrType.DIGITAL,0xd0330044,0x58165031);



                                                            //
  i2c_111.waitTime(15e6);
  i2c_111.transactionSequenceEnd();
  measurement_CLK_3.setSetups(ds_Mode_CLK_3);


  /************ measurement_Mode_CLK ************/
IDeviceSetup ds_Mode_CLK_4 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
ds_Mode_CLK_4.importSpec(spec_measurement);

BesPA_I2C i2c_1111 = new BesPA_I2C(ds_Mode_CLK_4, measurement_CLK_4, I2cRegAddrBits.RegAddr_8Bits);
i2c_1111.setSignals("I2C_SCL", "I2C_SDA");
// operating sequence
i2c_1111.transactionSequenceBegin("BIST_clk_4");



i2c_1111.write(BesI2cAddrType.DIGITAL,0x40085038,0xCAFE0000); //
i2c_1111.write(BesI2cAddrType.DIGITAL,0x400800CC,0x000000DD); //
i2c_1111.write(BesI2cAddrType.DIGITAL,0x40080004,0x13e8018c); //
i2c_1111.write(BesI2cAddrType.DIGITAL,0x4008004C,0x048C151F); //
i2c_1111.write(BesI2cAddrType.DIGITAL,0xD0350214,0x00000000);
i2c_1111.write(BesI2cAddrType.DIGITAL,0x40086008,0xFFFF3FFF); //
i2c_1111.write(BesI2cAddrType.DIGITAL,0x4008001c,0x00040205);
i2c_1111.write(BesI2cAddrType.DIGITAL,0xd0330044,0x58167031);
i2c_1111.write(BesI2cAddrType.DIGITAL,0xd0330020,0x00400000);


                //
i2c_1111.waitTime(15e6);
i2c_1111.transactionSequenceEnd();
measurement_CLK_4.setSetups(ds_Mode_CLK_4);



      //=============== Execute Digital Capture ============//
      IDeviceSetup dsl =DeviceSetupFactory.createInstance(StaticFields.prog_name);
      BesDsa_DigitalCapture capture=new BesDsa_DigitalCapture(dsl, measurement_Capture, "GPIO_13");
      capture.genMainSpec(800, CaptureEdge.x8, true);
      capture.genDigitalCaptureSSF(1000);
//      measurement_Capture.setSetups(dsl);
    }

    @Override
    public void execute() {

//
//          measurement.execute();
//         measurement_CLK.execute();
//         measurement_Capture.execute();
//         IDigInOutCaptureResults capRes1=measurement_Capture.digInOut().preserveCaptureResults();
//         MultiSiteWaveLong wave1=capRes1.getSerialBitsAsWaveLong("GPIO_13", 1, BitOrder.LEFT_TO_RIGHT);
//
//         MultiSiteDouble freq1= new BesCalc_PLL().freqCal(wave1, 0.00125, freqCalMode.FFT);        //0.00125 us -> 800Mhz  MultiSiteLong rea11 = resPA.getResult("read11");
//         wave1.plot("_waveform");
//         println("CLK 1 = "+freq1 );
//
//        measurement_CLK_2.execute();
//      measurement_Capture.execute();
//       IDigInOutCaptureResults capRes2=measurement_Capture.digInOut().preserveCaptureResults();
//       MultiSiteWaveLong wave2=capRes2.getSerialBitsAsWaveLong("GPIO_13", 1, BitOrder.LEFT_TO_RIGHT);
//
//       MultiSiteDouble freq2= new BesCalc_PLL().freqCal(wave2, 0.00125, freqCalMode.FFT);        //0.00125 us -> 800Mhz  MultiSiteLong rea11 = resPA.getResult("read11");
//       wave2.plot("_waveform");
//       println("CLK 2 = "+freq2 );
//
//       measurement_CLK_3.execute();
//       measurement_Capture.execute();
//
//      IDigInOutCaptureResults capRes3=measurement_Capture.digInOut().preserveCaptureResults();
//      MultiSiteWaveLong wave3=capRes3.getSerialBitsAsWaveLong("GPIO_13", 1, BitOrder.LEFT_TO_RIGHT);
//
//      MultiSiteDouble freq3= new BesCalc_PLL().freqCal(wave3, 0.00125, freqCalMode.FFT);        //0.00125 us -> 800Mhz  MultiSiteLong rea11 = resPA.getResult("read11");
//      wave3.plot("_waveform");
//      println("CLK 3 = "+freq3 );
//
//      measurement_CLK_4.execute();
//      measurement_Capture.execute();
//     IDigInOutCaptureResults capRes4=measurement_Capture.digInOut().preserveCaptureResults();
//     MultiSiteWaveLong wave4=capRes4.getSerialBitsAsWaveLong("GPIO_13", 1, BitOrder.LEFT_TO_RIGHT);
//
//     MultiSiteDouble freq4= new BesCalc_PLL().freqCal(wave4, 0.00125, freqCalMode.FFT);        //0.00125 us -> 800Mhz  MultiSiteLong rea11 = resPA.getResult("read11");
//     wave4.plot("_waveform");
//     println("CLK 4 = "+freq4 );













//        measurement_forceV.execute();
        measurement.execute();
        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults resPA = ProtocolAccess.preserveResults(measurement);


        if(dcdc_meas) {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");

            IDcVIResults Result1 = measurement.dcVI().preserveResults();
            MultiSiteDouble VCORE_org_vol = Result1.vmeas("Vm_VCORE").getVoltage("VCORE_0p8").getElement(0);
            MultiSiteDouble VANA_org_vol = Result1.vmeas("Vm_VANA").getVoltage("VANA").getElement(0);
            MultiSiteDouble VCODEC_org_vol = Result1.vmeas("Vm_VCODEC").getVoltage("VCODEC").getElement(0);
            println("DC_Bk_VCORE_nold     [V]  = "+VCORE_org_vol);
            println("DC_Bk_VANA_nold      [V]  = "+VANA_org_vol);
            println("DC_Bk_VCODEC_nold    [V]  = "+VCODEC_org_vol);
            println();
        }

//
        measurement1.spec().setVariable("Period_ns", period_ns * 1e-9);
        measurement1.spec().setVariable("Stb_ns", strobe_ns * 1e-9);
        measurement1.execute();
        BesCalc_DFT res1 = new BesCalc_DFT(measurement1, ftd);
        MultiSiteLong FunResult1 = res1.getOverallResult();
//        res1.writeFailLog2Text(ComparePinList,"FailLog_Spilit_Controller");

        if (StaticFields.debugMode) {
            String testSuiteName_Qualified = context.getTestSuiteName();
            String testSuiteName = testSuiteName_Qualified
                    .substring(1 + testSuiteName_Qualified.lastIndexOf("."));
            println("**********" + testSuiteName + "**********");

//            for (int site : context.getActiveSites()) {
//                println("reg_0x14= 0x" + Long.toHexString(readValue_0x14.get(site)) + " [site "
//                        + site + "]");
//            }
//            println("FunResult1 = " + FunResult1);
            println();
        }

        ptd_Bist_AtSpeed.evaluate(FunResult1);

    }
}
