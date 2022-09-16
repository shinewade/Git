package besLib.pa;

import com.advantest.itee.tmapi.protocolaccess.DataField;
import com.advantest.itee.tmapi.protocolaccess.IProtocolSetup;
import com.advantest.itee.tmapi.protocolaccess.OperationType;
import com.advantest.itee.tmapi.protocolaccess.ProtocolInterfaceBase;

import besLib.cal.BesCalc_General;
import xoc.dsa.DeviceSetupUncheckedException;
import xoc.dsa.IDeviceSetup;
import xoc.dta.measurement.IMeasurement;

/**
 * This class is used to write data to BES chip based on UART protocol (only for UART write !!!)
 *
 * @version V1.0
 * @author wenju.sun, Weng Yongxin
 **/
public class BesPA_UART extends ProtocolInterfaceBase{

    private IProtocolSetup protSetup;
    private String TX="UART_TX";
    private String RX="UART_RX";
//    private double baud=921600;
    private int dataFrameBitsSize=8;
    private String parityBits="None";
    private int stopBits=1;

    /**
     * 5 up to 8 bits with no parity bit. <br>
     * 9 bits long with no parity bit. <br>
     * Data is usually sent with the least significant bit first
     *
     */
    private enum DataFrameBitsSize{
        X5,
        X6,
        X7,
        X8,
        X9,
    }

    /**
     * 0, even parity
     * 1, odd parity
     *
     */
    public enum ParityBits{
        Even,
        Odd,
        None,
    }


    private enum StopBits{
        X1,
//        X1p5,
        X2,
    }

    private enum DataDirection{
        IN,
        OUT,
    }

    /**
     * Constructor <br>
     *
     * @param ds An instance of IDeviceSetup interface.
     * @param measurement An instance of IMeasurement interface.
     */
    public BesPA_UART(IDeviceSetup ds, IMeasurement measurement) {
        super.createSetup(ds, measurement);
        protSetup = getProtocolSetup();
    }

    /**
     * Specifies signals for the signal roles of the I2C Protocol Interface.
     *
     * @param TX  Signal for the role TX
     * @param RX  Signal for the role RX
     */
    public void setSignals(String TX, String RX) {
        if(TX != null){
            this.TX = TX;
        }
        else {
            throw new DeviceSetupUncheckedException("Signal Role 'TX' is a mandatory signal role. It cannot be set to null.");
        }
        if(RX != null){
            this.RX = RX;
        }
        else {
            throw new DeviceSetupUncheckedException("Signal Role 'RX' is a mandatory signal role. It cannot be set to null.");
        }
    }

    /**
     *
     * @param dataFrameBitsSize Data frame bits size type
     * @param parityBits Parity bits type
     * @param stopBits UART stop bit type
     */
    public void setUARTMode(DataFrameBitsSize dataFrameBitsSize,ParityBits parityBits,StopBits stopBits) {
        switch (dataFrameBitsSize) {
        case X5:
            this.dataFrameBitsSize=5;
            break;
        case X6:
            this.dataFrameBitsSize=6;
            break;
        case X7:
            this.dataFrameBitsSize=7;
            break;
        case X8:
            this.dataFrameBitsSize=8;
            break;
        case X9:
            this.dataFrameBitsSize=9;
            break;
        default:
            this.dataFrameBitsSize=7;
            break;
        }

        switch (parityBits) {
        case Even:
            this.parityBits="Even";
            break;
        case Odd:
            this.parityBits="Odd";
            break;
        case None:
            this.parityBits="None";
            break;
        default:
            this.parityBits="None";
            break;
        }

        switch (stopBits) {
        case X1:
            this.stopBits=1;
            break;
        case X2:
            this.stopBits=2;
            break;
        default:
            this.stopBits=1;
            break;
        }
    }

