package edu.brown.cs.student.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

    public Parser() {
    }

    public List<StarData> Parse(String filepath) throws FileNotFoundException {
        List<StarData> list = new ArrayList<>();
        String absolutePath = new File("").getAbsolutePath();
        File file = new File(absolutePath + "/" + filepath);
        Scanner sc = new Scanner(file);
        sc.nextLine(); // The first line of the file is not data
        while (sc.hasNextLine()) {
            String[] data = sc.nextLine().split(",");
            String starID = data[0], name = data[1];
            double x = Double.parseDouble(data[2]), y = Double.parseDouble(data[3]), z = Double.parseDouble(data[4]);
            list.add(new StarData(starID, name, x, y, z));
        }
        return list;
    }

}
