package org.mdavi.minigame.exception;

public class UnauthorizedException extends RuntimeException
{
  private static final long serialVersionUID = 5876979597540704726L;

  public UnauthorizedException ()
  {
    super();
  }

  public UnauthorizedException (String message)
  {
    super(message);
  }

  public UnauthorizedException (String message, Throwable cause)
  {
    super(message, cause);
  }

  public UnauthorizedException (Throwable cause)
  {
    super(cause);
  }

}
