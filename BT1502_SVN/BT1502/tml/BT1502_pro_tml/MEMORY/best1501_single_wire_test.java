package BT1502_pro_tml.MEMORY;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_UART;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupProtocolInterface;
import xoc.dsa.ISetupTransactionSeqDef;
import xoc.dsa.ISetupTransactionSeqDef.Direction;
import xoc.dsa.ISetupTransactionSeqDef.Type;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteBoolean;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.MultiSiteString;
import xoc.dta.datatypes.dsp.MultiSiteWaveLong;
import xoc.dta.measurement.ILocalMeasurement;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigInOutCaptureResults;
import xoc.dta.resultaccess.IProtocolInterfaceResults;
import xoc.dta.resultaccess.ITransactionSequenceResults;
import xoc.dta.resultaccess.datatypes.BitSequence.BitOrder;
import xoc.dta.testdescriptor.IParametricTestDescriptor;
import xoc.dta.workspace.IWorkspace;


public class best1501_single_wire_test extends TestMethod {

    @In public String spec_measurement;
    @In public String spec_measurement_UART;
    public String pattern1,pattern2;
    public IMeasurement measurement_dlduart;
    public IMeasurement measurement_read;
    public ILocalMeasurement measurement_uartwrite;
    public IMeasurement measurement_setuart;




    public IParametricTestDescriptor ptd_result_rx;
    public IParametricTestDescriptor ptd_result_tx;


    public uart_dyn dyn = new uart_dyn();

    String trCallName1 = "";
    String trCallName2 = "";
    String trCallName3 = "";


