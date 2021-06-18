package com.ouertani.safetyalertV2.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateCalculator {

	private static final Logger logger = LogManager.getLogger("DateCalculator");

	public static int ageCalculYears(String birthDateString, LocalDate localDateNow, String DATE_FORMAT)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date birthDate = sdf.parse(birthDateString);
		return Period
				.between(birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), localDateNow)
				.getYears();
	}

}