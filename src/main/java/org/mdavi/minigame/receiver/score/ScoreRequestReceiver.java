package org.mdavi.minigame.receiver.score;

import static org.mdavi.minigame.logger.ConsoleLogger.log;

import org.mdavi.minigame.exception.UnauthorizedException;
import org.mdavi.minigame.receiver.RequestReceiver;
import org.mdavi.minigame.session.SessionHolder;

public class ScoreRequestReceiver implements RequestReceiver
{
  private final int levelid;
  private final String sessionKey;
  private final SessionHolder sessionHolder;
  private final int score;
  private final ScoreHolder scoreHolder;

  public ScoreRequestReceiver (final int levelid, final int score, final String sessionKey, final SessionHolder sessionHolder, final ScoreHolder scoreHolder)
  {
    this.levelid = levelid;
    this.score = score;
    this.sessionKey = sessionKey;
    this.sessionHolder = sessionHolder;
    this.scoreHolder = scoreHolder;
  }

  @Override
  public String handle ()
  {
    log("Handling score request for session " + sessionKey + " level " + levelid + " and score " + score);
    if(!sessionHolder.isValidSession(sessionKey)) throw new UnauthorizedException();
    final int userid = sessionHolder.getUserFor(sessionKey);
    scoreHolder.saveScoreFor(userid, levelid, score);
    return "";
  }

}
