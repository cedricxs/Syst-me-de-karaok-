package Resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import Music.Music;
import Music.note;

//note -> action,channel,instrument,vitesse,date,dure
public class ParseMidi {
	Sequencer sequencer;

	public void playMidi() {
		try {
			// TODO Auto-generated constructor stub
			
	        sequencer.open();
	        Sequence sequence = new Sequence(Sequence.PPQ,384);
	        Track track = sequence.createTrack();
        
            ShortMessage noteOnMsg0 = new ShortMessage();
            ShortMessage noteOnMsg1 = new ShortMessage();
            ShortMessage noteOnMsg2 = new ShortMessage();
            ShortMessage noteOnMsg3 = new ShortMessage();
            ShortMessage noteOnMsg4 = new ShortMessage();
            ShortMessage noteOnMsg5 = new ShortMessage();
            ShortMessage noteOnMsg6 = new ShortMessage();
            ShortMessage noteOnMsg7 = new ShortMessage();
            ShortMessage noteOnMsg8 = new ShortMessage();	            
            ShortMessage noteOnMsg9 = new ShortMessage();
            //Signal/Channel/Pitch/Velocity
         
			noteOnMsg0.setMessage(176,0,101,0);
			noteOnMsg1.setMessage(176,0,100,0);
			noteOnMsg2.setMessage(176,0,6,2);
			noteOnMsg3.setMessage(176,0,7,64);
			noteOnMsg4.setMessage(144,0,74,100);
			noteOnMsg5.setMessage(128,0,74,64);
			noteOnMsg6.setMessage(144,0,75,100);
			noteOnMsg7.setMessage(128,0,75,64);
			noteOnMsg8.setMessage(144,0,62,100);
			noteOnMsg9.setMessage(144,0,58,100);
					
			//ShortMessage noteOffMsg = new ShortMessage();
			//Signal/Channel/Pitch/Velocity
			//noteOffMsg.setMessage(ShortMessage.NOTE_OFF,msg.GetChannel(),msg.GetPitch(),msg.GetVelocity());
			
			track.add(new MidiEvent(noteOnMsg0,0));
			track.add(new MidiEvent(noteOnMsg1,2));
			track.add(new MidiEvent(noteOnMsg2,0));
			track.add(new MidiEvent(noteOnMsg3,0));
			track.add(new MidiEvent(noteOnMsg4,0));
			track.add(new MidiEvent(noteOnMsg5,0));
			track.add(new MidiEvent(noteOnMsg6,0));
			track.add(new MidiEvent(noteOnMsg7,8));
			track.add(new MidiEvent(noteOnMsg8,12));
			track.add(new MidiEvent(noteOnMsg8,12));
			//track.add(new MidiEvent(noteOffMsg,1));
			//i = i+1;
			
			sequencer.setSequence(sequence);
			
			sequencer.start();
			sequencer.setTempoInBPM(120);
			Thread.sleep(10000);
		} catch (InvalidMidiDataException | InterruptedException | MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Music ParseMusic(String fileName) {
		Sequence sequence;
		try {
			sequence = MidiSystem.getSequence(new File(fileName));
			System.out.println(sequence.getTickLength());
			Track[] tracks = sequence.getTracks();
			Music music = new Music(fileName);
			music.setVite(sequence.getResolution());
			music.setDivisionType(sequence.getDivisionType());
			for(int i=0;i<tracks.length;i++) {
				ArrayList<note> track = new ArrayList<note>(); 
				music.getNotes().add(track);
				for(int j=0;j<tracks[i].size();j++) {
					//System.out.println(tracks[i].get(j).getTick());
					//System.out.println(tracks[i].get(j).getTick());
					MidiEvent e = tracks[i].get(j);
					MidiMessage m = e.getMessage();
				
					if(m instanceof ShortMessage) {
						ShortMessage ms = (ShortMessage)m;
						int channel = ms.getChannel();
						int action = ms.getCommand();
						int hauteur = ms.getData1();
						int puissance = ms.getData2();
						System.out.println(action);
						System.out.println(channel);
						System.out.println(hauteur);
						System.out.println(puissance);
						long time = e.getTick();
						track.add(new note(action, channel, hauteur, puissance , time));
					}
					else if(m instanceof MetaMessage) {
						MetaMessage ms = (MetaMessage)m;
						int action = ms.getType();
						//int channel = ms.getChannel();
						byte[] data = ms.getData();
						long time = e.getTick();
						track.add(new note(data,action,time));
					}
				}
			}
			return music;
		} catch (InvalidMidiDataException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}
	public void changeHauteur() {
		try {
			sequencer.stop();
			Sequence q = sequencer.getSequence();
			Track[] tracks = q.getTracks();
			Sequence n = new Sequence(q.getDivisionType(),q.getResolution());
			for(Track t:tracks) {
				Track m = n.createTrack();
				for(int i=0;i<t.size();i++) {
					if(t.get(i).getMessage() instanceof ShortMessage) {
						ShortMessage ms = (ShortMessage)t.get(i).getMessage();					
						ms.setMessage(ms.getCommand(), ms.getChannel(), ms.getData1(), ms.getData2()-20>0?ms.getData2()-20:0);
						m.add(new MidiEvent(ms,t.get(i).getTick()));	
					}
					else {
						m.add(t.get(i));
					}
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
					else{ShortMessage shMsg = new ShortMessage();
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
	public void changeInstrument(int instrument) {
		try {
			sequencer.stop();
			Sequence q = sequencer.getSequence();
			Track[] tracks = q.getTracks();
			System.out.println(tracks.length);
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
	
	
	
	public ParseMidi()  {
		try {
			sequencer = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			Sequence sequence = MidiSystem.getSequence(new File("music/baga01.mid"));
//			System.out.println(sequence.getTickLength());
//			Track[] tracks = sequence.getTracks();
////			for(Track t:tracks) {
////				//System.out.println(t.ticks());
////				System.out.println(t.size());
////			}
//			Music music = new Music("baga01.mid");
//			music.setVite(sequence.getResolution());
//			for(int i=0;i<tracks.length;i++) {
//				ArrayList<note> track = new ArrayList<note>(); 
//				music.getNotes().add(track);
//				for(int j=0;j<tracks[i].size();j++) {
//					//System.out.println(tracks[i].get(j).getTick());
//					//System.out.println(tracks[i].get(j).getTick());
//					MidiEvent e = tracks[i].get(j);
//					MidiMessage m = e.getMessage();
//					if(m instanceof ShortMessage) {
//						ShortMessage ms = (ShortMessage)m;
//						int channel = ms.getChannel();
//						int action = ms.getCommand();
//						int instrument = ms.getData1();
//						int hauteur = ms.getData2();
//						long time = e.getTick();
//						track.add(new note(action, channel,  instrument,  hauteur, time));
//					}
//				}
//			}
//			Sequence sequence1 = new Sequence(sequence.getDivisionType(),sequence.getResolution());
//			Track[] ts = new Track[tracks.length];
//			for(int i=0;i<tracks.length;i++) {
//				ts[i] = sequence1.createTrack();
//				for(int j=0;j<tracks[1].size();j++) {
//					ts[i].add(tracks[1].get(j));
//				}
//			}
//			Track[] tracks1 = sequence1.getTracks();
//			System.out.println("hahaha:"+tracks1.length);
//			for(Track t:tracks1) {
////				//System.out.println(t.ticks());
//				System.out.println(t.size());
//			}
//			Sequencer sequencer = MidiSystem.getSequencer();
//			sequencer.open();
//			sequencer.setSequence(sequence1);
//			sequencer.start();
//		} catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		

    }
	public static void main(String[] args) {
		ParseMidi m = new ParseMidi();
		
		Music music = m.ParseMusic("music/monk.mid");
		music.setViteRate(1);
		m.playMusic(music);
		Scanner s = new Scanner(System.in);
		while(true) {
			String str = s.nextLine();
			
			m.changeInstrument(Integer.valueOf(str));
		}
	}
	

}
