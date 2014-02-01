import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Download implements Runnable {
	/*
	 * can remove download method from Scraper class and use the Download thread
	 * class, it is much faster but will cause a lot of timeout issues so use
	 * with caution, or if you dont care about getting every image
	 */
	private String _url, name;
	private static final String filePath = "c:\\reddit\\";

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
			File file = new File(filePath);
			file.mkdir();
			os = new FileOutputStream(file + name + ".jpg");
			for (int b; (b = is.read()) != -1;) {
				os.write(b);
			}
		} catch (MalformedURLException mue) {
			System.out.println("invalid url");
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
