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

