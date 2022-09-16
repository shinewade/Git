package BT1502_pro_tml.MEMORY;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_UART;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.workspace.IWorkspace;


public class RAMRUN_nullBin extends TestMethod {

    @In public String spec_measurement;
    @In public String spec_measurement_UART;
    @In public String fileName;

    public IMeasurement measurement_setuart;
    public IMeasurement measurement_dlduart;

    @Override
    public void setup ()
    {
        // init chip 26M & UART
        IDeviceSetup deviceSetup1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup1.importSpec(spec_measurement);

        BesPA_I2C i2c=new BesPA_I2C(deviceSetup1, measurement_setuart, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        //operating sequence
        i2c.transactionSequenceBegin("enter_uart_mode");
            i2c.write(BesI2cAddrType.DIGITAL,0x40080038,0x00000130);
            i2c.write(BesI2cAddrType.DIGITAL,0x400800C8,0x00000080);
            i2c.waitTime(20e6);
            i2c.write(BesI2cAddrType.DIGITAL,0x40080038,0x00000130);
            i2c.write(BesI2cAddrType.DIGITAL,0x400800C8,0x00000080);
            i2c.waitTime(20e6);
        i2c.transactionSequenceEnd();

        measurement_setuart.setSetups(deviceSetup1);


        // dld UART
        IDeviceSetup deviceSetup3 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup3.importSpec(spec_measurement_UART);

        context.workspace();
        new BesDsa_UART(deviceSetup3).writeToRAM_NewPA(measurement_dlduart,IWorkspace.getActiveProjectPath()+ "/tml/BT1502_pro_tml/MEMORY/ramrun_bin_file/"+"1502_ramrun_open_i2c.bin");
        deviceSetup3.waitCall("10 ms");
        measurement_dlduart.setSetups(deviceSetup3);
    }

    @Override
    public void execute ()
    {
        measurement_setuart.execute();
        measurement_dlduart.execute();

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println();
        }

    }
}
