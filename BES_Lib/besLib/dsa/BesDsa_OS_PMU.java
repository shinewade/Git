package besLib.dsa;

import java.util.List;

import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDcVI;
import xoc.dsa.ISetupDcVI.SetupDisconnectMode;
import xoc.dsa.ISetupPattern;
import xoc.dta.ITestContext;
import xoc.dta.measurement.IMeasurement;

/**
 * This class is used to generate OS PMU test SSF file in Project
 * @version V1.0
 * @author Ronnie Li
 **/
public class BesDsa_OS_PMU {

    ISetupPattern pattern;
    private IDeviceSetup deviceSetup;
    private IMeasurement measurement;
    private String osPinNames;
    private List<String> allDigitalPins;
    private double iForceValue  = -200*1e-6;
    private double iRange       = 1*1e-3;
    private double vClampHigh   = 2;
    private double vClampLow    = -1.5;
    private double settlingTime = 5*1e-3;
    private double waitTime     = 5*1e-3;

    private int    indexSpecialPin[]        = new int[100];
    private double iForceValueSpecialPin[]  = new double[100];
    private double iRangeSpecialPin[]       = new double[100];
    private double vClampHighSpecialPin[]   = new double[100];
    private double vClampLowSpecialPin[]    = new double[100];
    private double settlingTimeSpecialPin[] = new double[100];
    private double waitTimeSpecialPin[]     = new double[100];
    private int    count = 0;

    /**
     * Constructor <br>
     * Generate OS PMU test SSF.<br>
     * Call the functions in the following order:<br>
     * [1] setPinGroup<br>
     * [2] removePin<br>
     * [3] set iForce and vMeas parameter<br>
     * [4] specialPin<br>
     * [5] genSSF_OS_PMU<br>
     *
     * @param deviceSetup An instance of IDeviceSetup interface.
     * @param measurement An instance of IMeasurement interface.
     */
    public BesDsa_OS_PMU(IDeviceSetup deviceSetup, IMeasurement measurement)
    {
        this.deviceSetup=deviceSetup;
        this.measurement=measurement;
    }

    /**
     * Set signal group which is defined in the specification and remove disabled pins.
     *
     * @param context This instance enables you to access the test context of a measurement.
     * @param spec The fully qualified name of the specification file.
     * @param pinGroupName The name of the signal group, as defined in the specification.
     *
     * @return object of BesDsa_OS_PMU.
     */
    public BesDsa_OS_PMU setPinGroup(ITestContext context,String spec,String pinGroupName )
    {
        allDigitalPins=context.inspection().initialSpec(spec).getSignalGroup(pinGroupName);
        allDigitalPins.removeAll( context.dutBoard().getDisabledDutSignals() );

        osPinNames = allDigitalPins.toString().replace(',', '+').replace('[', ' ').replace(']', ' ').replace(" ", "");

        return this;
    }

    /**
     * Remove single pin from pin group.
     *
     * @param pinName The single pin name which need to remove.
     *
     * @return object of BesDsa_OS_PMU.
     */
    public BesDsa_OS_PMU removePin(String pinName)
    {
        if(osPinNames.contains(pinName))
        {
            for(int i=0; i<allDigitalPins.size(); i++)
            {
                if(allDigitalPins.get(i)==pinName)
                {
                    allDigitalPins.remove(i);
                }
            }
            String pinNameTemp = "+"+pinName;
            if(osPinNames.contains(pinNameTemp))
            {
                osPinNames = osPinNames.replace(pinNameTemp, "");
            }
            else
            {
                pinNameTemp = pinName+"+";
                osPinNames = osPinNames.replace(pinNameTemp, "");
            }
        }

        return this;
    }

    /**
     * Pin name and wait cycles data collection.<br>
     *
     * @param pinName Special pin name.
     * @param iForceValueSpecial The iForce value in unit A of special pin's iForce action.
     * @param iRangeSpecial The iRange value in unit A of special pin's iForce action.
     * @param vClampHighSpecial The vClampHigh value in unit V of special pin's iForce action.
     * @param vClampLowSpecial The vClampLow value in unit V of special pin's iForce action.
     * @param settlingTimeSpecial The settling time in unit s of special pin's vMeas action.
     * @param waitTimeSpecial The wait time value in unit s between iForce action and vMeas action of special pin.
     *
     * @return object of BesDsa_OS_PMU
     */
    public BesDsa_OS_PMU specialPin(String pinName, double iForceValueSpecial, double iRangeSpecial, double vClampHighSpecial, double vClampLowSpecial, double settlingTimeSpecial, double waitTimeSpecial)
    {
        for(int i=0; i<allDigitalPins.size(); i++)
        {
            if(allDigitalPins.get(i)==pinName)
            {
                indexSpecialPin[count]        = i;
                iForceValueSpecialPin[count]  = iForceValueSpecial;
                iRangeSpecialPin[count]       = iRangeSpecial;
                vClampHighSpecialPin[count]   = vClampHighSpecial;
                vClampLowSpecialPin[count]    = vClampLowSpecial;
                settlingTimeSpecialPin[count] = settlingTimeSpecial;
                waitTimeSpecialPin[count]     = waitTimeSpecial;
		count++;
            }
        }
        return this;
    }

