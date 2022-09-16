
package BT1502_pro_tml.DC;

import java.util.Map;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_PMIC;
import besLib.dsa.BesDsa_PS1600_Disconnect;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDoubleArray;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigInOutActionResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class IIL_IIH_TEST extends TestMethod {

    @In public String spec_measurement;
    public IMeasurement measurement;
    public IParametricTestDescriptor    ptd_passCurrentLow_uA,
                                        ptd_passCurrentHigh_uA;

    private String activePin_GPIO;

    @Override
    public void setup()
    {
        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        activePin_GPIO=new BesDsa_PS1600_Disconnect(ds1).getPinGroup(context, spec_measurement, "GPIO_GRP");
     //   activePin_GPIO=activePin_GPIO.concat("+FLASH0_CS + FLASH0_SO + FLASH0_SI + FLASH0_CLK + FLASH0_HOLD + FLASH0_WP");
        //GPIO
        new BesDsa_PMIC(ds1).iMeas_PS1600_DigInOut_Std(activePin_GPIO, "1.8 V", "150 uA", "10 ms", true, "VfIm_H_Parallel");
        new BesDsa_PMIC(ds1).iMeas_PS1600_DigInOut_Std(activePin_GPIO, "0.0 V", "50 uA", "10 ms", true, "VfIm_L_Parallel");

//        new BesDsa_PMIC(ds1).iMeas_PS1600_DigInOut_Std(activePin_GPIO, "1.8 V", "1 mA", "10 ms", true, "VfIm_H_Parallel");
//        new BesDsa_PMIC(ds1).iMeas_PS1600_DigInOut_Std(activePin_GPIO, "0.0 V", "40 mA", "10 ms", true, "VfIm_L_Parallel");

        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_16Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        i2c.transactionSequenceBegin("GPIO_Leakage_Mode");
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008602c,0x00000000);//pu
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086030,0x00000000);//pd
            i2c.write(BesI2cAddrType.DIGITAL, 0x40084094,0x00000000);//pu
            i2c.write(BesI2cAddrType.DIGITAL, 0x40084098,0x00000000);//pd
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008409C,0x00000000);//pdpu
            i2c.waitTime(10e6);
        i2c.transactionSequenceEnd();

        ds1.actionCall("VfIm_H_Parallel");
        ds1.actionCall("VfIm_L_Parallel");

        measurement.setSetups(ds1);
    }


    @Override
    public void execute() {

        measurement.execute();
        IDigInOutActionResults Result= measurement.digInOut(activePin_GPIO).preserveActionResults();
        Map<String, MultiSiteDoubleArray> Res_IIL=Result.vforceImeas("VfIm_L_Parallel").getCurrent();
        Map<String, MultiSiteDoubleArray> Res_IIH=Result.vforceImeas("VfIm_H_Parallel").getCurrent();


        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            for(String PinName:Res_IIL.keySet()) {
                println("IIL_ "+PinName+" = "+Res_IIL.get(PinName).getElement(0));
            }
            for(String PinName:Res_IIH.keySet()) {
                println("IIH_ "+PinName+" = "+Res_IIH.get(PinName).getElement(0));
            }
            println();
        }

        ptd_passCurrentLow_uA.evaluate(Res_IIL,0,measurement);
        ptd_passCurrentHigh_uA.evaluate(Res_IIH,0,measurement);
    }
}

