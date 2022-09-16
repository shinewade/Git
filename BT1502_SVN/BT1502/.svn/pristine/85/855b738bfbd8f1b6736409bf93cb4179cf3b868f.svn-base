package BT1502_pro_tml.DC;

import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_PMIC;
import besLib.dsa.BesDsa_PMIC.V93K_Resource;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class LDO_CAL extends TestMethod {
    @In public String spec_measurement;

    public IMeasurement measurement;

    public IParametricTestDescriptor    ptd_Vcore_Before,
                                        ptd_Vana_Before,
                                        ptd_Vcodec_Before;

    public IParametricTestDescriptor    ptd_Vcore_After,
                                        ptd_Vana_After,
                                        ptd_Vcodec_After;

    public IParametricTestDescriptor    ptd_Vcore_Offset,
                                        ptd_Vana_Offset,
                                        ptd_Vcodec_Offset;

    public IParametricTestDescriptor    ptd_Vcore_CalShift,
                                        ptd_Vana_CalShift,
                                        ptd_Vcodec_CalShift;

    //********NOTE********//
    private MultiSiteLong ldo_data1=new MultiSiteLong(0xa0a0);//update by wenqiang 20211231
    private MultiSiteLong ldo_vio=new MultiSiteLong(0x5b0c);//update by wenqiang 20211231

    private MultiSiteLong ldo1_data=new MultiSiteLong(0x430c);//VCORE_0p6 vcore_logic
    private MultiSiteLong ldo2_data=new MultiSiteLong(0x730c);//VCORE_0p8 vcore_memory

    @Override
    public void setup() {
        //measurement1
        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VCORE_0p6", true, "Vm_VCORE_0p6", V93K_Resource.DPS128);
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VCORE_0p8", true, "Vm_VCORE_0p8", V93K_Resource.DPS128);

        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");
        ds1.sequentialBegin();

        i2c.transactionSequenceBegin("LDO_CAL_1");
            i2c.write(BesI2cAddrType.PMUIntern,0xd6,0x2f64);
            i2c.write(BesI2cAddrType.PMUIntern,0xd7,0xd008);
            i2c.write(BesI2cAddrType.PMUIntern,0x08,"dynamicWrite_0x08");
            i2c.write(BesI2cAddrType.PMUIntern,0xd5,"dynamicWrite_0xd5");
            i2c.read(BesI2cAddrType.PMUIntern, 0x08,"readValue_0x08");
            i2c.read(BesI2cAddrType.PMUIntern, 0xd5,"readValue_0xd5");
        i2c.transactionSequenceEnd();

        ds1.actionCall("Vm_VCORE_0p6");
        ds1.actionCall("Vm_VCORE_0p8");
        ds1.sequentialEnd();
        measurement.setSetups(ds1);
    }

    @Override
    public void execute() {

        double step_size=0.025;
        ldo1_data.set(0xa0a0);
        ldo2_data.set(0xa0a0);//TBD
        ProtocolAccess.setDynamicData("dynamicWrite_0x08", ldo1_data);
        ProtocolAccess.setDynamicData("dynamicWrite_0xd5", ldo2_data);
        ProtocolAccess.updateDynamicData(context, measurement);
        measurement.execute();

        IDcVIResults DPSResult1=measurement.dcVI().preserveResults();
        MultiSiteDouble ldo_cal_vcore_0p6_1=DPSResult1.vmeas("Vm_VCORE_0p6").getVoltage("VCORE_0p6").getElement(0);
        MultiSiteDouble ldo_cal_vcore_0p8_1=DPSResult1.vmeas("Vm_VCORE_0p8").getVoltage("VCORE_0p8").getElement(0);

        MultiSiteDouble target_vol_ldo_vcore_0p6 = new MultiSiteDouble(0.60);
        MultiSiteDouble target_vol_ldo_vcore_0p8 = new MultiSiteDouble(0.80);
        MultiSiteLong step_cnt_ldo1 = new MultiSiteLong();
        MultiSiteLong step_cnt_ldo2 = new MultiSiteLong();

        for(int site:context.getActiveSites())
        {
            step_cnt_ldo1.set(site, Math.round(target_vol_ldo_vcore_0p6.subtract(ldo_cal_vcore_0p6_1).divide(step_size).get(site)));
            step_cnt_ldo2.set(site, Math.round(target_vol_ldo_vcore_0p8.subtract(ldo_cal_vcore_0p8_1).divide(step_size).get(site)));

            step_cnt_ldo1.set(site,step_cnt_ldo1.get(site) >31  ?  31:step_cnt_ldo1.get(site));//5 bit efuse
            step_cnt_ldo2.set(site,step_cnt_ldo2.get(site) >31  ?  31:step_cnt_ldo2.get(site));//5 bit efuse
            step_cnt_ldo1.set(site,step_cnt_ldo1.get(site) <-12 ? -12:step_cnt_ldo1.get(site));
            step_cnt_ldo2.set(site,step_cnt_ldo2.get(site) <-12 ? -12:step_cnt_ldo2.get(site));
        }
        MultiSiteLong ldo1_cal_data = new MultiSiteLong();
        MultiSiteLong ldo2_cal_data = new MultiSiteLong();
        ldo1_cal_data = ldo1_data.add(step_cnt_ldo1);
        ldo2_cal_data = ldo2_data.add(step_cnt_ldo2);

//        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("TARGET_VOL_VCORE_0p6  [V] = "+target_vol_ldo_vcore_0p6);
            println("TARGET_VOL_VCORE_0p8  [V] = "+target_vol_ldo_vcore_0p8);
            println("LDO_CAL_VCORE_0p6_pre  [V] = "+ldo_cal_vcore_0p6_1);
            println("LDO_CAL_VCORE_0p8_pre  [V] = "+ldo_cal_vcore_0p8_1);
            println();
            for(int site:context.getActiveSites())
            {
                println("site"+site+": ldo1_init_data = 0x"+Long.toHexString(ldo1_data.get(site)));
                println("site"+site+": ldo2_init_data = 0x"+Long.toHexString(ldo2_data.get(site)));
                println();
            }
            println("step_cnt_ldo1             = "+step_cnt_ldo1);
            println("step_cnt_ldo2             = "+step_cnt_ldo2);
            println();
            for(int site:context.getActiveSites())
            {
                println("site"+site+": ldo1_cal_data  = 0x"+Long.toHexString(ldo1_cal_data.get(site)));
                println("site"+site+": ldo2_cal_data  = 0x"+Long.toHexString(ldo2_cal_data.get(site)));
                println();
            }
        }
        ProtocolAccess.setDynamicData("dynamicWrite_0x08", ldo1_cal_data);
        ProtocolAccess.setDynamicData("dynamicWrite_0xd5", ldo2_cal_data);
        ProtocolAccess.updateDynamicData(context, measurement);
        measurement.execute();
        IDcVIResults DPSResult2=measurement.dcVI().preserveResults();
        MultiSiteDouble ldo_cal_vcore_0p6_2=DPSResult2.vmeas("Vm_VCORE_0p6").getVoltage("VCORE_0p6").getElement(0);
        MultiSiteDouble ldo_cal_vcore_0p8_2=DPSResult2.vmeas("Vm_VCORE_0p8").getVoltage("VCORE_0p8").getElement(0);

        MultiSiteDouble offset_ldo1=ldo_cal_vcore_0p6_2.subtract(ldo_cal_vcore_0p6_1);
        MultiSiteDouble offset_ldo2=ldo_cal_vcore_0p8_2.subtract(ldo_cal_vcore_0p8_1);

//        StaticFields.cal_shift_ldo1.set(step_cnt_ldo1);
//        StaticFields.cal_shift_ldo2.set(step_cnt_ldo2);

        if(StaticFields.debugMode)
        {
            println("LDO_CAL_VCORE_0p6_post [V] = "+ldo_cal_vcore_0p6_2);
            println("LDO_CAL_VCORE_0p8_post [V] = "+ldo_cal_vcore_0p8_2);
            println("OFFSET_VCORE_0p6      [V] = "+offset_ldo1);
            println("OFFSET_VCORE_0p8      [V] = "+offset_ldo2);
            println("CAL_RESOLUTION_DCDC1 [mV] = "+ldo_cal_vcore_0p6_2.subtract(ldo_cal_vcore_0p6_1).divide(step_cnt_ldo1).multiply(1e3));
            println("CAL_RESOLUTION_DCDC2 [mV] = "+ldo_cal_vcore_0p8_2.subtract(ldo_cal_vcore_0p8_1).divide(step_cnt_ldo2).multiply(1e3));
//            println("cal_shift_ldo1            = "+StaticFields.cal_shift_ldo1);
//            println("cal_shift_ldo2            = "+StaticFields.cal_shift_ldo2);
            println();
        }
//        ptd_Vcore_Before.evaluate(vcore_NoLoad);
//        ptd_Vana_Before.evaluate(vana_NoLoad);
//        ptd_Vcodec_Before.evaluate(vcodec_NoLoad);
//
//        ptd_Vcore_After.evaluate(vcore_NoLoad2);
//        ptd_Vana_After.evaluate(vana_NoLoad2);
//        ptd_Vcodec_After.evaluate(vcodec_NoLoad2);
//
//        ptd_Vcore_Offset.evaluate(offset_Vcore);
//        ptd_Vana_Offset.evaluate(offset_Vana);
//        ptd_Vcodec_Offset.evaluate(offset_Vcodec);
//
//        ptd_Vcore_CalShift.evaluate(StaticFields.cal_Shift_Vcore);
//        ptd_Vana_CalShift.evaluate(StaticFields.cal_Shift_Vana);
//        ptd_Vcodec_CalShift.evaluate(StaticFields.cal_Shift_Vcodec);





    }
}
