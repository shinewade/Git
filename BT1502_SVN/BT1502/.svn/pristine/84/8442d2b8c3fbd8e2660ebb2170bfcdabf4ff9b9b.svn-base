package BT1502_pro_tml.FUNC;

import BT1502_pro_tml.Global.StaticFields;
import besLib.cal.BesCalc_PLL;
import besLib.cal.BesCalc_PLL.freqCalMode;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.dsp.MultiSiteWaveLong;
import xoc.dta.measurement.ILocalMeasurement;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigInOutCaptureResults;
import xoc.dta.resultaccess.datatypes.BitSequence.BitOrder;
import xoc.dta.testdescriptor.IParametricTestDescriptor;


public class LPO_freq_24M_on_test extends TestMethod {

    @In public String spec_measurement;
    @In public String pattern1;
    public IParametricTestDescriptor ptd_24M_On;

    public IMeasurement measurement1;
    public ILocalMeasurement measurement2;

    @Override
    public void setup()
    {
        IDeviceSetup deviceSetup1 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup1.importSpec(spec_measurement);

        BesPA_I2C i2c=new BesPA_I2C(deviceSetup1, measurement1, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        i2c.transactionSequenceBegin("LPO_FREQ_24M_ON_TEST");
//      [aons24m_clkout_p13]
            i2c.write(BesI2cAddrType.DIGITAL,0x40086008,0xFFFF3FFF);
            i2c.write(BesI2cAddrType.DIGITAL,0x4008001c,0x101b);
    //        // config 1
    //        i2c.write(BesI2cAddrType.RF,0x00,0xa010);//page1
    //        i2c.write(BesI2cAddrType.RF,0x07,0x4000);
    //        i2c.write(BesI2cAddrType.RF,0x00,0xa000);//page0
    //        i2c.write(BesI2cAddrType.RF,0x4d,0x003f);
    //        i2c.write(BesI2cAddrType.RF,0x00,0xa010);//page1
    //        i2c.write(BesI2cAddrType.RF,0x0a,0x4222);
    //        i2c.write(BesI2cAddrType.RF,0x0e,0x0007);
    //        i2c.write(BesI2cAddrType.RF,0x00,0xa000);//page0
    //        //[pd]
            i2c.write(BesI2cAddrType.RF,0x00,0xa010);//page1
            i2c.write(BesI2cAddrType.RF,0x0d,0x0082);
            i2c.write(BesI2cAddrType.RF,0x00,0xa000);//page0
            //[power on]
            i2c.write(BesI2cAddrType.RF,0x00,0xa010);//page1
            i2c.write(BesI2cAddrType.RF,0x0d,0x0182);
            i2c.write(BesI2cAddrType.RF,0x00,0xa000);//page0
    //
    //        // config 2
    //        i2c.write(BesI2cAddrType.RF,0x00,0xa010);//page1
    //        i2c.write(BesI2cAddrType.RF,0x07,0x2000);
    //        i2c.write(BesI2cAddrType.RF,0x00,0xa000);//page0
    //        i2c.write(BesI2cAddrType.RF,0x4d,0x001f);
    //        i2c.write(BesI2cAddrType.RF,0x00,0xa010);//page1
    //        i2c.write(BesI2cAddrType.RF,0x0a,0x319a);
    //        i2c.write(BesI2cAddrType.RF,0x0e,0x0001);
    //        i2c.write(BesI2cAddrType.RF,0x00,0xa000);//page0
    //        //[pd]s
    //        i2c.write(BesI2cAddrType.RF,0x00,0xa010);//page1
    //        i2c.write(BesI2cAddrType.RF,0x0d,0x0082);
    //        i2c.write(BesI2cAddrType.RF,0x00,0xa000);//page0
    //        //[power on]
    //        i2c.write(BesI2cAddrType.RF,0x00,0xa010);//page1
    //        i2c.write(BesI2cAddrType.RF,0x0d,0x0182);
    //        i2c.write(BesI2cAddrType.RF,0x00,0xa000);//page0
        i2c.transactionSequenceEnd();
        measurement1.setSetups(deviceSetup1);



        IDeviceSetup deviceSetup2 =DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup2.importSpec(spec_measurement);

        pattern1 = "vectors.pattern_fix.Cap_MCLK_GPIO13_freq";

        deviceSetup2.sequentialBegin();
            deviceSetup2.patternCall(pattern1);
        deviceSetup2.sequentialEnd();
        measurement2.setSetups(deviceSetup2);

    }

    @Override
    public void update ()
    {
        measurement2.spec().setVariable("Freq_MHz_eqn12", 25*1e6);
        measurement2.digInOut().result().capture().setEnabled(true);
        measurement2.digInOut().result().callPassFail().setEnabled(false);
    }

    @Override
    public void execute ()
    {
        measurement1.execute();

        measurement2.execute();
        IDigInOutCaptureResults result_Cap=measurement2.digInOut().preserveCaptureResults();
        MultiSiteWaveLong rawData=result_Cap.getSerialBitsAsWaveLong("GPIO_13", 1, BitOrder.LEFT_TO_RIGHT);
        int[] activeSites = context.getActiveSites();
        double Fs=measurement2.spec().getDouble("Freq_MHz_eqn12").get(activeSites[0]);
        //FFT
        MultiSiteDouble freq= new BesCalc_PLL().freqCal(rawData, 1/(Fs*8)*1e6, freqCalMode.FFT);//MHz
        ptd_24M_On.evaluate(freq.multiply(1e6));
        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            rawData.plot("LPO_Freq_24M_on");

            println("**********"+testSuiteName+"**********");
            println("freq_24M_on_GPIO_13 = "+freq+" MHz");
            println();
        }
    }
}
