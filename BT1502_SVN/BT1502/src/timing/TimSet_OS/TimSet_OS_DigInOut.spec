import configuration.Groups;

spec TimSet_OS_DigInOut {
    set timingSet_1;

    setup digInOut All_Digital {
        set timing timingSet_1 {
            period = 5000 ns;
            d1 = 0 ns;
            r1 = 4000 ns;
        }
    }


}
