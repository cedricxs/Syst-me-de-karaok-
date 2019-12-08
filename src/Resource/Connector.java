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
		try {
			Data data = (Data)in.readObject();
			return data;
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Fin de la réception des données...");
			isRunning = false;
			close();
		}
		return null;
	}
	public void send(Data data) {
		try {
			out.writeObject(data);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Echec de l'envoi des données...");
			isRunning = false;
			close();
		}

	}
	public void close(){
		try {
			connexion.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
