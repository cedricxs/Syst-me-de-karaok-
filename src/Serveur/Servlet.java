package Serveur;

import java.util.Map;
import Resource.Request;
import Resource.Response;

public interface Servlet {
	String getName();
	void service(Request req, Response res);
	void setServletContexte(Map<String,Object> servletContexte);
}
