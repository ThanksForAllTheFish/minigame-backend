package org.mdavi.minigame.receiver.login;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.junit.Test;
import org.mdavi.minigame.TestContext;
import org.mdavi.minigame.receiver.login.LoginRequestReceiver;
import org.mdavi.minigame.session.SessionHolder;

public class LoginRequestReceiverTest extends TestContext
{
  private SessionHolder sessionHolder = context.mock(SessionHolder.class);

  @Test
  public void givenAUserId_itGeneratesAResponseWithTheUserIdAndASessionKey ()
  {
    final int userid = 0;
    context.checking(new Expectations()
    {
      {
        oneOf(sessionHolder).createSession(with(userid), with(any(String.class)));
      }
    });
    
    LoginRequestReceiver receiver = new LoginRequestReceiver(userid, sessionHolder);
    
    String response = receiver.handle();
    assertThat(response.length(), is(greaterThan(0)));
  }
}
