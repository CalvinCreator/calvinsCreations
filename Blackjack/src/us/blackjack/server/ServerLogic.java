package us.blackjack.server;

import java.util.ArrayList;

import us.blackjack.game.Dealer;
import us.blackjack.game.Player;
import us.blackjack.util.Utils;

public class ServerLogic {

	private ArrayList<ClientConnection> clients;
	private ArrayList<Player> players;
	private int currPlayer;
	private boolean hasGameStarted;
	private Dealer dealer;
	private ArrayList<String> restarters;

	public ServerLogic() {
		clients = new ArrayList<ClientConnection>();

		players = new ArrayList<Player>();
		currPlayer = 0;
		dealer = new Dealer();
		hasGameStarted = false;
		restarters = new ArrayList<String>();
	}

	public void recieveInformation(String message, ClientConnection sender) {

		removeFalseClients();
		if (message != null) {
			String command = message.substring(0, message.indexOf("::"));
			switch (command) {
			case "/login":
				login(message, sender);
				break;
			case "/exit":
				clients.remove(sender);
				if (sender.isLoggedIn())
					System.out.println(Utils.getTimeStamp() + ": " + sender.getUsername() + " exited.");
				sender.closeConnection();
				break;
			case "/logout":
				clients.remove(sender);
				removePlayer(sender.getUsername());
				sendGameToClients();
				System.out.println(Utils.getTimeStamp() + ": " + sender.getUsername() + " logged out.");
				sender.closeConnection();
				if (players.size() < 2) {
					hasGameStarted = false;
					currPlayer = 0;
					dealer.newHand();
				}
				for (ClientConnection c : clients)
					c.sendToClient("/wait::");
				break;
			case "/join":
				System.out.println(Utils.getTimeStamp() + ": " + sender.getUsername() + " is joining the game.");
				Player p = new Player(sender.getUsername());
				dealer.deal(p);
				players.add(p);
				sendGameToClients();
				if (players.size() > 1 && !hasGameStarted) {
					sendToClientByUsername(players.get(currPlayer).getUsername(), "/start::");
					hasGameStarted = true;
				}
				sender.sendToClient("/wait::");
				break;
			case "/hit":
				hit(sender.getUsername());
				if (hasBroken(sender.getUsername())) {
					boolean hasAce = false;
					Player pl = null;
					for (Player p2 : players)
						if (p2.getUsername().equals(sender.getUsername()))
							pl = p2;
					for (Integer i : pl.getHand())
						if (i == 0 || i == 1)
							hasAce = true;
					if (hasAce) {
						for (int i = 0; i < pl.getHand().size(); i++) {
							int sum = getSum(pl.getHand());
							if (sum > 21 && pl.getHand().get(i) == 1) {
								pl.getHand().remove(i);
								pl.getHand().add(i, new Integer(0));
							}
						}
					}
				}
				System.out.println(Utils.getTimeStamp() + ": " + sender.getUsername() + " has hit.");
				sendGameToClients();
				if (hasBroken(sender.getUsername())) {
					sender.sendToClient("/wait::");
					currPlayer++;
					for (ClientConnection c : clients)
						c.sendToClient("/nextPlayer::");
					if (currPlayer < players.size())
						sendToClientByUsername(players.get(currPlayer).getUsername(), "/start::");
					else {
						for (ClientConnection c : clients)
							c.sendToClient("/startDealer::");
						playDealer();
						String winner = getWinner();
						for (ClientConnection c : clients)
							c.sendToClient("/endDealer::" + winner + "::");
					}
				}
				sendGameToClients();
				break;
			case "/stand":
				sender.sendToClient("/wait::");
				currPlayer++;
				for (ClientConnection c : clients)
					c.sendToClient("/nextPlayer::");
				if (currPlayer < players.size())
					sendToClientByUsername(players.get(currPlayer).getUsername(), "/start::");
				else {
					for (ClientConnection c : clients)
						c.sendToClient("/startDealer::");
					playDealer();
					String winner = getWinner();
					for (ClientConnection c : clients)
						c.sendToClient("/endDealer::" + winner + "::");
				}
				break;
			case "/bet":
				break;
			case "/updateHand":
				if (message.substring("/updateHand::".length()).indexOf("::") != -1) {
					String username = message.substring(message.indexOf("[") + 1, message.indexOf(" "));
					String hand = message.substring(message.indexOf(" ") + 1, message.indexOf("]") - 1);
					String[] nums = hand.split(",");
					Player pl = null;
					for (Player p2 : players)
						if (p2.getUsername().equals(username))
							pl = p2;
					ArrayList<Integer> newHand = new ArrayList<Integer>();
					for (String s : nums) {
						newHand.add(Integer.valueOf(s));
					}
					pl.setHand(newHand);
					if (hasBroken(sender.getUsername())) {
						sender.sendToClient("/wait::");
						currPlayer++;
						for (ClientConnection c : clients)
							c.sendToClient("/nextPlayer::");
						if (currPlayer < players.size())
							sendToClientByUsername(players.get(currPlayer).getUsername(), "/start::");
						else {
							for (ClientConnection c : clients)
								c.sendToClient("/startDealer::");
							playDealer();
							String winner = getWinner();
							for (ClientConnection c : clients)
								c.sendToClient("/endDealer::" + winner + "::");
						}
					}
					sendGameToClients();
				}
				break;
			case "/newGame":
				if (message.substring("/newGame::".length()).indexOf("::") != -1) {
					String username = message.substring(message.indexOf("::") + 2, message.lastIndexOf("::"));
					if (!restarters.contains(username))
						restarters.add(username);
					if (restarters.size() == players.size()) {
						hasGameStarted = false;
						currPlayer = 0;
						dealer.newHand();
						for (ClientConnection c : clients)
							c.sendToClient("/newGame::");
						for (Player pl : players) {
							pl.newHand();
							dealer.deal(pl);
						}
						sendGameToClients();
						sendToClientByUsername(players.get(currPlayer).getUsername(), "/start::");
						restarters.removeAll(restarters);
					}
				}
				break;
			}
		}
	}

