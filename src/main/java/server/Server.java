package server;

import model.Point;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    public static final int PORT = 8080;
    public static LinkedList<ServerHandler> serverList = new LinkedList<>(); // список всех нитей

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        try {
            System.out.println("Server started!");
            while (true) {
                // Блокируется до возникновения нового соединения:

                Socket socket = server.accept();
                try {
                    ServerHandler serverHandler = new ServerHandler(socket);
                    serverList.add(serverHandler);
                    new Thread(serverHandler).start();

                    ServerData.dataChangeListener(point -> {

                        for (ServerHandler s : serverList)
                        {
                            s.send("Координаты точки изменены!");
                        }
                    });
                } catch (IOException e) {
                    // Если завершится неудачей, закрывается сокет,
                    // в противном случае, нить закроет его при завершении работы:
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }

}
