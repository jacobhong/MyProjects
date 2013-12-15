import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class Addressbook {
	private JTextField firstName;
	private JTextField lastName;
	private JTextField number;
	private JTextField address;
	private JTextField city;
	private JTextField zipcode;
	private JTextField search;
	private JTabbedPane tabs;
	private JTable table;
	private JPanel editContact;
	private ConnectDB db;

	public void buildGui() {
		JFrame frame = new JFrame("hakob AddressBook");
		tabs = new JTabbedPane();
		JPanel mainPanel = new JPanel();
		mainPanel.add(tabs);
		tabs.addTab("add new contact", addContactTab());
		tabs.addTab("edit contact", editContactTab());
		tabs.addTab("view contacts", viewContactTab());
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.setSize(300, 500);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public JPanel addContactTab() {
		/*
		 * adds a contact to the database
		 */
		JPanel mainPanel = new JPanel();
		JPanel addContact = new JPanel(new GridLayout(7, 2));
		JLabel firstLabel = new JLabel("first name");
		JLabel lastLabel = new JLabel("last name");
		JLabel numLabel = new JLabel("phone number");
		JLabel addressLabel = new JLabel("address");
		JLabel cityLabel = new JLabel("city");
		JLabel zipLabel = new JLabel("zipcode");
		firstName = new JTextField(9);
		lastName = new JTextField(10);
		number = new JTextField(9);
		address = new JTextField(15);
		city = new JTextField(15);
		zipcode = new JTextField(5);
		JButton addButton = new JButton("add contact");
		addButton.addActionListener(new addListener());
		addContact.add(firstLabel);
		addContact.add(firstName);
		addContact.add(lastLabel);
		addContact.add(lastName);
		addContact.add(numLabel);
		addContact.add(number);
		addContact.add(addressLabel);
		addContact.add(address);
		addContact.add(cityLabel);
		addContact.add(city);
		addContact.add(zipLabel);
		addContact.add(zipcode);
		addContact.add(addButton);
		mainPanel.add(addContact);
		return mainPanel;
	}

	public JPanel editContactTab() {
		/*
		 * allow to search for a user and return result in a JOPtionPane message
		 * dialogue
		 */
		editContact = new JPanel();
		search = new JTextField(10);
		JLabel warning = new JLabel("search by last name spelled correctly");
		JButton searchButton = new JButton("search");
		searchButton.addActionListener(new searchListener());
		editContact.add(search);
		editContact.add(searchButton);
		editContact.add(warning);
		return editContact;
	}

	public JPanel viewContactTab() {
		/*
		 * create a table from MyTableModel, allow basic sorting, make table
		 * fill up whole container, update table immediately when changed
		 */
		db = new ConnectDB();
		JPanel viewContacts = new JPanel();
		table = new JTable(new MyTableModel(db.getTableData()));
		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);	
		table.setPreferredScrollableViewportSize(new Dimension(600,300));
		JScrollPane scrollPane = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		viewContacts.add(scrollPane);
		return viewContacts;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Addressbook().buildGui();
			}
		});
	}

	public class searchListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			/*
			 * if user is found, show user in JOptionPane dialogue
			 */
			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					db = new ConnectDB();
					JOptionPane.showMessageDialog(editContact,
							db.search(search.getText()));
					return null;
				}
			}.execute();
		}
	}

	public class addListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			/*
			 * if user enters valid inputs then proceed to add user to database
			 * and update table accordingly
			 */
			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					if (isValidInput()) {
						addUser();
						db = new ConnectDB();
						table.setModel(new MyTableModel(db.getTableData()));
					} else {
						System.out.println("enter valid inputs and try again");
					}
					return null;
				}

				@Override
				protected void done() {
					clearText();
				}
			}.execute();
		}
	}

	public void clearText() {
		firstName.setText("");
		lastName.setText("");
		number.setText("");
		address.setText("");
		city.setText("");
		zipcode.setText("");
	}

	public boolean isValidInput() {
		/*
		 * can only enter integer in number columns, do not accept null values
		 */
		boolean isValid = false;
		if (!firstName.getText().equals("") && !lastName.getText().equals("")
				&& number.getText().matches("[0-9]*")
				&& !address.getText().equals("") && !city.getText().equals("")
				&& zipcode.getText().matches("[0-9]*")) {
			isValid = true;
		}
		return isValid;
	}

	public void addUser() {
		/*
		 * connect to db add user
		 */
		db = new ConnectDB();
		db.add(new Person(firstName.getText(), lastName.getText(), (Long
				.parseLong(number.getText())), address.getText(), city
				.getText(), (Integer.parseInt(zipcode.getText()))));
	}

}
