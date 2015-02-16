package org.fionaarmstrong;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCountWithCombiner extends Configured implements Tool{
/*
 * Usage:
 * hadoop jar wordCount.jar WordCountWithCombiner inputPath outputPath
 * Run it without reducer, this way you can see the map output that's not reduced
 * hadoop jar wordCount.jar WordCountWithCombiner -D mapred.reduce.task=0 inputPath outputPath
 * 
 */
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new Configuration(), new WordCountWithCombiner(), args);
		System.exit(exitCode);
	}

	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		
		Job job = new Job(conf, "MyJob");
		job.setJarByClass(WordCount.class);
		job.setJobName("Word Count");
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(WordCountMapper.class);
		job.setCombinerClass(WordCountReducer.class);
		job.setReducerClass(WordCountReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		return job.waitForCompletion(true) ? 0 : 1;
	}
	
}
