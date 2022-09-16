package BT1502_pro_tml.Global;

import xoc.dta.datatypes.MultiSiteBoolean;
import xoc.dta.datatypes.MultiSiteDouble;
import xoc.dta.datatypes.MultiSiteLong;

public final class StaticFields{


   public static boolean debugMode=false;//false true

   /*********** Test Program Variables ************/
   public static String dev_name     ;
   public static String dev_ssv      ;
   public static String dev_sub      ;
   public static String dev_sub_ext  ;
   public static String dev_ver      ;
   public static String effuse_a1    ;
   public static String flash_type   ;
   public static String ftqa         ;
   public static String psram_type   ;
   public static String vcore_v      ;
   public static String vana_v       ;
   public static String vcodec_v     ;
   public static String lb_name      ;
   public static String prog_name    ;
   public static String pmic_name    ;

   public static String vcore_DFT_v; //add By wuhan for DFT signoff

   /*********** LoadBoard ATT Value ************/
   public static MultiSiteLong   LBID = new MultiSiteLong() ;

   /*********** LoadBoard ATT Value ************/
   public static MultiSiteDouble att_bt = new MultiSiteDouble();

   /*********** Temperature Sensor  ************/
   public static MultiSiteLong temperatureSensor=new MultiSiteLong();

   /*********** DCDC Mode Cal ************/
   public static MultiSiteLong regValue_DCBuck_Vcore =new MultiSiteLong();
   public static MultiSiteLong regValue_DCBuck_Vana =new MultiSiteLong();
   public static MultiSiteLong regValue_DCBuck_Vcodec =new MultiSiteLong();

   public static MultiSiteLong regValue_DCBuck_Vcore_DFT =new MultiSiteLong(); //add By wuhan for DFT signoff

   public static MultiSiteDouble Vcore_DCBuck_Cal =new MultiSiteDouble();

   public static MultiSiteDouble Vcore_DFT_DCBuck_Cal =new MultiSiteDouble();

   public static MultiSiteDouble Vana_DCBuck_Cal =new MultiSiteDouble();
   public static MultiSiteDouble Vcodec_DCBuck_Cal =new MultiSiteDouble();

   public static MultiSiteLong cal_Shift_Vcore =new MultiSiteLong();

   public static MultiSiteLong cal_Shift_Vcore_DFT =new MultiSiteLong(); //add By wuhan for DFT signoff

   public static MultiSiteLong cal_Shift_Vana =new MultiSiteLong();
   public static MultiSiteLong cal_Shift_Vcodec =new MultiSiteLong();

   /*********** BT TX Dly Cap Cal  ************/
   public static MultiSiteBoolean passflag_cal=new MultiSiteBoolean();
   public static MultiSiteLong   lBt_CalValue_0x28=new MultiSiteLong();

   /*********** BT TX PWR  ************/
   public static MultiSiteDouble BTTX_PWR_L = new MultiSiteDouble();
   public static MultiSiteDouble BTTX_PWR_H = new MultiSiteDouble();
   public static MultiSiteDouble BTTX_PWR_M = new MultiSiteDouble();
   public static MultiSiteLong BTTX_PWR_Marker_L = new MultiSiteLong();
   public static MultiSiteLong BTTX_PWR_Marker_H = new MultiSiteLong();
   public static MultiSiteLong BTTX_PWR_Marker_M = new MultiSiteLong();

   /*********** BT TX PWR Cal ************/
   public static MultiSiteLong   efuse_DirectionBit_L=new MultiSiteLong(1);     //bit[8 ], ">": bit 1, "<": bit 0, efuse7
   public static MultiSiteLong   efuse_DirectionBit_M=new MultiSiteLong(1);     //bit[12], ">": bit 1, "<": bit 0, efuse7
   public static MultiSiteLong   efuse_DirectionBit_H=new MultiSiteLong(1);     //bit[15], ">": bit 1, "<": bit 0, efuse5
   public static MultiSiteLong   efuse_ABS_CalShift_L=new MultiSiteLong(0);     //bit[7 :5 ] efuse7
   public static MultiSiteLong   efuse_ABS_CalShift_M=new MultiSiteLong(0);     //bit[11:9 ] efuse7
   public static MultiSiteLong   efuse_ABS_CalShift_H=new MultiSiteLong(0);     //bit[15:13] efuse7

   /*********** VSENSE ADC  Cal ************/
   public static MultiSiteLong VSENSE_3P2V = new MultiSiteLong();
   public static MultiSiteLong VSENSE_4P2V = new MultiSiteLong();



}