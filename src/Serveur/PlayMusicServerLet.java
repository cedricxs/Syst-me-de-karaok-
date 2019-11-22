package Serveur;

import java.util.Map;

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
			res.setStatus(200);
			res.setName((String)req.getContent());
			byte[] bytes = ReadFromFile.readFileByBytes((String)req.getContent());
			res.setContent(bytes);	
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


}
