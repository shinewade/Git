package BT1502_pro_tml.EFFUSE;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.efuse.BesDsa_EfuseData;
import xoc.dta.datatypes.MultiSiteBoolean;
import xoc.dta.datatypes.MultiSiteLong;

public class BesDsa_EfuseData_BT1502  implements BesDsa_EfuseData{

    @Override
    public MultiSiteLong efuseDataPreprocessing(int efuseAddr) {
        MultiSiteLong writeData_Efuse=new MultiSiteLong(0);
        MultiSiteLong temp=new MultiSiteLong(0);

        switch (efuseAddr) {
        case 0:
            return null;

        case 1:
            return new MultiSiteLong(0x0003);

        case 2:
            return null;

        case 3:
            writeData_Efuse.set(StaticFields.VSENSE_3P2V);
            for(int site: StaticFields.VSENSE_3P2V.getActiveSites()) {
                if(writeData_Efuse.get(site)<17000 || writeData_Efuse.get(site)>19000)
                {
                    writeData_Efuse.set(site, 0x0);
                }
            }
            return writeData_Efuse;

        case 4:
            writeData_Efuse.set(StaticFields.VSENSE_4P2V);
            for(int site: StaticFields.VSENSE_4P2V.getActiveSites()) {
                if(writeData_Efuse.get(site)<23000 || writeData_Efuse.get(site)>25000) {
                    writeData_Efuse.set(site, 0x0);
                }
            }
            return writeData_Efuse;

        case 5:
//            writeData_Efuse.set(contex, value)
            for(int site: StaticFields.VSENSE_4P2V.getActiveSites())
            {
//                writeData_Efuse.set(site, 0x3700+site<<1);
                writeData_Efuse.set(site, 0x37);
            }

//            writeData_Efuse.set( (StaticFields.efuse_DirectionBit_H.leftShift(15)).and(0x8000) );
            return writeData_Efuse;

        case 6:
            return null;

        case 7:
            //Bit[4:0]:dcdc1 cal shift step, range 0~31
            //Bit[5]:dcdc1 sign, 1 means -, 0 means +
            //Bit[9:6]:dcdc2 cal shift step, range 0~15
            //Bit[10]:dcdc2 sign, 1 means -, 0 means +
            //Bit[14:11]:dcdc3 cal shift step, range 0~15
            //Bit[15]:dcdc3 sign, 1 means -, 0 means +
            //when shift step == 0, sign be 1
            writeData_Efuse.set(0x0);
            for(int site: StaticFields.cal_Shift_Vcore.getActiveSites())
            {
                long temp11=0;
                temp11=Math.abs(StaticFields.cal_Shift_Vcore.get(site));
                temp11=temp11&0x1f+writeData_Efuse.get(site);
                writeData_Efuse.set(site,temp11);

                temp11=0;
                temp11=StaticFields.cal_Shift_Vcore.get(site)<=0?1:0;
                temp11=temp11<<5;
                temp11=writeData_Efuse.get(site)+temp11;
                writeData_Efuse.set(site,temp11);

                temp11=0;
                temp11=Math.abs(StaticFields.cal_Shift_Vana.get(site));
                temp11=temp11&0xf;
                temp11=(temp11<<6)+writeData_Efuse.get(site);
                writeData_Efuse.set(site,temp11);

                temp11=0;
                temp11=StaticFields.cal_Shift_Vana.get(site)<=0?1:0;
                temp11=(temp11<<10);
                temp11=writeData_Efuse.get(site)+temp11;
                writeData_Efuse.set(site,temp11);

                temp11=0;
                temp11=Math.abs(StaticFields.cal_Shift_Vcodec.get(site));
                temp11=temp11&0xf;
                temp11=(temp11<<11)+writeData_Efuse.get(site);
                writeData_Efuse.set(site,temp11);

                temp11=0;
                temp11=StaticFields.cal_Shift_Vcodec.get(site)<=0?1:0;
                temp11=(temp11<<15);
                temp11=writeData_Efuse.get(site)+temp11;
                writeData_Efuse.set(site,temp11);
            }
            return writeData_Efuse;

        case 8:
            //Bit[2:0]:tx cal freq L shift step, range 0~7
            //Bit[3]:tx cal sign, 1 means +, 0 means -
            //Bit[6:4]:tx cal freq M cal shift step, range 0~7
            //Bit[7]:tx cal sign, 1 means +, 0 means -
            //Bit[10:8]:tx cal freq H cal shift step, range 0~7
            //Bit[11]:tx cal sign, 1 means +, 0 means -
            //when shift step == 0, sign be 1

            writeData_Efuse.set(0x0);
            for(int site: StaticFields.BTTX_PWR_Marker_L.getActiveSites())
            {
                long temp_tx=0;
                temp_tx=Math.abs(StaticFields.BTTX_PWR_Marker_L.get(site));
                temp_tx=temp_tx&0x7+writeData_Efuse.get(site);
                writeData_Efuse.set(site,temp_tx);
                temp_tx=0;
                temp_tx=StaticFields.BTTX_PWR_Marker_L.get(site)>=0?1:0;
                temp_tx=temp_tx<<3;
                temp_tx=writeData_Efuse.get(site)+temp_tx;
                writeData_Efuse.set(site,temp_tx);

                temp_tx=0;
                temp_tx=Math.abs(StaticFields.BTTX_PWR_Marker_M.get(site));
                temp_tx=temp_tx&0x7;
                temp_tx=(temp_tx<<4)+writeData_Efuse.get(site);
                writeData_Efuse.set(site,temp_tx);

                temp_tx=0;
                temp_tx=StaticFields.BTTX_PWR_Marker_M.get(site)>=0?1:0;
                temp_tx=(temp_tx<<7);
                temp_tx=writeData_Efuse.get(site)+temp_tx;
                writeData_Efuse.set(site,temp_tx);

                temp_tx=0;
                temp_tx=Math.abs(StaticFields.BTTX_PWR_Marker_H.get(site));
                temp_tx=temp_tx&0x7;
                temp_tx=(temp_tx<<8)+writeData_Efuse.get(site);
                writeData_Efuse.set(site,temp_tx);

                temp_tx=0;
                temp_tx=StaticFields.BTTX_PWR_Marker_H.get(site)>=0?1:0;
                temp_tx=(temp_tx<<11);
                temp_tx=writeData_Efuse.get(site)+temp_tx;
                writeData_Efuse.set(site,temp_tx);
            }

            return writeData_Efuse;

        case 9:
            return null;

        case 10:
            return null;

        case 11:
            return null;

        case 12:
            return null;

        case 13:
            writeData_Efuse.set(StaticFields.temperatureSensor);
            for(int site: StaticFields.temperatureSensor.getActiveSites()) {
                if(writeData_Efuse.get(site)<20100 || writeData_Efuse.get(site)>23100) {
                    writeData_Efuse.set(site, 0);
                }
            }
            return writeData_Efuse;

        case 14:
            return null;

        case 15:
            return null;

        default:
            return null;
        }
    }


