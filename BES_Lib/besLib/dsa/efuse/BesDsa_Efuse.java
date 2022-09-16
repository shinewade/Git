package besLib.dsa.efuse;

import com.advantest.itee.tmapi.protocolaccess.IProtocolInterfaceResults;
import com.advantest.itee.tmapi.protocolaccess.ProtocolAccess;

import xoc.dta.ITestContext;
import xoc.dta.datatypes.MultiSiteBoolean;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;

public abstract class BesDsa_Efuse {

    /**
     * Customized BES e-fuse read code per project, please use it in setup() methods !
     *
     * @param efuseAddr e-fuse address 0- 15
     * @param measurement An instance of IMeasurement interface
     * @param spec The file path to import into the Spec setup
     * @param prefix The String to use as a root sub-directory under which all files generated for this instance are created.
     */
    public abstract void genDsa_ReadEfuse(int efuseAddr, IMeasurement measurement, String spec, String prefix);

    /**
     * Customized BES e-fuse write code per project, please use it in setup() methods !
     *
     * @param measurement An instance of IMeasurement interface
     * @param spec The file path to import into the Spec setup
     * @param prefix The String to use as a root sub-directory under which all files generated for this instance are created.
     */
    public abstract void genDsa_WriteEfuse(IMeasurement measurement, String spec, String prefix);

    /**
     * Read value of BES e-fuse register,please use it in execute() methods !
     *
     * @param measurement An instance of IMeasurement interface
     * @return e-fuse read data in MultiSiteLong type.
     */
    public MultiSiteLong readEfuse(IMeasurement measurement) {
        measurement.execute();
        IProtocolInterfaceResults res_PA=ProtocolAccess.preserveResults(measurement);
        MultiSiteLong resHigh=res_PA.getResult("efuse_Hihg");
        MultiSiteLong resLow=res_PA.getResult("efuse_Low");
        return resHigh.or(resLow);
    }

    /**
     * Customized BES e-fuse write code per project, please use it in execute() methods !
     *
     * @param efuseAddr e-fuse address 0- 15
     * @param efuseData e-fuse data
     * @param isEfuse e-fuse flag, true: write e-fuse, false: skip e-fuse
     * @param measurement An instance of IMeasurement interface
     * @param context This instance enables you to access the test context of a measurement.
     *
     */
    public abstract void writeEfuse(int efuseAddr, MultiSiteLong efuseData, MultiSiteBoolean isEfuse, IMeasurement measurement, ITestContext context);

}
