package BT1502_pro_tml.Global;

import xoc.dsa.DeviceSetupUncheckedException;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteDoubleArray;
import xoc.dta.datatypes.MultiSiteLong;

public class OffsetCompensation {
    /**
     * Enum type for property windowFunction.
     */
    public enum OffsetType
    {
        /**
         *Set BT TX Power
         */
        BT_TX_Power,

        /**
         *Set BT RX Current
         */
        WIFI2G4_TX_Power,

        /**
         *Set BT RX Current
         */
        WIFI5G_TX_Power,
    }

    /**
     * get the offset of corresponding item with predefined value.
     *
     * @param LBID LBID read from EEPROM at LB
     * @param offsetType specified compensation item
     * @return offset value of corresponding item
     */
    public MultiSiteDouble getOffsetValue(MultiSiteLong LBID, OffsetType offsetType)
    {
        int[] activeSites=LBID.getActiveSites();
        long LB_ID_long=LBID.get(activeSites[0]);
        int LB_ID_int=(int)LB_ID_long;

        MultiSiteDoubleArray overallOffset=new MultiSiteDoubleArray(100, 0);
        MultiSiteDouble siteOffset=new MultiSiteDouble(0);
        double lbOffset=0;
        switch (offsetType) {
        case BT_TX_Power:
            switch (LB_ID_int) {
            case 1://LB01 20220630 add by wuhan
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);           //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, -1.026);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0.481666667);//site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, -2.788666667); //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, -0.160333333); //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, -1.802333333); //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, -0.389);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, -2.131333333); //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset=0;//LB offset
                overallOffset.setElement(1 , siteOffset.add(lbOffset)  );
                break;

            case 2://LB02
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, -1.6);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, -0.16);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, -2.89);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, -0.89);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, -1.84);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0.07);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, -2.38);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset=0.80;//LB offset
                overallOffset.setElement(2 , siteOffset.add(lbOffset)  );
                break;

            case 3://LB03
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset= 0 ;//LB offset
                overallOffset.setElement(3 , siteOffset.add(lbOffset)  );
                break;

            case 4://LB04
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, -1.580);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0.560);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, -2.627);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, -0.259);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, -1.913);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0.067);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, -2.232);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset=0.628;//LB offset
                overallOffset.setElement(4 , siteOffset.add(lbOffset)  );
                break;

            case 10://LB10 for BGA LB
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset= 0 ;//LB offset
                overallOffset.setElement(10 , siteOffset.add(lbOffset)  );
                break;
            case 11://LB10 for BGA LB --forehope
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset= 0 ;//LB offset
                overallOffset.setElement(11 , siteOffset.add(lbOffset)  );
                break;
            case 12://LB10 for BGA LB --
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset= 0 ;//LB offset
                overallOffset.setElement(12 , siteOffset.add(lbOffset)  );
                break;
            case 13://LB10 for BGA LB --
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset= 0 ;//LB offset
                overallOffset.setElement(13 , siteOffset.add(lbOffset)  );
                break;
            default:
                throw new DeviceSetupUncheckedException("Error, Offset compensation can not find corresponding LB ID !");
            }
            break;

        case WIFI2G4_TX_Power:
            switch (LB_ID_int) {
            case 1://LB01
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset=0;//LB offset
                overallOffset.setElement(1 , siteOffset.add(lbOffset)  );
                break;

            case 2://LB02
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset=0;//LB offset
                overallOffset.setElement(2 , siteOffset.add(lbOffset)  );
                break;

            case 3://LB03
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset=0;//LB offset
                overallOffset.setElement(3 , siteOffset.add(lbOffset)  );
                break;

            case 4://LB04
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset=0;//LB offset
                overallOffset.setElement(4 , siteOffset.add(lbOffset)  );
                break;

            default:
                throw new DeviceSetupUncheckedException("Error, Offset compensation can not find corresponding LB ID !");
            }
            break;


        case WIFI5G_TX_Power:
            switch (LB_ID_int) {
            case 1://LB01
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset=0;//LB offset
                overallOffset.setElement(1 , siteOffset.add(lbOffset)  );
                break;

            case 2://LB02
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset=0;//LB offset
                overallOffset.setElement(2 , siteOffset.add(lbOffset)  );
                break;

            case 3://LB03
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset=0;//LB offset
                overallOffset.setElement(3 , siteOffset.add(lbOffset)  );
                break;

            case 4://LB04
                for(int site:LBID.getActiveSites()) {
                    switch (site) {
                    case 1:
                        siteOffset.set(1, 0);       //site1 offset
                        break;
                    case 2:
                        siteOffset.set(2, 0);       //site2 offset
                        break;
                    case 3:
                        siteOffset.set(3, 0);       //site3 offset
                        break;
                    case 4:
                        siteOffset.set(4, 0);       //site4 offset
                        break;
                    case 5:
                        siteOffset.set(5, 0);       //site5 offset
                        break;
                    case 6:
                        siteOffset.set(6, 0);       //site6 offset
                        break;
                    case 7:
                        siteOffset.set(7, 0);       //site7 offset
                        break;
                    case 8:
                        siteOffset.set(8, 0);       //site8 offset
                        break;
                    default:
                        break;
                    }
                }
                lbOffset=0;//LB offset
                overallOffset.setElement(4 , siteOffset.add(lbOffset)  );
                break;

            default:
                break;
            }
            throw new DeviceSetupUncheckedException("Error, Offset compensation can not find corresponding LB ID !");

        default:
            break;
        }

        return overallOffset.getElement(LB_ID_int);
    }

}
