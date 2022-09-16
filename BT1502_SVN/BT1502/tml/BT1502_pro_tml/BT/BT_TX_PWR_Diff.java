package BT1502_pro_tml.BT;

import BT1502_pro_tml.Global.StaticFields;
import xoc.dta.TestMethod;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class BT_TX_PWR_Diff extends TestMethod {

    public IParametricTestDescriptor    ptd_TX_PWR_L,
                                        ptd_TX_PWR_M,
                                        ptd_TX_PWR_H,
                                        ptd_TX_PWR_L2H,
                                        ptd_TX_PWR_M2L,
                                        ptd_TX_PWR_M2H;

    @Override
    public void execute() {

        MultiSiteDouble pwr_l2h=new MultiSiteDouble();
        MultiSiteDouble pwr_m2l=new MultiSiteDouble();
        MultiSiteDouble pwr_m2h=new MultiSiteDouble();

        MultiSiteDouble pwr_max=new MultiSiteDouble();
        MultiSiteDouble pwr_min=new MultiSiteDouble();
        MultiSiteDouble ch_max=new MultiSiteDouble();
        MultiSiteDouble ch_min=new MultiSiteDouble();
        MultiSiteDouble pwr_diff=new MultiSiteDouble();

        pwr_l2h=StaticFields.BTTX_PWR_L.subtract(StaticFields.BTTX_PWR_H);
        pwr_m2l=StaticFields.BTTX_PWR_M.subtract(StaticFields.BTTX_PWR_L);
        pwr_m2h=StaticFields.BTTX_PWR_M.subtract(StaticFields.BTTX_PWR_H);

        pwr_max.set(StaticFields.BTTX_PWR_L);
        pwr_min.set(StaticFields.BTTX_PWR_L);
        ch_max.set(2402);
        ch_min.set(2402);

        int[] activeSites = context.getActiveSites();
        for(int site:activeSites) {
            if(StaticFields.BTTX_PWR_M.get(site)>pwr_max.get(site)) {
                pwr_max.set(site,StaticFields.BTTX_PWR_M.get(site));
                ch_max.set(site, 2441);
            }
            if(StaticFields.BTTX_PWR_H.get(site)>pwr_max.get(site)) {
                pwr_max.set(site,StaticFields.BTTX_PWR_H.get(site));
                ch_max.set(site, 2480);
            }
            if(StaticFields.BTTX_PWR_M.get(site)<pwr_min.get(site)) {
                pwr_min.set(site,StaticFields.BTTX_PWR_M.get(site));
                ch_min.set(site, 2441);
            }
            if(StaticFields.BTTX_PWR_H.get(site)<pwr_min.get(site)) {
                pwr_min.set(site,StaticFields.BTTX_PWR_H.get(site));
                ch_min.set(site, 2480);
            }
            pwr_diff.set(site, pwr_max.subtract(pwr_min).get(site));

        }

        if(StaticFields.debugMode)
        {
            String testSuiteName_Qualified=context.getTestSuiteName();
            String testSuiteName=testSuiteName_Qualified.substring(1+testSuiteName_Qualified.lastIndexOf("."));
            println("**********"+testSuiteName+"**********");
            println("BTTX_PWR_L = "+StaticFields.BTTX_PWR_L);
            println("BTTX_PWR_M = "+StaticFields.BTTX_PWR_M);
            println("BTTX_PWR_H = "+StaticFields.BTTX_PWR_H);
            println("pwr_l2h = "+pwr_l2h);
            println("pwr_m2l = "+pwr_m2l);
            println("pwr_m2h = "+pwr_m2h);
            println();
        }

        ptd_TX_PWR_L.evaluate(StaticFields.BTTX_PWR_L);
        ptd_TX_PWR_M.evaluate(StaticFields.BTTX_PWR_M);
        ptd_TX_PWR_H.evaluate(StaticFields.BTTX_PWR_H);
        ptd_TX_PWR_L2H.evaluate(pwr_l2h);
        ptd_TX_PWR_M2L.evaluate(pwr_m2l);
        ptd_TX_PWR_M2H.evaluate(pwr_m2h);

    }

}


