package org.mdavi.minigame;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import org.jmock.Expectations;
import org.junit.Test;
import org.mdavi.minigame.RequestHandler;
import org.mdavi.minigame.exception.IllegalURIPathException;
import org.mdavi.minigame.exception.UnauthorizedException;
import org.mdavi.minigame.exception.UnsupportedMethodException;
import org.mdavi.minigame.receiver.RequestReceiver;
import org.mdavi.minigame.receiver.RequestReceiverFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.MockHttpExchange;

@SuppressWarnings(value = {"restriction"})
public class RequestHandlerTest extends TestContext
{
  private RequestReceiverFactory requestReceiverFactory = context.mock(RequestReceiverFactory.class);
  private RequestReceiver requestReceiver = context.mock(RequestReceiver.class);
  private String response = "response";
  private HttpExchange request;
  private RequestHandler handler = new RequestHandler(requestReceiverFactory);

  @Test
  public void givenAHttpExchange_theHandleCanGenerateA200Response () throws IOException
  {
    request = new MockHttpExchange("GET", "/32/login");
    request.setStreams(new ByteArrayInputStream("".getBytes()), new ByteArrayOutputStream());
    context.checking(new Expectations()
    {
      {
        oneOf(requestReceiverFactory).getRequestReceiver(request); will(returnValue(requestReceiver));
        oneOf(requestReceiver).handle(); will(returnValue(response));
      }
    });
    
    handler.handle(request);
    
    assertThat(request.getResponseCode(), is(HttpURLConnection.HTTP_OK));
    String responseBody = request.getResponseBody().toString();
    assertThat(responseBody, is(response));
  }
  
  @Test
  public void givenAHttpExchange_theHandleCanGenerateA400Response () throws IOException
  {
    request = new MockHttpExchange("POST", "/32/score?sessionkey=invalid");
    context.checking(new Expectations()
    {
      {
        oneOf(requestReceiverFactory).getRequestReceiver(request); will(throwException(new IllegalURIPathException()));
      }
    });
    
    handler.handle(request);
    
    assertThat(request.getResponseCode(), is(HttpURLConnection.HTTP_BAD_REQUEST));
  }
  
  @Test
  public void givenAHttpExchange_theHandleCanGenerateA401Response () throws IOException
  {
    request = new MockHttpExchange("POST", "/32/score?sessionkey=invalid");
    context.checking(new Expectations()
    {
      {
        oneOf(requestReceiverFactory).getRequestReceiver(request); will(throwException(new UnauthorizedException()));
      }
    });
    
    handler.handle(request);
    
    assertThat(request.getResponseCode(), is(HttpURLConnection.HTTP_UNAUTHORIZED));
  }
  
  @Test
  public void givenAHttpExchange_theHandleCanGenerateA403Response () throws IOException
  {
    request = new MockHttpExchange("POST", "/32/score?sessionkey=invalid");
    context.checking(new Expectations()
    {
      {
        oneOf(requestReceiverFactory).getRequestReceiver(request); will(throwException(new IllegalArgumentException()));
      }
    });
    
    handler.handle(request);
    
    assertThat(request.getResponseCode(), is(HttpURLConnection.HTTP_FORBIDDEN));
  }
  
  @Test
  public void givenAHttpExchange_theHandleCanGenerateA405Response () throws IOException
  {
    request = new MockHttpExchange("PUT", "/doesntmatter");
    context.checking(new Expectations()
    {
      {
        oneOf(requestReceiverFactory).getRequestReceiver(request); will(throwException(new UnsupportedMethodException("error")));
      }
    });
    
    handler.handle(request);
    
    assertThat(request.getResponseCode(), is(HttpURLConnection.HTTP_BAD_METHOD));
    assertThat(request.getResponseHeaders().get("ALLOW"), containsInAnyOrder("GET", "POST"));
  }
  
  @Test
  public void givenAHttpExchange_theHandleCanGenerateA500Response () throws IOException
  {
    request = new MockHttpExchange("POST", "/32/login");
    context.checking(new Expectations()
    {
      {
        oneOf(requestReceiverFactory).getRequestReceiver(request); will(throwException(new Exception("error")));
      }
    });
    
    handler.handle(request);
    
    assertThat(request.getResponseCode(), is(HttpURLConnection.HTTP_INTERNAL_ERROR));
  }

}
