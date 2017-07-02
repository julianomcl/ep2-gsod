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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class HadoopLauncher {
	
	private Job job;
	
	private EventHandler<ActionEvent> action;
	
	private Timeline workerThread;
	
	public HadoopLauncher(EventHandler<ActionEvent> action){
		this.action = action;
		workerThread = new Timeline(new KeyFrame(Duration.seconds(2), action));
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
	
	public int LaunchHadoop(Date dtInicio, Date dtFim, String metodo, String atributo) throws IOException, ClassNotFoundException, InterruptedException{
		Configuration conf = new Configuration();
		conf.set("Metodo", metodo);
		conf.set("Variavel", atributo);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		conf.set("DataInicio", sdf.format(dtInicio));
		conf.set("DataTermino", sdf.format(dtFim));
		
		
		job = Job.getInstance(conf, "GSOD Hadoop");
		
	    job.setJarByClass(Main.class);
	    
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
	
	
	
}
