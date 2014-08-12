/*
##############################################################
## 	                Sounds Of Code	    	    	    ##
##	        A GitHub Data Challenge III Entry	    ##
##			  LeonardA-L			    ##
##############################################################
*/
package soundsofcode;

import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.unitgen.UnitOscillator;

/**
 * A note that can be played by a NoteThread. Containing frequency, enveloppe, and instrument.
 */
public class Note {
    private int frequency;
    private SegmentedEnvelope enveloppe;
    // used for the chained list
    protected Note next;
    private UnitOscillator osc;
    
    /**
     * This constructor is called by Sample, a child object of Note, that only needs
     * the "Note" class type and the chained list system
     * @see Sample
     */
    public Note(){}
    
    /**
     * Instanciates a note with the given parameters
     * @param frequency
     * @param enveloppe
     * @param osc the instrument that plays it
     */
    public Note(int frequency, SegmentedEnvelope enveloppe, UnitOscillator osc) {
        this.frequency = frequency;
        this.enveloppe = enveloppe;
        this.osc = osc;
    }
    
    /**
     * Baseline constructor
     */
    public Note(int frequency, UnitOscillator osc){
        this(frequency,null,osc);
        this.enveloppe = Generator.generateEnveloppe("Ruby", SoundsOfCode.NoteType.BASELINE);
    }

    // --------- Accessors
    
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

    public void setOsc(UnitOscillator osc) {
        this.osc = osc;
    }

    public UnitOscillator getOsc() {
        return osc;
    }
}
