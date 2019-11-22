package Client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
			boolean played = false;
			for(Music m:musics) {
				if(m.getName().equals(content)) {
					play(m.getName());
					played = true;
					break;
				}
			}
			if(played)break;
			Request req = new Request("play");
			req.setContent(content);
			connexion.send(req);
		}
	}
	
	public void play(String name) {
		player.playMp3(name);
	}

	
	public void service(Data data) {
		try {
			// TODO Auto-generated method stub
			Response res = (Response)data;
			System.out.println(res.getStatus());
			if(res.getStatus()==404) {
				System.out.println(res.getContent());
				return;
			}
			byte[] bytes = (byte[])res.getContent();
			WriteToFile.write(bytes, res.getName());
			Music m = new Music(res.getName());
			musics.add(m);
			play(m.getName());
		}catch(Exception e) {
			System.out.println("解析相应失败...");
		}
	}

}





