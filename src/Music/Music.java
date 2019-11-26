package Music;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class Music implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	int vite;
	float divisionType;
	private ArrayList<ArrayList<note>> notes;
	//private ArrayList<parole> paroles;
	
	public Music(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
		notes = new ArrayList<ArrayList<note>>();
		
	}
	public void setViteRate(int rate) {
		this.vite = (int)this.vite*rate;
	}
	public void setVite(int vite) {
		this.vite = vite;
	}
	
	public void setDivisionType(float divisionType) {
		this.divisionType = divisionType;
	}
	public float getDivisionType() {
		return this.divisionType ;
	}
	public int getVite() {
		return this.vite;
	}
	public ArrayList<ArrayList<note>> getNotes(){
		return this.notes;
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
