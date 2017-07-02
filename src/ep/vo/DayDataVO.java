package ep.vo;

import java.util.Date;

public class DayDataVO {
	private int stationNumber;
	private int wban;
	private Date date;
	private float temp;
	private float dewp;
	private float slp;
	private float stp;
	private float visib;
	private float wdsp;
	private float mxsp;
	private float gust;
	private float max;
	private float min;
	private float prcp;
	private float sndp;
	private int frshtt;

	public int getStationNumber() {
		return stationNumber;
	}

	public void setStationNumber(int stationNumber) {
		this.stationNumber = stationNumber;
	}

	public int getWban() {
		return wban;
	}

	public void setWban(int wban) {
		this.wban = wban;
	}

	public float getTemp() {
		return temp;
	}

	public void setTemp(float temp) {
		if (Math.abs(temp - 9999.9f) < 0.1f)
			this.temp = 0.0f;
		else
			this.temp = temp;
	}

	public float getDewp() {
		return dewp;
	}

	public void setDewp(float dewp) {
		if (Math.abs(dewp - 9999.9f) < 0.1f)
			this.dewp = Float.MAX_VALUE;
		else
			this.dewp = dewp;
	}

	public float getSlp() {
		return slp;
	}

	public void setSlp(float slp) {
		if (Math.abs(slp - 9999.9f) < 0.1f)
			this.slp = Float.MAX_VALUE;
		else
			this.slp = slp;
	}

	public float getStp() {
		return stp;
	}

	public void setStp(float stp) {
		if (Math.abs(stp - 9999.9f) < 0.1f)
			this.stp = Float.MAX_VALUE;
		else
			this.stp = stp;
	}

	public float getVisib() {
		return visib;
	}

	public void setVisib(float visib) {
		if (Math.abs(visib - 999.9f) < 0.1f)
			this.visib = Float.MAX_VALUE;
		else
			this.visib = visib;
	}

	public float getWdsp() {
		return wdsp;
	}

	public void setWdsp(float wdsp) {
		if (Math.abs(wdsp - 999.9f) < 0.1f)
			this.wdsp = Float.MAX_VALUE;
		else
			this.wdsp = wdsp;
	}

	public float getMxsp() {
		return mxsp;
	}

	public void setMxsp(float mxsp) {
		if (Math.abs(mxsp - 999.9f) < 0.1f)
			this.mxsp = Float.MAX_VALUE;
		else
			this.mxsp = mxsp;
	}

	public float getGust() {
		return gust;
	}

	public void setGust(float gust) {
		if (Math.abs(gust - 999.9f) < 0.1f)
			this.gust = Float.MAX_VALUE;
		else
			this.gust = gust;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		if (Math.abs(max - 9999.9f) < 0.1f)
			this.max = Float.MAX_VALUE;
		else
			this.max = max;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		if (Math.abs(min - 9999.9f) < 0.1f)
			this.min = Float.MAX_VALUE;
		else
			this.min = min;
	}

	public float getPrcp() {
		return prcp;
	}

	public void setPrcp(float prcp) {
		if (Math.abs(prcp - 99.9f) < 0.1f)
			this.prcp = Float.MAX_VALUE;
		else
			this.prcp = prcp;
	}

	public float getSndp() {
		return sndp;
	}

	public void setSndp(float sndp) {
		this.sndp = sndp;
	}

	public int getFrshtt() {
		return frshtt;
	}

	public void setFrshtt(int frshtt) {
		this.frshtt = frshtt;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
