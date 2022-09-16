import timing.TimSet_I2C.TimSet_I2C_Variables;
import configuration.Groups;

spec TimSet_I2C_DigInOut {
    set timingSet_1;

    setup digInOut GPIO_GRP+OTHER_GRP+USB_GRP+ANA_GRP{
        set timing timingSet_1 {
            period = Period_ns_I2C;
            d1 = 0 ns;
            r1 = 0.5 * Period_ns_I2C;
        }
    }
    setup digInOut CTRL_GRP+VBAT_SENSE{
        set timing timingSet_1 {
            period = Period_ns_I2C;
            d1 = 0 ns;
        }
    }
    setup digInOut I2C_SDA {
        set timing timingSet_1 {
            period = Period_ns_I2C;
            d1 = 0.0 * Period_ns_I2C;
            d2 = 1.0 / 3.0 * Period_ns_I2C;
            d3 = 2.0 / 3.0 * Period_ns_I2C;
            r1 = 0.6 * Period_ns_I2C;
        }
    }
    setup digInOut I2C_SCL {
        set timing timingSet_1 {
            period = Period_ns_I2C;
            d1 = 0.0 * Period_ns_I2C;
            d2 = 1.0 / 3.0 * Period_ns_I2C;
            d3 = 2.0 / 3.0 * Period_ns_I2C;
        }
    }


}
