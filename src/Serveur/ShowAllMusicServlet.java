package Serveur;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Music.Music;
import Resource.Request;
import Resource.Response;

public class ShowAllMusicServlet implements Servlet{

	String name;
	private Map<String,Object> servletContexte;
	public ShowAllMusicServlet(String name) {
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
		List<Music> musics = (List<Music>) servletContexte.get("musics");
		List<String> result = new ArrayList<String>();
		for(Music m:musics) {
			result.add(m.getName());
		}
		res.setStatus(300);
		res.setContent(result);
	}

	@Override
	public void setServletContexte(Map<String, Object> servletContexte) {
		// TODO Auto-generated method stub
		this.servletContexte = servletContexte;
	}


}
