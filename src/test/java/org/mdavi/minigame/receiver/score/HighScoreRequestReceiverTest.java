package org.mdavi.minigame.receiver.score;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;
import java.util.TreeSet;

import org.jmock.Expectations;
import org.junit.Test;
import org.mdavi.minigame.TestContext;
import org.mdavi.minigame.receiver.score.HighScoreRequestReceiver;
import org.mdavi.minigame.receiver.score.Score;
import org.mdavi.minigame.receiver.score.ScoreHolder;

public class HighScoreRequestReceiverTest extends TestContext
{

  private int levelid = 1;
  private ScoreHolder scoreHolder = context.mock(ScoreHolder.class);
  private HighScoreRequestReceiver receiver = new HighScoreRequestReceiver(levelid, scoreHolder);
  
  @Test
  public void canGenerateHighScoresResponse_forGivenLevel ()
  {
    context.checking(new Expectations()
    {
      {
        oneOf(scoreHolder).getHighScores(levelid); will(returnValue(buildExpectedMap(10, 30, true)));
      }
    });
    
    String response = receiver.handle();
    
    assertThat(response.length(), is(greaterThan(0)));
  }
  
  @Test
  public void canGenerateUserWithNoScore_whenUserHasNoScore_forGivenLevel ()
  {
    final Set<Score> scores = new TreeSet<>();
    scores.add(new Score(1, 1));
    scores.add(new Score(5, -1));
    scores.add(new Score(10, -1));
    context.checking(new Expectations()
    {
      {
        oneOf(scoreHolder).getHighScores(levelid); will(returnValue(scores));
      }
    });
    
    String response = receiver.handle();
    
    assertThat(response, is("1=1,5=,10="));
  }
  
  @Test
  public void canGenerateEmptyResponse_whenThereAreNoScores_forGivenLevel ()
  {
    context.checking(new Expectations()
    {
      {
        oneOf(scoreHolder).getHighScores(levelid); will(returnValue(buildExpectedMap(10, 30, false)));
      }
    });
    
    String response = receiver.handle();
    
    assertThat(response.length(), is(0));
  }

  private Set<Score> buildExpectedMap (int users, int score, boolean hasHighScore)
  {
    Set<Score> scores = new TreeSet<>();
    for(int u = 0; u < users; u++) {
      if(u % 2 != 0 && hasHighScore)
        scores.add(new Score(u, score+u-1));
      else scores.add(new Score(u, -1));
    }
    return scores;
  }
}
