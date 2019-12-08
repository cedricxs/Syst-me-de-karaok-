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
	Map<Integer,String> Voix_parole;
	Map<Integer,Color> Voix_color;
	Map<Integer,Boolean> Activer_voix;
	Synthesizer syn;
	
	public MyPlayer() {
			Type_parole = new HashMap<Integer, String>();
			Type_parole.put(0,"(trémolo)");
			Type_parole.put(1,"(crié)");
			Type_parole.put(2,"(portamento)");
			Voix_parole = new HashMap<Integer, String>();
			Voix_parole.put(0,"homme");
			Voix_parole.put(1,"femme");
			Voix_parole.put(2,"choeurs");
			Voix_color = new HashMap<Integer, Color>();
			Voix_color.put(0, Color.BLUE);
			Voix_color.put(1, Color.CYAN);
			Voix_color.put(2, Color.ORANGE);
			Activer_voix = new HashMap<Integer, Boolean>();
			Activer_voix.put(0,true);
			Activer_voix.put(1,true);
			Activer_voix.put(2,true);
			timers = new ArrayList<Timer>();
			paroleFrame = new Frame();
			try {
				syn = MidiSystem.getSynthesizer();
			} catch (MidiUnavailableException e) {
				System.out.println("echou creer Synthesizer");
			}
	}

	public void playMusic(Music music){
		if(syn.isOpen())syn.close();
		paroleFrame.clear();
		for(Timer timer:timers) {
			timer.cancel();
		}
		timers.clear();
		vitesseRate = 1.0;
		pussanceOffset = 0;
		for(int i=0;i<3;i++) {
			Activer_voix.replace(i,true);			
		}
		playParoles(music);
		playNotes(music);
	}
	
	public void playParoles(Music music) {
		ArrayList<parole> paroles = music.getParoles();
		Timer timer = new Timer();
		timers.add(timer);
		paroleFrame.lanchFrame();
		boolean[] affiche = new boolean[paroles.size()];
		timer.scheduleAtFixedRate(new TimerTask() {
			double time = 0.0;
			int pos = 0;
			parole p = paroles.get(pos);
		    public void run() {
		    	time+=vitesseRate;   	
		    	if(pos<paroles.size()&&time>=paroles.get(pos).getTime()) {
		    		if(affiche[pos-1>0?pos-1:0])ChangeProcessus(p,time);
		    		p = paroles.get(pos);
		    		pos++;
		    		if(!Activer_voix.get(p.getVoix()))return;
		    		if(p.getText().isEmpty()) {
		    			paroleFrame.insertDocument("\n",Color.GRAY,paroleFrame.length());
		    		}else {
			    		paroleFrame.insertDocument(Type_parole.get(p.getType()), Color.green,paroleFrame.length());
			    		paroleFrame.insertDocument(Voix_parole.get(p.getVoix())+":", Voix_color.get(p.getVoix()),paroleFrame.length());
		    			paroleFrame.insertDocument(p.getText()+"\n", Color.GRAY,paroleFrame.length());
		    		}
		    		affiche[pos-1] = true;
		    	}else {
		    		if(!affiche[pos-1>0?pos-1:0])return;
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
			paroleFrame.insertDocument(r, Voix_color.get(p.getVoix()), paroleFrame.length()-rest);
		}
	}

	public void playNotes(Music music){
		try {
			syn.open();
		} catch (MidiUnavailableException e) {
			
			System.out.println("echou ouvrir Synthesizer");
		}
		final MidiChannel[] channels = syn.getChannels();
		for(int i=0;i<music.getNotes().size();i++) {
			ArrayList<note> notes = music.getNotes().get(i);
			if(notes.isEmpty())continue;
			Timer timer = new Timer();
			timers.add(timer);
			MidiChannel channel = channels[i] ;
			timer.scheduleAtFixedRate(new TimerTask() {
											  double time = 0.0;
											  int pos = 0;
											  @Override
											  public void run() {
												  time += vitesseRate;
												  try {
													  if(time>=notes.get(pos).getTime()) {
														  note current = notes.get(pos);
														  if(128==current.getCommand()) {
															  channel.noteOff(current.getHauteur(), current.getPuissance()-pussanceOffset>0?current.getPuissance()-pussanceOffset:0);
														  }
														  else if(144==current.getCommand()) {
															  channel.noteOn(current.getHauteur(), current.getPuissance()-pussanceOffset>0?current.getPuissance()-pussanceOffset:0);
														  }
														  else if(160==current.getCommand()) {
															  channel.setPolyPressure(current.getHauteur(), current.getPuissance());
														  }
														  else if(176==current.getCommand()) {
															  channel.controlChange(current.getHauteur(), current.getPuissance());
														  }
														  else if(192==current.getCommand()) {
															  channel.programChange(current.getPuissance(), current.getHauteur());
														  }
														  paroleFrame.changeBeat(current.getHauteur());
														  pos++;
														  if(pos==notes.size()) {
															  this.cancel();
														  }
													  }
												  }catch(Exception e) {
												  }
											  }
										  }
					, new Date(), 1);
		}
	}
    public void changeVite(double vitesseRate) {
    	this.vitesseRate = vitesseRate;
	}
    public void changeHauteur(int pussanceOffset) {
    	this.pussanceOffset = pussanceOffset;
	}
    public void changeActiver(int voix) {
    	if(Activer_voix.get(voix)) {
    		Activer_voix.replace(voix, false);  
    		System.out.println("Desactiver "+Voix_parole.get(voix));
    	}else {
    		Activer_voix.replace(voix, true);    		
    		System.out.println("Activer "+Voix_parole.get(voix));
    	}
    }
}
