package Serveur;

import java.io.IOException;

public class Lancer {

	public static void main(String[] args) throws IOException {
		Server server = new Server(8888,"log.txt");
		server.start();
	}

}
