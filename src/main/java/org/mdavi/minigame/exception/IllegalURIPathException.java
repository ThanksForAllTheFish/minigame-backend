package org.mdavi.minigame.exception;

public class IllegalURIPathException extends RuntimeException
{

  private static final long serialVersionUID = 6166376180891953396L;

  public IllegalURIPathException ()
  {
    super();
  }

  public IllegalURIPathException (String message)
  {
    super(message);
  }

  public IllegalURIPathException (String message, Throwable cause)
  {
    super(message, cause);
  }

  public IllegalURIPathException (Throwable cause)
  {
    super(cause);
  }

}
