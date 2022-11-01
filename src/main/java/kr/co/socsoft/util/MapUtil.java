package kr.co.socsoft.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.UIManager;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

//import csv.CsvToShp;
//import shp.loadToShp2;

public class MapUtil {
    String userPath = System.getProperty("user.dir");;
//    String propPath = userPath + "/config/global-mac.properties"; //properties 위치 설정;
    String propPath = userPath + "/config/global.properties"; //properties 위치 설정;
    String outputCsvfilePath;
    String outputShapefilePath;
    String indicatorfilePath;
    String resultPath;

    Properties prop  = new Properties();

    public MapUtil() {
        try {
//            propPath = userPath + "/config/global-mac.properties";
            propPath = userPath + "/config/global.properties";
            prop.load(new InputStreamReader(new FileInputStream(propPath)));
            this.outputCsvfilePath = prop.getProperty("output.csvfile.path");
            this.indicatorfilePath = prop.getProperty("indicatorfile.path");
            this.outputShapefilePath = prop.getProperty("output.shapefile.path");
            this.resultPath = prop.getProperty("result.path");

        } catch (IOException e) {

        }

    }

    public MapUtil(String propPath) {

        //TODO: param null 체크 추가
        try {
            prop.load(new InputStreamReader(new FileInputStream(propPath)));
            this.outputCsvfilePath = prop.getProperty("output.csvfile.path");
            this.indicatorfilePath = prop.getProperty("indicatorfile.path");
            this.outputShapefilePath = prop.getProperty("output.shapefile.path");
            this.resultPath = prop.getProperty("result.path");

        } catch (IOException e) {

        }
    }

    public MapUtil(String outputCsvfilePath, String indicatorfilePath, String resultPath) {

        //TODO: param null 체크 추가
        try {
//            propPath = userPath + "/config/global-mac.properties";
            propPath = userPath + "/config/global.properties";
            prop.load(new InputStreamReader(new FileInputStream(propPath)));
            this.outputCsvfilePath = outputCsvfilePath;
            this.indicatorfilePath = indicatorfilePath;
            this.outputShapefilePath = outputCsvfilePath;
            this.resultPath = resultPath;

        } catch (IOException e) {

        }
    }

    public void run(String anotherInputFilePath, String indicatorfilePath, String outputShapefilePath) throws Exception {
        
        //---------------------------- shp to csv -----------------------------------------
        if (anotherInputFilePath == null) {
            anotherInputFilePath = prop.getProperty("input.shapefile.path"); //읽고싶은 shpfile 위치 => properties에서 설정가능
        }
        if (indicatorfilePath == null) {
            indicatorfilePath = prop.getProperty("indicatorfile.path"); //읽고싶은 shpfile 위치 => properties에서 설정가능
        }
        if (outputShapefilePath == null) {
            outputShapefilePath = prop.getProperty("output.shapefile.path"); //읽고싶은 shpfile 위치 => properties에서 설정가능
        }
//        if (anotherOutputFilePath == null ) {
            String anotherOutputFilePath = prop.getProperty("output.csvfile.path"); //csvfile로 내보내질 경로및 이름 설정 => properties에서 설정가능
//        }

        Path path = Paths.get(outputShapefilePath);
        File directory = new File(String.valueOf(path.getParent()));
        if (! directory.exists()){
            directory.mkdirs();
        }

        Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(anotherOutputFilePath), "UTF-8"));

        loadToShp2 loadShape = new loadToShp2();
        loadShape.DefaultLoad(anotherInputFilePath);
        StringBuffer tmpStr = new StringBuffer();

        boolean check = true;
        try {
            while (loadShape.getlter().hasNext()) {
                loadShape.getlter().hasNext();

                tmpStr = new StringBuffer();
                SimpleFeature f = loadShape.getlter().next(); //44

                if (check) {
                    List properties = (List) f.getProperties();

                    for (int i = 0; i < properties.size(); i++) { //header 출력하는 부분
                        if (i == (properties.size() - 1)) {

                            tmpStr.append(((Property) properties.get(i)).getName()).append("\n");
                        } else {
                            tmpStr.append(((Property) properties.get(i)).getName()).append("|");
                        }
                    }

                    bw.write(tmpStr.toString());
                    tmpStr.delete(0, tmpStr.length());
                    check = false;
                }

                for (int i = 0; i < f.getAttributeCount(); i++) { //내용 출력하는 부분
                    if (i == (f.getAttributeCount() - 1)) {
                        tmpStr.append(f.getAttribute(i).toString()).append("\n");
                    } else if (f.getAttribute(i) == null) {
                        tmpStr.append("|"); //null일경우
                    } else {
                        tmpStr.append(f.getAttribute(i).toString()).append("|");
                    }
                }

                bw.write(tmpStr.toString());
                new String(tmpStr.toString().getBytes("euc-kr"), "x-windows-949");
            }
        } catch (Exception ex) {
            System.out.println("ERROR : " + ex.toString());
            ex.printStackTrace();
        } finally {
            loadShape.getlter().close();
        }
        bw.close();

