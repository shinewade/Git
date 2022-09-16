package BT1502_pro_tml.BT;

import com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults;
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

public class BT_TX_LOFreq_Dyn_Mode extends TestMethod {

    @In public String spec_measurement;
    @In public int freq ;

    public IMeasurement measurement;
    public IMeasurement measurement_GuardBand_L;
    public IMeasurement measurement_GuardBand_H;


    @Override
    public void setup ()
    {
        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);
        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement_GuardBand_L, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");
        i2c.transactionSequenceBegin("BT_TX_CW_Mode_GB_L");

        i2c.read(BesI2cAddrType.RF, 0x20,"read_0x20_BE");// 0x20[13]=1'b1
        i2c.read(BesI2cAddrType.RF, 0xd1,"read_0xd1_BE");// 0xd1[9]=1'b0

            i2c.write(BesI2cAddrType.RF, 0x20,0x2000);// 0x20[13]=1'b1
            i2c.write(BesI2cAddrType.RF, 0x80,0xc755);// 0x80,0xc755;
            i2c.write(BesI2cAddrType.RF, 0x7f,0x5555);// 0x7f,0x5555;

            i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00,0x0);
            i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00,0xa0000);
            i2c.write(BesI2cAddrType.RF, 0xd1,0xA000);// 0xd1[9]=1'b0

          i2c.read(BesI2cAddrType.RF, 0x20,"read_0x20");// 0x20[13]=1'b1
          i2c.read(BesI2cAddrType.RF, 0xd1,"read_0xd1");// 0xd1[9]=1'b0
            i2c.waitTime(2e6);
        i2c.transactionSequenceEnd();
        measurement_GuardBand_L.setSetups(ds1);



        IDeviceSetup ds2 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds2.importSpec(spec_measurement);
        BesPA_I2C i2c_2=new BesPA_I2C(ds2, measurement_GuardBand_H, I2cRegAddrBits.RegAddr_8Bits);
        i2c_2.setSignals("I2C_SCL", "I2C_SDA");
        i2c_2.transactionSequenceBegin("BT_TX_CW_Mode_GB_H");
//            i2c_2.read(BesI2cAddrType.RF, 0x20,"read_0x20");// 0x20[13]=1'b1
            i2c_2.write(BesI2cAddrType.RF, 0x20,0x2000);// 0x20[13]=1'b1
            i2c_2.write(BesI2cAddrType.RF, 0x80,0xcf80);// 0x80,0xc755;
            i2c_2.write(BesI2cAddrType.RF, 0x7f,0x0);// 0x7f,0x5555;
            i2c_2.write(BesI2cAddrType.RF, 0xd1,0xA200);// 0xd1[9]=1'b1 bt1 before digtal
            i2c_2.write(BesI2cAddrType.DIGITAL, 0xd0220c00,0x0);
            i2c_2.write(BesI2cAddrType.DIGITAL, 0xd0220c00,0xa004e);  //2490
