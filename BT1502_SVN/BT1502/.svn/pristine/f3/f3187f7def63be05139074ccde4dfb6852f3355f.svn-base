package besLib.dsa;

import java.util.ArrayList;

import besLib.cal.BesCalc_RamRunBinFile;
import besLib.cal.BesCalc_RamRunBinFile.cmd_Mode;
import besLib.pa.BesPA_UART;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupProtocolInterface;
import xoc.dsa.ISetupTransactionSeqDef;
import xoc.dta.measurement.IMeasurement;

/**
 * This class is used to generate BES RamRun SSF file in Project with V93K SMT8
 * @version V1.2
 * @author Weng Yongxin
 **/
public class BesDsa_UART {

    private  IDeviceSetup ds;

    /**
     * Constructor
     *
     * @param ds instance of IDeviceSetup interface
     */
    public BesDsa_UART(IDeviceSetup ds) {
        this.ds=ds;
    }


    /**
     * For BES bin file download to MCU RAM only
     *
     * @param fileName bin file name with qualified path in the project
     *
     */
    @Deprecated
    public void writeToRAM( String fileName)  {
        if(fileName.contains(".bin")!=true){
            System.out.println("***************************************");
            System.out.println("** ERROR: filename suffix incorrect! **");
            System.out.println("***************************************");
        }

        //get bin file data
        ArrayList<Long> rawData=BesCalc_RamRunBinFile.binFileExtract(fileName);

        //generate UART communication format
        ArrayList<Long> handShake=BesCalc_RamRunBinFile.getCMDSeq(cmd_Mode.HandShake_Non_Secure_Boot, rawData);
        ArrayList<Long> downloadProgram=BesCalc_RamRunBinFile.getCMDSeq(cmd_Mode.Download_Program, rawData);
        ArrayList<Long> downloadRET=BesCalc_RamRunBinFile.getCMDSeq(cmd_Mode.Download_RET, rawData);
        ArrayList<Long> goProgram=BesCalc_RamRunBinFile.getCMDSeq(cmd_Mode.Go_Program, rawData);

        //PA
        ISetupProtocolInterface[] paInterface=new ISetupProtocolInterface[1];
        paInterface[0] = this.ds.addProtocolInterface("UART_BES_"+fileName.substring(fileName.lastIndexOf("/")+1,fileName.lastIndexOf(".")), "besLib.pa.UART_BES");
        paInterface[0].addSignalRole("RXD", "I2C_SCL");
        paInterface[0].addSignalRole("TXD", "I2C_SDA");
        ISetupTransactionSeqDef[] transDigSrc=new ISetupTransactionSeqDef[1];
        transDigSrc[0]= paInterface[0].addTransactionSequenceDef();
        for(int i=0;i<handShake.size();i++)         {transDigSrc[0].addTransaction("writeUart", handShake.get(i));}
        transDigSrc[0].addWait("5 ms");
        for(int i=0;i<downloadProgram.size();i++)   {transDigSrc[0].addTransaction("writeUart", downloadProgram.get(i));}
        transDigSrc[0].addWait("5 ms");
        for(int i=0;i<downloadRET.size();i++)       {transDigSrc[0].addTransaction("writeUart", downloadRET.get(i));}
        for(int i=0;i<rawData.size();i++)           {transDigSrc[0].addTransaction("writeUart", rawData.get(i));}
        transDigSrc[0].addWait("5 ms");
        for(int i=0;i<goProgram.size();i++)         {transDigSrc[0].addTransaction("writeUart", goProgram.get(i));}

        //OpSeq
        this.ds.transactionSequenceCall(transDigSrc[0]);
    }

    /**
     * For BES bin file download to MCU RAM with New Protocol Access only
     *
     * @param measurement An instance of IMeasurement interface.
     * @param fileName bin file name with qualified path in the project
     *
     */
    public void writeToRAM_NewPA(IMeasurement measurement, String fileName)  {
        if(fileName.contains(".bin")!=true){
            System.out.println("***************************************");
            System.out.println("** ERROR: filename suffix incorrect! **");
            System.out.println("***************************************");
        }

        //get bin file data
        ArrayList<Long> rawData=BesCalc_RamRunBinFile.binFileExtract(fileName);

        //generate UART communication format
        ArrayList<Long> handShake=BesCalc_RamRunBinFile.getCMDSeq(cmd_Mode.HandShake_Non_Secure_Boot, rawData);
        ArrayList<Long> downloadProgram=BesCalc_RamRunBinFile.getCMDSeq(cmd_Mode.Download_Program, rawData);
        ArrayList<Long> downloadRET=BesCalc_RamRunBinFile.getCMDSeq(cmd_Mode.Download_RET, rawData);
        ArrayList<Long> goProgram=BesCalc_RamRunBinFile.getCMDSeq(cmd_Mode.Go_Program, rawData);

        //PA
        BesPA_UART uart=new BesPA_UART(ds, measurement);
        uart.setSignals("I2C_SDA", "I2C_SCL"); //0721 wuhan

        //OpSeq
        uart.transactionSequenceBegin("handShake");
            for(int i=0;i<handShake.size();i++){
                uart.write(handShake.get(i));
            }
        uart.transactionSequenceEnd();
        ds.waitCall("5 ms");

        uart.transactionSequenceBegin("downloadProgram");
            for(int i=0;i<downloadProgram.size();i++){
                uart.write(downloadProgram.get(i));
            }
        uart.transactionSequenceEnd();
        ds.waitCall("5 ms");

        uart.transactionSequenceBegin("downloadRET");
            for(int i=0;i<downloadRET.size();i++){
                uart.write(downloadRET.get(i));
            }
        uart.transactionSequenceEnd();
        ds.waitCall("5 ms");

        uart.transactionSequenceBegin("rawData");
            for(int i=0;i<rawData.size();i++){
                uart.write(rawData.get(i));
            }
        uart.transactionSequenceEnd();
        ds.waitCall("5 ms");

        uart.transactionSequenceBegin("goProgram");
            for(int i=0;i<goProgram.size();i++){
                uart.write(goProgram.get(i));
            }
        uart.transactionSequenceEnd();
    }



}

