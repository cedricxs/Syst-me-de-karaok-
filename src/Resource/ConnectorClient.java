package Resource;

import java.net.Socket;

import Client.Client;

public class ConnectorClient extends Connector{

	
	Client handler;
	
	public ConnectorClient(Socket connexion, Client handler) {
		// TODO Auto-generated constructor stub
		super(connexion);
		this.handler = handler;
	}

	
	@Override
	public void service(Data data) {
		handler.service(data);
	}

}
