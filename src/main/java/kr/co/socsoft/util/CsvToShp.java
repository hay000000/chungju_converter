package kr.co.socsoft.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class CsvToShp {
	 public static void main(String[] args) throws Exception {
		 
		String path = System.getProperty("user.dir");
			
		String resource = path + "/config/global.properties";
		    
		Properties prop = new Properties();
		
		prop.load(new InputStreamReader(new FileInputStream(resource)));
		
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        
        List<SimpleFeature> features = new ArrayList<>();
        

        SimpleFeatureType TYPE = createFeatureType();
        
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        File file = new File(prop.getProperty("result.path"));
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),Charset.forName("UTF-8")));
        
        try {
            /* First line of the data file is the header */
            String line = reader.readLine();
            System.out.println("Header: " + line);

            for (line = reader.readLine(); line != null; line = reader.readLine()) {
            	System.out.println(line);
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
	        File newFile = new File(prop.getProperty("output.shapefile.path")); 
	        
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
	        System.out.println("SHAPE:" + SHAPE_TYPE);
	        System.out.println("end");
	        if (featureSource instanceof SimpleFeatureStore) { 
	            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
	            SimpleFeatureCollection collection = new ListFeatureCollection(TYPE, features); 
	            featureStore.setTransaction(transaction);
	            try {
	                featureStore.addFeatures(collection);
	                transaction.commit();
	
	            } catch (Exception problem) {
	                problem.printStackTrace();
	                transaction.rollback();
	
	            }
	            finally {
	                transaction.close();
	                newDataStore.dispose();
	                reader.close();
	            }
	            System.exit(0); // success!
	        } else {
	            System.out.println(typeName + " does not support read/write access");
	            System.exit(1);
	        }
	   }catch(MalformedURLException ex){
		   ex.printStackTrace();
	   }catch(IOException ex) {
		   ex.printStackTrace();
	   }catch (ParseException e) {
	       e.printStackTrace();
	   }
	}

	 
	 public static SimpleFeatureType createFeatureType() throws IOException {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("the_geom");
        builder.setCRS(DefaultGeographicCRS.WGS84); // <- Coordinate reference system

        // add attributes in order
        builder.add("the_geom", MultiPolygon.class);
        builder.add("BASE_DATE",   Integer.class);
        builder.add("ADM_DR_CD",   String.class);
        builder.add("ADM_DR_NM",   String.class);
        builder.add("OBJECTID",   Integer.class);
        builder.add("LOCAL",   String.class);
        builder.add("SUBSCRIB",   String.class);
        builder.add("INSURANCE",   String.class);
        builder.add("GENDER",   String.class);
        builder.add("AGE",   String.class);
        builder.add("N_SI_NUMER",   String.class);
        builder.add("N_SI_DENO",  String.class);
        builder.add("N_SI_IV",   String.class);
        builder.add("SI_IV",   String.class);
        builder.add("YEAR",   String.class);
        final SimpleFeatureType LOCATION = builder.buildFeatureType();

        return LOCATION;
    }
	 
}
