package com.github.wesleyegberto.api.datetime;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Wesley Egberto
 */
public class ClockTest {
	public static void main(String[] args) {
		Clock currentClock = Clock.systemUTC();
		System.out.println("Instant = " + currentClock.instant());
		System.out.println("Ms = " + currentClock.millis());

		// Without time
		LocalDate localDate = LocalDate.now();
		LocalDate clockDate = LocalDate.now(currentClock);

		System.out.println("localDate = " + localDate);
		System.out.println("clockDate = " + clockDate);

		// Without date
		LocalTime localTime = LocalTime.now();
		LocalTime clockTime = LocalTime.now(currentClock);

		System.out.println("localTime = " + localTime);
		System.out.println("clockTime = " + clockTime);

		// Without Time-Zone
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDateTime clockDateTime = LocalDateTime.now(currentClock);

		System.out.println("localDateTime = " + localDateTime);
		System.out.println("clockDateTime = " + clockDateTime);

		// Zoned Date Time
		ZonedDateTime zonedDateTime = ZonedDateTime.now();
		ZonedDateTime clockZoned = ZonedDateTime.now(currentClock);
		ZonedDateTime zonedDatetimeFromZone = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));

		System.out.println("zonedDateTime = " + zonedDateTime);
		System.out.println("clockZoned = " + clockZoned);
		System.out.println("zonedDatetimeFromZone = " + zonedDatetimeFromZone);

		final Duration duration = Duration.between(zonedDateTime, zonedDatetimeFromZone);
		System.out.println("Duration in nanos: " + duration.toNanos());
		System.out.println("Duration in millis: " + duration.toMillis());
	}
}
