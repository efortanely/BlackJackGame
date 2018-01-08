public class BlackJackPlayer extends Player<BlackJackCard> {
	private int playerNumber;
	private PlayerType type;

	public enum PlayerType {
		Dealer, Player
	};

	public BlackJackPlayer(int playerNumber, PlayerType type) {
		super();
		this.playerNumber = playerNumber;
		this.type = type;
	}

	public boolean draw(BlackJackDeck deck, int numCards) {
		boolean successfulDraw = true;
		for (int i = 0; i < numCards; i++) {
			BlackJackCard card = deck.getNextCard();
			if (card == null) {
				successfulDraw = false;
				break;
			}
			if (!this.hasHiddenCard() && numCards > 1)
				card.hideCard();
			this.hand.add(card);
		}
		return successfulDraw;
	}

	public boolean draw(BlackJackDeck deck) {
		boolean successfulDraw = true;
		while (successfulDraw && this.getHandTotal() < 17)
			successfulDraw &= this.draw(deck, 1);
		return successfulDraw;
	}

	public boolean hasHiddenCard() {
		return this.hand.size() != 0 && this.hand.get(0).isHidden();
	}

	public void revealHiddenCard() {
		if (this.hasHiddenCard())
			this.hand.get(0).revealCard();
	}

	@Override
	public int getHandTotal() {
		int cardTotal = 0;
		int numAces = 0;
		for (BlackJackCard card : this.hand) {
			if (card.isAce())
				numAces++;
			cardTotal += card.getValue();
		}
		if (cardTotal < 21) {
			while (numAces != 0 && cardTotal + 10 <= 21) {
				cardTotal += 10;
				numAces--;
			}
		}
		return cardTotal;
	}

	public int getRevealedHandTotal() {
		return this.getHandTotal() - this.hand.get(0).getValue();
	}

	public int getPlayerNumber() {
		return this.playerNumber;
	}

	@Override
	public String toString() {
		StringBuilder cards = new StringBuilder(
				(this.type == PlayerType.Dealer ? "The dealer" : "Player " + this.playerNumber) + " has");
		for (int i = 0; i < this.hand.size(); i++) {
			BlackJackCard card = this.hand.get(i);
			if (i == this.hand.size() - 1)
				cards.append(" and " + card.toString() + ".");
			else {
				cards.append(" " + card.toString());
				if (this.hand.size() > 2)
					cards.append(",");
			}
		}
		return cards.toString();
	}
}
