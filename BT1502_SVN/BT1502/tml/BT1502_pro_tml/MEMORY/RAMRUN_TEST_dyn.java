package BT1502_pro_tml.MEMORY;

import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_UART;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDigInOut;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.testdescriptor.IParametricTestDescriptor;
import xoc.dta.workspace.IWorkspace;


public class RAMRUN_TEST_dyn extends TestMethod {

    @In public String spec_measurement;
    @In public String spec_measurement_UART;
    @In public String fileName;
    @In public String pattern_name = "vectors.pattern_fix.Dummy"; //SWD pattern
    @In public double wait_time_s;

    public IMeasurement measurement_setuart;
    public IMeasurement measurement_dlduart;
    public IMeasurement measurement_read;
    public IMeasurement measurement_informtion_read;

    public IParametricTestDescriptor ptd_pass_flag;
    public IParametricTestDescriptor ptd_enterMode;
    public IParametricTestDescriptor ptd_module_mark;
    public IParametricTestDescriptor ptd_module_case;
    public IParametricTestDescriptor ptd_module_state_CPU;
    public IParametricTestDescriptor ptd_module_state_flash;
    public IParametricTestDescriptor ptd_module_state_cp;
    public IParametricTestDescriptor ptd_module_state_SRAM;
    public IParametricTestDescriptor ptd_module_state_PSRAM;
    public IParametricTestDescriptor ptd_flash_mark;
    public IParametricTestDescriptor ptd_errcnt_mark;

    public IParametricTestDescriptor ptd_module_case0;
    public IParametricTestDescriptor ptd_module_case1;
    public IParametricTestDescriptor ptd_module_state0;
    public IParametricTestDescriptor ptd_module_state1;
    public IParametricTestDescriptor ptd_module_tag;

    public IParametricTestDescriptor ptd_errcnt;


             public IParametricTestDescriptor       ptd_module_case04 ;
             public IParametricTestDescriptor       ptd_module_case08 ;
             public IParametricTestDescriptor       ptd_module_case12 ;
             public IParametricTestDescriptor       ptd_module_case16 ;
             public IParametricTestDescriptor       ptd_module_case20 ;
             public IParametricTestDescriptor       ptd_module_state04 ;
             public IParametricTestDescriptor       ptd_module_state08 ;
             public IParametricTestDescriptor       ptd_module_state12 ;
             public IParametricTestDescriptor       ptd_module_state16 ;
             public IParametricTestDescriptor       ptd_module_state20 ;


  public long digital_st = 0x20040020L; //module_mark
  public long digital_sp = 0x20040090L; //errcnt

