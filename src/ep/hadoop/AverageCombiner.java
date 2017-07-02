package ep.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import ep.vo.ValueCountPair;

public class AverageCombiner extends Reducer<Text, ValueCountPair, Text, ValueCountPair> {
	private ValueCountPair pair = new ValueCountPair();

	@Override
	protected void reduce(Text key, Iterable<ValueCountPair> values, Context context)
			throws IOException, InterruptedException {
		float value = 0;
		int count = 0;
		for (ValueCountPair valuePair : values) {
			value += valuePair.getValue().get();
			count += valuePair.getCount().get();
		}
		pair.set(value, count);
		context.write(key, pair);
	}
}