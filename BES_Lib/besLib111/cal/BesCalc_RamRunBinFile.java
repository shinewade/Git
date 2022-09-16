package besLib.cal;

import java.io.FileReader;
import java.util.ArrayList;

import besLib.cal.BesCalc_CRC.crcMode;

/**
 * This class is used to parse BES RamRun binary file and generate specified BES data sequencer with UART writing mode.
 * @version V1.0
 * @author Weng Yongxin
 **/
public class BesCalc_RamRunBinFile {
    /**
     * Enum type for BES ram run communication CMD.
     */
    public static enum cmd_Mode{
        HandShake_Non_Secure_Boot,
        Download_Program,
        Download_RET,
        Go_Program,
    }

    /**
    * Extract valid data for BES ram run
    * @version 1.0
    * @param fileName bin file name with qualified path in the project
    * @return Valid data in ArrayList Type
    */
    @SuppressWarnings("null")
    public static ArrayList<Long> binFileExtract(String fileName)  {
        @SuppressWarnings("unused")
        String fileType="";
        if(fileName.contains(".bin")==true){
            fileType="bin";
        }
//        else if(fileName.contains(".txt")==true){
//            fileType="txt"; //not use
//        }
        else{
            System.out.println("***************************************");
            System.out.println("** ERROR: filename format incorrect! **");
            System.out.println("***************************************");
            new Exception("Error: Ramrun filename format incorrect, please check it!").printStackTrace();
        }

        ArrayList<Long> rawDataList = new ArrayList<Long>();
        @SuppressWarnings("resource")
        FileReader fr=null;
        int dumpByteNum=0;
        boolean firstData=false;
        boolean flag=true;
        try {
            fr=new FileReader(fileName);
            while(flag) {
                dumpByteNum++;
                long data=fr.read();
                if(dumpByteNum> 16 && firstData==false && data!=0x0 && data!=-1) {
                    firstData=true;
                }
                if(firstData==true) {//dump dummy bytes for BES only
                    if(data!=-1) {
                        rawDataList.add(data);
                    }
                    else {
                        flag=false;
                    }
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
        return rawDataList;
    }



    /**
    * Generate corresponding command to communicate with BES chip
    * @version 1.0
    * @param cmd Command to communicated with BES chip.
    *        <br>The options are: <i>HandShake_Non_Secure_Boot,Download_Program,Download_RET,Go_Program</i>  <br>
    * @param rawData Bin file processed by BES function binFileExtract
    * @return Command Sequencer in ArrayList Type
    */
    public static ArrayList<Long> getCMDSeq(cmd_Mode cmd, ArrayList<Long> rawData) {
        //*********************************//
        //*********** Get CMD SEQ *********//
        //*********************************//
        long PREFIX=0xBE;//1 Byte
        long TYPE=0x00;//1 Byte
        long SEQ=0x00;//1 Byte
        long LEN=0x00;//1 Byte
        ArrayList<Long> DATA=new ArrayList<Long>();//0~255 Byte
        long CHKSUM=0x00;//1 Byte

        switch (cmd) {
        case HandShake_Non_Secure_Boot:
            TYPE=0x50;
            SEQ=0x00;
            LEN=0x01;
            DATA.add(0x1L);
            break;
        case Download_Program:
            TYPE=0x53;
            SEQ=0x00;
            LEN=0x0c;
            for(int i=0;i<4;i++) {//ADDRESS

                DATA.add( rawData.get(rawData.size()-1-3+i) );
            }
            rawData.remove(rawData.size()-1);//remove last 4 bytes which represent ram run address
            rawData.remove(rawData.size()-1);
            rawData.remove(rawData.size()-1);
            rawData.remove(rawData.size()-1);
            for(int i=0;i<4;i++) {//CODE_LEN
                DATA.add(((rawData.size())>>(8*i))&0xffL);
            }
            long crc32=BesCalc_CRC.getCRC(rawData,crcMode.CRC32 );
            for(int i=0;i<4;i++) {//CRC32
                DATA.add( (crc32>>8*i)&0xffL );
            }
            break;
        case Download_RET:
            TYPE=0x54;
            SEQ=0xa2;
            LEN=0x03;
            DATA.add((long)0x0); DATA.add((long)0x0); DATA.add((long)0x0);
            break;
        case Go_Program:
            TYPE=0x55;
            SEQ=0x01;
            LEN=0x00;
            break;
        default:
            break;
        }

        ArrayList<Long> CMDSeq =new ArrayList<Long>();
        CMDSeq.add(PREFIX);
        CMDSeq.add(TYPE);
        CMDSeq.add(SEQ);
        CMDSeq.add(LEN);
        CMDSeq.addAll(DATA);
        for(int i=0;i<CMDSeq.size();i++) {
            CHKSUM=CHKSUM+CMDSeq.get(i);
        }
        CHKSUM=(~CHKSUM)&0xff;
        CMDSeq.add(CHKSUM);
        return CMDSeq;
    }


}
