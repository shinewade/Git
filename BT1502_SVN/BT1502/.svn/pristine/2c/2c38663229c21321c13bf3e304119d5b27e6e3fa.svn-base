package besLib.cal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import xoc.dta.datatypes.MultiSiteBoolean;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.datatypes.MultiSiteLongArray;
import xoc.dta.datatypes.MultiSitePatternFailureList;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.resultaccess.IDigInOutCallPassFailResults;
import xoc.dta.resultaccess.IDigInOutCyclePassFailResults;
import xoc.dta.resultaccess.IDigInOutResults;
import xoc.dta.setupaccess.IPatternCall;
import xoc.dta.testdescriptor.IFunctionalTestDescriptor;
import xoc.dta.workspace.IWorkspace;

/**
 * This class is used to retrieve and record DFT pattern run result
 * @version V1.1
 * @author Weng Yongxin
 **/
public class BesCalc_DFT{
    private IMeasurement imeasurement;
    private IFunctionalTestDescriptor ifunctionalTestDescriptor;

    /**
     * Constructor
     *
     * @param imeasurement This interface enables you to read and write to the measurement object
     * @param ifunctionalTestDescriptor One or more functional or scan test descriptor objects.
     *
     */
    public BesCalc_DFT(IMeasurement imeasurement,  IFunctionalTestDescriptor ifunctionalTestDescriptor) {
        this.imeasurement=imeasurement;
        this.ifunctionalTestDescriptor=ifunctionalTestDescriptor;
    }

    /**
     * Get executed measurement overall result
     *
     * @return Overall pass/fail result in MultiSiteLong type
     */
    public MultiSiteLong getOverallResult() {
        IDigInOutResults digInOutResults = imeasurement.digInOut().preserveResults(ifunctionalTestDescriptor);
        MultiSiteLong patRes=new MultiSiteLong(0);
        for(int site:digInOutResults.hasPassed().getActiveSites()) {
            if(digInOutResults.hasPassed().get(site)==true) {
                patRes.set(site,1);
            }
        }
        return patRes;
    }

    /**
     * Get executed measurement result per pattern
     *
     * @param capturePinName captured pin name
     *
     * @return Per pattern pass/fail result in multiSiteBoolean type
     */
    public MultiSiteLongArray getIndividualResult(String capturePinName) {
        IDigInOutCallPassFailResults callPassFailResults=this.imeasurement.digInOut(capturePinName).preserveCallPassFailResults();
        MultiSiteLongArray patRes=new MultiSiteLongArray(callPassFailResults.getPatternPassFail().length, 0);
        for(int i=0;i<callPassFailResults.getPatternPassFail().length;i++) {
            for(int site:callPassFailResults.getPatternPassFail()[0].hasPassed().getActiveSites()) {
                if(callPassFailResults.getPatternPassFail()[i].hasPassed().get(site)==true) {
                    patRes.setElement(site, i, 1);
                }
            }

            System.out.println("PatCall= "+callPassFailResults.getPatternPassFail()[i].getPatternCall());
            System.out.println("PatCal Result= "+callPassFailResults.getPatternPassFail()[i].hasPassed());
            System.out.println();
        }
        return patRes;
    }

