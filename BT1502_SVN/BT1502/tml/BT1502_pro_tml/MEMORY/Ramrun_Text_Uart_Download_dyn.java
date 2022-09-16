package BT1502_pro_tml.MEMORY;

import java.util.ArrayList;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupProtocolInterface;
import xoc.dsa.ISetupTransactionSeqDef;
import xoc.dsa.ISetupTransactionSeqDef.Direction;
import xoc.dsa.ISetupTransactionSeqDef.Type;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.workspace.IWorkspace;


public class Ramrun_Text_Uart_Download_dyn extends TestMethod {

    @In public String spec_measurement;
    @In public String spec_measurement_UART;

    @In public String fileName;

    public IMeasurement measurement_setuart;
    public IMeasurement measurement_dlduart;

    public uart_dyn dyn = new uart_dyn();

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



        // dld UART
        IDeviceSetup deviceSetup3 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup3.importSpec(spec_measurement_UART);

        ISetupProtocolInterface paInterface = deviceSetup3.addProtocolInterface("UART_BES", "besLib.pa.UART_BES");
        paInterface.addSignalRole("RXD", "I2C_SCL");
        paInterface.addSignalRole("TXD", "I2C_SDA");
        ISetupTransactionSeqDef transDigSrc= paInterface.addTransactionSequenceDef("bootCode_setup");

        ArrayList<String[]> regList = new ArrayList<>();
        context.workspace();
        regList = dyn.getRegFromFile(IWorkspace.getActiveProjectPath()+ "/tml/BT1501_pro_tml/MEMORY/"+fileName);
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



    }

    @Override
    public void execute ()
    {
        measurement_setuart.execute();
        measurement_dlduart.execute();

        if(StaticFields.debugMode)//StaticFields.debugMode
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println();
        }
    }
}
