package com.lswebworld.fhir.camelfhirexamples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CamelFhirExamplesApplicationTests {

  private static String dateString = "20050110045253";

  @Test
  public void contextLoads() {
    assertTrue(true);
  }

  @Test
  public void testYearDateConversion() {
    assertEquals(2005, Integer.parseInt(dateString.substring(0,4)));
  }

  @Test
  public void testMonthDateConversion() {
    assertEquals(1, Integer.parseInt(dateString.substring(4,6)));
  }

  @Test
  public void testDayDateConversion() {
    assertEquals(10, Integer.parseInt(dateString.substring(6,8)));
  }

  @Test
  public void testHourConversion() {
    assertEquals(4,Integer.parseInt(dateString.substring(8,10)));
  }

  @Test
  public void testMinuteConversion() {
    assertEquals(52, Integer.parseInt(dateString.substring(10,12)));
  }

}
