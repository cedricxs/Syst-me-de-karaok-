package Resource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Client.Client;

public class ConnectorClient implements Connector{

	Socket connexion;
	ObjectOutputStream out;
	ObjectInputStream in;
	boolean isRunning;
	Client handler;
	
	public ConnectorClient(Socket connexion, Client handler) {
		// TODO Auto-generated constructor stub
		this.connexion = connexion;
		try {
			this.out = new ObjectOutputStream(connexion.getOutputStream());
			this.in = new ObjectInputStream(connexion.getInputStream());
			this.handler = handler;
			isRunning = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("fail to create conntector...");
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
		handler.service(data);
	}

	@Override
	public Data receive() {
		// TODO Auto-generated method stub
		try {
			Data data = (Data)in.readObject();
			return data;
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("fail to receive data...");
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
			System.out.println("request has been sent...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("fail to send request...");
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
