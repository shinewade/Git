package besLib.dsa.efuse;

import xoc.dta.datatypes.MultiSiteBoolean;
import xoc.dta.datatypes.MultiSiteLong;

public interface BesDsa_EfuseData  {
    /**
     * Process e-fuse data for Writing
     * @param efuseAddr e-fuse address
     * @return Processed data for e-fuse
     */
    public MultiSiteLong efuseDataPreprocessing(int efuseAddr);

    /**
     * Get MultiSiteBoolean flag to decide whether write e-fuse per site
     *
     * @param readData_Pre read e-fuse data before writing
     * @param isEfuse  Flag to decide whether write e-fuse
     * @return   Flag to decide whether write e-fuse per site
     */
    public MultiSiteBoolean efuseWriteFlag(MultiSiteLong readData_Pre, boolean isEfuse );

    /**
     * Judge whether e-fuse has been written before (QA) to decide if need to run measurement object of writing e-fuse.
     * @param multiSiteEfuseFlag MultiSiteBoolean flag to decide whether write e-fuse per site
     * @return boolean flag to judge whether write e-fuse
     */
    public boolean efuseSkipFlag(MultiSiteBoolean multiSiteEfuseFlag );

    public MultiSiteLong dataEvaluation(int efuseAddr, MultiSiteLong writeData_Efuse, MultiSiteBoolean multiSiteEfuseFlag, MultiSiteLong readData_Pre, MultiSiteLong readData_Post);




}
