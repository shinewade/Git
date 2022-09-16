package besLib.dsa;

import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDcVI;
import xoc.dsa.ISetupDcVI.IImeas.SetupUngangMode;
import xoc.dsa.ISetupDcVI.SetupConnectMode;
import xoc.dsa.ISetupDigInOut;

/**
 * This class is used to operate BES chip PMIC with V93K SMT8
 * @version V1.0
 * @author Weng Yongxin
 **/
public class BesDsa_PMIC {

    private  IDeviceSetup ds;

    /**
     * Enum type for V93K Resource
     */
    public static enum V93K_Resource{
        PS1600,
        DPS128,
    }

    /**
     * Constructor
     *
     * @param ds instance of IDeviceSetup interface
     */
    public BesDsa_PMIC(IDeviceSetup ds) {
        this.ds=ds;
    }

    /**
     * Measure voltage with instrument connected in a high impedance mode
     *
     * @param signal Pin name
     * @param setDisconnect The value to assign to the disconnect property after measurement execution
     * @param vMeasAction Identifier of the vmeas action
     * @param v93K_Resource V93K hardware resource
     */
    public void vMeas_DcVI_Hiz(String signal, Boolean setDisconnect, String vMeasAction, V93K_Resource v93K_Resource) {
        switch (v93K_Resource) {
        case DPS128:
            ISetupDcVI dcVi_DPS128 = ds.addDcVI(signal).setConnect(true).setConnectMode(SetupConnectMode.highImpedance).setDisconnect(setDisconnect).setDisconnectModeHiz();
            dcVi_DPS128.vmeas(vMeasAction).setWaitTime("3 ms").setAverages(4);
            dcVi_DPS128.disconnect("disconnect_"+signal);
            break;
        case PS1600:
            ISetupDcVI dcVi_PS1600 = ds.addDcVI(signal).setConnect(true).setConnectMode(SetupConnectMode.highImpedance).setDisconnect(setDisconnect).setDisconnectModeHiz();
            dcVi_PS1600.vmeas(vMeasAction).setWaitTime("3 ms").setAverages(4).setHighAccuracy(true).setDirectEnabled();
            dcVi_PS1600.disconnect("disconnect_"+signal);
            break;
        default:
            break;
        }
    }

    /**
     * Measure voltage with DPS128 DcVI instrument connected in a standard mode
     *
     * <p>For force actions on DPS128-HC and DPS128-HV variants, level.vrange and level.irange must be given.
     * It is adequate that only these two properties are defined in the level group. Specifying level.vforce is not enforced.
     * Note that it is not allowed to change voltage or current ranges in an action. The ranges are selected by the level group properties.</p>
     *
     * @param signal Pin name
     * @param iForce  The value to assign to the forceValue property like "-10 mA" or "10 mA"
     * @param vExpected The value to assign to the vexpected property like "1.3 V".
     * @param iRange The value to assign to the irange property.
     * @param vRange The value to assign to the vrange property.
     * @param vClamp The value to assign to the vclamp property.
     * @param setDisconnect The value to assign to the disconnect property after measurement execution
     * @param iForceAction Identifier of the iforce action
     * @param vMeasAction Identifier of the vmeas action
     */
    public void vMeas_DPS128_DcVI_Std(String signal, String iForce, String vExpected, String iRange, String vRange, String vClamp, Boolean setDisconnect, String iForceAction, String vMeasAction) {
        ISetupDcVI dcVi_DPS128 = ds.addDcVI(signal).setConnect(true).setConnectMode(SetupConnectMode.standard).setDisconnect(setDisconnect).setDisconnectModeHiz();
        dcVi_DPS128.level().setIrange(iRange).setVrange(vRange);
        if(iForce.contains("-")) {
            dcVi_DPS128.iforce(iForceAction).setForceValue(iForce).setVexpected(vExpected).setVclampLow(vClamp);
        }
        else {
            dcVi_DPS128.iforce(iForceAction).setForceValue(iForce).setVexpected(vExpected).setVclampHigh(vClamp);

        }
        dcVi_DPS128.vmeas(vMeasAction).setWaitTime("2 ms").setAverages(4);
        dcVi_DPS128.disconnect("disconnect_"+signal);

    }

