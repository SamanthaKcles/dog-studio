package ca.noxid.lab.rsrc;

import javax.sound.midi.*;
import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class BlSound {
	
	private static Clip clip;
	private static Sequencer sequencer;

	public static void playSample(URL url) {
		try {
			clip = AudioSystem.getClip();
	        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
	        clip.open(ais);
	        clip.start();
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void playSample(InputStream is, boolean loop) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
			clip = AudioSystem.getClip();
			clip.open(ais);
			if (loop) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				clip.start();
			}
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void stopSample() {
		if (clip != null && clip.isRunning()) {
			clip.stop();
			clip.close();
		}
	}
	
	public static void playMidi(URL url) {
		try {
			Sequence sequence = MidiSystem.getSequence(url);
			sequencer = MidiSystem.getSequencer();
	        sequencer.open();
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
	        sequencer.setSequence(sequence);
	        sequencer.start();
		} catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void playMidi(InputStream is) {
		try {
			Sequence sequence = MidiSystem.getSequence(is);
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sequencer.setSequence(sequence);
			sequencer.start();
		} catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
			e.printStackTrace();
		}
	}
}