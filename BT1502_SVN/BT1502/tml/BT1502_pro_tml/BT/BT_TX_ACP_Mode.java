package BT1502_pro_tml.BT;

import BT1502_pro_tml.Global.StaticFields;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.measurement.ILocalMeasurement;

public class BT_TX_ACP_Mode extends TestMethod {

    @In public String spec_measurement;
    public ILocalMeasurement measurement;

    @Override
    public void setup() {

        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        i2c.transactionSequenceBegin("bt_tx_acp");
//            i2c.write(BesI2cAddrType.RF, 0xa7,0x0000);
            i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00,0x00175500);
            i2c.waitTime(5e6);
//            i2c.write(BesI2cAddrType.DIGITAL, 0xd0220c00,0xb5500);
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
            println();
        }




    }



}
