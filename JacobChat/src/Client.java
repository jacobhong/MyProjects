import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client {

	private JTextArea tArea;
	private JTextField tField;
	private JTextField portText;
	private JTextField hostText;
	private BufferedReader in;
	private Socket sock;
	private static PrintWriter out;
	private static String name;
	private String host;
	private String port;

	public static void main(String[] args) {
		System.out.println("Enter username");
		Scanner input = new Scanner(System.in);
		name = input.nextLine();
		input.close();
		new Client().go();

	}

	public void go() {
		/*
		 * build gui
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("hakobChat");
				JPanel topPanel = new JPanel();
				JLabel portLabel = new JLabel("port");
				JLabel hostLabel = new JLabel("host");
				portText = new JTextField(6);
				hostText = new JTextField(12);
				JButton connect = new JButton("connect");
				connect.addActionListener(new connectListener());
				JButton disconnect = new JButton("disconnect");
				disconnect.addActionListener(new disconnectListener());
				tField = new JTextField(30);
				tField.addActionListener(new sendListener());
				tArea = new JTextArea(30, 50);
				tArea.setEditable(false);
				tArea.setLineWrap(true);
				JScrollPane tScroll = new JScrollPane(tArea,
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				JButton button = new JButton("send");
				button.addActionListener(new sendListener());
				topPanel.add(hostLabel);
				topPanel.add(hostText);
				topPanel.add(portLabel);
				topPanel.add(portText);
				topPanel.add(connect);
				topPanel.add(disconnect);
				frame.setSize(300, 500);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(topPanel, BorderLayout.NORTH);
				frame.getContentPane().add(tScroll, BorderLayout.CENTER);
				frame.getContentPane().add(tField, BorderLayout.SOUTH);
				frame.getContentPane().add(button, BorderLayout.EAST);
				frame.pack();
			}
		});
	}

	public void setupNetwork(String host, int port) {
		/*
		 * setup in and out stream, send user name to server to check if
		 * duplicate, start thread for incoming messages
		 */
		try {
			sock = new Socket(host, port);
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);
			out.println(name);
			showMessage("Connected!");
			showMessage("enter /laugh or /roll  or kirby! for some fun!");
			Thread t = new Thread(new IncomingReader());
			t.start();
		} catch (IOException ex) {
			ex.printStackTrace();
			showMessage("please enter valid network");
		}
	}

	class IncomingReader implements Runnable {
		// receive messages from server
		public void run() {
			try {
				String message = null;
				while ((message = in.readLine()) != null) {
					showMessage(message + "\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
				showMessage("choose new name and reconnect please");
			}
		}
	}

	public synchronized void sendMessage(String message) {
		try {
			switch (message) {
			//check if user enters chat commands
			case "/laugh":
				out.println("/laugh");
				break;
			case "/roll":
				out.println("/roll");
				break;
			case "kirby!":
				out.println("kirby!");
				break;
			default:
				out.println("(" + name + ")" + ": " + message);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail send message");
		}
	}

	public void showMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tArea.append(message + "\n");
				tArea.setCaretPosition(tArea.getDocument().getLength());
				// SetCaretPosition forces autos scroll
			}
		});
	}

	class sendListener implements ActionListener {
		// listens for user to trying to send a message
		public void actionPerformed(ActionEvent ev) {
			sendMessage(tField.getText());
			tField.setText("");
		}
	}

	class disconnectListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				out.println("/disconnect");
			} catch (NullPointerException e) {
				showMessage("not connected");
			}
		}
	}

	class connectListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			host = hostText.getText();
			port = portText.getText();
			if (!host.equals("") && !port.equals("") && port.matches("[1-9]+")) {
				// make sure user enters valid inputs
				// before setting up network
				setupNetwork(host, Integer.parseInt(port));
			} else {
				showMessage("enter valid network credentials");
			}
		}

	}

}