    @Override
    public void setup ()
    {


        // init chip 26M & UART
        IDeviceSetup deviceSetup0 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup0.importSpec(spec_measurement);

        ISetupProtocolInterface paInterface1 = deviceSetup0.addProtocolInterface("I2C_BES", "setups.I2C.I2C_BES");
        paInterface1.addSignalRole("DATA", "I2C_SDA");
        paInterface1.addSignalRole("CLK", "I2C_SCL");

        ISetupTransactionSeqDef transDigSrc1= paInterface1.addTransactionSequenceDef("enter_uart_mode");
        //*****************************************************************************//
//        transDigSrc1.addParameter(Type.UnsignedLong, Direction.OUT, "reg_rd_digital_0x0");
//        transDigSrc1.addParameter(Type.UnsignedLong, Direction.OUT, "reg_rd_digital_0x1");
//        transDigSrc1.addParameter(Type.UnsignedLong, Direction.OUT, "reg_rd_digital_0x2");
//        transDigSrc1.addParameter(Type.UnsignedLong, Direction.OUT, "reg_rd_digital_0x3");
//        transDigSrc1.addParameter(Type.UnsignedLong, Direction.OUT, "reg_rd_digital_0x4");
//        transDigSrc1.addTransaction("DIGITAL_READ",0x40085038,"reg_rd_digital_0x0");
//        transDigSrc1.addTransaction("DIGITAL_READ",0x40080004,"reg_rd_digital_0x1");
//        transDigSrc1.addTransaction("DIGITAL_READ",0x400800cc,"reg_rd_digital_0x2");
//        transDigSrc1.addTransaction("DIGITAL_READ",0x400800cc,"reg_rd_digital_0x3");
//        transDigSrc1.addTransaction("DIGITAL_READ",0xd0350214,"reg_rd_digital_0x4");
        //*****************************************************************************//
//        transDigSrc1.addTransaction("PMU_WRITE",0x0d,0x1318);
        transDigSrc1.addTransaction("DIGITAL_WRITE",0x40080038,0x00000130);
        transDigSrc1.addTransaction("DIGITAL_WRITE",0x400800C8,0x00000080);
        transDigSrc1.addWait("20 ms");

        transDigSrc1.addTransaction("DIGITAL_WRITE",0x40080038,0x00000130);
        transDigSrc1.addTransaction("DIGITAL_WRITE",0x400800C8,0x00000080);
        transDigSrc1.addWait("20 ms");

        deviceSetup0.sequentialBegin();
            deviceSetup0.transactionSequenceCall(transDigSrc1);
        deviceSetup0.sequentialEnd();

        measurement_setuart.setSetups(deviceSetup0);


        // dld UART & single read 0x12345678 from IC

        IDeviceSetup deviceSetup1 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup1.importSpec(spec_measurement_UART);
        pattern1="vectors.pattern_fix.UART_COM_READ";
        pattern2="vectors.pattern_fix.UART_COM_WRITE";


        context.workspace();
        new BesDsa_UART(deviceSetup1).writeToRAM_NewPA(measurement_dlduart,IWorkspace.getActiveProjectPath()+ "/tml/BT1501_pro_tml/MEMORY/best1501_single_wire_test.bin");
//        deviceSetup1.sequentialBegin();
            deviceSetup1.waitCall("20 ms");
            deviceSetup1.patternCall(pattern1);
            deviceSetup1.patternCall(pattern2);
//        deviceSetup1.sequentialEnd();
        measurement_dlduart.setSetups(deviceSetup1);


        //single wire write 0xaabbccdd to IC
        IDeviceSetup deviceSetup3 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup3.importSpec(spec_measurement_UART);
        pattern2="vectors.pattern_fix.UART_COM_WRITE";

        deviceSetup3.sequentialBegin();
            deviceSetup3.waitCall("20 ms");
            deviceSetup3.patternCall(pattern2);
        deviceSetup3.sequentialEnd();

        measurement_uartwrite.setSetups(deviceSetup3);

        //measurement_read
        //measurement3
        IDeviceSetup deviceSetup_read =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup_read.importSpec(spec_measurement);

        ISetupProtocolInterface paInterface4 = deviceSetup_read.addProtocolInterface("I2C_BES", "setups.I2C.I2C_BES");
        paInterface4.addSignalRole("DATA", "I2C_SDA");
        paInterface4.addSignalRole("CLK", "I2C_SCL");
        ISetupTransactionSeqDef transDigSrc4= paInterface4.addTransactionSequenceDef("MCU_104M52M_07V_check_1");
        transDigSrc4.addParameter(Type.UnsignedLong, Direction.OUT, "MCU_104M52M_07V_check");
        transDigSrc4.addTransaction("DIGITAL_READ",0x400000f4,"MCU_104M52M_07V_check");

        deviceSetup_read.sequentialBegin();
            deviceSetup_read.waitCall("50 ms");
            trCallName1=deviceSetup_read.transactionSequenceCall(transDigSrc4);
        deviceSetup_read.sequentialEnd();
        measurement_read.setSetups(deviceSetup_read);


    }
    @Override
    public void update ()
    {
        measurement_dlduart.digInOut().result().capture().setEnabled(true);
        measurement_dlduart.digInOut().result().callPassFail().setEnabled(false);
    }

