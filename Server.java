package ChatRoomProject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class Server {
	private static Set<String> clients = new HashSet<>();
	private static Set<PrintWriter> chatters = new HashSet<>();
	private static int roomSize = 0;
	final static int PORT = 1023; // Port that will be used for the socket communication.

	public static void main(String[] args) throws Exception {
		System.out.println("The server is currently running on Port " + PORT + "...");
		System.out.println("Current chat room size: " + roomSize);
		ExecutorService room = Executors.newFixedThreadPool(127);
		try (ServerSocket listener = new ServerSocket(PORT)) {
			while (true)
				room.execute(new Handler(listener.accept()));
		}
	}

	private static class Handler implements Runnable {
		private String clientName;
		private String clientIP = "127.0.0.1";
		private Socket socket;
		private PrintWriter out;
		private Scanner sc;

		protected Handler(Socket s) {
			socket = s;
		}

		@Override // Override of the run() method for Handler class. Must be called run.
		public void run() {
			try {
				try {
					sc = new Scanner(socket.getInputStream());
					out = new PrintWriter(socket.getOutputStream(), true);
					while (true) {// Note to Jeff - DO NOT CHANGE A SINGLE THING IN THIS WHILE LOOP! IT CAUSED ME
									// HOURS OF HEADACHES TO FIX!
						out.println("NewClient");
						clientName = sc.nextLine();
						clientName = "<" + clientName.toUpperCase() + ">";
						if (clientName == null)
							return;
						synchronized (clients) {
							if (!clientName.isBlank() && !clients.contains(clientName)) {
								clients.add(clientName);
								roomSize++;
								DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
								DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
								Date date = new Date();
								Date time = new Date();
								System.out.println(clientName + " has joined on " + dateFormat.format(date) + " at "
										+ timeFormat.format(time));
								System.out.println("Currently communicating on port " + PORT + ".");
								System.out.println(clientName + "'s IP Address: " + clientIP + ".");
								System.out.println("Current chat room size: " + roomSize);
								break;
							}
						}
					}
					out.println("Accepted " + clientName);
					for (PrintWriter chatLog : chatters) {
						DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
						DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
						Date date = new Date();
						Date time = new Date();
						chatLog.println("ChatRoom " + clientName + " has joined on " + dateFormat.format(date) + " at "
								+ timeFormat.format(time));
						// roomSize++;
						chatLog.println("Currently communicating on port " + PORT + ".");
						chatLog.println(clientName + "'s IP Address: " + clientIP + ".");
						chatLog.println("ChatRoom " + "Size of chat room: " + roomSize + ".");
					}
					chatters.add(out);
					while (true) {
						String input = sc.nextLine();
						if (input.toLowerCase().startsWith("!quit"))
							return;
						for (PrintWriter writer : chatters) {
							DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
							Date time = new Date();
							writer.println("ChatRoom " + timeFormat.format(time) + " " + clientName + " " + input);
						}
					}
				} catch (IOException e) {
					System.out.println(e);
				} finally {
					if (out != null)
						chatters.remove(out);
					if (clientName != null) {
						DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
						DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
						Date date = new Date();
						Date time = new Date();
						System.out.println(clientName + " has left the chat room on " + dateFormat.format(date) + " at "
								+ timeFormat.format(time));
						clients.remove(clientName);
						roomSize--;
						dateFormat = new SimpleDateFormat("MM/dd/yyyy");
						timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
						date = new Date();
						time = new Date();
						for (PrintWriter writer : chatters) {
							writer.println("ChatRoom " + clientName + " has left the chat room on "
									+ dateFormat.format(date) + " at " + timeFormat.format(time));
							System.out.println("Current chat room size: " + roomSize);
						}
					}
					try {
						socket.close();
					} catch (IOException e) {
						// Purposefully empty. Exception handling needed to close the socket to prevent
						// memory leak. Also prevents non-fatal errors from printing in the terminal.
					}
				}
			} catch (NoSuchElementException e) {
				// Purposefully empty. Does nothing, just simply suppresses the non-fatal errors
				// that print in the console whenever a user leaves the chat room (Server still
				// runs when there are errors).
			}
		}
	}

	public int getRoomSize() {
		return roomSize;
	}
}
