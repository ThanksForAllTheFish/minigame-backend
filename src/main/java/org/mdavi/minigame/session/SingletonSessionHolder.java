package org.mdavi.minigame.session;

import static org.mdavi.minigame.logger.ConsoleLogger.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see http://en.wikipedia.org/wiki/Singleton_pattern#The_solution_of_Bill_Pugh
 */
public class SingletonSessionHolder implements SessionHolder
{
  protected static final long SESSION_CLEANUP_INTERVAL = 1000*60*20;
  
  private final Map<String, Session> availableSession;
  private long lastSessionCleanUp;
  private final Provider timeProvider;

  private SingletonSessionHolder ()
  {
    availableSession = new ConcurrentHashMap<>();
    timeProvider = new Provider()
    {
      @Override
      public long now ()
      {
        return System.currentTimeMillis();
      }
    };
    lastSessionCleanUp = timeProvider.now();
  }

  private static class SingletonInitializer
  {
    public static SingletonSessionHolder INSTANCE = new SingletonSessionHolder();
  }

  public static SingletonSessionHolder getInstance ()
  {
    return SingletonInitializer.INSTANCE;
  }

  @Override
  public void createSession (final int userid, final String sessionKey)
  {
    log("Create session " + sessionKey + " for user " + userid);
    final Session session = new Session(userid, timeProvider);
    availableSession.put(sessionKey, session);
    eventuallyCleanUpExpiredSessions(timeProvider);
  }

  @Override
  public boolean isValidSession (final String sessionKey)
  {
    log("Validating session " + sessionKey);
    final Session session = getSession(sessionKey);
    final boolean isValid = null != session && session.isValid();
    if (isValid) session.refresh();
    eventuallyCleanUpExpiredSessions(timeProvider);
    return isValid;
  }

  @Override
  public int getUserFor (final String sessionKey)
  {
    log("Getting owner for session " + sessionKey);
    eventuallyCleanUpExpiredSessions(timeProvider);
    return getSession(sessionKey).getUserid();
  }

  private Session getSession (final String sessionKey)
  {
    return availableSession.get(sessionKey);
  }
  
  /**
   * Ugly. protected for testing purposes 
   * @param timeProvider TODO
   */
  protected void eventuallyCleanUpExpiredSessions (final Provider timeProvider)
  {
    final long now = timeProvider.now();
    if(now > lastSessionCleanUp + SESSION_CLEANUP_INTERVAL) {
      log("Checking cleanup @" + now);
      lastSessionCleanUp = now;
      for(final String sessionKey : availableSession.keySet()) {
        final Session session = getSession(sessionKey);
        if(!session.isValid()) availableSession.remove(sessionKey); //How to test this?
      }
    }
  }
}
