package besLib.dsa;

import xoc.dsa.DeviceSetupFactory;
import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDigInOut.IResult;
import xoc.dta.measurement.IMeasurement;

/**
 * This class is used to operate BES chip DFT pattern run with V93K SMT8
 * @version V1.0
 * @author Weng Yongxin
 **/
public class BesDsa_DFT {

    private String prefix;

    /**
     * Constructor <br>
     * Generate DFT pattern run SSF.<br>
     *
     * @param prefix The String to use as a root sub-directory under which all files generated for this instance are created.
     */
    public BesDsa_DFT(String prefix){
        this.prefix=prefix;
    }

    /**
     * Single pattern run.
     *
     * @param pattern DFT pattern
     * @param spec The file path to import into the Spec setup
     * @param measurement An instance of IMeasurement interface
     * @return Instance of IDeviceSetup interface.
     */
    public IDeviceSetup patternRun(String pattern, IMeasurement measurement, String spec) {
        IDeviceSetup[] ds=new IDeviceSetup[1];
        ds[0]=DeviceSetupFactory.createInstance(prefix);
        ds[0].importSpec(spec);
        ds[0].patternCall(pattern);
        measurement.setSetups(ds[0]);

        return ds[0];
    }

    /**
     * This methods is used to configure how digital results, for example failing cycles...
     *
     * @param ds An instance of IDeviceSetup interface.
     * @param signals Signal pin name
     * @param maxFailedCycles Specifies the maximum number of fail cycles to be recorded
     */
    public void configPatternRun(IDeviceSetup ds,String signals, int maxFailedCycles) {
        IResult res1_DigInOut=ds.addDigInOut(signals).result();
        res1_DigInOut.callPassFail().setEnabled(true);
        res1_DigInOut.cyclePassFail().setEnabled(true).setGranularityCycle().setMaxFailedCycles(maxFailedCycles);
    }



}
