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


public class DCDC_PowerOn_2700 extends TestMethod {

    @In public String spec_measurement;
    public IMeasurement measurement;
    private boolean dcdc_meas=false;//true: measure DCDC output

    @Override
    public void setup() {

        IDeviceSetup ds1=DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        /**************************** disconnect & Hiz Mode ************************************************/
        BesDsa_PS1600_Disconnect relay_PS1600_1=new BesDsa_PS1600_Disconnect(ds1);
        String activePin_PS1600_1=relay_PS1600_1.getPinGroup(context, spec_measurement, "All_Digital");
        activePin_PS1600_1=activePin_PS1600_1.replaceAll("I2C_SCL\\+", "").replaceAll("I2C_SDA\\+", "").replaceAll("POWKEY\\+", "").replaceAll("RESETN\\+", "");
        relay_PS1600_1.disconnectAll_DigInOut(activePin_PS1600_1, false); //disconnect digital pins to Hiz mode
        /***************************************************************************************************/


        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        //operating sequence
        i2c.transactionSequenceBegin("DCDC_PowerOn");

            i2c.waitTime(5e6);

            i2c.write(BesI2cAddrType.PMUIntern,0x4a,0x9250);//buck1_bit
            i2c.write(BesI2cAddrType.PMUIntern,0x13,0x5c34);//buck2_bit
            i2c.write(BesI2cAddrType.PMUIntern,0xc3,0x9191);//buck3_bit
            i2c.write(BesI2cAddrType.PMUIntern,0x15,0xe300); //buck2 freq 11
            i2c.write(BesI2cAddrType.PMUIntern,0x64,0x0c08);//buck1 freq 11
            i2c.write(BesI2cAddrType.PMUIntern,0xbf,0x8cf8);//buck3 freq 11

            i2c.write(BesI2cAddrType.PMUIntern,0xc1,0xce20);//buck3 cap_bit
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

//            i2c.write(BesI2cAddrType.PMUIntern,0x4a,0x7777);//vcore     0x7777
//            i2c.write(BesI2cAddrType.PMUIntern,0x13,0x5c5c);//vana      0x5c5c
//            i2c.write(BesI2cAddrType.PMUIntern,0xc3,0x8484);//vcodec    0x9191
            i2c.write(BesI2cAddrType.PMUIntern,0x4a,"dynamicWrite_data_vcore_0p8");
            i2c.write(BesI2cAddrType.PMUIntern,0x13,"dynamicWrite_data_vana");
            i2c.write(BesI2cAddrType.PMUIntern,0xc3,"dynamicWrite_data_vcodec");

            i2c.waitTime(10e6);
            i2c.write(BesI2cAddrType.PMUIntern,0x0e,0x54c6);//ldo codec off
            i2c.write(BesI2cAddrType.PMUIntern,0x07,0x2a11);//ldo ana off
            i2c.write(BesI2cAddrType.PMUIntern,0x08,0x2a06);//ldo core off
            i2c.write(BesI2cAddrType.PMUIntern,0x68,0xb4c6);//ldo mem off 0xb4c6

            i2c.read(BesI2cAddrType.PMUIntern,0x4a,"readValue_0x4a");
            i2c.read(BesI2cAddrType.PMUIntern,0x13,"readValue_0x13");
            i2c.read(BesI2cAddrType.PMUIntern,0xc3,"readValue_0xc3");




        i2c.transactionSequenceEnd();

        if(dcdc_meas) {

            new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VCORE_0p8", true, "Vm_VCORE", V93K_Resource.DPS128);
            new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VANA", true, "Vm_VANA", V93K_Resource.DPS128);
            new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VCODEC", true, "Vm_VCODEC", V93K_Resource.DPS128);

            ds1.waitCall("10 ms");
            ds1.actionCall("Vm_VCORE");
            ds1.waitCall("10 ms");
            ds1.actionCall("Vm_VANA");
            ds1.waitCall("10 ms");
            ds1.actionCall("Vm_VCODEC");
        }

        measurement.setSetups(ds1);
    }

    @Override
    public void execute() {
        int[] activeSites = context.getActiveSites();

        //offset compensation
        MultiSiteLong calData_Vcore=new MultiSiteLong();
        MultiSiteLong calData_Vana=new MultiSiteLong();
        MultiSiteLong calData_Vcodec=new MultiSiteLong();
        calData_Vcore.set( StaticFields.cal_Shift_Vcore.add(StaticFields.cal_Shift_Vcore.leftShift(8)).add(StaticFields.regValue_DCBuck_Vcore)  );
        calData_Vana.set( StaticFields.cal_Shift_Vana.add(StaticFields.cal_Shift_Vana.leftShift(8)).add(StaticFields.regValue_DCBuck_Vana)  );
        calData_Vcodec.set( StaticFields.cal_Shift_Vcodec.add(StaticFields.cal_Shift_Vcodec.leftShift(8)).add(StaticFields.regValue_DCBuck_Vcodec)  );

        ProtocolAccess.setDynamicData("dynamicWrite_data_vcore_0p8", calData_Vcore);
        ProtocolAccess.setDynamicData("dynamicWrite_data_vana"     , calData_Vana);
        ProtocolAccess.setDynamicData("dynamicWrite_data_vcodec"   , calData_Vcodec);
        ProtocolAccess.updateDynamicData(context, measurement);
        measurement.execute();

        if(dcdc_meas) {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");

            IDcVIResults Result1 = measurement.dcVI().preserveResults();
            MultiSiteDouble VCORE_org_vol = Result1.vmeas("Vm_VCORE").getVoltage("VCORE_0p8").getElement(0);
            MultiSiteDouble VANA_org_vol = Result1.vmeas("Vm_VANA").getVoltage("VANA").getElement(0);
            MultiSiteDouble VCODEC_org_vol = Result1.vmeas("Vm_VCODEC").getVoltage("VCODEC").getElement(0);

            println("StaticFields.vcore_v  = "+StaticFields.vcore_v);
            println("StaticFields.vana_v   = "+StaticFields.vana_v);
            println("StaticFields.vcodec_v = "+StaticFields.vcodec_v);

            for(int site:activeSites) {
                println("site= "+site+" data_Vcore: 0x"+Long.toHexString(StaticFields.regValue_DCBuck_Vcore.get(site)));
                println("site= "+site+" data_Vana: 0x"+Long.toHexString(StaticFields.regValue_DCBuck_Vana.get(site)));
                println("site= "+site+" data_Vcodec: 0x"+Long.toHexString(StaticFields.regValue_DCBuck_Vcodec.get(site)));
                println("site= "+site+" Write_Vcore: 0x"+Long.toHexString(calData_Vcore.get(site)));
                println("site= "+site+" Write_Vana: 0x"+Long.toHexString(calData_Vana.get(site)));
                println("site= "+site+" Write_Vcodec: 0x"+Long.toHexString(calData_Vcodec.get(site)));
            }
            println("DC_Bk_VCORE_nold     [V]  = "+VCORE_org_vol);
            println("DC_Bk_VANA_nold      [V]  = "+VANA_org_vol);
            println("DC_Bk_VCODEC_nold    [V]  = "+VCODEC_org_vol);
            println();
        }

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println();
        }
    }
}
