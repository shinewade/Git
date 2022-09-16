package BT1502_pro_tml.FUNC;

import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import BT1502_pro_tml.Global.StaticFields;
import besLib.cal.BesCalc_DFT;
import besLib.dsa.BesDsa_DFT;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.testdescriptor.IFunctionalTestDescriptor;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class SCAN_TRANS_TM extends TestMethod {

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
    public IMeasurement measurement1;

    public IParametricTestDescriptor ptd_SCAN_TRANS;
    public IFunctionalTestDescriptor ftd;

    @Override
    public void setup() {
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


        i2c.write(BesI2cAddrType.ANA,0x01AD, 0x0002);//
        i2c.write(BesI2cAddrType.ANA,0x016D, 0x8140);
        i2c.write(BesI2cAddrType.ANA,0x01A8, 0x6700);//
        i2c.write(BesI2cAddrType.ANA,0x01A2, 0x0000);//
        i2c.write(BesI2cAddrType.ANA,0x01A1, 0xD000);//
        i2c.write(BesI2cAddrType.ANA,0x01A0, 0x0000);//
        i2c.write(BesI2cAddrType.ANA,0x0198, 0x00D1);
        i2c.write(BesI2cAddrType.ANA,0x0198, 0x00D3);

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
        i2c.write(BesI2cAddrType.RF, 0xbf, 0x3002);
        i2c.write(BesI2cAddrType.RF, 0xce, 0x00b5);
        i2c.write(BesI2cAddrType.RF, 0x2c, 0x261F);
        i2c.write(BesI2cAddrType.RF, 0x28, 0xbe10);
        i2c.write(BesI2cAddrType.RF, 0x2b, 0x0f00);
        i2c.write(BesI2cAddrType.RF, 0x27, 0x7604);
        i2c.write(BesI2cAddrType.RF, 0x81, 0x0009);
        i2c.write(BesI2cAddrType.RF, 0x4a, 0x0000);
        i2c.write(BesI2cAddrType.RF, 0x0f, 0x0001);
        i2c.write(BesI2cAddrType.RF, 0x11, 0x4040);
        i2c.write(BesI2cAddrType.RF, 0x91, 0x8608);
        i2c.write(BesI2cAddrType.RF, 0xa6, 0x1eea);
        i2c.write(BesI2cAddrType.RF, 0x95, 0x0032);
        i2c.write(BesI2cAddrType.RF, 0X18, 0X3948);
        i2c.write(BesI2cAddrType.RF, 0XF, 0X30);

        i2c.write(BesI2cAddrType.RF, 0x002c, 0x245E);// txf gain
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
        i2c.write(BesI2cAddrType.RF, 0x00bf, 0x3002);
        i2c.write(BesI2cAddrType.RF, 0x00c5, 0x0043);
        i2c.write(BesI2cAddrType.RF, 0x0122, 0x0700);
        i2c.write(BesI2cAddrType.RF, 0x002b, 0x0e00);// flt max gain
        i2c.write(BesI2cAddrType.RF, 0x0081, 0x0008);// i2v rin
        i2c.write(BesI2cAddrType.RF, 0x0092, 0x8390);// tmx bias
        i2c.write(BesI2cAddrType.RF, 0x0027, 0x7604);// flt bw
        i2c.write(BesI2cAddrType.RF, 0x0146, 0x461b);// adc cplx
        i2c.write(BesI2cAddrType.RF, 0x00ce, 0x00B5);
        i2c.write(BesI2cAddrType.RF, 0x0028, 0x3e10);// i2v_corner
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
        i2c.write(BesI2cAddrType.RF, 0xf, 0x000d);
        i2c.write(BesI2cAddrType.RF, 0xc3, 0x0004);


        i2c.write(BesI2cAddrType.PMUIntern, 0x14,0x4e79);//0x4000 0x0e79 //add wuhan
        i2c.waitTime(15e6); // 15ms
        i2c.transactionSequenceEnd();

        measurement.setSetups(ds_Mode);

        String pattern1 = "vectors.pattern_dft.bt1502x_verA_20220121_prod_scan_trans__0_16000_rev1p0_ascii";
        /********************* measurement1 ***********************/
        BesDsa_DFT besDsa_DFT1 = new BesDsa_DFT(StaticFields.prog_name);
        // pattern run
        IDeviceSetup ds1 = besDsa_DFT1.patternRun(pattern1, measurement1, spec_measurement1);
        // pattern run result configuration
        besDsa_DFT1.configPatternRun(ds1, ComparePinList, 15815707);
    }

    @Override
    public void execute() {

        measurement.execute();
        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults resPA = ProtocolAccess.preserveResults(measurement);
        MultiSiteLong readValue_0x14 = resPA.getResult("readValue_0x14");
//        MultiSiteLong read_0x1A8 = resPA.getResult("read_0x1A8");
//        MultiSiteLong read_0x1A8_0 = resPA.getResult("read_0x1A8_0");

//        for (int site1 : context.getActiveSites()) {
//        System.out.println( "read =" + Long.toHexString(read_0x1A8.get(site1)));
//        System.out.println( "read_before =" + Long.toHexString(read_0x1A8_0.get(site1)));
//
//        }
        measurement1.spec().setVariable("Period_ns", period_ns * 1e-9);
        measurement1.spec().setVariable("Stb_ns", strobe_ns * 1e-9);
        measurement1.execute();
        BesCalc_DFT res1 = new BesCalc_DFT(measurement1, ftd);
        MultiSiteLong FunResult1 = res1.getOverallResult();
//        res1.writeFailLog2Text(ComparePinList,"failLog_Scan_Trans_8");

        if (StaticFields.debugMode) {
            String testSuiteName_Qualified = context.getTestSuiteName();
            String testSuiteName = testSuiteName_Qualified
                    .substring(1 + testSuiteName_Qualified.lastIndexOf("."));
            println("**********" + testSuiteName + "**********");

            for (int site : context.getActiveSites()) {
                println("reg_0x14= 0x" + Long.toHexString(readValue_0x14.get(site)) + " [site "
                        + site + "]");
            }
            println("FunResult1 = " + FunResult1);
            println();
        }

        ptd_SCAN_TRANS.evaluate(FunResult1);

    }
}