    @Override
    public void execute ()
    {
        int[] activeSites = context.getActiveSites();
        measurement_setuart.execute();
        measurement_dlduart.execute();

        MultiSiteLong         index_startbit=new MultiSiteLong(0);
        MultiSiteLong         Extracted_data=new MultiSiteLong();
        MultiSiteLong         data_value=new MultiSiteLong(0);

        MultiSiteString            DataString=new MultiSiteString();
        MultiSiteString            DataString1=new MultiSiteString();
        MultiSiteString            Reverse_string=new MultiSiteString();
        MultiSiteBoolean isFirst_FallEdge=new MultiSiteBoolean(false);
        MultiSiteBoolean ComparePass=new MultiSiteBoolean(false);
        MultiSiteLong         read_pass=new MultiSiteLong(0);




        IDigInOutCaptureResults result1 = measurement_dlduart.digInOut().preserveCaptureResults();

        MultiSiteWaveLong UART_COM_rawData=result1.getSerialBitsAsWaveLong("LED3", 1, BitOrder.LEFT_TO_RIGHT);//UART_COM_rawData
//        UART_COM_rawData.plot("captrueData");
        MultiSiteWaveLong data1= new MultiSiteWaveLong();
        MultiSiteWaveLong data2= new MultiSiteWaveLong();
        MultiSiteWaveLong data3= new MultiSiteWaveLong();
        MultiSiteWaveLong data4= new MultiSiteWaveLong();
        MultiSiteWaveLong data_f= new MultiSiteWaveLong();

        isFirst_FallEdge.set(false);
        for(int site:activeSites) {
          for(int index=0;index<UART_COM_rawData.getSize(site);index++) {
              if( UART_COM_rawData.getValue(index).get(site)==0 && UART_COM_rawData.getValue(index+1).get(site)==0 && UART_COM_rawData.getValue(index+2).get(site)==0 && isFirst_FallEdge.get(site)==false)
              {
                  index_startbit.set(site, index+4);//
                  isFirst_FallEdge.set(site, true);

              }
          }
          data1.set(site, UART_COM_rawData.get(site).extractValues((int) (index_startbit.get(site)+8*1), 8, 8));
          data2.set(site, UART_COM_rawData.get(site).extractValues((int) (index_startbit.get(site)+8*11), 8, 8));
          data3.set(site, UART_COM_rawData.get(site).extractValues((int) (index_startbit.get(site)+8*21), 8, 8));
          data4.set(site, UART_COM_rawData.get(site).extractValues((int) (index_startbit.get(site)+8*31), 8, 8));
//          DataString.set(site, data1.get(site).);
          data_f.set(site, data1.get(site).append(data2.get(site)).append(data3.get(site)).append(data4.get(site)));
          for(int j=0;j<data_f.getSize(site);j++) {
              data_value.set(site,data_value.get(site)+data_f.get(site).getValue(j)*(long)Math.pow(2, j));

//              println("j data_f =  "+" bit= "+data_f.get(site).getValue(j));

          }
//          println("data_value =  "+" data_value= "+data_value.get(site));

        }


        for(int site:activeSites)
        {
            if(data_value.get(site)==0x78563412)
            {
                ComparePass.set(site, true);
                read_pass.set(site, 1);
//                measurement_uartwrite.execute();
            }
//            println(ComparePass.get(site));
        }
        MultiSiteLong MCU_104M52M_07V_check = new MultiSiteLong(0xffff);
        MultiSiteLong id_read= new MultiSiteLong(-1);
        int siteCount_done_ramrun=0, loop_count=0;
        int time_out_loopCount=40; //per step 50ms
        int siteLength=context.getActiveSites().length;
        while((siteCount_done_ramrun < siteLength) && (loop_count < time_out_loopCount)){
//        while((loop_count < time_out_loopCount) ){
            loop_count++;
            measurement_read.execute();

            IProtocolInterfaceResults pa_bt_debug=measurement_read.protocolInterface("I2C_BES").preserveResults();

            ITransactionSequenceResults Result_tsr=pa_bt_debug.transactSeq(trCallName1,0);
             MCU_104M52M_07V_check=Result_tsr.getValueAsLong("MCU_104M52M_07V_check");

            siteCount_done_ramrun=0;
            id_read=new MultiSiteLong(-1);
            for(int site:activeSites)
            {
                if(MCU_104M52M_07V_check.get(site)==0x9a55) {
                    id_read.set(site,1);
                }
                else{
                    id_read.set(site,-1);
                }
                if(MCU_104M52M_07V_check.get(site)==0x9a55 ||MCU_104M52M_07V_check.get(site)==0xfa11 ) {
                    siteCount_done_ramrun++;
                }
//                    println("MCU_104M_075V_check= 0x"+Long.toHexString(MCU_104M52M_07V_check.get(site))+" [site "+site+"]");
//                    println("siteCount_done_ramrun= "+siteCount_done_ramrun);
//                    println("siteLength= "+siteLength);
//                    println("loop_count= "+loop_count);
//                    println("time_out_loopCount= "+time_out_loopCount);
            }
            if(loop_count>time_out_loopCount) {
                break;
            }
        }
        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println();
            println("**********"+testSuiteName+"**********");
            for(int site:activeSites) {
                println("MCU_104M_075V_check= 0x"+Long.toHexString(MCU_104M52M_07V_check.get(site))+" [site "+site+"]");
            }

        }


        ptd_result_rx.evaluate(id_read);
        ptd_result_tx.evaluate(read_pass);


    }
}
