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

public class DCDC_CAL_DFT extends TestMethod {

    @In public String spec_measurement;
    public IMeasurement measurement;

    public IParametricTestDescriptor    ptd_Vcore_Before;


    public IParametricTestDescriptor    ptd_Vcore_After;


    public IParametricTestDescriptor    ptd_Vcore_Offset;


    public IParametricTestDescriptor    ptd_Vcore_CalShift;



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
//            ds1.waitCall("5 ms");
//            ds1.actionCall("Vm_VANA");
//            ds1.waitCall("5 ms");
//            ds1.actionCall("Vm_VCODEC");
        ds1.sequentialEnd();

        measurement.setSetups(ds1);
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        int[] activeSites = context.getActiveSites();

        ProtocolAccess.setDynamicData("dynamicWrite_0x4a", StaticFields.regValue_DCBuck_Vcore_DFT);

        ProtocolAccess.updateDynamicData(context, measurement);
        measurement.execute();

        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults paRes1=ProtocolAccess.preserveResults(measurement);
        MultiSiteLong Vcore=paRes1.getResult( "readValue_0x4a");


        IDcVIResults res1_DcVI=measurement.dcVI().preserveResults();
        MultiSiteDouble vcore_NoLoad=res1_DcVI.vmeas("Vm_VCORE").getVoltage("VCORE_0p8").getElement(0);//0x4a 0x5050 1.2V


        MultiSiteDouble offset_Vcore=new MultiSiteDouble();

        offset_Vcore.set(StaticFields.Vcore_DFT_DCBuck_Cal.subtract(vcore_NoLoad));


        MultiSiteLong step_cnt_Vcore=new MultiSiteLong();

        //Vcore [15:8], 0x4343, 1bit 7.5mV, 13step 100mV, 0x5020->1.2V, 0x5D20->1.3V, 0x6320->1.35V,0x5520->1.237V, 0x5720->1.252V,
        //Vana  [15:8], 0xa0a0, 1bit 7.5mV, 13step 100mV, 0xa2f0->1.84V, 0x92f0->1.72V, 0x78f0->1.5V
        for(int site:activeSites) {
            step_cnt_Vcore.set(site,Math.round((StaticFields.Vcore_DFT_DCBuck_Cal.get(site)-vcore_NoLoad.get(site))/0.0038));//3.3mV



        }

        MultiSiteLong cal_Vcore=new MultiSiteLong();

        cal_Vcore.set(StaticFields.regValue_DCBuck_Vcore_DFT.add(step_cnt_Vcore).add(step_cnt_Vcore.leftShift(8)));


        String testSuiteName_Qualified=context.getTestSuiteName();
        String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
        if(StaticFields.debugMode)
        {
            println("**********"+testSuiteName+"**********");

            println("target vol_Vcore: "+StaticFields.Vcore_DFT_DCBuck_Cal);

            for(int site:activeSites) {
                println("site= "+site+" Read Before_Vcore: 0x"+Long.toHexString(Vcore.get(site)));

            }
            println("Before VCORE_noload: "+vcore_NoLoad);

            println("step_cnt_Vcore: "+step_cnt_Vcore);

        }



        /***********  re-run *************/
        ProtocolAccess.setDynamicData("dynamicWrite_0x4a", cal_Vcore);

        ProtocolAccess.updateDynamicData(context, measurement);
        measurement.execute();

        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults paRes2=ProtocolAccess.preserveResults(measurement);
        MultiSiteLong after_Vcore=paRes2.getResult( "readValue_0x4a");



        IDcVIResults res2_DcVI=measurement.dcVI().preserveResults();
        MultiSiteDouble vcore_NoLoad2=res2_DcVI.vmeas("Vm_VCORE").getVoltage("VCORE_0p8").getElement(0);




        if(StaticFields.debugMode)
        {
            for(int site:activeSites) {
                println("site= "+site+" Write_Vcore: 0x"+Long.toHexString(cal_Vcore.get(site)));

                println("site= "+site+" Read After_Vcore: 0x"+Long.toHexString(after_Vcore.get(site)));

            }
            println("vcore_NoLoad2: "+vcore_NoLoad2);

            println("After offset_Vcore: "+offset_Vcore);

        }

        StaticFields.cal_Shift_Vcore_DFT.set(step_cnt_Vcore);

//        ptd_Vcore_Before.evaluate(vcore_NoLoad);
//
//
//        ptd_Vcore_After.evaluate(vcore_NoLoad2);
//
//
//        ptd_Vcore_Offset.evaluate(offset_Vcore);
//
//
//        ptd_Vcore_CalShift.evaluate(StaticFields.cal_Shift_Vcore);



    }

}