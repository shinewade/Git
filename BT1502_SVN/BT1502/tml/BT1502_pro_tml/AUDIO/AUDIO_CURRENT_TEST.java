package BT1502_pro_tml.AUDIO;

import BT1502_pro_tml.Global.StaticFields;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupUtility;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.measurement.ILocalMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class AUDIO_CURRENT_TEST extends TestMethod {

    @In public String spec_measurement;

    public ILocalMeasurement measurement;
//    public ILocalMeasurement measurement_read;

    public IParametricTestDescriptor ptd_VBAT_AUDIO;

    public String trCallName = "";

    @Override
    public void setup() {

        IDeviceSetup deviceSetup=DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement);
        deviceSetup.addUtility("K22").setValue(0);

        ISetupUtility uti1=deviceSetup.addUtility("K11").setValue(0);
        uti1.setState("K11_On") .setValue(1);//MIC46
        uti1.setState("K11_Off").setValue(0);//MIC1235

        deviceSetup.addDcVI("VSYS").imeas("AUDIO_CURRENT").setIrange("100 mA").setAverages(16).setRestoreIrange(true);

        BesPA_I2C i2c=new BesPA_I2C(deviceSetup, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");
        //[ANA ft ]
        //[codec tx]
        i2c.transactionSequenceBegin("AUDIO_CURRENT_TEST");
        i2c.write(BesI2cAddrType.PMUIntern,0x2a,0x0174);
        i2c.write(BesI2cAddrType.PMUIntern,0x29,0x236e);
        i2c.write(BesI2cAddrType.PMUIntern,0x2a,0x2374);

        i2c.write(BesI2cAddrType.ANA,0xb2,0x777b);
        i2c.write(BesI2cAddrType.ANA,0xb0,0x2000);
        i2c.write(BesI2cAddrType.ANA,0xb3,0x0081);
        i2c.write(BesI2cAddrType.ANA,0xb1,0x0848 );
        i2c.write(BesI2cAddrType.ANA,0x60,0xa010);//page1
        i2c.write(BesI2cAddrType.ANA,0x6d,0x8140);
        i2c.write(BesI2cAddrType.ANA,0x76,0x0811);//0x0611=>0x0811   20220613 lizhengda  change for lowpower
        i2c.write(BesI2cAddrType.ANA,0x72,0x8000);
        i2c.write(BesI2cAddrType.ANA,0x74,0x7848);
        i2c.write(BesI2cAddrType.ANA,0x75,0x725f);
        i2c.write(BesI2cAddrType.ANA,0x6f,0x7700);
        i2c.write(BesI2cAddrType.ANA,0x70,0xf1f1);
//        i2c.write(BesI2cAddrType.ANA,0x70,0x0031);
        i2c.write(BesI2cAddrType.ANA,0x7b,0x22aa);//0x2322=>0x22aa   20220613 lizhengda  change for lowpower
        i2c.write(BesI2cAddrType.ANA,0x7c,0x300d);
        i2c.write(BesI2cAddrType.ANA,0x71,0x300d);//0x3000=>0x300d   20220613 lizhengda  change for lowpower
        i2c.write(BesI2cAddrType.ANA,0x6e,0xf081);//0xf081=>0xf080   20220613 lizhengda  change for lowpower
        i2c.write(BesI2cAddrType.ANA,0x7e,0x0700);
        i2c.write(BesI2cAddrType.ANA,0x60,0xa000);//page0


        //[open codec tx]
        i2c.write(BesI2cAddrType.DIGITAL,0x40085078,0xCAFE02F0);   //codec psc
        i2c.write(BesI2cAddrType.DIGITAL,0x40080004,0x0000008F);   //aon clk en
        i2c.write(BesI2cAddrType.DIGITAL,0x400800a0,0x000019c0);
        i2c.write(BesI2cAddrType.DIGITAL,0x400800cc,0x00000002);   //clear codec reset
        i2c.write(BesI2cAddrType.DIGITAL,0x40300074,0x18004012);
        i2c.waitTime(10e6);
        i2c.write(BesI2cAddrType.DIGITAL,0x40300060,0x007fffff);
        i2c.write(BesI2cAddrType.DIGITAL,0x403000b8,0x00004000);
        i2c.write(BesI2cAddrType.DIGITAL,0x403000b4,0x0);
        i2c.write(BesI2cAddrType.DIGITAL,0x403000b4,0x00204000);
        i2c.write(BesI2cAddrType.DIGITAL,0x403000b0,0x010e0407);
        i2c.waitTime(100e6);
        i2c.transactionSequenceEnd();

        deviceSetup.actionCall("K11_Off");
        deviceSetup.actionCall("AUDIO_CURRENT");
        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute() {
        measurement.execute();
        IDcVIResults DcVI_Result = measurement.dcVI("VSYS").preserveResults();
        MultiSiteDouble BT_TX_CurrRslt = DcVI_Result.imeas("AUDIO_CURRENT").getCurrent("VSYS").getElement(0);

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("AUDIO_CurrRslt[mA] = "+BT_TX_CurrRslt.multiply(1e3));
            println();
        }

        ptd_VBAT_AUDIO.evaluate(BT_TX_CurrRslt);
    }
}
