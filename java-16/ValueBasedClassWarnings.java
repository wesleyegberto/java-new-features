/**
 * Error: `java --enable-preview --source 16 ValueBasedClassWarnings.java`
 * Only warning: `java -XX:+UnlockDiagnosticVMOptions -XX:DiagnoseSyncOnValueBasedClasses=2 --enable-preview --source 16 ValueBasedClassWarnings.java`
 *
 * From docs:
 * @ValueBased is applied to the following declarations in the Java Platform API and the JDK:
 * - The primitive wrapper classes in java.lang;
 * - The class java.lang.Runtime.Version;
 * - The "optional" classes in java.util: Optional, OptionalInt, OptionalLong, and OptionalDouble;
 * - Many classes in the java.time API: Instant, LocalDate, LocalTime, LocalDateTime, ZonedDateTime, ZoneId,
 *     OffsetTime, OffsetDateTime, ZoneOffset, Duration, Period, Year, YearMonth, and MonthDay, and, in
 *     java.time.chrono: MinguoDate, HijrahDate, JapaneseDate, and ThaiBuddhistDate;
 * - The interface java.lang.ProcessHandle and its implementation classes;
 * - The implementation classes of the collection factories in java.util: List.of, List.copyOf, Set.of, Set.copyOf,
 *   Map.of, Map.copyOf, Map.ofEntries, and Map.entry.
 */
public class ValueBasedClassWarnings {
	public static void main(String[] args) {
		var d = new Double(10);

		synchronized (d) {
			System.out.println("Synchronized block using a value-based object");
		}
	}

}
