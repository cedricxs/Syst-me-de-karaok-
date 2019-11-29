package Client;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import Music.Music;
import Music.note;
import Util.ParseMidi;

public class MyPlayer{

	Sequencer sequencer;

	public MyPlayer() {
		try {
			sequencer = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	public void changeVite(int viteRate) {
		try {
			sequencer.stop();
			Sequence q = sequencer.getSequence();
			Track[] tracks = q.getTracks();
			Sequence n = new Sequence(q.getDivisionType(),(int)q.getResolution()*viteRate);
			for(Track t:tracks) {
				Track m = n.createTrack();
				for(int i=0;i<t.size();i++) {
					m.add(t.get(i));
				}
			}
			long tick = sequencer.getTickPosition();
			sequencer.setSequence(n);
			sequencer.setTickPosition(tick);
			sequencer.start();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		MyPlayer p = new MyPlayer();
		//p.playMp3("芒种");
		Music music = ParseMidi.ParseMusic("music/feeling.mid");
		p.playMusic(music);
		Scanner s = new Scanner(System.in);
		while(true) {
			String c = s.nextLine();
			if(c.equals("cv")) {
			p.changeVite(2);
			}else if(c.equals("ch")){
				p.changeHauteur();
			}else {
				p.changeInstrument(Integer.valueOf(c));
			}
		}
		
	}
	
    
    public void playMusic(Music music) {
    	try {
			Sequence sequence = new Sequence(music.getDivisionType(),music.getVite());
			ArrayList<ArrayList<note>> notes = music.getNotes();
			for(int i=0;i<notes.size();i++) {
				ArrayList<note> track = notes.get(i);
				Track s = sequence.createTrack();
				for(int j=0;j<track.size();j++) {
					note n = track.get(j);
					if(n.getType()) {
						MetaMessage m = new MetaMessage();
						m.setMessage(n.getAction(), n.getData(), n.getData().length);
						s.add(new MidiEvent(m,n.getTime()));
					}
					else{
						ShortMessage shMsg = new ShortMessage();
						shMsg.setMessage(n.getAction(),n.getChannel() ,n.getHauteur(),n.getPuissance());
						s.add(new MidiEvent(shMsg,n.getTime()));
					}
				}
			}
	
	        sequencer.open(); 
	        sequencer.setSequence(sequence);
			sequencer.start();
			
		} catch (InvalidMidiDataException | MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    public void changeHauteur() {
		try {
			sequencer.stop();
			Sequence q = sequencer.getSequence();
			Track[] tracks = q.getTracks();
			for(Track t:tracks) {
				for(int i=0;i<t.size();i++) {
					if(t.get(i).getMessage() instanceof ShortMessage) {
						ShortMessage ms = (ShortMessage)t.get(i).getMessage();					
						ms.setMessage(ms.getCommand(), ms.getChannel(), ms.getData1(), ms.getData2()-10>0?ms.getData2()-10:0);
					}
				}
			}
			long tick = sequencer.getTickPosition();
			sequencer.setTickPosition(tick);
			sequencer.start();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void changeInstrument(int instrument) {
		try {
			sequencer.stop();
			Sequence q = sequencer.getSequence();
			Track[] tracks = q.getTracks();
			for(Track t:tracks) {
				for(int i=0;i<t.size();i++) {
					if(t.get(i).getMessage() instanceof ShortMessage) {
						ShortMessage ms = (ShortMessage)t.get(i).getMessage();	
						if(ms.getCommand() == ShortMessage.PROGRAM_CHANGE) {
							ms.setMessage(ms.getCommand(), ms.getChannel(), instrument, ms.getData2());
						}
					}
				}
			}
			
			long tick = sequencer.getTickPosition();
			sequencer.setTickPosition(tick);
			sequencer.start();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
    
    
	
	
}
