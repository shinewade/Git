package besLib.dsa;

import xoc.dsa.IDeviceSetup;
import xoc.dsa.ISetupDigInOut;
import xoc.dsa.ISetupPattern;
import xoc.dsa.ISetupWavetable.ReceiveAction;
import xoc.dta.measurement.IMeasurement;

/**
 * This class is used to generate digital capture SSF file in Project with V93K SMT8
 * @version V1.0
 * @author Ronnie Li
 **/
public class BesDsa_DigitalCapture {

    private IDeviceSetup deviceSetup;
    private IMeasurement measurement;
    private String capPinName;
    private double Voh = 0.9;
    private double Vol = 0.9;
    private double Ioh = 0.15*1e-3;
    private double Iol = 0.35*1e-3;
    private double Vt  = 1.8;

    /**
     * Enum type for digital capture edges in one tester cycle.
     */
    public enum CaptureEdge{
        x1,
        x2,
        x3,
        x4,
        x5,
        x6,
        x7,
        x8
    }

    /**
     * Constructor <br>
     * Generate digital capture SSF.<br>
     *
     * @param deviceSetup An instance of IDeviceSetup interface.
     * @param measurement An instance of IMeasurement interface.
     * @param capPinName Pin names of digital capture (You can define a single pin name or multiple pin names with "+").
     */
    public BesDsa_DigitalCapture(IDeviceSetup deviceSetup, IMeasurement measurement, String capPinName)
    {
        this.deviceSetup=deviceSetup;
        this.measurement=measurement;
        this.capPinName=capPinName;
    }



