package androidNetworkAnalyzer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.net.*;

public class Server {

    public static void main(String[] args) {

        int nreq = 1;
        try
        {
            ServerSocket sock = new ServerSocket (9002);
            for (;;)
            {
                Socket newsock = sock.accept();
                System.out.println("Creating thread ...");
                Thread t = new ThreadHandler(newsock,nreq);
                t.start();
            }
        }
        catch (Exception e)
        {
            System.out.println("IO error " + e);
        }
        System.out.println("End!");
    }
}
