package tutorial.chat.server;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MessageBoard implements StringConsumer, StringProducer
{
	private JTextArea board;
	private JScrollPane scroll;
	private JFrame frame;
	private LinkedList<StringConsumer> consumersList;
	

	public MessageBoard()
	{
		super();
		consumersList = new LinkedList<StringConsumer>();
		board = new JTextArea();
		scroll = new JScrollPane(board);
		
		frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(700,500);
		frame.add(scroll);
		board.setLineWrap(true);
		board.setEditable(false);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
	}


	public void addConsumer(StringConsumer sc)
	{
		consumersList.add(sc);
	}


	public void removeConsumer(StringConsumer sc)
	{
        consumersList.remove(sc);
	}

	public void consume(String str)
	{
       
        System.out.println(this.getClass().getSimpleName() +" Consume");
       
        board.append(str+ "\n");

       
        if (consumersList != null)
        {
            for (StringConsumer ob: consumersList)
            {
                ob.consume(str);        
            }
        }

	}

}
