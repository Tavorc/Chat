package tutorial.chat.server;

public class ClientDescriptor implements StringConsumer, StringProducer
{

    private StringConsumer consumer;
    private String clientNickName;



    public void addConsumer(StringConsumer str)
    {
        if (str != null)
        {
            consumer = str;
        }
    }


    public void removeConsumer(StringConsumer str)
    {
       if (str != null)
       {
           MessageBoard mb = (MessageBoard) consumer;
           mb.removeConsumer(str);
           this.consumer = null;
       }

    }

    public void consume(String str)
    {
        
        System.out.println(this.getClass().getSimpleName() + " Consume");
        if (clientNickName == null)            
        {
            clientNickName = new String(str);
            consumer.consume(str + " Joined the chat");
        }
        else                                   
        {
            consumer.consume(new String(clientNickName + ":" + str));   
        }

    }

    public StringConsumer getConsumer()
    {
        return this.consumer;
    }

}
