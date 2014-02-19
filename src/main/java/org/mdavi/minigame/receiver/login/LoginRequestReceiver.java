package org.mdavi.minigame.receiver.login;

import static org.mdavi.minigame.logger.ConsoleLogger.log;

import java.util.UUID;

import org.mdavi.minigame.receiver.RequestReceiver;
import org.mdavi.minigame.session.SessionHolder;

public class LoginRequestReceiver implements RequestReceiver
{
  private final int           userid;
  private final SessionHolder holder;

  public LoginRequestReceiver (final int userid, final SessionHolder holder)
  {
    this.userid = userid;
    this.holder = holder;
  }

  @Override
  public String handle ()
  {
    log("Handling login request for user " + userid);
    final String sessionKey = generateSessionKey();
    log(userid + " -> " + sessionKey);
    holder.createSession(userid, sessionKey);
    return sessionKey;
  }

  private String generateSessionKey ()
  {
    return UUID.randomUUID().toString();
  }

}
