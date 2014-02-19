package org.mdavi.minigame;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class TestContext
{
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  public TestContext ()
  {
    super();
  }

}
