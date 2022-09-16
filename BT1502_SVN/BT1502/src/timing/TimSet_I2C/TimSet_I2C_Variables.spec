parameter spec TimSet_I2C_Variables {
    // ----- For digInOut variables ------
    var Frequency Freq_MHz_I2C;
    var Time Period_ns_I2C = 1000.0 / Freq_MHz_I2C / 1000.0;
}
