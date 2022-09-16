package besLib.cal;

import java.util.ArrayList;

/**
 * This class is used to calculate universal CRC
 * @version V1.0
 * @author Weng Yongxin
 **/
public class BesCalc_CRC {
    /**
     * Enum type for property of CRC checksum model.
     */
    public static enum crcMode{
        CRC4_ITU,
        CRC5_EPC,
        CRC5_ITU,
        CRC5_USB,
        CRC6_ITU,
        CRC7_MMC,
        CRC8,
        CRC8_ITU,
        CRC8_ROHC,
        CRC8_MAXIM,
        CRC16_IBM,
        CRC16_MAXIM,
        CRC16_USB,
        CRC16_MODBUS,
        CRC16_CCITT,
        CRC16_CCITT_FALSE,
        CRC16_X25,
        CRC16_XMODEM,
        CRC16_DNP,
        CRC32,
        CRC32_MPEG_2,
    }

    /**
     * Calculate CRC checksum.<br>
     * @version 1.0
     * @param rawData Data to be used to calculate CRC checksum in ArrayList type
     * @param crcMode CRC mode
    *        <br>The options are: <i>CRC4_ITU,CRC8,CRC16_CCITT,CRC32,</i>...  <br>
     * @return CRC value in long type
     */
     public static long getCRC(ArrayList<Long> rawData, crcMode crcMode) {
         long crc=0x0L;
         long poly=0x0L;
         long gx=0x0L;
         long data=0L;
         long crcXOROUT=0x0L;
         int  gxLen=0;
         int dataLen=8;
         boolean refIN=false;
         boolean refOUT=false;

         switch (crcMode) {
             case CRC4_ITU:
                 poly=0x13L;
                 crc=0x0L;
                 crcXOROUT=0x0L;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC5_EPC:
                 poly=0x29L;
                 crc=0x09L;
                 crcXOROUT=0x0L;
                 refIN=false;
                 refOUT=false;
                 break;
             case CRC5_ITU:
                 poly=0x35L;
                 crc=0x0L;
                 crcXOROUT=0x0L;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC5_USB:
                 poly=0x25L;
                 crc=0x1fL;
                 crcXOROUT=0x1fL;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC6_ITU:
                 poly=0x43L;
                 crc=0x0L;
                 crcXOROUT=0x0L;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC7_MMC:
                 poly=0x89L;
                 crc=0x0L;
                 crcXOROUT=0x0L;
                 refIN=false;
                 refOUT=false;
                 break;
             case CRC8:
                 poly=0x107L;
                 crc=0x0L;
                 crcXOROUT=0x0L;
                 refIN=false;
                 refOUT=false;
                 break;
             case CRC8_ITU:
                 poly=0x107L;
                 crc=0x0L;
                 crcXOROUT=0x55L;
                 refIN=false;
                 refOUT=false;
                 break;
             case CRC8_ROHC:
                 poly=0x107L;
                 crc=0xffL;
                 crcXOROUT=0x0L;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC8_MAXIM:
                 poly=0x131L;
                 crc=0x0L;
                 crcXOROUT=0x0L;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC16_IBM:
                 poly=0x18005L;
                 crc=0x0L;
                 crcXOROUT=0x0L;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC16_MAXIM:
                 poly=0x18005L;
                 crc=0x0L;
                 crcXOROUT=0xffffL;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC16_USB:
                 poly=0x18005L;
                 crc=0xffffL;
                 crcXOROUT=0xffffL;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC16_MODBUS:
                 poly=0x18005L;
                 crc=0xffffL;
                 crcXOROUT=0x0L;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC16_CCITT:
                 poly=0x11021L;
                 crc=0x0L;
                 crcXOROUT=0x0L;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC16_CCITT_FALSE:
                 poly=0x11021L;
                 crc=0xffffL;
                 crcXOROUT=0x0L;
                 refIN=false;
                 refOUT=false;
                 break;
             case CRC16_X25:
                 poly=0x11021L;
                 crc=0xffffL;
                 crcXOROUT=0xffffL;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC16_XMODEM:
                 poly=0x11021L;
                 crc=0x0L;
                 crcXOROUT=0x0L;
                 refIN=false;
                 refOUT=false;
                 break;
             case CRC16_DNP:
                 poly=0x13d65L;
                 crc=0x0L;
                 crcXOROUT=0xffffL;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC32:
                 poly=0x104c11db7L;
                 crc=0xffffffffL;
                 crcXOROUT=0xffffffffL;
                 refIN=true;
                 refOUT=true;
                 break;
             case CRC32_MPEG_2:
                 poly=0x104c11db7L;
                 crc=0xffffffffL;
                 crcXOROUT=0x0L;
                 refIN=false;
                 refOUT=false;
                 break;
         default:
             break;
         }

         for(int i=0; i<rawData.size();i++) {
             gx=poly; //CRC32
             gxLen=Long.toBinaryString(gx).length();

             //judge if reverse the byte
             if(refIN==true) {
                 data=(Long.reverse(rawData.get(i))>>(64-8) & 0xff);
             }
             else {
                 data=rawData.get(i);
             }
             // calculate (data^CRC_INIT)<<gxLen
             if((gxLen-1)<=dataLen) {
                 data=((crc<<(dataLen-(gxLen-1)))^data)<<(gxLen-1);
             }
             else {
                 data=((data<<((gxLen-1)-dataLen))^crc)<<dataLen;
             }
             // keep bit size of gx and data is same
             gx=gx<<(dataLen-1);
             // calculate CRC value
             for(int j=0;j<dataLen;j++) {
                 if( (data & (0x1L<<(gxLen-1+dataLen-1)))!=0) {//judge if the highest bit is 1 or 0
                     data=data^gx;
                     data=data<<1;
                 }
                 else {
                     data=data<<1;
                 }
             }
             crc=data>>dataLen; //get crc value
         }
         //judge if reverse the byte
         if(refOUT==true) {
             crc=( Long.reverse(crc)>>>(64-(gxLen-1)) );
         }
         return crc^crcXOROUT;
     }

