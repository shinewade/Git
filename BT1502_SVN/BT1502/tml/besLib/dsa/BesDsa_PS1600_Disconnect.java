package besLib.dsa;

import java.util.List;

import xoc.dsa.IDeviceSetup;
import xoc.dta.ITestContext;

/**
 * This class is used to disconnect PS1600 channel with V93K SMT8
 * @version V1.0
 * @author Weng Yongxin
 **/
public class BesDsa_PS1600_Disconnect {

    private  IDeviceSetup ds;


    /**
     * Constructor
     *
     * @param ds instance of IDeviceSetup interface
     */
    public BesDsa_PS1600_Disconnect(IDeviceSetup ds) {
        this.ds=ds;
    }


    /**
     * Get signal group name which is defined in the specification and remove disabled pins.
     *
     * @param context This instance enables you to access the test context of a measurement.
     * @param spec The fully qualified name of the specification file.
     * @param signalGroupName The name of the signal group, as defined in the specification.
     *
     * @return Pin group name in string type.
     */
    public String getPinGroup(ITestContext context,String spec,String signalGroupName ) {
        List<String> allDigitalPins=context.inspection().initialSpec(spec).getSignalGroup(signalGroupName);
        allDigitalPins.removeAll( context.dutBoard().getDisabledDutSignals() );
        String pins=allDigitalPins.toString().replace(',', '+').replace('[', ' ').replace(']', ' ');

        return pins;
    }

    /**
     * Disconnect PS1600 DcVI instrument all at the beginning of measurement.
     *
     * @param signals signals The signals for which this instrument setup is to be created.
     * @param connectPulldown <p>false: Disconnect pull down state (Hiz mode).
     *                        true: Keep pull down state (Loz mode). </p>
     */
    public void disconnectAll_DcVI(String signals, boolean connectPulldown) {
        ds.addDcVI(signals).setConnect(false).setDisconnect(false).protection().setDisconnectPulldownState(connectPulldown);
    }

    /**
     * Disconnect PS1600 DigInOut instrument all at the beginning of measurement.
     *
     * @param signals signals The signals for which this instrument setup is to be created.
     * @param connectPulldown false: Disconnect pull down state, Hiz mode.  true: Keep pull down state, Loz mode.
     */
    public void disconnectAll_DigInOut(String signals, boolean connectPulldown) {
        ds.addDigInOut(signals).setConnect(false).setDisconnect(false).protection().setDisconnectPulldownState(connectPulldown);
    }

    /**
     * Disconnect PS1600 DigInOut instrument AC relay.
     *
     * @param signals signals The signals for which this instrument setup is to be created.
     */
    public void disconnectAC_DigInOut(String signals) {
        ds.actionCall( ds.addDigInOut(signals).frontendConnection().setDigitalFrontendDisable() );
    }

    /**
     * Disconnect PS1600 DigInOut instrument DC relay.
     *
     * @param signals signals The signals for which this instrument setup is to be created.
     */
    public void disconnectDC_DigInOut(String signals) {
        ds.actionCall( ds.addDigInOut(signals).frontendConnection().setDcFrontendDisable() );
    }

}

