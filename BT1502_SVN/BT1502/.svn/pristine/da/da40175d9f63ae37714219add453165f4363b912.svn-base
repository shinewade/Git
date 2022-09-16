package besLib.dsa;

import java.util.List;

import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupPattern;
import xoc.dta.ITestContext;
import xoc.dta.measurement.ILocalMeasurement;

/**
 * This class is used to generate OS functional test SSF file in Project
 * @version V1.0
 * @author Ronnie Li
 **/
public class BesDsa_OS_Func {

    ISetupPattern pattern;
    private IDeviceSetup deviceSetup;
    private ILocalMeasurement measurement;
    private String osPinNames;
    private List<String> allDigitalPins;
    private int index[] = new int[100];
    private int wait[] = new int[100];
    private int   count = 0;

    /**
     * Constructor <br>
     * Generate OS functional test SSF.<br>
     * Call the functions in the following order:<br>
     * [1] setPinGroup<br>
     * [2] removePin<br>
     * [3] addWaitCycle<br>
     * [4] genWalkingZPattern<br>
     * [5] genOperatingSequence<br>
     *
     * @param deviceSetup An instance of IDeviceSetup interface.
     * @param measurement An instance of IMeasurement interface.
     */
    public BesDsa_OS_Func(IDeviceSetup deviceSetup, ILocalMeasurement measurement)
    {
        this.deviceSetup=deviceSetup;
        this.measurement=measurement;
    }

    /**
     * Set signal group name which is defined in the specification and remove disabled pins in current dbd file.
     *
     * @param context This instance enables you to access the test context of a measurement.
     * @param spec The fully qualified name of the specification file.
     * @param pinGroupName The name of the signal group, as defined in the specification.
     *
     * @return object of BesDsa_OS_Func.
     */
    public BesDsa_OS_Func setPinGroup(ITestContext context,String spec,String pinGroupName )
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
     * @return object of BesDsa_OS_Func.
     */
    public BesDsa_OS_Func removePin(String pinName)
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
     * @param pinName The single pin name which need wait cycles before M.
     * @param waitCycle The number of vector wait cycles.
     *
     * @return object of BesDsa_OS_Func.
     */
    public BesDsa_OS_Func addWaitCycle(String pinName, int waitCycle)
    {
        for(int i=0; i<allDigitalPins.size(); i++)
        {
            if(allDigitalPins.get(i)==pinName)
            {
                index[count]=i;
                wait[count]=waitCycle;
            }
        }
        count++;
        return this;
    }

    /**
     * Generate walking z pattern in DSA path.<br>
     *
     * @return object of BesDsa_OS_Func.
     */
    public BesDsa_OS_Func genWalkingZPattern()
    {
        int num = allDigitalPins.size();

        String vector = "";
        pattern = this.deviceSetup.createPattern("walking_z", 1, this.osPinNames);
        for(int i=0; i<num; i++)
        {
            for(int m=0; m<count; m++)
            {
                if(index[m]==i)
                {
                    for(int n=0; n<num; n++)
                    {
                        if(n==i)
                        {
                            vector = vector+"X";
                        }
                        else
                        {
                            vector = vector+"0";
                        }
                    }
                    pattern.genVecBegin(wait[m]).addVector(vector).genVecEnd();
                    vector = "";
                }
            }

            for(int j=0; j<num; j++)
            {
                if(j==i)
                {
                    vector = vector+"M";
                }
                else
                {
                    vector = vector+"0";
                }
            }
            pattern.addVector(vector);
            vector = "";
        }

        return this;
    }

    /**
     * Generate OS functional test operating sequence. <br>
     */
    public void genOperatingSequence()
    {

        this.deviceSetup.sequentialBegin();
            this.deviceSetup.patternCall(pattern);
        this.deviceSetup.sequentialEnd();
        this.measurement.setSetups(this.deviceSetup);

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
