package org.mdavi.minigame.session;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.junit.Test;
import org.mdavi.minigame.TestContext;
import org.mdavi.minigame.session.Provider;
import org.mdavi.minigame.session.SingletonSessionHolder;

public class SingletonSessionHolderTest extends TestContext
{
  private SingletonSessionHolder holder = SingletonSessionHolder.getInstance();
  private Provider timeProvider = context.mock(Provider.class);

  @Test
  public void givenAUserIdAndASessionKey_generatesAValidSession ()
  {
    context.checking(new Expectations()
    {
      {
        allowing(timeProvider).now(); will(returnValue(System.currentTimeMillis()));
      }
    });
    
    int userid = 0;
    String sessionKey = "sessionkey";
    holder.createSession(userid, sessionKey);
    assertThat(holder.isValidSession(sessionKey), is(Boolean.TRUE));
  }
  
  @Test
  public void givenASession_canRetrieveTheUserAssociated ()
  {
    context.checking(new Expectations()
    {
      {
        allowing(timeProvider).now(); will(returnValue(System.currentTimeMillis()));
      }
    });
    int userid = 0;
    String sessionKey = "sessionkey";
    holder.createSession(userid, sessionKey);
    assertThat(holder.getUserFor(sessionKey), is(0));
  }
  
  @Test
  public void canTriggerSessionCleanUp_validSessionsAreKept ()
  {
    context.checking(new Expectations()
    {
      {
        allowing(timeProvider).now(); will(returnValue(System.currentTimeMillis()));
      }
    });
    holder.createSession(0, "sessionkey0");
    holder.createSession(1, "sessionkey1");
    holder.createSession(2, "sessionkey2");
    holder.createSession(3, "sessionkey3");
    holder.createSession(4, "sessionkey4");
    holder.createSession(5, "sessionkey5");
    
    holder.eventuallyCleanUpExpiredSessions(timeProvider);
    assertThat(holder.isValidSession("sessionkey0"), is(Boolean.TRUE));
    assertThat(holder.isValidSession("sessionkey1"), is(Boolean.TRUE));
    assertThat(holder.isValidSession("sessionkey2"), is(Boolean.TRUE));
    assertThat(holder.isValidSession("sessionkey3"), is(Boolean.TRUE));
    assertThat(holder.isValidSession("sessionkey4"), is(Boolean.TRUE));
    assertThat(holder.isValidSession("sessionkey5"), is(Boolean.TRUE));
  }
}
