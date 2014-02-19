package org.mdavi.minigame.receiver.score;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonScoreHolder implements ScoreHolder
{

  private final Map<Integer, Map<Integer, Set<Integer>>> userLevelScores;

  private SingletonScoreHolder ()
  {
    userLevelScores = new ConcurrentHashMap<>();
  }

  private static class SingletonInitializer
  {
    public static SingletonScoreHolder INSTANCE = new SingletonScoreHolder();
  }

  public static SingletonScoreHolder getInstance ()
  {
    return SingletonInitializer.INSTANCE;
  }

  @Override
  public void saveScoreFor (final int userid, final int levelid, final int score)
  {
    Map<Integer, Set<Integer>> levelScores = userLevelScores.get(userid);
    if (null == levelScores) levelScores = new ConcurrentHashMap<>(1);
    synchronized (levelScores)
    {
      saveNewUserScore(levelScores, userid, levelid, score);
    }
  }

  @Override
  public Set<Score> getHighScores (final int levelid)
  {
    final Map<Integer, Integer> result = new HashMap<>();
    for (final int userid : userLevelScores.keySet())
    {
      final Map<Integer, Set<Integer>> levelScores = userLevelScores.get(userid);
      synchronized (levelScores)
      {
        final Set<Integer> scores = levelScores.get(levelid);
        if (null == scores) result.put(userid, -1);
        else
        {
          final int score = scores.iterator().next();
          result.put(userid, score);
        }
      }
    }

    final Set<Score> scores = new TreeSet<>();
    for (final int userid : result.keySet())
    {
      scores.add(new Score(userid, result.get(userid)));
    }

    return scores;
  }

  private void saveNewUserScore (final Map<Integer, Set<Integer>> levelScores, final int userid, final int levelid, final int score)
  {
    createNewScore(levelScores, levelid, score);
    userLevelScores.put(userid, levelScores);
  }

  private void createNewScore (final Map<Integer, Set<Integer>> levelScores, final int levelid, final int score)
  {
    Set<Integer> scores = levelScores.get(levelid);
    if (null == scores) scores = Collections.synchronizedSortedSet(new TreeSet<Integer>(new ReverseComparator()));
    addScore(scores, score);
    levelScores.put(levelid, scores);
  }

  private void addScore (final Set<Integer> existingScores, final int score)
  {
    existingScores.add(score);
  }
}
