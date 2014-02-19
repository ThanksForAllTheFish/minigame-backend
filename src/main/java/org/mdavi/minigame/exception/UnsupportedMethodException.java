package org.mdavi.minigame.exception;

public class UnsupportedMethodException extends RuntimeException
{
  private static final long serialVersionUID = 5876979597540704726L;

  public UnsupportedMethodException ()
  {
    super();
  }

  public UnsupportedMethodException (String message)
  {
    super(message);
  }

  public UnsupportedMethodException (String message, Throwable cause)
  {
    super(message, cause);
  }

  public UnsupportedMethodException (Throwable cause)
  {
    super(cause);
  }

}
