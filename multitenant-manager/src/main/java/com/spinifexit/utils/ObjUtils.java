package com.spinifexit.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Object/string utility functions
 *
 * @author Reah Espiritu
 *
 */
public class ObjUtils {


	private static final String HTML_PATTERN = "\\<[^>]*>";
	private static Pattern PATTERN = Pattern.compile(HTML_PATTERN);
	private static Random ran = new Random();

	public static final String DATE_PATTERN_SLASH_YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
	public static final String DATE_PATTERN_SLASH_YYYY_MM_DD = "yyyy/MM/dd";
	public static final String DATE_PATTERN_DASH_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN_DASH_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_PATTERN_SLASH_MM_DD_YYYY_HH_MM_SS = "MM/dd/yyyy HH:mm:ss";
	public static final String DATE_PATTERN_SLASH_MM_DD_YYYY = "MM/dd/yyyy";
	public static final String DATE_PATTERN_DASH_MM_DD_YYYY_HH_MM_SS = "MM-dd-yyyy HH:mm:ss";
	public static final String DATE_PATTERN_DASH_MM_DD_YYYY = "MM-dd-yyyy";
	public static final String DATE_PATTERN_COMMON = "MMM dd, yyyy";
	public static final String DATE_PATTERN_DASH_YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String[] DATE_PATTERNS = {
			DATE_PATTERN_SLASH_YYYY_MM_DD_HH_MM_SS,
			DATE_PATTERN_SLASH_YYYY_MM_DD,
			DATE_PATTERN_DASH_YYYY_MM_DD_HH_MM_SS, DATE_PATTERN_DASH_YYYY_MM_DD,
			DATE_PATTERN_SLASH_MM_DD_YYYY_HH_MM_SS, DATE_PATTERN_SLASH_MM_DD_YYYY,
			DATE_PATTERN_DASH_MM_DD_YYYY_HH_MM_SS, DATE_PATTERN_DASH_MM_DD_YYYY,
			DATE_PATTERN_COMMON, DATE_PATTERN_DASH_YYYY_MM_DD_T_HH_MM_SS };

	private static final String SIGNED_PDF_EXT = "_SIGNED.pdf";
	// for now these are the form tags being used in form templates
	protected static final List<String> FORM_TAGS = Arrays.asList("input", "textarea", "select");

	/**
	 * @param value
	 * @return True if string is not null and not empty
	 */
	public static boolean hasValue(String value) {
		return value != null && !value.isEmpty();
	}

	/**
	 * Convenience method to check if a map has a value.
	 *
	 * @param value
	 * @return True if map is not null and not empty
	 */
	public static boolean hasValue(Map<String, Object> value) {
		return value != null && !value.isEmpty();
	}

	/**
	 * @param value
	 * @return True if string is not null, not empty and not only a whitespace
	 */
	public static boolean hasValueWhenTrimmed(String value) {
		return hasValue(value) && !value.trim().isEmpty();
	}

	/**
	 * @param value
	 * @return True if string value is not empty and null
	 */
	public static boolean isValueNotNull(String value) {
		return hasValue(value) && !"null".equalsIgnoreCase(value.trim());
	}

	/**
	 * @param value
	 * @return True if list is not null and not empty
	 */
	public static boolean hasValue(Collection<?> value) {
		return value != null && !value.isEmpty();
	}

