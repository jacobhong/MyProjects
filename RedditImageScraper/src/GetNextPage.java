import java.io.IOException;

import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class GetNextPage {

	public static void main(String[] args) {
		new GetNextPage().next();
	}

	private void next() {
		try {
			Document doc = Jsoup.connect("http://www.reddit.com/r/pics.json").get();
			JSONParser parser = new JSONParser();
			System.out.println(parser.parse(doc));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