//            i2c_2.read(BesI2cAddrType.RF, 0xd1,"read_0xd1");// 0xd1[9]=1'b0
            i2c_2.write(BesI2cAddrType.RF, 0xd1,0xA000);// 0xd1[9]=1'b0
            i2c_2.waitTime(2e6);
        i2c_2.transactionSequenceEnd();
        measurement_GuardBand_H.setSetups(ds2);



        long data_0xd0220c00=0;
        switch (freq) {
        case 2402:
            data_0xd0220c00 = 0x000a0000;//0x000a00000   x800a0000
            break;
        case 2441:
            data_0xd0220c00 = 0x000a0027;
            break;
        case 2480:
            data_0xd0220c00 = 0x000a004e;
            break;
        case 2392:
            data_0xd0220c00 = 0x000a0000;
            break;
        case 2490:
            data_0xd0220c00 = 0x000a0058;
            break;

        default:
            data_0xd0220c00 = 0x800a0000;
            break;
        }
        IDeviceSetup ds3 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds3.importSpec(spec_measurement);
        BesPA_I2C i2c_3=new BesPA_I2C(ds3, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c_3.setSignals("I2C_SCL", "I2C_SDA");
        i2c_3.transactionSequenceBegin("BT_TX_CW_Mode_Dyn");
            i2c_3.write(BesI2cAddrType.DIGITAL, 0xd0220c00,0x0);
            i2c_3.write(BesI2cAddrType.DIGITAL, 0xd0220c00,data_0xd0220c00);
            i2c_3.waitTime(2e6);
        i2c_3.transactionSequenceEnd();
        measurement.setSetups(ds3);
    }

    @Override
    public void execute ()
    {
        MultiSiteLong read_0x20_L=new MultiSiteLong();
        MultiSiteLong read_0xd1_L=new MultiSiteLong();
        MultiSiteLong read_0x20_BE=new MultiSiteLong();
        MultiSiteLong read_0xd1_BE=new MultiSiteLong();
        MultiSiteLong read_0x20_H=new MultiSiteLong();
        MultiSiteLong read_0xd1_H=new MultiSiteLong();

        switch (freq) {
        case 2392:
            measurement_GuardBand_L.execute();
            IProtocolInterfaceResults res_PA = ProtocolAccess.preserveResults(measurement_GuardBand_L);
            read_0x20_BE = res_PA.getResult("read_0x20_BE");
            read_0xd1_BE = res_PA.getResult("read_0xd1_BE");
            read_0x20_L = res_PA.getResult("read_0x20");
            read_0xd1_L = res_PA.getResult("read_0xd1");
//            for (int site : context.getActiveSites()) {
//                System.out.println("site= " + site + " read_0x20_BE = "+ Long.toHexString(read_0x20_BE.get(site)));
//                System.out.println("site= " + site + " read_0xd1_BE = "+ Long.toHexString(read_0xd1_BE.get(site)));
//                System.out.println("site= " + site + " read_0x20_L = "+ Long.toHexString(read_0x20_L.get(site)));
//                System.out.println("site= " + site + " read_0xd1_L = "+ Long.toHexString(read_0xd1_L.get(site)));
//
//
////                System.out.println("site= " + site + " readValue_0x4008a050_H1 Binary Data= "+ Long.toBinaryString(readValue_0x4008a050_H1.get(site)));
////                System.out.println("site= " + site + " readValue_0x40081050_H2 Binary Data= "+ Long.toBinaryString(readValue_0x40081050_H2.get(site)));
////                System.out.println("site= " + site + " readValue_0x40089050_H2 Binary Data= "+ Long.toBinaryString(readValue_0x40089050_H2.get(site)));
////                System.out.println("site= " + site + " readValue_0x4008a050_H2 Binary Data= "+ Long.toBinaryString(readValue_0x4008a050_H2.get(site)));
////                println();
//            }
            break;
        case 2490:
            measurement_GuardBand_H.execute();
            IProtocolInterfaceResults res_PA1 = ProtocolAccess.preserveResults(measurement_GuardBand_H);
            read_0x20_H = res_PA1.getResult("read_0x20");
            read_0xd1_H = res_PA1.getResult("read_0xd1");
            for (int site : context.getActiveSites()) {
//
//                System.out.println("site= " + site + " read_0x20_H = "+ Long.toHexString(read_0x20_H.get(site)));
//                System.out.println("site= " + site + " read_0xd1_H = "+ Long.toHexString(read_0xd1_H.get(site)));
//
//                System.out.println("site= " + site + " readValue_0x4008a050_H1 Binary Data= "+ Long.toBinaryString(readValue_0x4008a050_H1.get(site)));
//                System.out.println("site= " + site + " readValue_0x40081050_H2 Binary Data= "+ Long.toBinaryString(readValue_0x40081050_H2.get(site)));
//                System.out.println("site= " + site + " readValue_0x40089050_H2 Binary Data= "+ Long.toBinaryString(readValue_0x40089050_H2.get(site)));
//                System.out.println("site= " + site + " readValue_0x4008a050_H2 Binary Data= "+ Long.toBinaryString(readValue_0x4008a050_H2.get(site)));
//                println();
            }
            break;

        default:
            measurement.execute();
            break;
        }






        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
        }
    }


}
