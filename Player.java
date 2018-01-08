import java.util.ArrayList;

public class Player<T extends Card> {
	protected ArrayList<T> hand;
	protected int points;
	protected int gamesWon;

	public Player() {
		this.hand = new ArrayList<>();
	}

	public boolean draw(Deck<T> deck, int numCards) {
		boolean successfulDraw = true;
		for (int i = 0; i < numCards; i++) {
			T card = deck.getNextCard();
			if (card == null) {
				successfulDraw = false;
				break;
			}
			this.hand.add(card);
		}
		return successfulDraw;
	}

	public boolean draw(Deck<T> deck) {
		boolean successfulDraw = true;
		while (successfulDraw)
			successfulDraw &= this.draw(deck, 1);
		return successfulDraw;
	}

	public int getHandTotal() {
		int cardTotal = 0;
		for (T card : this.hand)
			cardTotal += card.getValue();
		return cardTotal;
	}

	public void emptyHand() {
		this.hand.clear();
	}

	public void incrementPoints() {
		this.points++;
	}

	public int getPoints() {
		return this.points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void wonGame() {
		this.gamesWon++;
	}

	public int getGamesWon() {
		return this.gamesWon;
	}
}
