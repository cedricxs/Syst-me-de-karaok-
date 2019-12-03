package Client;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.*;

import Music.*;

public class MyPlayer{

	Sequencer sequencer;
	Timer timer;
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
		} catch (Exception e) {
		}
	}
	
	public void playMusic(Music music) {
		if(paroleFrame!=null) {
			paroleFrame.clear();
			timer.cancel();
		}
		else{	
			paroleFrame = new Frame();
		}
		timer = new Timer();
		viteRate = 1f;
		playParoles(music);
		playNotes(music);
	}
	public void playParoles(Music music) {
		ArrayList<parole> paroles = music.getParoles();
		paroleFrame.lanchFrame();
		paroleFrame.insertDocument(Type_parole.get(paroles.get(0).getType())+":", Color.green,0);
		paroleFrame.insertDocument(paroles.get(0).getText()+"\n", Color.GRAY,paroleFrame.length());
		timer.scheduleAtFixedRate(new TimerTask() {   
			float time = 0f;
			int current = 0;
		    public void run() { 
		    	time+=viteRate;
		    	//System.out.println(time);
		    	parole p = paroles.get(current);
		    	if(time>p.getTime()+p.getDuree()) {
		    		ChangeProcessus(p,time);
		    		if(current>=paroles.size()-1){
		    			timer.cancel();
		    		}else {
		    			current++;			    		
		    			p = paroles.get(current);
		    			paroleFrame.insertDocument(Type_parole.get(paroles.get(current).getType())+":", Color.green,paroleFrame.length());
		    			paroleFrame.insertDocument(p.getText()+"\n", Color.GRAY,paroleFrame.length());			    		
		    		}
		    	}
		    	ChangeProcessus(p,time);
		    }
		}, new Date(), 1);
	}
	
	void ChangeProcessus(parole p,float time) {
		long start = p.getTime();
		String text = p.getText();
		long duree = p.getDuree();
		int position;
		if(time<start) return;
		if(time>start+duree) position = text.length();
		else position = (int) ((time-start)*text.length()/duree);
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
			e.printStackTrace();
		}
	}

    public void changeVite(float viteRate) {
		this.viteRate = viteRate;
		sequencer.stop();
		sequencer.setTempoFactor(viteRate);
		sequencer.start();
	}
    
    public void changeHauteur(int ecart) {
		try {
			sequencer.stop();
			Sequence q = sequencer.getSequence();
			Track[] tracks = q.getTracks();
			for(Track t:tracks) {
				for(int i=0;i<t.size();i++) {
					if(t.get(i).getMessage() instanceof ShortMessage) {
						ShortMessage ms = (ShortMessage)t.get(i).getMessage();					
						ms.setMessage(ms.getCommand(), ms.getChannel(), ms.getData1(), ms.getData2()-ecart>0?ms.getData2()-ecart:0);
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
