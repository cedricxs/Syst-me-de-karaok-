package Resource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Serveur.Server;

public class ConnectorServer implements Connector{

	Socket connexion;
	ObjectOutputStream out;
	ObjectInputStream in;
	boolean isRunning;
	Server handler;
	
	public ConnectorServer(Socket connexion, Server handler) {
		// TODO Auto-generated constructor stub
		this.connexion = connexion;
		try {
			this.out = new ObjectOutputStream(connexion.getOutputStream());
			this.in = new ObjectInputStream(connexion.getInputStream());
			this.handler = handler;
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
	
	@Override
	public void service(Data data) {
		Data result = handler.service(data);
		send(result);
	}

	@Override
	public Data receive() {
		// TODO Auto-generated method stub
		try {
			Data data = (Data)in.readObject();
			return data;
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("client déconnecté...");
			isRunning = false;
			close();
		} 
		return null;
	}

	@Override
	public void send(Data data) {
		// TODO Auto-generated method stub
		try {
			out.writeObject(data);
			out.flush();
			System.out.println("envoie des données...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("envoie des données échouée...");
			isRunning = false;
			close();
		}
		
	}
	
	@Override
	public void close(){
		try {
			connexion.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
