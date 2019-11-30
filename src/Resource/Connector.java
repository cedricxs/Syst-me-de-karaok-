package Resource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public abstract class Connector implements Runnable{
	Socket connexion;
	ObjectOutputStream out;
	ObjectInputStream in;
	boolean isRunning;
	abstract void service(Data data);
	
	public Connector(Socket connexion) {
		// TODO Auto-generated constructor stub
		this.connexion = connexion;
		try {
			this.out = new ObjectOutputStream(connexion.getOutputStream());
			this.in = new ObjectInputStream(connexion.getInputStream());
			isRunning = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("connexion échouée...");
			isRunning = false;
			close();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRunning) {
			Data data = receive();
			if(isRunning) {
				service(data);
			}
		}
	}
	public Data receive() {
		// TODO Auto-generated method stub
		try {
			Data data = (Data)in.readObject();
			return data;
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("recu les données échoué...");
			isRunning = false;
			close();
		} 
		return null;
	}
	public void send(Data data) {
		// TODO Auto-generated method stub
		try {
			out.writeObject(data);
			out.flush();
			System.out.println("请求已发送...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("请求发送失败...");
			isRunning = false;
			close();
		}
		
	}
	public void close(){
		try {
			connexion.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
