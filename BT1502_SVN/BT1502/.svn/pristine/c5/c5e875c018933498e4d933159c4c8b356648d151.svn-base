import configuration.Groups;
spec Wvt_uart {
  setup digInOut I2C_SCL {
    wavetable default {
      xModes = 1;
      a: d1:0 d2:1 d3:0;
      0: d1:0;
      1: d1:1;
      b: d1:1 d3:0;
      X: d1:Z r1:X;
      L: d1:Z r1:L;
      H: d1:Z r1:H;
      Z: d1:Z;
      Q: d1:N;
      brk 1;
    }
  }
  setup digInOut I2C_SDA {
    result.capture.enabled = true;
    wavetable default {
      xModes = 1;
      0: d1:0;
      1: d1:1;
      a: d1:0 d2:1;
      b: d1:1 d2:0;
      L: d1:Z r1:L;
      H: d1:Z r1:H;
      X: d1:Z r1:X;
      d: d1:0 d3:1;
      C: d1:Z r1:C;
      Z: d1:Z;
      Q: d1:N;
      brk 1;
    }
  }
  setup digInOut VBAT_SENSE {
    wavetable default {
      xModes = 1;
      0: d1:0;
      1: d1:1;
      Z: d1:Z;
      Q: d1:N;
      brk 1;
    }
  }
  setup digInOut CTRL_GRP-POWKEY {
    wavetable default {
      xModes = 1;
      0: d1:0;
      1: d1:1;
      Z: d1:Z;
      Q: d1:N;
      brk 0;
    }
  }
  setup digInOut POWKEY {
    wavetable default {
      xModes = 1;
      0: d1:0;
      1: d1:1;
      Z: d1:Z;
      Q: d1:N;
    }
  }
  setup digInOut ANA_GRP+USB_GRP+GPIO_GRP+DSI_GRP+CLK32_GRP {
    result.capture.enabled = false;
    wavetable default {
      xModes = 1;
      0: d1:0;
      1: d1:1;
      L: d1:Z r1:L;
      H: d1:Z r1:H;
      X: d1:Z r1:X;
      C: d1:Z r1:C;
      Q: d1:N;
      a: d1:N;
    }
  }

}
