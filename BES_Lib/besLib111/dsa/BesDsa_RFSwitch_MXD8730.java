package besLib.dsa;

import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDcVI;

/**
 * This class is used to generate BES RF Switch MXD8730 SSF file in Project with V93K SMT8
 * @version V1.0
 * @author Weng Yongxin
 **/
public class BesDsa_RFSwitch_MXD8730 {
    private IDeviceSetup ds;
    private ISetupDcVI dcVi_V1;
    private ISetupDcVI dcVi_V2;
    private ISetupDcVI dcVi_V3;
    /**
     * Enum type for RF switch MXD8730.
     */
    public static enum MXD8730{
        RF1,
        RF2,
        RF3,
    }

    /**
     * Constructor
     * @param ds an instance of IDeviceSetup interface
     * @param pinName_V1 DC control pin V1
     * @param pinName_V2 DC control pin V2
     * @param pinName_V3 DC control pin V3
     * @param KeepAlive Control DC control pin state after current measurement
     */
    public BesDsa_RFSwitch_MXD8730(IDeviceSetup ds, String pinName_V1,String pinName_V2,String pinName_V3, boolean KeepAlive)  {
        this.ds=ds;

        boolean disconnect=true;
        if(KeepAlive==true) {
            disconnect=false;
        }

        //PS1600 DcVI Instrument
        dcVi_V1=ds.addDcVI(pinName_V1).setConnect(true).setConnectModeStandard().setDisconnect(disconnect).setDisconnectModeSafe();
        dcVi_V2=ds.addDcVI(pinName_V2).setConnect(true).setConnectModeStandard().setDisconnect(disconnect).setDisconnectModeSafe();
        dcVi_V3=ds.addDcVI(pinName_V3).setConnect(true).setConnectModeStandard().setDisconnect(disconnect).setDisconnectModeSafe();
    }


    /**
     * Select RF Switch port
     * @version 1.0
     * @param rfPort RF port for Switching
     *        <br>The options are: <i>RF1,RF2,RF3</i>  <br>
     */
    public void setRFPortOn(MXD8730 rfPort) {
        String RFPortOn="DCControlHigh";
        String RFPortOff1="DCControlLow1";
        String RFPortOff2="DCControlLow2";

        switch (rfPort) {
        case RF1:
            dcVi_V1.vforce(RFPortOn).setForceValue("3.3 V").setIrange("1 mA").setIclamp("1 mA");
            dcVi_V2.vforce(RFPortOff1).setForceValue("0 V").setIrange("1 mA").setIclamp("1 mA");
            dcVi_V3.vforce(RFPortOff2).setForceValue("0 V").setIrange("1 mA").setIclamp("1 mA");
            break;
        case RF2:
            dcVi_V1.vforce(RFPortOff1).setForceValue("0 V").setIrange("1 mA").setIclamp("1 mA");
            dcVi_V2.vforce(RFPortOn).setForceValue("3.3 V").setIrange("1 mA").setIclamp("1 mA");
            dcVi_V3.vforce(RFPortOff2).setForceValue("0 V").setIrange("1 mA").setIclamp("1 mA");
            break;
        case RF3:
            dcVi_V1.vforce(RFPortOff1).setForceValue("0 V").setIrange("1 mA").setIclamp("1 mA");
            dcVi_V2.vforce(RFPortOff2).setForceValue("0 V").setIrange("1 mA").setIclamp("1 mA");
            dcVi_V3.vforce(RFPortOn).setForceValue("3.3 V").setIrange("1 mA").setIclamp("1 mA");
            break;
        default:
            break;
        }

        this.ds.actionCall(RFPortOff1);
        this.ds.waitCall("0.3 ms");
        this.ds.actionCall(RFPortOff2);
        this.ds.waitCall("0.3 ms");
        this.ds.actionCall(RFPortOn);
        this.ds.waitCall("0.4 ms");

    }

    public void setAllRFPortOff() {
        String RFPortOff1="DCControlLow1";
        String RFPortOff2="DCControlLow2";
        String RFPortOff3="DCControlLow3";

        dcVi_V1.vforce(RFPortOff1).setForceValue("0 V").setIrange("1 mA").setIclamp("1 mA");
        dcVi_V2.vforce(RFPortOff2).setForceValue("0 V").setIrange("1 mA").setIclamp("1 mA");
        dcVi_V3.vforce(RFPortOff3).setForceValue("0 V").setIrange("1 mA").setIclamp("1 mA");

        this.ds.actionCall(RFPortOff1);
        this.ds.waitCall("0.3 ms");
        this.ds.actionCall(RFPortOff2);
        this.ds.waitCall("0.3 ms");
        this.ds.actionCall(RFPortOff3);
        this.ds.waitCall("0.3 ms");

    }








}
