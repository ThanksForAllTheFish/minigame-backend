package org.mdavi.minigame.receiver;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.mdavi.minigame.TestContext;
import org.mdavi.minigame.exception.IllegalURIPathException;
import org.mdavi.minigame.exception.UnsupportedMethodException;
import org.mdavi.minigame.receiver.MiniGameRequestReceiverFactory;
import org.mdavi.minigame.receiver.RequestReceiver;
import org.mdavi.minigame.receiver.login.LoginRequestReceiver;
import org.mdavi.minigame.receiver.score.HighScoreRequestReceiver;
import org.mdavi.minigame.receiver.score.ScoreRequestReceiver;

import com.sun.net.httpserver.MockHttpExchange;

public class MiniGameRequestReceiverFactoryTest extends TestContext
{
  private MiniGameRequestReceiverFactory factory = new MiniGameRequestReceiverFactory();

  @Test
  public void givenAValidLoginRequest_createsALoginRequestReceiver ()
  {
    RequestReceiver requestReceiver = factory.getRequestReceiver(new MockHttpExchange("GET", "/0/login"));
    
    assertThat(requestReceiver, is(instanceOf(LoginRequestReceiver.class)));
  }
  
  @Test
  public void givenAValidPostScoreRequest_createsAScoreRequestReceiver ()
  {
    MockHttpExchange request = new MockHttpExchange("POST", "/32/score?sessionKey=session-key-sample");
    request.setStreams(new ByteArrayInputStream("1500".getBytes()), new ByteArrayOutputStream());
    RequestReceiver requestReceiver = factory.getRequestReceiver(request);
    
    assertThat(requestReceiver, is(instanceOf(ScoreRequestReceiver.class)));
  }
  
  @Test
  public void givenAValidHighScoreRequest_createsAHighRequestReceiver ()
  {
    RequestReceiver requestReceiver = factory.getRequestReceiver(new MockHttpExchange("GET", "/32/highscorelist"));
    
    assertThat(requestReceiver, is(instanceOf(HighScoreRequestReceiver.class)));
  }
  
  @Test
  public void givenANegativeScore_throwsIllegalArgumentException ()
  {
    MockHttpExchange request = new MockHttpExchange("POST", "/32/score?sessionKey=ses-sionkey");
    request.setStreams(new ByteArrayInputStream("-1".getBytes()), new ByteArrayOutputStream());
    expectedException.expect(IllegalArgumentException.class);
    
    factory.getRequestReceiver(request);
  }
  
  @Test
  public void givenANonnumericalScore_throwsNumberFormatException ()
  {
    MockHttpExchange request = new MockHttpExchange("POST", "/32/score?sessionKey=ses-sionkey");
    request.setStreams(new ByteArrayInputStream("score".getBytes()), new ByteArrayOutputStream());
    expectedException.expect(NumberFormatException.class);
    
    factory.getRequestReceiver(request);
  }

  @Test
  public void givenAnInvalidContext_generatesAnIllegalURIPathException ()
  {
    expectedException.expect(IllegalURIPathException.class);
    
    factory.getRequestReceiver(new MockHttpExchange("GET", "/32/login/any"));
    factory.getRequestReceiver(new MockHttpExchange("POST", "/32/score"));
    factory.getRequestReceiver(new MockHttpExchange("GET", "/32/score?wrongQuery=anything"));
    factory.getRequestReceiver(new MockHttpExchange("GET", "/32/other?sessionkey=any-thing"));
    factory.getRequestReceiver(new MockHttpExchange("POST", "/32/score?sessionkey=anything&other=whatever"));
    factory.getRequestReceiver(new MockHttpExchange("GET", "/wrong/score?sessionkey=anything&other=whatever"));
    factory.getRequestReceiver(new MockHttpExchange("GET", "/-1/score?sessionkey=anything&other=whatever"));
  }
  
  @Test
  public void givenAnUnsopportedMethod_generatesAnUnsupportedMethodException ()
  {
    expectedException.expect(UnsupportedMethodException.class);
    
    factory.getRequestReceiver(new MockHttpExchange("PUT", "/wrong/score?sessionkey=anything&other=whatever"));
  }
}
