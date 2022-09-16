package BT1502_pro_tml.DC;

import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_PS1600_Disconnect;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDigInOut;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigInOutActionResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class ADC_Vref extends TestMethod {

    @In
    public String spec_measurement;
    public IMeasurement measurement;

    public IParametricTestDescriptor ptd_ADC_VREF;
    private String  activePin_GPIO;

    @Override
    public void setup() {
        // TODO Auto-generated method stub
        // measurement
        IDeviceSetup ds1 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        /****************************
         * disconnect & Hiz Mode
         ************************************************/
        BesDsa_PS1600_Disconnect relay_PS1600_1 = new BesDsa_PS1600_Disconnect(ds1);
        String activePin_PS1600_1 = relay_PS1600_1.getPinGroup(context, spec_measurement,
                "All_Digital");
        activePin_PS1600_1 = activePin_PS1600_1.replaceAll("I2C_SCL\\+", "")
                .replaceAll("I2C_SDA\\+", "").replaceAll("POWKEY\\+", "")
                .replaceAll("RESETN\\+", "");
        relay_PS1600_1.disconnectAll_DigInOut(activePin_PS1600_1, false); // disconnect digital pins
                                                                          // to Hiz mode
        /***************************************************************************************************/

         activePin_GPIO = "ADC_VREF";
        ISetupDigInOut digInOut_GPIO=ds1.addDigInOut(activePin_GPIO).setConnect(true).setDisconnect(true);
        digInOut_GPIO.protection().setDisconnectPulldownState(false);

        digInOut_GPIO.vmeas("Vm_ADC_VREF").setAverages(8).setDirectEnabled().setHighAccuracy(true);

        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");
        // operating sequence
        ds1.sequentialBegin();

        i2c.transactionSequenceBegin("Vref_config");
        i2c.write(BesI2cAddrType.PMUIntern, 0x000D, 0x4492); // gpadc_pu
        i2c.read(BesI2cAddrType.PMUIntern, 0x000D, "read"); // gpadc_pu

        i2c.write(BesI2cAddrType.PMUIntern, 0x0090, 0x6200);
        i2c.write(BesI2cAddrType.PMUIntern, 0x0093, 0x6424);
        i2c.waitTime(5e6);
        i2c.write(BesI2cAddrType.PMUIntern, 0x0018, 0x1000); // interval mode
        i2c.write(BesI2cAddrType.PMUIntern, 0x001C, 0x0003);
        i2c.waitTime(5e6);
        i2c.write(BesI2cAddrType.PMUIntern, 0x001E, 0x1800); // reg_dr_pu_sar reg_pu_sar

        // i2c.read(BesI2cAddrType.PMUIntern,0x000D,"readValue_000D");
        // i2c.read(BesI2cAddrType.PMUIntern,0x0090,"readValue_0090");
        // i2c.read(BesI2cAddrType.PMUIntern,0x0093,"readValue_0093");
        // i2c.read(BesI2cAddrType.PMUIntern,0x0018,"readValue_0018");
        // i2c.read(BesI2cAddrType.PMUIntern,0x001C,"readValue_001C");
        // i2c.read(BesI2cAddrType.PMUIntern,0x001E,"readValue_001E");
        i2c.transactionSequenceEnd();
        ds1.waitCall("35 ms");
        ds1.actionCall("Vm_ADC_VREF");
        ds1.sequentialEnd();

        measurement.setSetups(ds1);
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        int[] activeSites = context.getActiveSites();

        measurement.execute();

        // com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults
        // paRes1=ProtocolAccess.preserveResults(measurement);
        // MultiSiteLong Vcore=paRes1.getResult( "readValue_0x4a");
        // MultiSiteLong Vana=paRes1.getResult( "readValue_0x13");
        // MultiSiteLong Vcodec=paRes1.getResult("readValue_0xc3");

        IDigInOutActionResults res_DigInOut=measurement.digInOut(activePin_GPIO).preserveActionResults();
         com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults paRes1=ProtocolAccess.preserveResults(measurement);
         MultiSiteLong read= paRes1.getResult("read");
        MultiSiteDouble ADC_VREF=res_DigInOut.vmeas("Vm_ADC_VREF").getVoltage(). get("ADC_VREF").getElement(0);
        for(int site:context.getActiveSites()) {

            println("read = 0x"+Long.toHexString(read.get(site))+" [site "+site+"]");

        }

//        if (StaticFields.debugMode) {
            String testSuiteName_Qualified = context.getTestSuiteName();
            String testSuiteName = testSuiteName_Qualified
                    .substring(1 + testSuiteName_Qualified.lastIndexOf("."));
            println("**********" + testSuiteName + "**********");
            println("ADC_VREF   [V]: " + ADC_VREF);
            println();
//        }

        ptd_ADC_VREF.evaluate(ADC_VREF);

    }

}
