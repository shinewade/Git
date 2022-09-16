package BT1502_pro_tml.FUNC;

import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import BT1502_pro_tml.Global.StaticFields;
import besLib.cal.BesCalc_DFT;
import besLib.dsa.BesDsa_DFT;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.testdescriptor.IFunctionalTestDescriptor;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class BIST_TM extends TestMethod {

    @In public String spec_measurement;
    @In public String spec_measurement1;
    @In public String ComparePinList;
    @In public double period_ns;
    @In public double strobe_ns;

    public IMeasurement measurement;
    public IMeasurement measurement1;

    public IParametricTestDescriptor ptd_BIST;
    public IFunctionalTestDescriptor ftd;


    @Override
    public void setup() {
        /************ measurement_Mode ************/
        IDeviceSetup ds_Mode = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds_Mode.importSpec(spec_measurement);

        BesPA_I2C i2c=new BesPA_I2C(ds_Mode, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        //operating sequence
        i2c.transactionSequenceBegin("scan_Mode");
            i2c.write(BesI2cAddrType.PMUIntern, 0x14,0x4e79);//0x4000 ldo, 0x0e79 dcdc
            i2c.read(BesI2cAddrType.PMUIntern, 0x14, "readValue_0x14");
        i2c.transactionSequenceEnd();
        ds_Mode.waitCall("100 ms");
        measurement.setSetups(ds_Mode);


        /********************* measurement1 ***********************/
        String pattern1="vectors.pattern_dft.bt1502x_verA_20220811_prod_MemoryBist_tck_rev0p1_ascii";
        BesDsa_DFT besDsa_DFT1=new BesDsa_DFT(StaticFields.prog_name);
        //pattern run
        IDeviceSetup ds1=besDsa_DFT1.patternRun(pattern1, measurement1, spec_measurement1);
        //pattern run result configuration
        besDsa_DFT1.configPatternRun(ds1, ComparePinList, 60344336);

    }

    @Override
    public void execute() {


        measurement.execute();
        com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults resPA=ProtocolAccess.preserveResults(measurement);
        MultiSiteLong readValue_0x14 = resPA.getResult("readValue_0x14");

        measurement1.spec().setVariable("Period_ns_DFT", period_ns*1e-9);
        measurement1.spec().setVariable("Stb_ns_DFT", strobe_ns*1e-9);
        measurement1.execute();
        BesCalc_DFT res1=new BesCalc_DFT(measurement1, ftd);
        MultiSiteLong FunResult1=res1.getOverallResult();
//        res1.writeFailLog2Text(ComparePinList, 10000,"failLog_Scan_Stuck");

        if (StaticFields.debugMode) {
            String testSuiteName_Qualified = context.getTestSuiteName();
            String testSuiteName = testSuiteName_Qualified.substring(1 + testSuiteName_Qualified.lastIndexOf("."));
            println("**********" + testSuiteName + "**********");
            for (int site : context.getActiveSites()) {
                println("reg_0x14= 0x" + Long.toHexString(readValue_0x14.get(site)) + " [site " + site+ "]");
            }
            println("FunResult1 = " + FunResult1);
            println();
        }

        ptd_BIST.evaluate(FunResult1);


    }
}
