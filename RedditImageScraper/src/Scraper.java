import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
	private String filePath = "c://reddit//";
	private String url = "http://www.reddit.com/r/pics/";
	private int count;
	private String after;

	public static void main(String[] args) {
		Scraper scraper = new Scraper();	
		int i = 0;
		while (i < 10) {
			i++;
			scraper.getImgur();
			scraper.getImgurAddI();
			scraper.getImgurA();
			scraper.getNextPage();
		}

	}

	public void download(String _url, String name) {
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

	public void getImgur() {
		/*
		 * grab all imgur's in the context of http://i.imgur.com/. The second
		 * parameter to download() is the filename
		 */
		try {
			System.out.println("connecting to imgur");
			Elements description = getSubreddit();
			for (Element imgur : description) {
				Pattern pattern = Pattern
						.compile("http://i\\.imgur\\.com/\\w+");
				Matcher matcher = pattern.matcher(imgur.text());
				if (matcher.find()) {
					System.out.println("downloading image: " + " "
							+ matcher.group());
					download((matcher.group() + ".jpg"), matcher.group()
							.substring(18));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("grabbed all imgurs");
		}

	}

	public void getImgurAddI() {
		/*
		 * grab all imgur's in the context of http://imgur.com/, if it is an
		 * album then skip otherwise add "i" to beginning of imgur in order to
		 * get image
		 */
		try {
			System.out.println("finding imgurs without prefix i and adding i");
			Elements description = getSubreddit();
			for (Element imgur : description) {
				Pattern pattern = Pattern.compile("http://imgur\\.com/\\w+");
				Matcher matcher = pattern.matcher(imgur.text());
				if (matcher.find()) {
					if (!matcher.group().endsWith("a")) {
						// make imgur reachable
						String newUrl = matcher.group();
						newUrl = "http://i." + newUrl.substring(7);
						download(newUrl + ".jpg", newUrl.substring(18));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("grabbed all imgurs by adding I");
		}

	}

	private void getImgurA() {
		/*
		 * grab all albums then call extract to get each individual image
		 */
		try {
			System.out.println("connecting to imgur album");
			Elements description = getSubreddit();
			for (Element imgur : description) {
				Pattern pattern2 = Pattern.compile("http://imgur.com/a/\\w+");
				Matcher matcher2 = pattern2.matcher(imgur.text());
				if (matcher2.find()) {
					System.out.println("Downloading image album...." + " "
							+ matcher2.group());
					extract(matcher2.group());
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			System.out.println("extracted all imgur albums");
		}
	}

	private void extract(String album) {
		/*
		 * open connection to imgur album and download each individual image,
		 * validate imgur..if it ends with "s" most likely a thumbnail duplicate
		 * so skip it
		 */
		try {
			Document doc = Jsoup.connect(album).get();
			Elements pics = doc.getElementsByTag("img");
			String image = null;
			for (Element pic : pics) {
				image = pic.attr("data-src");
				if (image != ""
						&& (!image.substring(0, image.length() - 4).endsWith(
								"s"))) {
					if (image.endsWith(".jpg?1") || image.endsWith(".jpg?2")) {
						if (image.substring(2, image.length() - 6)
								.endsWith("s")) {
							System.out
									.println("skipping download of thumbnail/duplicate");
						} else {
							System.out.println("extracting jpg1/jpg2..... "
									+ image.substring(2));
							download(
									"http://"
											+ image.substring(2,
													image.length() - 2),
									image.substring(14, image.length() - 6));
						}
					} else {
						System.out.println("extracting..... "
								+ image.substring(2));
						download("http://" + image.substring(2),
								image.substring(14));
					}
				}
			}
		} catch (IOException e) {
			System.out.println("extract fail");
			e.printStackTrace();
		}

	}

	public Elements getSubreddit() {
		Document doc;
		Elements description = null;
		try {
			doc = Jsoup.connect("http://www.reddit.com/r/pics.xml").get();
			description = doc.getElementsByTag("description");

		} catch (IOException e) {
			System.out.println("get sr fail");
			e.printStackTrace();
		}
		return description;
	}

	public void getNextPage() {
		System.out.println("Crawling next page..............");
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			Elements next = doc.getElementsByTag("span");
			for (Element n : next) {
				if (n.className().equals("nextprev")) {
					Pattern pattern = Pattern.compile("after=\\w+");
					Matcher matcher = pattern.matcher(n.toString());
					if (matcher.find()) {
						count += 25;
						after = (matcher.group().substring(6));
						url = String
								.format("http://www.reddit.com/r/pics/?count=%d&after=%s",
										count, after);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}

	}
}