package theundyingcarpet;

import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.unitgen.ImpulseOscillatorBL;
import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.UnitOscillator;

import theundyingcarpet.TheUndyingCarpet.NoteType;

public class Generator {
    public static UnitOscillator generateInstrument(String instrumentName) {
        UnitOscillator ug = null;
        // Java 8 would have allowed a switch/case with strings
        if (instrumentName.equals("JavaScript")) {
            ug = new SawtoothOscillatorBL();
        } else if (instrumentName.equals("Ruby")) {
            ug = new TriangleOscillator();
        } else if (instrumentName.equals("Java")) {
            ug = new ImpulseOscillatorBL();
        } else if (instrumentName.equals("PHP")) {
            ug = new SineOscillator();
        } else {
            ug = new SquareOscillator();
        }

        return ug;
    }

    private static double getInstrumentAmplitudeFactor(String instrumentName) {
        double a;
            if (instrumentName.equals("JavaScript")) {
                a = 0.6;
            } else if (instrumentName.equals("Ruby")) {
                a = 0.8;
            } else if (instrumentName.equals("Java")) {
                a = 2;
            } else if (instrumentName.equals("PHP")) {
                a = 0.8;
            } else {
                a = 0.5;
            }
        return a;
    }

    private static double getTypeAmplitudeFactor(NoteType noteType) {
        double a;
        switch (noteType) {
        case REPO:
        default:
            a = 0.5;
        }
        return a;
    }

    public static SegmentedEnvelope generateEnveloppe(String instrumentName, NoteType noteType) {
        double instrumentMax = getInstrumentAmplitudeFactor(instrumentName);
        double mixFactor = getTypeAmplitudeFactor(noteType);
        double maxAmpl = instrumentMax * mixFactor;
        switch (noteType) {
        case REPO:
        default:
            double[] enveloppeRepoData = { 0.00, maxAmpl, 0.01, 0.5 * maxAmpl, 0.02, maxAmpl, 0.24, 0.0, 1.0, 0.0 };
            SegmentedEnvelope enveloppe = new SegmentedEnvelope(enveloppeRepoData);
            return enveloppe;
        }
    }
}
