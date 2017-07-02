package ep.hadoop.startup;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import ep.core.Main;
import ep.hadoop.combiner.AverageCombiner;
import ep.hadoop.mapper.DataMapper;
import ep.hadoop.reducer.AverageReducer;
import ep.vo.ValueCountPair;

public class HadoopLauncher {
	public static int LaunchHadoop(Date dtInicio, Date dtFim, String metodo, String atributo) throws IOException, ClassNotFoundException, InterruptedException{
		Configuration conf = new Configuration();
		conf.set("Metodo", metodo);
		conf.set("Variavel", atributo);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		conf.set("DataInicio", sdf.format(dtInicio));
		conf.set("DataTermino", sdf.format(dtFim));
		
		
		Job job = Job.getInstance(conf, "GSOD Hadoop");
		
	    job.setJarByClass(Main.class);
	    
	    job.setMapperClass(DataMapper.class);
	    job.setCombinerClass(AverageCombiner.class);
	    job.setCombinerClass(AverageReducer.class);
	    
	    job.setOutputValueClass(ValueCountPair.class);
	    job.setOutputKeyClass(Text.class);
	    
	    FileInputFormat.addInputPath(job, new Path("CONFIGURAR"));
	    FileOutputFormat.setOutputPath(job, new Path("CONFIGURAR"));
	    
	    return job.waitForCompletion(true) ? 0 : 1;
	}
}
