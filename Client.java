package ChatRoomProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {
	String serverAddress = "127.0.0.1"; // Default local IP address for Google Chrome / PCs
	Scanner in = new Scanner(System.in);
	PrintWriter out;
	JFrame frame = new JFrame("Kyle & Jeff's Chat Room");
	JTextField textField = new JTextField(75);
	JTextArea messageArea = new JTextArea(24, 50);
	boolean first = true;
	static Socket socket;
	
	public static void main(String[] args) throws Exception {
		Client c = new Client();
		c.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // Start JFRAME customization
	    c.frame.setSize(800, 450);
	    c.frame.setLocationRelativeTo(null);
	    c.frame.setVisible(true);
	    c.frame.setAlwaysOnTop(true);
	    c.frame.setBackground(Color.RED);
	    c.frame.setFont(new Font("TimesRoman", Font.BOLD, 24));
	    c.frame.setForeground(Color.GREEN);
	    c.frame.setResizable(false);
//	    c.frame.setDefaultLookAndFeelDecorated(true);
	    // End JFRAME customization
	    c.frame.pack();
		c.connect();
		socket.close();
	}

	public Client() {
		textField.setEditable(false);
		messageArea.setEditable(false);
		frame.getContentPane().add(textField, BorderLayout.SOUTH);
		frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);	    
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(textField.getText());
				textField.setText("");
			}
		});
	}

	private void connect() throws IOException {
		try {
			socket = new Socket(serverAddress, 1023);
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);
			while (in.hasNextLine()) {
				String line = in.nextLine();
				if (line.startsWith("NewClient")) {
					out.println(getName());
				} else if (line.startsWith("Accepted")) {
					if (first) {
						JOptionPane.showMessageDialog(frame, "Welcome to the chat room!");
						first = false;
					}
					textField.setEditable(true);
				} else if (line.startsWith("ChatRoom")) {
					messageArea.append(line.substring(8) + "\n");
				}
			}
		} catch (NoSuchElementException e) {
			System.out.println("Client no longer exists within the chat room.");
		} finally {
			JOptionPane.showMessageDialog(frame, "Goodbye!");
			frame.setVisible(false);
			frame.dispose();
		}
	}
	
	public String getName() {
		return JOptionPane.showInputDialog(frame, "Enter name:", "Name", JOptionPane.PLAIN_MESSAGE);
	}
	
	public String getServerAddress() {
		return serverAddress;
	}
	
	public void setServerAddress(String s) {
		serverAddress = s;
	}

	public Scanner getIn() {
		return in;
	}

	public void setIn(Scanner in) {
		this.in = in;
	}

	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JTextArea getMessageArea() {
		return messageArea;
	}

	public void setMessageArea(JTextArea messageArea) {
		this.messageArea = messageArea;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public static Socket getSocket() {
		return socket;
	}

	public static void setSocket(Socket socket) {
		Client.socket = socket;
	}
	
}