package kr.co.socsoft.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


public class CSVTest2 {
	
	public static void sample(String filePath, String filePath2, String filePath3) {
		BufferedReader br = null;
		ArrayList<String[]> arrayList = new ArrayList<>();
		String nextLine;
		String[] lineArr;
		String split = "\\|";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
			while ((nextLine = br.readLine()) != null) {
				lineArr = nextLine.split(split);
				arrayList.add(lineArr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		CSVReader reader = null;
		ArrayList<String[]> arrayList2 = new ArrayList<>();
		
		try {
			reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath2), "EUC-KR"));
//			reader = new CSVReader(new FileReader(filePath2));
			while ((lineArr = reader.readNext()) != null) {
//			System.out.println(Arrays.toString(lineArr));
				arrayList2.add(lineArr);
				
//				System.out.println(Arrays.toString(lineArr));
			}
		} catch (CsvValidationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
		    System.out.println("ERROR : " + "파일을 찾을 수 없습니다." + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int index = 3;
		String[] splitStr;
		for (int i = 0; i < arrayList.size(); i++) {
			for (int j = 0; j < arrayList2.size(); j++) {
				
				if(i==0) {
					if(j==0) {
						List<String> list = new ArrayList<>(Arrays.asList(arrayList.get(i)));
						for(String str : arrayList2.get(j)) {
							list.add(str);
						}
						arrayList.set(i, list.toArray(arrayList.get(i)));
//						System.out.println(Arrays.toString(arrayList.get(i)));
					}
				} else {
					if(j==0) continue;
//					System.out.println(arrayList2.get(j)[0]);
					String a = arrayList2.get(j)[0].replaceAll("제", "");
					String b = a.toString().replaceAll("[.]", "・");
//					System.out.println(a);
//					System.out.println(b);
//					splitStr = arrayList2.get(j)[0].split("\\ ");
					splitStr = b.toString().split("\\ ");
					
//					System.out.println("arrayList.get(i)[index]="+arrayList.get(i)[index]);
//					System.out.println("splitStr="+splitStr[splitStr.length-1]);
					
				    if(arrayList.get(i)[index].equals(splitStr[splitStr.length-1])) {
//						System.out.println(splitStr[splitStr.length-1]);
//					System.out.println("arrayList.get(i)[index]="+arrayList.get(i)[index]);
//					System.out.println("splitStr="+splitStr[splitStr.length-1]);
//						System.out.println(arrayList.get(i)[index].equals(splitStr[splitStr.length-1]));
				        List<String> list = new ArrayList<>(Arrays.asList(arrayList.get(i)));
				        for(String str : arrayList2.get(j)) {
				            list.add(str);
				        }
				        arrayList.set(i, list.toArray(arrayList.get(i)));
//						System.out.println(arrayList.size());
				    }
				}
				
			}
		}
		
		
		BufferedWriter bw = null;
		String NEWLINE = System.lineSeparator(); // 줄바꿈(\n)
		
		try {
			bw = new BufferedWriter(new FileWriter(filePath3));
			
			for(String[] str : arrayList) {
				bw.write(String.join("|", str));

				bw.write(NEWLINE);
			}
			
			bw.flush();
			bw.close();
		} catch (Exception e) {
		    System.out.println("ERROR : 지역 확인222" + e.toString());
//		    System.out.pr intln("ERROR : "+e.toString());
			e.printStackTrace();
		}
//		System.out.println("end");
	}

//	public static void main(String args[]) throws IOException {
//		String path = System.getProperty("user.dir"); //사용자 디렉토리 정보
//		String resource = path + "/config/global.properties"; //properties 위치 설정
//		    
//		Properties prop = new Properties();
//		
//		prop.load(new InputStreamReader(new FileInputStream(resource)));// properties 읽어오기
//		
//		sample( prop.getProperty("output.csvfile.path"),prop.getProperty("indicatorfile.path"),prop.getProperty("result.path")); // 원본파일, 비교할파일, 생성할 파일
//	}
}