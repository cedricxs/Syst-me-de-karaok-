package Music;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import Client.LrcReader;
import Client.MyPlayer;
import javazoom.jl.decoder.JavaLayerException;

public class Music {

	int foisEtreChante;
	String name;
	ArrayList<note> notes;
	ArrayList<parole> paroles;
	
	public Music(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	
	
	
	
	
	public void play() {
		 File file = new File("music/baga01.mid");
		 try {
			 Sequence currentSound = MidiSystem.getSequence(file);
			 Sequencer player = MidiSystem.getSequencer();
			 player.open();
			 player.setSequence(currentSound);
			 player.start(); 
		} catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	public static void main(String[] args) {
		Music m = new Music("");
		m.play();
	}

}
