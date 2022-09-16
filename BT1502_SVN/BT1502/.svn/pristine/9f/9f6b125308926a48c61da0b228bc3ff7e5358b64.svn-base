import levels.LevSet_I2C.LevSet_I2C_Variables;
import configuration.Groups;

spec LevSet_I2C_DigInOut {
    set levelSet_1;

    setup digInOut GPIO_GRP +OTHER_GRP{
        set level levelSet_1 {
            vih = OTHER_VIH;
            vil = OTHER_VIL;
            vol = OTHER_VOL;
            voh = OTHER_VOH;
            iol = OTHER_IOL;
            ioh = OTHER_IOH;
            vt  = OTHER_VT;
            term = load;
        }
    }
  setup digInOut ANA_GRP {
    set level levelSet_1 {
      vih = OTHER_VIH;
      vil = OTHER_VIL;
      vol = OTHER_VOL;
      voh = OTHER_VOH;
//      iol = OTHER_IOL;
//      ioh = OTHER_IOH;
//      vt = OTHER_VT;
//      term = load;
      term = hiz;
        }
    }

    setup digInOut USB_GRP {
        set level levelSet_1 {
            vih = 3.3 V;
            vil = 0 V;
            vol = USB_COMPARE;
            voh = USB_COMPARE;
            iol = OTHER_IOL;
            ioh = OTHER_IOH;
            vt  = OTHER_VT;
            term = load;
        }
    }
    setup digInOut CTRL_GRP {
        set level levelSet_1 {
            vih = PWR_VIH;
            vil = PWR_VIL;
            vol = PWR_VOL;
            voh = PWR_VOH;
            iol = OTHER_IOL;
            ioh = OTHER_IOH;
            vt  = OTHER_VT;
            term = load;
        }
    }
    setup digInOut I2C_GRP {
        set level levelSet_1 {
            vih = I2C_VIH;
            vil = I2C_VIL;
            vol = I2C_VOL;
            voh = I2C_VOH;
            iol = I2C_IOL;
            ioh = I2C_IOH;
            vt  = I2C_VT;
            term = load;
        }
    }
  setup digInOut VBAT_SENSE {
    set level levelSet_1 {
      vih = Vbat;
      vil = 0 V;
      term = hiz;
    }
  }


}
