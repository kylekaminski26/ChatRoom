import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatroomClient 
{
	String serverAddress;
    Scanner serverInput;
    PrintWriter serverOutput;
    JFrame frame = new JFrame("CHATROOM");
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16, 50);

    public ChatroomClient(String serverAddress) 
    {
        this.serverAddress = serverAddress;

        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
	    frame.setSize(800, 450);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	    frame.setAlwaysOnTop(true);
	    frame.setBackground(Color.RED);
	    frame.setFont(new Font("TimesRoman", Font.BOLD, 24));
	    frame.setForeground(Color.GREEN);
	    frame.setResizable(false);
        frame.pack();

        textField.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
            	serverOutput.println(textField.getText());
                textField.setText("");
            }
        });
    
    }
    
    public static void main(String[] args) throws Exception 
    {
    	String ip = "127.0.0.1";
        
        var client = new ChatroomClient(ip);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
    
    
    private void run() throws IOException 
    {
        try 
        {
            Socket socket = new Socket(serverAddress, 40960);
            serverInput = new Scanner(socket.getInputStream());
            serverOutput = new PrintWriter(socket.getOutputStream(), true);

            while (serverInput.hasNextLine()) 
            {
                var line = serverInput.nextLine();
               
                if (line.startsWith("SVRNAME")) 
                {
                	this.frame.setTitle(line.substring(8));
                }
                
                else if (line.startsWith("SUBMITNAME")) 
                {
                	serverOutput.println(getName());    
                } 
                 
                else if (line.startsWith("NAMEACCEPTED")) 
                {
                	this.frame.setTitle(line.substring(13));
                    textField.setEditable(true);
                }               
                
                else if (line.startsWith("WELCOMEMSG"))
                {
                	messageArea.append("[SERVER BROADCAST] :: " + line.substring(11) + "\n");
                }
                
                else if (line.startsWith("MESSAGE")) 
                
                {
                    messageArea.append(line.substring(8) + "\n");
                }
            }
        } 
        
        catch (NoSuchElementException e) 
        {
			System.out.println("Client no longer exists within the chat room.");
		}
        finally 
        {
        	JOptionPane.showMessageDialog(frame, "Goodbye!");
            frame.setVisible(false);
            frame.dispose();
        }
    }
    
    
    private String getName() 
    {
        return JOptionPane.showInputDialog(frame, "Pick a username:", "Username",
                JOptionPane.PLAIN_MESSAGE);
    }      
}
