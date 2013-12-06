import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Server {
	private Set<Socket> sockets = new HashSet<Socket>();
	private Set<String> names = new HashSet<String>();
	private static int port;
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat date = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");

	public Server(int input) {
		port = input;
	}

	public static void main(String[] args) {
		/*
		 * user defines port number, server initialized
		 */
		System.out.println("enter a port");
		Scanner input = new Scanner(System.in);
		port = input.nextInt();
		new Server(port).go();
		input.close();
	}

	public void go() {
		try {
			/*
			 * wait for connections, add connections to Set, setup streams per
			 * connection in new threads
			 */
			System.out.println("waiting for connetion");
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				Socket sock = serverSocket.accept();
				sockets.add(sock);
				Thread t = new Thread(new ClientHandler(sock));
				t.start();
				System.out.println("connected: " + sock.getInetAddress());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("server setup failed");
		}
	}

	class ClientHandler implements Runnable {
		/*
		 * client handler sets up streams
		 */
		private BufferedReader in;
		private PrintWriter out;
		private Socket sock;
		private String name;

		public ClientHandler(Socket sock) {
			this.sock = sock;
			try {
				in = new BufferedReader(new InputStreamReader(
						sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("stream setup failed");
			}
		}

		@Override
		public void run() {
			/*
			 * First check if user exists, if true than close connection and
			 * warn user about name. Then relay message to all clients, if user
			 * DC's than remove the socket from the Set
			 */
			String message;
			try {
				name = in.readLine();
				if (names.contains(name)) {
					System.out.println("duplicate name detected, removing..");
					out.println("choose new name and reconnect: "
							+ sock.getInetAddress());
					remove(sock);
				} else {
					names.add(name);
					System.out.println("Users active: " + names);
					shout("user: " + name + " connected!" + " from "
							+ sock.getInetAddress());
				}
				while ((message = in.readLine()) != null) {
					/*
					 * call method which checks if user tries to enter a command
					 * such as /laugh or /roll, otherwise relay the message to
					 * all clients
					 */
					swich(message);
				}
			} catch (IOException ex) {
				System.out.println("user disconnected: " + name + " "
						+ sock.getInetAddress());
				shout("user disconnected: " + name + " "
						+ sock.getInetAddress());
				names.remove(name);
				remove(sock);
			}
		}

		public synchronized void shout(String message) {
			// send message to all clients in Set
			for (Socket sock : sockets) {
				try {
					PrintWriter writer = new PrintWriter(
							sock.getOutputStream(), true);
					writer.println(date.format(cal.getTime()) + " " + message
							+ "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

		public void swich(String message) throws IOException {
			// check if user calls a chat command
			// otherwise shout message to all clients
			switch (message) {
			case "/disconnect":
				out.println("disconnected");
				remove(sock);
				break;
			case "/laugh":
				String[] laughs = { "HahHA!", "HAHAAH!!!!", "haaaaa!!!",
						"hohohoohohahhaa!!!", "huehuehue!" };
				shout(name + " " + laughs[(int) (Math.random() * 5)]);
				break;
			case "/roll":
				shout(name + " rolls "
						+ Integer.toString((int) ((Math.random() * 6) + 1)));
				break;
			case "kirby!":
				shout("(>'-')> <('-'<) ^(' - ')^ <('-'<) (>'-')>");
				break;
			default:
				shout(message);
				System.out.println("client says : "
						+ date.format(cal.getTime()) + message);
			}
		}

	}

	public void remove(Socket soc) {
		//close and remove sockets
		try {
			soc.close();
			sockets.remove(soc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