	/**
	 * Gets value from nested object and returns null if exception occurs
	 *
	 * @param supplier
	 * @return
	 * @param <T>
	 */
	public static <T> T get(Supplier<T> supplier) {
		try {
			return supplier.get();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param array
	 * @param index
	 * @return True if list is not null, not empty, and index exists in array
	 */
	public static boolean isIndexExistsInArray(Collection<?> value, Integer index) {
		return index != null && hasValue(value) && index <= value.size();
	}

	/**
	 * Returns true if the list is equal, regardless of order.
	 *
	 * @param <T>
	 * @param src
	 * @param tgt
	 * @return
	 */
	public static <T> boolean isListEqual(List<T> src, List<T> tgt) {
		// if one is null and not the other, then not equal
		if (src == null && tgt != null || src != null && tgt == null) {
			return false;
		}

		return new HashSet<>(src).equals(new HashSet<>(tgt));
	}

	/**
	 * @param clazz
	 * @param c
	 * @return List of cast objects
	 */
	public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
		List<T> r = new ArrayList<T>(c.size());
		for (Object o : c) {
			r.add(clazz.cast(o));
		}
		return r;
	}

	/**
	 * @param name
	 * @param path
	 * @param pathDelimiter
	 * @return true if name is in contained in path; false, otherwise
	 */
	public static boolean isInPath(String name, String path, String pathDelimiter) {
		if (hasValue(path)) {
			Set<String> names = new HashSet<>(Arrays.asList(path.split(pathDelimiter)));

			return names.contains(name);
		}

		return false;
	}


	/**
	 * Formats a date with the given date format.
	 *
	 * @param date
	 * @param dateFormat
	 * @return formatted date
	 */
	public static String formatDate(Date date, String dateFormat) {
		String dateStr = "";

		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat(dateFormat);
			dateStr = format.format(date);
		}

		return dateStr;
	}

	/**
	 * Convenience method to convert a string date to a date object.
	 *
	 * @param value
	 * @param dateFormat
	 * @return the equivalent date object or a null if the conversion failed.
	 */
	public static boolean isDateFormatted(String value, String dateFormat) {
		boolean isFormatted = false;

		if (ObjUtils.hasValue(value) && ObjUtils.hasValue(dateFormat)) {
			SimpleDateFormat format = new SimpleDateFormat(dateFormat);
			try {
				format.parse(value);
				isFormatted = true;
			} catch (ParseException e) {
				// no log on this part, for it will log every value from reporting result
			}
		}
		return isFormatted;
	}

	/**
	 * Convenience method to convert a long time stamp to a formatted date time string.
	 *
	 * @param value
	 * @param dateFormat
	 * @return the equivalent date object or a null if the conversion failed.
	 */
	public static String convertLongToDateTime(Long value, String dateFormat) {
		Date date = new Date(value);
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(date);
	}

	public static String getTimezone(String value) {
		String tz;
		if (value.contains("+")) {
			tz = value.substring(value.indexOf("+"));
		} else {
			tz = value.substring(value.lastIndexOf("-"));
		}

		return tz;
	}

	public static String formatTimeZone(String value) {
		String tz = "";
		if (!value.contains(":")) {
			tz = value.concat(":00");
		}

		return tz;
	}

	public static String deleteTimezone(String value) {
		String tz;
		if (value.contains("+")) {
			tz = value.replace(value.substring(value.indexOf("+")), "");
		} else {
			tz = value.replace(value.substring(value.lastIndexOf("-")), "");
		}

		return tz;
	}

