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
import Serveur.PlayMusicServlet;

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
			Type_parole.put(0,"(trémolo)");
			Type_parole.put(1,"(crié)");
			Type_parole.put(2,"(portamento)");
		} catch (Exception e) {
		}
	}

	public void playMusic(Music music) throws MidiUnavailableException {
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

	public void playNotes(Music music) throws MidiUnavailableException {
		Timer[] timers = new Timer[music.getNotes().size()];
		Synthesizer syn = MidiSystem.getSynthesizer();
		syn.open();
		final MidiChannel[] canaux = syn.getChannels();
		for(int i=0;i<timers.length;i++) {
			timers[i] = new Timer();
			ArrayList<note> notes = music.getNotes().get(i);
			MidiChannel channel = canaux[i] ;
			timers[i].scheduleAtFixedRate(new TimerTask() {
											  int mtime = 0;
											  int pos = 0;
											  @Override
											  public void run() {
												  mtime++;
												  if(mtime>=notes.get(pos).getTime()) {
													  if(!notes.get(pos).isMetaNote()) {
														  if(128<=notes.get(pos).getChannel()&&notes.get(pos).getChannel()<144) {
															  channel.noteOff(notes.get(pos).getHauteur(), notes.get(pos).getPuissance());
														  }
														  else if(144<=notes.get(pos).getChannel()&&notes.get(pos).getChannel()<160) {
															  channel.noteOn(notes.get(pos).getHauteur(), notes.get(pos).getPuissance());
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
													  }else {
														  //System.out.println(notes.get(pos).getAction());
														  if(notes.get(pos).getAction()==81) {

															  byte[] data = notes.get(pos).getData();
														  }
													  }
													  pos++;
												  }





											  }

										  }
					, new Date(), 1);
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

	public static void main(String[] args) {
		PlayMusicServlet p = new PlayMusicServlet("");
		Music music = p.parseMusic("let it go");
//		for(int i=0;i<music.getNotes().get(1).size();i++) {
//			System.out.println(music.getNotes().get(1).get(i).getChannel());
//		}
		MyPlayer m = new MyPlayer();
		try {
			m.playNotes(music);
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
