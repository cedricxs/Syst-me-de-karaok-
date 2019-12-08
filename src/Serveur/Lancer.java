package Serveur;

//Lancer le serveur
public class Lancer {

	public static void main(String[] args){
		Server server = new Server(8888);
		server.start();
	}
}
