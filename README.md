# TrueCarMapReduceChallenge

## Map / Reduce assignment
### COMPLETED
First read up on Map / Reduce framework and Java Mapper / Reducer tutorial to get familiar with the framework.  

Challenge details:
We have data that comes in csv format where each row has the following structure:
Event_date, visitor_id, page_name, ab_test_campaign , ab_test_variant, device_type, car_make
Each row represents a click of a user on a website. (check attached input csv file for details)  

Write a Map/Reduce Java job that would give us the following overview:
The number of page views grouped by: day , ab_test_campaign, ab_test_variant of visitors that have non null/empty ab_test_campaign Day ab_test_campaign ab_test_variant page_views 2016-05- 16 ui_change A 1500 2016-05- 16 ui_change B 300

Here is the signature of the Mapper class and its void map() method.:

Mapper signature will look like that (mapper output (?) is up to your implementation):

public static class Map extends Mapper&lt;LongWritable, Text, ?, ?&gt; {

Map method:

public void map(LongWritable key, Text value, Context context) throws IOException,

InterruptedException

The output of the reducer should be in csv (comma delimited format) following the structure of

the table above.

For your implementations you need at least a Mapper and Reducer java files. Assume that the

input is in HDFS folders with multiple csv files (with millions of rows each)

Please submit the java files (and any other files that might be part of your solution)  

How to run:  
My solution was developed on Cloudera's QuickStart Single Node Hadoop VM on VirtualBox. To run my solution, you would have to be in the vm (http://www.cloudera.com/downloads/quickstart_vms/5-7.html) and navigate to the /TrueCarMapReduceChallenge directory and run ./true_car. I have included a make file with some commands for convenience. TrueCar.java includes a Mapper and Reducer that reads from a csv formatted file and returns the Day, ab_test_campaign, ab_test_variant, and number of pages views in output/map_reduce_output_data.csv.

run: jar
	hadoop fs -rm -f -r  /user/cloudera/truecar/output
	hadoop jar truecar.jar org.myorg.TrueCar /user/cloudera/truecar/input /user/cloudera/truecar/output

run_caseSensitive: jar
	hadoop fs -rm -f -r  /user/cloudera/truecar/output
	hadoop jar truecar.jar org.myorg.TrueCar -Dtruecar.case.sensitive=true /user/cloudera/truecar/input /user/cloudera/truecar/output 

run_stopwords: jar stopwords
	hadoop fs -rm -f -r  /user/cloudera/truecar/output
	hadoop jar truecar.jar org.myorg.TrueCar /user/cloudera/truecar/input /user/cloudera/truecar/output -skip /user/cloudera/truecar/stop_words.text

compile: build/org/myorg/TrueCar.class

jar: truecar.jar

truecar.jar: build/org/myorg/TrueCar.class
	jar -cvf truecar.jar -C build/ .

build/org/myorg/TrueCar.class: TrueCar.java
	mkdir -p build
	javac -cp /usr/lib/hadoop/*:/usr/lib/hadoop-mapreduce/* TrueCar.java -d build -Xlint

clean:
	rm -rf build truecar.jar

data:
	hadoop fs -rm -f -r /user/cloudera/truecar/input
	hadoop fs -mkdir /user/cloudera/truecar
	hadoop fs -mkdir /user/cloudera/truecar/input
	hadoop fs -put map_reduce_input_data.csv /user/cloudera/wordcount/input

showResult:
	hadoop fs -cat /user/cloudera/truecar/output/*
	
stopwords:
	hadoop fs -rm -f /user/cloudera/truecar/stop_words.text
	echo -e "a\\nan\\nand\\nbut\\nis\\nor\\nthe\\nto\\n.\\n," >stop_words.text
	hadoop fs -put stop_words.text /user/cloudera/truecar/

