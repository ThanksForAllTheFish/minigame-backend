package org.mdavi.minigame;

import static org.mdavi.minigame.logger.ConsoleLogger.log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.mdavi.minigame.exception.IllegalURIPathException;
import org.mdavi.minigame.exception.UnauthorizedException;
import org.mdavi.minigame.exception.UnsupportedMethodException;
import org.mdavi.minigame.receiver.RequestReceiver;
import org.mdavi.minigame.receiver.RequestReceiverFactory;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings(value = {"restriction"})
public class RequestHandler implements HttpHandler
{
  private final RequestReceiverFactory requestReceiverFactory;

  public RequestHandler (final RequestReceiverFactory requestReceiverFactory)
  {
    this.requestReceiverFactory = requestReceiverFactory;
  }

  @Override
  public void handle (final HttpExchange request) throws IOException
  {
    runProtected (new Operation()
    {
      @Override
      public void run (final HttpExchange request) throws IOException
      {
        log("Receive request " + request.getRequestURI());
        final RequestReceiver requestReceiver = requestReceiverFactory.getRequestReceiver(request);
        final byte[] response = requestReceiver.handle().getBytes();
        
        log("Sending response " + String.valueOf(response));
        
        request.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
        
        request.getResponseBody().write(response);
      }
    }, request);
    
  }
  
  private void runProtected (final Operation operation, final HttpExchange request) throws IOException
  {
    final OutputStream responseBody = request.getResponseBody();
    try {
      request.getResponseHeaders().add("Content-Type", "text/html");
      operation.run(request);
    } catch (final IllegalURIPathException ex) {
      log(ex);
      sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, ex.getMessage(), request, responseBody);
    } catch (final IllegalArgumentException ex) {
      log(ex);
      sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, ex.getMessage(), request, responseBody);
    } catch (final UnauthorizedException ex) {
      log(ex);
      sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, ex.getMessage(), request, responseBody);
    } catch (final UnsupportedMethodException ex) {
      log(ex);
      final Headers headers = request.getResponseHeaders();
      headers.add("ALLOW", "GET");
      headers.add("ALLOW", "POST");
      sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, ex.getMessage(), request, responseBody);
      
    } catch (final Exception ex) {
      log(ex);
      sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, ex.getMessage(), request, responseBody);
    }
    finally {
      responseBody.close();
    }
  }
  
  private void sendResponseHeaders(final int errorCode, final String message, final HttpExchange request, final OutputStream responseBody) throws IOException {
    final String realMessage = null == message ? "" : message;
    request.sendResponseHeaders(errorCode, realMessage.getBytes().length);
    responseBody.write(realMessage.getBytes());
  }

  interface Operation {

    void run (HttpExchange request) throws IOException;
  }
}
