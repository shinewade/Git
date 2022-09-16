package BT1502_pro_tml.DC;

import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

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
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class DCDC_CAL extends TestMethod {

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


    @Override
    public void setup() {
        // TODO Auto-generated method stub
        //measurement
        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        /**************************** disconnect & Hiz Mode ************************************************/
        BesDsa_PS1600_Disconnect relay_PS1600_1=new BesDsa_PS1600_Disconnect(ds1);
        String activePin_PS1600_1=relay_PS1600_1.getPinGroup(context, spec_measurement, "All_Digital");
        activePin_PS1600_1=activePin_PS1600_1.replaceAll("I2C_SCL\\+", "").replaceAll("I2C_SDA\\+", "").replaceAll("POWKEY\\+", "").replaceAll("RESETN\\+", "");
        relay_PS1600_1.disconnectAll_DigInOut(activePin_PS1600_1, false); //disconnect digital pins to Hiz mode
        /***************************************************************************************************/

        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VCORE_0p8", true, "Vm_VCORE", V93K_Resource.DPS128);
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VANA", true, "Vm_VANA", V93K_Resource.DPS128);
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VCODEC", true, "Vm_VCODEC", V93K_Resource.DPS128);

        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        //operating sequence
        ds1.sequentialBegin();

            i2c.transactionSequenceBegin("DCDC_CAL_1");

                i2c.write(BesI2cAddrType.PMUIntern,0x15,0xe300);  //
                i2c.write(BesI2cAddrType.PMUIntern,0x64,0x0c08);  //
                i2c.write(BesI2cAddrType.PMUIntern,0xbf,0x8cf8);  //
                i2c.write(BesI2cAddrType.PMUIntern,0xc1,0xce20);  //cap_bit=1110

                i2c.write(BesI2cAddrType.PMUIntern,0xc0,0x8ad2);//buck3 ix2
                i2c.write(BesI2cAddrType.PMUIntern,0xc7,0xf41f);//buck1 ix2
                i2c.write(BesI2cAddrType.PMUIntern,0xc4,0xf41f);//buck2 ix2
                i2c.write(BesI2cAddrType.PMUIntern,0xca,0x00e7);//buck3 on
                i2c.write(BesI2cAddrType.PMUIntern,0x14,0x0e79);//buck1&2 on

                i2c.write(BesI2cAddrType.PMUIntern,0xd1,0x898c);
                i2c.write(BesI2cAddrType.PMUIntern,0xc8,0x3a88);
                i2c.write(BesI2cAddrType.PMUIntern,0x63,0x1244);
                i2c.write(BesI2cAddrType.PMUIntern,0xc5,0x5a88);
                i2c.write(BesI2cAddrType.PMUIntern,0xc2,0x0836);

                i2c.waitTime(10e6);
                i2c.write(BesI2cAddrType.PMUIntern,0x0e,0x54c6);//ldo codec off
                i2c.write(BesI2cAddrType.PMUIntern,0x07,0x2a11);//ldo ana off
                i2c.write(BesI2cAddrType.PMUIntern,0x08,0x2a06);//ldo core off
                i2c.write(BesI2cAddrType.PMUIntern,0x68,0xb8c6);//ldo mem off 0xb4c6


//                i2c.write(BesI2cAddrType.PMUIntern,0x64,0x0408);
//                i2c.write(BesI2cAddrType.PMUIntern,0xca,0x0063); //dcdc hppa on
//                i2c.write(BesI2cAddrType.PMUIntern,0x14,0x0618); //dcdc ana on
//                i2c.waitTime(5e6);
//                i2c.write(BesI2cAddrType.PMUIntern,0x0e,0xd4c6);  //ldo_hppa off
//                i2c.write(BesI2cAddrType.PMUIntern,0x07,0x2e11);  //ldo_vana off
//                i2c.write(BesI2cAddrType.PMUIntern,0x08,0x2e66);  //ldo_vcore off
//                i2c.write(BesI2cAddrType.PMUIntern,0x68,0xb8c6);  //ldo_vmem off

                i2c.write(BesI2cAddrType.PMUIntern,0x4a,"dynamicWrite_0x4a");//vcore
                i2c.write(BesI2cAddrType.PMUIntern,0x13,"dynamicWrite_0x13");//vana
                i2c.write(BesI2cAddrType.PMUIntern,0xc3,"dynamicWrite_0xc3");//vcodec

                i2c.read(BesI2cAddrType.PMUIntern,0x4a,"readValue_0x4a");
                i2c.read(BesI2cAddrType.PMUIntern,0x13,"readValue_0x13");
                i2c.read(BesI2cAddrType.PMUIntern,0xc3,"readValue_0xc3");
            i2c.transactionSequenceEnd();


            ds1.waitCall("35 ms");
            ds1.actionCall("Vm_VCORE");
            ds1.waitCall("5 ms");
            ds1.actionCall("Vm_VANA");
            ds1.waitCall("5 ms");
            ds1.actionCall("Vm_VCODEC");
        ds1.sequentialEnd();

        measurement.setSetups(ds1);
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        int[] activeSites = context.getActiveSites();

        ProtocolAccess.setDynamicData("dynamicWrite_0x4a", StaticFields.regValue_DCBuck_Vcore);
        ProtocolAccess.setDynamicData("dynamicWrite_0x13", StaticFields.regValue_DCBuck_Vana);
        ProtocolAccess.setDynamicData("dynamicWrite_0xc3", StaticFields.regValue_DCBuck_Vcodec);
        ProtocolAccess.updateDynamicData(context, measurement);
        measurement.execute();

        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults paRes1=ProtocolAccess.preserveResults(measurement);
        MultiSiteLong Vcore=paRes1.getResult( "readValue_0x4a");
        MultiSiteLong Vana=paRes1.getResult(  "readValue_0x13");
        MultiSiteLong Vcodec=paRes1.getResult("readValue_0xc3");

        IDcVIResults res1_DcVI=measurement.dcVI().preserveResults();
        MultiSiteDouble vcore_NoLoad=res1_DcVI.vmeas("Vm_VCORE").getVoltage("VCORE_0p8").getElement(0);//0x4a 0x5050 1.2V
        MultiSiteDouble vana_NoLoad=res1_DcVI.vmeas("Vm_VANA").getVoltage("VANA").getElement(0);//0x13 0x9292 1.7V
        MultiSiteDouble vcodec_NoLoad=res1_DcVI.vmeas("Vm_VCODEC").getVoltage("VCODEC").getElement(0);//0x13 0x9292 1.7V

        MultiSiteDouble offset_Vcore=new MultiSiteDouble();
        MultiSiteDouble offset_Vana=new MultiSiteDouble();
        MultiSiteDouble offset_Vcodec=new MultiSiteDouble();
        offset_Vcore.set(StaticFields.Vcore_DCBuck_Cal.subtract(vcore_NoLoad));
        offset_Vana.set(StaticFields.Vana_DCBuck_Cal.subtract(vana_NoLoad));
        offset_Vcodec.set(StaticFields.Vcodec_DCBuck_Cal.subtract(vcodec_NoLoad));

        MultiSiteLong step_cnt_Vcore=new MultiSiteLong();
        MultiSiteLong step_cnt_Vana=new MultiSiteLong();
        MultiSiteLong step_cnt_Vcodec=new MultiSiteLong();
        //Vcore [15:8], 0x4343, 1bit 7.5mV, 13step 100mV, 0x5020->1.2V, 0x5D20->1.3V, 0x6320->1.35V,0x5520->1.237V, 0x5720->1.252V,
        //Vana  [15:8], 0xa0a0, 1bit 7.5mV, 13step 100mV, 0xa2f0->1.84V, 0x92f0->1.72V, 0x78f0->1.5V
        for(int site:activeSites) {
            step_cnt_Vcore.set(site,Math.round((StaticFields.Vcore_DCBuck_Cal.get(site)-vcore_NoLoad.get(site))/0.0038));//3.3mV
            step_cnt_Vana.set(site,Math.round((StaticFields.Vana_DCBuck_Cal.get(site)-vana_NoLoad.get(site))/0.0075));
            step_cnt_Vcodec.set(site,Math.round((StaticFields.Vcodec_DCBuck_Cal.get(site)-vcodec_NoLoad.get(site))/0.0075));

            //Effuse Addr7 Bit[4:0]:Vcore cal shift step, range 0~31
            step_cnt_Vcore.set(site,step_cnt_Vcore.get(site)>31?31:step_cnt_Vcore.get(site));
            //Effuse Addr7 Bit[9:6]:Vana cal shift step, range 0~15
            step_cnt_Vana.set(site,step_cnt_Vana.get(site)>31?31:step_cnt_Vana.get(site));
            step_cnt_Vcodec.set(site,step_cnt_Vcodec.get(site)>31?31:step_cnt_Vcodec.get(site));
        }

        MultiSiteLong cal_Vcore=new MultiSiteLong();
        MultiSiteLong cal_Vana=new MultiSiteLong();
        MultiSiteLong cal_Vcodec=new MultiSiteLong();
        cal_Vcore.set(StaticFields.regValue_DCBuck_Vcore.add(step_cnt_Vcore).add(step_cnt_Vcore.leftShift(8)));
        cal_Vana.set(StaticFields.regValue_DCBuck_Vana.add(step_cnt_Vana).add(step_cnt_Vana.leftShift(8)));
        cal_Vcodec.set(StaticFields.regValue_DCBuck_Vcodec.add(step_cnt_Vcodec).add(step_cnt_Vcodec.leftShift(8)));

        String testSuiteName_Qualified=context.getTestSuiteName();
        String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
        if(StaticFields.debugMode)
        {
            println("**********"+testSuiteName+"**********");

            println("target vol_Vcore: "+StaticFields.Vcore_DCBuck_Cal);
            println("target vol_Vana: "+StaticFields.Vana_DCBuck_Cal);
            println("target vol_Vcodec: "+StaticFields.Vcodec_DCBuck_Cal);
            for(int site:activeSites) {
                println("site= "+site+" Read Before_Vcore: 0x"+Long.toHexString(Vcore.get(site)));
                println("site= "+site+" Read Before_Vana: 0x"+Long.toHexString(Vana.get(site)));
                println("site= "+site+" Read Before_Vcodec: 0x"+Long.toHexString(Vcodec.get(site)));
            }
            println("Before VCORE_noload: "+vcore_NoLoad);
            println("Before vana_NoLoad: "+vana_NoLoad);
            println("Before vcodec_NoLoad: "+vcodec_NoLoad);
            println("step_cnt_Vcore: "+step_cnt_Vcore);
            println("step_cnt_Vana: "+step_cnt_Vana);
            println("step_cnt_VCODEC: "+step_cnt_Vcodec);
        }



        /***********  re-run *************/
        ProtocolAccess.setDynamicData("dynamicWrite_0x4a", cal_Vcore);
        ProtocolAccess.setDynamicData("dynamicWrite_0x13", cal_Vana);
        ProtocolAccess.setDynamicData("dynamicWrite_0xc3", cal_Vcodec);
        ProtocolAccess.updateDynamicData(context, measurement);
        measurement.execute();

        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults paRes2=ProtocolAccess.preserveResults(measurement);
        MultiSiteLong after_Vcore=paRes2.getResult( "readValue_0x4a");
        MultiSiteLong after_Vana=paRes2.getResult(  "readValue_0x13");
        MultiSiteLong after_Vcodec=paRes2.getResult("readValue_0xc3");


        IDcVIResults res2_DcVI=measurement.dcVI().preserveResults();
        MultiSiteDouble vcore_NoLoad2=res2_DcVI.vmeas("Vm_VCORE").getVoltage("VCORE_0p8").getElement(0);
        MultiSiteDouble vana_NoLoad2=res2_DcVI.vmeas("Vm_VANA").getVoltage("VANA").getElement(0);
        MultiSiteDouble vcodec_NoLoad2=res2_DcVI.vmeas("Vm_VCODEC").getVoltage("VCODEC").getElement(0);



        if(StaticFields.debugMode)
        {
            for(int site:activeSites) {
                println("site= "+site+" Write_Vcore: 0x"+Long.toHexString(cal_Vcore.get(site)));
                println("site= "+site+" Write_Vana: 0x"+Long.toHexString(cal_Vana.get(site)));
                println("site= "+site+" Write_Vcodec: 0x"+Long.toHexString(cal_Vcodec.get(site)));
                println("site= "+site+" Read After_Vcore: 0x"+Long.toHexString(after_Vcore.get(site)));
                println("site= "+site+" Read After_Vana: 0x"+Long.toHexString(after_Vana.get(site)));
                println("site= "+site+" Read After_Vcodec: 0x"+Long.toHexString(after_Vcodec.get(site)));
            }
            println("vcore_NoLoad2: "+vcore_NoLoad2);
            println("vana_NoLoad2: "+vana_NoLoad2);
            println("vcodec_NoLoad2: "+vcodec_NoLoad2);
            println("After offset_Vcore: "+offset_Vcore);
            println("After offset_Vana: "+offset_Vana);
            println("After offset_Vcodec: "+offset_Vcodec);
        }

        StaticFields.cal_Shift_Vcore.set(step_cnt_Vcore);
        StaticFields.cal_Shift_Vana.set(step_cnt_Vana);
        StaticFields.cal_Shift_Vcodec.set(step_cnt_Vcodec);

        ptd_Vcore_Before.evaluate(vcore_NoLoad);
        ptd_Vana_Before.evaluate(vana_NoLoad);
        ptd_Vcodec_Before.evaluate(vcodec_NoLoad);

        ptd_Vcore_After.evaluate(vcore_NoLoad2);
        ptd_Vana_After.evaluate(vana_NoLoad2);
        ptd_Vcodec_After.evaluate(vcodec_NoLoad2);

        ptd_Vcore_Offset.evaluate(offset_Vcore);
        ptd_Vana_Offset.evaluate(offset_Vana);
        ptd_Vcodec_Offset.evaluate(offset_Vcodec);

        ptd_Vcore_CalShift.evaluate(StaticFields.cal_Shift_Vcore);
        ptd_Vana_CalShift.evaluate(StaticFields.cal_Shift_Vana);
        ptd_Vcodec_CalShift.evaluate(StaticFields.cal_Shift_Vcodec);


    }

}
