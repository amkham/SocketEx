package client;

import java.io.*;
import java.net.Socket;

public class ClientGetMessageHandler implements Runnable{

    private BufferedReader in; // поток чтения из сокета

    private MessageAddListener messageAddListener;

    private Socket socket;

    public interface MessageAddListener
    {
        void newMessage(String msg);
    }

    public void setMessageAddListener(MessageAddListener messageAddListener) {
        this.messageAddListener = messageAddListener;
    }

    public ClientGetMessageHandler(Socket clientSocket) throws IOException {

        this.socket = clientSocket;


    }

    @Override
    public void run() {

        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = "";
            while (!msg.equals("close"))
            {
                if ((msg = in.readLine())!=null)
                {
                    System.out.println(msg);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
