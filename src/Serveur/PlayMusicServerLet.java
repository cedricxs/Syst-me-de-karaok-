package Serveur;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import Music.Music;
import Music.note;
import Resource.ReadFromFile;
import Resource.Request;
import Resource.Response;

public class PlayMusicServerLet implements ServerLet{

	private String name;
	@SuppressWarnings("unused")
	private Map<String,Object> serverLetContextes;
	
	public PlayMusicServerLet(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}
	
	public void setContextes(Map<String,Object> serverLetContextes) {
		this.serverLetContextes = serverLetContextes;
	}

	@Override
	public void service(Request req, Response res) {
		try {
			// TODO Auto-generated method stub
			res.setName((String)req.getContent());
			@SuppressWarnings("unchecked")
			ArrayList<Music> musics = (ArrayList<Music>)serverLetContextes.get("musics");
			for(Music music:musics) {
				if(music.getName().equals(req.getContent())) {
					ParseMusic("music/"+music.getName()+".mid");
					
//					//res.setContent(music);
//					Map<String,byte[]> result = new HashMap<String,byte[]>();
//					byte[] m = ReadFromFile.readFileByBytes("music/"+(String)req.getContent()+".mp3");
//					byte[] l = ReadFromFile.readFileByBytes("music/"+(String)req.getContent()+".lrc");
//					result.put("music", m);
//					result.put("lrc",l);
//					res.setContent(result);	
//					res.setStatus(200);
//					break;
				}
			}
			if(res.getStatus()!=200) {
				res.setStatus(404);
				res.setContent("Not Found");
			}
			
		}catch(Exception e) {
				res.setStatus(404);
				res.setContent("Not Found");
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
			Track[] tracks = sequence.getTracks();
			Music music = new Music(fileName);
			music.setVite(sequence.getResolution());
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
						int instrument = ms.getData1();
						int hauteur = ms.getData2();
						long time = e.getTick();
						track.add(new note(action, channel,  instrument,  hauteur, time));
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
