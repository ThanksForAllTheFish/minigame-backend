package org.mdavi.minigame.receiver.score;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.mdavi.minigame.receiver.score.Score;
import org.mdavi.minigame.receiver.score.SingletonScoreHolder;

public class SingletonScoreHolderTest
{
  private int levelid = 0;
  private int userid = 0;
  private int score = 0;

  private SingletonScoreHolder scoreHolder = SingletonScoreHolder.getInstance();

  @Test
  public void canSaveANewLevelScore_forANewUser ()
  {
    scoreHolder.saveScoreFor(userid, levelid, score);
  }
  
  @Test
  public void canSaveANewLevelScore_forExistingUser ()
  {
    scoreHolder.saveScoreFor(userid, levelid, score);
    int newLevel = 10;
    int newScore = 5;
    scoreHolder.saveScoreFor(userid, newLevel, newScore);
  }
  
  @Test
  public void canSaveANewScore_forExistingUserLevel ()
  {
    scoreHolder.saveScoreFor(userid, levelid, score);
    int newScore = 5;
    scoreHolder.saveScoreFor(userid, levelid, newScore);
  }
  
  @Test
  public void canComputeHighScores_forAGivenLevel ()
  {
    int users = 10;
    int scores = 10;
    initializeScores(users, 8, scores);
    
    Set<Score> highScores = scoreHolder.getHighScores(4);
    
    Set<Score> expected = buildExpectedMap(users, scores);
    
    assertThat(highScores, is(expected));
  }
  
  private Set<Score> buildExpectedMap (int users, int score)
  {
    Set<Score> scores = new TreeSet<>();
    for(int u = 0; u < users; u++) {
      if(u % 2 != 0)
        scores.add(new Score(u, score+u-1));
      else scores.add(new Score(u, -1));
    }
    return scores;
  }
  
  private void initializeScores (int users, int levels, int scores)
  {
    for(int u = 0; u < users; u++){
      for(int l = 1; l < levels; l++){
        for(int s = 0; s < scores; s++){
          if(u % 2 != 0)
            scoreHolder.saveScoreFor(u, l, s+u);
          else scoreHolder.saveScoreFor(u, l, -1);
        }
      }
    }
  }
}
