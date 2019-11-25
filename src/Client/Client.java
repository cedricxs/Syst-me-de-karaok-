package Client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import Resource.Connector;
import Resource.ConnectorClient;
import Resource.Data;
import Music.Music;
import Resource.Request;
import Resource.Response;
import Resource.WriteToFile;

public class Client{
	//与服务器通信的客户端
	Connector connexion;
	MyPlayer player;
	ArrayList<Music> musics;
	//Frame mainFrame;
	Scanner console;
	
	public Client() {
		try {
			connexion = new ConnectorClient(new Socket("127.0.0.1",8888),this);
			InitClient();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("服务器IP地址错误...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("连接服务器失败...");
			System.exit(0);
		}
	}
	
	public void InitClient(){
			player = new MyPlayer();
			musics = new ArrayList<Music>();
			//mainFrame = new Frame();
			console = new Scanner(System.in);
	}
	
	
	public void start() {
		new Thread(connexion).start();
		//mainFrame.lanchFrame();
		while(true) {
			String content = console.nextLine();
			Data req = parseRequest(content);
			if(req!=null) {
				connexion.send(req);				
			}
		}
	}
	
	public void playMusic(Music music) {
		player.playMusic(music);
	}
	
	public void playMp3(String  music) {
		player.playMp3(music);
	}

	
	public void service(Data data) {
		try {
			// TODO Auto-generated method stub
			parseResponse(data);
		}catch(Exception e) {
			System.out.println("解析响应失败...");
		}
	}
	
	Data parseRequest(String content) {
		for(Music m:musics) {
			if(m.getName().equals(content)) {
				playMusic(m);
				return null;
			}
		}
		Request req;
		if(content.indexOf("show")!=-1){ 
			req = new Request("show");
		}
		else if(content.indexOf("ajouter")!=-1){
			req = new Request("ajouter");
			req.setContent(new Music("MyMusic"));
		}
		else if(content.indexOf("changeVite")!=-1) {
			player.changeVite(2);
			return null;
		}
		else{
			req = new Request("play");
			req.setContent(content);
		}
		return req;
	}

	void parseResponse(Data data) {
		Response res = (Response)data;
		System.out.println(res.getStatus());
		if(res.getStatus()==300 | res.getStatus()==404) {
			System.out.println(res.getContent());
			return;
		}
		else {
//			@SuppressWarnings("unchecked")
//			Map<String,byte[]> result = (HashMap<String,byte[]>)res.getContent();
//			byte[] m = result.get("music");
//			byte[] l = result.get("lrc");
//			WriteToFile.write(m, "client/"+res.getName()+".mp3");
//			WriteToFile.write(l, "client/"+res.getName()+".lrc");
//			//Music m = (Music)res.getContent();
//			//musics.add(m);	
//			playMp3(res.getName());
			Music music = (Music)res.getContent();
			System.out.println(music.getVite());
			playMusic(music);
			musics.add(music);
		}
	}
}





