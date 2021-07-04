package com.ouertani.safetyalertV2.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateCalculatorTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testAgeCalcul_17() throws ParseException {
		String DATE_FORMAT = "MM/dd/yyyy";

		LocalDate localDate;
		String birhtDate;

		localDate = LocalDate.parse("2018-11-15");
		birhtDate = "11/16/2000";
		assertEquals(17, DateCalculator.ageCalculYears(birhtDate, localDate, DATE_FORMAT));

	}

	@Test
	final void testAgeCalcul_18() throws ParseException {
		String DATE_FORMAT = "MM/dd/yyyy";

		LocalDate localDate;
		String birhtDate;

		localDate = LocalDate.parse("2018-12-16");
		birhtDate = "11/16/2000";
		assertEquals(18, DateCalculator.ageCalculYears(birhtDate, localDate, DATE_FORMAT));

	}

	@Test
	final void testAgeCalcul_19() throws ParseException {
		String DATE_FORMAT = "MM/dd/yyyy";

		LocalDate localDate;
		String birhtDate;

		localDate = LocalDate.parse("2019-11-16");
		birhtDate = "11/16/2000";
		assertEquals(19, DateCalculator.ageCalculYears(birhtDate, localDate, DATE_FORMAT));

	}

	@Test
	final void testAgeCalcul_Exactly_18() throws ParseException {
		String DATE_FORMAT = "MM/dd/yyyy";

		LocalDate localDate;
		String birhtDate;

		localDate = LocalDate.parse("2018-11-16");
		birhtDate = "11/16/2000";
		assertEquals(18, DateCalculator.ageCalculYears(birhtDate, localDate, DATE_FORMAT));

	}

}
