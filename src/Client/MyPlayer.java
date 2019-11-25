package Client;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import Music.Music;
import Music.note;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MyPlayer{

	Player playerMp3;
	Sequencer sequencer;

	public MyPlayer() {
		try {
			sequencer = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		p.playMp3("芒种");
		//p.playMidi(new Music(""));
	}
	//播放方法
    public void playMp3(String music) {
    		String fileName = "client/"+music+".mp3";
            BufferedInputStream buffer;
            LrcReader l = new LrcReader("client/"+music+".lrc");
    		l.run();
			try {
				buffer = new BufferedInputStream(new FileInputStream(new File(fileName)));
				playerMp3 = new Player(buffer);
				new Thread(new Mp3()).start();
			} catch (FileNotFoundException | JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    public void playMidi(Music music) {
    	try {
			Sequence sequence = new Sequence(Sequence.PPQ,music.getVite());
			ArrayList<ArrayList<note>> notes = music.getNotes();
			for(int i=0;i<notes.size();i++) {
				ArrayList<note> track = notes.get(i);
				Track s = sequence.createTrack();
				for(int j=0;j<track.size();j++) {
					note n = track.get(j);
					ShortMessage noteOnMsg = new ShortMessage();
					noteOnMsg.setMessage(n.getAction(),n.getChannel() , n.getInstrument(), n.getHauteur());
					s.add(new MidiEvent(noteOnMsg,n.getTime()));
				}
			}
			
			Sequencer sequencer = MidiSystem.getSequencer();
	        sequencer.open();
	        sequencer.setSequence(sequence);
			sequencer.start();
			
		} catch (InvalidMidiDataException | MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    	
    
    class Mp3 implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				playerMp3.play();
			} catch (JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    }
	
	
}
