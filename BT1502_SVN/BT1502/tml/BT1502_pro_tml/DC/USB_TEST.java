package BT1502_pro_tml.DC;

import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import BT1502_pro_tml.Global.StaticFields;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class USB_TEST extends TestMethod {

    @In
    public String spec_measurement;

    public IMeasurement measurement;

    public IParametricTestDescriptor ptd_USB_FS_PHY_BIST;

    public String trCallName = "";
    private long expectData11 = 0x30000000L;
    private long expectData00 = 0x0L;

    @Override
    public void setup() {

        IDeviceSetup deviceSetup = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement);

        BesPA_I2C i2c = new BesPA_I2C(deviceSetup, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        i2c.transactionSequenceBegin("USB_TEST");
        i2c.read(BesI2cAddrType.DIGITAL, 0x4008006C, "read_0x4008006C_before");
        i2c.write(BesI2cAddrType.DIGITAL, 0x4008006C, 0x6D808264);// loopback=1 rxpd=0
        i2c.read(BesI2cAddrType.DIGITAL, 0x4008006C, "read_0x4008006C_after");
   //     i2c.waitTime(1e6);
        i2c.read(BesI2cAddrType.DIGITAL, 0x40080068, "read_0x40080068_before");// insert_det
        i2c.write(BesI2cAddrType.DIGITAL, 0x40080068, 0x3187F111);// insert_det 01C7F111
        i2c.read(BesI2cAddrType.DIGITAL, 0x40080068, "read_0x40080068_after");// insert_det
     //   i2c.waitTime(1e6);
        i2c.read(BesI2cAddrType.DIGITAL, 0x401C0008, "read_0x401C0008_before");//
        i2c.write(BesI2cAddrType.DIGITAL, 0x401C0008, 0x003F803F);// tx 17=1 16=1
        i2c.read(BesI2cAddrType.DIGITAL, 0x401C0008, "read_0x401C0008_after");//
      //  i2c.waitTime(1e6);
        i2c.read(BesI2cAddrType.DIGITAL, 0x40080068, "read_usb11");
        i2c.waitTime(1e6);
        i2c.write(BesI2cAddrType.DIGITAL, 0x401C0008, 0x0039803F);// tx 17=0 16=0
        //i2c.waitTime(1e6);
        i2c.read(BesI2cAddrType.DIGITAL, 0x40080068, "read_usb00");
        i2c.write(BesI2cAddrType.DIGITAL, 0x401C0008, 0x003B803F);// tx 17=1 16=0
      //  i2c.waitTime(1e6);
        i2c.read(BesI2cAddrType.DIGITAL, 0x40080068, "read_usb01");
        i2c.transactionSequenceEnd();
        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute() {

        measurement.execute();
        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults res_PA = ProtocolAccess.preserveResults(measurement);
//        MultiSiteLong rd6Cbefore = res_PA.getResult("read_0x4008006C_before");
//        MultiSiteLong rd6Cafter = res_PA.getResult("read_0x4008006C_after");
//        MultiSiteLong rd68before = res_PA.getResult("read_0x40080068_before");
//        MultiSiteLong rd68after = res_PA.getResult("read_0x40080068_after");
//        MultiSiteLong rd08before = res_PA.getResult("read_0x401C0008_before");
//        MultiSiteLong rd08after = res_PA.getResult("read_0x401C0008_after");
//        MultiSiteLong rd68after = res_PA.getResult("read_0x40080068_after");
        MultiSiteLong rd01 = res_PA.getResult("read_usb01");
        MultiSiteLong rd11 = res_PA.getResult("read_usb11");
        MultiSiteLong rd00 = res_PA.getResult("read_usb00");
        MultiSiteLong res11 = new MultiSiteLong(0);
        MultiSiteLong res00 = new MultiSiteLong(0);
        for (int site : context.getActiveSites()) {
            res11.set(site, (rd11.and(expectData11).get(site) == expectData11) ? 1 : -1);
            res00.set(site, (rd00.and(expectData11).get(site) == expectData00) ? 1 : -1);

        }

        MultiSiteLong res = new MultiSiteLong(0);
        for (int site : context.getActiveSites()) {
            if (res11.get(site) == 1 && res00.get(site) == 1) {
                res.set(site, 1);
            } else {
                res.set(site, 0);
            }
        }

         if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified = context.getTestSuiteName();
            String testSuiteName = testSuiteName_Qualified
                    .substring(1 + testSuiteName_Qualified.lastIndexOf("."));
            println("**********" + testSuiteName + "**********");
            for (int site : context.getActiveSites()) {
//                println("read11 = 0x" + Long.toHexString(rd11.get(site)) + " [site " + site + "]");
//                println("rd6Cbefore = 0x" + Long.toHexString(rd6Cbefore.get(site)) + " [site " + site + "]");
//                println("rd6Cafter = 0x" + Long.toHexString(rd6Cafter.get(site)) + " [site " + site + "]");  println("read11 = 0x" + Long.toHexString(rd11.get(site)) + " [site " + site + "]");
//                println("rd68before = 0x" + Long.toHexString(rd68before.get(site)) + " [site " + site + "]");  println("read11 = 0x" + Long.toHexString(rd11.get(site)) + " [site " + site + "]");
//                println("rd68after = 0x" + Long.toHexString(rd68after.get(site)) + " [site " + site + "]");
//                println("rd08before = 0x" + Long.toHexString(rd08before.get(site)) + " [site " + site + "]");
//                println("rd08after = 0x" + Long.toHexString(rd08after.get(site)) + " [site " + site + "]");
//                println("rd08after = 0x" + Long.toHexString(rd08after.get(site)) + " [site " + site + "]");
//                println("rd08after = 0x" + Long.toHexString(rd08after.get(site)) + " [site " + site + "]");
              println("rd00 = 0x" + Long.toHexString(rd00.get(site)) + " [site " + site + "]");
              println("rd01 = 0x" + Long.toHexString(rd01.get(site)) + " [site " + site + "]");
              println("rd11 = 0x" + Long.toHexString(rd11.get(site)) + " [site " + site + "]");
                //                println("res11 = " + res11.get(site) + " [site " + site + "]");
//                println("res00 = " + res00.get(site) + " [site " + site + "]");

            }
            println();
        }
        ptd_USB_FS_PHY_BIST.evaluate(res);
    }
}
