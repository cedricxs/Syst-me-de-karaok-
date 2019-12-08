package Serveur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import Music.parole;
import Resource.Request;
import Resource.Response;

public class PlayMusicServlet implements Servlet{

	private String name;
	private Map<String,Object> servletContexte;

	public PlayMusicServlet(String name) {
		this.name = name;
	}

	public void setServletContexte(Map<String,Object> servletContexte){
		this.servletContexte = servletContexte;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void service(Request req, Response res) {
		Map<String,Integer> nb_joue = (Map<String, Integer>) servletContexte.get("nb_joue");
		Map<String,Integer> nb_lectures = (Map<String, Integer>) servletContexte.get("nb_lectures");
		String musicName = (String)req.getContent();
		res.setName(musicName);
		Music music = parseMusic(musicName);
		String username = req.getUtilisateur();
		if(music==null) {
			res.setStatus(404);
			res.setContent("Not Found");
		}
		else {
			int nb = nb_joue.get(musicName);
			nb_joue.replace(musicName,++nb);
			int nb_user = nb_lectures.get(username);
			nb_lectures.replace(username,++nb_user);
			res.setStatus(200);
			res.setContent(music);
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	public Music parseMusic(String musicName) {
		Music music = new Music(musicName);
		if(parseNotes(musicName,music)&&parseParoles(musicName, music)) {
			System.out.println("parseMusic réussi");
			return music;
		}else {
			return null;
		}
	}

	/**
	 * parse le fichier .mid
	 *
	 * @param musicName: Titre de la musique
	 *
	 * @param music: Enregistre les notes apres le parse
	 *
	 * @return Etat du parse : booleen
	 */
	public boolean parseNotes(String musicName, Music music) {
		Sequence sequence;
		String fileName = "music/music/"+musicName+".mid";
		try {
			sequence = MidiSystem.getSequence(new File(fileName));
			int so = sequence.getResolution();
			double Tempo = 1.0;
			Track[] tracks = sequence.getTracks();
			ArrayList<ArrayList<note>> notes = music.getNotes();
			for(int i=0;i<tracks.length;i++) {
				for(int j=0;j<tracks[i].size();j++) {
					MidiEvent e = tracks[i].get(j);
					MidiMessage m = e.getMessage();
					if(m instanceof ShortMessage) {
						ShortMessage ms = (ShortMessage)m;
						int status = ms.getStatus();
						int command = ms.getCommand();
						int channel = status - command;
						int hauteur = ms.getData1();
						int puissance = ms.getData2()>127?127:ms.getData2();
						double time = e.getTick()*Tempo;
						notes.get(channel).add(new note(command, hauteur, puissance , time));
					}
					else if(m instanceof MetaMessage) {
						MetaMessage ms = (MetaMessage)m;
						if(ms.getType()==81) {
							int bpm = bytesToInt(ms.getData());
							Tempo = (double)bpm/(1000*so);
							for(int k=0;k<i;k++) {
								ArrayList<note> k_track = music.getNotes().get(k);
								for(note n:k_track) {
								n.setTime(n.getTime()*Tempo);
								}
							}	
						}
					}
				}
			}
			return true;
		} catch (InvalidMidiDataException | IOException e1) {
			return false;
		}
	}
	private int bytesToInt(byte[] src) {
		int value;
		value = (int) (((src[0] & 0xFF)<<16)
				|((src[1] & 0xFF)<<8)
				|(src[2] & 0xFF));
		return value;
	}

	/**
     * parse fichier de .lrc
     *
     * @param String musicName
     * 		  Music music: enregistre les paroles apres parser
     *
     * @return l'etat de parser : boolean
     */
    private boolean parseParoles(String musicName, Music music) {
        ArrayList<parole> paroles = music.getParoles();
        String fileName = "music/lyric/"+musicName+".lrc";
        try {
             String encoding = "utf-8"; // encoding correspendant le lyric fichier
            // String encoding = "GBK";
            File file = new File(fileName);
            if (file.isFile() && file.exists()) { // si le fichier existe
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String regex = "\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,2})\\]"; // reg
                Pattern pattern = Pattern.compile(regex); // creer Pattern
                String lineStr = null; // lire un ligne chaque fois
                int i=0;
                while ((lineStr = bufferedReader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(lineStr);
                    while (matcher.find()) {
                        // [02:34.94] ----correspendant---> [min:sec.millsec]
                        String min = matcher.group(1); // min
                        String sec = matcher.group(2); // sec
                        String mill = matcher.group(3); // millsec, il faut multifier 10
                        long time = getLongTime(min, sec, mill + "0");
                        // obtenir le parole pour le moment
                        String text = lineStr.substring(matcher.end());

                        if(!paroles.isEmpty()) {
                        	parole last = paroles.get(paroles.size()-1);
                        	last.setDuree(time-last.getTime());
                        }
                        parole p = new parole(time,text);
                        p.setType(i++%3);
                        paroles.add(p);
                    }
                }
                parole last = paroles.get(paroles.size()-1);
            	last.setDuree(1000);
                read.close();
                System.out.println("parse fini...");
                return true;
            } else {
                System.out.println("Fichier non trouvé:" + fileName);
                return false;
            }
        } catch (Exception e) {
            System.out.println("parseParoles échoué");
            return false;
        }
    }

    /**
     * retourner le nombre de millseconde(long) selon le l'entree de min,sec,millsec
     *
     * @param min
     *
     * @param sec
     *
     * @param mill
     *
     * @return time
     */
    private long getLongTime(String min, String sec, String mill) {
        int m = Integer.parseInt(min);
        int s = Integer.parseInt(sec);
        int ms = Integer.parseInt(mill);
        if (s >= 60) {
            System.out.println("warning: un mal formation de date--> [" + min + ":" + sec + "."
                    + mill.substring(0, 2) + "]");
        }
        // calculer le nombre de millseconde
        long time = m * 60 * 1000 + s * 1000 + ms;
        return time;
    }

}
