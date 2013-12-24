import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
	private String url;
	private int count;
	private String after;// link to next page
	private static String sr;// subreddit

	public Scraper(String sr) {
		url = String.format("http://www.reddit.com/r/%s.xml?limit=100", sr);
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		sr = input.next();
		Scraper scraper = new Scraper(sr);
		input.close();
		int i = 0;
		while (i < 5) {
			scraper.getImgur();
			scraper.getImgurA();
			scraper.getImgurAddI();
			scraper.getNextPage();
			i++;
		}

	}

	public Elements getSubreddit() {
		/*
		 * load website and scrape the description tag and return to caller
		 */
		Document doc;
		Elements description = null;
		try {
			doc = Jsoup
					.connect(url)
					.userAgent(
							"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
					.referrer("http://www.google.com").get();
			description = doc.getElementsByTag("description");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return description;
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
					Thread t = new Thread(new Download(
							(matcher.group() + ".jpg"), matcher.group()
									.substring(18)));
					t.start();

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
		 * get url to image
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
						Thread t = new Thread(new Download(newUrl + ".jpg",
								newUrl.substring(18)));
						t.start();

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
		} catch (Exception e) {
			e.printStackTrace();
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
							Thread t = new Thread(new Download("http://"
									+ image.substring(2, image.length() - 2),
									image.substring(14, image.length() - 6)));
						}
					} else {
						System.out.println("extracting..... "
								+ image.substring(2));
						Thread t = new Thread(new Download("http://"
								+ image.substring(2), image.substring(14)));

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void getNextPage() {
		/*
		 * load current url, grab the nextpage url, set as new url
		 */
		System.out.println("Crawling next page..............");
		Document doc;
		try {
			url = url.replace(".xml", "");
			doc = Jsoup.connect(url).get();
			Elements next = doc.getElementsByTag("span");
			for (Element n : next) {
				if (n.className().equals("nextprev")) {
					Pattern pattern = Pattern.compile("after=\\w+");
					Matcher matcher = pattern.matcher(n.toString());
					if (matcher.find()) {
						after = matcher.group().substring(6);
						count += 100;
						url = String
								.format("http://www.reddit.com/r/%s.xml?limit=100&count=%d&after=%s",
										sr, count, after);
						System.out.println("Crawling page.........: " + url);

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}