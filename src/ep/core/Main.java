package ep.core;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import ep.hadoop.combiner.AverageCombiner;
import ep.hadoop.mapper.DataMapper;
import ep.hadoop.reducer.AverageReducer;
import ep.vo.ValueCountPair;
public class Main extends Configured implements Tool{
	
	
	//public static void main(String[] args) throws Exception{
	//	int exitCode = ToolRunner.run(new Main(), args);
	//	System.exit(exitCode);
	//}

	@Override
	public int run(String[] args) throws Exception {
	    Job job = Job.getInstance(getConf(), "gsod hadoop");
	    
	    job.setJarByClass(Main.class);
	    
	    job.setMapperClass(DataMapper.class);
	    job.setCombinerClass(AverageCombiner.class);
	    job.setCombinerClass(AverageReducer.class);
	    
	    job.setOutputValueClass(ValueCountPair.class);
	    job.setOutputKeyClass(Text.class);
	    
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	    
	    return job.waitForCompletion(true) ? 0 : 1;
	}
	
}
