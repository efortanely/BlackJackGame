import java.util.LinkedList;

public class BlackJackDeck extends Deck<BlackJackCard> {
	private int numDecks;

	public BlackJackDeck() {
		this(1);
	}

	public BlackJackDeck(int numDecks) {
		this.numDecks = numDecks;
		this.deck = new LinkedList<>();
		this.initializeDeck();
	}

	public void refresh() {
		this.initializeDeck();
	}

	private void initializeDeck() {
		this.deck.clear();
		for (int i = 1; i <= 13; i++) {
			for (int j = 0; j < this.numDecks; j++) {
				this.deck.add(new BlackJackCard(i, Card.Suit.Hearts));
				this.deck.add(new BlackJackCard(i, Card.Suit.Spades));
				this.deck.add(new BlackJackCard(i, Card.Suit.Clubs));
				this.deck.add(new BlackJackCard(i, Card.Suit.Diamonds));
			}
		}
		this.shuffle();
	}

	@Override
	public int getNext() {
		return this.getNextCard().getValue();
	}
}