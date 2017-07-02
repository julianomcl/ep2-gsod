package ep.helper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateHelper {
	public static Date LocalDateToDate(LocalDate localDate){
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
}