     /**
      * Calculate CRC checksum.<br>
      * ********************************** <br>
      * ** Pay attention to the data type! ** <br>
      * ********************************** <br>
      * The default data type of java is 'int'. If value 'crcInit' has more than 32 bits , remember to add the suffix 'L'
      * @version 1.0
      * @param rawData Data to be used to calculate CRC checksum in ArrayList type
      * @param poly CRC Polynomial
      * @param crcInit CRC register initial value in long or hex type.
      * @param crcXOROUT crcXOROUT XOR calculated result to get final CRC
      * @param refIN Flip per bit of data which to be tested
      * @param refOUT Flip per bit of data which is after calculation and before XOR output
      * @return CRC value in long type
      */
      public static long getCRC(ArrayList<Long> rawData,long poly,long crcInit,long crcXOROUT,boolean refIN,boolean refOUT) {
          long crc=crcInit;
          long gx=0x0L;
          long data=0L;
          int  gxLen=0;
          int dataLen=8;

          for(int i=0; i<rawData.size();i++) {
              gx=poly; //CRC32
              gxLen=Long.toBinaryString(gx).length();

              //judge if reverse the byte
              if(refIN==true) {
                  data=(Long.reverse(rawData.get(i))>>(64-8) & 0xff);
              }
              else {
                  data=rawData.get(i);
              }
              // calculate (data^CRC_INIT)<<gxLen
              if((gxLen-1)<=dataLen) {
                  data=((crc<<(dataLen-(gxLen-1)))^data)<<(gxLen-1);
              }
              else {
                  data=((data<<((gxLen-1)-dataLen))^crc)<<dataLen;
              }
              // keep bit size of gx and data is same
              gx=gx<<(dataLen-1);
              // calculate CRC value
              for(int j=0;j<dataLen;j++) {
                  if( (data & (0x1L<<(gxLen-1+dataLen-1)))!=0) {//judge if the highest bit is 1 or 0
                      data=data^gx;
                      data=data<<1;
                  }
                  else {
                      data=data<<1;
                  }
              }
              crc=data>>dataLen; //get crc value
          }
          //judge if reverse the byte
          if(refOUT==true) {
              crc=( Long.reverse(crc)>>>(64-(gxLen-1)) );
          }
          return crc^crcXOROUT;
      }

}
