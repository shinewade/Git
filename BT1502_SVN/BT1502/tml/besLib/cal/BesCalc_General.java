package besLib.cal;

import java.io.FileReader;
import java.util.ArrayList;

import besLib.pa.BesPA_UART.ParityBits;
import xoc.dsa.DeviceSetupUncheckedException;
import xoc.dta.datatypes.MultiSiteCharacterArray;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.MultiSiteLongArray;
import xoc.dta.datatypes.MultiSiteString;
import xoc.dta.datatypes.dsp.MultiSiteWaveLong;

/**
 * This class is used for general function
 * @version V1.0
 * @author Weng Yongxin
 **/
public class BesCalc_General {
    /**
     * Convert MultiSiteLongArray ASCII data to MultiSiteString.
     * @param dataOutput Output data in MultiSiteLongArray.
     * @return ASCII output in MultiSiteString
     */
    public static MultiSiteString asciiToString(MultiSiteLongArray dataOutput ){
        MultiSiteString stringLog = new MultiSiteString();
        StringBuffer stringBuffer=new StringBuffer();
        char[] charTemp=new char[dataOutput.length()];
        MultiSiteCharacterArray characterArray=new MultiSiteCharacterArray(charTemp);

        for(int site:dataOutput.getActiveSites()) {
            for(int index=0;index<dataOutput.length();index++) {
                char tempChar=(char)(dataOutput.getElement(index).get(site).shortValue());
                characterArray.setElement(site, index,tempChar);
                if(tempChar!=0) {
                    stringBuffer.append(tempChar);
                }
            }
            stringLog.set(site, stringBuffer.toString());
            //avoid meet null character and stop print to console
            if(stringBuffer.length()>0)
            {
                stringBuffer.delete(0, stringBuffer.length()-1);
            }
        }

        return stringLog;
    }

    /**
     * Convert  ASCII data to String.
     * @param dataOutput Output data in long array.
     * @return ASCII output in String
     */
    public static String asciiToString(long[] dataOutput ){
        String stringLog = "";
        StringBuffer stringBuffer=new StringBuffer();
        Character[] characterArray=new Character[dataOutput.length];


        for(int index=0;index<dataOutput.length;index++) {
            char tempChar=(char)((short)dataOutput[index]);
            characterArray[index]=tempChar;
            if(tempChar!=0) {
                stringBuffer.append(tempChar);
            }
        }
        stringLog=stringBuffer.toString();
        //avoid meet null character and stop print to console
        if(stringBuffer.length()>0)
        {
            stringBuffer.delete(0, stringBuffer.length()-1);
        }
        return stringLog;
    }

