package kr.co.socsoft.util;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Hello world!
 *
 */
public class App {

    @Parameter(names={"--shapeinput", "-si"})
    String shapeinputPath;

    @Parameter(names={"--indicatorinput", "-ci"})
    String indicatorinputPath;

    @Parameter(names={"--shapeoutput", "-so"})
    String shapeoutputPath;

    public static void main( String ... argv ) {
        App main = new App();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run();
    }

    public void run() {
        System.out.println("START! Please Wait a Minute. :)");
        
        if (shapeinputPath == null) {
            System.out.printf("shapeinput path is required.");
        }
        if (indicatorinputPath == null) {
            System.out.printf("indicatorinputPath path is required.");
        }
        if (shapeoutputPath == null) {
            System.out.printf("shapeoutputPath path is required.");
        }
//        System.out.printf("%s %s %s\n", shapeinputPath, indicatorinputPath, shapeoutputPath);


        MapUtil mapUtil = new MapUtil();
        try {
            mapUtil.run(shapeinputPath, indicatorinputPath, shapeoutputPath);
        } catch (Exception e) {
            System.out.println("ERROR : " + "Please Check the Contents of the File Again \n" +e.toString());
            throw new RuntimeException(e);
        }

    }
}
