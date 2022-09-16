package BT1502_pro_tml.DC;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDcVI;
import xoc.dsa.ISetupDcVI.SetupConnectMode;
import xoc.dsa.ISetupDcVI.SetupDisconnectMode;
import xoc.dta.TestMethod;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDcVIResults;


public class LDO_meas extends TestMethod {

    public IMeasurement measurement;

    @Override
    public void setup() {
        //measurement
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);

        //VCORE_0p6_VM
        ISetupDcVI dcVi_VCORE_0p6_VM=deviceSetup.addDcVI("VCORE_0p6").setConnectMode(SetupConnectMode.highImpedance).setDisconnect(true).setDisconnectMode(SetupDisconnectMode.hiz);
        dcVi_VCORE_0p6_VM.vmeas("Vm_VCORE_0p6").setWaitTime("2 ms").setAverages(4);

        //VCORE_0p8_VM
        ISetupDcVI dcVi_VCORE_0p8_VM=deviceSetup.addDcVI("VCORE_0p8").setConnectMode(SetupConnectMode.highImpedance).setDisconnect(true).setDisconnectMode(SetupDisconnectMode.hiz);
        dcVi_VCORE_0p8_VM.vmeas("Vm_VCORE_0p8").setWaitTime("2 ms").setAverages(4);

        //VCORE_0p8_1_VM
        ISetupDcVI dcVi_VCORE_0p8_1_VM=deviceSetup.addDcVI("VCORE_0p8").setConnectMode(SetupConnectMode.highImpedance).setDisconnect(true).setDisconnectMode(SetupDisconnectMode.hiz);
        dcVi_VCORE_0p8_1_VM.vmeas("Vm_VCORE_0p8_1").setWaitTime("2 ms").setAverages(4);

        //VANA_VM
        ISetupDcVI dcVi_VANA_VM=deviceSetup.addDcVI("VANA").setConnectMode(SetupConnectMode.highImpedance).setDisconnect(true).setDisconnectMode(SetupDisconnectMode.hiz);
        dcVi_VANA_VM.vmeas("Vm_VANA").setWaitTime("1 ms").setAverages(4);

        //VCODEC_VM
        ISetupDcVI dcVi_VCODEC_VM=deviceSetup.addDcVI("VCODEC").setConnectMode(SetupConnectMode.highImpedance).setDisconnect(true).setDisconnectMode(SetupDisconnectMode.hiz);
        dcVi_VCODEC_VM.vmeas("Vm_VCODEC").setWaitTime("10 ms").setAverages(4);

        //VIO_VM
        ISetupDcVI dcVi_VIO_VM=deviceSetup.addDcVI("VIO").setConnectMode(SetupConnectMode.highImpedance).setDisconnect(true).setDisconnectMode(SetupDisconnectMode.hiz);
        dcVi_VIO_VM.vmeas("Vm_VIO").setWaitTime("1 ms").setAverages(4);

        deviceSetup.sequentialBegin();
            deviceSetup.actionCall("Vm_VCORE_0p6");
            deviceSetup.actionCall("Vm_VCORE_0p8");
            deviceSetup.actionCall("Vm_VCORE_0p8_1");
            deviceSetup.actionCall("Vm_VANA");
            deviceSetup.actionCall("Vm_VCODEC");
            deviceSetup.actionCall("Vm_VIO");
        deviceSetup.sequentialEnd();
        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute() {
        measurement.execute();
        IDcVIResults DPSResult1=measurement.dcVI().preserveResults();
        MultiSiteDouble ldo_vcore_0p6_nold=DPSResult1.vmeas("Vm_VCORE_0p6").getVoltage("VCORE_0p6").getElement(0);
//        MultiSiteDouble ldo_vcore_0p8_nold=DPSResult1.vmeas("Vm_VCORE_0p8").getVoltage("VCORE_0p8").getElement(0);
        MultiSiteDouble ldo_vcore_0p8_1_nold=DPSResult1.vmeas("Vm_VCORE_0p8_1").getVoltage("VCORE_0p8").getElement(0);
        MultiSiteDouble ldo_vana_nold=DPSResult1.vmeas("Vm_VANA").getVoltage("VANA").getElement(0);
        MultiSiteDouble ldo_vio_nold=DPSResult1.vmeas("Vm_VIO").getVoltage("VIO").getElement(0);
        MultiSiteDouble ldo_vcodec_nold=DPSResult1.vmeas("Vm_VCODEC").getVoltage("VCODEC").getElement(0);

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("LDO_VCORE_0P6_NOLOAD  [V] = "+ldo_vcore_0p6_nold);
            println();

//            println("LDO_VCORE_0P8_NOLOAD  [V] = "+ldo_vcore_0p8_nold);
            println();

            println("LDO_VCORE_0P8_1_NOLOAD  [V] = "+ldo_vcore_0p8_1_nold);
            println();

            println("LDO_VANA_NOLOAD  [V] = "+ldo_vana_nold);
            println();

            println("LDO_VCODEC_NOLOAD [V] = "+ldo_vcodec_nold);
            println();

            println("LDO_VIO_NOLOAD [V] = "+ldo_vio_nold);
            println();
        }
    }
}