    /**
     * Generate mainSpec of digital capture pin.<br>
     * Flexible control the levelSpec values through use function "setCapPinLevelSpec".<br>
     *
     * @param fsMHz Sample rate of digital capture in unit MHz.
     * @param captureEdge Insert multiple receive edges in one capture period.
     * @return object of BesDsa_DigitalCapture
     *
     */
    public BesDsa_DigitalCapture genMainSpec(double fsMHz, CaptureEdge captureEdge)
    {
        int setCaptureEdge = captureEdge.toString().charAt(1)-0x30;
        double testerPeriod=1/(fsMHz/setCaptureEdge*1e6);
        double r1=0.0/setCaptureEdge * testerPeriod;
        double r2=1.0/setCaptureEdge * testerPeriod;
        double r3=2.0/setCaptureEdge * testerPeriod;
        double r4=3.0/setCaptureEdge * testerPeriod;
        double r5=4.0/setCaptureEdge * testerPeriod;
        double r6=5.0/setCaptureEdge * testerPeriod;
        double r7=6.0/setCaptureEdge * testerPeriod;
        double r8=7.0/setCaptureEdge * testerPeriod;

        ISetupDigInOut digInOutObject1 = this.deviceSetup.addDigInOut(this.capPinName).setConnect(true).setDisconnect(true);
        digInOutObject1.level("cap_pin_level_set").setVoh(this.Voh).setVol(this.Vol);//.setIoh(this.Ioh).setIol(this.Iol).setVt(this.Vt).setTermLoad();
        switch (setCaptureEdge) {
        case 1:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C);
            break;
        case 2:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C);
            break;
        case 3:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2).setR3(r3);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C,ReceiveAction.C);
            break;
        case 4:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2).setR3(r3).setR4(r4);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C);
            break;
        case 5:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2).setR3(r3).setR4(r4).setR5(r5);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C);
            break;
        case 6:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2).setR3(r3).setR4(r4).setR5(r5).setR6(r6);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C);
            break;
        case 7:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2).setR3(r3).setR4(r4).setR5(r5).setR6(r6).setR7(r7);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C);
            break;
        case 8:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2).setR3(r3).setR4(r4).setR5(r5).setR6(r6).setR7(r7).setR8(r8);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C);
            break;
        default:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C);
        }

        digInOutObject1.result().capture().setEnabled(true);
        digInOutObject1.result().callPassFail().setEnabled(false);

        return this;
    }

    /**
     * Generate mainSpec of digital capture pin.<br>
     * Flexible control the levelSpec values through use function "setCapPinLevelSpec".<br>
     *
     * @param fsMHz Sample rate of digital capture in unit MHz.
     * @param captureEdge Insert multiple receive edges in one capture period.
     * @param enableActiveLoad enable active load.
     * @return object of BesDsa_DigitalCapture
     *
     */
    public BesDsa_DigitalCapture genMainSpec(double fsMHz, CaptureEdge captureEdge, boolean enableActiveLoad)
    {
        int setCaptureEdge = captureEdge.toString().charAt(1)-0x30;
        double testerPeriod=1/(fsMHz/setCaptureEdge*1e6);
        double r1=0.0/setCaptureEdge * testerPeriod;
        double r2=1.0/setCaptureEdge * testerPeriod;
        double r3=2.0/setCaptureEdge * testerPeriod;
        double r4=3.0/setCaptureEdge * testerPeriod;
        double r5=4.0/setCaptureEdge * testerPeriod;
        double r6=5.0/setCaptureEdge * testerPeriod;
        double r7=6.0/setCaptureEdge * testerPeriod;
        double r8=7.0/setCaptureEdge * testerPeriod;

        ISetupDigInOut digInOutObject1 = this.deviceSetup.addDigInOut(this.capPinName).setConnect(true).setDisconnect(true);
        if(enableActiveLoad)
        {
            digInOutObject1.level("cap_pin_level_set").setVoh(this.Voh).setVol(this.Vol).setIoh(this.Ioh).setIol(this.Iol).setVt(this.Vt).setTermLoad();
        }
        else
        {
            digInOutObject1.level("cap_pin_level_set").setVoh(this.Voh).setVol(this.Vol);
        }
        switch (setCaptureEdge) {
        case 1:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C);
            break;
        case 2:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C);
            break;
        case 3:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2).setR3(r3);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C,ReceiveAction.C);
            break;
        case 4:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2).setR3(r3).setR4(r4);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C);
            break;
        case 5:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2).setR3(r3).setR4(r4).setR5(r5);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C);
            break;
        case 6:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2).setR3(r3).setR4(r4).setR5(r5).setR6(r6);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C);
            break;
        case 7:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2).setR3(r3).setR4(r4).setR5(r5).setR6(r6).setR7(r7);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C);
            break;
        case 8:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1).setR2(r2).setR3(r3).setR4(r4).setR5(r5).setR6(r6).setR7(r7).setR8(r8);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C,ReceiveAction.C);
            break;
        default:
            digInOutObject1.timing("timingSet1").setPeriod(testerPeriod).setR1(r1);
            digInOutObject1.addWavetable("wavetable1", 1).addStateCharDescription('C', ReceiveAction.C);
        }

        digInOutObject1.result().capture().setEnabled(true);
        digInOutObject1.result().callPassFail().setEnabled(false);

        return this;
    }

    /**
     * Generate digital capture SSF. <br>
     * A digital capture pattern will generate in DSA path.<br>
     *
     * @param vectorNumber Vector number of generated pattern.
     * @return object of BesDsa_DigitalCapture
     */
    public BesDsa_DigitalCapture genDigitalCaptureSSF(int vectorNumber)
    {
        int count = 1;
        String capPinNameTemp = this.capPinName;
        while (capPinNameTemp.contains("+")) {
            capPinNameTemp = capPinNameTemp.substring(capPinNameTemp.indexOf("+")+1);
            count++;
        }
        String vector = "";
        for(int i=0; i<count; i++)
        {
            vector = vector+"C";
        }

        ISetupPattern pattern = this.deviceSetup.createPattern("digitalCapture", 1, this.capPinName);
        pattern.genVecBegin(vectorNumber).addVector(vector).genVecEnd();

        this.deviceSetup.sequentialBegin();
            this.deviceSetup.patternCall(pattern);
        this.deviceSetup.sequentialEnd();
        this.measurement.setSetups(this.deviceSetup);

        return this;
    }



    /**
     * Set capture pin VOH which unit is V in the generated main specification.
     *
     * @param Voh value The value to assign to the voh property.
     * @return object of BesDsa_DigitalCapture
     */
    public BesDsa_DigitalCapture setCapPinLevelSpecVoh(double Voh)
    {
        this.Voh = Voh;
        return this;
    }

    /**
     * Set the levelSpec VOL value in unit V of digital capture pin.
     *
     * @param Vol value The value to assign to the Vol property.
     * @return object of BesDsa_DigitalCapture
     */
    public BesDsa_DigitalCapture setCapPinLevelSpecVol(double Vol)
    {
        this.Vol = Vol;
        return this;
    }

    /**
     * Set the levelSpec IOH value in unit A of digital capture pin.
     *
     * @param Ioh value The value to assign to the Ioh property.
     * @return object of BesDsa_DigitalCapture
     */
    public BesDsa_DigitalCapture setCapPinLevelSpecIoh(double Ioh)
    {
        this.Ioh = Ioh;
        return this;
    }

    /**
     * Set the levelSpec IOL value in unit A of digital capture pin.
     *
     * @param Iol value The value to assign to the Iol property.
     * @return object of BesDsa_DigitalCapture
     */
    public BesDsa_DigitalCapture setCapPinLevelSpecIol(double Iol)
    {
        this.Iol = Iol;
        return this;
    }

    /**
     * Set the levelSpec VT value in unit V of digital capture pin.
     *
     * @param Vt value The value to assign to the Vt property.
     * @return object of BesDsa_DigitalCapture
     */
    public BesDsa_DigitalCapture setCapPinLevelSpecVt(double Vt)
    {
        this.Vt = Vt;
        return this;
    }


}
