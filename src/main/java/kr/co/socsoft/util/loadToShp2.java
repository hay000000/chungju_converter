// loadToShp backup file


package kr.co.socsoft.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

public class loadToShp2 {
	
	private static SimpleFeatureIterator simpleFeatureiterator;
		
	static FileDataStore store;
	
	
	public void DefaultLoad(String filePath) throws IOException {
		File dataFile = new File(filePath);
		
		dataFile.setReadOnly();
		
		store = FileDataStoreFinder.getDataStore(dataFile);
		
		SimpleFeatureSource source = store.getFeatureSource();
	    SimpleFeatureCollection featureCollection = source.getFeatures();
	    simpleFeatureiterator = featureCollection.features();
	}
	
	public SimpleFeatureIterator getlter() {
	return simpleFeatureiterator;
}
	
//	public SimpleFeatureIterator getlter() {
//	    try{
//	    while(simpleFeatureIterator.hasNext())
//	    {
//	
//	        SimpleFeature f = simpleFeatureIterator.next();
//	
//	       System.out.println("@@@"+f.getID());
//	    }
//	    }
//	    catch(Exception ex){
//	        ex.printStackTrace();
//	    }
//	    finally{
//	        simpleFeatureIterator.close();
//	        store.dispose();
//	
//	    }
//	    return simpleFeatureiterator;
//	}
//	public static void main(String[] args) {
//	    try {
//	        for(int i=0;i<5;i++){
//	        System.out.println("reading "+ i);
//	
//	        GeoOpen.openShapeFile();
//	        GeoOpen.iterate();
//	
//	        System.out.println("Stopped");
//	        //Thread.sleep(1000);
//	        }
//	
//	    } catch (Exception e) {
//	        // TODO Auto-generated catch block
//	        e.printStackTrace();
//	    }
//	}


//	public void DefaultLoad(String filePath) {
//		File dataFile = new File(filePath);
//		ShapefileDataStore store;
//		
//		try {
//			store = new ShapefileDataStore(new URL("file://"+dataFile));
//			store.setCharset(Charset.forName("EUC-KR"));
//			
//			SimpleFeatureSource source = store.getFeatureSource(store.getTypeNames()[0]);
//			SimpleFeatureCollection featureCollection = source.getFeatures();
//			simpleFeatureiterator = featureCollection.features();
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
//	}
//	public SimpleFeatureIterator getlter() {
//		return simpleFeatureiterator;
//	}
}
