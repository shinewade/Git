package besLib.cal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

import besLib.dsa.BesDsa_DigitalCapture.CaptureEdge;
import xoc.dta.datatypes.MultiSiteDoubleArray;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.MultiSiteLongArray;
import xoc.dta.datatypes.dsp.EdgeType;
import xoc.dta.datatypes.dsp.MultiSiteWaveLong;
import xoc.dta.datatypes.dsp.SearchDirection;
import xoc.dta.workspace.IWorkspace;

/**
 * This class is used for UART data process
 * @version V1.0
 * @author Ronnie Li, Weng Yongxin
 **/
public class BesCalc_UART {
    /**
     * Parse BES UART ASCII data and print <br>
     * BES UART Output format: <br>
     * <pre>
     * _______     ____________________________     ____________
     *        \___/ X   X   X   X   X   X   X  \___/
     * idle   start                              0   stop  idle
     * state  bit                                    bit   state
     *             LSB                          MSB</pre>
     * @param rawData digital capture data.
     * @param captureEdge set number of receive edges in capture pin WaveTable, range: 1-8
     */
    public static void printRamRunOutput_ASCIIToConsole(MultiSiteWaveLong rawData, CaptureEdge captureEdge) {
        MultiSiteLongArray outputData=new BesCalc_UART().getRamrunData(rawData, captureEdge);
        for(int site: rawData.getActiveSites()) {
            System.out.println();
            System.out.println("****** RamRun Log "+"site="+site+" ******");
            System.out.println(BesCalc_General.asciiToString(outputData.get(site)));
            System.out.println("*******************************");
            System.out.println();
        }
    }

