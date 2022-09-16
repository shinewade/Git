
package BT1502_pro_tml.DC;

import com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults;
import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.BesDsa_PS1600_Disconnect;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDigInOut;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class GPIO_TEST_INPUT_GANG extends TestMethod {

    @In
    public String spec_measurement;
    public IMeasurement measurement;
    public IParametricTestDescriptor ptd_GPIO_Input_H1, ptd_GPIO_Input_H2, ptd_GPIO_Input_L1, ptd_GPIO_Input_L2,
                                     ptd_GPIO_Input_H11, ptd_GPIO_Input_H12, ptd_GPIO_Input_L11, ptd_GPIO_Input_L12,
                                     ptd_GPIO_Input_H21, ptd_GPIO_Input_H22, ptd_GPIO_Input_L21, ptd_GPIO_Input_L22;

    private String activePin_GPIO;

    // set compared GPIO pins
    private short activePin_P0 = 0b11111111, // 8 bits, [ P0.7 - P0.0 ] -> [ GPIO_07 - GPIO_00 ]
            activePin_P1 = 0b11111111, // 8 bits [ P1.7 - P1.0 ] -> [ GPIO_17 - GPIO_10 ]
            activePin_P2 = 0b11110011, // 8 bits [ P2.7 - P2.0 ] -> [ GPIO_27 - GPIO_20 ]
            activePin_P3 = 0b11111111, // 8 bits [ P3.7 - P3.0 ] -> [ GPIO_37 - GPIO_30 ]
            activePin_P4 = 0b11111111, // 8 bits [ P1.7 - P1.0 ] -> [ GPIO_17 - GPIO_10 ]
            activePin_P5 = 0b11111111, // 8 bits [ P2.7 - P2.0 ] -> [ GPIO_27 - GPIO_20 ]
            activePin_P6 = 0b11111111, // 8 bits [ P3.7 - P3.0 ] -> [ GPIO_37 - GPIO_30 ]
            activePin_P7 = 0b11111111, // 8 bits [ P1.7 - P1.0 ] -> [ GPIO_17 - GPIO_10 ]
            activePin_P8 = 0b11111111, // 8 bits [ P2.7 - P2.0 ] -> [ GPIO_27 - GPIO_20 ]
            activePin_P9 = 0b11111111; // 8 bits [ P3.7 - P3.0 ] -> [ GPIO_37 - GPIO_30 ]
    private Long expectData1;
    private Long expectData2;
    private Long expectData3;

    @Override
    public void setup() {
        IDeviceSetup ds1 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        activePin_GPIO = new BesDsa_PS1600_Disconnect(ds1).getPinGroup(context, spec_measurement,"GPIO_GRP");

        // GPIO
        ISetupDigInOut digInOut_GPIO = ds1.addDigInOut(activePin_GPIO).setConnect(true).setDisconnect(true);
        digInOut_GPIO.protection().setDisconnectPulldownState(false);
        digInOut_GPIO.vforce("Vf_GPIO_0V").setForceValue("0 V").setIclamp("40 mA");
        digInOut_GPIO.vforce("Vf_GPIO_1p8V").setForceValue("2 V").setIclamp("40 mA");

        BesPA_I2C i2c = new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_16Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

        i2c.transactionSequenceBegin("GPIO_Input_Mode");

        if (StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086004, 0xffffffff); // GPIO 00-07 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086008, 0xffffffff); // GPIO 10-17
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008600c, 0xffff00ff); // GPIO 20-27 except I2C_SCL
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086010, 0xffffffff); // GPIO 30-37 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008607C, 0xffffffff); // GPIO 40-47 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086080, 0xffffffff); // GPIO 50-57 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086084, 0xffffffff); // GPIO 60-67 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086088, 0xffffffff); // GPIO 70-77 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008608C, 0xffffffff); // GPIO 80-87 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086090, 0xffffffff); // GPIO 90-97 0721 wuhan
            i2c.waitTime(1e6);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40081004, 0x00000000);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40089004, 0x00000000);
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008A004, 0x00000000);// ask xinhua

        } else if (StaticFields.dev_name == "WLCSP") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086004, 0xffffffff); // GPIO 00-07 0721 wuhan
            i2c.write(BesI2cAddrType.DIGITAL, 0x40086008, 0xffffffff); // GPIO 10-17
            i2c.write(BesI2cAddrType.DIGITAL, 0x4008600c, 0xffff00ff); // GPIO 20-27 except I2C_SCL
            i2c.waitTime(1e6);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40081004, 0x00000000); // ask xinhua
        }
        i2c.waitTime(1e6);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4008602c, 0x00000000);// pu 0-3
        i2c.write(BesI2cAddrType.DIGITAL, 0x40086094, 0x00000000);// pu 4-7
        i2c.write(BesI2cAddrType.DIGITAL, 0x40086030, 0x00000000);// pd 0-3
        i2c.write(BesI2cAddrType.DIGITAL, 0x40086098, 0x00000000);// pd 4-7
        i2c.write(BesI2cAddrType.DIGITAL, 0x4008609C, 0x00000000);// pupd 89

        i2c.waitTime(1e6);

        i2c.transactionSequenceEnd();

        ds1.actionCall("Vf_GPIO_1p8V");
        i2c.transactionSequenceBegin("read_GPIO_State_H_1");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40081050, "readValue_0x40081050_H1");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40089050, "readValue_0x40089050_H1");
        i2c.read(BesI2cAddrType.DIGITAL, 0x4008a050, "readValue_0x4008a050_H1");
        i2c.transactionSequenceEnd();

        ds1.actionCall("Vf_GPIO_0V");
        i2c.transactionSequenceBegin("read_GPIO_State_L_1");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40081050, "readValue_0x40081050_L1");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40089050, "readValue_0x40089050_L1");
        i2c.read(BesI2cAddrType.DIGITAL, 0x4008a050, "readValue_0x4008a050_L1");
        i2c.transactionSequenceEnd();

        ds1.actionCall("Vf_GPIO_1p8V");
        // ds1.waitCall("5 ms");
        i2c.transactionSequenceBegin("read_GPIO_State_H_2");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40081050, "readValue_0x40081050_H2");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40089050, "readValue_0x40089050_H2");
        i2c.read(BesI2cAddrType.DIGITAL, 0x4008a050, "readValue_0x4008a050_H2");
        i2c.transactionSequenceEnd();

        ds1.actionCall("Vf_GPIO_0V");
        i2c.transactionSequenceBegin("read_GPIO_State_L_2");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40081050, "readValue_0x40081050_L2");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40089050, "readValue_0x40089050_L2");
        i2c.read(BesI2cAddrType.DIGITAL, 0x4008a050, "readValue_0x4008a050_L2");
        i2c.transactionSequenceEnd();

        measurement.setSetups(ds1);
    }

    @Override
    public void execute() {

        measurement.execute();

        // get register result
        IProtocolInterfaceResults res_PA = ProtocolAccess.preserveResults(measurement);
        MultiSiteLong readValue_0x40081050_H1 = res_PA.getResult("readValue_0x40081050_H1");
        MultiSiteLong readValue_0x40081050_L1 = res_PA.getResult("readValue_0x40081050_L1");
        MultiSiteLong readValue_0x40081050_H2 = res_PA.getResult("readValue_0x40081050_H2");
        MultiSiteLong readValue_0x40081050_L2 = res_PA.getResult("readValue_0x40081050_L2");
        MultiSiteLong readValue_0x40089050_H1 = res_PA.getResult("readValue_0x40089050_H1");
        MultiSiteLong readValue_0x40089050_L1 = res_PA.getResult("readValue_0x40089050_L1");
        MultiSiteLong readValue_0x40089050_H2 = res_PA.getResult("readValue_0x40089050_H2");
        MultiSiteLong readValue_0x40089050_L2 = res_PA.getResult("readValue_0x40089050_L2");
        MultiSiteLong readValue_0x4008a050_H1 = res_PA.getResult("readValue_0x4008a050_H1");
        MultiSiteLong readValue_0x4008a050_L1 = res_PA.getResult("readValue_0x4008a050_L1");
        MultiSiteLong readValue_0x4008a050_H2 = res_PA.getResult("readValue_0x4008a050_H2");
        MultiSiteLong readValue_0x4008a050_L2 = res_PA.getResult("readValue_0x4008a050_L2");


//        for (int site : context.getActiveSites()) {
//            System.out.println("site= " + site + " readValue_0x40081050_H1 Binary Data= "+ Long.toBinaryString(readValue_0x40081050_H1.get(site)));
//            System.out.println("site= " + site + " readValue_0x40089050_H1 Binary Data= "+ Long.toBinaryString(readValue_0x40089050_H1.get(site)));
//            System.out.println("site= " + site + " readValue_0x4008a050_H1 Binary Data= "+ Long.toBinaryString(readValue_0x4008a050_H1.get(site)));
//            System.out.println("site= " + site + " readValue_0x40081050_H2 Binary Data= "+ Long.toBinaryString(readValue_0x40081050_H2.get(site)));
//            System.out.println("site= " + site + " readValue_0x40089050_H2 Binary Data= "+ Long.toBinaryString(readValue_0x40089050_H2.get(site)));
//            System.out.println("site= " + site + " readValue_0x4008a050_H2 Binary Data= "+ Long.toBinaryString(readValue_0x4008a050_H2.get(site)));
//            println();
//        }

        // set compared GPIO pins
        if (StaticFields.dev_name == "WLCSP") {
            activePin_P0 = 0b11111111; // 8 bits [ P0.7 - P0.0 ] -> [ GPIO_07 - GPIO_00 ]
            activePin_P1 = 0b11111111; // 8 bits [ P1.7 - P1.0 ] -> [ GPIO_17 - GPIO_10 ]
            activePin_P2 = 0b00000011; // 8 bits [ P2.7 - P2.0 ] -> [ GPIO_27 - GPIO_20 ]
            activePin_P3 = 0b00000000; // 8 bits [ P3.7 - P3.0 ] -> [ GPIO_37 - GPIO_30 ]
            expectData1 = (activePin_P0 & 0xffL) | ((activePin_P1 & 0xffL) << 8) | ((activePin_P2 & 0xffL) << 16) | ((activePin_P3 & 0xffL) << 24);
        }
        else if (StaticFields.dev_name == "BGA7273") {
            activePin_P0 = 0b11111111; // 8 bits [ P0.7 - P0.0 ] -> [ GPIO_07 - GPIO_00 ]
            activePin_P1 = 0b11111111; // 8 bits [ P1.7 - P1.0 ] -> [ GPIO_17 - GPIO_10 ]
            activePin_P2 = 0b11110011; // 8 bits [ P2.7 - P2.0 ] -> [ GPIO_27 - GPIO_20 ]
            activePin_P3 = 0b11111111; // 8 bits [ P3.7 - P3.0 ] -> [ GPIO_37 - GPIO_30 ]
            activePin_P4 = 0b11111111; // 8 bits [ P1.7 - P1.0 ] -> [ GPIO_17 - GPIO_10 ]
            activePin_P5 = 0b11111111; // 8 bits [ P2.7 - P2.0 ] -> [ GPIO_27 - GPIO_20 ]
            activePin_P6 = 0b11111111; // 8 bits [ P3.7 - P3.0 ] -> [ GPIO_37 - GPIO_30 ]
            activePin_P7 = 0b11111111; // 8 bits [ P1.7 - P1.0 ] -> [ GPIO_17 - GPIO_10 ]
            activePin_P8 = 0b11111111; // 8 bits [ P2.7 - P2.0 ] -> [ GPIO_27 - GPIO_20 ]
            activePin_P9 = 0b11111111; // 8 bits [ P3.7 - P3.0 ] -> [ GPIO_37 - GPIO_30 ]
            expectData1 = (activePin_P0 & 0xffL) | ((activePin_P1 & 0xffL) << 8) | ((activePin_P2 & 0xffL) << 16) | ((activePin_P3 & 0xffL) << 24);// 00-37
            expectData2 = (activePin_P4 & 0xffL)  | ((activePin_P5 & 0xffL) << 8) | ((activePin_P6 & 0xffL) << 16) | ((activePin_P7 & 0xffL) << 24);// 40-77
            expectData3 = ((activePin_P8 & 0xffL) | ((activePin_P9 & 0xffL) << 8)) & 0x0000ffffL; // 80-97
        }

        MultiSiteLong GPIO_Input_H1 = new MultiSiteLong(0);
        MultiSiteLong GPIO_Input_L1 = new MultiSiteLong(0);
        MultiSiteLong GPIO_Input_H2 = new MultiSiteLong(0);
        MultiSiteLong GPIO_Input_L2 = new MultiSiteLong(0);
        MultiSiteLong GPIO_Input_H11 = new MultiSiteLong(0);
        MultiSiteLong GPIO_Input_L11 = new MultiSiteLong(0);
        MultiSiteLong GPIO_Input_H21 = new MultiSiteLong(0);
        MultiSiteLong GPIO_Input_L21 = new MultiSiteLong(0);
        MultiSiteLong GPIO_Input_H12 = new MultiSiteLong(0);
        MultiSiteLong GPIO_Input_L12 = new MultiSiteLong(0);
        MultiSiteLong GPIO_Input_H22 = new MultiSiteLong(0);
        MultiSiteLong GPIO_Input_L22 = new MultiSiteLong(0);

        //H1_P0P1P2P3 H2_P0P1P2P3
        //H1_P4P5P6P7
        //H1_P8P9

        if (StaticFields.dev_name == "WLCSP") {

            for (int site1 : context.getActiveSites()) {
                GPIO_Input_H11.set(site1, 1);
                GPIO_Input_L11.set(site1, 1);
                GPIO_Input_H21.set(site1, 1);
                GPIO_Input_L21.set(site1, 1);
                GPIO_Input_H12.set(site1, 1);
                GPIO_Input_L12.set(site1, 1);
                GPIO_Input_H22.set(site1, 1);
                GPIO_Input_L22.set(site1, 1);
                GPIO_Input_H1.set(site1,
                        (readValue_0x40081050_H1.and(expectData1).get(site1).longValue() == expectData1) ? 1
                                : -1);
                GPIO_Input_H2.set(site1,
                        (readValue_0x40081050_H2.and(expectData1).get(site1).longValue() == expectData1) ? 1
                                : -1);
                GPIO_Input_L1.set(site1,
                        (readValue_0x40081050_L1.and(expectData1).get(site1).longValue() == 0) ? 1 : -1);
                GPIO_Input_L2.set(site1,
                        (readValue_0x40081050_L2.and(expectData1).get(site1).longValue() == 0) ? 1 : -1);
            }
        } else if (StaticFields.dev_name == "BGA7273") {

            for (int site1 : context.getActiveSites()) {
                GPIO_Input_H1.set(site1,
                        (readValue_0x40081050_H1.and(expectData1).get(site1).longValue() == expectData1) ? 1:-1);
                GPIO_Input_H2.set(site1,
                        (readValue_0x40081050_H2.and(expectData1).get(site1).longValue() == expectData1) ? 1:-1);
                GPIO_Input_L1.set(site1,
                        (readValue_0x40081050_L1.and(expectData1).get(site1).longValue() == 0) ? 1 : -1);
                GPIO_Input_L2.set(site1,
                        (readValue_0x40081050_L2.and(expectData1).get(site1).longValue() == 0) ? 1 : -1); // 00-37

                GPIO_Input_H11.set(site1,
                        (readValue_0x40089050_H1.and(expectData2).get(site1).longValue() == expectData2) ? 1
                                : -1);
                GPIO_Input_H21.set(site1,
                        (readValue_0x40089050_H2.and(expectData2).get(site1).longValue() == expectData2) ? 1
                                : -1);
                GPIO_Input_L11.set(site1,
                        (readValue_0x40089050_L1.and(expectData2).get(site1).longValue() == 0) ? 1 : -1);
                GPIO_Input_L21.set(site1,
                        (readValue_0x40089050_L2.and(expectData2).get(site1).longValue() == 0) ? 1 : -1);

                GPIO_Input_H12.set(site1,(readValue_0x4008a050_H1.and(expectData3).get(site1).longValue() == expectData3) ? 1: -1);
                GPIO_Input_H22.set(site1,(readValue_0x4008a050_H2.and(expectData3).get(site1).longValue() == expectData3) ? 1:-1);
                GPIO_Input_L12.set(site1,
                        (readValue_0x4008a050_L1.and(expectData3).get(site1).longValue() == 0) ? 1 : -1);
                GPIO_Input_L22.set(site1,
                        (readValue_0x4008a050_L2.and(expectData3).get(site1).longValue() == 0) ? 1 : -1);
            }
        }

        if (StaticFields.debugMode)
        {
            String testSuiteName_Qualified = context.getTestSuiteName();
            String testSuiteName = testSuiteName_Qualified
                    .substring(1 + testSuiteName_Qualified.lastIndexOf("."));
            println("**********" + testSuiteName + "**********");
            for (int site1 : context.getActiveSites()) {
                System.out.println("site= " + site1 + " readValue_0x40081050_H1 Binary Data= "+ Long.toBinaryString(readValue_0x40081050_H1.get(site1)));
                System.out.println("site= " + site1 + " readValue_0x40089050_H1 Binary Data= "+ Long.toBinaryString(readValue_0x40089050_H1.get(site1)));
                System.out.println("site= " + site1 + " readValue_0x4008a050_H1 Binary Data= "+ Long.toBinaryString(readValue_0x4008a050_H1.get(site1)));
                System.out.println("site= " + site1 + " readValue_0x4008a050_L1 Binary Data= "+ Long.toBinaryString(readValue_0x4008a050_L1.get(site1)));
                System.out.println("site= " + site1 + " readValue_0x40081050_H2 Binary Data= "+ Long.toBinaryString(readValue_0x40081050_H2.get(site1)));
                System.out.println("site= " + site1 + " readValue_0x40081050_L2 Binary Data= "+ Long.toBinaryString(readValue_0x40081050_L2.get(site1)));
//                println();
//                System.out.println("site= " + site1 + " readValue_0x40081050_H1 Binary Data[7-0]= "+ Long.toBinaryString(readValue_0x40081050_H1.and(0xff).get(site1)));
//                System.out.println("site= " + site1 + " readValue_0x40081050_H1 Binary Data[15-8]= " + Long.toBinaryString((readValue_0x40081050_H1.and(0xff00).rightShift(8)).get(site1)));
//                System.out.println("site= " + site1 + " readValue_0x40081050_H1 Binary Data[23-16]= "
//                        + Long.toBinaryString(
//                                (readValue_0x40081050_H1.and(0xff0000).rightShift(16)).get(site1)));
//                System.out.println("site= " + site1 + " readValue_0x40081050_H1 Binary Data[31-24]= "
//                        + Long.toBinaryString(
//                                (readValue_0x40081050_H1.and(0xff000000).rightShift(24))
//                                        .get(site1)));
//                println();
//                System.out.println("site= " + site1 + " readValue_0x40081050_L1 Binary Data[7-0]= "
//                        + Long.toBinaryString(readValue_0x40081050_L1.and(0xff).get(site1)));
//                System.out.println("site= " + site1 + " readValue_0x40081050_L1 Binary Data[15-8]= "
//                        + Long.toBinaryString(
//                                readValue_0x40081050_L1.and(0xff00).rightShift(8).get(site1)));
//                System.out.println("site= " + site1 + " readValue_0x40081050_L1 Binary Data[23-16]= "
//                        + Long.toBinaryString(
//                                readValue_0x40081050_L1.and(0xff0000).rightShift(16).get(site1)));
//                System.out.println("site= " + site1 + " readValue_0x40081050_L1 Binary Data[31-24]= "
//                        + Long.toBinaryString(
//                                readValue_0x40081050_L1.and(0xff000000).rightShift(24).get(site1)));
//                println();
//                System.out.println("site= " + site1 + " readValue_0x40081050_H2 Binary Data[7-0]= "
//                        + Long.toBinaryString(readValue_0x40081050_H2.and(0xff).get(site1)));
//                System.out.println("site= " + site1 + " readValue_0x40081050_H2 Binary Data[15-8]= "
//                        + Long.toBinaryString(
//                                readValue_0x40081050_H2.and(0xff00).rightShift(8).get(site1)));
//                System.out.println("site= " + site1 + " readValue_0x40081050_H2 Binary Data[23-16]= "
//                        + Long.toBinaryString(
//                                readValue_0x40081050_H2.and(0xff0000).rightShift(16).get(site1)));
//                System.out.println("site= " + site1 + " readValue_0x40081050_H2 Binary Data[31-24]= "
//                        + Long.toBinaryString(
//                                readValue_0x40081050_H2.and(0xff000000).rightShift(24).get(site1)));
//                println();
//                System.out.println("site= " + site1 + " readValue_0x40081050_L2 Binary Data[7-0]= "
//                        + Long.toBinaryString(readValue_0x40081050_L2.and(0xff).get(site1)));
//                System.out.println("site= " + site1 + " readValue_0x40081050_L2 Binary Data[15-8]= "
//                        + Long.toBinaryString(
//                                readValue_0x40081050_L2.and(0xff00).rightShift(8).get(site1)));
//                System.out.println("site= " + site1 + " readValue_0x40081050_L2 Binary Data[23-16]= "
//                        + Long.toBinaryString(
//                                readValue_0x40081050_L2.and(0xff0000).rightShift(16).get(site1)));
//                System.out.println("site= " + site1 + " readValue_0x40081050_L2 Binary Data[31-24]= "
//                        + Long.toBinaryString(
//                                readValue_0x40081050_L2.and(0xff000000).rightShift(24).get(site1)));
            }
//            println("GPIO_Input_H1= " + GPIO_Input_H1);
//            println("GPIO_Input_L1= " + GPIO_Input_L1);
//            println("GPIO_Input_H2= " + GPIO_Input_H2);
//            println("GPIO_Input_L2= " + GPIO_Input_L2);

            // println("pf1_GPIO_Input= "+pf1_GPIO_Input);
            println();
        }


        ptd_GPIO_Input_H1.evaluate(GPIO_Input_H1);  //add branch
        ptd_GPIO_Input_L1.evaluate(GPIO_Input_L1);
        ptd_GPIO_Input_H2.evaluate(GPIO_Input_H2);
        ptd_GPIO_Input_L2.evaluate(GPIO_Input_L2);
        if (StaticFields.dev_name == "BGA7273")
        {
        ptd_GPIO_Input_H11.evaluate(GPIO_Input_H11);
        ptd_GPIO_Input_L11.evaluate(GPIO_Input_L11);
        ptd_GPIO_Input_H12.evaluate(GPIO_Input_H21);
        ptd_GPIO_Input_L12.evaluate(GPIO_Input_L21);
        ptd_GPIO_Input_H21.evaluate(GPIO_Input_H12);
        ptd_GPIO_Input_L21.evaluate(GPIO_Input_L12);
        ptd_GPIO_Input_H22.evaluate(GPIO_Input_H22);
        ptd_GPIO_Input_L22.evaluate(GPIO_Input_L22);
        }

    }
}