    @Override
    public void setup ()
    {
        // init chip 26M & UART
        IDeviceSetup deviceSetup1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup1.importSpec(spec_measurement);

        BesPA_I2C i2c=new BesPA_I2C(deviceSetup1, measurement_setuart, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        //operating sequence
        i2c.transactionSequenceBegin("enter_uart_mode");
            i2c.write(BesI2cAddrType.DIGITAL,0x40080038,0x00000130);
            i2c.write(BesI2cAddrType.DIGITAL,0x400800C8,0x00000080);
            i2c.waitTime(20e6);
            i2c.write(BesI2cAddrType.DIGITAL,0x40080038,0x00000130);
            i2c.write(BesI2cAddrType.DIGITAL,0x400800C8,0x00000080);
            i2c.waitTime(20e6);
        i2c.transactionSequenceEnd();

        measurement_setuart.setSetups(deviceSetup1);


        // dld UART
        IDeviceSetup deviceSetup3 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup3.importSpec(spec_measurement_UART);

        if(StaticFields.dev_name == "WLCSP")
        {
            fileName = "common_best1502x_ramrun_dcdc0p8V_logic0p8V_ldo0p8V_pll_dcdc0p9V_202207151640.bin";
        }
        if(StaticFields.dev_name == "BGA7273")
        {
            fileName = "common_best1502x_ramrun_dcdc0.8V_logic0.8V_ldo0.8V_pll_dcdc0.9V_202207151641.bin";
        }

        context.workspace();
        new BesDsa_UART(deviceSetup3).writeToRAM_NewPA(measurement_dlduart,IWorkspace.getActiveProjectPath()+ "/tml/BT1502_pro_tml/MEMORY/ramrun_bin_file/"+fileName);
        measurement_dlduart.setSetups(deviceSetup3);


        //measurement_read
        IDeviceSetup deviceSetup_read =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup_read.importSpec(spec_measurement);

        BesPA_I2C i2c_read=new BesPA_I2C(deviceSetup_read, measurement_read, I2cRegAddrBits.RegAddr_8Bits);
        i2c_read.setSignals("I2C_SCL", "I2C_SDA");

        //POWKEY
        ISetupDigInOut digInOut_POWKEY=deviceSetup_read.addDigInOut("POWKEY").setConnect(true).setDisconnect(true);
        digInOut_POWKEY.protection().setDisconnectPulldownState(false);
        digInOut_POWKEY.vforce("Vf_POWKEY_0V").setForceValue("0 V").setIclamp("40 mA");
        digInOut_POWKEY.vforce("Vf_POWKEY_1p8V").setForceValue("1.8 V").setIclamp("40 mA");

        //operating sequence
        deviceSetup_read.actionCall("Vf_POWKEY_0V");
        deviceSetup_read.waitCall("200 ms");
        deviceSetup_read.actionCall("Vf_POWKEY_1p8V");
        deviceSetup_read.waitCall("200 ms");
        deviceSetup_read.actionCall("Vf_POWKEY_0V");
        deviceSetup_read.waitCall("200 ms");

        i2c_read.transactionSequenceBegin("read");
            i2c_read.waitTime(5e6);
            i2c_read.read(BesI2cAddrType.DIGITAL,0x400000F4,"readValue_0x400000F4");
        i2c_read.transactionSequenceEnd();
        measurement_read.setSetups(deviceSetup_read);

        //measurement_informtion_read
        IDeviceSetup deviceSetup_informtion_read =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup_informtion_read.importSpec(spec_measurement);

        BesPA_I2C i2c_read_info=new BesPA_I2C(deviceSetup_informtion_read, measurement_informtion_read, I2cRegAddrBits.RegAddr_8Bits);
        i2c_read_info.setSignals("I2C_SCL", "I2C_SDA");

        i2c_read_info.transactionSequenceBegin("information_read");
            for(long i=digital_st;i<=digital_sp;i=i+4)
            {
                i2c_read_info.read(BesI2cAddrType.DIGITAL,i,"reg_rd_digital_0x"+Long.toHexString(i));
            }
        i2c_read_info.transactionSequenceEnd();

        measurement_informtion_read.setSetups(deviceSetup_informtion_read);

    }

    @Override
    public void execute ()
    {
        measurement_setuart.execute();
        measurement_dlduart.execute();

        MultiSiteLong pass_flag= new MultiSiteLong(-1);
        MultiSiteLong enterMode_flag= new MultiSiteLong(-1);
        int siteCount_done_ramrun=0, loop_count=0;
        long time_out_loopCount=Math.round(wait_time_s*1e3)/13;
        int siteLength=context.getActiveSites().length;
        while((siteCount_done_ramrun < siteLength) && (loop_count < time_out_loopCount))
        {
            loop_count++;
            measurement_read.execute();
            MultiSiteLong bt_read_0x400000F4 =ProtocolAccess.preserveResults(measurement_read).getResult("readValue_0x400000F4");
            siteCount_done_ramrun=0;
            int[] activeSites = context.getActiveSites();
            pass_flag=new MultiSiteLong(-1);
            for(int site:activeSites)
            {
                if(bt_read_0x400000F4.get(site)==0x9a55)
                {
                    pass_flag.set(site,1);
                }
                else
                {
                    pass_flag.set(site,-1);
                }
                if(bt_read_0x400000F4.get(site)==0xffffffffL)
                {
                    enterMode_flag.set(site,-1);
                }
                else
                {
                    enterMode_flag.set(site,1);
                }
                if(bt_read_0x400000F4.get(site)==0x9a55 || bt_read_0x400000F4.get(site)==0xfa11)// || bt_read_0x400000F4.get(site)!=0
                {
                    siteCount_done_ramrun++;
                }
                if(StaticFields.debugMode)//StaticFields.debugMode
                {
                    String testSuiteName_Qualified=context.getTestSuiteName();
                    String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
                    println("**********"+testSuiteName+"**********");
                    println("****time_out_loopCount******"+time_out_loopCount+"**********");
                    println("****loop_count******"+loop_count+"**********");
                    println("bt_read_0x400000F4      = 0x"+Long.toHexString(bt_read_0x400000F4.get(site))+" [site "+site+"]");
                    println();
                }
            }
            if(loop_count>time_out_loopCount) {
                break;
            }
        }

        measurement_informtion_read.execute();
        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults paRes=ProtocolAccess.preserveResults(measurement_informtion_read);
        MultiSiteLong module_mark  = new MultiSiteLong();//data_0x2050001c
        MultiSiteLong module_case0  = new MultiSiteLong();//data_0x20500020
        MultiSiteLong module_case1  = new MultiSiteLong();//data_0x20500020
        MultiSiteLong module_state0 = new MultiSiteLong();//data_0x20500024
        MultiSiteLong module_state1 = new MultiSiteLong();//data_0x20500024
        MultiSiteLong module_tag   = new MultiSiteLong();//data_0x20500028
        MultiSiteLong flash_mark   = new MultiSiteLong();//data_0x2050002c
        MultiSiteLong errcnt   = new MultiSiteLong(0);//data_0x2050002c
        module_mark    = paRes.getResult("reg_rd_digital_0x20040020");
        module_case0   = paRes.getResult("reg_rd_digital_0x20040024");
        module_case1   = paRes.getResult("reg_rd_digital_0x20040028");
        module_state0  = paRes.getResult("reg_rd_digital_0x20040034");
        module_state1  = paRes.getResult("reg_rd_digital_0x20040038");
        module_tag     = paRes.getResult("reg_rd_digital_0x20040044");
        flash_mark     = paRes.getResult("reg_rd_digital_0x20040058");
        errcnt         = paRes.getResult("reg_rd_digital_0x2004005C");
//        module_case0   = module_case0.subtract(3);


        MultiSiteLong  module_case04   = module_case0.and(0xf);
        MultiSiteLong  module_case08   = module_case0.rightShift(4).and(0xf);
        MultiSiteLong  module_case12   = module_case0.rightShift(8).and(0xf);
        MultiSiteLong  module_case16   = module_case0.rightShift(12).and(0xf);
        MultiSiteLong  module_case20   = module_case0.rightShift(16).and(0xf);
     //   MultiSiteLong  module_case24   = module_case20.rightShift(4);
        MultiSiteLong  module_state04   = module_state0.and(0xf);
        MultiSiteLong  module_state08   = module_state0.rightShift(4).and(0xf);
        MultiSiteLong  module_state12   = module_state0.rightShift(8).and(0xf);
        MultiSiteLong  module_state16   = module_state0.rightShift(12).and(0xf);
        MultiSiteLong  module_state20   = module_state0.rightShift(16).and(0xf);

        if(StaticFields.debugMode)//StaticFields.debugMode
        {
            for(int site1 : context.getActiveSites())
            {
                println("module_mark  = 0x"+Long.toHexString(module_mark .get(site1))+" : site["+site1+"]");
                println("module_case  = 0x"+Long.toHexString(module_case0 .get(site1))+" : site["+site1+"]");
                println("module_state = 0x"+Long.toHexString(module_state1.get(site1))+" : site["+site1+"]");
//                println("module_state_CPU   = 0x"+Long.toHexString(module_state.and(0x1).get(site))+" : site["+site+"]");
//                println("module_state_SRAM  = 0x"+Long.toHexString(module_state.rightShift(1) .and(0x3f) .get(site))+" : site["+site+"]");
//                println("module_state_flash = 0x"+Long.toHexString(module_state.rightShift(7) .and(0xf).get(site))+" : site["+site+"]");
//                println("module_state_PSRM  = 0x"+Long.toHexString(module_state.rightShift(25).and(0x7) .get(site))+" : site["+site+"]");
                println("module_tag   = 0x"+Long.toHexString(module_tag  .get(site1))+" : site["+site1+"]");
                println("flash_mark   = 0x"+Long.toHexString(flash_mark  .get(site1))+" : site["+site1+"]");
                println();
            }
        }

        ptd_pass_flag.         evaluate(pass_flag); // 9A55
        ptd_enterMode.         evaluate(enterMode_flag);
        ptd_module_tag.        evaluate(module_tag);
        ptd_module_case0.      evaluate(module_case0);
        ptd_module_case1.      evaluate(module_case1);
        ptd_module_state0.     evaluate(module_state0);
        ptd_module_state1.     evaluate(module_state1);
        ptd_module_mark.       evaluate(module_mark);
//        ptd_module_state_CPU.  evaluate(module_state0.and(0x1));
//        ptd_module_state_SRAM. evaluate(module_state0.rightShift(1).and(0x3f));
//        ptd_module_state_flash.evaluate(module_state0.rightShift(7).and(0xf));
//        ptd_module_state_cp.   evaluate(module_state0.rightShift(15).and(0x1ff));
        ptd_flash_mark.evaluate(flash_mark);

//        ptd_errcnt.            evaluate(errcnt);
        ptd_module_case04   . evaluate(module_case04) ;
        ptd_module_case08   . evaluate(module_case08) ;
        ptd_module_case16   . evaluate(module_case16) ;
        ptd_module_case12   . evaluate(module_case12) ;
        ptd_module_case20   . evaluate(module_case20) ;
        ptd_module_state04  . evaluate(module_state04) ;
        ptd_module_state08  . evaluate(module_state08) ;
        ptd_module_state16  . evaluate(module_state16) ;
        ptd_module_state12  . evaluate(module_state12) ;
        ptd_module_state20  . evaluate(module_state20) ;

           }
       }