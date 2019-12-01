package Serveur;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import Resource.Request;
import Resource.Response;

public class ShowAllMusicServlet implements Servlet{

	String name;
	private Map<String,Object> servletContexte;
	public ShowAllMusicServlet(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void service(Request req, Response res) {
		List<String> musics = (List<String>) servletContexte.get("musics");
		List<String> result = new ArrayList<String>();
		for(String m:musics) {
			result.add(m);
		}
		res.setStatus(300);
		res.setContent(result);
	}

	@Override
	public void setServletContexte(Map<String, Object> servletContexte) {
		this.servletContexte = servletContexte;
	}


}
