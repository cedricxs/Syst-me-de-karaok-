package Resource;

public interface Connector extends Runnable{
	void service(Data data);
	Data receive();
	void send(Data data);
	void close();
}
