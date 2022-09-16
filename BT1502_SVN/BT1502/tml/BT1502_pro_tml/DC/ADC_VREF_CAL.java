package BT1502_pro_tml.DC;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDigInOut;
import xoc.dsa.ISetupProtocolInterface;
import xoc.dsa.ISetupTransactionSeqDef;
import xoc.dsa.ISetupTransactionSeqDef.Direction;
import xoc.dsa.ISetupTransactionSeqDef.Type;
import xoc.dsa.SetupVariableType;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigInOutActionResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class ADC_VREF_CAL extends TestMethod {
    @In public String spec_measurement;

    public IMeasurement measurement;
    public IMeasurement measurement1;

    public IParametricTestDescriptor    ptd_cal_shift_ADC_VREF ,
                                        ptd_vol_before_ADC_VREF,
                                        ptd_vol_after_ADC_VREF ;

    private MultiSiteLong initData=new MultiSiteLong(0x1808);

    @Override
    public void setup() {
        //measurement
        IDeviceSetup deviceSetup =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement);

        ISetupProtocolInterface paInterface = deviceSetup.addProtocolInterface("I2C_BES", "besLib.pa.I2C_8bit_BES");
        paInterface.addSignalRole("DATA", "I2C_SDA");
        paInterface.addSignalRole("CLK", "I2C_SCL");
        ISetupTransactionSeqDef transDigSrc= paInterface.addTransactionSequenceDef("ADC_VREF_CAL_01");
        transDigSrc.addTransaction("PMU_WRITE",0x00,0xa010);
        transDigSrc.addTransaction("PMU_WRITE",0x01,0xfa20);//0xfa00
        transDigSrc.addTransaction("PMU_WRITE",0x00,0xa000);
        if(StaticFields.pmic_name == "BT1805")
        {
            transDigSrc.addTransaction("PMU_WRITE",0x25,0x1800);
            transDigSrc.addTransaction("PMU_WRITE",0x43,0x080c);//0x80c
        }
        if(StaticFields.pmic_name == "BT1806")
        {
            transDigSrc.addTransaction("PMU_WRITE",0x25,0x1808);//0x8
        }
        transDigSrc.addWait("5 ms");

        deviceSetup.sequentialBegin();
            deviceSetup.transactionSequenceCall(transDigSrc);
        deviceSetup.sequentialEnd();
        measurement.setSetups(deviceSetup);



        //measurement1
        IDeviceSetup deviceSetup1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup1.importSpec(spec_measurement);
        deviceSetup1.addVariable(SetupVariableType.UnsignedLong,"spec_data_adc_vref_cal",0);

        ISetupProtocolInterface paInterface1 = deviceSetup1.addProtocolInterface("I2C_BES", "besLib.pa.I2C_8bit_BES");
        paInterface1.addSignalRole("DATA", "I2C_SDA");
        paInterface1.addSignalRole("CLK", "I2C_SCL");
        ISetupTransactionSeqDef transDigSrc1= paInterface1.addTransactionSequenceDef("ADC_VREF_CAL_02");
        transDigSrc1.addParameter(Type.UnsignedLong, Direction.IN, "trSqe_data_adc_vref_cal");
        if(StaticFields.pmic_name == "BT1805")
        {
            transDigSrc1.addTransaction("PMU_WRITE",0x43,"trSqe_data_adc_vref_cal");
        }
        if(StaticFields.pmic_name == "BT1806")
        {
            transDigSrc1.addTransaction("PMU_WRITE",0x25,"trSqe_data_adc_vref_cal");
        }
        transDigSrc1.addWait("5 ms");

        ISetupDigInOut digIO_ADC_VREF=deviceSetup1.addDigInOut("ADC_VREF").setDisconnect(true);
        digIO_ADC_VREF.protection().setDisconnectPulldownState(false);
        digIO_ADC_VREF.frontendConnection("disconnect_AC_ADC_VREF").setDigitalFrontendDisable();
        digIO_ADC_VREF.iforce("If_ADC_VREF").setForceValue("0 uA").setIrange("2 uA").setVclampLow("0.6 V").setVclampHigh("2 V");
        digIO_ADC_VREF.vmeas("Vm_ADC_VREF").setHighAccuracy(true).setAverages(128).setWaitTime("10 ms");

        deviceSetup1.sequentialBegin();
            deviceSetup1.transactionSequenceCall(transDigSrc1,"trSqe_data_adc_vref_cal=spec_data_adc_vref_cal");
            deviceSetup1.actionCall("If_ADC_VREF");
            deviceSetup1.actionCall("Vm_ADC_VREF");
        deviceSetup1.sequentialEnd();
        measurement1.setSetups(deviceSetup1);
    }

    @Override
    public void execute() {

        if(StaticFields.pmic_name == "BT1805")
        {
            initData.set(0x080c);
        }
        if(StaticFields.pmic_name == "BT1806")
        {
            initData.set(0x1808);
        }

        measurement.execute();

        measurement1.spec().setVariable("spec_data_adc_vref_cal", initData);
        measurement1.execute();
        IDigInOutActionResults digIORes1=measurement1.digInOut().preserveActionResults();
        MultiSiteDouble adc_vref_cal_pre = digIORes1.vmeas("Vm_ADC_VREF").getVoltage("ADC_VREF").getElement(0);

        MultiSiteDouble target_vol_adc_vref = new MultiSiteDouble(1.8);
        MultiSiteLong step_cnt_adc_vref = new MultiSiteLong();
        for(int site:context.getActiveSites())
        {
            if(StaticFields.pmic_name == "BT1805")
            {
                target_vol_adc_vref.set(1.8);
                step_cnt_adc_vref.set(site, Math.round(target_vol_adc_vref.subtract(adc_vref_cal_pre).divide(0.005).get(site)));
                step_cnt_adc_vref.set(site,step_cnt_adc_vref.get(site) < -32 ? -32:step_cnt_adc_vref.get(site));
                step_cnt_adc_vref.set(site,step_cnt_adc_vref.get(site) >  31 ?  31:step_cnt_adc_vref.get(site));
            }
            if(StaticFields.pmic_name == "BT1806")
            {
                target_vol_adc_vref.set(1.75);
                step_cnt_adc_vref.set(site, Math.round(target_vol_adc_vref.subtract(adc_vref_cal_pre).divide(0.015).get(site)));
                step_cnt_adc_vref.set(site,step_cnt_adc_vref.get(site) < -8 ? -8:step_cnt_adc_vref.get(site));
                step_cnt_adc_vref.set(site,step_cnt_adc_vref.get(site) >  7 ?  7:step_cnt_adc_vref.get(site));
            }
        }

        if(StaticFields.pmic_name == "BT1805")
        {
            measurement1.spec().setVariable("spec_data_adc_vref_cal", initData.add(step_cnt_adc_vref.leftShift(6)));//bit6~bit11
        }
        if(StaticFields.pmic_name == "BT1806")
        {
            measurement1.spec().setVariable("spec_data_adc_vref_cal", initData.add(step_cnt_adc_vref));//bit0~bit3
        }
        measurement1.execute();
        IDigInOutActionResults digIORes2=measurement1.digInOut().preserveActionResults();
        MultiSiteDouble adc_vref_cal_post = digIORes2.vmeas("Vm_ADC_VREF").getVoltage("ADC_VREF").getElement(0);

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("adc_vref_cal_pre  [V] = "+adc_vref_cal_pre);
            println("adc_vref_cal_post [V] = "+adc_vref_cal_post);
            println("delt_voltage             [mV] = "+adc_vref_cal_pre.subtract(adc_vref_cal_post).multiply(1e3).abs());
            println("step_cnt_adc_vref  = "+step_cnt_adc_vref);
            println();
            if(StaticFields.pmic_name == "BT1805")
            {
                for(int site:context.getActiveSites())
                {
                    println("site"+site+": data_value = 0x"+Long.toHexString(step_cnt_adc_vref.add(0x080c).get(site)));
                }
            }
            if(StaticFields.pmic_name == "BT1806")
            {
                for(int site:context.getActiveSites())
                {
                    println("site"+site+": data_value = 0x"+Long.toHexString(step_cnt_adc_vref.add(0x1808).get(site)));
                }
            }
            println();
        }

        ptd_cal_shift_ADC_VREF .evaluate(step_cnt_adc_vref);
        ptd_vol_before_ADC_VREF.evaluate(adc_vref_cal_pre);
        ptd_vol_after_ADC_VREF .evaluate(adc_vref_cal_post);
    }
}
