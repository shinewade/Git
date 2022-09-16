package BT1502_pro_tml.DC;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_PS1600_Disconnect;
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


public class POWER_OFF_CURRENT extends TestMethod {

    @In public String spec_measurement;
    public IMeasurement measurement;
    public IParametricTestDescriptor ptd_I_vbat_off;

    @Override
    public void setup ()
    {
        IDeviceSetup ds1 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);


        BesDsa_PS1600_Disconnect relay_PS1600=new BesDsa_PS1600_Disconnect(ds1);

        String activePin_PS1600=relay_PS1600.getPinGroup(context, spec_measurement, "All_Digital");
        String disconnectPin_PS1600=activePin_PS1600.replaceAll("RESETN\\+", "")
                                                    .replaceAll("POWKEY\\+", "")
                                                    .replaceAll("I2C_SCL\\+", "")
                                                    .replaceAll("I2C_SDA\\+", "");
        relay_PS1600.disconnectAll_DigInOut(disconnectPin_PS1600, false); //disconnect digital pins to Hiz mode

        ISetupDcVI  dcVi_VBAT=ds1.addDcVI("VSYS").setDisconnect(false);
        dcVi_VBAT.imeas("imeas_VBAT").setIrange("125 uA").setAverages(128).setWaitTime("5 ms").setRestoreIrange(true);

        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        //operating Sequence
        i2c.transactionSequenceBegin("power_off_current");
            i2c.write(BesI2cAddrType.PMUIntern, 0x4f, 0x5);
            i2c.write(BesI2cAddrType.PMUIntern, 0x4f, 0x5);
        i2c.transactionSequenceEnd();

        ds1.waitCall("40 ms");
        ds1.actionCall("imeas_VBAT");


        measurement.setSetups(ds1);
    }

    @Override
    public void execute ()
    {
        measurement.execute();
        IDcVIResults dpsResult = measurement.dcVI("VSYS").preserveResults();
        MultiSiteDouble offCurrRslt=dpsResult.imeas("imeas_VBAT").getCurrent("VSYS").getElement(0);

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("I_power_down_Vbat[uA] = "+offCurrRslt.multiply(1e6));
            println();
        }

        ptd_I_vbat_off.evaluate(offCurrRslt);
    }
}
