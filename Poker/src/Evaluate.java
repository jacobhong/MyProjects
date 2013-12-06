import java.util.Scanner;

public class Evaluate {

	public static void main(String[] args) {
		/*
		 * This program just evaluates a poker hand and you can choose how many
		 * hands to evaluate
		 */

		boolean quit = false;
		Scanner input = new Scanner(System.in);
		while (quit == false) {
			System.out.println("\n" + "how many hands to evaluate, 0 to quit");
			if (input.hasNextInt()) {
				int hands = input.nextInt();
				if (hands == 0) {
					quit = true;
				}
				for (int i = 0; i < hands; i++) {
					Hand hand = new Hand();
					hand.evaluateHand();
					System.out.println(hand.deck);
				}
			} else {
				System.out.println("enter valid string");
				input.next();
			}
		}
		input.close();
	};

}
