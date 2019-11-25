package Serveur;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Music.Music;
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
/**
 * 实现点对点通信，服务器只负责监听客户端链接请求和发送链接请求
 * 真正的通信流是客户端之间建立的
 * @author cedricxs
 *
 */


public class Server {
	ServerSocket server = null;
	BufferedWriter file =null;

	//客户端通信池
	ArrayList<Connector>clients = null;
	ArrayList<ServerLet> serverLets;
	ArrayList<Music> musics;
	Map<String,Object> serverLetContextes;
	
	
	public Server(int port,String FileName) {
		InitServer(port,FileName);
	}
	
	private void addServerLet(ServerLet s) {
		serverLets.add(s);
		s.setContextes(serverLetContextes);
	}
	
	public void InitServer(int port,String FileName) {
		try {
			server = new ServerSocket(port);
			clients = new ArrayList<Connector>();
			musics = new ArrayList<Music>();
			musics.add(new Music("haha"));
			musics.add(new Music("xixi"));
			musics.add(new Music("芒种"));
			musics.add(new Music("When you're gone"));
			serverLets = new ArrayList<ServerLet>();
			serverLetContextes = new HashMap<String,Object>();
			serverLetContextes.put("server", server);
			serverLetContextes.put("file", file);
			serverLetContextes.put("clients",clients);
			serverLetContextes.put("serverLets",serverLets);
			serverLetContextes.put("musics",musics);
			addServerLet(new PlayMusicServerLet("play"));
			addServerLet(new TestServerLet("test"));
			addServerLet(new ShowAllMusicServerLet("show"));
			addServerLet(new TestServerLet("ajouter"));
			
		} catch (IOException e) {
			System.out.println("端口已被占用...");
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
			//main线程为服务器主线程
			//通信模式由客户端决定,因为是客户端与服务器建立连接
			//客户端决定建立连接之后直接运行完程序，则通信管道Socket直接失效
			//可分为持续链接和非持续链接状态,可参照HTTP请求/响应头的connect:alive
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
		for(ServerLet s:serverLets) {
			if(s.getName().equals(req.getCommande())) {
				System.out.println("开始处理请求...");
				s.service(req, res);
			}
		}
		return res;
	}
	
	
}


