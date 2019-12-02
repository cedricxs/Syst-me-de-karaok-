package Serveur;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Resource.ConnectorServer;
import Resource.Connector;
import Resource.Data;
import Resource.Request;
import Resource.Response; 
import java.io.File;
import java.io.IOException;


public class Server {
	ServerSocket server = null;
	ArrayList<Servlet> servlets;
	ArrayList<String> musics;
	Map<String,Object> servletContexte;
	//Nombre de musiques jouées par chaque utilisateur
	Map<String,ArrayList<String>> music_user;
	Map<String,Integer> nb_joue;
	
	public Server(int port,String FileName) {
		InitServer(port,FileName);
	}
	
	private void addServlet(Servlet s) {
		servlets.add(s);
		s.setServletContexte(servletContexte);
	}
	
	public void InitMusics() {
		String path = "music/music/";
		File musicsDir = new File(path);
		if(musicsDir.isDirectory()) {
			String[] musicsAll = musicsDir.list();
			for(String musicName:musicsAll) {
				String music = musicName.substring(0,musicName.indexOf(".mid"));
				if(!musics.contains(music)) {
					musics.add(music);
					music_user.put(music, new ArrayList<String>());
					nb_joue.put(music,0);
				}
			}
		}
	}
	public void InitServer(int port,String FileName) {
		try {
			server = new ServerSocket(port);
			musics = new ArrayList<String>();
			music_user = new HashMap<String, ArrayList<String>>(); 
			nb_joue = new HashMap<String,Integer>();
			servletContexte = new HashMap<String, Object>();
			servlets = new ArrayList<Servlet>();
			InitMusics();		
			addServlet(new PlayMusicServlet("play"));
			addServlet(new StatistiqueServlet("statistique"));
			addServlet(new ShowAllMusicServlet("show"));
			servletContexte.put("nb_joue",nb_joue);
			servletContexte.put("music_user",music_user);
			servletContexte.put("musics",musics);
		} catch (IOException e) {
			System.out.println("la porte déja utilisé...");
			System.exit(0);
		}
		System.out.println("serveur demarrer!!!");
	}
	
	public void start() {
		while(true) {
			Socket socket;
			try {
				socket = server.accept();
				startChannel(socket);
				System.out.println("has client come in");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("fail to get client...");
			}
			
		}
	}
	
	void startChannel(Socket socket) {
		Connector client = new ConnectorServer (socket,this);
		new Thread(client).start();
	}

	public Data service(Data data) {
		Request req = (Request)data;
		Response res = new Response();
		for(Servlet s:servlets) {
			if(s.getName().equals(req.getCommande())) {
				System.out.println("Traitement de la requete...");
				s.service(req, res);
			}
		}
		return res;
	}
	
}


