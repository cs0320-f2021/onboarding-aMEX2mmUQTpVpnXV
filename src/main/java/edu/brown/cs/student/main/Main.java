package edu.brown.cs.student.main;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;


/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

    // use port 4567 by default when running server
    private static final int DEFAULT_PORT = 4567;

    /**
     * The initial method called when execution begins.
     *
     * @param args An array of command line arguments
     */
    public static void main(String[] args) {
        new Main(args).run();
    }

    private String[] args;

    List<StarData> starData = null;

    private Main(String[] args) {
        this.args = args;
    }

    private StarData findStar(String name, List<StarData> list) throws Exception {
        String newName = name.replace("\"", "");
        for (StarData star : list) {
            if (star.getName().equals(newName)) return star;
        }
        System.out.println("ERROR: Could not find the listed star in the data");
        return null;
    }

    private double distanceFormula(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));
    }

    private List<StarData> kClosestNeighbors(List<StarData> list, double x, double y, double z, int k) {
        List<StarData> randomList = list;
        List<StarData> res = new ArrayList<>();
        Collections.shuffle(list); // So that we encounter ties in a random order
        PriorityQueue<StarData> pq = new PriorityQueue<>((star1, star2) -> {
            double diff = (distanceFormula(star1.getX(), star1.getY(), star1.getZ(), x, y, z) - distanceFormula(star2.getX(), star2.getY(), star2.getZ(), x, y, z));
            if (diff == 0) return 0;
            return (diff > 0) ? 1 : -1;
        });
        for (StarData star : randomList) pq.add(star);
        for (int i = 0; i < Math.min(k, list.size()); i++) {
            res.add(pq.poll());
        }
        return res;
    }

    private static void printStarInfo(StarData star) {
        System.out.println(star.getStarID());
    }

    private void run() {
        // set up parsing of command line flags
        OptionParser parser = new OptionParser();

        // "./run --gui" will start a web server
        parser.accepts("gui");

        // use "--port <n>" to specify what port on which the server runs
        parser.accepts("port").withRequiredArg().ofType(Integer.class)
                .defaultsTo(DEFAULT_PORT);

        OptionSet options = parser.parse(args);
        if (options.has("gui")) {
            runSparkServer((int) options.valueOf("port"));
        }

        // TODO: Add your REPL here!
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            while ((input = br.readLine()) != null) {
                try {
                    input = input.trim();
                    String[] arguments = input.split(" ");
                    MathBot bot = new MathBot();
                    if (arguments[0].equals("add")) {
                        if (Objects.equals(arguments[1], "") || Objects.equals(arguments[2], ""))
                            throw new Exception("");
                        else {
                            double val1 = Double.parseDouble(arguments[1]);
                            double val2 = Double.parseDouble(arguments[2]);
                            System.out.println(bot.add(val1, val2));
                        }
                    } else if (arguments[0].equals("subtract")) {
                        if (Objects.equals(arguments[1], "") || Objects.equals(arguments[2], ""))
                            throw new Exception("");
                        else {
                            double val1 = Double.parseDouble(arguments[1]);
                            double val2 = Double.parseDouble(arguments[2]);
                            System.out.println(bot.subtract(val1, val2));
                        }
                    }
                    // Project 0 Start
                    else if (arguments[0].equals("stars")) {
                        Parser customParser = new Parser();
                        starData = customParser.Parse(arguments[1]);
                        System.out.println("Read " + starData.size() + " stars from " + arguments[1]);
                    }
                    // Naive Neighbors Start
                    else if (arguments[0].equals("naive_neighbors")) {
                        int k = Integer.parseInt(arguments[1]);
                        String nameOfStar = arguments[2];
                        if (arguments[2].startsWith("\"")) {
                            for (int i = 3; i < arguments.length; i++) {
                                nameOfStar = nameOfStar.concat(" ");
                                nameOfStar = nameOfStar.concat(arguments[i]);
                            }
                        }
                        if (nameOfStar.startsWith("\"") && nameOfStar.endsWith("\"")) {
                            StarData desiredStar = findStar(nameOfStar, starData);
                            List<StarData> res = kClosestNeighbors(starData, desiredStar.getX(), desiredStar.getY(), desiredStar.getZ(), k + 1);
                            res.remove(0);
                            for (StarData star : res) printStarInfo(star);
                            //stars data/stars/three-star.csv
                            //naive_neighbors 1 "Star One"
                        } else if (arguments.length == 0) {
                        } else {
                            double x = Double.parseDouble(arguments[2]);
                            double y = Double.parseDouble(arguments[3]);
                            double z = Double.parseDouble(arguments[4]);
                            List<StarData> res = kClosestNeighbors(starData, x, y, z, k);
                            for (StarData star : res) printStarInfo(star);
                        }
                    } else {
                        System.out.println("ERROR:");
                    }

                } catch (Exception e) {
                    // e.printStackTrace();
                    System.out.println("ERROR: We couldn't process your input");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: Invalid input for REPL");
        }

    }

    private static FreeMarkerEngine createEngine() {
        Configuration config = new Configuration(Configuration.VERSION_2_3_0);

        // this is the directory where FreeMarker templates are placed
        File templates = new File("src/main/resources/spark/template/freemarker");
        try {
            config.setDirectoryForTemplateLoading(templates);
        } catch (IOException ioe) {
            System.out.printf("ERROR: Unable use %s for template loading.%n",
                    templates);
            System.exit(1);
        }
        return new FreeMarkerEngine(config);
    }

    private void runSparkServer(int port) {
        // set port to run the server on
        Spark.port(port);

        // specify location of static resources (HTML, CSS, JS, images, etc.)
        Spark.externalStaticFileLocation("src/main/resources/static");

        // when there's a server error, use ExceptionPrinter to display error on GUI
        Spark.exception(Exception.class, new ExceptionPrinter());

        // initialize FreeMarker template engine (converts .ftl templates to HTML)
        FreeMarkerEngine freeMarker = createEngine();

        // setup Spark Routes
        Spark.get("/", new MainHandler(), freeMarker);
    }

    /**
     * Display an error page when an exception occurs in the server.
     */
    private static class ExceptionPrinter implements ExceptionHandler<Exception> {
        @Override
        public void handle(Exception e, Request req, Response res) {
            // status 500 generally means there was an internal server error
            res.status(500);

            // write stack trace to GUI
            StringWriter stacktrace = new StringWriter();
            try (PrintWriter pw = new PrintWriter(stacktrace)) {
                pw.println("<pre>");
                e.printStackTrace(pw);
                pw.println("</pre>");
            }
            res.body(stacktrace.toString());
        }
    }

    /**
     * A handler to serve the site's main page.
     *
     * @return ModelAndView to render.
     * (main.ftl).
     */
    private static class MainHandler implements TemplateViewRoute {
        @Override
        public ModelAndView handle(Request req, Response res) {
            // this is a map of variables that are used in the FreeMarker template
            Map<String, Object> variables = ImmutableMap.of("title",
                    "Go go GUI");

            return new ModelAndView(variables, "main.ftl");
        }
    }
}
