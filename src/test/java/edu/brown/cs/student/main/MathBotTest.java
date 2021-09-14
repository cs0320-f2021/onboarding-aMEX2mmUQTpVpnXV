package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MathBotTest {

  @Test
  public void testAddition() {
    MathBot matherator9000 = new MathBot();
    double output = matherator9000.add(10.5, 3);
    assertEquals(13.5, output, 0.01);
  }

  @Test
  public void testLargerNumbers() {
    MathBot matherator9001 = new MathBot();
    double output = matherator9001.add(100000, 200303);
    assertEquals(300303, output, 0.01);
  }

  @Test
  public void testSubtraction() {
    MathBot matherator9002 = new MathBot();
    double output = matherator9002.subtract(18, 17);
    assertEquals(1, output, 0.01);
  }

  // TODO: add more unit tests of your own
  @Test
  public void customTestOne() {
    MathBot matherator9003 = new MathBot();
    double output = matherator9003.subtract(14, 19.2);
    assertEquals(-5.2, output, 0.01);
  }
  @Test
  public void customTestTwo() {
    MathBot matherator9004 = new MathBot();
    double output = matherator9004.add(1.5, 2.5);
    assertEquals(4, output, 0.01);
  }
  @Test
  public void customTestThree() {
    MathBot matherator9005 = new MathBot();
    double output = matherator9005.add(105.2, 7.4);
    assertEquals(112.6, output, 0.01);
  }
  @Test
  public void customTestFour() {
    MathBot matherator9005 = new MathBot();
    double output = matherator9005.subtract(9.3, 5.3);
    assertEquals(4, output, 0.01);
  }
}
