package server;

import model.Point;

import java.util.LinkedList;

public class ServerData {

    private static Point point ;

    private static Listener listener;



    public interface Listener
     {
         void dataChanged(Point point);
     }

     public static void dataChangeListener(Listener listener)
     {
         ServerData.listener = listener;
     }




    private ServerData(){

    }


    public static synchronized void setPoint(Point point) {
        ServerData.point = point;
        listener.dataChanged(point);

    }

    public static Point getPoint() {

        if (point == null)
        {
            synchronized (ServerData.class)
            {
                if (point == null)
                {
                    point = new Point();
                }
            }
        }

        return point;
    }
}
