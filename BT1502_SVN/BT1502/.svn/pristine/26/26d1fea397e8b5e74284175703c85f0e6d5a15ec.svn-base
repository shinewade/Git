import configuration.Groups;

spec Wvt_OS {
  setup digInOut All_Digital{
    wavetable Wvt_OS {
      xModes = 1;
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

      brk Z;
    }
  }
}