    /**
     * Measure voltage with PS1600 DcVI instrument connected in a standard mode
     *
     * @param signal Pin name
     * @param iForce  The value to assign to the forceValue property like "-10 mA" or "10 mA"
     * @param vClampHigh The value to assign to the vclampHigh property
     * @param vClampLow The value to assign to the vclampLow property
     * @param setDisconnect The value to assign to the disconnect property after measurement execution
     * @param iForceAction Identifier of the iforce action
     * @param vMeasAction Identifier of the vmeas action
     */
    public void vMeas_PS1600_DcVI_Std(String signal, String iForce, String vClampHigh, String vClampLow, Boolean setDisconnect, String iForceAction, String vMeasAction) {
        String disconnectAction="disconnect_"+signal;
        while (disconnectAction.contains(" ") || disconnectAction.contains("+")  ) {
            disconnectAction=disconnectAction.replaceAll(" ", "");
            disconnectAction=disconnectAction.replaceAll("\\+", "_");
        }
        ISetupDcVI dcVi_PS1600 = ds.addDcVI(signal).setConnect(true).setConnectMode(SetupConnectMode.standard).setDisconnect(setDisconnect).setDisconnectModeHiz();
        dcVi_PS1600.iforce(iForceAction).setForceValue(iForce).setVclampHigh(vClampHigh).setVclampLow(vClampLow).setKeepVclampEnabled();
        dcVi_PS1600.vmeas(vMeasAction).setWaitTime("2 ms").setAverages(4).setHighAccuracy(true);
        dcVi_PS1600.disconnect(disconnectAction);
    }

    /**
     * Measure voltage with PS1600 DigInOut instrument, AC relay is off.
     *
     * @param signal Pin name
     * @param iForce  The value to assign to the forceValue property like "-10 mA" or "10 mA"
     * @param vClampHigh The value to assign to the vclampHigh property.
     * @param vClampLow The value to assign to the vclampLow property.
     * @param setDisconnect The value to assign to the disconnect property after measurement execution
     * @param iForceAction Identifier of the iforce action
     * @param vMeasAction Identifier of the vmeas action
     */
    public void vMeas_PS1600_DigInOut_Std(String signal, String iForce, String vClampHigh, String vClampLow, Boolean setDisconnect, String iForceAction, String vMeasAction) {
        String disconnectAction="disconnect_"+signal;
        while (disconnectAction.contains(" ") || disconnectAction.contains("+")  ) {
            disconnectAction=disconnectAction.replaceAll(" ", "");
            disconnectAction=disconnectAction.replaceAll("\\+", "_");
        }
        ISetupDigInOut digInOut_PS1600 = ds.addDigInOut(signal).setConnect(true).setDisconnect(setDisconnect);
        ds.actionCall( ds.addDigInOut(signal).frontendConnection().setDigitalFrontendDisable() );
        digInOut_PS1600.protection().setDisconnectPulldownState(false);
        digInOut_PS1600.iforce(iForceAction).setForceValue(iForce).setVclampHigh(vClampHigh).setVclampLow(vClampLow).setKeepVclampEnabled();
        digInOut_PS1600.vmeas(vMeasAction).setWaitTime("2 ms").setAverages(4).setHighAccuracy(true);
        digInOut_PS1600.frontendConnection(disconnectAction).setDcFrontendDisable();
    }

    /**
     * Measure voltage with PS1600 DigInOut instrument in Hiz mode, AC relay is off.
     *
     * @param signal Pin name
     * @param setDisconnect The value to assign to the disconnect property after measurement execution
     * @param vMeasAction Identifier of the vmeas action
     */
    public void vMeas_PS1600_DigInOut_Hiz(String signal, Boolean setDisconnect, String vMeasAction) {
        String disconnectAction="disconnect_"+signal;
        while (disconnectAction.contains(" ") || disconnectAction.contains("+")  ) {
            disconnectAction=disconnectAction.replaceAll(" ", "");
            disconnectAction=disconnectAction.replaceAll("\\+", "_");
        }
        ISetupDigInOut digInOut_PS1600 = ds.addDigInOut(signal).setConnect(true).setDisconnect(setDisconnect);
        ds.actionCall( ds.addDigInOut(signal).frontendConnection().setDigitalFrontendDisable() );
        digInOut_PS1600.protection().setDisconnectPulldownState(false);
        digInOut_PS1600.vmeas(vMeasAction).setWaitTime("2 ms").setAverages(4).setHighAccuracy(true).setDirectEnabled();
        digInOut_PS1600.frontendConnection(disconnectAction).setDcFrontendDisable();
    }

