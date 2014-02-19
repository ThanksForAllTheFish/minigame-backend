package org.mdavi.minigame.receiver.score;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.junit.Test;
import org.mdavi.minigame.TestContext;
import org.mdavi.minigame.exception.UnauthorizedException;
import org.mdavi.minigame.receiver.score.ScoreHolder;
import org.mdavi.minigame.receiver.score.ScoreRequestReceiver;
import org.mdavi.minigame.session.SessionHolder;

public class ScoreRequestReceiverTest extends TestContext
{
  private static final int LEVEL_ID = 0;
  private static final int SCORE = 10;
  private static final int USER_ID = 0;

  private SessionHolder sessionHolder = context.mock(SessionHolder.class);
  private ScoreHolder scoreHolder = context.mock(ScoreHolder.class);
  private ScoreRequestReceiver receiver = new ScoreRequestReceiver(LEVEL_ID, SCORE, "sessionkey", sessionHolder, scoreHolder);

  @Test
  public void givenAValidSession_storesTheScoreForUserAndLevel ()
  {
    context.checking(new Expectations()
    {
      {
        oneOf(sessionHolder).isValidSession("sessionkey"); will(returnValue(Boolean.TRUE));
        oneOf(sessionHolder).getUserFor("sessionkey"); will(returnValue(USER_ID));
        oneOf(scoreHolder).saveScoreFor(USER_ID, LEVEL_ID, SCORE);
      }
    });
    
    String response = receiver.handle();
    
    assertThat(response.length(), is(0));
  }
  
  @Test
  public void givenAnInvalidSession_throwsAnUnauthorizedException ()
  {
    context.checking(new Expectations()
    {
      {
        oneOf(sessionHolder).isValidSession("sessionkey"); will(returnValue(Boolean.FALSE));
      }
    });
    
    expectedException.expect(UnauthorizedException.class);
    
    receiver.handle();
  }

}
