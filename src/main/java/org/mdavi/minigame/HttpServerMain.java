package org.mdavi.minigame;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import org.mdavi.minigame.receiver.MiniGameRequestReceiverFactory;

import com.sun.net.httpserver.HttpServer;

@SuppressWarnings(value = {"restriction"})
public class HttpServerMain
{
  private static final int TIME_TO_SLEEP = 100*1000;
  private static final int PORT = 8080;
  private static final String HOSTNAME = "localhost";
  
  public static void main (String args[]) throws Exception
  {
    String hostname = HOSTNAME;
    int port = PORT;
    int timeToSleep = TIME_TO_SLEEP;
    
    if(args.length > 0) {
      List<String> argList = Arrays.asList(args);
      if(argList.contains("-h")) {
        int indexOf = argList.indexOf("-h");
        hostname = argList.get(indexOf + 1);
      }
      if(argList.contains("-t")) {
        int indexOf = argList.indexOf("-t");
        timeToSleep = Integer.valueOf( argList.get(indexOf + 1) ) * 1000;
      }
      if(argList.contains("-p")) {
        int indexOf = argList.indexOf("-p");
        port = Integer.valueOf( argList.get(indexOf + 1) );
      }
    }

    InetSocketAddress socketAddress = new InetSocketAddress(hostname, port);
    HttpServer server = HttpServer.create(socketAddress, 0);
    server.createContext("/", new RequestHandler(new MiniGameRequestReceiverFactory()));
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();
    try
    {
      Thread.sleep(timeToSleep);
    }
    catch (InterruptedException e)
    {
    }
    server.stop(10);
  }
  
  static class Server implements Runnable {

    private HttpServer server;
    private int timeToSleep;

    public Server (HttpServer server, int timeToSleep)
    {
      this.server = server;
      this.timeToSleep = timeToSleep;
    }

    @Override
    public void run ()
    {
      server.start();
      try
      {
        Thread.sleep(timeToSleep);
      }
      catch (InterruptedException e)
      {
      }
      server.stop(10);
    }
    
  }
}
