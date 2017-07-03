package ep.hadoop.startup;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

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
	    
	  //pegar todos os anos entre a data de inicio e termino
        DateFormat formater = new SimpleDateFormat("dd-MM-yyyy");

        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();

        try {
            beginCalendar.setTime(formater.parse(arg0[0]));
            finishCalendar.setTime(formater.parse(arg0[1]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        ArrayList<String> years = new ArrayList<String>();

        while (beginCalendar.before(finishCalendar)) {
            // adiciona um ano a data por loop
            String date = formater.format(beginCalendar.getTime()).toUpperCase();
            System.out.println(date.substring(6));
            years.add(date.substring(6));
            beginCalendar.add(Calendar.YEAR, 1);
        }
        
        //pega os paths de arquivos de acordo com os anos
        String inputPaths = ""; 
        for(String year : years) {
        	inputPaths += "/usr/local/hadoop/gsod/" + year + ",";
        }
        inputPaths = inputPaths.substring(0, inputPaths.length()-1);
        System.out.println(inputPaths);
	    
	    FileInputFormat.addInputPaths(job, inputPaths);
	    
	    String outputFolder = "/usr/local/hadoop/average/output";
	    FileOutputFormat.setOutputPath(job, new Path(outputFolder));
	    
	    File folder = new File(outputFolder);
	    String[] files = folder.list();
	    if(files != null) {
	    	for(String file : files){
		    	File currentFile = new File(folder.getPath(), file);
		    	currentFile.delete();
		    }
		    folder.delete();
	    }
	   
	    workerThread.play();
	    
	    return job.waitForCompletion(true) ? 0 : 1;
	}
	
}