    /**
     * Measure voltage with PS1600 DigInOut instrument, AC relay is off.
     *
     * @param signal Pin name
     * @param vForce  The value to assign to the forceValue property like "1.8 V"
     * @param iRange The value to assign to the irange property like "150 uA"
     * @param waitTime The value to assign to the waitTime property like "10 ms"
     * @param setDisconnect The value to assign to the disconnect property after measurement execution
     * @param iMeasAction Identifier of the imeas action
     */
    public void iMeas_PS1600_DigInOut_Std(String signal, String vForce, String iRange, String waitTime, Boolean setDisconnect, String iMeasAction) {
        String disconnectAction="disconnect_"+signal;
        while (disconnectAction.contains(" ") || disconnectAction.contains("+")  ) {
            disconnectAction=disconnectAction.replaceAll(" ", "");
            disconnectAction=disconnectAction.replaceAll("\\+", "_");
        }
        ISetupDigInOut digInOut_PS1600 = ds.addDigInOut(signal).setConnect(true).setDisconnect(setDisconnect);
        ds.actionCall( ds.addDigInOut(signal).frontendConnection().setDigitalFrontendDisable() );
        digInOut_PS1600.protection().setDisconnectPulldownState(false);
        digInOut_PS1600.vforceImeas(iMeasAction).setForceValue(vForce).setIrange(iRange).setWaitTime(waitTime).setHighAccuracy(true);
        digInOut_PS1600.frontendConnection(disconnectAction).setDcFrontendDisable();
    }

    /**
     * Measure voltage with PS1600 DigInOut instrument, AC relay is off.
     *
     * @param signal Pin name
     * @param vForce  The value to assign to the forceValue property like "1.8 V"
     * @param iRange The value to assign to the irange property like "150 uA"
     * @param setDisconnect The value to assign to the disconnect property after measurement execution
     * @param vForceAction Identifier of the vforce action
     * @param iMeasAction Identifier of the imeas action
     */
    public void iMeas_PS1600_DcVi_Std(String signal, String vForce, String iRange, Boolean setDisconnect, String vForceAction, String iMeasAction) {
        String disconnectAction="disconnect_"+signal;
        while (disconnectAction.contains(" ") || disconnectAction.contains("+")  ) {
            disconnectAction=disconnectAction.replaceAll(" ", "");
            disconnectAction=disconnectAction.replaceAll("\\+", "_");
        }
        ISetupDcVI dcVi_PS1600 = ds.addDcVI(signal).setConnect(true).setDisconnect(setDisconnect).setDisconnectModeHiz();
        dcVi_PS1600.vforce(vForceAction).setForceValue(vForce).setIrange(iRange).setIclamp(iRange);
        dcVi_PS1600.imeas(iMeasAction).setAverages(16).setIrange(iRange);
        dcVi_PS1600.disconnect(disconnectAction);

    }

    /**
     * Measure voltage with PS1600 DigInOut instrument, AC relay is off.
     *
     * @param signal Pin name
     * @param vForce  The value to assign to the forceValue property like "1.8 V"
     * @param iRange The value to assign to the irange property like "150 uA"
     * @param setDisconnect The value to assign to the disconnect property after measurement execution
     * @param vForceAction Identifier of the vforce action
     * @param iMeasAction Identifier of the imeas action
     */
    public void iMeas_DPS128_Std(String signal, String vForce, String iRange, Boolean setDisconnect, String vForceAction, String iMeasAction) {
        String disconnectAction="disconnect_"+signal;
        while (disconnectAction.contains(" ") || disconnectAction.contains("+")  ) {
            disconnectAction=disconnectAction.replaceAll(" ", "");
            disconnectAction=disconnectAction.replaceAll("\\+", "_");
        }
        ISetupDcVI dcVi_Dps128=ds.addDcVI(signal).setConnect(true).setDisconnect(true).setDisconnectModeHiz();
        dcVi_Dps128.level().setIrange(iRange).setVrange("6 V");
        dcVi_Dps128.vforce(vForceAction).setForceValue(vForce).setIrange(iRange).setIclamp(iRange);
        dcVi_Dps128.imeas(iMeasAction).setAverages(16).setIrange(iRange).setUngangMode(SetupUngangMode.never).setRestoreIrange(true);
        dcVi_Dps128.disconnect(disconnectAction);
    }



}

