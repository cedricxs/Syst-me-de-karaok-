package Serveur;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import Resource.ConnectorServer;
import Resource.Connector;
import Resource.Data;
import Resource.Request;
import Resource.Response; 

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Server {
	ServerSocket server = null;
	BufferedWriter file =null;

	//les connexion avec les clients
	ArrayList<Connector>clients = null;

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
		musics = new ArrayList<String>();
		music_user = new HashMap<String, ArrayList<String>>(); 
		nb_joue = new HashMap<String,Integer>();
		String path = "music/music/";
		File musicsDir = new File(path);
		if(musicsDir.isDirectory()) {
			String[] musicsAll = musicsDir.list();
			for(String musicName:musicsAll) {
				String music = musicName.substring(0,musicName.indexOf(".mid"));
				musics.add(music);
				music_user.put(music, new ArrayList<String>());
				nb_joue.put(music,0);
			}
		}
	}
	public void InitServer(int port,String FileName) {
		try {
			server = new ServerSocket(port);
			clients = new ArrayList<Connector>();
			InitMusics();		
			servletContexte = new HashMap<String, Object>();
			servlets = new ArrayList<Servlet>();
			addServlet(new PlayMusicServlet("play"));
			addServlet(new ShowStatistique("statistique"));
			addServlet(new ShowAllMusicServlet("show"));
			servletContexte.put("nb_joue",nb_joue);
			servletContexte.put("music_user",music_user);
			servletContexte.put("musics",musics);
		} catch (IOException e) {
			System.out.println("la porte déja utilisé...");
			System.exit(0);
		}
		try {
			//FileOutputStream(file,true)为追加写模式
			file = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(FileName),true)));
			writeToFile("当前服务器启动时间:"+new Date().toString());
			
		} catch ( IOException e) {
			System.out.println("日志文件打开失败...");
		}
		System.out.println("服务器已启动!!!");
	}
	
	public void start() {
		while(true) {
			Socket socket;
			try {
				socket = server.accept();
				startChannel(socket);
				System.out.println("有人连进来了");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("接受客户端出错...");
			}
			
		}
	}
	
	void startChannel(Socket socket) {
		Connector client = new ConnectorServer (socket,this);
		new Thread(client).start();
		clients.add(client);
	}

	
		
	void writeToFile(String msg) {
		try {
			file.write(msg);
			file.newLine();
			file.flush();
		} catch (IOException e) {
			System.out.println("日志文件记录失败");
		}
	}

	public Data service(Data data) {
		// TODO Auto-generated method stub
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


