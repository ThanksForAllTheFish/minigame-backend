package org.mdavi.minigame.receiver.score;

import static org.mdavi.minigame.logger.ConsoleLogger.log;

import java.util.Iterator;
import java.util.Set;

import org.mdavi.minigame.receiver.RequestReceiver;

public class HighScoreRequestReceiver implements RequestReceiver
{

  private final int         levelid;
  private final ScoreHolder scoreHolder;

  public HighScoreRequestReceiver (final int levelid, final ScoreHolder scoreHolder)
  {
    this.levelid = levelid;
    this.scoreHolder = scoreHolder;
  }

  @Override
  public String handle ()
  {
    log("Handling hiscore request for level " + levelid);
    final Set<Score> highScores = scoreHolder.getHighScores(levelid);

    String body = "";
    if(!highScores.isEmpty() && highScores.iterator().next().getScore() != -1) {
      body = composeBodyString(highScores);
    }

    return body;
  }

  private String composeBodyString (final Set<Score> highScores)
  {
    int i = 0;
    
    final Iterator<Score> scores = highScores.iterator();
    StringBuilder body = new StringBuilder();
    while (scores.hasNext() && i < 15)
    {
      body.append( scores.next().printableScore() ).append( "," );
      i++;
    }

    return body.toString().substring(0, body.length()-1);
  }

}
