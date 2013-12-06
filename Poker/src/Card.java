public class Card implements Comparable<Card> {

	private Suit suit;
	private Rank rank;

	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	public String toString() {
		return suit + " " + rank;
	}

	public Rank getRank() {
		return rank;
	}

	public Suit getSuit() {
		return suit;
	}

	@Override
	public int hashCode() {
		return rank.hashCode();
	}

	@Override
	public boolean equals(Object cards) {
		Card card = (Card) cards;
		return getRank().equals(card.getRank());
	}

	public int compareTo(Card card) {
		return rank.compareTo(card.rank);
	}

}
