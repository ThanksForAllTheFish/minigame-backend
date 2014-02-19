package org.mdavi.minigame.receiver.score;

import java.util.Comparator;

public class ReverseComparator implements Comparator<Integer>
{

  @Override
  public int compare (Integer left, Integer right)
  {
    return right.compareTo(left);
  }

}
