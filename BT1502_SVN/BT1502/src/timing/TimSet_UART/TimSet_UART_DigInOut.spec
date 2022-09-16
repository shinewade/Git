import timing.TimSet_UART.TimSet_UART_Variables;
import configuration.Groups;

spec TimSet_UART_DigInOut {
    set timingSet_1;

    setup digInOut GPIO_GRP+OTHER_GRP+USB_GRP+ANA_GRP{
        set timing timingSet_1 {
            period = Period_ns_UART;
            d1 = 0 ns;
            r1 = 0.5 * Period_ns_UART;
        }
    }
    setup digInOut CTRL_GRP+VBAT_SENSE{
        set timing timingSet_1 {
            period = Period_ns_UART;
            d1 = 0 ns;
        }
    }
    setup digInOut I2C_GRP { //UART TX & RX
        set timing timingSet_1 {
            period = Period_ns_UART;
            d1 = 0.0 * Period_ns_UART;
            d2 = 1.0 / 3.0 * Period_ns_UART;
            d3 = 2.0 / 3.0 * Period_ns_UART;
            r1 = 0.6 * Period_ns_UART;
        }
    }


}
