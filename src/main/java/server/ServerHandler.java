package server;

import com.google.gson.Gson;
import model.Point;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerHandler implements Runnable {

    private Socket socket; // сокет, через который сервер общается с клиентом,
    // кроме него - клиент и сервер никак не связаны
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток записи в сокет

    public ServerHandler(@NotNull Socket socket) throws IOException {
        this.socket = socket;

    }
    @Override
    public void run() {



        String word ="";
        System.out.println("Подключился клиент");

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (true) {

                if ((word = in.readLine())!=null)
                {
                    Pattern pattern = Pattern.compile("(--edit--).{10}");
                    Matcher matcher = pattern.matcher(word);

                    if (word.equals("close"))
                    {
                        send("close");
                        System.out.println("Сервер закрыт...");
                        break;
                    }

                    if (word.equals("?"))
                    {
                        System.out.println("Клиент запросил координаты точки");
                        send(new Gson().toJson(ServerData.getPoint()));
                    }

                    if (matcher.find())
                    {
                        StringBuilder stringBuilder = new StringBuilder(word);
                        stringBuilder.delete(0,8);
                        ServerData.setPoint(new Gson().fromJson(stringBuilder.toString(), Point.class));
                    }






                }


            }

        } catch (IOException e) {
        }
    }

    public void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }



}
