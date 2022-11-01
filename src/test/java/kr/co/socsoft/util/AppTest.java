package kr.co.socsoft.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import kr.co.socsoft.util.CSVTest2;

/**
 * Unit test for simple App.
 */
public class AppTest {

    String userPath;
    String resource;
    String outputCsvfilePath;
    String outputShapefilePath;
    String healthfilePath;
    String resultPath;

    Properties prop;

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
//        assertTrue(true);
//        Args args = new Args();
//        String[] argv = { "-log", "2", "-groups", "unit" };
//        JCommander.newBuilder()
//                .addObject(args)
//                .build()
//                .parse(argv);
//
//        Assert.assertEquals(jct.verbose.intValue(), 2);

        userPath = System.getProperty("user.dir"); //사용자 디렉토리 정보
        resource = userPath + "/config/global.properties"; //properties 위치 설정

        prop = new Properties();

        try {
            prop.load(new InputStreamReader(new FileInputStream(resource)));// properties 읽어오기
            this.outputCsvfilePath = prop.getProperty("output.csvfile.path");
            this.healthfilePath = prop.getProperty("healthfile.path");
            this.resultPath = prop.getProperty("result.path");
        } catch (IOException e) {

        }

        //---------------------------- csv + csv => csv -----------------------------------------
        CSVTest2 csvtest2 = new CSVTest2();


        csvtest2.sample(prop.getProperty("output.csvfile.path"), prop.getProperty("healthfile.path"), prop.getProperty("result.path")); // 건강지표 원본파일, 비교할파일, 생성할 파일
        csvtest2.sample(outputCsvfilePath, healthfilePath, resultPath); // 건강지표 원본파일, 비교할파일, 생성할 파일
//		csvtest2.sample( prop.getProperty("output.csvfile.path"),prop.getProperty("dementiafile.path"),prop.getProperty("result.path")); // 원본파일, 비교할파일, 생성할 파일

    }
}
