package ep.hadoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import ep.vo.ValueCountPair;

public class StdDevReducer extends Reducer<Text, ValueCountPair, Text, FloatWritable> {

	@Override
	protected void reduce(Text key, Iterable<ValueCountPair> values, Context context)
			throws IOException, InterruptedException {
		float value = 0;
		float average = 0;
		int count = 0;
		

		Path pt = new Path("hdfs://localhost:9000/usr/local/hadoop/average/part-r-00000");
		FileSystem fs = FileSystem.get(new Configuration());
		BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt)));

		String line;
		line = br.readLine();
		while (line != null) {
			String[] keyvalue = line.split("\\s+");
			if (keyvalue[0].contains(key + "")) {
				average = Float.parseFloat(keyvalue[1]);
			}
			line = br.readLine();
		}

		for (ValueCountPair valuePair : values) {
			value += Math.pow((valuePair.getValue().get() - average), 2);
			count += valuePair.getCount().get();
		}

		float stdev = (float) Math.sqrt(value / (count - 1));

		FloatWritable a = new FloatWritable(stdev);
		context.write(key, a);
	}
}