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
import xoc.dta.measurement.ILocalMeasurement;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class Check_ADC extends TestMethod {

    @In public String spec_measurement;

    public ILocalMeasurement measurement;

    public IParametricTestDescriptor    ptd_adc1_VBAT_3P2,
                                        ptd_adc1_VBAT_3P7,
                                        ptd_adc1_VBAT_4P2,
                                        ptd_delta_3p7_3p2,
                                        ptd_delta_3p7_4p2;

    @Override
    public void setup()
    {




        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        i2c.transactionSequenceBegin("read_ADC");
            i2c.write(BesI2cAddrType.PMUIntern, 0x0d, 0x4492);
            i2c.write(BesI2cAddrType.PMUIntern, 0x90, 0x6200);
            i2c.write(BesI2cAddrType.PMUIntern, 0x93, 0x6424);
            i2c.write(BesI2cAddrType.PMUIntern, 0x1d, 0x2702);
            i2c.write(BesI2cAddrType.PMUIntern, 0xda, 0x0220);
            i2c.write(BesI2cAddrType.PMUIntern, 0x4f, 0x0024);//start
            i2c.waitTime(5e6);
            i2c.read(BesI2cAddrType.PMUIntern, 0x57,"readValue_0x57");//ch1
        i2c.transactionSequenceEnd();

        measurement.setSetups(ds1);

    }

    @Override
    public void execute ()
    {
        measurement.spec().setVariable("Vbat", 3.2);
        measurement.execute();
        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults res1_PA=ProtocolAccess.preserveResults(measurement);
        MultiSiteLong adc1_VBAT_3p2 = res1_PA.getResult("readValue_0x57");

        measurement.spec().setVariable("Vbat", 3.7);
        measurement.execute();
        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults res2_PA=ProtocolAccess.preserveResults(measurement);
        MultiSiteLong adc1_VBAT_3p7 = res2_PA.getResult("readValue_0x57");

        measurement.spec().setVariable("Vbat", 4.2);
        measurement.execute();
        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults res3_PA=ProtocolAccess.preserveResults(measurement);
        MultiSiteLong adc1_VBAT_4p2 = res3_PA.getResult("readValue_0x57");

        StaticFields.VSENSE_3P2V.set(adc1_VBAT_3p2);
        StaticFields.VSENSE_4P2V.set(adc1_VBAT_4p2);

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            for(int site:context.getActiveSites()) {
                println("adc1_VBAT_3p2 = 0x"+Long.toHexString(adc1_VBAT_3p2.get(site))+" [site "+site+"]");
                println("adc1_VBAT_3p7 = 0x"+Long.toHexString(adc1_VBAT_3p7.get(site))+" [site "+site+"]");
                println("adc1_VBAT_4p2 = 0x"+Long.toHexString(adc1_VBAT_4p2.get(site))+" [site "+site+"]");
            }
            println();
        }

        ptd_adc1_VBAT_3P2.evaluate(StaticFields.VSENSE_3P2V);
//        ptd_adc1_VBAT_3P7.evaluate(adc1_VBAT_3p7);
        ptd_adc1_VBAT_4P2.evaluate(StaticFields.VSENSE_4P2V);
//        ptd_delta_3p7_3p2.evaluate(adc1_VBAT_3p7.subtract(StaticFields.VSENSE_3P2V));
//        ptd_delta_3p7_4p2.evaluate(StaticFields.VSENSE_4P2V.subtract(adc1_VBAT_3p7));
    }
}
