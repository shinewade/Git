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

public class AUDIO_ADDA_MODE_ANC_TTMC extends TestMethod {

    @In
    public String spec_measurement;

    @In
    public String modeMux = "Nor";// "Nor" : normal mode vcodec 1.8V
                                  // "NorLV" : normal mode vcodec 1.4V
                                  // "HP" : high performance mode vcodec 1.8V

    public ILocalMeasurement measurement;

    @Override
    public void setup() {
        IDeviceSetup deviceSetup = DeviceSetupFactory.createInstance(StaticFields.prog_name);
        deviceSetup.importSpec(spec_measurement);

        BesPA_I2C i2c = new BesPA_I2C(deviceSetup, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals("I2C_SCL", "I2C_SDA");
        // [ANA ft ]
        // [codec adda]
        i2c.transactionSequenceBegin("AUDIO_ADDA_MODE");
        // ISetupProtocolInterface paInterface = deviceSetup.addProtocolInterface("I2C_BES",
        // "besLib.pa.I2C_8bit_BES");
        // paInterface.addSignalRole("DATA", "I2C_SDA");
        // paInterface.addSignalRole("CLK", "I2C_SCL");
        // ISetupTransactionSeqDef transDigSrc=
        // paInterface.addTransactionSequenceDef("AUDIO_ADDA_MODE");

        // [DAC 1k close]
        i2c.write(BesI2cAddrType.DIGITAL, 0x403000b0, 0x010A0207);
        i2c.waitTime(1e6);
        // [anc_tt_mcfb_lmt]
        i2c.write(BesI2cAddrType.DIGITAL, 0x40085078, 0xCAFE02F0); // codec psc
        // 0x4008004c,0x10947040); //codec clk sel inner dig
        i2c.write(BesI2cAddrType.DIGITAL, 0x40080004, 0x0002008F); // aon clk en
        i2c.write(BesI2cAddrType.DIGITAL, 0x40000060, 0x00006000); // pu oscx2
        i2c.write(BesI2cAddrType.DIGITAL, 0x400800a0, 0x000019c0);
        i2c.write(BesI2cAddrType.DIGITAL, 0x400800cc, 0x00000002); // clear codec reset
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300074, 0x1a004012);
        i2c.waitTime(10e6);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300060, 0x007fffff); // codec clk en
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030006c, 0x020007d6); // iir_clk_en
        // ch0
        // FF iira coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301000, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301004, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301008, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030100c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301010, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301014, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301018, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030101c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301020, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301024, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301028, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030102c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301030, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301034, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301038, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030103c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301040, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301044, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301048, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030104c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301050, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301054, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301058, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030105c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301060, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301064, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301068, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030106c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301070, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301074, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301078, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030107c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301080, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301084, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301088, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030108c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301090, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301094, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301098, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030109c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010a0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010a4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010a8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010ac, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010b0, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010b4, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010b8, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010bc, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010c0, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010c4, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010c8, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010cc, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010d0, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010d4, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010d8, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010dc, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010e0, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010e4, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010e8, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010ec, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010f0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010f4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010f8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403010fc, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301100, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301104, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301108, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030110c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301110, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301114, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301118, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030111c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301120, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301124, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301128, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030112c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301130, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301134, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301138, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030113c, 0x08000000);
        // FF gaina ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301140, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301144, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301148, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030114c, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301150, 0x00000200);
        // TT gaina ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301154, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301158, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030115c, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301160, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301164, 0x00000200);
        // FF lmta coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301168, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030116c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301170, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301174, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301178, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030117c, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301180, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301184, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301188, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030118c, 0x00000000);
        // TT lmta coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301190, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301194, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301198, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030119c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403011a0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403011a4, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403011a8, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403011ac, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403011b0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403011b4, 0x00000000);
        // FF iirb coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301200, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301204, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301208, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030120c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301210, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301214, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301218, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030121c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301220, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301224, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301228, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030122c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301230, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301234, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301238, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030123c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301240, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301244, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301248, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030124c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301250, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301254, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301258, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030125c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301260, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301264, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301268, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030126c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301270, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301274, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301278, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030127c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301280, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301284, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301288, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030128c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301290, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301294, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301298, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030129c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012a0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012a4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012a8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012ac, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012b0, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012b4, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012b8, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012bc, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012c0, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012c4, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012c8, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012cc, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012d0, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012d4, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012d8, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012dc, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012e0, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012e4, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012e8, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012ec, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012f0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012f4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012f8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403012fc, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301300, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301304, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301308, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030130c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301310, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301314, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301318, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030131c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301320, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301324, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301328, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030132c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301330, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301334, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301338, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030133c, 0x08000000);
        // FF gainb ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301340, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301344, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301348, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030134c, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301350, 0x00000200);
        // TT gaina ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301354, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301358, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030135c, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301360, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301364, 0x00000200);
        // FF lmtb coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301368, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030136c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301370, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301374, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301378, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030137c, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301380, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301384, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301388, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030138c, 0x00000000);
        // TT lmta coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301390, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301394, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40301398, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030139c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403013a0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403013a4, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403013a8, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403013ac, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403013b0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403013b4, 0x00000000);
        // FB iira coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303000, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303004, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303008, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030300c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303010, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303014, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303018, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030301c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303020, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303024, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303028, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030302c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303030, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303034, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303038, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030303c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303040, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303044, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303048, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030304c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303050, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303054, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303058, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030305c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303060, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303064, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303068, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030306c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303070, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303074, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303078, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030307c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303080, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303084, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303088, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030308c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303090, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303094, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303098, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030309c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030a0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030a4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030a8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030ac, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030b0, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030b4, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030b8, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030bc, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030c0, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030c4, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030c8, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030cc, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030d0, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030d4, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030d8, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030dc, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030e0, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030e4, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030e8, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030ec, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030f0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030f4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030f8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403030fc, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303100, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303104, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303108, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030310c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303110, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303114, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303118, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030311c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303120, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303124, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303128, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030312c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303130, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303134, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303138, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030313c, 0x08000000);
        // FB gaina ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303140, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303144, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303148, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030314c, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303150, 0x00000200);
        // MC gaina ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303154, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303158, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030315c, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303160, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303164, 0x00000200);
        // FB lmta coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303168, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030316c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303170, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303174, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303178, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030317c, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303180, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303184, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303188, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030318c, 0x00000000);
        // MC lmta coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303190, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303194, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303198, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030319c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403031a0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403031a4, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403031a8, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403031ac, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403031b0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403031b4, 0x00000000);
        // FB iirb coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303200, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303204, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303208, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030320c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303210, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303214, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303218, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030321c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303220, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303224, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303228, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030322c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303230, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303234, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303238, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030323c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303240, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303244, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303248, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030324c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303250, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303254, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303258, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030325c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303260, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303264, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303268, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030326c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303270, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303274, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303278, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030327c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303280, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303284, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303288, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030328c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303290, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303294, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303298, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030329c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032a0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032a4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032a8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032ac, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032b0, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032b4, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032b8, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032bc, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032c0, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032c4, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032c8, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032cc, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032d0, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032d4, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032d8, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032dc, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032e0, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032e4, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032e8, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032ec, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032f0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032f4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032f8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403032fc, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303300, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303304, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303308, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030330c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303310, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303314, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303318, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030331c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303320, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303324, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303328, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030332c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303330, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303334, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303338, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030333c, 0x08000000);
        // FB gainb ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303340, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303344, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303348, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030334c, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303350, 0x00000200);
        // MC gaina ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303354, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303358, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030335c, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303360, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303364, 0x00000200);
        // FB lmtb coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303368, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030336c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303370, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303374, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303378, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030337c, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303380, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303384, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303388, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030338c, 0x00000000);
        // MC lmta coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303390, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303394, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40303398, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030339c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403033a0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403033a4, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403033a8, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403033ac, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403033b0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403033b4, 0x00000000);
        // ch1
        // FF iira coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302000, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302004, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302008, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030200c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302010, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302014, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302018, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030201c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302020, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302024, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302028, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030202c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302030, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302034, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302038, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030203c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302040, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302044, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302048, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030204c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302050, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302054, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302058, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030205c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302060, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302064, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302068, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030206c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302070, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302074, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302078, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030207c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302080, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302084, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302088, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030208c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302090, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302094, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302098, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030209c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020a0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020a4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020a8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020ac, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020b0, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020b4, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020b8, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020bc, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020c0, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020c4, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020c8, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020cc, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020d0, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020d4, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020d8, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020dc, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020e0, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020e4, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020e8, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020ec, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020f0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020f4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020f8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403020fc, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302100, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302104, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302108, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030210c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302110, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302114, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302118, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030211c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302120, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302124, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302128, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030212c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302130, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302134, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302138, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030213c, 0x08000000);
        // FF gaina ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302140, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302144, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302148, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030214c, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302150, 0x00000200);
        // TT gaina ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302154, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302158, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030215c, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302160, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302164, 0x00000200);
        // FF lmta coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302168, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030216c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302170, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302174, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302178, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030217c, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302180, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302184, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302188, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030218c, 0x00000000);
        // TT lmta coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302190, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302194, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302198, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030219c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403021a0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403021a4, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403021a8, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403021ac, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403021b0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403021b4, 0x00000000);
        // FF iirb coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302200, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302204, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302208, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030220c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302210, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302214, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302218, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030221c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302220, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302224, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302228, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030222c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302230, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302234, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302238, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030223c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302240, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302244, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302248, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030224c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302250, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302254, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302258, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030225c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302260, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302264, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302268, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030226c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302270, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302274, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302278, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030227c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302280, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302284, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302288, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030228c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302290, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302294, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302298, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030229c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022a0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022a4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022a8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022ac, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022b0, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022b4, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022b8, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022bc, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022c0, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022c4, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022c8, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022cc, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022d0, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022d4, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022d8, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022dc, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022e0, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022e4, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022e8, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022ec, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022f0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022f4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022f8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403022fc, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302300, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302304, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302308, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030230c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302310, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302314, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302318, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030231c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302320, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302324, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302328, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030232c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302330, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302334, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302338, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030233c, 0x08000000);
        // FF gainb ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302340, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302344, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302348, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030234c, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302350, 0x00000200);
        // TT gaina ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302354, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302358, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030235c, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302360, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302364, 0x00000200);
        // FF lmtb coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302368, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030236c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302370, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302374, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302378, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030237c, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302380, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302384, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302388, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030238c, 0x00000000);
        // TT lmta coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302390, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302394, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40302398, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030239c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403023a0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403023a4, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403023a8, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403023ac, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403023b0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403023b4, 0x00000000);
        // FB iira coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304000, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304004, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304008, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030400c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304010, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304014, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304018, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030401c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304020, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304024, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304028, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030402c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304030, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304034, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304038, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030403c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304040, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304044, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304048, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030404c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304050, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304054, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304058, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030405c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304060, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304064, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304068, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030406c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304070, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304074, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304078, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030407c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304080, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304084, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304088, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030408c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304090, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304094, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304098, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030409c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040a0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040a4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040a8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040ac, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040b0, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040b4, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040b8, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040bc, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040c0, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040c4, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040c8, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040cc, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040d0, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040d4, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040d8, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040dc, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040e0, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040e4, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040e8, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040ec, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040f0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040f4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040f8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403040fc, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304100, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304104, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304108, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030410c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304110, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304114, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304118, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030411c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304120, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304124, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304128, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030412c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304130, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304134, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304138, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030413c, 0x08000000);
        // FB gaina ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304140, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304144, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304148, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030414c, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304150, 0x00000200);
        // MC gaina ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304154, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304158, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030415c, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304160, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304164, 0x00000200);
        // FB lmta coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304168, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030416c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304170, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304174, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304178, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030417c, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304180, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304184, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304188, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030418c, 0x00000000);
        // MC lmta coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304190, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304194, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304198, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030419c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403041a0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403041a4, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403041a8, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403041ac, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403041b0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403041b4, 0x00000000);
        // FB iirb coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304200, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304204, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304208, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030420c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304210, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304214, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304218, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030421c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304220, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304224, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304228, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030422c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304230, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304234, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304238, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030423c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304240, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304244, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304248, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030424c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304250, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304254, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304258, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030425c, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304260, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304264, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304268, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030426c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304270, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304274, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304278, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030427c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304280, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304284, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304288, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030428c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304290, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304294, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304298, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030429c, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042a0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042a4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042a8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042ac, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042b0, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042b4, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042b8, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042bc, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042c0, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042c4, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042c8, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042cc, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042d0, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042d4, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042d8, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042dc, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042e0, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042e4, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042e8, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042ec, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042f0, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042f4, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042f8, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403042fc, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304300, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304304, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304308, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030430c, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304310, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304314, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304318, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030431c, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304320, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304324, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304328, 0x08000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030432c, 0x0fe01542);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304330, 0xf81f6e4b);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304334, 0xf01feabe);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304338, 0x07e091b5);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030433c, 0x08000000);
        // FB gainb ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304340, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304344, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304348, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030434c, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304350, 0x00000200);
        // MC gaina ramp coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304354, 0x0fd825bb);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304358, 0xf827d243);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030435c, 0x00000401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304360, 0x00000200);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304364, 0x00000200);
        // FB lmtb coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304368, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030436c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304370, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304374, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304378, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030437c, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304380, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304384, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304388, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030438c, 0x00000000);
        // MC lmta coef
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304390, 0x07bae1cf);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304394, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x40304398, 0x00451e31);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030439c, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403043a0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403043a4, 0x07ffc401);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403043a8, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403043ac, 0x00003bff);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403043b0, 0x00000000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403043b4, 0x00000000);
        // ANC cfg
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300300, 0x08102040); // lmt_delay=64, FF&FB
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300304, 0x08102040); // lmt_delay=64, TT&MC
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030031c, 0x007ffffc); // lmt_th, FF, limit to 0db
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300320, 0x007ffffc); // lmt_th, TT, limit to 0db
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300324, 0x007ffffc); // lmt_th, FF, limit to 0db
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300328, 0x007ffffc); // lmt_th, FF, limit to 0db
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030032c, 0x007ffffc); // lmt_th, FB, limit to 0db
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300330, 0x007ffffc); // lmt_th, MC, limit to 0db
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300334, 0x007ffffc); // lmt_th, FB, limit to 0db
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300338, 0x007ffffc); // lmt_th, FB, limit to 0db
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030025c, 0x0FF00000); // lmt_th_update
        // for FF/TT test
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300260, 0x04000000); // FF iir gaina
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300264, 0x04000000); // FF iir gaina
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300268, 0x04000000); // FF iir gainb
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030026c, 0x04000000); // FF iir gainb
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300270, 0x04000000); // FF iir gaina
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300274, 0x04000000); // FF iir gaina
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300278, 0x04000000); // FF iir gainb
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030027c, 0x04000000); // FF iir gainb
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030025c, 0x05500033); // FF iir gain update
        // 0x40300248,0x00084557); //FF iir, en iira & iirb
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300248, 0x000842AF); // TT iir, en iira & iirb
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030024c, 0x000842AF); // TT iir, en iira & iirb
        // for FB/MC test
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300280, 0x04000000); // FB iir gaina
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300284, 0x04000000); // FB iir gaina
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300288, 0x04000000); // FB iir gainb
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030028c, 0x04000000); // FB iir gainb
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300290, 0x04000000); // FB iir gaina
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300294, 0x04000000); // FB iir gaina
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300298, 0x04000000); // FB iir gainb
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030029c, 0x04000000); // FB iir gainb
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030025c, 0x05503333); // FB iir gain update
        // 0x40300250,0x00108557); //FB iir, en iira & iirb
        // 0x40300250,0x001082AF); //MC iir, en iira & iirb
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300250, 0x000A5007); // FB&MC iir, en iira & iirb
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300254, 0x000A5007); // FB&MC iir, en iira & iirb
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300238, 0x00000000); // mute fb adc input
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030023C, 0x00000000); // mute fb adc input
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300244, 0x000000FF); // update fb calib gain
        // anc path
        // 0x403000d0,0x00000001); //open anc, FF mode only
        i2c.write(BesI2cAddrType.DIGITAL, 0x403000d0, 0x00600003); // open anc, FB mode only
        i2c.write(BesI2cAddrType.DIGITAL, 0x403000DC, 0x0000000C); // mc enable
        // TTMC path
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030022C, 0x00001001); // open tt
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030022C, 0x00831831); // open mc, delay 16
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030033C, 0x00000A11); // music_mix_off
        i2c.write(BesI2cAddrType.DIGITAL, 0x403000b0, 0x010A0207); // open dac
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300080, 0x00001f3F); // open adc
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300084, 0x00400020); // adc ch0(FF sel micA)
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300088, 0x00400020);
        i2c.write(BesI2cAddrType.DIGITAL, 0x4030008c, 0x00400020); // adc ch2(FB sel micA)
        i2c.write(BesI2cAddrType.DIGITAL, 0x40300090, 0x00400020);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403000b4, 0x00004000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403000b8, 0x00004000);
        i2c.write(BesI2cAddrType.DIGITAL, 0x403000b4, 0x07E04000); // update adc/dac gain
        i2c.transactionSequenceEnd();

        measurement.setSetups(deviceSetup);
    }

    @Override
    public void execute() {
        measurement.execute();

        if (StaticFields.debugMode) {
            String testSuiteName_Qualified = context.getTestSuiteName();
            String testSuiteName = testSuiteName_Qualified
                    .substring(1 + testSuiteName_Qualified.lastIndexOf("."));
            println("**********" + testSuiteName + "**********");
            println();
        }
    }
}