	private String getWinner() {
		String winner = "";
		Player topPlayer = null;
		int topPlayerSum = 0;
		for (int i = 0; i < players.size(); i++) {
			int sum = getSum(players.get(i).getHand());
			if (sum <= 21 && sum > topPlayerSum) {
				topPlayerSum = sum;
				topPlayer = players.get(i);
				winner = players.get(i).getUsername();
			} else if (sum <= 21 && sum == topPlayerSum) {
				topPlayerSum = sum;
				topPlayer = players.get(i);
				winner += " and " + players.get(i).getUsername();
			}
		}
		int dealerSum = getSum(dealer.getHand());
		if (dealerSum <= 21 && dealerSum > topPlayerSum) {
			return "Dealer";
		}
		if (winner != "")
			return winner;
		return "No one";
	}

	private void playDealer() {
		synchronized (dealer) {
			while (getSum(dealer.getHand()) < 17) {
				long startTime = System.currentTimeMillis();
				while ((System.currentTimeMillis() - startTime) / 1000 < 1.5) {
				}

				dealer.hit();
				boolean hasAce = false;
				for (Integer i : dealer.getHand())
					if (i == 0 || i == 1)
						hasAce = true;

				if (hasAce) {
					for (int i = 0; i < dealer.getHand().size(); i++) {
						if (dealer.getHand().get(i) == 0) {
							dealer.getHand().remove(i);
							dealer.getHand().add(i, new Integer(1));
						}
						if (!(getSum(dealer.getHand()) > 16 && getSum(dealer.getHand()) < 22)
								&& dealer.getHand().get(i) == 1) {
							dealer.getHand().remove(i);
							dealer.getHand().add(i, new Integer(0));
						}
					}
				}

				sendGameToClients();
			}
		}
	}

	private boolean hasBroken(String username) {
		int sum = 0;
		Player p = null;
		for (Player pl : players)
			if (pl.getUsername().equals(username))
				p = pl;
		for (Integer i : p.getHand())
			if (i == 0)
				sum += 1;
			else if (i == 1)
				sum += 11;
			else if (i == 11 || i == 12 || i == 13)
				sum += 10;
			else
				sum += i;

		return sum > 21;
	}

	private int getSum(ArrayList<Integer> hand) {
		int sum = 0;
		for (Integer i : hand)
			if (i == 0)
				sum += 1;
			else if (i == 1)
				sum += 11;
			else if (i == 11 || i == 12 || i == 13)
				sum += 10;
			else
				sum += i;
		return sum;
	}

	private void sendToClientByUsername(String username, String message) {
		for (ClientConnection client : clients)
			if (client.getUsername().equals(username))
				client.sendToClient(message);
	}

	public void addClient(ClientConnection client) {
		clients.add(client);
	}

	public void hit(String username) {
		Player p = null;
		for (Player p2 : players)
			if (p2.getUsername().equals(username))
				p = p2;

		dealer.hit(p);

	}

	public void sendGameToClients() {

		// tell client that you are sending the game state
		for (ClientConnection c : clients)
			c.sendToClient("/sendingGame::");

		// build string to send
		String msg = "";
		for (Player p : players) {
			msg += "[" + p.getUsername() + " ";
			for (Integer i : p.getHand())
				msg += String.valueOf(i) + ",";
			msg.substring(0, msg.length() - 1);
			msg += "]\n";
		}

		msg += "?!"; // start dealer
		for (Integer i : dealer.getHand()) {
			msg += String.valueOf(i) + ",";
		}

		msg = msg.substring(0, msg.length() - 1);
		for (ClientConnection c : clients) {
			c.sendToClient(msg + "::");
		}
	}

	private void removePlayer(String username) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getUsername().equals(username)) {
				players.remove(i);
				return;
			}
		}
	}

	private void login(String msg, ClientConnection client) {
		try {
			msg = msg.substring(msg.indexOf("::") + 2);
			String[] login = msg.split(",");
			String[][] accounts = Utils.getAccounts();
			boolean b = false;
			for (String s[] : accounts) {
				if (s[0].equals(login[0]) && s[1].equals(login[1]))
					b = true;
			}
			try {
				for (ClientConnection c : clients)
					if (c.getUsername().equals(login[0]))
						b = false;
			} catch (Exception e) {
			}

			if (b) {
				client.setUsername(login[0]);
				System.out.println(Utils.getTimeStamp() + ": " + login[0] + " logged in.");
				client.sendToClient("true");
				client.setLoggedIn(true);
			} else {
				client.sendToClient("false");
				client.setLoggedIn(false);
				clients.remove(client);
				client.closeConnection();
				System.out.println(Utils.getTimeStamp() + ": " + login[0] + " tried to loggin.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// pre: each client has a unique username
	private void removeFalseClients() {
		ClientConnection[] clients = new ClientConnection[this.clients.size()];
		for (ClientConnection c : clients) {
			if (c != null)
				synchronized (c) {
					if (!c.isLoggedIn()) {
						for (int i = 0; i < players.size(); i++)
							if (players.get(i).getUsername().equals(c.getUsername()))
								players.remove(i--);

						this.clients.remove(c);
					}
				}
		}

	}

}
