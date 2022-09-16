package BT1502_pro_tml.MEMORY;

import java.util.ArrayList;
import java.util.Map;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupProtocolInterface;
import xoc.dsa.ISetupTransactionSeqDef;
import xoc.dsa.ISetupTransactionSeqDef.Direction;
import xoc.dsa.ISetupTransactionSeqDef.Type;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IProtocolInterfaceResults;
import xoc.dta.resultaccess.ITransactionSequenceResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;
import xoc.dta.workspace.IWorkspace;


public class RAMRUN_TEST_TEXT_dyn extends TestMethod {

    @In public String spec_measurement;
    @In public String spec_measurement_UART;
    @In public String spec_measurement_SWD;

    @In public String file_name;
    @In public String pattern_name = "vectors.pattern_fix.Dummy"; //SWD pattern
    @In public String download_mode = "UART";//Only support:"URAT","SWD"

    @In public double Freq_MHz_SWD = 1;//limiting download speed 8MHz(Fsys/3).

    @In public double wait_time_s;

    public IMeasurement measurement_setuart;
    public IMeasurement measurement_setswd;
    public IMeasurement measurement_dlduart;
    public IMeasurement measurement_dldswd;
    public IMeasurement measurement_read;

    public IParametricTestDescriptor ptd_pass_flag;

    public uart_dyn dyn = new uart_dyn();

    String trCallName = "";

    @Override
    public void setup ()
    {
        // init chip 26M & UART
        IDeviceSetup deviceSetup1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup1.importSpec(spec_measurement);

        ISetupProtocolInterface paInterface1 = deviceSetup1.addProtocolInterface("I2C_BES", "besLib.pa.I2C_8bit_BES");
        paInterface1.addSignalRole("DATA", "I2C_SDA");
        paInterface1.addSignalRole("CLK", "I2C_SCL");

        ISetupTransactionSeqDef transDigSrc1= paInterface1.addTransactionSequenceDef("enter_uart_mode");
        //*****************************************************************************//
        transDigSrc1.addParameter(Type.UnsignedLong, Direction.OUT, "reg_rd_digital_0x0");
        transDigSrc1.addParameter(Type.UnsignedLong, Direction.OUT, "reg_rd_digital_0x1");
        transDigSrc1.addParameter(Type.UnsignedLong, Direction.OUT, "reg_rd_digital_0x2");
        transDigSrc1.addParameter(Type.UnsignedLong, Direction.OUT, "reg_rd_digital_0x3");
        transDigSrc1.addParameter(Type.UnsignedLong, Direction.OUT, "reg_rd_digital_0x4");
        transDigSrc1.addTransaction("DIGITAL_READ",0x40085038,"reg_rd_digital_0x0");
        transDigSrc1.addTransaction("DIGITAL_READ",0x40080004,"reg_rd_digital_0x1");
        transDigSrc1.addTransaction("DIGITAL_READ",0x400800cc,"reg_rd_digital_0x2");
        transDigSrc1.addTransaction("DIGITAL_READ",0x400800cc,"reg_rd_digital_0x3");
        transDigSrc1.addTransaction("DIGITAL_READ",0xd0350214,"reg_rd_digital_0x4");
        //*****************************************************************************//
//        transDigSrc1.addTransaction("PMU_WRITE",0x0d,0x1318);
        transDigSrc1.addTransaction("DIGITAL_WRITE",0x40080038,0x00000130);
        transDigSrc1.addTransaction("DIGITAL_WRITE",0x400800C8,0x00000080);
        transDigSrc1.addWait("20 ms");

        transDigSrc1.addTransaction("DIGITAL_WRITE",0x40080038,0x00000130);
        transDigSrc1.addTransaction("DIGITAL_WRITE",0x400800C8,0x00000080);
        transDigSrc1.addWait("20 ms");

        deviceSetup1.sequentialBegin();
            deviceSetup1.transactionSequenceCall(transDigSrc1);
        deviceSetup1.sequentialEnd();

        measurement_setuart.setSetups(deviceSetup1);

        //SWD
        IDeviceSetup deviceSetup2 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup2.importSpec(spec_measurement);

        ISetupProtocolInterface paInterface2 = deviceSetup2.addProtocolInterface("I2C_BES", "besLib.pa.I2C_8bit_BES");
        paInterface2.addSignalRole("DATA", "I2C_SDA");
        paInterface2.addSignalRole("CLK", "I2C_SCL");
        ISetupTransactionSeqDef transDigSrc2= paInterface2.addTransactionSequenceDef("enter_swd_mode");
        //[switch to swd 31/32]
        transDigSrc2.addTransaction("DIGITAL_WRITE",0x40086010,0xfffff44f);//sel iomux:swd
        transDigSrc2.addTransaction("DIGITAL_WRITE",0x4008602c,0x00000000);//clear pullup
        transDigSrc2.addTransaction("DIGITAL_WRITE",0x40086030,0x00000000);//clear pulldown
        transDigSrc2.addTransaction("DIGITAL_WRITE",0x40080004,0x00000298);//open jtag clock
        transDigSrc2.addWait("20 ms");

        deviceSetup2.sequentialBegin();
            deviceSetup2.transactionSequenceCall(transDigSrc2);
        deviceSetup2.sequentialEnd();

        measurement_setswd.setSetups(deviceSetup2);

        // dld UART
        IDeviceSetup deviceSetup3 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup3.importSpec(spec_measurement_UART);

        ISetupProtocolInterface paInterface = deviceSetup3.addProtocolInterface("UART_BES", "besLib.pa.UART_BES");
        paInterface.addSignalRole("RXD", "I2C_SCL");
        paInterface.addSignalRole("TXD", "I2C_SDA");
        ISetupTransactionSeqDef transDigSrc= paInterface.addTransactionSequenceDef("bootCode_setup");

        ArrayList<String[]> regList = new ArrayList<>();
        context.workspace();
        regList = dyn.getRegFromFile(IWorkspace.getActiveProjectPath()+ "/tml/BT1501_pro_tml/MEMORY/"+file_name);
        for(int i=0; i<regList.size(); i++) {
            String[] tmpArray = regList.get(i);
            int datacnt = tmpArray.length;
            for(int j=0; j<tmpArray.length; j++) {
                transDigSrc.addTransaction("writeUart","0x"+tmpArray[j]);
            }
            if(datacnt<32) {
                transDigSrc.addWait("5 ms");
            }
        }
        deviceSetup3.sequentialBegin();
        {
            deviceSetup3.transactionSequenceCall(transDigSrc);
        }
        deviceSetup3.sequentialEnd();
        measurement_dlduart.setSetups(deviceSetup3);

        // dld SWD
        IDeviceSetup deviceSetup4 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup4.importSpec(spec_measurement_SWD);

        deviceSetup4.sequentialBegin();
            deviceSetup4.patternCall(pattern_name);
        deviceSetup4.sequentialEnd();
        measurement_dldswd.setSetups(deviceSetup4);

        //measurement_read
        IDeviceSetup deviceSetup_read =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup_read.importSpec(spec_measurement);
        ISetupProtocolInterface paInterface_btdebug_read = deviceSetup_read.addProtocolInterface("I2C_BES", "besLib.pa.I2C_8bit_BES");
        paInterface_btdebug_read.addSignalRole("DATA", "I2C_SDA");
        paInterface_btdebug_read.addSignalRole("CLK", "I2C_SCL");

        ISetupTransactionSeqDef transDigSrc_read= paInterface_btdebug_read.addTransactionSequenceDef("read");
        transDigSrc_read.addParameter(Type.UnsignedLong, Direction.OUT, "data2");

        transDigSrc_read.addTransaction("DIGITAL_READ",0x400000F4,"data2");

        deviceSetup_read.sequentialBegin();
            deviceSetup_read.waitCall("5 ms");
            trCallName=deviceSetup_read.transactionSequenceCall(transDigSrc_read);
        deviceSetup_read.sequentialEnd();

        measurement_read.setSetups(deviceSetup_read);

    }

