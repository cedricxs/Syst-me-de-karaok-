package Client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner;
import Resource.Connector;
import Resource.ConnectorClient;
import Resource.Data;
import Music.Music;
import Resource.Request;
import Resource.Response;

public class Client{
	//connector communique avec serveur
	Connector connexion;
	MyPlayer player;
	//console pour rentrer les commandes
	Scanner console;
	String utilisateur;
	public Client() {
		try {
			//connecter avec serveur
			connexion = new ConnectorClient(new Socket("127.0.0.1",8888),this);
			InitClient();
		} catch (UnknownHostException e) {
			System.out.println("IP de serveur incorrecte...");
		} catch (IOException e) {
			System.out.println("Echec de connexion au serveur...");
			System.exit(0);
		}
	}

	//initialiser le client
	public void InitClient(){
		player = new MyPlayer();
		console = new Scanner(System.in);
	}
	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}

	public void start() {
		new Thread(connexion).start();
		//envoie un requete pour statistique
		Request statistique = new Request("statistique");
		statistique.setUtilisateur(utilisateur);
		connexion.send(statistique);
		while(true) {
			//obtient un String depuis console et le parse devient requete
			String content = console.nextLine();
			Data req = parseRequest(content);
			if(req!=null) {
				//l'envoie a serveur
				connexion.send(req);
			}
		}
	}

	public void playMusic(Music music) {
		player.playMusic(music);
	}


	public void service(Data data) {
		try {
			parseResponse(data);
		}catch(Exception e) {
			System.out.println("parseResponse échoué...");
		}
	}

	Data parseRequest(String content) {
		Request req;
		try {
			if(content.indexOf("show")!=-1){
				req = new Request("show");
			}
			else if(content.indexOf("cv")!=-1) {
				Double rate = Double.valueOf(content.split(":")[1]);
				player.changeVite(rate);
				return null;
			}
			else if(content.indexOf("ch")!=-1) {
				int hauteur = Integer.valueOf(content.split(":")[1]);
				player.changeHauteur(hauteur);
				return null;
			}
			else if(content.indexOf("ca")!=-1) {
				int voix = Integer.valueOf(content.split(":")[1]);
				player.changeActiver(voix);
				return null;
			}
			else{
				req = new Request("play");
				req.setContent(content);
			}
			req.setUtilisateur(utilisateur);
			return req;
		}catch(Exception e) {
			return null;
		}
	}

	//parse un reponde
	/*reponde.status:300->showAllMusic
					 404->No such Music
					 200->Ok
					 100->Statistique
	*/
	@SuppressWarnings("unchecked")
	void parseResponse(Data data) {
		Response res = (Response)data;
		if(res.getStatus()==300 | res.getStatus()==404) {
			System.out.println(res.getContent());
			return;
		}
		else if(res.getStatus()==100){
			Map<String,Object> result = (Map<String, Object>) res.getContent();
			String music = (String) result.get("music");
			int nb = (int) result.get("nb_music");
			String max_user = (String)result.get("max_user");
			int max_lectures = (int)result.get("max_lectures");
			System.out.println("Bonjour "+utilisateur+" !");
			System.out.println("Morceau le plus joué : "+music);
			System.out.println("Nombre de lectures : "+nb);
			System.out.println("Utilisateur ayant joué le plus de morceaux : " + max_user + " avec " + max_lectures + " morceaux");
		}
		else if(res.getStatus()==200){
			Music music = (Music)res.getContent();
			playMusic(music);
		}
	}
}
