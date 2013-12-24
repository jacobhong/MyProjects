import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Download implements Runnable {
	private String _url, name;
	private String filePath = "c://reddit//";

	public Download(String u, String n) {
		_url = u;
		name = n;
	}

	@Override
	public void run() {
		/*
		 * setup streams.. write image as bytes to filePath
		 */
		InputStream is = null;
		OutputStream os = null;
		try {
			URL url = new URL(_url);
			is = url.openStream();
			os = new FileOutputStream(filePath + name + ".jpg");
			for (int b; (b = is.read()) != -1;) {
				os.write(b);
			}
		} catch (MalformedURLException e1) {
			System.out.println("invalid url");
			System.out.println(e1);
		} catch (IOException e) {
			System.out.println("no stream");
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
