package edu.brown.cs.student.main;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParserTest {
    @Test
    public void testParser() {
        Parser parser = new Parser();
        StarData star = new StarData("1","Lonely Star",5.0,-2.24,10.04);
        List<StarData> expected = new ArrayList<>();
        expected.add(star);

        try {
            List<StarData> output = parser.Parse("data/stars/one-star.csv");
            assertEquals(true,expected.get(0).equals(output.get(0)));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Invalid FilePath");
        }
    }
}