    /**
     * Parse BES UART ASCII data and save to text <br>
     * BES UART Output format: <br>
     * <pre>
     * _______     ____________________________     ____________
     *        \___/ X   X   X   X   X   X   X  \___/
     * idle   start                              0   stop  idle
     * state  bit                                    bit   state
     *             LSB                          MSB</pre>
     * @param rawData Digital capture data.
     * @param captureEdge set number of receive edges in capture pin WaveTable, range: 1-8
     * @param fileName File name to be saved and only alphanumeric characters can be contained.
     */
    @SuppressWarnings({ "null", "resource" })
    public static void printRamRunOutput_ASCIIToTextFile(MultiSiteWaveLong rawData, CaptureEdge captureEdge, String fileName)
    {
        MultiSiteLongArray outputData=new BesCalc_UART().getRamrunData(rawData, captureEdge);
        String outputString[]=new String[rawData.getActiveSites().length];
        int siteIndex=0;
        for(int site: rawData.getActiveSites()) {
            outputString[siteIndex]=BesCalc_General.asciiToString(outputData.get(site));
            siteIndex++;
        }

        java.util.Date time = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        BufferedWriter bw=null;
        siteIndex=0;
        try{
            bw=new BufferedWriter(new FileWriter(IWorkspace.getActiveProjectPath()+"/"+fileName.replace(" ", "_").replace(".", "")+".txt",true));
            System.out.println("RamRun UART output has Been generated in the path : "+IWorkspace.getActiveProjectPath()+"/"+fileName.replace(" ", "_").replace(".", "")+"_ASCII.txt");
            bw.write("****************************************************************");
            bw.newLine();
            bw.write(fileName+" -- System Time: "+sdf.format(time).toString());
            bw.newLine();
            bw.write("****************************************************************");
            bw.newLine();
            bw.flush();
            for(int site:rawData.getActiveSites()){
                if(!outputString[siteIndex].isEmpty()){
                    bw.write("site["+site+"]: ");
                    bw.newLine();
                    bw.flush();
                    bw.write(outputString[siteIndex]);
                    bw.newLine();
                    bw.newLine();
                    bw.newLine();
                    bw.newLine();
                    bw.newLine();
                    bw.newLine();
                    bw.flush();
                }
                siteIndex++;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                 bw.close();
            }
            catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }

    /**
     * Parse BES UART Hex data and save to text <br>
     * BES UART Output format: <br>
     * <pre>
     * _______     ____________________________     ____________
     *        \___/ X   X   X   X   X   X   X  \___/
     * idle   start                              0   stop  idle
     * state  bit                                    bit   state
     *             LSB                          MSB</pre>
     * @param rawData digital capture data.
     * @param captureEdge set number of receive edges in capture pin WaveTable, range: 1-8
     * @param fileName File name to be saved and only alphanumeric characters can be contained.
     */
    @SuppressWarnings({ "null", "resource" })
    @Deprecated
    public static void printRamRunOutput_HexToTextFile(MultiSiteWaveLong rawData, CaptureEdge captureEdge, String fileName)
    {
        MultiSiteLongArray outputData=new BesCalc_UART().getRamrunData(rawData, captureEdge);

        java.util.Date time = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        BufferedWriter bw=null;
        try
        {
            bw=new BufferedWriter(new FileWriter(IWorkspace.getActiveProjectPath()+"/hex_"+fileName.replace(" ", "_").replace(".", "")+".txt",true));
            System.out.println("RamRun UART output has Been generated in the path : "+IWorkspace.getActiveProjectPath()+"/hex_"+fileName.replace(" ", "_").replace(".", "")+".txt");
            bw.write("****************************************************************");
            bw.newLine();
            bw.write(fileName+" -- System Time: "+sdf.format(time).toString());
            bw.newLine();
            bw.write("Ramrun Debug Hex Data Log");
            bw.newLine();
            bw.write("****************************************************************");
            bw.newLine();
            bw.flush();
            for(int site:rawData.getActiveSites()){
                if(outputData.toMultiSiteWave().getSize(site)>0){
                    bw.write("site["+site+"]: ");
                    bw.newLine();
                    bw.flush();
                    for(int index=0;index<outputData.toMultiSiteWave().getSize(site);index++){
                        bw.write(Long.toHexString(outputData.getElement(site, index)));
                        bw.write(" ");
                        if(index%50==0){
                            bw.newLine();
                        }
                        bw.flush();
                        }
                    bw.newLine();
                    bw.newLine();
                    bw.newLine();
                    bw.newLine();
                    bw.flush();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                 bw.close();
            }
            catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }



    /**
     * Parse BES UART output data <br>
     * BES UART Output format: <br>
     * <pre>
     * _______     ____________________________     ____________
     *        \___/ X   X   X   X   X   X   X  \___/
     * idle   start                              0   stop  idle
     * state  bit                                    bit   state
     *             LSB                          MSB</pre>
     * @param rawData digital capture data.
     * @param captureEdge set number of receive edges in capture pin WaveTable, range: 1-8
     * @return UART output data in ASCII format.
     */
    private MultiSiteLongArray getRamrunData(MultiSiteWaveLong rawData, CaptureEdge capEdge) {
        int activeSite[]=rawData.getActiveSites();
        //back up rawData
        MultiSiteWaveLong rawData_Copy=new MultiSiteWaveLong();
        rawData_Copy.set(rawData);
        //set limiting condition
        int captureEdge = capEdge.toString().charAt(1)-0x30;
        if(captureEdge<1 || captureEdge >8) {
            new Exception("Error, XMode out of range 1-8 !!! ").printStackTrace();
        }


        //* Remove the idle or invalid bits data from the raw data.
        //* UART protocol output high levels in idle state and these high level data is invalid.
        //* Excessive and continuous high or low level data is redundant,greatly increasing the time to extract and calculate data within the waveform.
        MultiSiteLong rawDataSize       = rawData.getSize();
        MultiSiteLong rawDataFirstValue = rawData.getValue(0);
        MultiSiteLong rawDataLastValue  = rawData.getValue(rawDataSize.subtract(1));

        MultiSiteDoubleArray rawDataRisingEdgesArray  = rawData.edges(0.5, EdgeType.RISING,  SearchDirection.ASCENDING);
        MultiSiteDoubleArray rawDataFallingEdgesArray = rawData.edges(0.5, EdgeType.FALLING, SearchDirection.ASCENDING);
        MultiSiteLong risingEdgesArraySize            = rawDataRisingEdgesArray .toMultiSiteWave().getSize();
        MultiSiteLong fallingEdgesArraySize           = rawDataFallingEdgesArray.toMultiSiteWave().getSize();

        int indexArrayMaxSize = -999;
        for(int site:activeSite)
        {
            if( (risingEdgesArraySize.get(site)+fallingEdgesArraySize.get(site)+1)>indexArrayMaxSize )
            {
                indexArrayMaxSize = risingEdgesArraySize.getAsInt(site)+fallingEdgesArraySize.getAsInt(site)+1;
            }
        }
        if(indexArrayMaxSize<=0)
        {
            indexArrayMaxSize = 1;
        }

        MultiSiteLongArray startIndexArray = new MultiSiteLongArray(indexArrayMaxSize,0);
        MultiSiteLongArray stopIndexArray  = new MultiSiteLongArray(indexArrayMaxSize,0);
        int startIndex = 0;
        int stopIndex  = 0;

        int count = 0;

        for(int site:activeSite)
        {
            if(risingEdgesArraySize.get(site)>0 && fallingEdgesArraySize.get(site)>0)
            {
                if(rawDataLastValue.get(site) == 1)
                {
                    if( (rawDataSize.get(site)-rawDataRisingEdgesArray.getElement(site, risingEdgesArraySize.getAsInt(site)-1)) > captureEdge*30 )
                    {
                        startIndex = (int)(rawDataRisingEdgesArray.getElement(site, risingEdgesArraySize.getAsInt(site)-1)+0.5);
                        stopIndex  = (int)(rawDataSize.get(site)-captureEdge*20);
                        rawData.set(site, rawData.get(site).removeValues(startIndex,stopIndex-startIndex));
                    }
                }
                else
                {
                    if( (rawDataSize.get(site)-rawDataFallingEdgesArray.getElement(site, fallingEdgesArraySize.getAsInt(site)-1)) > captureEdge*30 )
                    {
                        startIndex = (int)(rawDataFallingEdgesArray.getElement(site, fallingEdgesArraySize.getAsInt(site)-1)+0.5);
                        stopIndex  = (int)(rawDataSize.get(site)-captureEdge*20);
                        rawData.set(site, rawData.get(site).removeValues(startIndex,stopIndex-startIndex));
                    }
                }
                for(int i=0; i<risingEdgesArraySize.getAsInt(site); i++)
                {
                    if( (i+1) <risingEdgesArraySize.getAsInt(site) )
                    {
                        if( (rawDataRisingEdgesArray.getElement(site, i+1)-rawDataRisingEdgesArray.getElement(site, i)) > captureEdge*10*3 )
                        {
                            startIndexArray.setElement(site, count, (long)(rawDataRisingEdgesArray.getElement(site, i  )+0.5)          );
                            stopIndexArray .setElement(site, count, (long)(rawDataRisingEdgesArray.getElement(site, i+1)-captureEdge*10*2));
                            if(count<(indexArrayMaxSize-1))
                            {
                                count++;
                            }
                        }
                    }
                }
                for(int i=count; i>=0; i--)
                {
                    startIndex = (int)startIndexArray.getElement(site, i);
                    stopIndex  = (int)stopIndexArray.getElement(site, i);
                    rawData.set(site, rawData.get(site).removeValues(startIndex,stopIndex-startIndex));
                }
                if(rawDataFirstValue.get(site) == 1)
                {
                    if( rawDataFallingEdgesArray.getElement(site, 0) > captureEdge*20 )
                    {
                        startIndex = 0;
                        stopIndex  = (int)(rawDataFallingEdgesArray.getElement(site, 0)-captureEdge*10);
                        rawData.set(site, rawData.get(site).removeValues(startIndex,stopIndex-startIndex));
                    }
                }
                else
                {
                    if( rawDataRisingEdgesArray.getElement(site, 0) > captureEdge*20 )
                    {
                        startIndex = 0;
                        stopIndex  = (int)(rawDataRisingEdgesArray.getElement(site, 0)-captureEdge*10);
                        rawData.set(site, rawData.get(site).removeValues(startIndex,stopIndex-startIndex));
                    }
                }
            }
        }
//        rawData.plot("extract_new_wave");

        //get max match size
        int siteMatchSizeMax=0;
        for(int site:activeSite) {
            if(rawData.getSize(site)>siteMatchSizeMax) {
                siteMatchSizeMax=rawData.getSize(site);
            }
        }
        //parse valid bits
        MultiSiteLongArray uartReadDataArray=new MultiSiteLongArray((siteMatchSizeMax/captureEdge), 0);
        MultiSiteWaveLong tempWave_11bits = new MultiSiteWaveLong();
        MultiSiteWaveLong tempWave_8bits = new MultiSiteWaveLong();
        MultiSiteLong extractIndex_StartBitH=new MultiSiteLong();
        MultiSiteLong extractIndex_StartBitL=new MultiSiteLong();
        MultiSiteLong extractIndex_StopBitH=new MultiSiteLong();
        MultiSiteLong extractIndex_StopBitL=new MultiSiteLong();
        MultiSiteLong length_Extract=new MultiSiteLong(1);
        MultiSiteLong newSize=new MultiSiteLong(1);

        int lastRisingEdgeIndex = -1;
        int splitSize_8bits = 0;
        int bitValue = 0;
        startIndex=-1;
        stopIndex =-1;
        count = 0;
        rawData = rawData.resize(new MultiSiteLong(siteMatchSizeMax));
        int maxDataSize=0;
        for(int site:activeSite){
            count=0;//initialize per site
            for(int i=0; i<rawData.getSize(site); i++){
                for(int siteEn:activeSite){
                    extractIndex_StartBitH.set(siteEn, i+ 0*captureEdge);
                    extractIndex_StartBitL.set(siteEn, i+ 1*captureEdge);
                    extractIndex_StopBitH .set(siteEn, i+ 9*captureEdge);
                    extractIndex_StopBitL .set(siteEn, i+10*captureEdge);
                }
                length_Extract .set(site, captureEdge);

                if( (i+11*captureEdge) <= rawData.getSize(site) ){
                    if( rawData.extractValues(extractIndex_StartBitH, length_Extract).mean().get(site) >= 0.750 &&
                        rawData.extractValues(extractIndex_StartBitL, length_Extract).mean().get(site) <= 0.250 &&
                        rawData.extractValues(extractIndex_StopBitH , length_Extract).mean().get(site) <= 0.250 &&
                        rawData.extractValues(extractIndex_StopBitL , length_Extract).mean().get(site) >= 0.750 ){

                        tempWave_11bits = rawData.extractValues(i, 11*captureEdge);
                        lastRisingEdgeIndex = tempWave_11bits.get(site).edges(0.5, EdgeType.RISING,SearchDirection.ASCENDING).length - 1;
                        startIndex = (int)(tempWave_11bits.edges(0.5, EdgeType.FALLING,  SearchDirection.ASCENDING).getElement(site, 0)+0.5+captureEdge);//Add captureEdge to remove start bit.
                        stopIndex  = (int)(tempWave_11bits.edges(0.5, EdgeType.RISING,   SearchDirection.ASCENDING).getElement(site, lastRisingEdgeIndex)-0.5);
                        tempWave_8bits = tempWave_11bits.extractValues(startIndex, (stopIndex+captureEdge/2)-startIndex);//Stop index add captureEdge/2 points to get appropriate splitSize_8bits value in division operation and avoid over size when extract value.
                        splitSize_8bits = tempWave_8bits.getSize(site)/8;
                        for(int j=0; j<7; j++){
                            bitValue=tempWave_8bits.extractValues(j*splitSize_8bits, splitSize_8bits).get(site).mean() >= 0.5?1:0;
                            uartReadDataArray.setElement( site, count, uartReadDataArray.getElement(site, count)+bitValue*(int)(Math.pow(2, j)) );
                        }
//                        System.out.println("data"+count+" Data= "+uartReadDataArray.getElement(site, count));
                        count++;
                        i = stopIndex+i;
                    }
                    if(count>maxDataSize) {
                        maxDataSize=count;
                    }
                }
            }
            newSize.set(site, count);
        }
        //restore raw data
        rawData.set(rawData_Copy);

        //resize  returnUart size to actual max data size
        MultiSiteLongArray returnUart=new MultiSiteLongArray(maxDataSize,0);
        returnUart.set(uartReadDataArray.toMultiSiteWave().resize(newSize).toMultiSiteArray());

        return returnUart;
    }


}


