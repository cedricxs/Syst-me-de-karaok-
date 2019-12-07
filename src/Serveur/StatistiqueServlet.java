package Serveur;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import Resource.Request;
import Resource.Response;

public class StatistiqueServlet implements Servlet {

	private String name;
	private Map<String,Object> servletContexte;
	
	public StatistiqueServlet(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void service(Request req, Response res) {
		Map<String,Integer> nb_joue = (Map<String, Integer>) servletContexte.get("nb_joue");
		Map<String,Integer> nb_lectures = (Map<String, Integer>) servletContexte.get("nb_lectures");
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
		String username = req.getUtilisateur();
		if (!nb_lectures.containsKey(username))
			nb_lectures.put(username,0);
		int max_lectures = 0;
		String max_user = null;
		for(String user:nb_lectures.keySet()){
			if(nb_lectures.get(user) >= max_lectures){
				max_lectures = nb_lectures.get(user);
				max_user = user;
			}
		}

		Map<String,Object> result = new HashMap<String,Object>();
		result.put("music", music);
		result.put("nb_music", max);
		result.put("max_user", max_user);
		result.put("max_lectures", max_lectures);
		res.setContent(result);
		res.setStatus(100);
	}

	@Override
	public void setServletContexte(Map<String, Object> servletContexte) {
		this.servletContexte  = servletContexte;
	}

}
