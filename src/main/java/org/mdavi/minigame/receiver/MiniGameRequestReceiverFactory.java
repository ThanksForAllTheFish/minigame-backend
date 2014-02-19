package org.mdavi.minigame.receiver;

import static org.mdavi.minigame.logger.ConsoleLogger.log;

import java.io.InputStream;
import java.util.Scanner;

import org.mdavi.minigame.exception.IllegalURIPathException;
import org.mdavi.minigame.exception.UnsupportedMethodException;
import org.mdavi.minigame.receiver.login.LoginRequestReceiver;
import org.mdavi.minigame.receiver.score.HighScoreRequestReceiver;
import org.mdavi.minigame.receiver.score.ScoreRequestReceiver;
import org.mdavi.minigame.receiver.score.SingletonScoreHolder;
import org.mdavi.minigame.session.SingletonSessionHolder;

import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings(value = {"restriction"})
public class MiniGameRequestReceiverFactory implements RequestReceiverFactory
{

  @Override
  public RequestReceiver getRequestReceiver (final HttpExchange request)
  {
    final String requestMethod = request.getRequestMethod();
    final String requestContext = extractRequestContext(request);
    log("Request method: " + requestMethod + ", request context " + requestContext);
    assertValidRequest(requestMethod, requestContext);

    final int resource = Integer.parseInt(requestContext.substring(requestContext.indexOf('/') + 1,
        requestContext.lastIndexOf('/')));

    if (requestMethod.equalsIgnoreCase("GET"))
    {
      if (requestContext.endsWith("login")) return getLoginRequestReceiver(resource);
      else
        return getHighScoreRequestReceiver(resource);
    }

    return getScoreRequestReceiver(resource, requestContext.substring(requestContext.indexOf('=') + 1),
        getRequestBody(request.getRequestBody()));
  }

  private String getRequestBody (final InputStream requestBody)
  {
    @SuppressWarnings("resource")
    final
    Scanner scanner = new Scanner(requestBody).useDelimiter("\\A");
    return scanner.next();
  }

  private String extractRequestContext (final HttpExchange request)
  {
    String requestContext = request.getRequestURI().getPath();
    final String query = request.getRequestURI().getQuery();
    if (null != query) requestContext += "?" + query;
    return requestContext;
  }

  private void assertValidRequest (final String requestMethod, final String requestContext)
  {
    // if(!"text/html".equalsIgnoreCase(requestContentType)) throw new
    // UnsupportedContentTypeException("Content-Type must be 'text/html'");

    if (isNotSupported(requestMethod)) throw new UnsupportedMethodException("I can handle only GET and POST");

    if (!requestContext.toLowerCase().matches("/\\d+/(score\\?sessionkey=(\\w+-?)+|login|highscorelist)")) throw new IllegalURIPathException(
        "Bad URI call");
  }

  private boolean isNotSupported (final String requestMethod)
  {
    return !"GET".equalsIgnoreCase(requestMethod) && !"POST".equalsIgnoreCase(requestMethod);
  }

  private RequestReceiver getLoginRequestReceiver (final int userid)
  {
    return new LoginRequestReceiver(userid, SingletonSessionHolder.getInstance());
  }

  private RequestReceiver getScoreRequestReceiver (final int levelid, final String sessionKey, final String requestBody)
  {
    final int score = Integer.parseInt(requestBody);
    if (score < 0) throw new IllegalArgumentException("Invalid score");

    return new ScoreRequestReceiver(levelid, score, sessionKey, SingletonSessionHolder.getInstance(),
        SingletonScoreHolder.getInstance());
  }

  private RequestReceiver getHighScoreRequestReceiver (final int resource)
  {
    return new HighScoreRequestReceiver(resource, SingletonScoreHolder.getInstance());
  }
}
