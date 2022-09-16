package BT1502_pro_tml.EFFUSE;

import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import besLib.dsa.efuse.BesDsa_Efuse;
import besLib.pa.BesPA_I2C;
import besLib.pa.BesPA_I2C.BesI2cAddrType;
import besLib.pa.BesPA_I2C.I2cRegAddrBits;
import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dta.ITestContext;
import xoc.dta.datatypes.MultiSiteBoolean;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;

public class BesDsa_Efuse_BT1502  extends BesDsa_Efuse{

    private String I2C_SCL="I2C_SCL";
    private String I2C_SDA="I2C_SDA";
    private int efuseAddr=0;
    private boolean efuseBlock2=false;

    @Override
    public void genDsa_ReadEfuse(int efuseAddr, IMeasurement measurement, String spec, String prefix) {
        // TODO Auto-generated method stub
        this.efuseAddr=efuseAddr;
        if (this.efuseAddr==16||this.efuseAddr ==17) {// user define which efuse page is written to block 2;
            this.efuseBlock2=true;

        }
        IDeviceSetup ds1=DeviceSetupFactory.createInstance(prefix);
        ds1.importSpec(spec);
        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals(I2C_SCL, I2C_SDA);
        if (this.efuseBlock2) {
            i2c.transactionSequenceBegin("BT1502_ReadEfuse_"+efuseAddr);
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7,0x8000);                //open array read mode
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7,0x8008);                //open effuse control clk
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7,0x8018);                //open effuse turn on
//                i2c.write(BesI2cAddrType.PMUIntern, 0xb7,0x0000);                //open array read mode
//                i2c.write(BesI2cAddrType.PMUIntern, 0xb7,0x0008);                //open effuse control clk
//                i2c.write(BesI2cAddrType.PMUIntern, 0xb7,0x0018);                //open effuse turn on
                i2c.waitTime(0.25e6);
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, (efuseAddr << 6) | 0x8018);           //write address, 9 bits {data | (1'b0,address,6'b0) }
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, (efuseAddr << 6) | 0x8038);           //read trigger {data | (10'h0,1'b1,5'h0)}
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, ((efuseAddr << 6)| 0x8038) & 0xffdf);//close read trigger
                i2c.waitTime(0.25e6);
                i2c.read( BesI2cAddrType.PMUIntern, 0xbd, "efuse_Hihg");          //efuse_data_out_hi[15:0]
                i2c.read( BesI2cAddrType.PMUIntern, 0xbe, "efuse_Low");           //efuse_data_out_low[15:0]
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x8008);                //close effuse control clk
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x8000);                //close array read mode
            i2c.transactionSequenceEnd();
        }
        else {
            i2c.transactionSequenceBegin("BT1502_ReadEfuse_"+efuseAddr);
            i2c.write(BesI2cAddrType.PMUIntern, 0xb7,0x0000);                //open array read mode
            i2c.write(BesI2cAddrType.PMUIntern, 0xb7,0x0008);                //open effuse control clk
            i2c.write(BesI2cAddrType.PMUIntern, 0xb7,0x0018);                //open effuse turn on
            i2c.waitTime(0.25e6);
            i2c.write(BesI2cAddrType.PMUIntern, 0xb7, (efuseAddr << 6) | 0x0018);           //write address, 9 bits {data | (1'b0,address,6'b0) }
            i2c.write(BesI2cAddrType.PMUIntern, 0xb7, (efuseAddr << 6) | 0x0038);           //read trigger {data | (10'h0,1'b1,5'h0)}
            i2c.write(BesI2cAddrType.PMUIntern, 0xb7, ((efuseAddr << 6)| 0x0038) & 0xffdf);//close read trigger
            i2c.waitTime(0.25e6);
            i2c.read( BesI2cAddrType.PMUIntern, 0xbd, "efuse_Hihg");          //efuse_data_out_hi[15:0]
            i2c.read( BesI2cAddrType.PMUIntern, 0xbe, "efuse_Low");           //efuse_data_out_low[15:0]
            i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x0008);                //close effuse control clk
            i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x0000);                //close array read mode
            i2c.transactionSequenceEnd();

        }

        measurement.setSetups(ds1);
    }

    @Override
    public void genDsa_WriteEfuse(IMeasurement measurement, String spec, String prefix) {
        // TODO Auto-generated method stub
        IDeviceSetup ds1=DeviceSetupFactory.createInstance(prefix);
        ds1.importSpec(spec);
        BesPA_I2C i2c=new BesPA_I2C(ds1, measurement, I2cRegAddrBits.RegAddr_8Bits);
        i2c.setSignals(I2C_SCL, I2C_SDA);
        if(this.efuseBlock2) {
            i2c.transactionSequenceBegin("BT1502_Dyn_WriteEfuse");
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x8001);//open array write mode
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x8009);//open effuse control clk
                for(int bitIndex=0; bitIndex<16; bitIndex++) {

                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_Data_PGM_mode"+bitIndex);//open function turn on
                    i2c.waitTime(0.25e6);
                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_Data_PGM_addr1_"+bitIndex);//write trigger enable
                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_PGM_bit_efuse_open1_"+bitIndex);//write trigger disable
                    i2c.waitTime(0.25e6);
                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_PGM_bit_efuse_close1_"+bitIndex);//open function turn on
                    i2c.waitTime(0.25e6);
                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_PGM_addr2_"+bitIndex);//write trigger enable
                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_PGM_bit_efuse_open2_"+bitIndex);//write trigger disable
                    i2c.waitTime(0.25e6);
                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_PGM_bit_efuse_close2_"+bitIndex);//open function turn on
                    i2c.waitTime(0.25e6);
                }
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x8009);//function turn off
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x8001);//clear data[14:10],address[9:4]
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x8000);                //close clock,pgm mode
            i2c.transactionSequenceEnd();
        }
        else {
            i2c.transactionSequenceBegin("BT1502_Dyn_WriteEfuse");
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x0001);//open array write mode
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x0009);//open effuse control clk
                for(int bitIndex=0; bitIndex<16; bitIndex++) {

                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_Data_PGM_mode"+bitIndex);//open function turn on
                    i2c.waitTime(0.25e6);
                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_Data_PGM_addr1_"+bitIndex);//write trigger enable
                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_PGM_bit_efuse_open1_"+bitIndex);//write trigger disable
                    i2c.waitTime(0.25e6);
                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_PGM_bit_efuse_close1_"+bitIndex);//open function turn on
                    i2c.waitTime(0.25e6);
                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_PGM_addr2_"+bitIndex);//write trigger enable
                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_PGM_bit_efuse_open2_"+bitIndex);//write trigger disable
                    i2c.waitTime(0.25e6);
                    i2c.write(BesI2cAddrType.PMUIntern, 0xb7, "dynWrite_PGM_bit_efuse_close2_"+bitIndex);//open function turn on
                    i2c.waitTime(0.25e6);
                }
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x0009);//function turn off
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x0001);//clear data[14:10],address[9:4]
                i2c.write(BesI2cAddrType.PMUIntern, 0xb7, 0x0000);                //close clock,pgm mode
            i2c.transactionSequenceEnd();

        }
        measurement.setSetups(ds1);
    }

    @Override
    public void writeEfuse(int efuseAddr, MultiSiteLong efuseData, MultiSiteBoolean isEfuse, IMeasurement measurement, ITestContext context) {
        for(int i=0;i<16;i++){
            MultiSiteLong Data_PGM_mode=new MultiSiteLong();
            MultiSiteLong Data_PGM_addr_1=new MultiSiteLong();
            MultiSiteLong Data_PGM_bit_efuse_open_1=new MultiSiteLong();
            MultiSiteLong Data_PGM_bit_efuse_close_1=new MultiSiteLong();
            MultiSiteLong Data_PGM_addr_2=new MultiSiteLong();
            MultiSiteLong Data_PGM_bit_efuse_open_2=new MultiSiteLong();
            MultiSiteLong Data_PGM_bit_efuse_close_2=new MultiSiteLong();
            int[] activeSites = context.getActiveSites();
            for(int site:activeSites) {
                if(this.efuseBlock2) {
                    Data_PGM_mode.set(site, ((efuseData.get(site)>>i) & 0x1) == 0x1 ? 0x8019:0x8009);
                }
                else {
                    Data_PGM_mode.set(site, ((efuseData.get(site)>>i) & 0x1) == 0x1 ? 0x0019:0x0009);
                }
                Data_PGM_addr_1.set(site,Data_PGM_mode.get(site) | (((i<<4) | efuseAddr)<<6));
                Data_PGM_addr_2.set(site,Data_PGM_mode.get(site) | ((((i+16)<<4) | efuseAddr)<<6));
                Data_PGM_bit_efuse_open_1.set(site,0x20 | Data_PGM_addr_1.get(site));
                Data_PGM_bit_efuse_close_1.set(site,Data_PGM_bit_efuse_open_1.get(site) & 0xffdf);
                Data_PGM_bit_efuse_open_2.set(site,0x20 | Data_PGM_addr_2.get(site));
                Data_PGM_bit_efuse_close_2.set(site,Data_PGM_bit_efuse_open_2.get(site) & 0xffdf);
            }
            ProtocolAccess.setDynamicData("dynWrite_Data_PGM_mode"+i,Data_PGM_mode );
            ProtocolAccess.setDynamicData("dynWrite_Data_PGM_addr1_"+i,Data_PGM_addr_1 );
            ProtocolAccess.setDynamicData("dynWrite_PGM_bit_efuse_open1_"+i,Data_PGM_bit_efuse_open_1 );
            ProtocolAccess.setDynamicData("dynWrite_PGM_bit_efuse_close1_"+i,Data_PGM_bit_efuse_close_1 );
            ProtocolAccess.setDynamicData("dynWrite_PGM_addr2_"+i,Data_PGM_addr_2 );
            ProtocolAccess.setDynamicData("dynWrite_PGM_bit_efuse_open2_"+i,Data_PGM_bit_efuse_open_2 );
            ProtocolAccess.setDynamicData("dynWrite_PGM_bit_efuse_close2_"+i,Data_PGM_bit_efuse_close_2 );
        }
        ProtocolAccess.updateDynamicData(context, measurement);
        measurement.execute();

    }




}
