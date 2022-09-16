package BT1502_pro_tml.FUNC;

import BT1502_pro_tml.Global.StaticFields;
import BT1502_pro_tml.MEMORY.uart_dyn;
import besLib.dsa.BesDsa_UART;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.workspace.IWorkspace;


public class UART_INIT extends TestMethod {

    @In public String spec_measurement;
    @In public String spec_measurement_UART;

    public IMeasurement measurement;

    public uart_dyn dyn = new uart_dyn();

    @Override
    public void setup ()
    {
        IDeviceSetup deviceSetup = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement_UART);

        context.workspace();
        new BesDsa_UART(deviceSetup).writeToRAM_NewPA(measurement,IWorkspace.getActiveProjectPath()+ "/tml/BT1502_pro_tml/MEMORY/ramrun_bin_file/1502_ramrun_open_i2c.bin");
        deviceSetup.waitCall("10 ms");
        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute ()
    {
        measurement.execute();
    }
}
