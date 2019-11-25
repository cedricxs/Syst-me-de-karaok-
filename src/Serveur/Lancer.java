package Serveur;


public class Lancer {

	public static void main(String[] args){
		Server server = new Server(8888,"log.txt");
		server.start();
	}
}
