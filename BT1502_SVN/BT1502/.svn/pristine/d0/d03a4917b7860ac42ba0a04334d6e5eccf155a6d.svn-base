package BT1502_pro_tml.Global;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import xoc.dta.datatypes.MultiSiteBoolean;
import xoc.dta.datatypes.MultiSitePatternFailureList;
import xoc.dta.resultaccess.IDigInOutCyclePassFailResults;

public final class DFT_FUNC_DEBUG{

   private DFT_FUNC_DEBUG()
   {
       throw new AssertionError();
   }

/**
 * Pattern fail cycle write to text.
 * @param FailResults IDigInOutCyclePassFailResults
 * @param log_level 1:failcycle count; 2:failcycle count & failcycles log
 * @return Null
 */
public static void Println_Func_FailLog(IDigInOutCyclePassFailResults FailResults,int log_level)
{
    if(log_level == 1)
    {
        System.out.println("Fail Count: "+FailResults.getNumberOfFailedCycles());
    }

    if(log_level == 2)
    {
//		System.out.println(FailResults.getPatternFailures());
        Map<String, MultiSitePatternFailureList> fail_log = FailResults.getPatternFailures();
        for(String pin:fail_log.keySet())
        {
            for(int site:fail_log.get(pin).getActiveSites())
            {
                if(fail_log.get(pin).get(site).size()!=0)
                {
                    System.out.println("site["+site+"]  failcycle count: "+fail_log.get(pin).get(site).size());
                    for(int index=0;index<fail_log.get(pin).get(site).size();index++)
                    {
                        System.out.println(fail_log.get(pin).get(site).get(index));
                    }
                    System.out.println();
                }
            }
        }
    }
    System.out.println();
}

/**
 * Pattern fail cycle write to text.
 * @param file_path       Used for fail log file naming
 * @param pattern_name    Used for fail log file naming
 * @param testSuiteName   Title of fail log content
 * @param PassFailResults IDigInOutCyclePassFailResults
 * @param FunResults      MultiSite Pattern pass fail results
 * @return Null
 */
@SuppressWarnings("null")
public static void Func_FailLog_Write_To_File(String file_path, String pattern_name, String testSuiteName, IDigInOutCyclePassFailResults PassFailResults, MultiSiteBoolean FunResults)
{
    boolean log_flag = false;

    for(int site:FunResults.getActiveSites())
    {
        if(FunResults.get(site).equals(false))
        {
            log_flag = true;
            break;
        }
    }

    Map<String, MultiSitePatternFailureList> fail_log = PassFailResults.getPatternFailures();
    @SuppressWarnings("resource")
    BufferedWriter bw=null;
    try
    {
        if(log_flag == true)
        {
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));//time zone
            //
//                    System.out.println(new Date(System.currentTimeMillis()));//current time
            Calendar calendar = Calendar.getInstance(); // get current instance of the calendar
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            bw=new BufferedWriter(new FileWriter(file_path+pattern_name+"_"+formatter.format(calendar.getTime())+"_fail_log.txt",true));//"/export/home/bestechnic3/BT1501"

            bw.write("***************************************");
            bw.newLine();
            bw.write(formatter.format(calendar.getTime()));
            bw.newLine();
            bw.write("***************************************");
            bw.newLine();
            bw.write(testSuiteName);
            bw.newLine();
            bw.write("***************************************");
            bw.newLine();
            bw.flush();

           for(String pin:fail_log.keySet())
           {
                for(int site:fail_log.get(pin).getActiveSites())
                {
                    if(fail_log.get(pin).get(site).size()!=0)
                    {
                        bw.write("site["+site+"] "+pin+" failcycle count: "+fail_log.get(pin).get(site).size());
                        bw.newLine();
                        bw.flush();
                        for(int index=0;index<fail_log.get(pin).get(site).size();index++)
                        {
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
    catch (Exception e)
    {
        // TODO: handle exception
    }
    finally
    {
        try
        {
             bw.close();
        }
        catch (Exception e2)
        {
            // TODO: handle exception
        }
    }
}

}
