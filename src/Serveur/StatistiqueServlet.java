package Serveur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import Resource.Request;
import Resource.Response;

public class StatistiqueServlet implements Servlet {

	private String name;
	private Map<String,Object> servletContexte;
	
	public StatistiqueServlet(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void service(Request req, Response res) {
		Map<String,Integer> nb_joue = (Map<String, Integer>) servletContexte.get("nb_joue");
		Map<String,ArrayList<String>> music_user = (Map<String, ArrayList<String>>) servletContexte.get("music_user");
		int max = 0;
		String music = null;
		Set<String> keys = nb_joue.keySet();
		for(String key:keys) {
			int nb = nb_joue.get(key);
			if(nb>=max) {
				max = nb;
				music = key;
			}
		}
		ArrayList<String> users = music_user.get(music);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("music", music);
		result.put("nb", max);
		result.put("users",users);
		res.setContent(result);
		res.setStatus(100);
	}

	@Override
	public void setServletContexte(Map<String, Object> servletContexte) {
		// TODO Auto-generated method stub
		this.servletContexte  = servletContexte;
	}

}