    /**
     *
     * @param txtFile Qualified name of ASCII file
     * @return StringBuffer which contains output data in string
     */
    @SuppressWarnings({ "resource", "null" })
    public static StringBuffer asciiToString(String txtFile ){

        StringBuffer stringBufferTemp=new StringBuffer();
        StringBuffer stringBuffer=new StringBuffer();

        if(txtFile.contains(".txt")!=true){
            new Exception("ERROR: filename format incorrect!").printStackTrace();
            System.out.println("***************************************");
            System.out.println("** ERROR: filename format incorrect! **");
            System.out.println("***************************************");
         }

         ArrayList<Long> rawDataList = new ArrayList<Long>();
         FileReader fr=null;
         boolean flag=true;
         try {
             fr=new FileReader(txtFile);
             while(flag) {
                 long data=fr.read();
                     if(data!=-1) {
                         if(data==10) { //replace LF with space
                             rawDataList.add((long)32);
                         }
                         else {
                             rawDataList.add(data);
                         }
                     }
                     else {
                         flag=false;
                     }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }finally {
             try {
                 fr.close();
             } catch (Exception e2) {
                 e2.printStackTrace();
             }
         }

         for(int index=0;index<rawDataList.size();index++) {
             char tempChar=(char)(rawDataList.get(index).shortValue());
             if(tempChar!=0) {
                 stringBufferTemp.append(tempChar);
             }
         }

         int length=(stringBufferTemp.toString().split(" ")).length;
         String[] tempStrnig=new String[length];
         tempStrnig=stringBufferTemp.toString().toUpperCase().split(" ");

         for(int i=0;i<length;i++) {
             double hexValueToLong=0;
             for(int j=0;j<2;j++) {//two byte
                 if(tempStrnig[i].toCharArray()[j]>=48 && tempStrnig[i].toCharArray()[j]<=57) {
                     hexValueToLong=hexValueToLong+(tempStrnig[i].toCharArray()[j]-48)*Math.pow(16.0, (1-j)*1.0);
                 }
                 else if(tempStrnig[i].toCharArray()[j]>=65 && tempStrnig[i].toCharArray()[j]<=70) {
                     hexValueToLong=hexValueToLong+(tempStrnig[i].toCharArray()[j]-65+10)*Math.pow(16.0, (1-j)*1.0);
                 }
             }
             if(Long.valueOf((long)hexValueToLong) !=0) {
                 stringBuffer.append( (char)(Long.valueOf((long)hexValueToLong).shortValue()) );
             }
         }
         return stringBuffer;
     }



    /**
    * remove maximum minimum then calculate mean
    * @param rawData_Array Array in MultiSiteLongArray data type
    * @return Data value in MultiSiteLong data type
    */
   public static MultiSiteLong meanCal(MultiSiteLongArray rawData_Array)
   {
       int[] activeSites = rawData_Array.getActiveSites();
       MultiSiteWaveLong Data_Wave_removeMax = new MultiSiteWaveLong();
       MultiSiteWaveLong Data_Wave_removeMin = new MultiSiteWaveLong();
       MultiSiteLong     Data = new MultiSiteLong();
       for(int site:activeSites)
       {
           Data_Wave_removeMax.set(site, rawData_Array.toMultiSiteWave().get(site).removeValues(rawData_Array.toMultiSiteWave().get(site).maxIndex(), 1));
           Data_Wave_removeMin.set(site, Data_Wave_removeMax.get(site).removeValues(Data_Wave_removeMax.get(site).minIndex(),1));

           Data.set(site, (long) Data_Wave_removeMin.get(site).mean());
       }
       return Data;
   }


   /**
    * calculate data odd parity bit or even parity bit
    *
    * @param dataFrame MultiSiteLong data frame which need to be calculated
    * @param dataFrameBitsSize bit size of MultiSiteLong data frame
    * @param parityBits parityBits which need to be specified to calculate odd or even parity bit in enumeration type
    * @return Parity bit in MultiSiteLong data type
    */
   public static MultiSiteLong getParityBit(MultiSiteLong dataFrame, int dataFrameBitsSize, ParityBits parityBits)
   {
       int[] activeSites = dataFrame.getActiveSites();
       boolean isOddCheck=false;
       switch (parityBits) {
       case Even:
            isOddCheck=false;
            break;
       case Odd:
            isOddCheck=true;
            break;
       case None:
           throw new DeviceSetupUncheckedException("Error, this method should not be called when parityBits is set to \"None\"");
        default:
            break;
       }
       //calculate number of bit 1
       MultiSiteLong count=new MultiSiteLong(0);
       for(int site:activeSites) {
           for(int i=0;i<dataFrameBitsSize;i++) {
               if( (dataFrame.get(site).intValue() & 0x1 )==1) {
                   count.set(site, count.get(site)+1);
               }
               dataFrame.set(site, (dataFrame.get(site)) >> 1 );
           }
       }
       //calculate parityBit
       MultiSiteLong parityBit=new MultiSiteLong(0);
       for(int site:activeSites) {
           if( (count.get(site).intValue() & 0x1 ) ==1 ) { //number of bit 1 i odd
               if(isOddCheck) {
                   parityBit.set(site, 0);
               }
               else {
                   parityBit.set(site, 1);
               }
           }
           else { //number of bit 1 i even
               if(isOddCheck) {
                   parityBit.set(site, 1);
               }
               else {
                   parityBit.set(site, 0);
               }
           }
       }
       return parityBit;
   }

   /**
    * calculate data odd parity bit or even parity bit
    *
    * @param dataFrame long data frame which need to be calculated
    * @param dataFrameBitsSize bit size of long data frame
    * @param parityBits parityBits which need to be specified to calculate odd or even parity bit in enumeration type
    * @return Parity bit in long data type
    */
   public static int getParityBit(long dataFrame, int dataFrameBitsSize, ParityBits parityBits)
   {
       boolean isOddCheck=false;
       switch (parityBits) {
       case Even:
            isOddCheck=false;
            break;
       case Odd:
            isOddCheck=true;
            break;
       case None:
           throw new DeviceSetupUncheckedException("Error, this method should not be called when parityBits is set to \"None\"");
        default:
            break;
       }
       //calculate number of bit 1
       int count=0;
       for(int i=0;i<dataFrameBitsSize;i++) {
           if( (dataFrame & 0x1 )==1) {
               count=count+1;
           }
           dataFrame=dataFrame >> 1 ;
       }

       //calculate parityBit
       int parityBit=0;
       if( (count& 0x1 ) ==1 ) { //number of bit 1 i odd
           if(isOddCheck) {
               parityBit= 0;
           }
           else {
               parityBit= 1;
           }
       }
       else { //number of bit 1 i even
           if(isOddCheck) {
               parityBit= 1;
           }
           else {
               parityBit= 0;
           }
       }

       return parityBit;
   }


//   /**
//    * Querying production related data.<br>
//    * @version 1.0
//    * @param testContext This interface enables you to access the test context of a measurement.
//    */
//   public  void getProductionInfo(ITestContext testContext) {
//       MultiSiteString waferX  =testContext.testProgram().variables().getString("STDF.X_COORD");
//       MultiSiteString waferY  =testContext.testProgram().variables().getString("STDF.Y_COORD");
//       MultiSiteString waferID =testContext.testProgram().variables().getString("STDF.WAFER_ID");
//
//       System.out.println("Die_X = "+waferX);
//       System.out.println("Die_Y = "+waferY);
//       System.out.println("Wafer_id = "+waferID);
//
//
//   }


}
