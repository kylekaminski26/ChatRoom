import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class ChatroomServer 
{
    private static Set<String> clients = new HashSet<>();
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    

    
	private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception 
    {	
    	
       		System.out.println("Enter Server Name: ");
       			String serverName = new String(sc.nextLine());
       	    
       		
       			System.out.println("Enter Welcome Message: ");
       				String serverMessage = new String(sc.nextLine());
       				
	
    	System.out.println("Chatroom Server '" + serverName + "' has started." + "\r\n" +
    					    "WELCOME MESSAGE: " + serverMessage + "\r\n");
    		       
    	var pool = Executors.newFixedThreadPool(100);
        try (var listener = new ServerSocket(40960)) 
        {
            while (true) 
            {
                pool.execute(new ClientHandler(listener.accept(), serverName, serverMessage));
            }
        }
    }

    private static class ClientHandler implements Runnable 
    {
    	private String serverName;
    	private String serverMessage;
    	
    	
        private String clientName;
        private Socket clientSocket;
        private Scanner clientInput;
        private PrintWriter clientOutput;
        
		private DateFormat dateFormat;
		private DateFormat timeFormat;
		private Date dateTime;

		
        public ClientHandler(Socket socket, String serverName, String serverMessage) 
        {
            this.clientSocket = socket;
            this.serverName = serverName;
            this.serverMessage = serverMessage;
            
			this.dateTime = new Date();
			this.dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			this.timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");      
        }
        
   
        public void run() 
        {
            try 
            {
            	clientInput = new Scanner(clientSocket.getInputStream());
            	clientOutput = new PrintWriter(clientSocket.getOutputStream(), true);
      	
           	 	clientEntry();
           	 	clientSession();
            } 
            
            catch (Exception e) 
            {
                System.out.println(e);
            } 
            
            finally 
            {
            	clientRemoval();
            	
                try 
                {
                    clientSocket.close();
                } 
                
                catch (IOException e) 
                {
                	
                }
            }
        }
        
        
        public void clientEntry()        
        {
             while (true) 
             {
            	 clientOutput.println("SVRNAME" + serverName);
            	 
            	 clientOutput.println("SUBMITNAME ");
            	 clientName = clientInput.nextLine();
                 if (clientName == null) 
                 {
                     return;
                 }
                 synchronized (clients) 
                 {
                     if (!clientName.isBlank() && !clients.contains(clientName)) 
                     {
                    	 
                    	 clients.add(clientName);
                        System.out.println
                         (
                        		 clientName + " has joined on " + dateFormat.format(dateTime) + 
                        		 		" at "+ timeFormat.format(dateTime) + "\r\n" +
                        		 			"Client count: " + clients.size()+ "\r\n"
                         );
                         break;
                     }
                 }
             }
             
             clientOutput.println("NAMEACCEPTED " + serverName + " - " + clientName);
             
             clientOutput.println("WELCOMEMSG " + serverMessage);
             
             for (PrintWriter cWriter : clientWriters) 
             {
            	 cWriter.println("MESSAGE " + dateFormat.format(dateTime) + ", " + timeFormat.format(dateTime) + ":: " 
            			 		+ clientName + " has joined." + "\n" + "Client count: " + clients.size());

             }
             clientWriters.add(clientOutput);
        }
        
        
        public void clientSession()
        {
            while (true) 
            {
                String input = clientInput.nextLine();
                if (input.toLowerCase().startsWith("/exit")) 
                {
                    return;
                }
                  
                
                for (PrintWriter cWriter : clientWriters) 
                {
                	cWriter.println("MESSAGE " + clientName + ": " + input);
                }
            }
        }
       
        
        public void clientRemoval()
        {
            if (clientOutput != null) 
            {
            	clientWriters.remove(clientOutput);
            }
            
            if (clientName != null) 
            {
            	System.out.println
                (
               		 clientName + " has left on " + dateFormat.format(dateTime) + 
               		 		" at "+ timeFormat.format(dateTime)
               		 			
                );
            	
                clients.remove(clientName);
                
                System.out.println("Client count: " + clients.size() + "\r\n");
                
                for (PrintWriter cWriter : clientWriters) 
                {
                	cWriter.println("MESSAGE " + dateFormat.format(dateTime) + ", " + timeFormat.format(dateTime) + ":: " + 
                	clientName + " has left." + "\n" + "Client count: " + clients.size());
                }
            }     
        }
             
    }
}