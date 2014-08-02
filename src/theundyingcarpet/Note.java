package theundyingcarpet;

import com.jsyn.data.SegmentedEnvelope;

public class Note {
    private int frequency;
    private SegmentedEnvelope enveloppe;
    protected Note next;
    
    public Note(){}
    
    public Note(int frequency, SegmentedEnvelope enveloppe) {
        this.frequency = frequency;
        this.enveloppe = enveloppe;
    }
    
    // Tinnitus
    public Note(int frequency){
        double ramp = 0.05;
        double totalMRamp = TheUndyingCarpet.totalDurationS - ramp;
        double maxAmplitude = 0.0;
        double[] enveloppeData =
            {
                0.00, 0,
                ramp, maxAmplitude,
                totalMRamp,maxAmplitude,
                ramp, 0
            };
        SegmentedEnvelope enveloppe = new SegmentedEnvelope( enveloppeData );
        this.frequency = frequency;
        this.enveloppe = enveloppe;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public double getDuration() {
        double duration = 0;
        double[] envData = new double[enveloppe.getNumFrames()*2];
        enveloppe.read(envData);
        for(int i=0;i<envData.length;i+=2){
            duration+=envData[i];
        }
        return duration;
    }

    public void setEnveloppe(SegmentedEnvelope enveloppe) {
        this.enveloppe = enveloppe;
    }

    public SegmentedEnvelope getEnveloppe() {
        return enveloppe;
    }

    public void setNext(Note next) {
        this.next = next;
    }

    public Note getNext() {
        return next;
    }
}