	/**
	 * Checks whether the date is valid by converting it to a Date object. A blank or null date is considered invalid.
	 *
	 * @param date
	 * @param format
	 * @return true, if date is valid
	 */
	public static boolean isValidDate(String date, String format) {
		boolean isValid = false;

		if (ObjUtils.hasValue(date) && ObjUtils.hasValue(format)) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			try {
				sdf.parse(date);
				isValid = true;
			} catch (ParseException e) {
				// invalid date
			}
		}
		return isValid;
	}

	/**
	 *
	 * Get plus one day of a date
	 *
	 * @return
	 */
	public static String getPlusOneDay(String value) {
		LocalDateTime date = Instant.ofEpochMilli(Long.valueOf(value)).atZone(ZoneId.systemDefault()).toLocalDateTime();
		date = date.plusDays(1L).plus(-1, ChronoUnit.SECONDS);

		return String.valueOf(date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
	}

	/**
	 * Format the given date to the specified format
	 *
	 * @param dateValue
	 * @param fromFormat
	 * @param toFormat
	 * @return converted date string
	 */
	public static String formatGivenDate(String dateValue, String fromFormat, String toFormat) {
		try {
			Date date = new SimpleDateFormat(fromFormat).parse(dateValue);
			SimpleDateFormat toFormatter = new SimpleDateFormat(toFormat);
			return toFormatter.format(date);
		} catch (ParseException e) {
			return dateValue;
		}
	}

	/**
	 * Reformat the dateValue (regardless of its current format) to the specified format
	 *
	 * @param dateValue
	 * @param toFormat
	 * @return
	 */
	public static String reformatDateString(String dateValue, String toFormat) {
		if (isValueNotNull(dateValue) && isValueNotNull(toFormat)) {
			SimpleDateFormat formatter = new SimpleDateFormat(toFormat);
			@SuppressWarnings("deprecation")
			String reformattedValue = formatter.format(new Date(dateValue));
			return reformattedValue;
		}

		return dateValue;
	}

	/**
	 * Get the time difference in minutes
	 *
	 * @param oldDate
	 * @param newDate
	 * @return the difference(in minutes) between two dates
	 */
	public static int getMinutesSince(Date oldDate, Date newDate) {
		if (oldDate == null || newDate == null) {
			return 0;
		}
		long difference = newDate.getTime() - oldDate.getTime();

		return (int) TimeUnit.MILLISECONDS.toMinutes(difference);

	}

	public static boolean isNumeric(String str) {

		return str != null && str.matches("-?\\d+(\\.\\d+)?"); // match a number
		// with optional
		// '-' and
		// decimal.
	}

	/**
	 * Transform a name ex: from Hello to Hello(1); Hello(10) to Hello(11)
	 *
	 * @param name
	 * @return the name with the count incremented
	 */
	public static String appendCountToName(String name) {
		// the pattern to get the (xxx) at the end of the name
		String nameCount = "\\(\\d+\\)$";
		Pattern nameCountPattern = Pattern.compile(nameCount);
		Matcher m = nameCountPattern.matcher(name);
		int num = 1;
		if (m.find()) {
			// this is (xxx)
			String count = m.group();
			// the actual number, incremented by 1
			num = Integer.parseInt(count.substring(1, count.length() - 1)) + 1;
			// strip out the count
			name = name.substring(0, name.length() - count.length());
		}
		name = name.trim() + " (" + num + ")";

		return name;
	}

	/**
	 * Convert a list of string to csv string.
	 *
	 * @param list
	 * @return CSV string
	 */
	public static String convertListToCsv(List<String> list) {
		String str = "";
		if (list != null && !list.isEmpty()) {
			for (String element : list) {
				str += element + ",";
			}
		}

		if (str.length() > 0) {
			str = str.substring(0, str.length() - 1);
		}

		return str;
	}

	/**
	 * Convert a list of long to csv string.
	 *
	 * @param list
	 * @return CSV string
	 */
	public static String convertLongListToCsv(List<Long> list) {
		StringBuilder str = new StringBuilder();
		if (list != null && !list.isEmpty()) {
			for (Long element : list) {
				str.append(element).append(" ,");
			}
		}

		if (str.toString().length() > 0) {
			int lastIndex = str.lastIndexOf(",");
			return str.delete(lastIndex, str.length()).toString();
		}

		return "";
	}

	/**
	 * Convert a csv string of int/long numbers into list
	 *
	 * @param CSV
	 *            numbers
	 * @return list of long numbers
	 */
	public static List<Long> convertCsvToLongList(String csv) {
		List<Long> list = new ArrayList<>();
		if (hasValue(csv)) {
			String[] stringArray = csv.trim().split(",");
			if (stringArray.length > 0) {
				list = Stream.of(stringArray).map(Long::valueOf).collect(Collectors.toList());
			}
		}
		return list;
	}

	/**
	 * Convert a csv of string into list
	 *
	 * @param CSV
	 *
	 * @return list of strings
	 */
	public static List<String> convertCsvToList(String csv) {
		List<String> list = new ArrayList<>();
		if (hasValue(csv)) {
			String[] stringArray = csv.trim().split(",");
			if (stringArray.length > 0) {
				list = Arrays.asList(stringArray);
			}
		}
		return list;
	}

	/**
	 * Returns the environment variable value
	 *
	 * @param property
	 * @return property value
	 */
	public static String getEnvironmentVariable(String propertyName) {

		String propertyValue = System.getProperty(propertyName);
		if (!ObjUtils.hasValue(propertyValue)) {
			propertyValue = System.getenv(propertyName);
		}

		return propertyValue;
	}

	

}
