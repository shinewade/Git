import timing.TimSet_SWD.TimSet_SWD_Variables;
import configuration.Groups;

spec TimSet_SWD_DigInOut {
    set timingSet_1;

    setup digInOut GPIO_GRP+OTHER_GRP+USB_GRP-GPIO_01{
        set timing timingSet_1 {
            period = Period_ns_SWD;
            d1 = 0 ns;
            r1 = 0.5 * Period_ns_SWD;
        }
    }
    setup digInOut CTRL_GRP{
        set timing timingSet_1 {
            period = Period_ns_SWD;
            d1 = 0 ns;
        }
    }
    setup digInOut I2C_SDA {
        set timing timingSet_1 {
            period = Period_ns_SWD;
            d1 = 0.0 * Period_ns_SWD;
            d2 = 1.0 / 3.0 * Period_ns_SWD;
            d3 = 2.0 / 3.0 * Period_ns_SWD;
            r1 = 0.6 * Period_ns_SWD;
        }
    }
    setup digInOut I2C_SCL {
        set timing timingSet_1 {
            period = Period_ns_SWD;
            d1 = 0.0 * Period_ns_SWD;
            d2 = 1.0 / 3.0 * Period_ns_SWD;
            d3 = 2.0 / 3.0 * Period_ns_SWD;
        }
    }

    setup digInOut GPIO_01 {
        set timing timingSet_1 {
            period = Period_ns_SWD;
            d1 = 0 * Period_ns_SWD;
            d2 = 0.25 * Period_ns_SWD;
            d3 = 0.75 * Period_ns_SWD;
        }
    }


}
