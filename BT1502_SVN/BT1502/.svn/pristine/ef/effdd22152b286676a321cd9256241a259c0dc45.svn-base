package BT1502_pro_tml.EFFUSE;

import BT1502_pro_tml.Global.StaticFields;
import besLib.dsa.efuse.BesDsa_Efuse;
import besLib.dsa.efuse.BesDsa_EfuseData;
import xoc.dta.TestMethod;
import xoc.dta.annotations.In;
import xoc.dta.datatypes.MultiSiteBoolean;
import xoc.dta.datatypes.MultiSiteLong;
import xoc.dta.measurement.IMeasurement;
import xoc.dta.testdescriptor.IParametricTestDescriptor;

public class BesEfuse extends TestMethod {
    @In public String spec_measurement;
    @In public int efuseAddr;
    @In public boolean isEfuse;

    public IMeasurement measurement;
    public IMeasurement measurement_read;

    public IParametricTestDescriptor ptd_EfuseData_pre;
    public IParametricTestDescriptor ptd_EfuseData_post;
    public IParametricTestDescriptor ptd_EfuseData;
    public IParametricTestDescriptor ptd_PassFlag;

    BesDsa_Efuse efuse=new BesDsa_Efuse_BT1502();
    BesDsa_EfuseData efuseData=new BesDsa_EfuseData_BT1502();

    @Override
    public void setup() {
        efuse.genDsa_ReadEfuse(efuseAddr, measurement_read, spec_measurement, StaticFields.prog_name);
        efuse.genDsa_WriteEfuse(measurement, spec_measurement, StaticFields.prog_name);
    }

    @Override
    public void execute() {
        MultiSiteBoolean multiSiteEfuseFlag=new MultiSiteBoolean(isEfuse);
        MultiSiteLong readData_Pre=new MultiSiteLong();
        MultiSiteLong readData_Post=new MultiSiteLong();
        MultiSiteLong writeData_Efuse=new MultiSiteLong();
        MultiSiteLong passFlag=new MultiSiteLong(0);

        /*********************************************************************************
         ******** step1: process e-fuse data which need to be written ********************
         ********************************************************************************/
        switch (efuseAddr) {
        case 0:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(0));//User-defined
            break;
        case 1:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(1));//User-defined
            break;
        case 2:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(2));//User-defined
            break;
        case 3:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(3));//User-defined
            break;
        case 4:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(4));//User-defined
            break;
        case 5:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(5));//User-defined
            break;
        case 6:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(6));//User-defined
            break;
        case 7:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(7));//User-defined
            break;
        case 8:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(8));//User-defined
            break;
        case 9:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(9));//User-defined
            break;
        case 10:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(10));//User-defined
            break;
        case 11:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(11));//User-defined
            break;
        case 12:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(12));//User-defined
            break;
        case 13:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(13));//User-defined
            break;
        case 14:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(14));//User-defined
            break;
        case 15:
            writeData_Efuse.set(efuseData.efuseDataPreprocessing(15));//User-defined
            break;

        default:
            break;
        }

        /*********************************************************************************
         ******** step2: read initial value before e-fuse ********************************
         *********************************************************************************/
        readData_Pre.set( efuse.readEfuse(measurement_read) );

        /*********************************************************************************
         ******** step3: Judge if or not write e-fuse ************************************
         *********************************************************************************/
        multiSiteEfuseFlag.set( efuseData.efuseWriteFlag(readData_Pre, isEfuse) );//User-defined
        boolean skip_Efuse=efuseData.efuseSkipFlag(multiSiteEfuseFlag);//User-defined

        if(!skip_Efuse) {
            efuse.writeEfuse(efuseAddr, writeData_Efuse, multiSiteEfuseFlag, measurement,context);//User-defined
        }

        /*********************************************************************************
         ******** step4: read value after e-fuse *****************************************
         *********************************************************************************/
        readData_Post.set( efuse.readEfuse(measurement_read) );

        /*********************************************************************************
         ******** step5: judge e-fuse result *********************************************
         *********************************************************************************/
        passFlag.set(efuseData.dataEvaluation(efuseAddr, writeData_Efuse, multiSiteEfuseFlag, readData_Pre, readData_Post));//User-defined


//        ptd_EfuseData.setLowLimitToPass();
//        ptd_EfuseData.setHighLimitToPass();
//        ptd_EfuseData_pre.setLowLimitToPass();
//        ptd_EfuseData_pre.setHighLimitToPass();
//        ptd_EfuseData_post.setLowLimitToPass();
//        ptd_EfuseData_post.setHighLimitToPass();

        ptd_EfuseData.evaluate(writeData_Efuse);
        ptd_EfuseData_pre.evaluate(readData_Pre);
        ptd_EfuseData_post.evaluate(readData_Post);

        //need test table
        ptd_PassFlag.evaluate(passFlag);
    }

}
