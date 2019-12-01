package Client;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

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
import Music.parole;
import Serveur.PlayMusicServlet;

public class MyPlayer{

	Sequencer sequencer;
	Timer timer;
	float time;
	int current;
	Frame paroleFrame;
	float viteRate;
	Map<Integer,String> Type_parole;
	public MyPlayer() {
		try {
			sequencer = MidiSystem.getSequencer();
			Type_parole = new HashMap<Integer, String>();
			Type_parole.put(0,"trémolo");
			Type_parole.put(1,"crié");
			Type_parole.put(2,"portamento");
		} catch (MidiUnavailableException e) {
		}
	}
	
	public void changeVite(float viteRate) {
		this.viteRate = viteRate;
		sequencer.stop();
		sequencer.setTempoFactor(viteRate);
//		long tick= sequencer.getTickPosition();
//		sequencer.setTickPosition(tick);
		sequencer.start();
	}
	
	
	public static void main(String[] args) {
		MyPlayer p = new MyPlayer();
		PlayMusicServlet l = new PlayMusicServlet("");
		Music music = l.parseMusic("baga01");
		p.playMusic(music);
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		while(true) {
			String c = s.nextLine();
			if(c.equals("cv")) {
			p.changeVite((float) (2*p.viteRate));
			}else if(c.equals("ch")){
				p.changeHauteur();
			}
		}
	}
	
	public void playMusic(Music music) {
		if(paroleFrame!=null) {
			paroleFrame.dispose();
		}
		paroleFrame = new Frame();
		timer = new Timer();
		viteRate = 1f;
		current = 0;
		playParoles(music);
		playNotes(music);
	}
	
	public void playParoles(Music music) {
		ArrayList<parole> paroles = music.getParoles();
		paroleFrame.lanchFrame();
		paroleFrame.insertDocument(Type_parole.get(paroles.get(current).getType())+":", Color.green,0);
		paroleFrame.insertDocument(paroles.get(current).getText()+"\n", Color.GRAY,paroleFrame.length());
		timer.scheduleAtFixedRate(new TimerTask() {   
		    public void run() { 
		    	time+=viteRate;
		    	parole p = paroles.get(current);
		    	if(time>p.getTime()+p.getDuree()) {
		    		ChangeProcessus(p);
		    		if(current>=paroles.size()-1){
		    			timer.cancel();
		    		}else {
		    			current++;			    		
		    			p = paroles.get(current);
		    			paroleFrame.insertDocument(Type_parole.get(paroles.get(current).getType())+":", Color.green,paroleFrame.length());
		    			paroleFrame.insertDocument(p.getText()+"\n", Color.GRAY,paroleFrame.length());			    		
		    		}
		    	}
		    	ChangeProcessus(p);
		    }
		}, new Date(), 1);
	}
	
	void ChangeProcessus(parole p) {
		long start = p.getTime();
		String text = p.getText();
		long duree = p.getDuree();
		int position;
		if(time<start) {
			return;
		}
		if(time>start+duree) {
			position = text.length();
		}
		else {
			position = (int) ((time-start)*text.length()/duree);
		}
			int rest = text.length()+1-position;
			int last = (int) ((time-viteRate-start)*text.length()/duree);
			if(position>last) {
				String r = paroleFrame.getDocument(paroleFrame.length()-rest-1,1);
				paroleFrame.removeDocument(paroleFrame.length()-rest-1,1);
				paroleFrame.insertDocument(r, Color.green, paroleFrame.length()-rest);
			}			
	}
    
    public void playNotes(Music music) {
    	try {
			Sequence sequence = new Sequence(Sequence.PPQ,music.getVitesse());
			ArrayList<ArrayList<note>> notes = music.getNotes();
			for(int i=0;i<notes.size();i++) {
				ArrayList<note> track = notes.get(i);
				Track s = sequence.createTrack();
				for(int j=0;j<track.size();j++) {
					note n = track.get(j);
					if(n.isMetaNote()) {
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
    
    
    
	
	
}