    /**
     * UART write operation
     *
     * @param data data to be written by UART protocol
     */
    public void write(long data) {
        protSetup.vectorBlock("UART_WRITE_0x"+Long.toHexString(data))
        .signal(TX) .stateCharRepeat("1", 3+new DataFormat_UART(data, dataFrameBitsSize, DataDirection.IN, this.parityBits).getDataFrameBitsSize()+this.stopBits)
        .signal(RX) .stateChar("1")                                                                             //idle bit
                    .stateChar("0")                                                                             //start bit
                    .stateChar(new DataFormat_UART(data, dataFrameBitsSize, DataDirection.IN, this.parityBits)) //data frame with optional parity bit
                    .stateCharRepeat("1", this.stopBits)                                                        //stop bit
                    .stateChar("1");                                                                            //idle bit
    }


    /**
     * This class is used to generate or retrieve I2C data in specified bit order.
     * @version 1.0
     * @author Weng Yongxin
     */
    private class DataFormat_UART extends DataField{
        private int dataFrameBitsSize1=8;
        private boolean isWriteMode=true;
        private String ParityBitsMode="None";

        /**
         * Constructor <br>
         * For UART write
         *
         * @param data UART data
         * @param dataFrameBitsSize data frame bit size
         * @param DataDirection Data direction mode IN or OUT
         * @param parityBits  Even,Odd or None in String type
         */
        protected DataFormat_UART(long data, int dataFrameBitsSize, DataDirection dataDirection, String parityBits) {
            //data operation type
            super( dataDirection.toString().equals("IN")?(OperationType.WRITE):(OperationType.READ));
            //error exception
            if(this.dataFrameBitsSize1==9) {
                throw new DeviceSetupUncheckedException("UART format is illegal, data frame with 9 bits has no parity bit !!!");
            }
            this.dataFrameBitsSize1=dataFrameBitsSize;
            this.ParityBitsMode=parityBits.toString();
            //recalculate data frame
            long dataFinal=0;
            switch (parityBits) {
            case "Even":
                if(isWriteMode==true) {
                    dataFinal=(dataFinal<<1)+(BesCalc_General.getParityBit(data, dataFrameBitsSize, ParityBits.Even)&0x1);
                    this.dataFrameBitsSize1++;
                }
                else {
                    dataFinal=data;
                }
                break;
            case "Odd":
                if(isWriteMode==true) {
                    dataFinal=(dataFinal<<1)+(BesCalc_General.getParityBit(data, dataFrameBitsSize, ParityBits.Odd)&0x1);
                    this.dataFrameBitsSize1++;
                }
                else {
                    dataFinal=data;
                }
                break;
            case "None":
                dataFinal=data;
                break;
            default:
                dataFinal=data;
                break;
            }
            //set data
            this.setData(dataFinal);
        }

        /**
         * Specified UART write & read data bit order
         *
         * @return return UART data bit order in String type
         */
        @Override
        protected String getDataLayout() {
            //write with parity bit
            if(this.isWriteMode==true && this.ParityBitsMode.equals("None")==false) {
                switch (this.dataFrameBitsSize1) {
                case 6:
                    return "[0-5]";
                case 7:
                    return "[0-6]";
                case 8:
                    return "[0-7]";
                case 9:
                    return "[0-8]";
                case 10:
                    return "[0-9]";
                default:
                    new Exception("Warning, UART data frame bit size is set illegally in UART protocol !!!").printStackTrace();
                    return "[0-8]";
                }
            }
            //write with no parity bit or read
            switch (this.dataFrameBitsSize1) {
            case 5:
                    return "[0-4]";
            case 6:
                return "[0-5]";
            case 7:
                return "[0-6]";
            case 8:
                return "[0-7]";
            case 9:
                return "[0-8]";
            default:
                new Exception("Warning, UART data frame bit size is set illegally in UART protocol !!!").printStackTrace();
                return "[0-7]";
            }
        }

        /**
         * get data frame bit size
         *
         * @return data frame bit size in int type
         */
        public int getDataFrameBitsSize() {
            return this.dataFrameBitsSize1;

        }
    }


}
