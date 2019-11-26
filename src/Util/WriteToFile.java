package Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WriteToFile {

	public WriteToFile() {
		// TODO Auto-generated constructor stub
	}
	public static void write(byte[] bytes,String fileName) {
		File f = new File(fileName);
		OutputStream out = null;
		try {
			out = new FileOutputStream(f);
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                	
                }
            }
        }
		
		
	}
	

}
