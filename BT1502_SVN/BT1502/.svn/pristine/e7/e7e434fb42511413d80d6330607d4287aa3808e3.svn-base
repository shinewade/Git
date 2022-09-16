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
import xoc.dsa.ISetupDcVI;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.ILocalMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class DCDC_Buck extends TestMethod {

    @In public double VBat=4;
    @In public String spec_measurement;

    public ILocalMeasurement measurement_NoLoad;
    public ILocalMeasurement measurement_Load;

    public IParametricTestDescriptor    ptd_Efficency_Vcore,
                                        ptd_Efficency_Vcore0p6,
                                        ptd_Efficency_Vana,
                                        ptd_Efficency_Vcodec;

    public IParametricTestDescriptor    ptd_Buck_Vana_NoLoad,
                                        ptd_Buck_Vcore_NoLoad,
                                        ptd_Buck_Vcore0p6_NoLoad,
                                        ptd_Buck_Vcodec_NoLoad;

    public IParametricTestDescriptor    ptd_Buck_Vana_dp,
                                        ptd_Buck_Vcore_dp,
                                        ptd_Buck_Vcore0p6_dp,
                                        ptd_Buck_Vcodec_dp;

    @Override
    public void setup() {
        /************* measurement_NoLoad *****************/
        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        /**************************** disconnect & Hiz Mode ************************************************/
        BesDsa_PS1600_Disconnect relay_PS1600_1=new BesDsa_PS1600_Disconnect(ds1);
        String activePin_PS1600_1=relay_PS1600_1.getPinGroup(context, spec_measurement, "All_Digital");
        activePin_PS1600_1=activePin_PS1600_1.replaceAll("I2C_SCL\\+", "").replaceAll("I2C_SDA\\+", "").replaceAll("POWKEY\\+", "").replaceAll("RESETN\\+", "");
        relay_PS1600_1.disconnectAll_DigInOut(activePin_PS1600_1, false); //disconnect digital pins to Hiz mode
        /***************************************************************************************************/

        //VBAT
        ISetupDcVI dcVi_VBAT1=ds1.addDcVI("VSYS").setDisconnect(false);
        dcVi_VBAT1.imeas("Im_VBAT_NOLOAD").setIrange("200 mA").setWaitTime("5 ms").setAverages(32).setRestoreIrange(true);

        //VCORE_0p8, VANA, VCODEC
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VCORE_0p6", true, "Vm_VCORE0P6", V93K_Resource.DPS128);
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VCORE_0p8", true, "Vm_VCORE", V93K_Resource.DPS128);
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VANA", true, "Vm_VANA", V93K_Resource.DPS128);
        new BesDsa_PMIC(ds1).vMeas_DcVI_Hiz("VCODEC", true, "Vm_VCODEC", V93K_Resource.DPS128);
//        new BesDsa_PMIC(ds1).vMeas_DPS128_DcVI_Std("VCODEC", "-5 mA", "1.9 V", "10 mA", "6 V", "0 V", true, "iforce_1mA_VCODEC", "Vm_VCODEC");

        //operating sequence
        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement_NoLoad, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        i2c.transactionSequenceBegin("DCDC_Buck");
//            [LDO to DCDC-6.13]
//            i2c.write(BesI2cAddrType.PMUIntern,0x4a,0x9250);//buck1_bit
//            i2c.write(BesI2cAddrType.PMUIntern,0x13,0x5c34);//buck2_bit
//            i2c.write(BesI2cAddrType.PMUIntern,0xc3,0x9191);//buck3_bit
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

            i2c.waitTime(10e6);
            i2c.write(BesI2cAddrType.PMUIntern,0x0e,0x54c6);//ldo codec off
            i2c.write(BesI2cAddrType.PMUIntern,0x07,0x2a11);//ldo ana off
            i2c.write(BesI2cAddrType.PMUIntern,0x08,0x2a06);//ldo core off
            i2c.write(BesI2cAddrType.PMUIntern,0x68,0xb8c6);//ldo mem off 0xb4c6

//            i2c.write(BesI2cAddrType.PMUIntern,0x52,0x0011);
//            i2c.write(BesI2cAddrType.PMUIntern,0x54,0x2fc4);
//            i2c.write(BesI2cAddrType.PMUIntern,0x55,0x0da0);

            i2c.write(BesI2cAddrType.PMUIntern,0x4a,0x7777);//vcore_0p8     0x7777 0.75
            i2c.write(BesI2cAddrType.PMUIntern,0x13,0x5c5c);//vana      0x5c5c 1.30
            i2c.write(BesI2cAddrType.PMUIntern,0xc3,0x9191);//vcodec    0x9191 1.70

//            i2c.write(BesI2cAddrType.PMUIntern,0x69,0xbd8c);

            i2c.read(BesI2cAddrType.PMUIntern,0x4a,"readValue_0x4a");
            i2c.read(BesI2cAddrType.PMUIntern,0x13,"readValue_0x13");
            i2c.read(BesI2cAddrType.PMUIntern,0xc3,"readValue_0xc3");
        i2c.transactionSequenceEnd();

        ds1.waitCall("10 ms");
        ds1.actionCall("Im_VBAT_NOLOAD");
        ds1.actionCall("Vm_VCORE");
        ds1.actionCall("Vm_VANA");
//        ds1.actionCall("iforce_1mA_VCODEC");
        ds1.actionCall("Vm_VCODEC");
        ds1.actionCall("Vm_VCORE0P6"); //add wuhan


        measurement_NoLoad.setSetups(ds1);



        /************* measurement_Load *****************/
        IDeviceSetup ds2= DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds2.importSpec(spec_measurement);

        /**************************** disconnect & Hiz Mode ************************************************/
        BesDsa_PS1600_Disconnect relay_PS1600_2=new BesDsa_PS1600_Disconnect(ds2);
        String activePin_PS1600_2=relay_PS1600_2.getPinGroup(context, spec_measurement, "All_Digital");
        activePin_PS1600_2=activePin_PS1600_2.replaceAll("I2C_SCL\\+", "").replaceAll("I2C_SDA\\+", "").replaceAll("POWKEY\\+", "").replaceAll("RESETN\\+", "");
        relay_PS1600_2.disconnectAll_DigInOut(activePin_PS1600_2, false); //disconnect digital pins to Hiz mode
        /***************************************************************************************************/

        //VBAT
        ISetupDcVI dcVi_VBAT2=ds2.addDcVI("VSYS").setDisconnect(false);
        dcVi_VBAT2.imeas("Im_VBAT_LOAD").setIrange("100 mA").setWaitTime("5 ms").setAverages(32).setRestoreIrange(true);

        //VCORE_0p8, VANA, VCODEC
        new BesDsa_PMIC(ds2).vMeas_DPS128_DcVI_Std("VCORE_0p8", "-50 mA", "0.9 V", "100 mA", "6 V", "0 V", true, "iforce_20mA_VCORE_0p8", "vmeas_20mA_VCORE_0p8");
        new BesDsa_PMIC(ds2).vMeas_DPS128_DcVI_Std("VANA", "-50 mA", "1.3 V", "100 mA", "6 V", "0 V", true, "iforce_20mA_VANA", "vmeas_20mA_VANA");
        new BesDsa_PMIC(ds2).vMeas_DPS128_DcVI_Std("VCODEC", "-50 mA", "1.9 V", "100 mA", "6 V", "0 V", true, "iforce_20mA_VCODEC", "vmeas_20mA_VCODEC");
        new BesDsa_PMIC(ds2).vMeas_DPS128_DcVI_Std("VCORE_0p6", "-50 mA", "0.9 V", "100 mA", "6 V", "0 V", true, "iforce_20mA_VCORE_0p6", "vmeas_20mA_VCORE_0p6");

        ds2.sequentialBegin();

            ds2.actionCall("disconnect_VANA");
            ds2.actionCall("disconnect_VCODEC");
            ds2.actionCall("disconnect_VCORE_0p6");

            ds2.actionCall("iforce_20mA_VCORE_0p8");
            ds2.actionCall("Im_VBAT_LOAD");
            ds2.actionCall("vmeas_20mA_VCORE_0p8");
            ds2.actionCall("disconnect_VCORE_0p8");
            ds2.waitCall("2 ms");

            ds2.actionCall("iforce_20mA_VANA");
            ds2.actionCall("Im_VBAT_LOAD");
            ds2.actionCall("vmeas_20mA_VANA");
            ds2.actionCall("disconnect_VANA");
            ds2.waitCall("2 ms");

            ds2.actionCall("iforce_20mA_VCODEC");
            ds2.actionCall("Im_VBAT_LOAD");
            ds2.actionCall("vmeas_20mA_VCODEC");
            ds2.actionCall("disconnect_VCODEC");
            ds2.waitCall("2 ms");

            ds2.actionCall("iforce_20mA_VCORE_0p6");
            ds2.actionCall("Im_VBAT_LOAD");
            ds2.actionCall("vmeas_20mA_VCORE_0p6");
            ds2.actionCall("disconnect_VCORE_0p6");
            ds2.waitCall("2 ms");

        ds2.sequentialEnd();

        measurement_Load.setSetups(ds2);



}
    @Override
    public void update ()
    {
        measurement_NoLoad.spec().setVariable("Vbat", VBat);
        measurement_Load.spec().setVariable("Vbat", VBat);
    }

    @Override
    public void execute() {
        measurement_NoLoad.execute();
        IDcVIResults res_NoLoad_DcVI = measurement_NoLoad.dcVI().preserveResults();

        MultiSiteDouble I_Vbat_NoLoad = res_NoLoad_DcVI.imeas("Im_VBAT_NOLOAD").getCurrent("VSYS").getElement(0);
        MultiSiteDouble vcore_NoLoad = res_NoLoad_DcVI.vmeas("Vm_VCORE").getVoltage("VCORE_0p8").getElement(0);
        MultiSiteDouble vana_NoLoad = res_NoLoad_DcVI.vmeas("Vm_VANA").getVoltage("VANA").getElement(0);
        MultiSiteDouble vcodec_NoLoad = res_NoLoad_DcVI.vmeas("Vm_VCODEC").getVoltage("VCODEC").getElement(0);
        MultiSiteDouble vcore0P6_NoLoad = res_NoLoad_DcVI.vmeas("Vm_VCORE0P6").getVoltage("VCORE_0p6").getElement(0);

        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults paRes1=ProtocolAccess.preserveResults(measurement_NoLoad);
        MultiSiteLong Vcore=paRes1.getResult( "readValue_0x4a");
        MultiSiteLong Vana=paRes1.getResult(  "readValue_0x13");
        MultiSiteLong Vcodec=paRes1.getResult("readValue_0xc3");
//        for(int site:context.getActiveSites()) {
//            System.out.println("456456");
//            println("site= "+site+" Read Before_Vcore: 0x"+Long.toHexString(Vcore.get(site)));
//            println("site= "+site+" Read Before_Vana: 0x"+Long.toHexString(Vana.get(site)));
//            println("site= "+site+" Read Before_Vcodec: 0x"+Long.toHexString(Vcodec.get(site)));
//        }

        measurement_Load .execute();
        IDcVIResults res_Load_DcVI = measurement_Load.dcVI().preserveResults();

        MultiSiteDouble I_Vbat_Vcore_Load = res_Load_DcVI.imeas("Im_VBAT_LOAD").getCurrent("VSYS").getElement(0);
        MultiSiteDouble vcore_Load= res_Load_DcVI.vmeas("vmeas_20mA_VCORE_0p8").getVoltage("VCORE_0p8").getElement(0);
        MultiSiteDouble vcore_dp =vcore_NoLoad.subtract(vcore_Load);

        MultiSiteDouble I_Vbat_Vana_Load = res_Load_DcVI.imeas("Im_VBAT_LOAD").getCurrent("VSYS").getElement(1);
        MultiSiteDouble vana_Load= res_Load_DcVI.vmeas("vmeas_20mA_VANA").getVoltage("VANA").getElement(0);
        MultiSiteDouble vana_dp=vana_NoLoad.subtract(vana_Load);

        MultiSiteDouble I_Vbat_Vcodec_Load = res_Load_DcVI.imeas("Im_VBAT_LOAD").getCurrent("VSYS").getElement(2);
        MultiSiteDouble vcodec_Load= res_Load_DcVI.vmeas("vmeas_20mA_VCODEC").getVoltage("VCODEC").getElement(0);
        MultiSiteDouble vcodec_dp =vcodec_NoLoad.subtract(vcodec_Load);

        MultiSiteDouble I_Vbat_Vcore0P6_Load = res_Load_DcVI.imeas("Im_VBAT_LOAD").getCurrent("VSYS").getElement(3);
        MultiSiteDouble vcore0P6_Load= res_Load_DcVI.vmeas("vmeas_20mA_VCORE_0p6").getVoltage("VCORE_0p6").getElement(0);
        MultiSiteDouble vcore0P6_dp =vcore0P6_NoLoad.subtract(vcore0P6_Load);

        double Vbat_Spec = 0.0;
        Vbat_Spec =  measurement_Load.spec().getDouble("Vbat").get(context.getActiveSites()[0]);

        MultiSiteDouble buckEfficency_Vcore = new MultiSiteDouble("999.99");
        MultiSiteDouble I_Vcore_Delta = I_Vbat_Vcore_Load.subtract(I_Vbat_NoLoad);
        buckEfficency_Vcore = vcore_Load.multiply(measurement_Load.dcVI("VCORE_0p8").iforce("iforce_20mA_VCORE_0p8").getForceValue(context.getActiveSites()[0])).multiply(-1e3).divide(I_Vcore_Delta.multiply(1e3).multiply(Vbat_Spec)).multiply(1e2);

        MultiSiteDouble buckEfficency_Vana = new MultiSiteDouble("999.99");
        MultiSiteDouble I_Vana_Delta = I_Vbat_Vana_Load.subtract(I_Vbat_NoLoad);
        buckEfficency_Vana = vana_Load.multiply(measurement_Load.dcVI("VANA").iforce("iforce_20mA_VANA").getForceValue(context.getActiveSites()[0])).multiply(-1e3).divide(I_Vana_Delta.multiply(1e3).multiply(Vbat_Spec)).multiply(1e2);

        MultiSiteDouble buckEfficency_Vcodec = new MultiSiteDouble("999.99");
        MultiSiteDouble I_Vcodec_Delta = I_Vbat_Vcodec_Load.subtract(I_Vbat_NoLoad);
        buckEfficency_Vcodec = vcodec_Load.multiply(measurement_Load.dcVI("VCODEC").iforce("iforce_20mA_VCODEC").getForceValue(context.getActiveSites()[0])).multiply(-1e3).divide(I_Vcodec_Delta.multiply(1e3).multiply(Vbat_Spec)).multiply(1e2);

        MultiSiteDouble buckEfficency_Vcore0P6 = new MultiSiteDouble("999.99");
        MultiSiteDouble I_Vcore0P6_Delta = I_Vbat_Vcore0P6_Load.subtract(I_Vbat_NoLoad);
        buckEfficency_Vcore0P6 = vcore0P6_Load.multiply(measurement_Load.dcVI("VCORE_0p6").iforce("iforce_20mA_VCORE_0p6").getForceValue(context.getActiveSites()[0])).multiply(-1e3).divide(I_Vcore0P6_Delta.multiply(1e3).multiply(Vbat_Spec)).multiply(1e2);


        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println();
            println("**********"+testSuiteName+"**********");

            println("DC_Bk_I_Vbat_NoLoad    [mA]  = "+I_Vbat_NoLoad.multiply(1e3));
            println();
            println("I_Vbat_Vcore0P6_Load      [mA]  = "+I_Vbat_Vcore0P6_Load.multiply(1e3));
            println("DC_Bk_VCORE0P6_nold       [V ]  = "+vcore0P6_NoLoad);
            println("DC_Bk_VCORE0P6_ld         [V ]  = "+vcore0P6_Load);
            println("DC_Bk_VCORE0P6_dp         [mV]  = "+vcore0P6_dp.multiply(1e3));
            println();
            println("I_Vbat_Vcore_Load      [mA]  = "+I_Vbat_Vcore_Load.multiply(1e3));
            println("DC_Bk_VCORE_nold       [V ]  = "+vcore_NoLoad);
            println("DC_Bk_VCORE_ld         [V ]  = "+vcore_Load);
            println("DC_Bk_VCORE_dp         [mV]  = "+vcore_dp.multiply(1e3));
            println();
            println("I_Vbat_Vana_Load       [mA]  = "+I_Vbat_Vana_Load.multiply(1e3));
            println("DC_Bk_VANA_nold        [V ]  = "+vana_NoLoad);
            println("DC_Bk_VANA_ld          [V ]  = "+vana_Load);
            println("DC_Bk_VANA_dp          [mV]  = "+vana_dp.multiply(1e3));
            println();
            println("I_Vbat_Vcodec_Load     [mA]  = "+I_Vbat_Vcodec_Load.multiply(1e3));
            println("DC_Bk_VCODEC_nold      [V ]  = "+vcodec_NoLoad);
            println("DC_Bk_VCODEC_ld        [V ]  = "+vcodec_Load);
            println("DC_Bk_VCODEC_dp        [mV]  = "+vcodec_dp.multiply(1e3));
            println();

            println("buckEfficency_Vcore    [% ]  = "+buckEfficency_Vcore);
            println("buckEfficency_Vana     [% ]  = "+buckEfficency_Vana);
            println("buckEfficency_Vcodec   [% ]  = "+buckEfficency_Vcodec);
            println("buckEfficency_Vcore0P6   [% ]  = "+buckEfficency_Vcore0P6);
            println();

        }

        ptd_Buck_Vcore_NoLoad.evaluate(vcore_NoLoad);
   //     ptd_Buck_Vcore0p6_NoLoad.evaluate(vcore0P6_NoLoad);
        ptd_Buck_Vana_NoLoad.evaluate(vana_NoLoad);
        ptd_Buck_Vcodec_NoLoad.evaluate(vcodec_NoLoad);

        ptd_Buck_Vcore_dp.evaluate(vcore_dp);
 //       ptd_Buck_Vcore0p6_dp.evaluate(vcore0P6_dp);
        ptd_Buck_Vana_dp.evaluate(vana_dp);
        ptd_Buck_Vcodec_dp.evaluate(vcodec_dp);

        ptd_Efficency_Vcore.evaluate(buckEfficency_Vcore);
  //      ptd_Efficency_Vcore0p6.evaluate(buckEfficency_Vcore0P6);
        ptd_Efficency_Vana.evaluate(buckEfficency_Vana);
        ptd_Efficency_Vcodec.evaluate(buckEfficency_Vcodec);




    }
}