    @Override
    public MultiSiteBoolean efuseWriteFlag(MultiSiteLong readData_Pre, boolean isEfuse ) {
        MultiSiteBoolean multiSiteEfuseFlag=new MultiSiteBoolean();
        for(int site :readData_Pre.getActiveSites()) {
            if(isEfuse && readData_Pre.get(site)==0 ) {
                multiSiteEfuseFlag.set(site, true);
            }
            else {
                multiSiteEfuseFlag.set(site, false);
            }
        }
        return multiSiteEfuseFlag;
    }

    @Override
    public boolean efuseSkipFlag(MultiSiteBoolean multiSiteEfuseFlag ) {
        boolean skip_Efuse=true;
        for(int site :multiSiteEfuseFlag.getActiveSites()) {
            if(multiSiteEfuseFlag.get(site)) {
                skip_Efuse=false;
                break;
            }
        }
        return skip_Efuse;
    }

    @Override
    public MultiSiteLong dataEvaluation(int efuseAddr,MultiSiteLong writeData_Efuse, MultiSiteBoolean multiSiteEfuseFlag, MultiSiteLong readData_Pre, MultiSiteLong readData_Post) {
        MultiSiteLong passFlag=new MultiSiteLong(0);
        for(int site :writeData_Efuse.getActiveSites()) {
            if(multiSiteEfuseFlag.get(site)) {//FT
                if(readData_Post.get(site).equals(writeData_Efuse.get(site)) && readData_Post.get(site)!=0x0 && readData_Post.get(site)!=0xffff) {
                    passFlag.set(site, 1);
                }
            }
            else {//QA or FT_RT which address has been fused
                switch (efuseAddr) {
                case 0:
                    if(readData_Pre.get(site).equals(writeData_Efuse.get(site))) {
                        passFlag.set(site, 1);
                    }
                    break;
                case 1:
                    if(readData_Pre.get(site).equals(writeData_Efuse.get(site))) {
                        passFlag.set(site, 1);
                    }
                    break;
                case 2:
                    if(readData_Pre.get(site).equals(writeData_Efuse.get(site))) {
                        passFlag.set(site, 1);
                    }
                    break;
                case 3:
                    if(Math.abs( readData_Pre.get(site)-writeData_Efuse.get(site))<300) {
//                    if(readData_Pre.get(site).equals(writeData_Efuse.get(site))) {
                        passFlag.set(site, 1);

                    }
                    break;
                case 4:
                    if(Math.abs( readData_Pre.get(site)-writeData_Efuse.get(site))<300) {
                        passFlag.set(site, 1);
                    }
                    break;
                case 5:

                    if(readData_Pre.get(site).equals(writeData_Efuse.get(site)))
                    {
                        passFlag.set(site, 1);
                    }
                    break;
                case 6:
                    if(readData_Pre.get(site).equals(writeData_Efuse.get(site))) {
                        passFlag.set(site, 1);
                    }
                    break;
                case 7:
                    if(Math.abs((Math.abs(StaticFields.cal_Shift_Vcore.get(site)) & 0x1f)-(readData_Pre.get(site)&0x1f))<5        &&
                            Math.abs((Math.abs(StaticFields.cal_Shift_Vana.get(site)) & 0xf)-((readData_Pre.get(site)>>6) & 0xf))<5    &&
                            Math.abs((Math.abs(StaticFields.cal_Shift_Vcodec.get(site)) & 0xf)-((readData_Pre.get(site)>>11) & 0xf))<5    )
                    {
                        passFlag.set(site, 1);
                    }
                    break;
                case 8:
//                    if(readData_Pre.get(site).equals(writeData_Efuse.get(site))) {
                        passFlag.set(site, 1);
//                    }
                    break;
                case 9:
                    if(readData_Pre.get(site).equals(writeData_Efuse.get(site))) {
                        passFlag.set(site, 1);
                    }
                    break;
                case 10:
                    if(readData_Pre.get(site).equals(writeData_Efuse.get(site))) {
                        passFlag.set(site, 1);
                    }
                    break;
                case 11:
                    if(readData_Pre.get(site).equals(writeData_Efuse.get(site))) {
                        passFlag.set(site, 1);
                    }
                    break;
                case 12:
                    if(readData_Pre.get(site).equals(writeData_Efuse.get(site))) {
                        passFlag.set(site, 1);
                    }
                    break;
                case 13:
                    if(Math.abs( readData_Pre.get(site)-writeData_Efuse.get(site))<400) {
                        passFlag.set(site, 1);
                    }
                    break;
                case 14:
                    if(Math.abs( readData_Pre.get(site)-writeData_Efuse.get(site))<100) {
                        passFlag.set(site, 1);
                    }
                    break;
                case 15:
                    MultiSiteLong Wifi_0x37_addr = new MultiSiteLong();
                    MultiSiteLong Wifi_0x34_addr = new MultiSiteLong();
                    MultiSiteLong Wifi_0x37_w = new MultiSiteLong();
                    MultiSiteLong Wifi_0x34_w = new MultiSiteLong();
                    Wifi_0x37_addr.set(site, (readData_Pre.get(site)& 0x1f));
                    Wifi_0x34_addr.set(site, ((readData_Pre.get(site)>>5) & 0x1f));    //Wifi_0x34_addr.set(site, ((readData_Pre.get(site)>>8) & 0xff));
                    Wifi_0x37_w.set(site, (writeData_Efuse.get(site)& 0x1f));
                    Wifi_0x34_w.set(site, ((writeData_Efuse.get(site)>>5) & 0x1f));
                    long delta1=Math.abs(Wifi_0x37_addr.get(site)-Wifi_0x37_w.get(site));// diff <= 1 step
                    long delta2=Math.abs(Wifi_0x34_addr.get(site)-Wifi_0x34_w.get(site));// diff <= 1 step

//                    if(Math.abs(Wifi_0x37_addr.get(site)-Wifi_0x37_w.get(site)) <= 1 && Math.abs(Wifi_0x34_addr.get(site)-Wifi_0x34_w.get(site)) <=1 && ((readData_Pre.get(site)&0x8000) == 0x8000) ) { // diff <= 1 step
//                            if ((delta1 & delta2) ==1) { // diff <= 1 step
                    if((readData_Pre.get(site)&0x0400) == 0x0400 ) { //diff <= 2 step
                        if (delta1==2) {
                            if(delta2 ==0 ){
                                passFlag.set(site,1);
                            }
                            else {
                                passFlag.set(site,0);
                            }
                        }
                        else if (delta1==1){
                            if(delta2 <= 1){// diff <= 2 step
                                passFlag.set(site,1);
                            }
                            else {
                                passFlag.set(site,0);
                            }
                        }
                        else if (delta1==0){
                            if(delta2 <= 2){// diff <= 2 step
                                passFlag.set(site,1);
                            }
                            else {
                                passFlag.set(site,0);
                            }
                        }
                        else {
                            passFlag.set(site,0);
                        }
                    }
                    else {
                        passFlag.set(site,0);
                    }
                    break;

                default:
                    break;
                }

            }

        }
        return passFlag;
    }









}
