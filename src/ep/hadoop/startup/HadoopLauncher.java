package ep.hadoop.startup;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import ep.core.Main;
import ep.hadoop.combiner.AverageCombiner;
import ep.hadoop.mapper.DataMapper;
import ep.hadoop.reducer.AverageReducer;
import ep.vo.ValueCountPair;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class HadoopLauncher extends Configured implements Tool {
	
	private Job job;
	
	private EventHandler<ActionEvent> action;
	
	private Timeline workerThread;
	
	public HadoopLauncher(EventHandler<ActionEvent> action){
		this.action = action;
		workerThread = new Timeline(new KeyFrame(Duration.millis(250), action));
		workerThread.setCycleCount(Timeline.INDEFINITE);
	}
	
	public float getMapProgress(){
		if(job == null)
			return 0.0f;
		
		try {
			return job.mapProgress();
		} catch (Exception e) {
			return -1.0f;
		}
	}
	
	public float getReduceProgress(){
		if(job == null)
			return 0.0f;
		
		try{
			return job.reduceProgress();
		}catch(Exception ex){
			return -1.0f;
		}
	}
	
	public int LaunchHadoop(Date dtInicio, Date dtFim, String metodo, String atributo) throws Exception{
		String[] args = new String[4];
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		args[0] = sdf.format(dtInicio);
		args[1] = sdf.format(dtFim);
		args[2] = metodo;
		args[3] = atributo;
		
		return run(args);
		
	}

	public EventHandler<ActionEvent> getAction() {
		return action;
	}

	public void setAction(EventHandler<ActionEvent> action) {
		this.action = action;
		workerThread.stop();
		workerThread.setOnFinished(action);
		workerThread.setCycleCount(Timeline.INDEFINITE);
		workerThread.play();
	}

	@Override
	public int run(String[] arg0) throws Exception {
		Configuration conf = new Configuration();
		conf.set("DataInicio", arg0[0]);
		conf.set("DataTermino", arg0[1]);
		conf.set("Metodo", arg0[2]);
		conf.set("Variavel", arg0[3]);
			
		
		job = Job.getInstance(conf, "GSOD Hadoop");
		
	    job.setJarByClass(HadoopLauncher.class);
	    
	    job.setMapperClass(DataMapper.class);
	    job.setCombinerClass(AverageCombiner.class);
	    job.setReducerClass(AverageReducer.class);
	    
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(ValueCountPair.class);
	    
	    job.setOutputValueClass(ValueCountPair.class);
	    job.setOutputKeyClass(Text.class);
	    
	    FileInputFormat.addInputPath(job, new Path("/usr/local/hadoop/gsod-partial/1939/1939"));
	    FileOutputFormat.setOutputPath(job, new Path("/usr/local/hadoop/average/output"));
	    
	    workerThread.play();
	    
	    return job.waitForCompletion(true) ? 0 : 1;
	}
	
}
