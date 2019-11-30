package Resource;

import java.net.Socket;

import Serveur.Server;

public class ConnectorServer extends Connector{

	Server handler;
	
	public ConnectorServer(Socket connexion, Server handler) {
		super(connexion);
		this.handler = handler;
	}
	
	@Override
	public void service(Data data) {
		Data result = handler.service(data);
		send(result);
	}



}
