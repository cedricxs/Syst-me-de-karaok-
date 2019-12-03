package Client;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.*;

import Music.*;
import Serveur.PlayMusicServlet;

public class MyPlayer{
	double rate = 1.0;
	public MyPlayer() {

	}
	
	public void playMusic(Music music) throws MidiUnavailableException {

		playNotes(music);
	}

    
	//play a music myself
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
					mtime+=rate;
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
							System.out.println(notes.get(pos).getAction());
							
						}
						pos++;
					}
					
					
					
					
					
				}
    			
    		}
    				, new Date(), 1);
    	}
    	
    	
    	
	}

    public void changeVite(float viteRate) {

	}
    
    public void changeHauteur(int ecart) {
		
	}	
    public static void main(String[] args) {
		PlayMusicServlet p = new PlayMusicServlet("");
		Music music = p.parseMusic("let it go");
		System.out.println(music.getVitesse());
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
		Scanner s = new Scanner(System.in);
		while(true) {
			if(s.nextLine().indexOf("cv")!=-1) {
				m.rate+=0.5;
			}
		}
    }
}
