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
	private String filePath = "c://reddit//";
	private String url;
	private int count;
	private String after;
	private static String subreddit;

	public Scraper(String sr) {
		url = String.format("http://www.reddit.com/r/%s.xml?limit=100", sr);
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("enter subreddit with pics only");
		subreddit = input.next();
		System.out.println("enter amount of pages to crawl(max is 9)");
		int pages = input.nextInt();
		Scraper scraper = new Scraper(subreddit);
		input.close();
		int i = 0;
		long startTime = System.nanoTime();
		while (i < pages) {
			scraper.getNextPage();
			scraper.getImgur();
			scraper.getImgurA();
			scraper.getImgurAddI();
			i++;
		}
		System.out.println(System.nanoTime()-startTime + " time elapsed");

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
			System.out.println("getImgur() failed");
			System.out.println(e);
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
						// make imgur downloadable by adding 'i' before imgur
						String newUrl = matcher.group();
						newUrl = "http://i." + newUrl.substring(7);
						download(newUrl + ".jpg", newUrl.substring(18));
					}
				}
			}

		} catch (Exception e) {
			System.out.println("getImgurAddI() failed");
			System.out.println(e);
		} finally {
			System.out.println("grabbed all imgurs by adding I");
		}

	}

	private void getImgurA() {
		/*
		 * grab all albums then call extract() to get each individual image
		 */
		try {
			System.out.println("connecting to imgur album");
			Elements description = getSubreddit();
			for (Element imgur : description) {
				Pattern pattern = Pattern.compile("http://imgur.com/a/\\w+");
				Matcher matcher = pattern.matcher(imgur.text());
				if (matcher.find()) {
					System.out.println("Downloading image album...." + " "
							+ matcher.group());
					extract(matcher.group());
				}
			}
		} catch (Exception e) {
			System.out.println("getImgurA() failed");
			System.out.println(e);
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
				/*
				 * get all image's inside the data-src attribute, make sure url
				 * is valid first
				 */
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
			System.out.println(e);
			System.out.println("extract() failed");
		}

	}

	public Elements getSubreddit() {
		/*
		 * return an Elements with the information to be scraped
		 *  to caller method, setup user agent
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
			System.out.println("getSubreddit() failed");
			System.out.println(e);
			System.out.println("getSubreddit() url is .........." + url);
		}
		return description;
	}

	public void getNextPage() {
		/*
		 * crawls current url to get next url
		 */
		System.out.println("Crawling next page..............");
		Document doc;
		try {
			System.out.println("getNextPage() url....is...." + url);
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
										subreddit, count, after);
						System.out.println("Crawling page.........: " + url);

					}
				}
			}
		} catch (IOException e) {
			System.out.println("getNextPage() failed");

		}
	}
}