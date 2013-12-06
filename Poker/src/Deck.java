import java.util.ArrayList;
import java.util.Collections;

public class Deck {

	ArrayList<Card> cards;

	public Deck() {
		// create a deck of 52 cards with every suit/value combo
		cards = new ArrayList<Card>();
		for (Rank rank : Rank.values()) {
			for (Suit suit : Suit.values()) {
				cards.add(new Card(rank, suit));
			}
		}
		shuffleDeck(cards);
	}

	public ArrayList<Card> shuffleDeck(ArrayList<Card> deck) {
		Collections.shuffle(deck);
		return deck;
	}

	public Card drawCard() {
		return cards.remove(0);
	}

}
