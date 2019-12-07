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

	ArrayList<Timer> timers;
	Frame paroleFrame;
	double vitesseRate;
	int pussanceOffset;
	Map<Integer,String> Type_parole;
	
	public MyPlayer() {
			Type_parole = new HashMap<Integer, String>();
			Type_parole.put(0,"(trémolo)");
			Type_parole.put(1,"(crié)");
			Type_parole.put(2,"(portamento)");
			timers = new ArrayList<>();
			paroleFrame = new Frame();
	}

	public void playMusic(Music music){
		paroleFrame.clear();
		for(Timer timer:timers) {
			timer.cancel();
		}
		timers.clear();
		vitesseRate = 1.0;
		pussanceOffset = 0;
		playParoles(music);
		playNotes(music);
	}
	
	public void playParoles(Music music) {
		ArrayList<parole> paroles = music.getParoles();
		Timer timer = new Timer();
		timers.add(timer);
		paroleFrame.lanchFrame();
		timer.scheduleAtFixedRate(new TimerTask() {
			double time = 0.0;
			int pos = 0;
			parole p = paroles.get(pos);
		    public void run() {
		    	time+=vitesseRate;   	
		    	if(pos<paroles.size()&&time>=paroles.get(pos).getTime()) {
		    		ChangeProcessus(p,time);
		    		p = paroles.get(pos);
		    		paroleFrame.insertDocument(Type_parole.get(p.getType())+":", Color.green,paroleFrame.length());
	    			paroleFrame.insertDocument(p.getText()+"\n", Color.GRAY,paroleFrame.length());
	    			pos++;
		    	}else {
		    		ChangeProcessus(p,time);
		    		if(pos==paroles.size()&&time>=p.getTime()+p.getDuree())this.cancel();
		    	}
		    }
		}, new Date(), 1);
	}

	void ChangeProcessus(parole p, double time) {
		long start = p.getTime();
		String text = p.getText();
		long duree = p.getDuree();
		int position;
		if(time<start) return;
		if(time>start+duree) position = text.length();
		else position = (int) ((time-start)*text.length()/duree);
		int rest = text.length()+1-position;
		int last = (int) ((time-vitesseRate-start)*text.length()/duree);
		if(position>last) {
			String r = paroleFrame.getDocument(paroleFrame.length()-rest-1,1);
			paroleFrame.removeDocument(paroleFrame.length()-rest-1,1);
			paroleFrame.insertDocument(r, Color.green, paroleFrame.length()-rest);
		}
	}

	public void playNotes(Music music){
		Synthesizer syn = null;
		try {
			syn = MidiSystem.getSynthesizer();
			syn.open();
		} catch (MidiUnavailableException e) {
			System.out.println("echou ouvrir Synthesizer");
		}
		final MidiChannel[] canaux = syn.getChannels();
		for(int i=0;i<music.getNotes().size();i++) {
			Timer timer = new Timer();
			timers.add(timer);
			ArrayList<note> notes = music.getNotes().get(i);
			MidiChannel channel = canaux[i] ;
			timer.scheduleAtFixedRate(new TimerTask() {
											  double time = 0.0;
											  int pos = 0;
											  @Override
											  public void run() {
												  time += vitesseRate;
												  if(time>=notes.get(pos).getTime()) {
													  if(!notes.get(pos).isMetaNote()) {
														  if(128<=notes.get(pos).getChannel()&&notes.get(pos).getChannel()<144) {
															  channel.noteOff(notes.get(pos).getHauteur(), notes.get(pos).getPuissance()-pussanceOffset>0?notes.get(pos).getPuissance()-pussanceOffset:0);
														  }
														  else if(144<=notes.get(pos).getChannel()&&notes.get(pos).getChannel()<160) {
															  channel.noteOn(notes.get(pos).getHauteur(), notes.get(pos).getPuissance()-pussanceOffset>0?notes.get(pos).getPuissance()-pussanceOffset:0);
														  }
														  else if(160<=notes.get(pos).getChannel()&&notes.get(pos).getChannel()<176) {
															  channel.setPolyPressure(notes.get(pos).getHauteur(), notes.get(pos).getPuissance());
														  }
														  else if(176<=notes.get(pos).getChannel()&&notes.get(pos).getChannel()<192) {
															  channel.controlChange(notes.get(pos).getHauteur(), notes.get(pos).getPuissance());
														  }
														  else if(192<=notes.get(pos).getChannel()&&notes.get(pos).getChannel()<208) {
															  channel.programChange(notes.get(pos).getHauteur(), notes.get(pos).getPuissance());
														  }
													  }
													  pos++;
													  if(pos==notes.size()) {
														  this.cancel();
													  }
												  }
											  }
										  }
					, new Date(), 1);
		}
	}
    public void changeVite(double vitesseRate) {
    	this.vitesseRate = vitesseRate;
	}
    public void changeHauteur(int ecart) {
    	this.pussanceOffset = ecart;
	}
}
