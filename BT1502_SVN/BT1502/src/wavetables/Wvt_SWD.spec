import configuration.Groups;

spec Wvt_SWD {

    setup digInOut GPIO_GRP+OTHER_GRP+USB_GRP-GPIO_01{
        wavetable Wvt_SWD {
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
    setup digInOut CTRL_GRP {
        wavetable Wvt_SWD {
            xModes = 1;
            //Drive
            0 : d1:0;
            1 : d1:1;
            Z : d1:Z;
            N : d1:N;

            brk 0;
        }
    }
    setup digInOut I2C_SDA {
        result.capture.enabled = true;
        wavetable Wvt_SWD {
            xModes = 1;
            //Drive
            0 : d1:0;
            1 : d1:1;
            a : d1:0 d2:1;
            b : d1:1 d2:0;
            d : d1:0 d3:1;
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
    setup digInOut I2C_SCL {
        wavetable Wvt_SWD {
            xModes = 1;
            //Drive
            0: d1:0;
            a: d1:0 d2:1 d3:0;
            1: d1:1;
            b: d1:1 d3:0;
            Z : d1:Z;
            N : d1:N;

            brk 1;
        }
    }

    setup digInOut GPIO_01 {
        wavetable Wvt_SWD {
            xModes = 1;
            //Drive
            0: d1:0;
            1: d1:0 d2:1 d3:0;
            Z : d1:Z;
            N : d1:N;
            brk 0;
        }
  }

}
