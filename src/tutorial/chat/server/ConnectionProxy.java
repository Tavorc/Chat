package tutorial.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectionProxy extends Thread implements StringConsumer, StringProducer
{
    private String buffer;
    private String serverName = "127.0.0.1";
    private int portNumber = 1300;
    private Socket socket;
    private DataOutputStream output = null;
    private DataInputStream dataInputStream = null;
    private StringConsumer consumer = null;
    private boolean flagClosed = false;

  
    public ConnectionProxy(String serverName, int portNumber)
    {
        super();
        setServerName(serverName);
        setPortNumber(portNumber);

        try
        {
            socket = new Socket(getServerName(), getPortNumber());


        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    public ConnectionProxy()
    {
        try
        {
            socket = new Socket(getServerName(), getPortNumber());


        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    
    public ConnectionProxy(Socket socket)
    {
        try
        {
            setSocket(socket);
            setPortNumber(socket.getPort());
            InetAddress num = socket.getLocalAddress();
            setServerName(num.toString());
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }


    public void run()
    {
       
        if (dataInputStream == null)                        
        {
            try
            {
               
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

       
        while (socket.isConnected())  // wait for new messages to arrive from the client 
        {
            try
            {
                if ((buffer = dataInputStream.readUTF()) != null)
                {
                   
                    consumer.consume(buffer);     
                }
            } catch (IOException e)
            {
                
                System.out.println("Connection was closed");
                removeConsumer(consumer);
                closeStreams();
                flagClosed = true;
                break;             
            }
        }
    }

    private void closeStreams()
    {
        if (dataInputStream != null)
        {
            try
            {
                dataInputStream.close();
            } catch (IOException e)
            {
                e.printStackTrace();  
            }
        }
        if (output != null)
        {
            try
            {
                output.close();
            } catch (IOException e)
            {
                e.printStackTrace();  
            }
        }
        if (socket != null)
        {
            try
            {
                socket.close();
            } catch (IOException e)
            {
                e.printStackTrace(); 
            }
        }
    }


    public Socket getSocket()
    {
        return socket;
    }

    public void setSocket(Socket socket) throws UnknownHostException
    {
        if (socket != null)
        {
            this.socket = socket;
        } else throw new UnknownHostException("recived NULL Socket");
    }

    public String getServerName()
    {
        return serverName;
    }

    public void setServerName(String serverName)
    {
        if (serverName != null)
            this.serverName = serverName;
        else throw new NullPointerException("serverName is NULL!");
    }

    public int getPortNumber()
    {
        return portNumber;
    }

    public void setPortNumber(int portNumber)
    {
        if (portNumber > 1024)
            this.portNumber = portNumber;
        else throw new IllegalArgumentException("Port number is illegal");
    }


    public void addConsumer(StringConsumer str)
    {
        if (str != null && consumer == null)
        {
            consumer = str;
        }

    }


    public void removeConsumer(StringConsumer str)
    {
        if (str != null)
        {
            ClientDescriptor clientDescriptor = (ClientDescriptor) str;
            clientDescriptor.removeConsumer(this);              
            this.consumer = null;
        }
    }

    public void consume(String str)
    {
        
        
        if (!flagClosed)
        {
            if (output == null)
            {
                try
                {
                    output = new DataOutputStream(socket.getOutputStream());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
         

            try
            {
                output.writeUTF(str);
            } catch (IOException e)
            {
               
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("It was closed ");
        }

    }


}
