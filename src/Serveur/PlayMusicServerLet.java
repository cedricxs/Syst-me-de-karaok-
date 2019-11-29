package Serveur;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import Music.Music;
import Music.note;
import Resource.Request;
import Resource.Response;

public class PlayMusicServerLet implements ServerLet{

	private String name;
	Map<String,int> nb_played_music;
	Map<String, int> nb_musics_user;

	public PlayMusicServerLet(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}

	void setMusicStats(Map<String,int> nb_played_music){
		this.nb_played_music = nb_played_music;
	}

	void setUserStats(Map<String,int> nb_music_user){
		this.nb_music_user = nb_music_user;
	}

	@Override
	public void service(Request req, Response res) {
			// TODO Auto-generated method stub
			String name = (String)req.getContent();
			res.setName(name);
					Music m = ParseMusic("music/"+name+".mid");
					if(m==null) {
						res.setStatus(404);
						res.setContent("Not Found");
					}
					else {
						int iterations = nb_played_music.get(name);
						nb_played_music.put(name,++iterations);
						res.setStatus(200);
						res.setContent(m);
					}
			
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
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

}
