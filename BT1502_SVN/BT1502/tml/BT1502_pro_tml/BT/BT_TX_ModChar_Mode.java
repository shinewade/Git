package BT1502_pro_tml.BT;

import BT1502_pro_tml.Global.StaticFields;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.measurement.IMeasurement;

public class BT_TX_ModChar_Mode extends TestMethod {

    @In public String spec_measurement;
    @In public String modChar_Mode;

    public IMeasurement measurement;

    @Override
    public void setup() {

        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        long channel_tx=0;
        long ch_rf_tx=0;
        if(modChar_Mode.equals("df1")) {
            channel_tx=0xaf000;    // f0:11110000
            ch_rf_tx=0x0000;
        }
        else if(modChar_Mode.equals("df2")) {
            channel_tx=0xa5500;    //55:01010101
            ch_rf_tx=0x0000;
        }

        //operating sequence
        i2c.transactionSequenceBegin("bt_tx_mc_df1");
            i2c.write(BesI2cAddrType.RF, 0xA7,ch_rf_tx);
            i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00,"0x00000000");
            i2c.waitTime(5e6);
            i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00,channel_tx);
        i2c.transactionSequenceEnd();
        measurement.setSetups(ds1);

    }

    @Override
    public void execute() {
        measurement.execute();

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
        }







    }



}
