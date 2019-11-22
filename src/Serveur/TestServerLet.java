package Serveur;

import java.util.Map;

import Resource.Request;
import Resource.Response;

public class TestServerLet implements ServerLet {

	String name;
	public TestServerLet(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "test";
	}

	@Override
	public void service(Request req, Response res) {
		// TODO Auto-generated method stub
		res.setContent("成功接收，现在回复...");
	}

	@Override
	public void setContextes(Map<String,Object> serverLetContextes) {
		// TODO Auto-generated method stub

	}

}
