public class BlackJackCard extends Card {
	private int pointsValue;
	private boolean hidden;

	public BlackJackCard(int cardValue, Suit suit) {
		super(cardValue, suit);
		this.hidden = false;
		this.pointsValue = cardValue > 10 ? 10 : cardValue;
	}

	public BlackJackCard(int cardValue, Suit suit, boolean hidden) {
		super(cardValue, suit);
		this.hidden = hidden;
	}

	@Override
	public int getValue() {
		return this.pointsValue;
	}

	public void hideCard() {
		this.hidden = true;
	}

	public void revealCard() {
		this.hidden = false;
	}

	public boolean isHidden() {
		return this.hidden;
	}

	@Override
	public String toString() {
		if (this.hidden)
			return "a hidden card";
		else if (this.cardValue == 1)
			return "an ace";
		else if (this.cardValue == 11)
			return "a jack";
		else if (this.cardValue == 12)
			return "a queen";
		else if (this.cardValue == 13)
			return "a king";
		else
			return Integer.toString(this.cardValue);
	}
}
