package besLib.pa;

import com.advantest.itee.tmapi.protocolaccess.DataField;
import com.advantest.itee.tmapi.protocolaccess.IProtocolSetup;
import com.advantest.itee.tmapi.protocolaccess.OperationType;
import com.advantest.itee.tmapi.protocolaccess.ProtocolInterfaceBase;

import xoc.dsa.DeviceSetupUncheckedException;
import xoc.dsa.IDeviceSetup;
import xoc.dta.measurement.IMeasurement;

/**
 * This class is used to communicated with BES chip based on I2C protocol
 *
 * @version V1.0
 * @author wenju.sun, Weng Yongxin
 **/
public class BesPA_I2C extends ProtocolInterfaceBase{

    private IProtocolSetup protSetup;
    private String SCL="I2C_SCL";
    private String SDA="I2C_SDA";
    private String slaveAddressName;
    private long slaveAddress;
    private int slaveAddressBits;
    private int  pageAddr;

    public static enum I2cRegAddrBits{
        RegAddr_8Bits,
        RegAddr_16Bits,
        RegAddr_32Bits,
    }
    public enum I2CReadBitSize{
        x16,
        x32
    }
    public enum I2CWriteBitSize{
        X8,
        x16,
        x32,
    }

    public static enum BesI2cAddrType{
        Charger,
        PMU,
        PMUIntern,
        RF,
        ANA,
        CDC,
        RC,
        XTAL,
        EMMC,
        PCIE,
        USB,
        WIFI,
        PSRAM0,
        PSRAM1,
        DIGITAL,
    }

    private static int  pageAddr_Charger    =0x60,
                        pageAddr_PMU        =0x60,
                        pageAddr_PMUIntern  =0x60,
                        pageAddr_RF         =0x60, //00
                        pageAddr_ANA        =0x60,
                        pageAddr_CDC        =0x60,
                        pageAddr_RC         =0x60,
                        pageAddr_XTAL       =0x60,
                        pageAddr_EMMC       =0x60,
                        pageAddr_PCIE       =0x60,
                        pageAddr_USB        =0x60,
                        pageAddr_WIFI       =0x60,
                        pageAddr_PSRAM0     =0x60,
                        pageAddr_PSRAM1     =0x60;

    /**
    *
    * @param besI2cAddrType BES I2C device address type in enumeration type
    * @param pageAddress I2C register page address
    */
    public static void setI2CPageAddress(BesI2cAddrType besI2cAddrType, long pageAddress) {
        switch (besI2cAddrType) {
        case Charger:
            pageAddr_Charger=(int)pageAddress;
            break;
        case PMU:
            pageAddr_PMU=(int)pageAddress;
            break;
        case PMUIntern:
            pageAddr_PMUIntern=(int)pageAddress;
            break;
        case RF:
            pageAddr_RF=(int)pageAddress;
            break;
        case ANA:
            pageAddr_ANA=(int)pageAddress;
            break;
        case CDC:
            pageAddr_CDC=(int)pageAddress;
            break;
        case RC:
            pageAddr_RC=(int)pageAddress;
            break;
        case XTAL:
            pageAddr_XTAL=(int)pageAddress;
            break;
        case EMMC:
            pageAddr_EMMC=(int)pageAddress;
            break;
        case PCIE:
            pageAddr_PCIE=(int)pageAddress;
            break;
        case USB:
            pageAddr_USB=(int)pageAddress;
            break;
        case WIFI:
            pageAddr_WIFI=(int)pageAddress;
            break;
        case PSRAM0:
            pageAddr_PSRAM0=(int)pageAddress;
            break;
        case PSRAM1:
            pageAddr_PSRAM1=(int)pageAddress;
            break;

        default:
            break;
        }
    }



