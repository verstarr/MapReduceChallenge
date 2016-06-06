package org.myorg;

import java.io.IOException;
import java.text.ParseException;
import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;

public class TrueCar extends Configured implements Tool {

  private static final Logger LOG = Logger.getLogger(TrueCar.class);

  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new TrueCar(), args);
    System.exit(res);
  }

  public int run(String[] args) throws Exception {
	Configuration conf = getConf();  
	conf.set("mapreduce.output.textoutputformat.separator", ",");
	  
    Job job = Job.getInstance(conf, "truecar");
    job.setJarByClass(this.getClass());
    // Use TextInputFormat, the default unless job.setInputFormatClass is used
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    return job.waitForCompletion(true) ? 0 : 1;
  }
  
  public static String dateConverter(String day) {
	  Date date = null;
	  String dateStr = "";
	    
	  SimpleDateFormat oldFormat = new SimpleDateFormat("MM/dd/yy");
	  SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
	  try {
	    date = oldFormat.parse(day);
		dateStr = newFormat.format(date);
	  }
	  catch (ParseException e) {
		  e.printStackTrace();
	  }
	  return dateStr;
  }

  public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);

    // Key
    private Text pageview = new Text();

    public void map(LongWritable offset, Text lineText, Context context)
        throws IOException, InterruptedException {
    	try {
    		if (lineText.toString().contains("event_date"))
    			return;
			else {
				String[] line = lineText.toString().split(",");
			    String day = line[0].split(" ")[0];
			    String ab_test_campaign = line[3];
			    String ab_test_variant = line[4];
			    
			      
			    if (!ab_test_campaign.equalsIgnoreCase("")) {
			    	pageview.set(dateConverter(day) + "," + ab_test_campaign + "," + ab_test_variant);
			    	context.write(pageview, one);
			    }
			}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
  }

  public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
    @Override
    public void reduce(Text pageview, Iterable<IntWritable> counts, Context context)
        throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable count : counts) {
        sum += count.get();
      }
      context.write(pageview, new IntWritable(sum));
    }
  }
}
