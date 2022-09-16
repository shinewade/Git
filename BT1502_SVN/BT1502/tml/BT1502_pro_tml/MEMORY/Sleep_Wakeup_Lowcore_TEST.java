package BT1502_pro_tml.MEMORY;

import java.util.Map;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_PMIC;
import besLib.dsa.BesDsa_PMIC.V93K_Resource;
import besLib.dsa.BesDsa_UART;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDcVI;
import xoc.dsa.ISetupDcVI.SetupConnectMode;
import xoc.dsa.ISetupDcVI.SetupDisconnectMode;
import xoc.dsa.ISetupDigInOut;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDoubleArray;
import xoc.dta.datatypes.dsp.MultiSiteWaveDoubleArray;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;
import xoc.dta.workspace.IWorkspace;

public class Sleep_Wakeup_Lowcore_TEST extends TestMethod {

    @In
    public String spec_measurement;
    @In
    public String spec_measurement_UART;

    // @In public double wait_time_s;

    @In
    public String fileName;

    public IMeasurement measurement_setuart;
    public IMeasurement measurement_dlduart;
    public IMeasurement measurement_wakeup_sleep;

    public IParametricTestDescriptor ptd_I_VBAT_wakeup_1;
    public IParametricTestDescriptor ptd_V_VCore_wakeup_1;
    public IParametricTestDescriptor ptd_I_VBAT_sleep_1;
    public IParametricTestDescriptor ptd_V_VCore_sleep_1;
    public IParametricTestDescriptor ptd_I_VBAT_wakeup_2;
    public IParametricTestDescriptor ptd_V_VCore_wakeup_2;
    public IParametricTestDescriptor ptd_I_VBAT_sleep_2;
    public IParametricTestDescriptor ptd_V_VCore_sleep_2;

    public uart_dyn dyn = new uart_dyn();

    String trCallName = "";
    String trCallName1 = "";

    @Override
    public void setup() {
        // init chip 26M & UART
        IDeviceSetup deviceSetup1 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup1.importSpec(spec_measurement);

        new BesDsa_PMIC(deviceSetup1).vMeas_DcVI_Hiz("VANA", true, "Vm_VANA", V93K_Resource.DPS128);

        BesPA_I2C i2c = new BesPA_I2C(deviceSetup1, measurement_setuart,I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        // operating sequence
        i2c.transactionSequenceBegin("enter_uart_mode");
            i2c.write(BesI2cAddrType.DIGITAL, 0x40080038, 0x00000130);
            i2c.write(BesI2cAddrType.DIGITAL, 0x400800C8, 0x00000080);
            i2c.waitTime(20e6);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40080038, 0x00000130);
            i2c.write(BesI2cAddrType.DIGITAL, 0x400800C8, 0x00000080);
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
            fileName = "watch_best1600_ramrun_0p74V_0p74V_osc_x4_x2_pll208_dvx2_ram_full_dyn_off_no_bs_flash_scr_wr_rd_4kB_at_128kB_psram_cp_202111221201.bin";
        }

        context.workspace();
        new BesDsa_UART(deviceSetup3).writeToRAM_NewPA(measurement_dlduart,IWorkspace.getActiveProjectPath()+ "/tml/BT1502_pro_tml/MEMORY/ramrun_bin_file/"+fileName);
        measurement_dlduart.setSetups(deviceSetup3);
        // new
        // BesDsa_UART(deviceSetup3).writeToRAM_NewPA(measurement_dlduart,IWorkspace.getActiveProjectPath()+
        // "/tml/BT1502_pro_tml/MEMORY/ramrun_bin_file/"+fileName);
        measurement_dlduart.setSetups(deviceSetup3);

        // GPIO_01 low pulse, wakeup, measure I_VBAT and Vcore
        IDeviceSetup deviceSetup2 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup2.importSpec(spec_measurement);

        ISetupDigInOut Dig_GPIO_ds2 = deviceSetup2.addDigInOut("GPIO_14").setConnect(true)
                .setDisconnect(true);
        Dig_GPIO_ds2.frontendConnection("Disconnect_AC").setDigitalFrontendDisable();
        Dig_GPIO_ds2.vforce("GPIO_L").setForceValue("0.0 V").setIrange("10 mA").setVrange("2.00 V")
                .setIclamp("10 mA");
        Dig_GPIO_ds2.vforce("GPIO_H").setForceValue("1.8 V").setIrange("10 mA").setVrange("2.00 V")
                .setIclamp("10 mA");

        deviceSetup2.addDcVI("VSYS").imeas("imeas_Vbat_wakeup").setIrange("100 mA").setAverages(16)
                .setRestoreIrange(true);

        ISetupDcVI dcVi_VBAT_MeasWave = deviceSetup2.addDcVI("VSYS").setDisconnect(false);
        // dcVi_VBAT_MeasWave.measureWaveform("imeas_Vbat_wakeup_wave").setMeasTypeCurrent().setSampleRate("20*1e3
        // Hz").setSamples(300).setIrange("100
        // mA").setRestoreIrange(true).setHighAccuracy(true).setAveragesPerSample(8);
        dcVi_VBAT_MeasWave.measureWaveform("imeas_Vbat_sleep_wave").setMeasTypeCurrent()
                .setSampleRate("20*1e3 Hz").setSamples(300).setIrange("2.5 mA")
                .setRestoreIrange(true).setHighAccuracy(true).setAveragesPerSample(8);