    @Override
    public void update ()
    {
        if(download_mode == "SWD")
        {
            measurement_dldswd.spec().setVariable("Freq_MHz_eqn12", Freq_MHz_SWD*1e6);
        }
    }

    @Override
    public void execute ()
    {

        if(download_mode == "UART")
        {
            measurement_setuart.execute();
            measurement_dlduart.execute();
        }

        if(download_mode == "SWD")
        {
            measurement_setswd.execute();
            measurement_dldswd.execute();
        }

        MultiSiteLong pass_flag= new MultiSiteLong(-1);
        int siteCount_done_ramrun=0, loop_count=0;
        long time_out_loopCount=Math.round(wait_time_s*1e3)/13;
        int siteLength=context.getActiveSites().length;
        while((siteCount_done_ramrun < siteLength) && (loop_count < time_out_loopCount))
        {
            loop_count++;
            measurement_read.execute();
            IProtocolInterfaceResults pa_read = measurement_read.protocolInterface("I2C_BES").preserveResults();
            ITransactionSequenceResults tsr_read = pa_read.transactSeq(trCallName,0);
            Map<String, MultiSiteLong> capResult_read = tsr_read.getValueAsLong();
            MultiSiteLong bt_read_0x400000F4 = capResult_read.get("data2");


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
                if(bt_read_0x400000F4.get(site)==0x9a55 || bt_read_0x400000F4.get(site)==0xfa11)// || bt_read_0x400000F4.get(site)!=0
                {
                    siteCount_done_ramrun++;
                }
                if(StaticFields.debugMode)//StaticFields.debugMode
                {
                    String testSuiteName_Qualified=context.getTestSuiteName();
                    String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
                    println("**********"+testSuiteName+"**********");
                    println("bt_read_0x400000F4      = 0x"+Long.toHexString(bt_read_0x400000F4.get(site))+" [site "+site+"]");
                    println();
                }
            }
            if(loop_count>time_out_loopCount) {
                break;
            }
        }

        ptd_pass_flag.evaluate(pass_flag);

    }
}
