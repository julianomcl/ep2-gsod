package ep.vo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class ValueCountPair implements Writable, WritableComparable<ValueCountPair> {
	private FloatWritable value;
	private FloatWritable count;

	public ValueCountPair() {
		set(new FloatWritable(0), new FloatWritable(0));
	}

	public ValueCountPair(float value, float count) {
		set(new FloatWritable(value), new FloatWritable(count));
	}

	public void set(float value, float count) {
		this.value.set(value);
		this.count.set(count);
	}

	public void set(FloatWritable value, FloatWritable count) {
		this.value = value;
		this.count = count;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		value.write(out);
		count.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		value.readFields(in);
		count.readFields(in);
	}

	@Override
	public int compareTo(ValueCountPair other) {
		int compareVal = this.value.compareTo(other.getValue());
		if (compareVal != 0) {
			return compareVal;
		}
		return this.count.compareTo(other.getCount());
	}

	public static ValueCountPair read(DataInput in) throws IOException {
		ValueCountPair averagingPair = new ValueCountPair();
		averagingPair.readFields(in);
		return averagingPair;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ValueCountPair that = (ValueCountPair) o;
		if (!count.equals(that.count))
			return false;
		if (!value.equals(that.value))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = value.hashCode();
		result = 163 * result + count.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "ValueCountPair{" + "value=" + value + ", count=" + count + '}';
	}

	public FloatWritable getValue() {
		return value;
	}

	public FloatWritable getCount() {
		return count;
	}
}
