package BT1502_pro_tml.DC;

import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import BT1502_pro_tml.Global.StaticFields;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDigInOut;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class ADC0_TEST extends TestMethod {

    @In public String spec_measurement;

    public IMeasurement measurement;

    public String trCallName = "";
    private String ADC = "";

    public IParametricTestDescriptor ptd_adc0_sense;

    @Override
    public void setup()
    {
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement);

        BesPA_I2C i2c=new BesPA_I2C(deviceSetup, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        ADC="ADC0";

        ISetupDigInOut DigInOut_ADC0 = deviceSetup.addDigInOut(ADC).setConnect(true).setDisconnect(true);
        DigInOut_ADC0.vforce("Vforce_1V_ADC0").setForceValue("1 V").setIrange("56 mA").setIclamp("56 mA");

        deviceSetup.actionCall("Vforce_1V_ADC0");
        i2c.transactionSequenceBegin("ADC0_TEST");
            i2c.write(BesI2cAddrType.PMUIntern, 0x0d,0x4492);
            i2c.write(BesI2cAddrType.PMUIntern, 0x90,0x6200);
            i2c.write(BesI2cAddrType.PMUIntern, 0x93,0x6424);
            i2c.write(BesI2cAddrType.PMUIntern, 0x1d,0x2704);
            i2c.write(BesI2cAddrType.PMUIntern, 0x4f,0x0e24);
            i2c.waitTime(5e6);
            i2c.read(BesI2cAddrType.PMUIntern, 0x58,"readValue_0x58");//ch2 ADC0
        i2c.transactionSequenceEnd();

        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute ()
    {
        measurement.execute();
        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults res1_PA=ProtocolAccess.preserveResults(measurement);
        MultiSiteLong l_58 = res1_PA.getResult("readValue_0x58");

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            for(int site:context.getActiveSites()) {
                println("l_58 = 0x"+Long.toHexString(l_58.get(site))+" [site "+site+"]");
            }
            println();
        }

        ptd_adc0_sense.evaluate(l_58);
    }
}
