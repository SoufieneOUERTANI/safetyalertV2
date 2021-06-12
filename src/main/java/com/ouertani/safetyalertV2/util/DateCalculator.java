package com.ouertani.safetyalertV2.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class DateCalculator {

	public static int ageCalculYears(String birhtDate, LocalDate localDateNow, String DATE_FORMAT)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date birthDate = sdf.parse(birhtDate);
		// Date currentDate = sdf.parse(sdf.format(new Date()));
		System.out.println(Period
				.between(birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), localDateNow));
		return Period
				.between(birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), localDateNow)
				.getYears();
	}

}