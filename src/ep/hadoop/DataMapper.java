package ep.hadoop;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import ep.parser.GsodParser;
import ep.vo.DayDataVO;
import ep.vo.ValueCountPair;

public class DataMapper extends Mapper<Object, Text, Text, ValueCountPair> {

	private Text outText = new Text();
	private ValueCountPair pair = new ValueCountPair();

	@Override
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		DayDataVO vo = null;

		try {
			vo = GsodParser.parseText(value);
			if (vo == null)
				return;
		} catch (ParseException e) {
			throw new IOException("Erro ao ler linha " + value.toString());
		}

		Configuration conf = context.getConfiguration();
		String variavel = conf.get("Variavel").toUpperCase();
		String metodo = conf.get("Metodo").toUpperCase();
		String sDtInicio = conf.get("DataInicio");
		String sDtFim = conf.get("DataTermino");

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date dtInicio = null;
		Date dtFim = null;
		try {
			dtInicio = sdf.parse(sDtInicio);
			dtFim = sdf.parse(sDtFim);
		} catch (ParseException e) {
			throw new IOException(
					"Datas em formato invalido. Linha: " + value.toString() + ". Datas: " + sDtInicio + " - " + sDtFim);
		}

		if (vo.getDate().before(dtInicio) || vo.getDate().after(dtFim))
			return;

		Float valor = getValor(vo, variavel);

		if (valor == Float.MAX_VALUE)
			return;

		String data = getPeriodo(vo, metodo);

		outText.set(data);
		pair.set(valor, 1);
		context.write(outText, pair);

	}

	private String getPeriodo(DayDataVO vo, String metodo) throws IOException {
		String t = null;
		SimpleDateFormat sdf = null;
		if (metodo.equals("YA")) {
			sdf = new SimpleDateFormat("yyyy");
		} else if (metodo.equals("MA")) {
			sdf = new SimpleDateFormat("MMyyyy");
		} else if (metodo.equals("WA")) {
			sdf = new SimpleDateFormat("wwyyyy");
		} else {
			throw new IOException("METODO INVALIDO: " + metodo);
		}

		t = sdf.format(vo.getDate());
		return t;
	}

	private Float getValor(DayDataVO vo, String variavel) throws IOException {
		switch (variavel) {
		case "TEMP":
			return vo.getTemp();

		case "DEWP":
			return vo.getDewp();

		case "SLP":
			return vo.getSlp();

		case "STP":
			return vo.getStp();

		case "VISIB":
			return vo.getVisib();

		case "WDSP":
			return vo.getWdsp();

		case "MXSPD":
			return vo.getMxsp();

		case "GUST":
			return vo.getGust();

		case "MAX":
			return vo.getMax();

		case "MIN":
			return vo.getMin();

		case "PRCP":
			return vo.getPrcp();

		case "SNDP":
			return vo.getSndp();
		}

		throw new IOException("PARAMETRO NAO ENCONTRADO: " + variavel);
	}
}