    /**
     * Constructor <br>
     *
     * @param ds An instance of IDeviceSetup interface.
     * @param measurement An instance of IMeasurement interface.
     * @param I2cRegAddrBits BES I2C device address type in enumeration type
     */
    public BesPA_I2C(IDeviceSetup ds, IMeasurement measurement ,I2cRegAddrBits I2cRegAddrBits) {
        switch (I2cRegAddrBits) {
        case RegAddr_8Bits:
            this.slaveAddressBits=8;
            break;
        case RegAddr_16Bits:
            this.slaveAddressBits=16;
            break;
        case RegAddr_32Bits:
            this.slaveAddressBits=32;
            break;
        default:
            this.slaveAddressBits=8;
            break;
        }

        super.createSetup(ds, measurement);
        protSetup = getProtocolSetup();
    }

    /**
     * Specifies signals for the signal roles of the I2C Protocol Interface.
     *
     * @param SCL  Signal for the role SCL
     * @param SDA  Signal for the role SDA
     */
    public void setSignals(String SCL, String SDA) {
        if(SCL != null){
            this.SCL = SCL;
        }
        else {
            throw new DeviceSetupUncheckedException("Signal Role 'SCL' is a mandatory signal role. It cannot be set to null.");
        }
        if(SDA != null){
            this.SDA = SDA;
        }
        else {
            throw new DeviceSetupUncheckedException("Signal Role 'SDA' is a mandatory signal role. It cannot be set to null.");
        }
    }


    /**
    * Specified BES i2c device address
    *
    * @param BesI2cAddrType BES i2c device address type in enumeration type
    */
   private void setBesI2cAddrType(BesI2cAddrType BesI2cAddrType) {
       switch (BesI2cAddrType) {
       case Charger:
           this.slaveAddress=0x34;
           this.slaveAddressName="CHarger";
           this.pageAddr=pageAddr_Charger;
           break;
       case PMU:
           this.slaveAddress=0x2e;
           this.slaveAddressName="PMU";
           this.pageAddr=pageAddr_PMU;
           break;
       case PMUIntern:
           this.slaveAddress=0x4e;
           this.slaveAddressName="PMUIntern";
           this.pageAddr=pageAddr_PMUIntern;
           break;
       case RF:
           this.slaveAddress=0x2c;
           this.slaveAddressName="RF";
           this.pageAddr=pageAddr_RF;
           break;
       case ANA:
           this.slaveAddress=0x2a;
           this.slaveAddressName="ANA";
           this.pageAddr=pageAddr_ANA;
           break;
       case CDC:
           this.slaveAddress=0x24;
           this.slaveAddressName="CDC";
           this.pageAddr=pageAddr_CDC;
           break;
       case RC:
           this.slaveAddress=0x38;
           this.slaveAddressName="RC";
           this.pageAddr=pageAddr_RC;
           break;
       case XTAL:
           this.slaveAddress=0x36;
           this.slaveAddressName="XTAL";
           this.pageAddr=pageAddr_XTAL;
           break;
       case EMMC:
           this.slaveAddress=0x32;
           this.slaveAddressName="EMMC";
           this.pageAddr=pageAddr_EMMC;
           break;
       case PCIE:
           this.slaveAddress=0x64;//0x20
           this.slaveAddressName="PCIE";
           this.pageAddr=pageAddr_PCIE;
           break;
       case USB:
           this.slaveAddress=0x30;
           this.slaveAddressName="USB";
           this.pageAddr=pageAddr_USB;
           break;
       case WIFI:
           this.slaveAddress=0x40;
           this.slaveAddressName="WIFI";
           this.pageAddr=pageAddr_WIFI;
           break;
       case PSRAM0:
           this.slaveAddress=0x42;
           this.slaveAddressName="PSRAM0";
           this.pageAddr=pageAddr_PSRAM0;
           break;
       case PSRAM1:
           this.slaveAddress=0x44;
           this.slaveAddressName="PSRAM1";
           this.pageAddr=pageAddr_PSRAM1;
           break;
       case DIGITAL:
           this.slaveAddress=0x22;
           this.slaveAddressName="DIGITAL";
           break;

       default:
           break;
       }
   }


