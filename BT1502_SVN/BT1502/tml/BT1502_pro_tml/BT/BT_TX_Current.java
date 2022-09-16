package BT1502_pro_tml.BT;

import BT1502_pro_tml.Global.StaticFields;
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
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class BT_TX_Current extends TestMethod {

    @In public String spec_measurement;

    public IMeasurement measurement;
    public IParametricTestDescriptor ptd_I_Vbat_BT;

    @Override
    public void setup() {
        IDeviceSetup ds1 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        // VBAT
        ISetupDcVI dcVi_VBAT = ds1.addDcVI("VSYS").setDisconnect(false);
        dcVi_VBAT.imeas("Im_VBAT").setUngangModeFast().setIrange("150 mA").setAverages(16).setRestoreIrange(true);
        // VANA
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VANA", true, "Vm_VANA", V93K_Resource.DPS128);

        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        //operating sequence
        i2c.transactionSequenceBegin("BT_TX_CURRENT");

        //    [new tst tx 1]
              i2c.write(BesI2cAddrType.RF,0x2c,0x245E);//txf gain
              i2c.write(BesI2cAddrType.RF,0xcb,0x00f0);//pad i    //0705: 0x00f0->0x00f4
              i2c.write(BesI2cAddrType.RF,0xc1,0x1a40);//pad gain //0705: 0x1b00->0x1a40
              i2c.write(BesI2cAddrType.RF,0x49,0x9384);//ind v
              i2c.write(BesI2cAddrType.RF,0x00,0xa010);
              i2c.write(BesI2cAddrType.RF,0x47,0x3055);//pa i     //0705: 0x3055->0x4055
              i2c.write(BesI2cAddrType.RF,0x00,0xa000);
              i2c.write(BesI2cAddrType.RF,0xCC,0x803f);// pa i
              i2c.write(BesI2cAddrType.RF,0xCA,0x0F00);// pa bias
              i2c.write(BesI2cAddrType.RF,0x00,0xa010);
              i2c.write(BesI2cAddrType.RF,0x60,0x068C);           //0705: 0x068C->0x040C
              i2c.write(BesI2cAddrType.RF,0x5C,0x35F7);           //0705: 0x35F7->0x20a2
              i2c.write(BesI2cAddrType.RF,0x00,0xa000);
              i2c.write(BesI2cAddrType.RF,0x4C,0x780F); //drv cap
              i2c.write(BesI2cAddrType.RF,0x51,0x035e);           //0705: 0x035e->keep
              i2c.write(BesI2cAddrType.RF,0xbf,0x1002);           //0705: keep
              i2c.write(BesI2cAddrType.RF,0xc5,0x0043);           //0705: 0x0043->0x0003
              i2c.write(BesI2cAddrType.RF,0x00,0xa010);

              i2c.write(BesI2cAddrType.RF,0x22,0x0700);
              i2c.write(BesI2cAddrType.RF,0x00,0xa000);

              i2c.write(BesI2cAddrType.RF,0x2b,0x0e00);//flt max gain           //0705: keep
              i2c.write(BesI2cAddrType.RF,0x81,0x0008);//i2v rin           //0705: keep
              i2c.write(BesI2cAddrType.RF,0x92,0x8390);//tmx bias
              i2c.write(BesI2cAddrType.RF,0x27,0x7604);//flt bw
              i2c.write(BesI2cAddrType.RF,0x00,0xa010);

              i2c.write(BesI2cAddrType.RF,0x46,0x461b);//adc cplx  //0705: 0x461b->0x401b
              i2c.write(BesI2cAddrType.RF,0x00,0xa000);

              i2c.write(BesI2cAddrType.RF,0xce,0x00B5);
              i2c.write(BesI2cAddrType.RF,0x28,0x3e10);//i2v_corner //0705: keep
              i2c.write(BesI2cAddrType.RF,0x90,0x3808);//dwa
              i2c.write(BesI2cAddrType.RF,0x00,0xa010);

              i2c.write(BesI2cAddrType.RF,0x62,0x1807);//0705: 0x1807->0x104f
              i2c.write(BesI2cAddrType.RF,0x00,0xa000);

              i2c.write(BesI2cAddrType.RF,0x41,0x01cf);//rx0 //0705: 0x01cf->0x034b
              i2c.write(BesI2cAddrType.RF,0x42,0x01cf);
              i2c.write(BesI2cAddrType.RF,0x43,0x024f);
              i2c.write(BesI2cAddrType.RF,0x44,0x01cc);
              i2c.write(BesI2cAddrType.RF,0x45,0x01c4);
              i2c.write(BesI2cAddrType.RF,0x46,0x05b4);
              i2c.write(BesI2cAddrType.RF,0x47,0x0da4);
              i2c.write(BesI2cAddrType.RF,0x48,0x0d84);
              i2c.write(BesI2cAddrType.RF,0x39,0xe03f);//rx1
              i2c.write(BesI2cAddrType.RF,0x3A,0xe018);
              i2c.write(BesI2cAddrType.RF,0x3B,0x6048);
              i2c.write(BesI2cAddrType.RF,0x3C,0x6048);
              i2c.write(BesI2cAddrType.RF,0x3D,0x6088);
              i2c.write(BesI2cAddrType.RF,0x3E,0x6088);
              i2c.write(BesI2cAddrType.RF,0x3F,0x6088);
              i2c.write(BesI2cAddrType.RF,0x40,0x6088);
              i2c.write(BesI2cAddrType.RF,0x00,0xa010);

//              i2c.write(BesI2cAddrType.RF,0x41,0x034b);//rx0 //0705: 0x01cf->0x034b
//              i2c.write(BesI2cAddrType.RF,0x42,0x01d3);
//              i2c.write(BesI2cAddrType.RF,0x43,0x01d3);
//              i2c.write(BesI2cAddrType.RF,0x44,0x05c3);
//              i2c.write(BesI2cAddrType.RF,0x45,0x05d3);
//              i2c.write(BesI2cAddrType.RF,0x46,0x05b4);
//              i2c.write(BesI2cAddrType.RF,0x47,0x0db3);
//              i2c.write(BesI2cAddrType.RF,0x48,0x0d9a);
//              i2c.write(BesI2cAddrType.RF,0x39,0xe038);//rx1
//              i2c.write(BesI2cAddrType.RF,0x3A,0xc02c);
//              i2c.write(BesI2cAddrType.RF,0x3B,0xa005);
//              i2c.write(BesI2cAddrType.RF,0x3C,0xa005);
//              i2c.write(BesI2cAddrType.RF,0x3D,0x0005);
//              i2c.write(BesI2cAddrType.RF,0x3E,0x0005);
//              i2c.write(BesI2cAddrType.RF,0x3F,0x0005);
//              i2c.write(BesI2cAddrType.RF,0x40,0x0005);
//              i2c.write(BesI2cAddrType.RF,0x00,0xa010);


              i2c.write(BesI2cAddrType.RF,0x2c,0x003e);//rx2
              i2c.write(BesI2cAddrType.RF,0x2d,0x026f);
              i2c.write(BesI2cAddrType.RF,0x2e,0x003e);
              i2c.write(BesI2cAddrType.RF,0x2f,0x003e);
              i2c.write(BesI2cAddrType.RF,0x30,0x003e);
              i2c.write(BesI2cAddrType.RF,0x31,0x003e);
              i2c.write(BesI2cAddrType.RF,0x32,0x003e);
              i2c.write(BesI2cAddrType.RF,0x33,0x003e);
              i2c.write(BesI2cAddrType.RF,0x00,0xa000);

              i2c.write(BesI2cAddrType.RF,0x84,0x9294);//0705: 0x9294->0xa449
              i2c.write(BesI2cAddrType.RF,0x85,0x0024);
              i2c.write(BesI2cAddrType.RF,0xd1,0xa200);
              i2c.write(BesI2cAddrType.RF,0xd9,0x0307);
              i2c.write(BesI2cAddrType.RF,0x00,0xa010);

              i2c.write(BesI2cAddrType.RF,0x88,0x003f);
              i2c.write(BesI2cAddrType.RF,0x00,0xa000);

              i2c.write(BesI2cAddrType.RF,0x5f,0x01cf);//BLE  //0705: 0x01cf->0x034b
              i2c.write(BesI2cAddrType.RF,0x60,0x01cf);
              i2c.write(BesI2cAddrType.RF,0x61,0x024f);
              i2c.write(BesI2cAddrType.RF,0x62,0x01cc);
              i2c.write(BesI2cAddrType.RF,0x63,0x01c4);
              i2c.write(BesI2cAddrType.RF,0x64,0x05b4);
              i2c.write(BesI2cAddrType.RF,0x65,0x0da4);
              i2c.write(BesI2cAddrType.RF,0x66,0x0d84);
              i2c.write(BesI2cAddrType.RF,0x67,0xe03f);//
              i2c.write(BesI2cAddrType.RF,0x68,0xe018);
              i2c.write(BesI2cAddrType.RF,0x69,0x6048);
              i2c.write(BesI2cAddrType.RF,0x6a,0x6048);
              i2c.write(BesI2cAddrType.RF,0x6b,0x6088);
              i2c.write(BesI2cAddrType.RF,0x6c,0x6088);
              i2c.write(BesI2cAddrType.RF,0x6d,0x6088);
              i2c.write(BesI2cAddrType.RF,0x6e,0x6088);
              i2c.write(BesI2cAddrType.RF,0x00,0xa010);

              i2c.write(BesI2cAddrType.RF,0x3c,0x003e);//
              i2c.write(BesI2cAddrType.RF,0x3d,0x026f);
              i2c.write(BesI2cAddrType.RF,0x3e,0x003e);
              i2c.write(BesI2cAddrType.RF,0x3f,0x003e);
              i2c.write(BesI2cAddrType.RF,0x40,0x003e);
              i2c.write(BesI2cAddrType.RF,0x41,0x003e);
              i2c.write(BesI2cAddrType.RF,0x42,0x003e);
              i2c.write(BesI2cAddrType.RF,0x43,0x003e);
              i2c.write(BesI2cAddrType.RF,0x00,0xa000);

              i2c.write(BesI2cAddrType.RF,0x82,0x9294);//0705: 0x9294->0xa449
              i2c.write(BesI2cAddrType.RF,0x83,0x0024);
              i2c.write(BesI2cAddrType.RF,0x00,0xa010);

              i2c.write(BesI2cAddrType.RF,0x19,0x0818);
              i2c.write(BesI2cAddrType.RF,0x1a,0x0818);
              i2c.write(BesI2cAddrType.RF,0x1b,0x0818);
              i2c.write(BesI2cAddrType.RF,0x1c,0x0dc0);
              i2c.write(BesI2cAddrType.RF,0x00,0xa000);

              i2c.write(BesI2cAddrType.RF,0X91,0X8237);
              i2c.write(BesI2cAddrType.RF,0x01,0x0101);
              i2c.write(BesI2cAddrType.RF,0x0a,0x0c01);

        //          [new fdata_tx_2]
              i2c.write(BesI2cAddrType.RF,0x2e,0x54aa);//0705: 0x54aa->0xa8d4
              i2c.write(BesI2cAddrType.RF,0x2d,0x3B20);//0705: 0x3B20-0xfb20

//            //[xtal enable dac clk]
//            i2c.write(BesI2cAddrType.XTAL,0x04,0x890d);
            i2c.waitTime(3e6);


//          [new ft tx 3]
            i2c.write(BesI2cAddrType.DIGITAL,0xd0350308,0x40104812);
            i2c.write(BesI2cAddrType.DIGITAL,0xD0350300,0x3FF00405);
            i2c.write(BesI2cAddrType.DIGITAL,0xd0350344,0x00000012);
            i2c.write(BesI2cAddrType.DIGITAL,0xd0350340,0x00000005);

            i2c.waitTime(300e6);

//          [new ft tx 3]
            i2c.write(BesI2cAddrType.DIGITAL,0xd0350308,0x40104812);
            i2c.write(BesI2cAddrType.DIGITAL,0xD0350300,0x3FF00405);
            i2c.write(BesI2cAddrType.DIGITAL,0xd0350344,0x00000012);
            i2c.write(BesI2cAddrType.DIGITAL,0xd0350340,0x00000005);

            i2c.waitTime(300e6);
//
            //[core init nansu]
            i2c.write(BesI2cAddrType.DIGITAL,0x40085038,0xCAFE0000);
            i2c.write(BesI2cAddrType.DIGITAL,0x400800CC,0x000000DD);
            i2c.write(BesI2cAddrType.DIGITAL,0x40080004,0x13e8018c);
            i2c.write(BesI2cAddrType.DIGITAL,0x4008004C,0x048C151F);
            i2c.waitTime(100e6);

            //  [ss]
            i2c.write(BesI2cAddrType.DIGITAL,0xD0350214,0x00000000);



            i2c.write(BesI2cAddrType.DIGITAL,0xd0220c00,0);
            i2c.waitTime(2e6);
            i2c.write(BesI2cAddrType.DIGITAL,0xd0220c00,0x000a0000);
            i2c.waitTime(100e6);
        i2c.transactionSequenceEnd();

        ds1.waitCall("50 ms");
        ds1.actionCall("Vm_VANA");
        ds1.actionCall("Im_VBAT");

        measurement.setSetups(ds1);


    }

    @Override
    public void execute() {
        measurement.execute();
        IDcVIResults res_DcVI = measurement.dcVI().preserveResults();

        MultiSiteDouble I_Vbat = res_DcVI.imeas("Im_VBAT").getCurrent("VSYS").getElement(0);
        MultiSiteDouble vana_NoLoad = res_DcVI.vmeas("Vm_VANA").getVoltage("VANA").getElement(0);

        if (StaticFields.debugMode)
        {
            String testSuiteName_Qualified = context.getTestSuiteName();
            String testSuiteName = testSuiteName_Qualified
                    .substring(1 + testSuiteName_Qualified.lastIndexOf("."));
            println("**********" + testSuiteName + "**********");
            println("I_Vbat[mA] = " + I_Vbat.multiply(1e3) + " mA");
            println("DC_VANA_nold      [V]  = "+vana_NoLoad);
            println();
        }
        ptd_I_Vbat_BT.evaluate(I_Vbat);

    }

}
