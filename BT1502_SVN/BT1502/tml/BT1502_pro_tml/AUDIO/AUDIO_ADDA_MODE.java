package BT1502_pro_tml.AUDIO;

import BT1502_pro_tml.Global.StaticFields;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.measurement.ILocalMeasurement;


public class AUDIO_ADDA_MODE extends TestMethod {

    @In public String spec_measurement;

    @In public String modeMux = "Nor";//"Nor"   : normal mode vcodec 1.8V
                                      //"NorLV" : normal mode vcodec 1.4V
                                      //"HP"    : high performance mode vcodec 1.8V

    public ILocalMeasurement measurement;

    @Override
    public void setup() {
        IDeviceSetup deviceSetup=DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement);

        BesPA_I2C i2c=new BesPA_I2C(deviceSetup, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");
        //[ANA ft ]
//      [codec adda]
        i2c.transactionSequenceBegin("AUDIO_ADDA_MODE");
//        ISetupProtocolInterface paInterface = deviceSetup.addProtocolInterface("I2C_BES", "besLib.pa.I2C_8bit_BES");
//        paInterface.addSignalRole("DATA", "I2C_SDA");
//        paInterface.addSignalRole("CLK", "I2C_SCL");
//        ISetupTransactionSeqDef transDigSrc= paInterface.addTransactionSequenceDef("AUDIO_ADDA_MODE");

      //[DAC 1k close]
        i2c.write(BesI2cAddrType.DIGITAL,0x403000b0,0x010A0207);
            i2c.waitTime(1e6);
            //[ADC-DAC LOOP]
            i2c.write(BesI2cAddrType.DIGITAL,0x40300080,0x00021f3f);//0x00221f3f 0x00021f3f
            i2c.write(BesI2cAddrType.DIGITAL,0x40300084,0x00400020);//ana adcA -> dig adc_ch0
            i2c.write(BesI2cAddrType.DIGITAL,0x40300088,0x00400022);//ana adcB -> dig adc_ch1
            i2c.write(BesI2cAddrType.DIGITAL,0x4030008c,0x00400024);//ana adcC -> dig adc_ch2
            i2c.write(BesI2cAddrType.DIGITAL,0x40300090,0x00400026);//ana adcD -> dig adc_ch3
            i2c.write(BesI2cAddrType.DIGITAL,0x40300094,0x00400028);//dig adc_ch4
            i2c.write(BesI2cAddrType.DIGITAL,0x403000b8,0x00004000);
            i2c.write(BesI2cAddrType.DIGITAL,0x403000b4,0x00000000);
            i2c.write(BesI2cAddrType.DIGITAL,0x403000b4,0x07E04000);//set adc/dac gain
            i2c.write(BesI2cAddrType.DIGITAL,0x403000b0,0x010A0407);//dac en 0x010E0407
            i2c.waitTime(1e6);

            //[adcA open new]
            i2c.write(BesI2cAddrType.ANA,0x61,0x1021);
            i2c.write(BesI2cAddrType.ANA,0x62,0x7ff8);
            i2c.write(BesI2cAddrType.ANA,0x63,0x601f);//0x801f ==> 0x881f 20211223  //601F by zhengda
            i2c.write(BesI2cAddrType.ANA,0x64,0x8888);//0x88c8 ==> 0x8888 20220110 by lizhengda
            i2c.write(BesI2cAddrType.ANA,0x65,0x8428);
            i2c.write(BesI2cAddrType.ANA,0x66,0x4084);
            i2c.write(BesI2cAddrType.ANA,0x67,0xe000); //a000 ==> e000
            i2c.write(BesI2cAddrType.ANA,0x68,0x1087);//0x1086 ==> 0x1087 20210720
            i2c.write(BesI2cAddrType.ANA,0x69,0x1144);
            //[adcB open new]
            i2c.write(BesI2cAddrType.ANA,0x71,0x1021);
            i2c.write(BesI2cAddrType.ANA,0x72,0x7ff8);
            i2c.write(BesI2cAddrType.ANA,0x73,0x601f);
            i2c.write(BesI2cAddrType.ANA,0x74,0x8888);
            i2c.write(BesI2cAddrType.ANA,0x75,0x8428);
            i2c.write(BesI2cAddrType.ANA,0x76,0x4084);
            i2c.write(BesI2cAddrType.ANA,0x77,0xe000);
            i2c.write(BesI2cAddrType.ANA,0x78,0x1087);//0x1086 ==> 0x1087 20210720
            i2c.write(BesI2cAddrType.ANA,0x79,0x1144);
            //[adcC open new]
            i2c.write(BesI2cAddrType.ANA,0x81,0x1021);
            i2c.write(BesI2cAddrType.ANA,0x82,0x7ff8);
            i2c.write(BesI2cAddrType.ANA,0x83,0x601f);
            i2c.write(BesI2cAddrType.ANA,0x84,0x8888);
            i2c.write(BesI2cAddrType.ANA,0x85,0x8428);
            i2c.write(BesI2cAddrType.ANA,0x86,0x4084);
            i2c.write(BesI2cAddrType.ANA,0x87,0xe000);
            i2c.write(BesI2cAddrType.ANA,0x88,0x1087);//0x1086 ==> 0x1087 20210720
            i2c.write(BesI2cAddrType.ANA,0x89,0x1144);
            //[adcD open new]
            i2c.write(BesI2cAddrType.ANA,0xC0,0x1021);//0x91
            i2c.write(BesI2cAddrType.ANA,0xC1,0x7ff8);
            i2c.write(BesI2cAddrType.ANA,0xC2,0x601f);
            i2c.write(BesI2cAddrType.ANA,0xC3,0x8888);
            i2c.write(BesI2cAddrType.ANA,0xC4,0x8428);
            i2c.write(BesI2cAddrType.ANA,0xC5,0x4084);
            i2c.write(BesI2cAddrType.ANA,0xC6,0xe000);
            i2c.write(BesI2cAddrType.ANA,0xC7,0x1087);//0x1086 ==> 0x1087 20210720
            i2c.write(BesI2cAddrType.ANA,0xC8,0x1144);
            //[adcE open new]
            i2c.write(BesI2cAddrType.ANA,0xb0,0x6000);
            i2c.write(BesI2cAddrType.ANA,0xa1,0x1021);
            i2c.write(BesI2cAddrType.ANA,0xa2,0xfff0);
            i2c.write(BesI2cAddrType.ANA,0xa3,0x601f);
            i2c.write(BesI2cAddrType.ANA,0xa4,0x8888);
            i2c.write(BesI2cAddrType.ANA,0xa5,0x8428);
            i2c.write(BesI2cAddrType.ANA,0xa6,0x4084);
            i2c.write(BesI2cAddrType.ANA,0xa7,0xe000);
            i2c.write(BesI2cAddrType.ANA,0xa8,0x1087);
            i2c.write(BesI2cAddrType.ANA,0xa9,0x1144);
            i2c.waitTime(150e6);
        i2c.transactionSequenceEnd();

        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute() {
        measurement.execute();

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println();
        }
    }
}
