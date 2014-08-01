package theundyingcarpet;

import com.jsyn.data.SegmentedEnvelope;

public class Note {
    private int frequency;
    private SegmentedEnvelope enveloppe;
    private Note next;
    
    public Note(int frequency, SegmentedEnvelope enveloppe) {
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
