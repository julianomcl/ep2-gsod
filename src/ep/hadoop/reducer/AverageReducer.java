package ep.hadoop.reducer;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import ep.vo.ValueCountPair;

public class AverageReducer extends Reducer<Text, ValueCountPair, Text, FloatWritable> {
	private FloatWritable average = new FloatWritable();

	@Override
	protected void reduce(Text key, Iterable<ValueCountPair> values, Context context)
			throws IOException, InterruptedException {
		float value = 0;
		int count = 0;
		for (ValueCountPair valuePair : values) {
			value += valuePair.getValue().get();
			count += valuePair.getCount().get();
		}
		
		average.set((float) (value / count));
		System.out.println("******************************************************");
		System.out.println("AverageReducer: " + key + " - " + average);
		System.out.println("******************************************************");
		context.write(key, average);
	}
}
