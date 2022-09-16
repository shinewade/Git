import configuration.Groups;

spec Wvt_UART {

    setup digInOut GPIO_GRP+OTHER_GRP+USB_GRP+ANA_GRP{
        wavetable Wvt_UART {
            xModes = 1;
            //Drive
            0 : d1:0;
            1 : d1:1;
            Z : d1:Z;
            N : d1:N;
            //Recieve
            L : d1:Z r1:L;
            H : d1:Z r1:H;
            X : d1:Z r1:X;
            C : d1:Z r1:C;
        }
    }
    setup digInOut CTRL_GRP+VBAT_SENSE {
        wavetable Wvt_UART {
            xModes = 1;
            //Drive
            0 : d1:0;
            1 : d1:1;
            Z : d1:Z;
            N : d1:N;

            brk 0;
        }
    }
    setup digInOut I2C_GRP { //UART TX & RX
        result.capture.enabled = true;
        wavetable Wvt_UART {
            xModes = 1;
            //Drive
            0 : d1:0;
            1 : d1:1;
            Z : d1:Z;
            N : d1:N;
            //Recieve
            L : d1:Z r1:L;
            H : d1:Z r1:H;
            X : d1:Z r1:X;
            C : d1:Z r1:C;
            brk 1;
        }
    }

}
