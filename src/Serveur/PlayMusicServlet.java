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
		// TODO Auto-generated method stub
		Map<String,Integer> nb_joue = (Map<String, Integer>) servletContexte.get("nb_joue");
		Map<String,ArrayList<String>> music_user = (Map<String, ArrayList<String>>) servletContexte.get("music_user");
		String musicName = (String)req.getContent();
		res.setName(musicName);
		Music music = parseMusic(musicName);
		if(music==null) {
			res.setStatus(404);
			res.setContent("Not Found");
		}
		else {
			int nb = nb_joue.get(musicName);
			nb_joue.put(musicName,++nb);
			if(!music_user.get(musicName).contains(req.getUtilisateur())){
				music_user.get(musicName).add(req.getUtilisateur());				
			}
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
			System.out.println("reussir parser Music");
			return music;	
		}else {
			return null;
		}
	}

	public boolean parseNotes(String musicName, Music music) {
		Sequence sequence;
		String fileName = "music/music/"+musicName+".mid";
		try {
			sequence = MidiSystem.getSequence(new File(fileName));
			music.setVitesse(sequence.getResolution());
			Track[] tracks = sequence.getTracks();
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
						int puissance = ms.getData2()>127?127:ms.getData2();
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
			return true;
		} catch (InvalidMidiDataException | IOException e1) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	/**
     * parse fichier de .lrc
     * 
     * @param String musicName
     * 		  Music music: enregistre les paroles apres parser
     *            
     * @return le etat de parser
     */
    private boolean parseParoles(String musicName, Music music) {
        ArrayList<parole> paroles = music.getParoles();
        String fileName = "music/lyric/"+musicName+".lrc";
        try {
             String encoding = "utf-8"; // 字符编码，若与歌词文件编码不符将会出现乱码
            // String encoding = "GBK";
            File file = new File(fileName);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String regex = "\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,2})\\]"; // 正则表达式
                Pattern pattern = Pattern.compile(regex); // 创建 Pattern 对象
                String lineStr = null; // 每次读取一行字符串
                int i=0;
                while ((lineStr = bufferedReader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(lineStr);
                    while (matcher.find()) {
                        // System.out.println(m.group(0)); // 例：[02:34.94]
                        // [02:34.94] ----对应---> [分钟:秒.毫秒]
                        String min = matcher.group(1); // 分钟
                        String sec = matcher.group(2); // 秒
                        String mill = matcher.group(3); // 毫秒，注意这里其实还要乘以10
                        long time = getLongTime(min, sec, mill + "0");
                        // 获取当前时间的歌词信息
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
            	last.setDuree(10);
                read.close();
                System.out.println("解析完成。。。");
                return true;
            } else {
                System.out.println("找不到指定的文件:" + fileName);
                return false;
            }
        } catch (Exception e) {
            System.out.println("读取文件出错!");
            //e.printStackTrace();
            return false;
        }
    }

    /**
     * 将以字符串形式给定的分钟、秒钟、毫秒转换成一个以毫秒为单位的long型数
     * 
     * @param min
     *            分钟
     * @param sec
     *            秒钟
     * @param mill
     *            毫秒
     * @return
     */
    private long getLongTime(String min, String sec, String mill) {
        // 转成整型
        int m = Integer.parseInt(min);
        int s = Integer.parseInt(sec);
        int ms = Integer.parseInt(mill);

        if (s >= 60) {
            System.out.println("警告: 出现了一个时间不正确的项 --> [" + min + ":" + sec + "."
                    + mill.substring(0, 2) + "]");
        }
        // 组合成一个长整型表示的以毫秒为单位的时间
        long time = m * 60 * 1000 + s * 1000 + ms;
        return time;
    }

}

