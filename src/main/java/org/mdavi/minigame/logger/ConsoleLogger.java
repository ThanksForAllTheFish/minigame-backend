package org.mdavi.minigame.logger;


public class ConsoleLogger
{
  
  public static void log(final String message) {
    System.out.println(message);
  }

  public static void log (Throwable ex)
  {
    ex.printStackTrace();
  }
}
