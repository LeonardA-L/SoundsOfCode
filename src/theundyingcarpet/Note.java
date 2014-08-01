package theundyingcarpet;

public class Note {
    private int frequency;
    private double amplitude;
    private double duration;
    
    public Note(int frequency) {
        this.frequency = frequency;
        this.amplitude = 0.4;  // Will be computed from enveloppe
        this.duration = 4;  // Will be computed from enveloppe
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }
}
