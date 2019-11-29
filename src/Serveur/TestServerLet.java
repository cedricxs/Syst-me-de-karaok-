package Serveur;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Music.Music;
import Resource.Request;
import Resource.Response;

public class TestServerLet implements ServerLet {

	String name;
	private Map<String,Object> serverLetContextes;
	
	public TestServerLet(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void service(Request req, Response res) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<Music> musics = (List<Music>) serverLetContextes.get("musics");
		musics.add((Music)req.getContent());
		String music;
		int fois = 0;
		for(Music m:musics) {
			 if(m.fois>fois){
				 m.getName() = music;
			 }
		}
		res.setStatus(300);
		res.setContent(result);
	}

	@Override
	public void setContextes(Map<String, Object> serverLetContextes) {
		// TODO Auto-generated method stub
		this.serverLetContextes = serverLetContextes;
	}

}
