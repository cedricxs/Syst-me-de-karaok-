package Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadFromFile {
	public static void main(String[] args) {
		//readFileByBytes("music/baga01.mid");
	}
    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     * @throws IOException 
     */
    public static byte[] readFileByBytes(String fileName) throws IOException {
        InputStream in = null;
        System.out.println("以字节为单位读取文件内容，一次读多个字节：");
        in = new FileInputStream("music/"+fileName+".mp3");
        int total = ReadFromFile.showAvailableBytes(in);
        // 一次读多个字节
        byte[] tempbytes = new byte[total];
        int byteread = 0;
        // 读入多个字节到字节数组中，byteread为一次读入的字节数
        while ((byteread = in.read(tempbytes)) != -1) {
            System.out.println("-----------"+byteread*100/total+"/100%-----------");
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e1) {
            }
        }
        return tempbytes;
    }
    /**
     * 显示输入流中还剩的字节数
     */
    private static int showAvailableBytes(InputStream in) {
        try {
            System.out.println("当前字节输入流中的字节数为:" + in.available());
            return in.available();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}