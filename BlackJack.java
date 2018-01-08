import java.util.ArrayList;
import java.util.Scanner;

public class BlackJack {
	private BlackJackDeck deck;
	private BlackJackPlayer[] players;
	private Scanner scanner;

	public static void main(String[] s) {
		BlackJack game = new BlackJack(1);
		game.runBlackJackGame();
	}

	public BlackJack(int numDecks) {
		this.scanner = new Scanner(System.in);
		System.out.println("How many players?");
		int numPlayers = this.scanner.nextInt();
		this.scanner.nextLine();
		this.deck = new BlackJackDeck(numDecks);
		this.players = new BlackJackPlayer[numPlayers + 1];
		for (int i = 0; i < this.players.length; i++)
			this.players[i] = new BlackJackPlayer(i,
					i == 0 ? BlackJackPlayer.PlayerType.Dealer : BlackJackPlayer.PlayerType.Player);
	}

	public void runBlackJackGame() {
		System.out.println("\nPress enter after round terminates to continue.");
		boolean ongoingRound = true;
		do {
			while (!this.deck.isEmpty()) {
				if (!this.dealCards())
					break;
				int currentPlayer = 1;
				while (currentPlayer < this.players.length) {
					if (this.players[currentPlayer].getRevealedHandTotal() >= 21) {
						currentPlayer++;
						continue;
					}
					this.printGameStatus();
					System.out.println("Player " + currentPlayer + ", hit? Enter \"Y\"/\"N\"");
					String next = this.scanner.nextLine().toUpperCase();
					if (next.equals("Y")) {
						if (!this.players[currentPlayer].draw(this.deck, 1))
							break;
					} else if (next.equals("N"))
						currentPlayer++;
					else
						System.out.println("Enter a valid choice!");
				}

				this.revealHiddenCards();
				this.players[0].draw(this.deck);
				this.printGameStatus();
				System.out.println(this.updateWinnings());
				this.clearRound();
				if (this.scanner.nextLine().equals("\n"))
					continue;
			}

			this.printFinalScore();
			ongoingRound = this.askForNewRound(ongoingRound);
		} while (ongoingRound);
		System.out.println("Thanks for playing!");
	}

	private boolean dealCards() {
		boolean successfulDraw = true;
		for (BlackJackPlayer player : this.players)
			successfulDraw &= player.draw(this.deck, 2);
		return successfulDraw;
	}

	private void printGameStatus() {
		for (BlackJackPlayer player : this.players)
			System.out.println(player.toString());
	}

	private void revealHiddenCards() {
		for (BlackJackPlayer player : this.players)
			player.revealHiddenCard();
	}

	private String updateWinnings() {
		int dealerHand = this.players[0].getHandTotal();
		boolean dealerHasWon = false;
		StringBuilder updates = new StringBuilder(
				"\n~~~~~~~END OF ROUND UPDATE~~~~~~~\nThe dealer's hand had " + dealerHand + " points.\n");
		for (int i = 1; i < this.players.length; i++) {
			int playerHand = this.players[i].getHandTotal();
			updates.append("Player " + i + "'s hand had " + playerHand + " points.\n");
			if (playerHand == 21 || playerHand < 21 && (playerHand > dealerHand || dealerHand > 21))
				this.players[i].incrementPoints();
			else if (playerHand == dealerHand && playerHand < 21 && dealerHand < 21) {
				this.players[i].incrementPoints();
				if (!dealerHasWon)
					this.players[0].incrementPoints();
				dealerHasWon = true;
			} else if (dealerHand <= 21) {
				if (!dealerHasWon)
					this.players[0].incrementPoints();
				dealerHasWon = true;
			}
		}
		int dealerPoints = this.players[0].getPoints();
		boolean plural = dealerPoints != 1;
		updates.append("\nThe dealer now has " + dealerPoints + " point" + (plural ? "s" : "") + ".\n");
		for (int i = 1; i < this.players.length; i++) {
			int playerPoints = this.players[i].getPoints();
			plural = playerPoints != 1;
			updates.append("Player " + i + " now has " + playerPoints + " point" + (plural ? "s" : "") + ".");
			if (i != this.players.length - 1)
				updates.append("\n");
		}
		updates.append("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		return updates.toString();
	}

	public void clearRound() {
		for (BlackJackPlayer player : this.players)
			player.emptyHand();
	}

	// this is not the most efficient algorithm, but is fairly straightforward to
	// read and works for small amounts of data
	private void printFinalScore() {
		int winningPlayerNum = 0;
		int winningVal = this.players[0].getPoints();
		for (int i = 1; i < this.players.length; i++) {
			BlackJackPlayer player = this.players[i];
			if (player.getPoints() > winningVal) {
				winningPlayerNum = player.getPlayerNumber();
				winningVal = player.getPoints();
			}
		}
		ArrayList<BlackJackPlayer> ties = new ArrayList<>();
		for (int i = 0; i < this.players.length; i++) {
			BlackJackPlayer player = this.players[i];
			if (player.getPoints() == winningVal && i != winningPlayerNum)
				ties.add(player);
		}
		if (!ties.isEmpty()) {
			ties.add(this.players[winningPlayerNum]);
			StringBuilder winners = new StringBuilder("There was a tie... ");
			for (int i = 0; i < ties.size(); i++) {
				this.players[i].wonGame();
				int playerNum = ties.get(i).getPlayerNumber();
				String winner = playerNum == 0 ? "the dealer" : "player " + playerNum;
				if (i == 0)
					winners.append(winner + " won");
				else if (i == ties.size() - 1)
					winners.append(" and " + winner + " won");
				else
					winners.append(", " + winner + " won");
			}
			winners.append(".\nGit gud.");
			System.out.println(winners.toString());
		} else
			this.players[winningPlayerNum].wonGame();
		System.out.println((winningPlayerNum == 0 ? "The dealer won!" : "Player " + winningPlayerNum + " won!") + "\n");

		if (this.players.length > 2) {
			for (int i = 1; i < this.players.length; i++) {
				int gamesWon = this.players[i].getGamesWon();
				boolean plural = gamesWon != 1;
				System.out.println("Player " + i + " has won " + gamesWon + " game" + (plural ? "s" : "") + ".");
			}
		}
	}

	private boolean askForNewRound(boolean ongoingRound) {
		String next = "";
		do {
			System.out.println("Play again? Enter \"Y\"/\"N\"");
			next = this.scanner.nextLine().toUpperCase();
			if (next.equals("Y"))
				this.clearGame();
			else if (next.equals("N"))
				ongoingRound = false;
			else
				System.out.println("Enter a valid choice!");
		} while (!next.equals("Y") && !next.equals("N"));
		return ongoingRound;
	}


	private void clearGame() {
		this.deck.refresh();
		this.clearRound();
		for (BlackJackPlayer player : this.players)
			player.setPoints(0);
	}

}