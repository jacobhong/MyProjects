import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Hand {
	Card[] hand = new Card[5];
	List<Card> deck = Arrays.asList(hand); // convert to ArrayList to use get()

	Hand() {
		// draw 5 cards and sort
		Deck deck = new Deck();
		for (int i = 0; i < 5; i++) {
			hand[i] = deck.drawCard();
		}
		Collections.sort(this.deck);

	}

	public boolean isHighCard() {
		boolean isHighCard = false;
		if (!isPair() && !isTwoPair() && !isThreeOfAKind() && !isFourOfAKind()
				&& !isStraight() && !isStraightFlush() && !isRoyalFlush()
				&& !isFlush()) {
			isHighCard = true;
		}
		return isHighCard;
	}

	public boolean isPair() {
		boolean isPair = false;
		for (int i = 0; i < 5; i++) {
			for (int j = i + 1; j < 5; j++) {
				if (deck.get(i).equals(deck.get(j))) {
					isPair = true;
				}
			}
		}
		return isPair;

	}

	public boolean isTwoPair() {
		boolean isTwoPair = false;
		List<Card> cards = new LinkedList<Card>(deck);
		// need a new ArrayList to remove pairs easily
		for (int cardOne = 0; cardOne < cards.size(); cardOne++) {
			for (int cardTwo = cardOne + 1; cardTwo < cards.size(); cardTwo++) {
				if (deck.get(cardOne).equals(deck.get(cardTwo))) {
					cards.remove(cardOne);
					cards.remove(cardTwo - 1);
					for (int cardThree = 0; cardThree < cards.size(); cardThree++) {
						for (int cardFour = cardThree + 1; cardFour < cards
								.size(); cardFour++) {
							if (cards.get(cardThree)
									.equals(cards.get(cardFour))) {
								isTwoPair = true;
							}
						}
					}
				}
			}

		}
		return isTwoPair;

	}

	public boolean isThreeOfAKind() {
		boolean isThreeOfAKind = false;
		int count = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = i + 1; j < 5; j++) {
				if (deck.get(i).equals(deck.get(j))) {
					count++;
					if (count == 3) {
						isThreeOfAKind = true;
					}
				}
			}
		}
		return isThreeOfAKind;

	}

	public boolean isStraight() {
		boolean isStraight = false;
		int count = 0;
		for (int i = 0; i < 4; i++) {
			if (deck.get(i + 1).getRank().ordinal()
					- deck.get(i).getRank().ordinal() == 1) {
				count++;
				if (count == 4) {
					isStraight = true;
				}
			}
		}
		return isStraight;

	}

	public boolean isFlush() {
		boolean isFlush = false;
		int count = 0;
		for (int i = 0; i < 4; i++) {
			if (deck.get(i).getSuit() == (deck.get(i + 1).getSuit())) {
				count++;
				if (count == 4) {
					isFlush = true;
				}
			}
		}
		return isFlush;

	}

	public boolean isFullHouse() {
		boolean isFullHouse = false;
		List<Card> cards = new LinkedList<Card>(deck);
		for (int cardOne = 0; cardOne < deck.size(); cardOne++) {
			for (int cardTwo = cardOne + 1; cardTwo < deck.size(); cardTwo++) {
				for (int cardThree = cardTwo + 1; cardThree < deck.size(); cardThree++) {
					// if there is a three of a kind remove them and
					// check if the remaining cards are pairs
					if (deck.get(cardOne).equals(deck.get(cardTwo))
							&& deck.get(cardTwo).equals(deck.get(cardThree))) {
						cards.remove(cardOne);
						cards.remove(cardTwo - 1);
						cards.remove(cardThree - 2);
						if (cards.get(0).equals(cards.get(1))) {
							isFullHouse = true;
						}
					}
				}
			}
		}
		return isFullHouse;

	}

	public boolean isFourOfAKind() {
		boolean isFourOfAKind = false;
		for (int cardOne = 0; cardOne < deck.size(); cardOne++) {
			for (int cardTwo = cardOne + 1; cardTwo < deck.size(); cardTwo++) {
				for (int cardThree = cardTwo + 1; cardThree < deck.size(); cardThree++) {
					for (int cardFour = cardThree + 1; cardFour < deck.size(); cardFour++) {
						if (deck.get(cardOne).equals(deck.get(cardTwo))
								&& deck.get(cardTwo)
										.equals(deck.get(cardThree))
								&& deck.get(cardThree).equals(
										deck.get(cardFour))) {
							isFourOfAKind = true;
						}
					}
				}
			}
		}
		return isFourOfAKind;

	}

	public boolean isStraightFlush() {
		boolean isStraightFlush = false;
		int count = 0;
		for (int cardOne = 0; cardOne < 4; cardOne++) {
			if (deck.get(cardOne).getSuit() == (deck.get(cardOne + 1).getSuit())) {
				if (deck.get(cardOne + 1).getRank().ordinal()
						- deck.get(cardOne).getRank().ordinal() == 1)
					count++;
				if (count == 4) {
					isStraightFlush = true;
				}
			}
		}
		return isStraightFlush;

	}

	public boolean isRoyalFlush() {
		boolean isRoyalFlush = false;
		int count = 0;
		Rank[] rank = Rank.values();
		for (int cardOne = 0; cardOne < 4; cardOne++) {
			if (deck.get(cardOne).getSuit() == (deck.get(cardOne + 1).getSuit())) {
				if (deck.get(cardOne).getRank().equals(rank[cardOne + 8])) {
					// start the count at the index for "TEN" instead of "TWO"
					count++;
					if (count == 4) {
						isRoyalFlush = true;
					}

				}
			}
		}
		return isRoyalFlush;

	}

	public void evaluateHand() {
		/*
		 * evaluate which hand you have starting with the highest rank, stop if
		 * it matches
		 */
		while (true) {
			if (isRoyalFlush()) {
				System.out.println("Royal Flush");
				break;
			}
			if (isStraightFlush()) {
				System.out.println("Straight Flush");
				break;
			}
			if (isFourOfAKind()) {
				System.out.println("4 of a kind");
				break;
			}
			if (isFullHouse()) {
				System.out.println("full house");
				break;
			}
			if (isFlush()) {
				System.out.println("Flush");
				break;

			}
			if (isStraight()) {
				System.out.println("Straight");
				break;

			}
			if (isThreeOfAKind()) {
				System.out.println("3 of a kind");
				break;

			}
			if (isTwoPair()) {
				System.out.println("2 pair");
				break;

			}
			if (isPair()) {
				System.out.println("pair");
				break;
			}
			if (isHighCard()) {
				System.out.println("high card is " + deck.get(4));
				break;

			}
		}

	}
}
