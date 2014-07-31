package theundyingcarpet;

 import com.jsyn.*;          // JSyn and Synthesizer classes
import com.jsyn.swing.*;    // Swing tools like knobs and JAppletFrame
import com.jsyn.unitgen.*;

import com.softsynth.jsyn.Synth; // Unit generators like SineOscillator

public class theUndyingCarpet{
	public static void main(String[] args){
		System.out.println("Hellow World");
                SawtoothOscillatorBL osc;
	        LineOut lineOut;
	        // Start JSyn synthesizer.
                Synthesizer synth = JSyn.createSynthesizer();
                synth.start();

	        // Create some unit generators.
	        osc = new SawtoothOscillatorBL();
	        lineOut = new LineOut();
                
                synth.add(osc);
                synth.add(lineOut);

	        // Connect oscillator to both left and right channels of output.
	        osc.output.connect(0, lineOut.input, 0);
	        osc.output.connect(0, lineOut.input, 1);

	        // Start the unit generators so they make sound.
	        osc.start();
	        lineOut.start();

	        // Set the frequency of the oscillator to 200 Hz.
	        osc.frequency.set(200.0);
	        osc.amplitude.set(0.8);

	        // Sleep for awhile so we can hear the sound.
	        try {
	            synth.sleepFor(4);
	        } catch (InterruptedException e) {
	        }
                // Change the frequency of the oscillator.
                osc.frequency.set(300.0);
                try {
                    synth.sleepFor(4);
                } catch (InterruptedException e) {
                }

	        // Stop units and delete them to reclaim their resources.
	        osc.stop();
	        lineOut.stop();

	        // Stop JSyn synthesizer.
	        synth.stop();
	}
}