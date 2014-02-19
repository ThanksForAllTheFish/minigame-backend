package org.mdavi.minigame.session;

public interface SessionHolder
{

  void createSession (int userid, String sessionKey);

  boolean isValidSession (String string);

  int getUserFor (String sessionKey);

}