    /**
     * Generate SSF of OS PMU test.<br>
     */
    public void genSSF_OS_PMU()
    {

        ISetupDcVI dcVi_OS_GND=deviceSetup.addDcVI(osPinNames).setDisconnect(true).setDisconnectMode(SetupDisconnectMode.safe);
        dcVi_OS_GND.vforce("Vforce_0V").setForceValue("0 V").setIclamp("5 mA");
        dcVi_OS_GND.disconnect("Disconncet");

        double iForceValueTemp  = iForceValue ;
        double iRangeTemp       = iRange      ;
        double vClampHighTemp   = vClampHigh  ;
        double vClampLowTemp    = vClampLow   ;
        double settlingTimeTemp = settlingTime;
        double waitTimeTemp     = waitTime    ;
        for(int i=0; i<allDigitalPins.size(); i++)
        {
            iForceValueTemp  = iForceValue ;
            iRangeTemp       = iRange      ;
            vClampHighTemp   = vClampHigh  ;
            vClampLowTemp    = vClampLow   ;
            settlingTimeTemp = settlingTime;
            if(count != 0)
            {
                for(int j=0; j<count; j++)
                {
                    if(indexSpecialPin[j]==i)
                    {
                        iForceValueTemp  = iForceValueSpecialPin[j] ;
                        iRangeTemp       = iRangeSpecialPin[j]      ;
                        vClampHighTemp   = vClampHighSpecialPin[j]  ;
                        vClampLowTemp    = vClampLowSpecialPin[j]   ;
                        settlingTimeTemp = settlingTimeSpecialPin[j];
                    }
                }
            }
            ISetupDcVI dcVi_OS_pin=deviceSetup.addDcVI(allDigitalPins.get(i)).setDisconnect(true).setDisconnectMode(SetupDisconnectMode.safe);
            dcVi_OS_pin.iforce("OS_If_"+allDigitalPins.get(i)).setForceValue(iForceValueTemp).setIrange(iRangeTemp).setVclampHigh(vClampHighTemp).setVclampLow(vClampLowTemp).setKeepVclampEnabled();
            dcVi_OS_pin.vmeas ("OS_Vm_"+allDigitalPins.get(i)).setWaitTime(settlingTimeTemp).setAverages(16);
        }

        deviceSetup.sequentialBegin();
            deviceSetup.actionCall("Vforce_0V");
            for(int i=0; i<allDigitalPins.size(); i++)
            {
                waitTimeTemp = waitTime;
                for(int j=0; j<count; j++)
                {
                    if(indexSpecialPin[j]==i)
                    {
                        if(waitTimeSpecialPin[j] != 0)
                        {
                            waitTimeTemp = waitTimeSpecialPin[j];
                        }
                    }
                }
                deviceSetup.actionCall("OS_If_"+allDigitalPins.get(i));
                if(waitTimeTemp != 0)
                {
                    deviceSetup.waitCall(waitTimeTemp);
                }
                deviceSetup.actionCall("OS_Vm_"+allDigitalPins.get(i));
            }
            deviceSetup.actionCall("Disconncet");
        deviceSetup.sequentialEnd();
        measurement.setSetups(deviceSetup);

    }

    /**
     * Set iForceValue parameter value in unit A of iForce action.
     *
     * @param iForceValue The iForce value in unit A of iForce action.
     *
     * @return object of BesDsa_OS_PMU
     */
    public BesDsa_OS_PMU setiForceValue(double iForceValue)
    {
        this.iForceValue = iForceValue;
        return this;
    }

    /**
     * Set iRange parameter value in unit A of iForce action.
     *
     * @param iRange The iRange value in unit A of iForce action.
     *
     * @return object of BesDsa_OS_PMU
     */
    public BesDsa_OS_PMU setIrange(double iRange)
    {
        this.iRange = iRange;
        return this;
    }

    /**
     * Set vClampHigh parameter value in unit V of iForce action.
     *
     * @param vClampHigh The vClampHigh value in unit V of iForce action.
     *
     * @return object of BesDsa_OS_PMU
     */
    public BesDsa_OS_PMU setVclampHigh(double vClampHigh)
    {
        this.vClampHigh = vClampHigh;
        return this;
    }

    /**
     * Set vClampLow parameter value in unit V of iForce action.
     *
     * @param vClampLow The vClampLow value in unit V of iForce action.
     *
     * @return object of BesDsa_OS_PMU
     */
    public BesDsa_OS_PMU setVclampLow(double vClampLow)
    {
        this.vClampLow = vClampLow;
        return this;
    }

    /**
     * Set settling time parameter in unit s of vMeas action.
     *
     * @param settlingTime The settling time value in unit s of vMeas action.
     *
     * @return object of BesDsa_OS_PMU
     */
    public BesDsa_OS_PMU setSettlingTime(double settlingTime)
    {
        this.settlingTime = settlingTime;
        return this;
    }

    /**
     * Set wait time in unit s between iForce action and vMeas action.
     *
     * @param waitTime The wait time value in unit s between iForce action and vMeas action.
     *
     * @return object of BesDsa_OS_PMU
     */
    public BesDsa_OS_PMU setWaitTime(double waitTime)
    {
        this.waitTime = waitTime;
        return this;
    }

    /**
     * Get valid signals list which is defined in the Group specification after remove disabled pins.
     *
     * @return OS pin names.
     */
    public String getPinGroup()
    {
        return osPinNames;
    }

}