    /**
     * Start BES I2C static write operation.
     *
     * @param besI2cAddrType BES i2c device address type in enumeration type
     * @param address Register address
     * @param data Register data
     *
     */
    public void write(BesI2cAddrType besI2cAddrType, long address, long data) {
        setBesI2cAddrType(besI2cAddrType);//get Device Address and Address Namer
        if(this.slaveAddressName.equals("DIGITAL")) {
            i2cWrite_Addr32_Data32(address, data);
        }
        else {
            if (this.slaveAddressBits==16) {
                i2cWrite_Addr16_Data16(address, data);
            }
            else if (this.slaveAddressBits==8){
                if( (int)((address>>8) & 0xf)!=0  && (int)((address>>8) & 0xf)<5 ) {//turn to pageX (page1-page4)
                    i2cWrite_Addr8_Data16(pageAddr, 0xa000+(0x10*(int)((address>>8)&0xf)) );
                }
                else if((int)((address>>8) & 0xf)!=0) {
                    System.out.println("Attention: current page is" + ((int)((address>>8) & 0xf)) );
                }
                i2cWrite_Addr8_Data16(address, data);
                if( (int)((address>>8) & 0xf)!=0 ) {//restore to page0
                    i2cWrite_Addr8_Data16(pageAddr, 0xa000);
                }
            }
        }
    }

    /**
     * Start BES I2C dynamic write operation.
     *
     * @param besI2cAddrType BES i2c device address type in enumeration type
     * @param address Register address
     * @param dyn_WriteDataID Dynamic write data ID
     *
     */
    public void write(BesI2cAddrType besI2cAddrType, long address, String dyn_WriteDataID) {
        setBesI2cAddrType(besI2cAddrType);//get Device Address and Address Namer
        if(this.slaveAddressName.equals("DIGITAL")) {
            i2cWrite_Addr32_Data32(address,dyn_WriteDataID);
        }
        else {
                if (this.slaveAddressBits==16) {
                    i2cWrite_Addr16_Data16(address, dyn_WriteDataID);
                }
                else if (this.slaveAddressBits==8) {
                    if( (int)((address>>8) & 0xf)!=0  && (int)((address>>8) & 0xf)<5 ) {//turn to pageX (page1-page4)
                        i2cWrite_Addr8_Data16(pageAddr, 0xa000+(0x10*(int)((address>>8)&0xf)) );
                    }
                    else if((int)((address>>8) & 0xf)!=0) {
                        System.out.println("Attention: current page is" + ((int)((address>>8) & 0xf)) );
                    }
                    i2cWrite_Addr8_Data16(address, dyn_WriteDataID);
                    if( (int)((address>>8) & 0xf)!=0 ) {//restore to page0
                        i2cWrite_Addr8_Data16(pageAddr, 0xa000);
                    }
                }
        }
    }

    /**
     * Start BES I2C dynamic write operation.
     *
     * @param besI2cAddrType BES i2c device address type in enumeration type
     * @param dyn_WriteAddrID Dynamic write address ID
     * @param dyn_WriteDataID Dynamic write data ID
     *
     */
    public void write(BesI2cAddrType besI2cAddrType, String dyn_WriteAddrID, String dyn_WriteDataID) {
        setBesI2cAddrType(besI2cAddrType);//get Device Address and Address Namer
        if(this.slaveAddressName.equals("DIGITAL")) {
            i2cWrite_Addr32_Data32(dyn_WriteAddrID,dyn_WriteDataID);
        }
        else {
                if (this.slaveAddressBits==16) {
                    i2cWrite_Addr16_Data16(dyn_WriteAddrID,dyn_WriteDataID);
                }
                else if (this.slaveAddressBits==8) {
                    i2cWrite_Addr8_Data16(dyn_WriteAddrID,dyn_WriteDataID);
                }
        }
    }



