package Serveur;

import java.util.Map;

import Resource.Request;
import Resource.Response;

public interface ServerLet {
	String getName();
	void service(Request req, Response res);
	void setMusicStats(Map<String,int> nb_played_music);
}