        ISetupDcVI dcVi_VCORE_0p8_VM = deviceSetup2.addDcVI("VCORE_0p8")
                .setConnectMode(SetupConnectMode.highImpedance).setDisconnect(true)
                .setDisconnectMode(SetupDisconnectMode.hiz);
        dcVi_VCORE_0p8_VM.vmeas("Vm_VCORE_0p8").setWaitTime("2 ms").setAverages(8);

        deviceSetup2.sequentialBegin();
        deviceSetup2.actionCall("Disconnect_AC");
        for (int Loop = 0; Loop < 2; Loop++) {
            // wake up
            deviceSetup2.actionCall("GPIO_L");
            deviceSetup2.waitCall("50 ms");
            deviceSetup2.actionCall("imeas_Vbat_wakeup");
            deviceSetup2.actionCall("Vm_VCORE_0p8");
            deviceSetup2.waitCall("50 ms");
            // sleep
            deviceSetup2.actionCall("GPIO_H");
            deviceSetup2.waitCall("50 ms");
            deviceSetup2.actionCall("imeas_Vbat_sleep_wave");
            deviceSetup2.actionCall("Vm_VCORE_0p8");
        }
        deviceSetup2.sequentialEnd();
        measurement_wakeup_sleep.setSetups(deviceSetup2);
    }

    @Override
    public void execute() {
        measurement_setuart.execute();
        measurement_dlduart.execute();

        MultiSiteDoubleArray I_VBAT_wakeup = new MultiSiteDoubleArray(2, 0);
        MultiSiteDoubleArray I_VBAT_sleep = new MultiSiteDoubleArray(2, 0);
        MultiSiteDoubleArray V_VCore_wakeup = new MultiSiteDoubleArray(2, 0);
        MultiSiteDoubleArray V_VCore_sleep = new MultiSiteDoubleArray(2, 0);
        measurement_wakeup_sleep.execute();
        IDcVIResults DPSResult = measurement_wakeup_sleep.dcVI().preserveResults();

        Map<String, MultiSiteWaveDoubleArray> GetValue = DPSResult.measureWaveform("")
                .getWaveform();
        MultiSiteWaveDoubleArray wave_I_VBAT = GetValue.get("VSYS");
        I_VBAT_sleep.setElement(0, wave_I_VBAT.getElement(0).mean());
        I_VBAT_sleep.setElement(1, wave_I_VBAT.getElement(1).mean());

        for (int i = 0; i < 2; i++) {
            I_VBAT_wakeup.setElement(i,
                    DPSResult.imeas("imeas_Vbat_wakeup").getCurrent("VSYS").getElement(0 + i));

            V_VCore_wakeup.setElement(i,
                    DPSResult.vmeas("Vm_VCORE_0p8").getVoltage("VCORE_0p8").getElement(0 + i * 2));
            V_VCore_sleep.setElement(i,
                    DPSResult.vmeas("Vm_VCORE_0p8").getVoltage("VCORE_0p8").getElement(1 + i * 2));
        }

        if (StaticFields.debugMode) {
            String testSuiteName_Qualified = context.getTestSuiteName();
            String testSuiteName = testSuiteName_Qualified
                    .substring(1 + testSuiteName_Qualified.lastIndexOf("."));
            println("**********" + testSuiteName + "**********");
            wave_I_VBAT.getElement(0).plot("I_VBAT_sleep_1");
            wave_I_VBAT.getElement(1).plot("I_VBAT_sleep_2");

            println("I_VBAT_wakeup_1 [mA] = " + I_VBAT_wakeup.getElement(0).multiply(1e3));
            println("V_VCore_wakeup_1[V]  = " + V_VCore_wakeup.getElement(0));
            println("I_VBAT_sleep_1  [mA] = " + I_VBAT_sleep.getElement(0).multiply(1e3));
            println("V_VCore_sleep_1 [V]  = " + V_VCore_sleep.getElement(0));
            println();

            println("I_VBAT_wakeup_2[mA] = " + I_VBAT_wakeup.getElement(1).multiply(1e3));
            println("V_VCore_wakeup_2[V] = " + V_VCore_wakeup.getElement(1));
            println("I_VBAT_sleep_2[mA] = " + I_VBAT_sleep.getElement(1).multiply(1e3));
            println("V_VCore_sleep_2[V] = " + V_VCore_sleep.getElement(1));
            println();

        }

        ptd_I_VBAT_wakeup_1.evaluate(I_VBAT_wakeup.getElement(0));
        ptd_V_VCore_wakeup_1.evaluate(V_VCore_wakeup.getElement(0));
        ptd_I_VBAT_sleep_1.evaluate(I_VBAT_sleep.getElement(0));
        ptd_V_VCore_sleep_1.evaluate(V_VCore_sleep.getElement(0));
        ptd_I_VBAT_wakeup_2.evaluate(I_VBAT_wakeup.getElement(1));
        ptd_V_VCore_wakeup_2.evaluate(V_VCore_wakeup.getElement(1));
        ptd_I_VBAT_sleep_2.evaluate(I_VBAT_sleep.getElement(1));
        ptd_V_VCore_sleep_2.evaluate(V_VCore_sleep.getElement(1));

    }
}
