package ChatRoomClient;

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
	JFrame frame = new JFrame("Jeff & Kyle's Chat Room");
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

	private String getName() {
		return JOptionPane.showInputDialog(frame, "Enter name:", "Name", JOptionPane.PLAIN_MESSAGE);
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
}