import configuration.Groups;
import timing.TimSet_DFT.TimSet_DFT_Variables;
spec TimSet_DFT_DigInOut {
    set timingSet_1;
    setup digInOut OTHER_GRP+USB_GRP+ANA_GRP{
        set timing timingSet_1{
            period = Period_ns_DFT;
            d1 = 0.0 *Period_ns_DFT;
            r1 = Stb_ns_DFT;
        }
    }
    setup digInOut CTRL_GRP+I2C_GRP +VBAT_SENSE{
        set timing timingSet_1 {
            period = Period_ns_DFT;
            d1 = 0.0 *Period_ns_DFT;
            d2 = 0.25 *Period_ns_DFT;
            d3 = 0.75 *Period_ns_DFT;
            r1 = Stb_ns_DFT;
        }
    }
    setup digInOut GPIO_GRP-GPIO_11{
        set timing timingSet_1{
            period = Period_ns_DFT;
            d1 = 0.0 *Period_ns_DFT;
            r1 = Stb_ns_DFT;
        }
    }
    setup digInOut GPIO_11 {
        set timing timingSet_1 {
            period = Period_ns_DFT;
            d1 = 0.0 *Period_ns_DFT;
            d2 = 0.25 *Period_ns_DFT;
            d3 = 0.75 *Period_ns_DFT;
            r1 = Stb_ns_DFT;
        }
    }
}
