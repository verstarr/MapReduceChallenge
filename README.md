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
