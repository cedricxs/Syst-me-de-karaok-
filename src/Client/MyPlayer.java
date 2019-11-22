package Client;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MyPlayer{

	Player playerMp3;
	
	public MyPlayer() {
		
	}
	public static void main(String[] args) {
		MyPlayer p = new MyPlayer();
		p.playMp3("芒种");
	}
	//播放方法
    public void playMp3(String music) {
    		String fileName = "client/"+music+".mp3";
            BufferedInputStream buffer;
			try {
				buffer = new BufferedInputStream(new FileInputStream(new File(fileName)));
				playerMp3 = new Player(buffer);
				new Thread(new Mp3()).start();
			} catch (FileNotFoundException | JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    class Mp3 implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				playerMp3.play();
			} catch (JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    }
	
	
}
