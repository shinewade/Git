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
import xoc.dta.measurement.IMeasurement;
import xoc.dta.workspace.IWorkspace;


public class Ramrun_Bin_Uart_Download_dyn extends TestMethod {

    @In public String spec_measurement;
    @In public String spec_measurement_UART;

    @In public String fileName;

    public IMeasurement measurement_setuart;
    public IMeasurement measurement_dlduart;

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
        transDigSrc1.addTransaction("DIGITAL_WRITE",0x40080038,0x00000300);
        transDigSrc1.addTransaction("DIGITAL_WRITE",0x400800C8,0x00000400);
        transDigSrc1.addWait("20 ms");

        transDigSrc1.addTransaction("DIGITAL_WRITE",0x40080038,0x00000300);
        transDigSrc1.addTransaction("DIGITAL_WRITE",0x400800C8,0x00000400);
        transDigSrc1.addWait("20 ms");

        deviceSetup1.sequentialBegin();
            deviceSetup1.transactionSequenceCall(transDigSrc1);
        deviceSetup1.sequentialEnd();

        measurement_setuart.setSetups(deviceSetup1);



        // dld UART
        IDeviceSetup deviceSetup2 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup2.importSpec(spec_measurement_UART);
        String testSuiteName_Qualified=context.getTestSuiteName();
        String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
        if(testSuiteName.equals("Ramrun_Sram_Flash_Bin_Download"))
        {
            if(StaticFields.dev_name == "BGA4173")
            {
//                fileName = "common2_best1600_ramrun_0p74V_0p74V_osc_x4_x2_pll208_dvx2_ram_rw32_flash_scr_wr_rd_4kB_at_128kB_a_202111092045.bin";
//                fileName = "common3_best1600_ramrun_0p74V_0p74V_osc_x4_x2_pll208_dvx2_ram_full_dyn_off_no_bs_flash_scr_wr_rd_4kB_at_128kB_a_cp_202111201738.bin";
                fileName = "common3_best1600_ramrun_0p74V_0p74V_osc_x4_x2_pll208_dvx2_ram_full_dyn_off_no_bitscan_flash_scr_wr_rd_8mB_4kB_at_128kB_no_psram_cp_202112282102.bin";
            }
            if(StaticFields.dev_name == "BGA7273")
            {
//                fileName = "watch_best1600_ramrun_0p74V_0p74V_osc_x4_x2_pll208_dvx2_ram_rw32_flash_scr_wr_rd_4kB_at_128kB_psram_202111092051.bin";
                fileName = "watch_best1600_ramrun_0p74V_0p74V_osc_x4_x2_pll208_dvx2_ram_full_dyn_off_no_bs_flash_scr_wr_rd_4kB_at_128kB_psram_cp_202111221201.bin";
            }
        }
        context.workspace();
        new BesDsa_UART(deviceSetup2).writeToRAM_NewPA(measurement_dlduart,IWorkspace.getActiveProjectPath()+ "/tml/BT1502_pro_tml/MEMORY/ramrun_bin_file/"+fileName);
        measurement_dlduart.setSetups(deviceSetup2);



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
