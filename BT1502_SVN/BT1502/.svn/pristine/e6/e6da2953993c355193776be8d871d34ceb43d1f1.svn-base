
package BT1502_pro_tml.DC;

import java.util.Map;

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
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteDoubleArray;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigInOutActionResults;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class GPIO_FLASH_TEST_INPUT_GANG extends TestMethod {

    @In
    public String spec_measurement;
    public IMeasurement measurement;
    public IParametricTestDescriptor ptd_FlashClk_Output_L, ptd_FlashCS_Output_L,
            ptd_FlashWP_Output_L, ptd_FlashHOLD_Output_L, ptd_FlashClk_Output_H,
            ptd_FlashCS_Output_H, ptd_FlashWP_Output_H, ptd_FlashHOLD_Output_H,
            ptd_FlashSO_Output_H, ptd_FlashSI_Output_H, ptd_FLAG_FF, ptd_FLAG_00

    ;

    private String activePin_GPIO;

    @Override
    public void setup() {
        IDeviceSetup ds1 = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        ds1.importSpec(spec_measurement);

        /****************************
         * disconnect & Hiz Mode
         ************************************************/
        BesDsa_PS1600_Disconnect relay_PS1600_1 = new BesDsa_PS1600_Disconnect(ds1);
        String activePin_PS1600_1 = relay_PS1600_1.getPinGroup(context, spec_measurement,
                "All_Digital");
        activePin_PS1600_1 = activePin_PS1600_1.replaceAll("I2C_SCL\\+", "")
                .replaceAll("I2C_SDA\\+", "").replaceAll("POWKEY\\+", "")
                .replaceAll("RESETN\\+", "");
        relay_PS1600_1.disconnectAll_DigInOut(activePin_PS1600_1, false); // disconnect digital pins
                                                                          // to Hiz mode
        /***************************************************************************************************/

        // activePin_GPIO=new BesDsa_PS1600_Disconnect(ds1).getPinGroup(context, spec_measurement,
        // "FLASH_GRP_0");
        if (StaticFields.dev_name == "BGA7273") {
            activePin_GPIO = "FLASH1_CS + FLASH1_SO + FLASH1_SI + FLASH1_CLK + FLASH1_HOLD + FLASH1_WP";

        } else if (StaticFields.dev_name == "WLCSP") {
            activePin_GPIO = "FLASH0_CS + FLASH0_SO + FLASH0_SI + FLASH0_CLK + FLASH0_HOLD + FLASH0_WP";
        }
        ISetupDigInOut digInOut_GPIO = ds1.addDigInOut(activePin_GPIO).setConnect(true)
                .setDisconnect(true);
        digInOut_GPIO.protection().setDisconnectPulldownState(false);
        digInOut_GPIO.vforce("Vf_GPIO_0V").setForceValue("0 V").setIclamp("40 mA");
        digInOut_GPIO.vforce("Vf_GPIO_1p8V").setForceValue("2 V").setIclamp("40 mA");
        digInOut_GPIO.vmeas("Vm_Flash").setAverages(8).setDirectEnabled().setHighAccuracy(true);

        BesPA_I2C i2c = new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_16Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");

//        i2c.transactionSequenceBegin("FLASH1_low_mode");
//        if (StaticFields.dev_name == "BGA7273") {
//            i2c.read(BesI2cAddrType.DIGITAL,  0x40000038, "read_0x40000038"); // bit3
//            i2c.read(BesI2cAddrType.DIGITAL,  0x40000000, "read_0x40000000"); // bit3
//            i2c.read(BesI2cAddrType.DIGITAL,  0x40000048, "read_0x40000048"); // bit20
//            i2c.read(BesI2cAddrType.DIGITAL,  0x40000010, "read_0x40000010"); // bit20
//        }
//        i2c.transactionSequenceEnd();

        i2c.transactionSequenceBegin("GPIO_FLASH_Output_Low");
        if (StaticFields.dev_name == "WLCSP") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x4014002c, 0x3); // CS output low
            i2c.write(BesI2cAddrType.DIGITAL, 0x40140040, 0x00); // CLK output low
            i2c.write(BesI2cAddrType.DIGITAL, 0x40140000, 0x06);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40140034, 0x00);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40140014, 0x20013); // S0,S1 output high, S2,S3 output low
        }
        else if (StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x4050002c, 0x3); // CS output low
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500040, 0x00); // CLK output low
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500000, 0x06);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500034, 0x00);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500014, 0x20013); // S0,S1 output high, S2,S3 output low
        }
        i2c.transactionSequenceEnd();
        ds1.waitCall("100 ms");

        i2c.transactionSequenceBegin("read");
        i2c.read(BesI2cAddrType.DIGITAL, 0x4014002c, "read1"); // CS output low
        i2c.read(BesI2cAddrType.DIGITAL, 0x40140040, "read2"); // CLK output low
        i2c.read(BesI2cAddrType.DIGITAL, 0x40140000, "read3");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40140034, "read4");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40140014, "read5");
        i2c.transactionSequenceEnd();

        ds1.actionCall("Vm_Flash"); //Low

        i2c.transactionSequenceBegin("GPIO_FLASH_Output_High");
        if(StaticFields.dev_name == "WLCSP") {
        i2c.write(BesI2cAddrType.DIGITAL, 0x4014002c, 0x33); // CS output high
        i2c.write(BesI2cAddrType.DIGITAL, 0x40140040, 0x80); // CLK output high
        i2c.write(BesI2cAddrType.DIGITAL, 0x40140000, 0x06);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40140034, 0x00);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40140014, 0x20010); // S0,S1 output high, S2,S3 output high
        }
        else if (StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x4050002c, 0x33); // CS output high
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500040, 0x80); // CLK output high
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500000, 0x06);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500034, 0x00);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500014, 0x20010); // S0,S1 output high, S2,S3 output
            }
        i2c.transactionSequenceEnd();

        i2c.transactionSequenceBegin("readhi");
        i2c.read(BesI2cAddrType.DIGITAL, 0x4014002c, "read1hi"); // CS output hi
        i2c.read(BesI2cAddrType.DIGITAL, 0x40140040, "read2hi"); // CLK output hi
        i2c.read(BesI2cAddrType.DIGITAL, 0x40140000, "read3hi");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40140034, "read4hi");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40140014, "read5hi");
        i2c.transactionSequenceEnd();

        ds1.waitCall("5 ms");
        ds1.actionCall("Vm_Flash");

        i2c.transactionSequenceBegin("GPIO_FLASH_Input_Mode");
        i2c.write(BesI2cAddrType.DIGITAL, 0x40086008, 0x3fff);
        i2c.waitTime(1e6);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4008001c, 0x41007);
        i2c.waitTime(1e6);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4000005c, 0x21401);
        i2c.waitTime(10e6);
        i2c.read(BesI2cAddrType.DIGITAL, 0x40000060, "readValue_0x40000060");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40000010, "readValue_0x40000010");
        i2c.read(BesI2cAddrType.DIGITAL, 0x40000020, "readValue_0x40000020");
        if (StaticFields.dev_name == "WLCSP") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40140034, 0x3fc0000);
        } else if (StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500034, 0x3fc0000);
        }
        i2c.transactionSequenceEnd();

        ds1.waitCall("5 ms");
        ds1.actionCall("Vf_GPIO_0V");

        i2c.transactionSequenceBegin("GPIO_FLASH_Input_Mode_Low");
        if (StaticFields.dev_name == "WLCSP") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40140018, 0x3);
            i2c.waitTime(1e6);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40140000, 0xeb);
            i2c.waitTime(10e6);// wait for BaseAddr + 0x0c bit3 change to 1
            i2c.read(BesI2cAddrType.DIGITAL, 0x40140010, "readValue_0x40140010_L1");
        } else if (StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500018, 0x3);
            i2c.waitTime(1e6);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500000, 0xeb);
            i2c.waitTime(10e6);// wait for BaseAddr + 0x0c bit3 change to 1
            i2c.read(BesI2cAddrType.DIGITAL, 0x40500010, "readValue_0x40050010_L1");
        }

        i2c.transactionSequenceEnd();

        ds1.waitCall("5 ms");
        ds1.actionCall("Vf_GPIO_1p8V");

        i2c.transactionSequenceBegin("GPIO_FLASH_Input_Mode_High");
        if (StaticFields.dev_name == "WLCSP") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40140018, 0x3);
            i2c.waitTime(1e6);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40140000, 0xeb);
            i2c.waitTime(10e6);// wait for BaseAddr + 0x0c bit3 change to 1
            i2c.read(BesI2cAddrType.DIGITAL, 0x40140010, "readValue_0x40140010_H1");
        } else if (StaticFields.dev_name == "BGA7273") {
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500018, 0x3);
            i2c.waitTime(1e6);
            i2c.write(BesI2cAddrType.DIGITAL, 0x40500000, 0xeb);
            i2c.waitTime(10e6);// wait for BaseAddr + 0x0c bit3 change to 1
            i2c.read(BesI2cAddrType.DIGITAL, 0x40500010, "readValue_0x40050010_H1");
        }
        i2c.transactionSequenceEnd();

        measurement.setSetups(ds1);
    }

    @Override
    public void execute() {

        measurement.execute();
        // get diginout result
        IDigInOutActionResults res_DigInOut = measurement.digInOut(activePin_GPIO)
                .preserveActionResults();
        Map<String, MultiSiteDoubleArray> res_GPIO_Array = res_DigInOut.vmeas("Vm_Flash")
                .getVoltage();
        IProtocolInterfaceResults res_PA = ProtocolAccess.preserveResults(measurement);
        // FLASH0_CS + FLASH0_SO + FLASH0_SI + FLASH0_CLK + FLASH0_HOLD + FLASH0_WP
        MultiSiteDouble FLASH0_CS_L = new MultiSiteDouble(0);
        MultiSiteDouble FLASH0_CS_H = new MultiSiteDouble(0);

        MultiSiteDouble FLASH0_SO_L = new MultiSiteDouble(0);
        MultiSiteDouble FLASH0_SO_H = new MultiSiteDouble(0);

        MultiSiteDouble FLASH0_SI_L = new MultiSiteDouble(0);
        MultiSiteDouble FLASH0_SI_H = new MultiSiteDouble(0);

        MultiSiteDouble FLASH0_CLK_L = new MultiSiteDouble(0);
        MultiSiteDouble FLASH0_CLK_H = new MultiSiteDouble(0);

        MultiSiteDouble FLASH0_HOLD_L = new MultiSiteDouble(0);
        MultiSiteDouble FLASH0_HOLD_H = new MultiSiteDouble(0);

        MultiSiteDouble FLASH0_WP_L = new MultiSiteDouble(0);
        MultiSiteDouble FLASH0_WP_H = new MultiSiteDouble(0);

//
//      MultiSiteLong read_0x40000038 = res_PA.getResult("read_0x40000038");
//      MultiSiteLong read_0x40000000 = res_PA.getResult("read_0x40000000");
//      MultiSiteLong read_0x40000048 = res_PA.getResult("read_0x40000048");
//      MultiSiteLong read_0x40000010 = res_PA.getResult("read_0x40000010");

//    for (int j : context.getActiveSites()) {
//    System.out.println("read_0x40000038 =" + Long.toHexString(read_0x40000038.get(j)));
//    System.out.println("read_0x40000000 =" + Long.toHexString(read_0x40000000.get(j)));
//    System.out.println("read_0x40000048 =" + Long.toHexString(read_0x40000048.get(j)));
//    System.out.println("read_0x40000010 =" + Long.toHexString(read_0x40000010.get(j)));
//    }

//        MultiSiteLong read1 = res_PA.getResult("read1");
//        MultiSiteLong read2 = res_PA.getResult("read2");
//        MultiSiteLong read3 = res_PA.getResult("read3");
//        MultiSiteLong read4 = res_PA.getResult("read4");
//
//        MultiSiteLong read1hi = res_PA.getResult("read1hi");
//        MultiSiteLong read2hi = res_PA.getResult("read2hi");
//        MultiSiteLong read3hi = res_PA.getResult("read3hi");
//        MultiSiteLong read4hi = res_PA.getResult("read4hi");
//
//        for (int j : context.getActiveSites()) {
//        System.out.println("read1 =" +read1.get(j));
//        System.out.println("read2 =" +read2.get(j));
//        System.out.println("read3 =" +read3.get(j));
//        System.out.println("read4 =" +read4.get(j));
//        }
//
//        for (int j : context.getActiveSites()) {
//        System.out.println("read1 =" +read1hi.get(j));
//        System.out.println("read2 =" +read2hi.get(j));
//        System.out.println("read3 =" +read3hi.get(j));
//        System.out.println("read4 =" +read4hi.get(j));
//        }

        if (StaticFields.dev_name == "BGA7273") {
            FLASH0_CS_L = res_GPIO_Array.get("FLASH1_CS").getElement(0);
            FLASH0_CS_H = res_GPIO_Array.get("FLASH1_CS").getElement(1);

            FLASH0_SO_L = res_GPIO_Array.get("FLASH1_SO").getElement(0);
            FLASH0_SO_H = res_GPIO_Array.get("FLASH1_SO").getElement(1);

            FLASH0_SI_L = res_GPIO_Array.get("FLASH1_SI").getElement(0);
            FLASH0_SI_H = res_GPIO_Array.get("FLASH1_SI").getElement(1);

            FLASH0_CLK_L = res_GPIO_Array.get("FLASH1_CLK").getElement(0);
            FLASH0_CLK_H = res_GPIO_Array.get("FLASH1_CLK").getElement(1);

            FLASH0_HOLD_L = res_GPIO_Array.get("FLASH1_HOLD").getElement(0);
            FLASH0_HOLD_H = res_GPIO_Array.get("FLASH1_HOLD").getElement(1);

            FLASH0_WP_L = res_GPIO_Array.get("FLASH1_WP").getElement(0);
            FLASH0_WP_H = res_GPIO_Array.get("FLASH1_WP").getElement(1);
        } else if (StaticFields.dev_name == "WLCSP") {
            FLASH0_CS_L = res_GPIO_Array.get("FLASH0_CS").getElement(0);
            FLASH0_CS_H = res_GPIO_Array.get("FLASH0_CS").getElement(1);

            FLASH0_SO_L = res_GPIO_Array.get("FLASH0_SO").getElement(0);
            FLASH0_SO_H = res_GPIO_Array.get("FLASH0_SO").getElement(1);

            FLASH0_SI_L = res_GPIO_Array.get("FLASH0_SI").getElement(0);
            FLASH0_SI_H = res_GPIO_Array.get("FLASH0_SI").getElement(1);

            FLASH0_CLK_L = res_GPIO_Array.get("FLASH0_CLK").getElement(0);
            FLASH0_CLK_H = res_GPIO_Array.get("FLASH0_CLK").getElement(1);

            FLASH0_HOLD_L = res_GPIO_Array.get("FLASH0_HOLD").getElement(0);
            FLASH0_HOLD_H = res_GPIO_Array.get("FLASH0_HOLD").getElement(1);

            FLASH0_WP_L = res_GPIO_Array.get("FLASH0_WP").getElement(0);
            FLASH0_WP_H = res_GPIO_Array.get("FLASH0_WP").getElement(1);
        }
        // get register result
        MultiSiteLong readValue_0x40140010_L1 = new MultiSiteLong();
        MultiSiteLong readValue_0x40140010_H1 = new MultiSiteLong();
        MultiSiteLong readValue_0x40050010_L1 = new MultiSiteLong();
        MultiSiteLong readValue_0x40050010_H1 = new MultiSiteLong();
//        IProtocolInterfaceResults res_PA = ProtocolAccess.preserveResults(measurement);
        if (StaticFields.dev_name == "WLCSP") {
            readValue_0x40140010_L1 = res_PA.getResult("readValue_0x40140010_L1");
            readValue_0x40140010_H1 = res_PA.getResult("readValue_0x40140010_H1");
        } else if (StaticFields.dev_name == "BGA7273") {
            readValue_0x40050010_L1 = res_PA.getResult("readValue_0x40050010_L1");
            readValue_0x40050010_H1 = res_PA.getResult("readValue_0x40050010_H1");
        }
        MultiSiteLong readValue_0x4008004C = res_PA.getResult("readValue_0x4008004C");
        MultiSiteLong res = new MultiSiteLong();
        MultiSiteLong res1 = new MultiSiteLong();
        if (StaticFields.dev_name == "WLCSP") {
            for (int site : context.getActiveSites()) {
                if (readValue_0x40140010_L1.get(site) == 0x0) {
                    res.set(site, 1);
                } else {
                    res.set(site, 0);
                }
            }

            for (int site : context.getActiveSites()) {
                if (readValue_0x40140010_H1.get(site) == 0xffffffffL) {
                    res1.set(site, 1);
                } else {
                    res1.set(site, 0);
                }
            }
        } else if (StaticFields.dev_name == "BGA7273") {
            for (int site : context.getActiveSites()) {
                if (readValue_0x40050010_L1.get(site) == 0x0) {
                    res.set(site, 1);
                } else {
                    res.set(site, 0);
                }
            }

            for (int site : context.getActiveSites()) {
                if (readValue_0x40050010_H1.get(site) == 0xffffffffL) {
                    res1.set(site, 1);
                } else {
                    res1.set(site, 0);
                }
            }
        }
        if (StaticFields.debugMode) {
            String testSuiteName_Qualified = context.getTestSuiteName();
            String testSuiteName = testSuiteName_Qualified
                    .substring(1 + testSuiteName_Qualified.lastIndexOf("."));
            println("**********" + testSuiteName + "**********");
            for (int site : context.getActiveSites()) {
                System.out.println("site= " + site + " readValue_0x40140010_L1 Binary Data= "
                        + Long.toBinaryString(readValue_0x40140010_L1.get(site)));
                System.out.println("site= " + site + " readValue_0x40140010_H1 Binary Data= "
                        + Long.toBinaryString(readValue_0x40140010_H1.get(site)));

                // System.out.println("site= "+site+" readValue_0x4008004C=
                // "+Long.toHexString(readValue_0x4008004C.get(site)));

//                System.out.println("site= " + site + " readValue_0x4008004C= "
//                        + Long.toHexString(readValue_0x4008004C.get(site)));

                println();
            }
            System.out.println("FLASH0_CS_L= " + FLASH0_CS_L);
            System.out.println("FLASH0_CS_H= " + FLASH0_CS_H);
            System.out.println("FLASH0_SO_L= " + FLASH0_SO_L);
            System.out.println("FLASH0_SO_H= " + FLASH0_SO_H);
            System.out.println("FLASH0_SI_L= " + FLASH0_SI_L);
            System.out.println("FLASH0_SI_H= " + FLASH0_SI_H);
            System.out.println("FLASH0_CLK_L= " + FLASH0_CLK_L);
            System.out.println("FLASH0_CLK_H= " + FLASH0_CLK_H);
            System.out.println("FLASH0_HOLD_L= " + FLASH0_HOLD_L);
            System.out.println("FLASH0_HOLD_H= " + FLASH0_HOLD_H);
            System.out.println("FLASH0_WP_L= " + FLASH0_WP_L);
            System.out.println("FLASH0_WP_H= " + FLASH0_WP_H);
            println();
        }

        ptd_FlashClk_Output_H.evaluate(FLASH0_CLK_H);
        ptd_FlashClk_Output_L.evaluate(FLASH0_CLK_L);
        ptd_FlashCS_Output_H.evaluate(FLASH0_CS_H);
        ptd_FlashCS_Output_L.evaluate(FLASH0_CS_L);
        ptd_FlashHOLD_Output_H.evaluate(FLASH0_HOLD_H);
        ptd_FlashHOLD_Output_L.evaluate(FLASH0_HOLD_L);
        ptd_FlashWP_Output_H.evaluate(FLASH0_WP_H);
        ptd_FlashWP_Output_L.evaluate(FLASH0_WP_L);
        ptd_FlashClk_Output_H.evaluate(FLASH0_CLK_H);
        ptd_FlashClk_Output_L.evaluate(FLASH0_CLK_L);
        ptd_FlashSI_Output_H.evaluate(FLASH0_SI_H);
        ptd_FlashSO_Output_H.evaluate(FLASH0_SO_H);
        ptd_FLAG_FF.evaluate(res1);
        ptd_FLAG_00.evaluate(res);

    }
}
