import configuration.Groups;

spec Wvt_DFT {
    setup digInOut OTHER_GRP+USB_GRP+ANA_GRP{
        wavetable Wvt_DFT_Bist {
            xModes=1;
            //Drive
            0 : d1:0;
            1 : d1:1;
            Z : d1:Z;
            N : d1:N;
            //Recieve
            L : d1:Z r1:L;
            H : d1:Z r1:H;
            X : d1:Z r1:X;
        }
    }
    setup digInOut CTRL_GRP+I2C_GRP+GPIO_GRP-GPIO_11{
        wavetable Wvt_DFT_Bist {
            xModes=1;
            //Drive
            0 : d1:0;
            1 : d1:1;
            Z : d1:Z;
            N : d1:N;
            //Recieve
            L : d1:Z r1:L;
            H : d1:Z r1:H;
            M : d1:Z r1:M;
            X : d1:Z r1:X;
        }
    }
    setup digInOut GPIO_11 {    //DFT CLK
        wavetable Wvt_DFT_Bist {
            xModes=1;
            //Drive
            0 : d1:0;
            a : d1:1;
            1 : d1:0 d2:1 d3:0;
            Z : d1:Z;
            N : d1:N;
            //Recieve
            L : d1:Z r1:L;
            H : d1:Z r1:H;
            X : d1:Z r1:X;
        }
    }
}
