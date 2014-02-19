package org.mdavi.minigame.session;

public class Session
{
  protected static final int MAX_SESSION_VALIDITY = 1000*60*10;
  
  private final int userid;
  private long validUntil;
  private final Provider timeProvider;

  public Session (final int userid, final Provider timeProvider)
  {
    this.userid = userid;
    this.timeProvider = timeProvider;
    this.validUntil = this.timeProvider.now() + MAX_SESSION_VALIDITY;
  }
  
  public int getUserid ()
  {
    return userid;
  }
  
  public boolean isValid() {
    return validUntil > timeProvider.now();
  }

  public void refresh ()
  {
    this.validUntil = timeProvider.now() + MAX_SESSION_VALIDITY;
  }
}
