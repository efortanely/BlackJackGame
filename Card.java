public abstract class Card {
	public enum Suit {
		Hearts, Spades, Clubs, Diamonds
	};

	protected int cardValue;
	protected Suit suit;

	// cardValue is 1 - 13, with 1 being ace, and 11,12,13 being face cards
	public Card(int cardValue, Suit suit) {
		this.cardValue = cardValue;
		this.suit = suit;
	}

	public abstract int getValue();

	public Suit getSuit() {
		return this.suit;
	}

	public boolean isAce() {
		return this.cardValue == 1;
	}
}
