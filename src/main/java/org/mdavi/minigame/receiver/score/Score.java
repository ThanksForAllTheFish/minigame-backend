package org.mdavi.minigame.receiver.score;

public class Score implements Comparable<Score>
{
  int userid;
  int score;
  
  public Score (int userid, int score)
  {
    this.userid = userid;
    this.score = score;
  }
  
  public int getScore ()
  {
    return score;
  }
  
  @Override
  public int hashCode ()
  {
    int mul = 31;
    return mul + userid * mul + score * mul;
  }
  
  @Override
  public boolean equals (Object obj)
  {
    if(null == obj || !(obj instanceof Score)) return false;
    Score that = (Score)obj;
    return userid == that.userid && score == that.score;
  }

  @Override
  public int compareTo (Score o)
  {
    int compare = o.score - score;
    return compare == 0 ? userid - o.userid : compare;
  }
  
  public String printableScore ()
  {
    return userid + "=" + (score < 0 ? "" : score);
  }
}
