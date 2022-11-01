# CSV TO SHAPE
# maven install
~~~
mvn clean install
~~~


# 빌드
~~~
mvn clean compile assembly:single
~~~
# 실행 방법 (샘플)
~~~
java -jar target/ShpToCsv-0.0.1-SNAPSHOT-jar-with-dependencies.jar -i "<내프로젝트루트경로>/sampleData/행정동경계.shp" -o "<내프로젝트루트경로>/sampleData/output/ShpToCsv.csv"
java -jar target/ShpToCsv-0.0.1-SNAPSHOT-jar-with-dependencies.jar -i "/Users/askurios8/temp/ShpToCsv/sampleData/border.shp" -o "/Users/askurios8/temp/ShpToCsv/sampleData/output/ShpToCsv.csv"
java -jar target/ShpToCsv-0.0.1-SNAPSHOT-jar-with-dependencies.jar -i "./sampleData/border.shp" -o "./sampleData/output/ShpToCsv.csv" 

java -jar target/ShpToCsv_jcommander-0.0.1-SNAPSHOT-jar-with-dependencies.jar -si "./sampleData/border.shp" -ci "./sampleData/2017.csv" -so "./sampleData/output/indicator.shp"
java -jar -Dfile.encoding=UTF-8 target/ShpToCsv_jcommander-0.0.1-SNAPSHOT-jar-with-dependencies.jar -si "./sampleData/border.shp" -ci "./sampleData/2017.csv" -so "./sampleData/outputfile/indicator.shp"
~~~