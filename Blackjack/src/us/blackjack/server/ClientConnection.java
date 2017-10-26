package us.blackjack.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection implements Runnable {

	private Socket socket;

	private BufferedReader input;
	private PrintWriter output;
	private ServerLogic serverThread;

	private String username;
	private boolean loggedIn;

	private boolean running;

	public ClientConnection(Socket socket, ServerLogic serverThread) {
		System.out.println("\nClient connected from " + socket.getInetAddress().getHostAddress());
		this.socket = socket;
		this.serverThread = serverThread;
		running = false;
		loggedIn = false;
	}

	@Override
	public void run() {

		try {

			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());

			running = true;
			while (running) {
				String msg = input.readLine();
				if (msg != null) {
					synchronized (serverThread) {
						serverThread.recieveInformation(msg, this);
					}
				}
			}

		} catch (IOException e) {
			try {
				output.close();
				input.close();
				socket.close();
				loggedIn = false;
			} catch (Exception e2) {
				loggedIn = false;
			}

			e.printStackTrace();
		}

		try {
			output.close();
			input.close();
			socket.close();
		} catch (IOException e) {
			loggedIn = false;
		}
	}

	public void sendToClient(String s) {
		// System.out.println("Sending: " + s);
		output.println(s);
		output.flush();

	}

	public void closeConnection() {
		running = false;
	}

	public void setUsername(String s) {
		username = s;
	}

	public String getUsername() {
		return username;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean b) {
		loggedIn = b;
	}

}
