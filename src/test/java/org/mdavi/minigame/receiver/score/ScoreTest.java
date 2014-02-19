package org.mdavi.minigame.receiver.score;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mdavi.minigame.receiver.score.Score;

public class ScoreTest
{
  
  private Score score = new Score(2, 20);

  @Test
  public void testHashCode ()
  {
    assertThat(score.hashCode(), is(713));
  }

  @Test
  public void testEqualsObject ()
  {
    assertThat(score.equals(new Score(2, 20)), is(Boolean.TRUE));
    assertThat(score.equals(new Score(1, 20)), is(Boolean.FALSE));
    assertThat(score.equals(new Object()), is(Boolean.FALSE));
  }

  @Test
  public void testCompareTo ()
  {
    assertThat(score.compareTo(new Score(2, 20)), is(0));
    assertThat(score.compareTo(new Score(1, 20)), is(1));
    assertThat(score.compareTo(new Score(1, 30)), is(10));
  }

  @Test
  public void testPrintableScore ()
  {
    assertThat(score.printableScore(), is("2=20"));
    assertThat(new Score(1, -1).printableScore(), is("1="));
  }

}
