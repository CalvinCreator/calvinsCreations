package us.blackjack.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import us.blackjack.util.Constants;
import us.blackjack.util.Utils;

public class BlackjackServer {

	public static void main(String[] args) {

		ServerSocket server = null;

		try {

			server = new ServerSocket(Constants.PORT);

			ServerLogic serverThread = new ServerLogic();
			System.out.println(Utils.getTimeStamp() + ": SERVER STARTED");

			while (true) {
				try {
					Socket connection = server.accept();

					ClientConnection client = new ClientConnection(connection, serverThread);
					Thread t = new Thread(client);
					t.start();

					serverThread.addClient(client);

				} catch (IOException ex) {
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(Utils.getTimeStamp() + ": SERVER CLOSED");
		} finally {
			try {
				if (server != null)
					server.close();
			} catch (IOException ex) {
			}
		}

	}

}
