package ep.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.hadoop.io.Text;

import ep.vo.DayDataVO;

public class GsodParser {
	public static DayDataVO parseText(Text value) throws ParseException {
		String text = value.toString();

		if (text.startsWith("STN"))
			return null;

		DayDataVO vo = new DayDataVO();

		vo.setStationNumber(Integer.parseInt(text.substring(0, 5)));
		vo.setWban(Integer.parseInt(text.substring(7, 11)));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		vo.setDate(sdf.parse(text.substring(14, 22)));
		vo.setTemp(Float.parseFloat(text.substring(25, 30)));
		vo.setDewp(Float.parseFloat(text.substring(35, 41)));
		vo.setSlp(Float.parseFloat(text.substring(46, 52)));
		vo.setStp(Float.parseFloat(text.substring(57, 63)));
		vo.setVisib(Float.parseFloat(text.substring(68, 73)));
		vo.setWdsp(Float.parseFloat(text.substring(78, 83)));
		vo.setMxsp(Float.parseFloat(text.substring(88, 93)));
		vo.setGust(Float.parseFloat(text.substring(94, 100)));
		vo.setMax(Float.parseFloat(text.substring(102, 108)));
		vo.setMin(Float.parseFloat(text.substring(110, 116)));
		vo.setPrcp(Float.parseFloat(text.substring(118, 123)));
		vo.setSndp(Float.parseFloat(text.substring(125, 130)));

		return vo;
	}
}
