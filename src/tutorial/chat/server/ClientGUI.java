package tutorial.chat.server;



import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.UnknownHostException;
import java.util.LinkedList;
import javax.swing.*;
import java.net.*;
import java.awt.*;


import tutorial.chat.server.ConnectionProxy;
import tutorial.chat.server.StringConsumer;
import tutorial.chat.server.StringProducer;

public class ClientGUI implements StringConsumer, StringProducer, ActionListener
{
	
	private String clientNickName = "Default";
	private JFrame frame;
	private JPanel topPanel, bottomPanel;
	private JScrollPane scroll;
	private JButton connect, send,nickN;
	private JTextField messageTextField,nickname1;
	private JTextArea mainTextArea;
	
	private StringConsumer consumer = null;
	
	
	
	public ClientGUI()
	{
		frame = new JFrame("Chat Room");
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		mainTextArea = new JTextArea();
		scroll = new JScrollPane(mainTextArea);
		connect = new JButton("connect");
		send = new JButton("send");
		messageTextField = new JTextField(40);
		nickN = new JButton("Enter nickname");
		nickname1= new JTextField();
		
	}
	
	public void go()
	{
		frame.addWindowListener(new WindowAdapter()
						{
							public void windowClosing (WindowEvent e)
							{
								
								System.exit(0);
							}
						});
		connect.setBackground(Color.GREEN);
		
		connect.addActionListener(this);
		send.addActionListener(this);
		messageTextField.addActionListener(this);
		nickN.addActionListener(this);
		nickname1.addActionListener(this);
		
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		frame.setSize(700,500);
		
		
		frame.add(BorderLayout.NORTH, topPanel);
		topPanel.setLayout(new GridLayout(1,2));
		
		topPanel.add(connect);
		topPanel.add(nickN);
		topPanel.add(nickname1);
		topPanel.setBackground(Color.GREEN);
		
		
		
		
		frame.add(BorderLayout.SOUTH, bottomPanel);
		bottomPanel.add(messageTextField);
		bottomPanel.add(send);
		
		frame.add(BorderLayout.CENTER, scroll);
		mainTextArea.setLineWrap(true);
		mainTextArea.setEditable(false);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
	}
	public String getClientNickName()
	{
		return clientNickName;
	}

	public void setClientNickName(String clientNickName)
	{
		this.clientNickName = clientNickName;
	}


	

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == connect)
		{
			
			
			ConnectionProxy proxy  = new ConnectionProxy("127.0.0.1" ,1300);
			proxy.addConsumer(this);
			this.addConsumer(proxy);
			consumer.consume(new String (this.getClientNickName()));
			
			proxy.start();
			
			
			System.out.println("CONNECT Button Pressed");
		}
		else if (e.getSource() == send ||e.getSource() == messageTextField )
		{
			// check if the user in connected
			if (consumer == null)
			{
				mainTextArea.append("You must first connect in order to send a message!\n");
				messageTextField.setText("");
				return;
			}
			else
			{
				String newMassage = messageTextField.getText();
				messageTextField.setText("");
				System.out.println(newMassage);
				System.out.println("SEND Button Pressed");
				consumer.consume(newMassage);
			}
			
			
		}
		else if(e.getSource()==nickN ||e.getSource() == nickname1 )
		{
			setClientNickName(nickname1.getText());
			nickname1.setText("");
			
		}
	
	}
	
	

	public void addConsumer(StringConsumer sc)
	{
		if (sc != null)
		{
			this.consumer = sc;
		}

	}

	
	public void removeConsumer(StringConsumer sc)
	{
		
		// TODO Auto-generated method stub

	}

	
	public void consume(String str)
	{
		if (str != null)
		{
			mainTextArea.append("\n");
			mainTextArea.append(str);
		}
	}

	
	
	public static void main(String[] args)
	{
	
        
		if (args.length < 1)
         {
             System.out.println("Not enough arguments... Please enter your nick name");
             System.exit(1);
         }
         
       
        SwingUtilities.invokeLater(new Runnable()
        {
        
            public void run()
            {
                ClientGUI demo = new ClientGUI();
               
                demo.go(); }
        });
 	}



}
