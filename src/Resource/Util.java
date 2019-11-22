package Resource;

import java.io.Closeable;

public class Util {
	@SafeVarargs
	public static<T extends Closeable> void closeAll(T ... io) {
		for(Closeable t:io) {
			try {
				t.close();
			} catch (Exception e) {
			}
		}
	}
	public static void main(String[] args) {
		System.out.println(String.valueOf(true));
		String s = null;
		s+="123";
		System.out.println(s);
	}
}