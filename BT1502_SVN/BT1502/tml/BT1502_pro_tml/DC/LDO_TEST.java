package BT1502_pro_tml.DC;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_PMIC;
import besLib.dsa.BesDsa_PMIC.V93K_Resource;
import besLib.dsa.BesDsa_PS1600_Disconnect;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.measurement.ILocalMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.resultaccess.IDigInOutActionResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class LDO_TEST extends TestMethod {
    @In public double VBat=4;
    @In public String spec_measurement;

    public ILocalMeasurement measurement_NoLoad;
    public ILocalMeasurement measurement_Load;
    public IParametricTestDescriptor    ptd_LDO_Vcore_0p8_NoLoad,
                                        ptd_LDO_Vcore_0p6_NoLoad,
                                        ptd_LDO_Vana_NoLoad,
                                        ptd_LDO_Vcodec_NoLoad,
                                        ptd_LDO_Vpa3p3_NoLoad,
                                        ptd_LDO_Vpa1p8_NoLoad,
                                        ptd_LDO_Vio_NoLoad,
                                        ptd_LDO_Vpad_NoLoad,
                                        ptd_LDO_Vusb_NoLoad,
                                        ptd_LDO_V1p5_NoLoad;

    public IParametricTestDescriptor    ptd_LDO_Vcore_0p8_dp,
                                        ptd_LDO_Vcore_0p6_dp,
                                        ptd_LDO_Vana_dp,
                                        ptd_LDO_Vcodec_dp,
                                        ptd_LDO_Vpa3p3_dp,
                                        ptd_LDO_Vpa1p8_dp,
                                        ptd_LDO_Vpad_dp,
                                        ptd_LDO_Vusb_dp,
                                        ptd_LDO_Vio_dp;

    @Override
    public void setup() {

        /************* measurement_NoLoad *****************/
        IDeviceSetup ds1 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        /**************************** disconnect & Hiz Mode ************************************************/
        BesDsa_PS1600_Disconnect relay_PS1600_1=new BesDsa_PS1600_Disconnect(ds1);
        String activePin_PS1600_1=relay_PS1600_1.getPinGroup(context, spec_measurement, "All_Digital");
        activePin_PS1600_1=activePin_PS1600_1.replaceAll("I2C_SCL\\+", "").replaceAll("I2C_SDA\\+", "").replaceAll("POWKEY\\+", "").replaceAll("RESETN\\+", "");
        relay_PS1600_1.disconnectAll_DigInOut(activePin_PS1600_1, false); //disconnect digital pins to Hiz mode
        /***************************************************************************************************/


        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VCORE_0p8", true, "Vm_VCORE_0p8", V93K_Resource.DPS128);
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VCORE_0p6", true, "Vm_VCORE_0p6", V93K_Resource.DPS128);
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VANA", true, "Vm_VANA", V93K_Resource.DPS128);
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VCODEC", true, "Vm_VCODEC", V93K_Resource.DPS128);
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VPA_3P3", true, "Vm_VPA3P3", V93K_Resource.DPS128);
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VIO", true, "Vm_VIO", V93K_Resource.DPS128);
        new BesDsa_PMIC(ds1).vMeas_PS1600_DigInOut_Hiz("VUSB", true, "Vm_VUSB");
//        new BesDsa_PMIC(ds1).vMeas_PS1600_DigInOut_Hiz("VOUT_1P5", true, "Vm_V1P5");

//        // PS1600 channel for CP Test
//        {
////            new BesDsa_PMIC(ds1).vMeas_PS1600_DigInOut_Std("VPA", "0 uA", "3.5 V", "0 V", "Vm_VPA");
//            //new BesDsa_PMIC(ds1).vMeas_PS1600_DigInOut_Hiz("VPA", "Vm_VPA");
//            new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VPA", true, "Vm_VPA", V93K_Resource.PS1600);
//            new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VPAD", true, "Vm_VPAD", V93K_Resource.PS1600);
//            new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VUSB", true, "Vm_VUSB", V93K_Resource.PS1600);
//
//        }
//        // DPS128-HC channel for FT Test
//        {
//            new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VPA", true, "Vm_VPA", V93K_Resource.DPS128);
//            new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VPAD", true, "Vm_VPAD", V93K_Resource.DPS128);
//            new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VUSB", true, "Vm_VUSB", V93K_Resource.DPS128);
//        }

      //operating sequence
        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement_NoLoad, I2cRegAddrBits.RegAddr_16Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        i2c.transactionSequenceBegin("LDO");
            i2c.write(BesI2cAddrType.PMUIntern,0xd6,0x2f64);     //added by swj 20220121
            i2c.write(BesI2cAddrType.PMUIntern,0xd7,0xd008);
            i2c.write(BesI2cAddrType.PMUIntern,0xd5,0x6464);
            i2c.waitTime(5e6);
        i2c.transactionSequenceEnd();

        ds1.actionCall("Vm_VCORE_0p8");
        ds1.actionCall("Vm_VCORE_0p6");
        ds1.actionCall("Vm_VANA");
        ds1.actionCall("Vm_VCODEC");
        ds1.actionCall("Vm_VIO");
        ds1.actionCall("Vm_VPA3P3");
//        ds1.actionCall("Vm_VPA");
//        ds1.actionCall("Vm_VPAD");
        ds1.actionCall("Vm_VUSB");
//        ds1.actionCall("Vm_V1P5");

        measurement_NoLoad.setSetups(ds1);



        /************* measurement_Load *****************/
        IDeviceSetup ds2 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds2.importSpec(spec_measurement);

        /**************************** disconnect & Hiz Mode ************************************************/
        BesDsa_PS1600_Disconnect relay_PS1600_2=new BesDsa_PS1600_Disconnect(ds2);
        String activePin_PS1600_2=relay_PS1600_2.getPinGroup(context, spec_measurement, "All_Digital");
        activePin_PS1600_2=activePin_PS1600_2.replaceAll("I2C_SCL\\+", "").replaceAll("I2C_SDA\\+", "").replaceAll("POWKEY\\+", "").replaceAll("RESETN\\+", "");
        relay_PS1600_2.disconnectAll_DigInOut(activePin_PS1600_2, false); //disconnect digital pins to Hiz mode
        /***************************************************************************************************/


        new BesDsa_PMIC(ds2).vMeas_DPS128_DcVI_Std("VCORE_0p8", "-20 mA", "0.8 V", "100 mA", "6 V", "0 V", true, "iforce_20mA_VCORE_0p8", "vmeas_20mA_VCORE_0p8");
        new BesDsa_PMIC(ds2).vMeas_DPS128_DcVI_Std("VCORE_0p6", "-10 mA", "0.8 V", "10 mA", "6 V", "0 V", true, "iforce_20mA_VCORE_0p6", "vmeas_20mA_VCORE_0p6");
        new BesDsa_PMIC(ds2).vMeas_DPS128_DcVI_Std("VANA", "-10 mA", "1.3 V", "20 mA", "6 V", "0 V", true, "iforce_20mA_VANA", "vmeas_20mA_VANA");
        new BesDsa_PMIC(ds2).vMeas_DPS128_DcVI_Std("VCODEC", "-10 mA", "1.8 V", "20 mA", "6 V", "0 V", true, "iforce_20mA_VCODEC", "vmeas_20mA_VCODEC");
        new BesDsa_PMIC(ds2).vMeas_DPS128_DcVI_Std("VIO", "-10 mA", "0.8 V", "20 mA", "6 V", "0 V", true, "iforce_20mA_VIO", "vmeas_20mA_VIO");
        if (StaticFields.lb_name=="WLCSP_8_SITE") {
            new BesDsa_PMIC(ds2).vMeas_DPS128_DcVI_Std("VPA_3P3", "-10 mA", "0.8 V", "20 mA", "5.5 V", "0 V", true, "iforce_20mA_VPA_3P3", "vmeas_20mA_VPA_3P3");
        }
        else {
            new BesDsa_PMIC(ds2).vMeas_PS1600_DcVI_Std("VPA_3P3",  "-10 mA", "4 V", "0 V", true, "iforce_20mA_VPA_3P3", "vmeas_20mA_VPA_3P3");
        }
//        new BesDsa_PMIC(ds2).vMeas_DPS128_DcVI_Std("VPAD", "-10 mA", "1.3 V", "20 mA", "6 V", "0 V", true, "iforce_20mA_VPAD", "vmeas_20mA_VPAD");
//        new BesDsa_PMIC(ds2).vMeas_DPS128_DcVI_Std("VUSB", "-10 mA", "1.8 V", "20 mA", "6 V", "0 V", true, "iforce_20mA_VUSB", "vmeas_20mA_VUSB");

        ds2.sequentialBegin();
        ds2.actionCall("iforce_20mA_VCORE_0p8");
        ds2.actionCall("vmeas_20mA_VCORE_0p8");
        ds2.actionCall("iforce_20mA_VCORE_0p6");
        ds2.actionCall("vmeas_20mA_VCORE_0p6");
            ds2.actionCall("iforce_20mA_VANA");
            ds2.actionCall("vmeas_20mA_VANA");
            ds2.actionCall("iforce_20mA_VCODEC");
            ds2.actionCall("vmeas_20mA_VCODEC");
            ds2.actionCall("iforce_20mA_VIO");
            ds2.actionCall("vmeas_20mA_VIO");
            ds2.actionCall("iforce_20mA_VPA_3P3");
            ds2.actionCall("vmeas_20mA_VPA_3P3");
//            ds2.actionCall("iforce_20mA_VPAD");
//            ds2.actionCall("vmeas_20mA_VPAD");
//            ds2.actionCall("iforce_20mA_VUSB");
//            ds2.actionCall("vmeas_20mA_VUSB");
        ds2.sequentialEnd();
        measurement_Load.setSetups(ds2);
    }

    @Override
    public void update() {
        measurement_NoLoad.spec().setVariable("Vbat", VBat);
        measurement_Load.spec().setVariable("Vbat", VBat);
    }

    @Override
    public void execute() {
        measurement_NoLoad.execute();
        IDcVIResults res_NoLoad_DcVI = measurement_NoLoad.dcVI().preserveResults();

        MultiSiteDouble vcore_0p8_NoLoad = res_NoLoad_DcVI.vmeas("Vm_VCORE_0p8").getVoltage("VCORE_0p8").getElement(0);
        MultiSiteDouble vcore_0p6_NoLoad = res_NoLoad_DcVI.vmeas("Vm_VCORE_0p6").getVoltage("VCORE_0p6").getElement(0);
        MultiSiteDouble vana_NoLoad = res_NoLoad_DcVI.vmeas("Vm_VANA").getVoltage("VANA").getElement(0);
        MultiSiteDouble vcodec_NoLoad = res_NoLoad_DcVI.vmeas("Vm_VCODEC").getVoltage("VCODEC").getElement(0);
        MultiSiteDouble vio_NoLoad = res_NoLoad_DcVI.vmeas("Vm_VIO").getVoltage("VIO").getElement(0);
        MultiSiteDouble vpa3p3_NoLoad = res_NoLoad_DcVI.vmeas("Vm_VPA3P3").getVoltage("VPA_3P3").getElement(0);
//        MultiSiteDouble vpad_NoLoad=res_NoLoad_DcVI.vmeas("Vm_VPAD").getVoltage("VPAD").getElement(0);

        IDigInOutActionResults res_NoLoad_Diginout=measurement_NoLoad.digInOut().preserveActionResults();
        MultiSiteDouble vusb_NoLoad=res_NoLoad_Diginout.vmeas("Vm_VUSB").getVoltage("VUSB").getElement(0);
//        MultiSiteDouble v1p5_NoLoad=res_NoLoad_Diginout.vmeas("Vm_V1P5").getVoltage("VOUT_1P5").getElement(0);

        measurement_Load.execute();
        IDcVIResults res_Load_DcVI = measurement_Load.dcVI().preserveResults();

        MultiSiteDouble vcore_0p8_Load = res_Load_DcVI.vmeas("vmeas_20mA_VCORE_0p8").getVoltage("VCORE_0p8").getElement(0);
        MultiSiteDouble vcore_0p8_dp = vcore_0p8_NoLoad.subtract(vcore_0p8_Load);
        MultiSiteDouble vcore_0p6_Load = res_Load_DcVI.vmeas("vmeas_20mA_VCORE_0p6").getVoltage("VCORE_0p6").getElement(0);
        MultiSiteDouble vcore_0p6_dp = vcore_0p6_NoLoad.subtract(vcore_0p6_Load);
        MultiSiteDouble vana_Load = res_Load_DcVI.vmeas("vmeas_20mA_VANA").getVoltage("VANA").getElement(0);
        MultiSiteDouble vana_dp = vana_NoLoad.subtract(vana_Load);
        MultiSiteDouble vcodec_Load = res_Load_DcVI.vmeas("vmeas_20mA_VCODEC").getVoltage("VCODEC").getElement(0);
        MultiSiteDouble vcodec_dp = vcodec_NoLoad.subtract(vcodec_Load);
        MultiSiteDouble vio_Load = res_Load_DcVI.vmeas("vmeas_20mA_VIO").getVoltage("VIO").getElement(0);
        MultiSiteDouble vio_dp = vcodec_NoLoad.subtract(vio_Load);
        MultiSiteDouble vpa3p3_Load = res_Load_DcVI.vmeas("vmeas_20mA_VPA_3P3").getVoltage("VPA_3P3").getElement(0);
        MultiSiteDouble vpa3p3_dp = vpa3p3_NoLoad.subtract(vpa3p3_Load);
//        MultiSiteDouble vpad_Load = res_Load_DcVI.vmeas("vmeas_20mA_VPAD").getVoltage("VPAD").getElement(0);
//        MultiSiteDouble vpad_dp = vpad_NoLoad.subtract(vpad_Load);
//        MultiSiteDouble vusb_Load = res_Load_DcVI.vmeas("vmeas_20mA_VUSB").getVoltage("VUSB").getElement(0);
//        MultiSiteDouble vusb_dp = vusb_NoLoad.subtract(vusb_Load);

        if (StaticFields.debugMode) {
            String testSuiteName_Qualified = context.getTestSuiteName();
            String testSuiteName = testSuiteName_Qualified.substring(1 + testSuiteName_Qualified.lastIndexOf("."));
            println();
            println("**********" + testSuiteName + "**********");
            println("vcore_0p8_NoLoad [V] = " + vcore_0p8_NoLoad);
            println("vcore_0p8_LD   [V] = " + vcore_0p8_Load);
            println("VCORE_0p8_dp   [mV]= " + vcore_0p8_dp.multiply(1e3));

            println("vana_NoLoad      [V] = " + vana_NoLoad);
            println("VANA_LD        [V] = " + vana_Load);
            println("VANA_dp        [mV]= " + vana_dp.multiply(1e3));

            println("vcodec_NoLoad    [V] = " + vcodec_NoLoad);
            println("VCODEC_LD      [V] = " + vcodec_Load);
            println("VCODEC_dp      [mV]= " + vcodec_dp.multiply(1e3));

//            println("vpa_NoLoad       [V] = " + vpa_NoLoad);
//            println("vpa_NoLoad       [V] = " + vpa_Load);
//            println("vpa_dp         [mV]= " + vpa_dp.multiply(1e3));
//
//            println("vpad_NoLoad      [V] = " + vpad_NoLoad);
//            println("VPAD_LD        [V] = " + vpad_Load);
//            println("vpad_dp        [mV]= " + vpad_dp.multiply(1e3));
//
//            println("vusb_NoLoad      [V] = " + vusb_NoLoad);
//            println("VUSB_LD        [V] = " + vusb_Load);
//            println("vusb_dp        [mV]= " + vusb_dp.multiply(1e3));
            println();
        }

        ptd_LDO_Vcore_0p8_NoLoad.evaluate(vcore_0p8_NoLoad);
        ptd_LDO_Vcore_0p6_NoLoad.evaluate(vcore_0p6_NoLoad);
        ptd_LDO_Vana_NoLoad.evaluate(vana_NoLoad);
        ptd_LDO_Vcodec_NoLoad.evaluate(vcodec_NoLoad);
        ptd_LDO_Vio_NoLoad.evaluate(vio_NoLoad);
        ptd_LDO_Vpa3p3_NoLoad.evaluate(vpa3p3_NoLoad);
//        ptd_LDO_Vpad_NoLoad.evaluate(vpad_NoLoad);
        ptd_LDO_Vusb_NoLoad.evaluate(vusb_NoLoad);
//        ptd_LDO_V1p5_NoLoad.evaluate(v1p5_NoLoad);

        ptd_LDO_Vcore_0p8_dp.evaluate(vcore_0p8_dp);
        ptd_LDO_Vcore_0p6_dp.evaluate(vcore_0p6_dp);
        ptd_LDO_Vana_dp.evaluate(vana_dp);
        ptd_LDO_Vcodec_dp.evaluate(vcodec_dp);
        ptd_LDO_Vio_dp.evaluate(vio_dp);
        ptd_LDO_Vpa3p3_dp.evaluate(vpa3p3_dp);
//        ptd_LDO_Vpad_dp.evaluate(vpad_dp);
//        ptd_LDO_Vusb_dp.evaluate(vusb_dp);

    }
}