    /**
     * Start BES I2C read operation.
     *
     * @param besI2cAddrType BES i2c device address type in enumeration type
     * @param address Register address
     * @param readID ID to retrieve register value
     *
     */
    public void read(BesI2cAddrType besI2cAddrType, long address, String readID) {
        setBesI2cAddrType(besI2cAddrType);//get Device Address and Address Namer

        if(this.slaveAddressName.equals("DIGITAL")) {
            i2cRead_Addr32_Data32(address, readID);
        }
        else {
            if (this.slaveAddressBits==16) {
                i2cRead_Addr16_Data16(address, readID);
            }
            else if (this.slaveAddressBits==8) {
                if( (int)((address>>8) & 0xf)!=0  && (int)((address>>8) & 0xf)<5 ) {//turn to pageX (page1-page4)
                    i2cWrite_Addr8_Data16(pageAddr, 0xa000+(0x10*(int)((address>>8)&0xf)) );
                }
                else if((int)((address>>8) & 0xf)!=0) {
                    System.out.println("Attention: current page is" + ((int)((address>>8) & 0xf)) );
                }
                i2cRead_Addr8_Data16(address, readID);
                if( (int)((address>>8) & 0xf)!=0 ) {//restore to page0
                    i2cWrite_Addr8_Data16(pageAddr, 0xa000);
                }
            }
        }
    }




