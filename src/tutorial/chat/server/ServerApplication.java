package tutorial.chat.server;

//import javax.swing.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerApplication
{
    @SuppressWarnings("resource")
	public static void main(String args[])
    {
        ServerSocket server = null;
        MessageBoard mb = new MessageBoard();
        try
        {
            server = new ServerSocket(1300);
        } catch (IOException e)
        {
        }
        Socket socket = null;
        ClientDescriptor client = null;
        ConnectionProxy connection = null;
        while (true)
        {
            try
            {
                System.out.println("server wait...");
             
                socket = server.accept();
                connection = new ConnectionProxy(socket);   
                client = new ClientDescriptor();             
                connection.addConsumer(client);              
                client.addConsumer(mb);                     
                mb.addConsumer(connection);                 
                connection.start();                         

              
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
