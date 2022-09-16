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


public class CHIPID extends TestMethod {

    @In public String spec_measurement;

    public IMeasurement measurement;

    public IParametricTestDescriptor ptd_chip_id;
    public IParametricTestDescriptor ptd_chip_rev;

    public String trCallName = "";

    @Override
    public void setup()
    {
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement);

        BesPA_I2C i2c=new BesPA_I2C(deviceSetup, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        i2c.transactionSequenceBegin("chipID_Read");
            i2c.read( BesI2cAddrType.PMUIntern, 0x0, "dummy");
            i2c.read( BesI2cAddrType.PMUIntern, 0x0, "chipid");
        i2c.transactionSequenceEnd();
        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute ()
    {
        measurement.execute();

        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults paRes=ProtocolAccess.preserveResults(measurement);
        MultiSiteLong chipId = paRes.getResult("chipid");

        MultiSiteLong chipRev = chipId.and(0xf);

        chipId.set( chipId.and(0xfff0).rightShift(4) );

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println();
            println("**********"+testSuiteName+"**********");
            for(int site:context.getActiveSites()) {
                System.out.format("Site=%s, chipId =0x%x",site,chipId.get(site)).println();
                System.out.format("Site=%s, chipRev=0x%x",site,chipRev.get(site)).println();
                System.out.println();
            }
        }

        ptd_chip_id.evaluate(chipId);
        ptd_chip_rev.evaluate(chipRev);

    }
}