//---------------------------- csv + csv => csv -----------------------------------------
        CSVTest2 csvtest2 = new CSVTest2();
//
////        csvtest2.sample(prop.getProperty("output.csvfile.path"), prop.getProperty("indicatorfile.path"), prop.getProperty("result.path")); // 건강지표 원본파일, 비교할파일, 생성할 파일
        csvtest2.sample(outputCsvfilePath, indicatorfilePath, resultPath); // 건강지표 원본파일, 비교할파일, 생성할 파일
////		csvtest2.sample( prop.getProperty("output.csvfile.path"),prop.getProperty("dementiafile.path"),prop.getProperty("result.path")); // 원본파일, 비교할파일, 생성할 파일


//---------------------------- csv to shp -----------------------------------------------
        CsvToShp csvtoshp = new CsvToShp();

        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

        List<SimpleFeature> features = new ArrayList<>();


        SimpleFeatureType TYPE = csvtoshp.createFeatureType();

        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        File file = new File(resultPath); //shp로 만들 csv 위치와 파일명 기재 => properties에서 변경가능

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));

        try {
            /* First line of the data file is the header */
            String line = reader.readLine();
//            System.out.println("Header: " + line);
            for (line = reader.readLine(); line != null; line = reader.readLine()) {
//                System.out.println(line);
                if (line.trim().length() > 0) { // skip blank lines
                    String tokens[] = line.split("\\|");
                    WKTReader reader2 = new WKTReader();
                    MultiPolygon the_geom = (MultiPolygon) reader2.read(tokens[0].toString());
                    String BASE_DATE = tokens[1].trim(); 
                    String ADM_DR_CD = tokens[2].trim();
                    String ADM_DR_NM = tokens[3].trim();
                    String OBJECTID = tokens[4].trim();
                    String LOCAL = tokens[5].trim();
                    String SUBSCRIB = tokens[6].trim();
                    String INSURANCE = tokens[7].trim();
                    String GENDER = tokens[8].trim();
                    String AGE = tokens[9].trim();
                    String N_SI_NUMER = tokens[10].trim();
                    String N_SI_DENO = tokens[11].trim();
                    String N_SI_IV = tokens[12].trim();
                    String SI_IV = tokens[13].trim();
                    String YEAR = tokens[14].trim();
                    
                    featureBuilder.add(the_geom);
                    featureBuilder.add(BASE_DATE);
                    featureBuilder.add(ADM_DR_CD);
                    featureBuilder.add(ADM_DR_NM);
                    featureBuilder.add(OBJECTID);
                    featureBuilder.add(LOCAL);
                    featureBuilder.add(SUBSCRIB);
                    featureBuilder.add(INSURANCE);
                    featureBuilder.add(GENDER);
                    featureBuilder.add(AGE);
                    featureBuilder.add(N_SI_NUMER);
                    featureBuilder.add(N_SI_DENO);
                    featureBuilder.add(N_SI_IV);
                    featureBuilder.add(SI_IV);
                    featureBuilder.add(YEAR);
                    SimpleFeature feature = featureBuilder.buildFeature(null);
                    features.add(feature);
                }
            }
            File newFile = new File(outputShapefilePath); //shp파일이 만들어질 위치와  파일명 => properties에서 변경 가능

            Map<String, Serializable> params = new HashMap<>();
            params.put("url", newFile.toURI().toURL());
//	        params.put("create spatial index", Boolean.TRUE);
            params.put("create spatial index", true);
            ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
            ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);

            Charset cset = Charset.forName("UTF-8");

            newDataStore.createSchema(TYPE);
            // newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);
            newDataStore.setCharset(cset);
            Transaction transaction = new DefaultTransaction("create");

            String typeName = newDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);
            SimpleFeatureType SHAPE_TYPE = featureSource.getSchema();
//	        System.out.println("SHAPE:" + SHAPE_TYPE);
            System.out.println("SUCCESS! Please Check the File. :)");
            if (featureSource instanceof SimpleFeatureStore) {
                SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
                SimpleFeatureCollection collection = new ListFeatureCollection(TYPE, features);
                featureStore.setTransaction(transaction);
                try {
                    featureStore.addFeatures(collection);
                    transaction.commit();

                } catch (Exception problem) {
                    System.out.println("ERROR : "+ problem.toString());
                    problem.printStackTrace();
                    transaction.rollback();

                } finally {
                    transaction.close();
                    newDataStore.dispose();
                    reader.close();
                }
                System.exit(0); // success!
            } else {
                System.out.println(typeName + " does not support read/write access");
                System.exit(1);
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
