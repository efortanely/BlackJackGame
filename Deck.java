import java.util.Collections;
import java.util.LinkedList;

public class Deck<T extends Card> {
	protected LinkedList<T> deck;

	protected void shuffle() {
		Collections.shuffle(this.deck);
	}

	public int getNext() {
		return this.getNextCard().getValue();
	}

	public T getNextCard() {
		return this.deck.pollFirst();
	}

	public boolean isEmpty() {
		return this.deck.isEmpty();
	}

	protected LinkedList<T> getAllCards() {
		return this.deck;
	}
}