    /**
     * Pattern fail cycle write to text.
     *
     * @param capturePinName Pattern compared pins
     * @param failCycles Pattern fail cycles saved to text
     * @param fileName fail log file
     */
    @Deprecated
    @SuppressWarnings("null")
    public void writeFailLog2Text(String capturePinName, int failCycles, String fileName) {
        IDigInOutResults digInOutResults = imeasurement.digInOut().preserveResults(ifunctionalTestDescriptor);
        IDigInOutCyclePassFailResults cyclePassFailResults = imeasurement.digInOut(capturePinName).preserveCyclePassFailResults();

        //get pattern name from operating sequencer
        List<IPatternCall> patternCall=digInOutResults.getPatternCalls();

        //get pattern failure per pattern
        @SuppressWarnings("unchecked")
        Map<String, MultiSitePatternFailureList>[] failLog=new Map[patternCall.size()];
        String[] patName=new String[patternCall.size()];
        for(int patindex=0; patindex<patternCall.size(); patindex++) {
            failLog[patindex] = cyclePassFailResults.pattern(patternCall.get(patindex)).getPatternFailures();
            patName[patindex]=patternCall.get(patindex).toString().substring( patternCall.get(patindex).toString().lastIndexOf(".")+1 ).replace(";", "");
        }

        //get pattern failure information
        for(int patindex=0; patindex<patternCall.size(); patindex++) {
            for(String Pin:failLog[patindex].keySet()) {
                int activeSites[]=failLog[patindex].get(Pin).getActiveSites();
                for(int site:activeSites) {
                    //Write fail log to text
                    if(Pin.equals( failLog[patindex].keySet().toArray()[0]) ) {
                        System.out.println("DFT fail Log has Been generated in the path : "+IWorkspace.getActiveProjectPath()+"/"+fileName.replace(" ", "_").replace(".", "")+"_Site"+site+".txt");
                    }
                    if(failLog[patindex].get(Pin).get(site).size()==0) {
                        @SuppressWarnings("resource")
                        BufferedWriter bw=null;
                        try {
                            bw=new BufferedWriter(new FileWriter(IWorkspace.getActiveProjectPath()+"/"+fileName+"_Site"+site+".txt",true));
//                            bw=new BufferedWriter(new FileWriter(IWorkspace.getActiveProjectPath()+"/"+"Time_"+df.format(new Date())+"__"+patName[patindex]+"_"+"PatCall"+patindex+"_site"+site+".txt",true));
                            if(Pin.equals( failLog[patindex].keySet().toArray()[0]) ) {
                                bw.newLine();
                                bw.newLine();
                                bw.newLine();
                                bw.newLine();
                                bw.newLine();
                                bw.newLine();
                                bw.write("*********************************************************************************************************************");
                                bw.newLine();
                                bw.newLine();
                                bw.write(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
                                bw.newLine();

                                for(int i=0;i<patName.length;i++) {
                                    bw.write("--------------------------------------------------------------------------------");
                                    bw.newLine();
                                    bw.write("Pattern: "+patName[i]);
                                    bw.newLine();
                                }
                                bw.write("--------------------------------------------------------------------------------");
                                bw.newLine();
                                bw.write("site= "+site);
                                bw.newLine();
                                bw.write("--------------------------------------------------------------------------------");
                                bw.newLine();
                            }
                            bw.flush();
                            bw.newLine();
                            bw.newLine();
                            bw.write("---------------------");
                            bw.newLine();
                            bw.write("Compare Pin: "+Pin);
                            bw.newLine();
                            bw.write("---------------------");
                            bw.newLine();
                            bw.write("Fail Cycle Count= "+failLog[patindex].get(Pin).get(site).size());
                            bw.newLine();
                            bw.write("---------------------");
                            bw.newLine();
                            bw.newLine();
                            bw.flush();
                            long maxFailCycles=0;
                            if(failLog[patindex].get(Pin).get(site).size()>failCycles) {
                                maxFailCycles=failCycles;
                            }
                            else {
                                maxFailCycles=failLog[patindex].get(Pin).get(site).size();
                            }
                            for(int failIndex=0; failIndex<maxFailCycles; failIndex++) {
    //                            System.out.println(""+failLog[patindex].get(Pin).get(site).get(failIndex));
                                bw.write(failLog[patindex].get(Pin).get(site).get(failIndex).toString());
                                bw.newLine();
                                bw.flush();
                            }
                            bw.newLine();
                            bw.newLine();
                            bw.newLine();
                            bw.flush();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally{
                            try{
                               bw.close();
                            }
                            catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }





    /**
     * Pattern fail cycle write to text.
     *
     * @param capturePinName Pattern compared pins
     * @param fileName fail log file
     */
    @SuppressWarnings("null")
    public void writeFailLog2Text(String capturePinName, String fileName) {
        IDigInOutResults digInOutResults = imeasurement.digInOut().preserveResults(ifunctionalTestDescriptor);
        IDigInOutCyclePassFailResults cyclePassFailResults = imeasurement.digInOut(capturePinName).preserveCyclePassFailResults();

        boolean log_flag = false;
        MultiSiteBoolean FunResults=digInOutResults.hasPassed();

        for(int site:FunResults.getActiveSites()) {
            if(FunResults.get(site).equals(false)) {
                log_flag = true;
                break;
            }
        }

        Map<String, MultiSitePatternFailureList> fail_log = cyclePassFailResults.getPatternFailures();
        @SuppressWarnings("resource")
        BufferedWriter bw=null;
        try {
            if(log_flag == true) {
                bw=new BufferedWriter(new FileWriter(IWorkspace.getActiveProjectPath()+"/"+fileName+"_fail_log.txt",true));//"/export/home/bestechnic3/BT1501"

                bw.write("***************************************");
                bw.newLine();
                bw.write(fileName);
                bw.newLine();
                bw.write("***************************************");
                bw.newLine();
                bw.flush();

               for(String pin:fail_log.keySet()) {
                    for(int site:fail_log.get(pin).getActiveSites()) {
                        if(fail_log.get(pin).get(site).size()!=0) {
                            bw.write("site["+site+"] "+pin+" failcycle count: "+fail_log.get(pin).get(site).size());
                            bw.newLine();
                            bw.flush();
                            for(int index=0;index<fail_log.get(pin).get(site).size();index++) {
                                bw.write(fail_log.get(pin).get(site).get(index).toString());
                                bw.newLine();
                                bw.flush();
                            }
                            bw.newLine();
                            bw.flush();
                        }
                    }
               }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                 bw.close();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


}
