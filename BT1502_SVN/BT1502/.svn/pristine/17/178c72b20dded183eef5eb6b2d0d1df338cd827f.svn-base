package BT1502_pro_tml.BT;

import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import BT1502_pro_tml.Global.StaticFields;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.ILocalMeasurement;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class BT_TX_PWR_Cal extends TestMethod {
    @In public String spec_measurement;

    public IMeasurement measurement1;
    public ILocalMeasurement measurement2;
    public IParametricTestDescriptor
                                        ptd_BTTX_PWR_L_BeforeCal,
                                        ptd_BTTX_PWR_M_BeforeCal,
                                        ptd_BTTX_PWR_H_BeforeCal,
                                        ptd_AvgShift_BTTX_PWR,
                                        ptd_efuse_DirectionBit_L,
                                        ptd_efuse_DirectionBit_M,
                                        ptd_efuse_DirectionBit_H,
                                        ptd_efuse_ABS_CalShift_L,
                                        ptd_efuse_ABS_CalShift_M,
                                        ptd_efuse_ABS_CalShift_H;
    public String trCallName1;

    @Override
    public void setup() {
        IDeviceSetup ds1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement1, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");
        i2c.transactionSequenceBegin("BT_TX_PWR_Cal_Read");
            i2c.read(BesI2cAddrType.RF, 0xcc,"Read_0xcc");
            i2c.read(BesI2cAddrType.RF, 0xcb,"Read_0xcb");
            i2c.read(BesI2cAddrType.DIGITAL, 0xd0350308,"Read_0xd0350308");
            i2c.read(BesI2cAddrType.DIGITAL, 0xd0350344,"Read_0xd0350344");
        i2c.transactionSequenceEnd();

        measurement1.setSetups(ds1);



        IDeviceSetup ds2 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds2.importSpec(spec_measurement);

        BesPA_I2C i2c_2=new BesPA_I2C(ds2, measurement2, I2cRegAddrBits.RegAddr_8Bits);
        i2c_2.setSignals("I2C_SCL", "I2C_SDA");
        i2c_2.transactionSequenceBegin("BT_TX_PWR_Cal_Dyn_Write");
            i2c_2.write(BesI2cAddrType.RF, 0xcb,"dynWrite_0xcb");
            i2c_2.write(BesI2cAddrType.RF, 0xcc,"dynWrite_0xcc");

            i2c_2.write(BesI2cAddrType.DIGITAL, 0xd0350308,"dynWrite_0xd0350308");
            i2c_2.write(BesI2cAddrType.DIGITAL, 0xd0350344,"dynWrite_0xd0350344");
            i2c_2.waitTime(2e6);
        i2c_2.transactionSequenceEnd();

        measurement2.setSetups(ds2);
    }


    @Override
    public void execute() {

        MultiSiteLong AvgShift_BTTX_PWR=new MultiSiteLong(0);
        AvgShift_BTTX_PWR=getAvgShift_BTTX_PWR();

        //get register initial value
        measurement1.execute();
        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults res1_PA=ProtocolAccess.preserveResults(measurement1);
        MultiSiteLong  Read_0xcb_Init= res1_PA.getResult("Read_0xcb");
        MultiSiteLong  Read_0xcc_Init= res1_PA.getResult("Read_0xcc");
        MultiSiteLong  Read_0xd0350308_Init= res1_PA.getResult("Read_0xd0350308");
        MultiSiteLong  Read_0xd0350344_Init= res1_PA.getResult("Read_0xd0350344");

//        print("Read_0xd0350308_Init = "+ Long.toHexString(Read_0xd0350308_Init.get(1)));
//        print("==================="+"\n");
//        print("Read_0xd0350344_Init = "+ Long.toHexString(Read_0xd0350344_Init.get(1)));

        MultiSiteLong calValue_0xcb=new MultiSiteLong(0);
        MultiSiteLong calValue_0xcc=new MultiSiteLong(0);
        MultiSiteLong calValue_0xd0350308=new MultiSiteLong(0);
        MultiSiteLong calValue_0xd0350344=new MultiSiteLong(0);

        for(int site:context.getActiveSites()) {
            //cal step: -1, 0, +1
            if(AvgShift_BTTX_PWR.get(site)>=-1 && AvgShift_BTTX_PWR.get(site)<=1) {
                calValue_0xcb.set(site,Read_0xcb_Init.get(site));
                calValue_0xcc.set(site,Read_0xcc_Init.get(site));
                calValue_0xd0350308.set(site,Read_0xd0350308_Init.get(site));
                calValue_0xd0350344.set(site,Read_0xd0350344_Init.get(site));
            }
            //cal step: +2
            else if(AvgShift_BTTX_PWR.get(site)==2 ) {
                calValue_0xcb.set(site,Read_0xcb_Init.get(site)&(0xffff^(0b11111)<<4)|(0b00000<<4));
                calValue_0xcc.set(site,Read_0xcc_Init.get(site)&(0xffff^(0b11111)<<4)|(0b10001<<4));
                calValue_0xd0350308.set(site,Read_0xd0350308_Init.get(site));
                calValue_0xd0350344.set(site,Read_0xd0350344_Init.get(site));
            }
            //cal step: +3, +4, +5, +6, +7
            else if(AvgShift_BTTX_PWR.get(site)>=3 ) {
                calValue_0xcb.set(site,Read_0xcb_Init.get(site)&(0xffff^(0b11111)<<4)|(0b00000<<4));
                calValue_0xcc.set(site,Read_0xcc_Init.get(site)&(0xffff^(0b11111)<<4)|(0b10001<<4));
                calValue_0xd0350308.set(site,Read_0xd0350308_Init.get(site)&(0xffffffff^((0b11111<<10) | 0b11111))|(0b01101)|(0b01101<<10));
                calValue_0xd0350344.set(site,Read_0xd0350344_Init.get(site)&(0xffffffff^0b11111)|(0b10010) );
            }
            //cal step: -2
            else if(AvgShift_BTTX_PWR.get(site)==-2 ) {
                calValue_0xcb.set(site,Read_0xcb_Init.get(site)&(0xffff^(0b11111)<<4)|(0b11111<<4));
                calValue_0xcc.set(site,Read_0xcc_Init.get(site));
//                calValue_0xd0350308.set(site,Read_0xd0350308_Init.get(site)&(0xffffffff^((0b11111<<10) | 0b11111))|(0b10100)|(0b10100<<10));
//                calValue_0xd0350344.set(site,Read_0xd0350344_Init.get(site)&(0xffffffff^0b11111)|(0b11100) );
                calValue_0xd0350308.set(site,Read_0xd0350308_Init.get(site)&(0xffffffff^((0b11111<<10) | 0b11111))|(0b10000)|(0b10000<<10));
                calValue_0xd0350344.set(site,Read_0xd0350344_Init.get(site)&(0xffffffff^0b11111)|(0b10111) );
            }
            //cal step: -3, -4, -5, -6, -7
            else if(AvgShift_BTTX_PWR.get(site)<=-3 ) {
                calValue_0xcb.set(site,Read_0xcb_Init.get(site)&(0xffff^(0b11111)<<4)|(0b11111<<4));
                calValue_0xcc.set(site,Read_0xcc_Init.get(site)&(0xffff^(0b11111)<<4)|(0b11111<<4));
//                calValue_0xd0350308.set(site,Read_0xd0350308_Init.get(site)&(0xffffffff^((0b11111<<10) | 0b11111))|(0b10101)|(0b10101<<10));
//                calValue_0xd0350344.set(site,Read_0xd0350344_Init.get(site)&(0xffffffff^0b11111)|(0b11101) );
                calValue_0xd0350308.set(site,Read_0xd0350308_Init.get(site)&(0xffffffff^((0b11111<<10) | 0b11111))|(0b01111)|(0b01111<<10));
                calValue_0xd0350344.set(site,Read_0xd0350344_Init.get(site)&(0xffffffff^0b11111)|(0b10101) );
            }
        }

        //dynamic write with updated calibration value
        ProtocolAccess.setDynamicData("dynWrite_0xcb", calValue_0xcb);
        ProtocolAccess.setDynamicData("dynWrite_0xcc", calValue_0xcc);
        ProtocolAccess.setDynamicData("dynWrite_0xd0350308", calValue_0xd0350308);
        ProtocolAccess.setDynamicData("dynWrite_0xd0350344", calValue_0xd0350344);
        ProtocolAccess.updateDynamicData(context, measurement2);
        measurement2.execute();




        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("BTTX_PWR_L_Before_Cal = "+StaticFields.BTTX_PWR_L);
            println("BTTX_PWR_M_Before_Cal = "+StaticFields.BTTX_PWR_M);
            println("BTTX_PWR_H_Before_Cal = "+StaticFields.BTTX_PWR_H);
            println();
            println("efuse_DirectionBit_L = "+StaticFields.efuse_DirectionBit_L);
            println("efuse_DirectionBit_M = "+StaticFields.efuse_DirectionBit_M);
            println("efuse_DirectionBit_H = "+StaticFields.efuse_DirectionBit_H);
            println();
            println("efuse_ABS_CalShift_L = "+StaticFields.efuse_ABS_CalShift_L);
            println("efuse_ABS_CalShift_M = "+StaticFields.efuse_ABS_CalShift_M);
            println("efuse_ABS_CalShift_H = "+StaticFields.efuse_ABS_CalShift_H);
            println();
            println("AvgShift_BTTX_PWR = "+AvgShift_BTTX_PWR);
            println();
            for(int site:context.getActiveSites()) {
                println("calValue_0xcb= 0x"+Long.toHexString(calValue_0xcb.get(site))+" [site "+site+"]");
                println("calValue_0xcc= 0x"+Long.toHexString(calValue_0xcc.get(site))+" [site "+site+"]");
                println("calValue_0xd0350308= 0x"+Long.toHexString(calValue_0xd0350308.get(site))+" [site "+site+"]");
                println("calValue_0xd0350344= 0x"+Long.toHexString(calValue_0xd0350344.get(site))+" [site "+site+"]");
                println();
            }
            println();
        }

        ptd_BTTX_PWR_L_BeforeCal.evaluate(StaticFields.BTTX_PWR_L);
        ptd_BTTX_PWR_M_BeforeCal.evaluate(StaticFields.BTTX_PWR_M);
        ptd_BTTX_PWR_H_BeforeCal.evaluate(StaticFields.BTTX_PWR_H);

        ptd_efuse_DirectionBit_L.evaluate(StaticFields.efuse_DirectionBit_L);
        ptd_efuse_DirectionBit_M.evaluate(StaticFields.efuse_DirectionBit_M);
        ptd_efuse_DirectionBit_H.evaluate(StaticFields.efuse_DirectionBit_H);

        ptd_efuse_ABS_CalShift_L.evaluate(StaticFields.efuse_ABS_CalShift_L);
        ptd_efuse_ABS_CalShift_M.evaluate(StaticFields.efuse_ABS_CalShift_M);
        ptd_efuse_ABS_CalShift_H.evaluate(StaticFields.efuse_ABS_CalShift_H);

        ptd_AvgShift_BTTX_PWR.evaluate(AvgShift_BTTX_PWR);    //-1 ~1  bin1
   }








    public MultiSiteLong getAvgShift_BTTX_PWR() {
//      StaticFields.BTTX_PWR_L.set(-1.2);   // -7 step
//      StaticFields.BTTX_PWR_M.set(3.2);   // -4 step
//      StaticFields.BTTX_PWR_H.set(10.55); // +3 step

        double targetPWR_L=8;    //dBm
        double targetPWR_M=8.5;  //dBm
        double targetPWR_H=8.5;    //dBm
        double calStep =1;       //dBm
        MultiSiteDouble calShift_L=new MultiSiteDouble(0);
        MultiSiteDouble calShift_M=new MultiSiteDouble(0);
        MultiSiteDouble calShift_H=new MultiSiteDouble(0);
        MultiSiteLong calShift_Round_L=new MultiSiteLong(0);
        MultiSiteLong calShift_Round_M=new MultiSiteLong(0);
        MultiSiteLong calShift_Round_H=new MultiSiteLong(0);
        MultiSiteLong newCalShift_Round_L=new MultiSiteLong(0);
        MultiSiteLong newCalShift_Round_M=new MultiSiteLong(0);
        MultiSiteLong newCalShift_Round_H=new MultiSiteLong(0);

        calShift_L.set(StaticFields.BTTX_PWR_L.subtract(targetPWR_L).divide(calStep));
        calShift_M.set(StaticFields.BTTX_PWR_M.subtract(targetPWR_M).divide(calStep));
        calShift_H.set(StaticFields.BTTX_PWR_H.subtract(targetPWR_H).divide(calStep));
        calShift_Round_L.set(calShift_L.round());
        calShift_Round_M.set(calShift_M.round());
        calShift_Round_H.set(calShift_H.round());

//        println("calShift_L= "+calShift_L);
//        println("calShift_M= "+calShift_M);
//        println("calShift_H= "+calShift_H);
//        println("calShift_Round_L= "+calShift_Round_L);
//        println("calShift_Round_M= "+calShift_Round_M);
//        println("calShift_Round_H= "+calShift_Round_H);


        //Initial global variable
        StaticFields.efuse_DirectionBit_L=new MultiSiteLong(0);      //bit[8 ], positive: bit 1, negative: bit 0, efuse7
        StaticFields.efuse_DirectionBit_M=new MultiSiteLong(0);      //bit[12], positive: bit 1, negative: bit 0, efuse7
        StaticFields.efuse_DirectionBit_H=new MultiSiteLong(0);      //bit[15], positive: bit 1, negative: bit 0, efuse5
        StaticFields.efuse_ABS_CalShift_L=new MultiSiteLong(0);     //bit[7 :5 ] efuse7
        StaticFields.efuse_ABS_CalShift_M=new MultiSiteLong(0);     //bit[11:9 ] efuse7
        StaticFields.efuse_ABS_CalShift_H=new MultiSiteLong(0);     //bit[15:13] efuse7

        //Set global variable
        StaticFields.efuse_ABS_CalShift_L.set(calShift_Round_L.abs());
        StaticFields.efuse_ABS_CalShift_M.set(calShift_Round_M.abs());
        StaticFields.efuse_ABS_CalShift_H.set(calShift_Round_H.abs());
        for(int site:context.getActiveSites()) {
            StaticFields.efuse_DirectionBit_L.set(site, calShift_Round_L.get(site)>=0 ? 1 : 0 );
            StaticFields.efuse_DirectionBit_M.set(site, calShift_Round_M.get(site)>=0 ? 1 : 0 );
            StaticFields.efuse_DirectionBit_H.set(site, calShift_Round_H.get(site)>=0 ? 1 : 0 );

            StaticFields.efuse_ABS_CalShift_L.set(site, StaticFields.efuse_ABS_CalShift_L.get(site)>=7 ? 7 : StaticFields.efuse_ABS_CalShift_L.get(site));
            StaticFields.efuse_ABS_CalShift_M.set(site, StaticFields.efuse_ABS_CalShift_M.get(site)>=7 ? 7 : StaticFields.efuse_ABS_CalShift_M.get(site));
            StaticFields.efuse_ABS_CalShift_H.set(site, StaticFields.efuse_ABS_CalShift_H.get(site)>=7 ? 7 : StaticFields.efuse_ABS_CalShift_H.get(site));

            newCalShift_Round_L.set(site, calShift_Round_L.get(site)>=0 ? StaticFields.efuse_ABS_CalShift_L.get(site) : StaticFields.efuse_ABS_CalShift_L.multiply(-1).get(site) );
            newCalShift_Round_M.set(site, calShift_Round_M.get(site)>=0 ? StaticFields.efuse_ABS_CalShift_M.get(site) : StaticFields.efuse_ABS_CalShift_M.multiply(-1).get(site) );
            newCalShift_Round_H.set(site, calShift_Round_H.get(site)>=0 ? StaticFields.efuse_ABS_CalShift_H.get(site) : StaticFields.efuse_ABS_CalShift_H.multiply(-1).get(site) );
        }

        MultiSiteLong avg_NewCalShift_Round=new MultiSiteLong(0);
        avg_NewCalShift_Round.set(newCalShift_Round_L.add(newCalShift_Round_M).add(newCalShift_Round_H).divide(3.0).round());

//        println("newCalShift_Round_L= "+newCalShift_Round_L);
//        println("newCalShift_Round_M= "+newCalShift_Round_M);
//        println("newCalShift_Round_H= "+newCalShift_Round_H);
//        println("avg_NewCalShift_Round= "+avg_NewCalShift_Round);

        return avg_NewCalShift_Round;
    }

}


