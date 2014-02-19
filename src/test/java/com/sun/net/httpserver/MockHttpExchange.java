package com.sun.net.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings(value = {"restriction"})
public class MockHttpExchange extends HttpExchange {

  private OutputStream responseBody;
  private Headers headers;
  private String requestMethod;
  private int responseCode;
  private String uri;
  private InputStream requestBody;
  
  public MockHttpExchange(String methodName, String uri) {
    this.requestMethod = methodName;
    this.uri = uri;
    this.responseBody = new ByteArrayOutputStream();
    this.headers = new Headers();
  }

  @Override
  public void close ()
  {
    try
    {
      responseBody.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public Object getAttribute (String arg0)
  {
    return null;
  }

  @Override
  public HttpContext getHttpContext ()
  {
    return null;
  }

  @Override
  public InetSocketAddress getLocalAddress ()
  {
    return null;
  }

  @Override
  public HttpPrincipal getPrincipal ()
  {
    return null;
  }

  @Override
  public String getProtocol ()
  {
    return null;
  }

  @Override
  public InetSocketAddress getRemoteAddress ()
  {
    return null;
  }

  @Override
  public InputStream getRequestBody ()
  {
    return requestBody;
  }

  @Override
  public Headers getRequestHeaders ()
  {
    return null;
  }

  @Override
  public String getRequestMethod ()
  {
    return requestMethod;
  }

  @Override
  public URI getRequestURI ()
  {
    try
    {
      return new URI("http://localhost:8080" + uri);
    }
    catch (URISyntaxException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public OutputStream getResponseBody ()
  {
    return responseBody;
  }

  @Override
  public int getResponseCode ()
  {
    return responseCode;
  }

  @Override
  public Headers getResponseHeaders ()
  {
    return headers;
  }

  @Override
  public void sendResponseHeaders (int responseCode, long responseLenght) throws IOException
  {
    this.responseCode = responseCode;
  }

  @Override
  public void setAttribute (String arg0, Object arg1)
  {
  }

  @Override
  public void setStreams (InputStream requestBody, OutputStream responseBody)
  {
    this.requestBody = requestBody;
    this.responseBody = responseBody;
  }
}