    /**
     * @param address Register address
     * @param data Register value
     */
    private void i2cWrite_Addr8_Data16(long address,long data) {
        protSetup.vectorBlock(this.slaveAddressName+"_WRITE_0x"+Long.toHexString(address)+"_0x"+Long.toHexString(data))
        .signal(SCL).stateChar("1111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 18)
                    .stateChar("01111")
        .signal(SDA).stateChar("1100")                                                                      //start
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8) )  .stateChar("X")   //device address +ACK
                    .stateChar( new BESDataFormat_I2C(address,I2CWriteBitSize.X8)      )  .stateChar("X")   //register address +ACK
                    .stateChar( new BESDataFormat_I2C(data,I2CWriteBitSize.x16)        )  .stateChar("X")   //register data[15:8] +ACK + register data[7:0] +ACK
                    .stateChar("00111");                                                                    //stop
    }

    /**
     * @param address Register address
     * @param initData Register initial value
     * @param dynamicWriteID Dynamic write ID
     */
    private void i2cWrite_Addr8_Data16(long address,String dyn_WriteDataID) {
        protSetup.vectorBlock(this.slaveAddressName+"_DynamicWRITE_0x"+Long.toHexString(address))
        .signal(SCL).stateChar("1111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 18)
                    .stateChar("01111")
        .signal(SDA).stateChar("1100")                                                                             //start
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8)         )   .stateChar("X") //device address +ACK
                    .stateChar( new BESDataFormat_I2C(address,I2CWriteBitSize.X8)              )   .stateChar("X") //register address +ACK
                    .stateChar( new BESDataFormat_I2C(0x0,I2CWriteBitSize.x16,dyn_WriteDataID) )   .stateChar("X") //register data[15:8] +ACK + register data[7:0] +ACK
                    .stateChar("00111");                                                                           //stop
    }

    /**
     * @param initAddress Register initial address
     * @param initData Register initial value
     * @param dyn_WriteAddrID Dynamic write ID
     * @param dyn_WriteDataID Dynamic write ID
     */
    private void i2cWrite_Addr8_Data16(String dyn_WriteAddrID, String dyn_WriteDataID) {
        protSetup.vectorBlock(this.slaveAddressName+"_DynamicWRITE")
        .signal(SCL).stateChar("1111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 18)
                    .stateChar("01111")
        .signal(SDA).stateChar("1100")                                                                        //start
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8)        ).stateChar("X")//device address +ACK
                    .stateChar( new BESDataFormat_I2C(0x0,I2CWriteBitSize.X8,dyn_WriteAddrID) ).stateChar("X")//register address +ACK
                    .stateChar( new BESDataFormat_I2C(0x0,I2CWriteBitSize.x16,dyn_WriteDataID)).stateChar("X")//register data[15:8] +ACK + register data[7:0] +ACK
                    .stateChar("00111");                                                                      //stop
    }

    /**
     * @param address Register address
     * @param data Register value
     */
    private void i2cWrite_Addr16_Data16(long address,long data) {
        protSetup.vectorBlock(this.slaveAddressName+"_WRITE_0x"+Long.toHexString(address)+"_0x"+Long.toHexString(data))
        .signal(SCL).stateChar("1111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 18)
                    .stateCharRepeat("a", 18)
                    .stateChar("01111")
        .signal(SDA).stateChar("1100")                                                                    //start
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8) )  .stateChar("X") //device address +ACK
                    .stateChar( new BESDataFormat_I2C(address,I2CWriteBitSize.x16)     )  .stateChar("X") //register address[15:8] +ACK + register address[7:0] +ACK
                    .stateChar( new BESDataFormat_I2C(data,I2CWriteBitSize.x16)        )  .stateChar("X") //register data[15:8] +ACK + register data[7:0] +ACK
                    .stateChar("00111");                                                                  //stop
    }


    /**
     * @param address Register address
     * @param initData Register initial value
     * @param dynamicWriteID Dynamic write ID
     */
    private void i2cWrite_Addr16_Data16(long address,String dyn_WriteDataID) {
        protSetup.vectorBlock(this.slaveAddressName+"_DynamicWRITE_0x"+Long.toHexString(address))
        .signal(SCL).stateChar("1111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 18)
                    .stateCharRepeat("a", 18)
                    .stateChar("01111")
        .signal(SDA).stateChar("1100")                                                                             //start
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8)         )   .stateChar("X") //device address +ACK
                    .stateChar( new BESDataFormat_I2C(address,I2CWriteBitSize.x16)             )   .stateChar("X") //register address[15:8] +ACK + register address[7:0] +ACK
                    .stateChar( new BESDataFormat_I2C(0x0,I2CWriteBitSize.x16,dyn_WriteDataID) )   .stateChar("X") //register data[15:8] +ACK + register data[7:0] +ACK
                    .stateChar("00111");                                                                           //stop
    }

    /**
     * @param address Register address
     * @param initData Register initial value
     * @param dyn_WriteAddrID Dynamic write Address ID
     * @param dyn_WriteDataID Dynamic write Data ID
     */
    private void i2cWrite_Addr16_Data16(String dyn_WriteAddrID, String dyn_WriteDataID) {
        protSetup.vectorBlock(this.slaveAddressName+"_DynamicWRITE")
        .signal(SCL).stateChar("1111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 18)
                    .stateCharRepeat("a", 18)
                    .stateChar("01111")
        .signal(SDA).stateChar("1100")                                                                         //start
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8)        ).stateChar("X") //device address +ACK
                    .stateChar( new BESDataFormat_I2C(0x0,I2CWriteBitSize.x16,dyn_WriteAddrID)).stateChar("X") //register address[15:8] +ACK + register address[7:0] +ACK
                    .stateChar( new BESDataFormat_I2C(0x0,I2CWriteBitSize.x16,dyn_WriteDataID)).stateChar("X") //register data[15:8] +ACK + register data[7:0] +ACK
                    .stateChar("00111");                                                                       //stop
    }


    /**
     * @param address Register address
     * @param data Register value
     */
    private void i2cWrite_Addr32_Data32(long address,long data) {
        protSetup.vectorBlock(this.slaveAddressName+"_WRITE_0x"+Long.toHexString(address)+"_0x"+Long.toHexString(data))
        .signal(SCL).stateChar("1111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 36)
                    .stateChar("0111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 36)
                    .stateChar("01111")
        .signal(SDA).stateChar("1100")                                                                      //start
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8) )  .stateChar("X")   //device address +ACK
                    .stateChar( new BESDataFormat_I2C(address,I2CWriteBitSize.x32)     )  .stateChar("X")   //register address[31:24] +ACK + register address[23:16] +ACK+ register address[15:8] +ACK + register address[7:0] +ACK
                    .stateChar("1100")                                                                      //restart
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8) )  .stateChar("X")   //device address +ACK
                    .stateChar( new BESDataFormat_I2C(data,I2CWriteBitSize.x32)        )  .stateChar("X")   //register data[31:24] +ACK + register data[23:16] +ACK+ register data[15:8] +ACK + register data[7:0] +ACK
                    .stateChar("00111");                                                                    //start Condition
    }

    /**
     * @param address Register address
     * @param initData Register initial value
     * @param dynamicWriteID Dynamic write ID
     */
    private void i2cWrite_Addr32_Data32(long address ,String dyn_WriteDataID) {
        protSetup.vectorBlock(this.slaveAddressName+"_DynamicWRITE_0x"+Long.toHexString(address))
        .signal(SCL).stateChar("1111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 36)
                    .stateChar("0111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 36)
                    .stateChar("01111")
        .signal(SDA).stateChar("1100")                                                                          //start
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8)        )  .stateChar("X")//device address +ACK
                    .stateChar( new BESDataFormat_I2C(address,I2CWriteBitSize.x32)            )  .stateChar("X")//register address[31:24] +ACK + register address[23:16] +ACK+ register address[15:8] +ACK + register address[7:0] +ACK
                    .stateChar("1100")                                                                          //restart
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8)        )  .stateChar("X")//device address +ACK
                    .stateChar( new BESDataFormat_I2C(0x0,I2CWriteBitSize.x32,dyn_WriteDataID))  .stateChar("X")//register data[31:24] +ACK + register data[23:16] +ACK+ register data[15:8] +ACK + register data[7:0] +ACK
                    .stateChar("00111");                                                                        //start Condition
    }

    /**
     * @param address Register address
     * @param initData Register initial value
     * @param dyn_WriteAddrID Dynamic write Address ID
     * @param dyn_WriteDataID Dynamic write Data ID
     */
    private void i2cWrite_Addr32_Data32(String dyn_WriteAddrID,String dyn_WriteDataID) {
        protSetup.vectorBlock(this.slaveAddressName+"_DynamicWRITE")
        .signal(SCL).stateChar("1111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 36)
                    .stateChar("0111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 36)
                    .stateChar("01111")
        .signal(SDA).stateChar("1100")                                                                            //start
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8)        )  .stateChar("X")  //device address +ACK
                    .stateChar( new BESDataFormat_I2C(0x0,I2CWriteBitSize.x32,dyn_WriteAddrID))  .stateChar("X")  //register address[31:24] +ACK + register address[23:16] +ACK+ register address[15:8] +ACK + register address[7:0] +ACK
                    .stateChar("1100")                                                                            //restart
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8)        )  .stateChar("X")  //device address +ACK
                    .stateChar( new BESDataFormat_I2C(0x0,I2CWriteBitSize.x32,dyn_WriteDataID))  .stateChar("X")  //register data[31:24] +ACK + register data[23:16] +ACK+ register data[15:8] +ACK + register data[7:0] +ACK
                    .stateChar("00111");                                                                          //start Condition
    }

    /**
     * @param address Register address
     * @param readID ID to retrieve register value
     */
    private void i2cRead_Addr8_Data16(long address,String readID ) {
       protSetup.vectorBlock(this.slaveAddressName+"_READ_0x"+Long.toHexString(address))
       .signal(SCL).stateChar("1111")
                   .stateCharRepeat("a", 9)
                   .stateCharRepeat("a", 9)
                   .stateChar("0111")
                   .stateCharRepeat("a", 9)
                   .stateCharRepeat("a", 18)
                   .stateChar("0111")
       .signal(SDA).stateChar("1100")                                                                     //start
                   .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8) )   .stateChar("X") //device address +ACK
                   .stateChar( new BESDataFormat_I2C(address,I2CWriteBitSize.X8)      )   .stateChar("X") //register address[7:0] +ACK
                   .stateChar("1100")                                                                     //restart
                   .stateChar( new BESDataFormat_I2C(slaveAddress+1,I2CWriteBitSize.X8) )   .stateChar("X") //device address +ACK
                   .stateChar( new BESDataFormat_I2C(readID,I2CReadBitSize.x16)       )   .stateChar("1") //read data[0:15]
                   .stateChar("0011");                                                                    //stop
    }

    /**
     * @param address Register address
     * @param readID ID to retrieve register value
     */
    private void i2cRead_Addr16_Data16(long address,String readID ) {
        protSetup.vectorBlock(this.slaveAddressName+"_READ_0x"+Long.toHexString(address))
        .signal(SCL).stateChar("1111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 18)
                    .stateChar("0111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 18)
                    .stateChar("0111")
        .signal(SDA).stateChar("1100")                                                                  //start
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8)).stateChar("X")  //device address +ACK
                    .stateChar( new BESDataFormat_I2C(address,I2CWriteBitSize.x16)    ).stateChar("X")  //register address[15:8] +ACK + register address[7:0] +ACK
                    .stateChar("1100")                                                                  //restart
                    .stateChar( new BESDataFormat_I2C(slaveAddress+1,I2CWriteBitSize.X8)).stateChar("X")  //device address +ACK
                    .stateChar( new BESDataFormat_I2C(readID,I2CReadBitSize.x16)      ).stateChar("1")  //read data[0:15]
                    .stateChar("0011");                                                                 //stop
    }

    /**
     * @param address Register address
     * @param readID ID to retrieve register value
     */
    private void i2cRead_Addr32_Data32(long address,String readID ) {
        protSetup.vectorBlock(this.slaveAddressName+"_READ_0x"+Long.toHexString(address))
        .signal(SCL).stateChar("1111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 36)
                    .stateChar("0111")
                    .stateCharRepeat("a", 9)
                    .stateCharRepeat("a", 36)
                    .stateChar("0111")
        .signal(SDA).stateChar("1100")                                                                      //start
                    .stateChar( new BESDataFormat_I2C(slaveAddress,I2CWriteBitSize.X8))     .stateChar("X") //device address +ACK
                    .stateChar( new BESDataFormat_I2C(address,I2CWriteBitSize.x32)    )     .stateChar("X") //register address[31:24] +ACK + register address[23:16] +ACK+ register address[15:8] +ACK + register address[7:0] +ACK
                    .stateChar("1100")                                                                      //restart
                    .stateChar( new BESDataFormat_I2C(slaveAddress+1,I2CWriteBitSize.X8))   .stateChar("X") //device address +ACK
                    .stateChar( new BESDataFormat_I2C(readID,I2CReadBitSize.x32)      )     .stateChar("1") //read data[0:31]
                    .stateChar("0011");                                                                     //stop
    }




    /**
     * This class is used to generate or retrieve I2C data in specified bit order.
     * @version 1.0
     * @author Weng Yongxin
     */
    private class BESDataFormat_I2C extends DataField{
        private int dataBitSize;
        private String dataDirection;

        /**
         * Constructor <br>
         * For I2C static write
         *
         * @param data I2C Write data
         * @param I2CWriteBitSize Bit size of BES I2C data
         */
        protected BESDataFormat_I2C(long data, I2CWriteBitSize I2CWriteBitSize) {
            super(OperationType.WRITE);
            this.setData(data);
            //get data direction
            this.dataDirection="WRITE";
            //get data bit size
            int tempDataBitSize=0;
            if(I2CWriteBitSize.toString().length()==2) {//X8
                tempDataBitSize=I2CWriteBitSize.toString().charAt(1)-0x30;
            }
            else if(I2CWriteBitSize.toString().length()==3) {//X16, X32
                for(int i=0;i<I2CWriteBitSize.toString().length()-1;i++) {
                    tempDataBitSize+=(I2CWriteBitSize.toString().charAt(i+1)-0x30)*Math.pow(10.0, I2CWriteBitSize.toString().length()-2-i);
                }
            }
            this.dataBitSize=tempDataBitSize;
        }

        /**
         * Constructor <br>
         * For I2C dynamic write
         *
         * @param initData Initial data of I2C dynamic write
         * @param I2CWriteBitSize Bit size of BES I2C data
         * @param dynamicWriteID Dynamic write ID
         */
        protected BESDataFormat_I2C(long initData, I2CWriteBitSize I2CWriteBitSize,String dynamicWriteID) {
            super(OperationType.DYNAMIC_WRITE);
            this.setData(initData);
            super.setId(dynamicWriteID);
            //get data direction
            this.dataDirection="WRITE";
            //get data bit size
            int tempDataBitSize=0;
            if(I2CWriteBitSize.toString().length()==2) {//X8
                tempDataBitSize=I2CWriteBitSize.toString().charAt(1)-0x30;
            }
            else if(I2CWriteBitSize.toString().length()==3) {//X16, X32
                for(int i=0;i<I2CWriteBitSize.toString().length()-1;i++) {
                    tempDataBitSize+=(I2CWriteBitSize.toString().charAt(i+1)-0x30)*Math.pow(10.0, I2CWriteBitSize.toString().length()-2-i);
                }
            }
            this.dataBitSize=tempDataBitSize;
        }
        /**
         * Constructor <br>
         * For I2C read
         *
         * @param readID Read ID
         * @param I2CWriteBitSize Bit size of BES I2C data
         */
        protected BESDataFormat_I2C(String readID,I2CReadBitSize I2CReadBitSize) {
            super(OperationType.READ);
            super.setId(readID);
            //get data direction
            this.dataDirection="READ";
            //get data bit size
            int tempDataBitSize=0;
            if(I2CReadBitSize.toString().length()==2) {//X8
                tempDataBitSize=I2CReadBitSize.toString().charAt(1)-0x30;
            }
            else if(I2CReadBitSize.toString().length()==3) {//X16, X32
                for(int i=0;i<I2CReadBitSize.toString().length()-1;i++) {
                    tempDataBitSize+=(I2CReadBitSize.toString().charAt(i+1)-0x30)*Math.pow(10.0, I2CReadBitSize.toString().length()-2-i);
                }
            }
            this.dataBitSize=tempDataBitSize;
        }

        /**
         * Specified data bit order
         *
         * @return return data bit order in String type
         */
        @Override
        protected String getDataLayout() {
            switch (this.dataBitSize) {
            case 8:
                    return "[7-0]"; //only for case write device address
            case 16:
                if(this.dataDirection.equals("WRITE")) {
                    return "[15-8]X{1}[7-0]"; //for write data bit layout
                }
                return "[15-8]0{1}[7-0]"; //for read data bit layout
            case 32:
                if(this.dataDirection.equals("WRITE")) {
                    return "[31-24]X{1}[23-16]X{1}[15-8]X{1}[7-0]"; //for write data bit layout
                }
                return "[31-24]0{1}[23-16]0{1}[15-8]0{1}[7-0]"; //for read data bit layout

            default:
                new Exception("Warning, dataBitSize is set illegally in BES I2C protocol !!!").printStackTrace();
                if(this.dataDirection.equals("WRITE")) {
                    return "[7-0]"; //for write data bit layout
                }
                return "[15-8]0{1}[7-0]"; //for read data bit layout
            }
        }
    }


}
