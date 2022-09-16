package BT1502_pro_tml.DC;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

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
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteDoubleArray;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigInOutActionResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class GPIO_TEST_OUTPUT_GANG extends TestMethod {

    @In public String spec_measurement;
    public IMeasurement measurement;
    public IParametricTestDescriptor    ptd_Vmeas_GPIO_H_1,
                                        ptd_Vmeas_GPIO_L_1,
                                        ptd_Vmeas_GPIO_H_2,
                                        ptd_Vmeas_GPIO_L_2;

    private String activePin_GPIO;

    @Override
    public void setup()
    {
        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        activePin_GPIO=new BesDsa_PS1600_Disconnect(ds1).getPinGroup(context, spec_measurement, "GPIO_GRP");
//        activePin_GPIO=activePin_GPIO+"+SDIO_CLK+SDIO_CMD+SDIO_D0+SDIO_D1+SDIO_D2+SDIO_D3";
        //System.out.println("GPIO_Output: activePin_GPIO= "+activePin_GPIO);
        new BesDsa_PMIC(ds1).vMeas_PS1600_DigInOut_Hiz(activePin_GPIO, true, "Vmeas_GPIO");


        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        i2c.transactionSequenceBegin("GPIO_Output_Mode");
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086004,0xffffffff);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086008,0xffffffff);
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008600c,0xffff00ff);
            if (StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086010, 0xffffffff); // GPIO 30-37 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008607C, 0xffffffff); // GPIO 40-47 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086080, 0xffffffff); // GPIO 50-57 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086084, 0xffffffff); // GPIO 60-67 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086088, 0xffffffff); // GPIO 70-77 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008608C, 0xffffffff); // GPIO 80-87 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086090, 0xffffffff); // GPIO 90-97 0721 wuhan
            }
            i2c.waitTime(10e6);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40081004,0xfff3ffff); //????
            if (StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40089004,0xffffffff); //????
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A004,0xffffffff); //????
            }
            i2c.waitTime(10e6);

            i2c.write(BesI2cAddrType.DIGITAL, 0x4008100c,0x0);//lo_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x40081000,0xfff3ffff);
            if (StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008900c,0x0);//lo_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x40089000,0xffffffff);
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A00c,0x0);//lo_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A000,0xffffffff);
            }
            i2c.waitTime(10e6);

            i2c.write(BesI2cAddrType.DIGITAL, 0x40081000,0x0);//hi_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008100c,0xfff3ffff);
            if (StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40089000,0x0);//hi_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008900c,0xffffffff);
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A000,0x0);//hi_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A00c,0xffffffff);
            }
            i2c.waitTime(10e6);
        i2c.transactionSequenceEnd();

        i2c.transactionSequenceBegin("write_GPIO_State_H_1");
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008100c,0x0);//lo_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x40081000,0xfff3ffff);
            if (StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008900c,0x0);//lo_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x40089000,0xffffffff);
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A00c,0x0);//lo_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A000,0xffffffff);
            }
        i2c.transactionSequenceEnd();
        ds1.waitCall("20 ms");
        ds1.actionCall("Vmeas_GPIO");

        i2c.transactionSequenceBegin("write_GPIO_State_L_1");
            i2c.write(BesI2cAddrType.DIGITAL, 0x40081000,0x0);//hi_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008100c,0xfff3ffff);
            if(StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40089000,0x0);//hi_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008900c,0xffffffff);
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A000,0x0);//hi_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A00c,0xffffffff);
            }
        i2c.transactionSequenceEnd();
        ds1.waitCall("20 ms");
        ds1.actionCall("Vmeas_GPIO");

        i2c.transactionSequenceBegin("write_GPIO_State_H_2");
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008100c,0x0);//lo_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x40081000,0xfff3ffff);
            if(StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008900c,0x0);//lo_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x40089000,0xffffffff);
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A00c,0x0);//lo_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A000,0xffffffff);
            }
        i2c.transactionSequenceEnd();
        ds1.waitCall("20 ms");
        ds1.actionCall("Vmeas_GPIO");

        i2c.transactionSequenceBegin("write_GPIO_State_L_2");
            i2c.write(BesI2cAddrType.DIGITAL, 0x40081000,0x0);//hi_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008100c,0xfff3ffff);
            if(StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40089000,0x0);//hi_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008900c,0xffffffff);
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A000,0x0);//hi_off
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A00c,0xffffffff);
            }
        i2c.transactionSequenceEnd();
        ds1.waitCall("20 ms");
        ds1.actionCall("Vmeas_GPIO");


        measurement.setSetups(ds1);
    }


    @Override
    public void execute() {
        measurement.execute();
        IDigInOutActionResults res_DigInOut=measurement.digInOut(activePin_GPIO).preserveActionResults();
        Map<String, MultiSiteDoubleArray> res_GPIO_Array=res_DigInOut.vmeas("Vmeas_GPIO").getVoltage();

        @SuppressWarnings("unchecked")
        Map<String, MultiSiteDouble> Vmeas_GPIO_H_1=new HashedMap();
        @SuppressWarnings("unchecked")
        Map<String, MultiSiteDouble> Vmeas_GPIO_L_1=new HashedMap();
        @SuppressWarnings("unchecked")
        Map<String, MultiSiteDouble> Vmeas_GPIO_H_2=new HashedMap();
        @SuppressWarnings("unchecked")
        Map<String, MultiSiteDouble> Vmeas_GPIO_L_2=new HashedMap();
        for(String Pin :res_GPIO_Array.keySet()) {
            Vmeas_GPIO_H_1.put(Pin, res_GPIO_Array.get(Pin).getElement(0));
            Vmeas_GPIO_L_1.put(Pin, res_GPIO_Array.get(Pin).getElement(1));
            Vmeas_GPIO_H_2.put(Pin, res_GPIO_Array.get(Pin).getElement(2));
            Vmeas_GPIO_L_2.put(Pin, res_GPIO_Array.get(Pin).getElement(3));
        }


        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println();
        }
        ptd_Vmeas_GPIO_H_1.evaluate(Vmeas_GPIO_H_1, measurement);
        ptd_Vmeas_GPIO_L_1.evaluate(Vmeas_GPIO_L_1, measurement);
        ptd_Vmeas_GPIO_H_2.evaluate(Vmeas_GPIO_H_2, measurement);
        ptd_Vmeas_GPIO_L_2.evaluate(Vmeas_GPIO_L_2, measurement);
    }
}

