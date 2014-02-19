package org.mdavi.minigame.receiver.score;

import java.util.Set;

public interface ScoreHolder
{

  void saveScoreFor (int userid, int levelid, int score);

  Set<Score> getHighScores (int levelid);

}
