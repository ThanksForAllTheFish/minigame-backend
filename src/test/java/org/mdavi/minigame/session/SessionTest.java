package org.mdavi.minigame.session;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.junit.Test;
import org.mdavi.minigame.TestContext;
import org.mdavi.minigame.session.Provider;
import org.mdavi.minigame.session.Session;

public class SessionTest extends TestContext
{
  private Provider provider = context.mock(Provider.class);

  @Test
  public void canCheckValidSessions ()
  {
    context.checking(new Expectations()
    {
      {
        allowing(provider).now(); will(returnValue(System.currentTimeMillis()));
      }
    });
    
    
    Session session = new Session(0, provider);
    assertThat(session.isValid(), is(Boolean.TRUE));
  }

  @Test
  public void canCheckInvalidSessions ()
  {
    context.checking(new Expectations()
    {
      {
        oneOf(provider).now(); will(returnValue(System.currentTimeMillis() - Session.MAX_SESSION_VALIDITY - 10));
        oneOf(provider).now(); will(returnValue(System.currentTimeMillis()));
      }
    });
    
    Session session = new Session(0, provider);
    assertThat(session.isValid(), is(Boolean.FALSE));
  }
  
  @Test
  public void canGetRefreshed ()
  {
    context.checking(new Expectations()
    {
      {
        allowing(provider).now(); will(returnValue(System.currentTimeMillis()));
      }
    });
    
    Session session = new Session(0, provider);
    session.refresh();
    assertThat(session.isValid(), is(Boolean.TRUE));
  }
}
