package client;

import com.google.gson.Gson;
import model.Point;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class Client {

private static Socket clientSocket;
    private static BufferedReader reader; // нам нужен ридер читающий с консоли, иначе как
    // мы узнаем что хочет сказать клиент?
    private static BufferedWriter out; // поток записи в сокет


    public static void main(String[] args) throws IOException {

        clientSocket = new Socket("localhost", 8080);

        ClientGetMessageHandler clientHandler = new ClientGetMessageHandler(clientSocket);

        new Thread(clientHandler).start();

            try {
                reader = new BufferedReader(new InputStreamReader(System.in));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String clientMsg = "";

                System.out.println("Клиент готов!");

                while (!clientMsg.equals("close"))
                {

                    if(((clientMsg = reader.readLine()) != null))
                    {
                        if (clientMsg.equals("send"))
                        {
                            System.out.println("Введите X: ");
                            int x = Integer.parseInt(reader.readLine());
                            System.out.println("Введите Y: " );
                            int y = Integer.parseInt(reader.readLine());
                            Point point = new Point(x,y);

                            String msg = "--edit--" + new Gson().toJson(point) + "\n";
                            System.out.println("Отправляем...");
                            out.write(msg);
                            out.flush();
                        }
                        if (clientMsg.equals("?"))
                        {
                            System.out.println("Запрос актуальных координат...");
                            out.write(clientMsg + "\n"); // отправляем сообщение на сервер
                            out.flush();

                        }

                        if (clientMsg.equals("close"))
                        {

                            out.write(clientMsg + "\n"); // отправляем сообщение на сервер
                            out.flush();
                            System.out.println("Клиент был закрыт...");
                            clientSocket.close();
                            out.close();
                            break;
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }








    }



}
