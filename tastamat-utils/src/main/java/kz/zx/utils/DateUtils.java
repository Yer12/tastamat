package kz.zx.utils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by didar (deedarb@gmail.com) on 8/21/16
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DateUtils {
	public static final Date MIN_DATE = new Date((long) -1e13);//Mon Feb 10 12:13:20 ALMT 1653
	private static final String DATE_MARKER = "yyyy-MM-dd";
	private static final String DATE_TIME_MARKER = "yyyy-MM-dd_HHmm";
	private static final String DATE_TIME_MARKER_EXT = "yyyy-MM-dd HH:mm:ss";
	private static final String HUMAN_DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
	private static final String LOTUS_DATE_TIME_FORMAT = "yyyyMMdd'T'HHmmss,SS'Z'";
	private static final String YYYYMMMDD = "yyyyMMdd";
	private static final String DDMMYYYY = "dd.MM.yyyy";
	private static final String HUMAN_TIME_FORMAT = "HH:mm:ss";
	private static final String PERMUTATION_FORMAT = "HH:mm dd.MM.yyyy";
	private static final String TRIAL_TO_EGOV_FORMAT = "HH:mm dd.MM.yyyy";
	private static final String DATE_HOUR_MINUTE_FORMAT = "dd.MM.yyyy HH:mm";
	private static final String SHORT_HUMAN_FORMAT = "HH:mm";
	private static final String HUMAN_DATE_FORMAT = "dd.MM.yyyy";
	private static DatatypeFactory factory;
	private static final long SECONDS_IN_DAY = 24 * 60 * 60;
	private static final long MILLIS_IN_DAY = SECONDS_IN_DAY * 1000;
	private static final String[] MONTH_RU_NAMES = {
		"Январь", "Февраль", "Март", "Апрель",
		"Май", "Июнь", "Июль", "Август",
		"Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
	private static final String[] MONTH_KK_NAMES = {
		"Қаңтар", "Ақпан", "Наурыз", "Сәуір",
		"Мамыр", "Маусым", "Шілде", "Тамыз",
		"Қыркүйек", "Қазан", "Қараша", "Желтоксан"};

	//преобразовать XMLGregorianCalendar в Date
	public static Date calendarToDate(XMLGregorianCalendar calendar) {
		if (calendar == null) return null;
		return calendar.toGregorianCalendar().getTime();
	}

	//преобразовать Date в XMLGregorianCalendar
	public static XMLGregorianCalendar dateToCalendar(Date date) {
		if (date == null) {
			return null;
		}
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(date);
		try {
			return getFactory().newXMLGregorianCalendar(gCalendar);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	//получить текущую даты в формате yyyy-MM-dd
	public static String getDateMarker() {
		return new SimpleDateFormat(DATE_MARKER).format(new Date());
	}

	//получить дату в формате yyyy-MM-dd_HHmm
	public static String getDateTimeMarker() {
		return getDateTimeMarker(new Date());
	}

	//получить дату в формате yyyy-MM-dd_HHmm
	public static String getDateTimeMarker(Date date) {
		if (date == null) return "";
		return new SimpleDateFormat(DATE_TIME_MARKER).format(date);
	}


	//конвертирование даты в формат dd.MM.yyyy HH:mm:ss
	public static String formatToHumanDateTime(Date date) {
		if (date == null) return null;
		return new SimpleDateFormat(HUMAN_DATE_TIME_FORMAT).format(date);
	}

	public static DatatypeFactory getFactory() throws DatatypeConfigurationException {
		if (factory == null) {
			factory = DatatypeFactory.newInstance();
		}
		return factory;
	}

	//конвертирование даты в формат xml Lotus Notes
	public static String formatToLotusDateTime(Date date) {
		if (date == null) return null;
		return new SimpleDateFormat(LOTUS_DATE_TIME_FORMAT).format(date);
	}

	//конвертировать дату в формат yyyyMMdd
	public static String formatToYYYYMMDD(Date date) {
		if (date == null) return null;
		return new SimpleDateFormat(YYYYMMMDD).format(date);
	}

	//конвертировать время в формат HH:mm:ss
	public static String formatToHumanTime(Date date) {
		if (date == null) return null;
		return new SimpleDateFormat(HUMAN_TIME_FORMAT).format(date);
	}

	//конвертировать дату и время в формат HH:mm dd.MM.yyyy
	public static String formatToPermutation(Date date) {
		if (date == null) return null;
		return new SimpleDateFormat(PERMUTATION_FORMAT).format(date);
	}

	//конвертировать дату и время в формат HH:mm dd.MM.yyyy
	public static String formatToTrialEgov(Date date) {
		if (date == null) return null;
		return new SimpleDateFormat(TRIAL_TO_EGOV_FORMAT).format(date);
	}

	//конвертировать дату и время в формат dd.MM.yyyy HH:mm
	public static String formatToDateHourMinute(Date date) {
		if (date == null) return null;
		return new SimpleDateFormat(DATE_HOUR_MINUTE_FORMAT).format(date);
	}

	/**
	 * конвертировать время в формат HH:mm
	 */
	public static String formatToShortHumanTime(Date date) {
		if (date == null) return null;
		return new SimpleDateFormat(SHORT_HUMAN_FORMAT).format(date);
	}

	//конвертировать дату в формат dd.MM.yyyy
	public static String formatToHumanDate(Date date) {
		if (date == null) return null;
		return new SimpleDateFormat(HUMAN_DATE_FORMAT).format(date);
	}

	//конвертировать дату в формат dd.mm.yyyy
	public static String formatToDDMMYYYY(Date date) {
		if (date == null) return null;
		return new SimpleDateFormat(DDMMYYYY).format(date);
	}

	//конвертировать дату в формат yyyy-MM-dd
	public static String formatToDateMarker(Date date) {
		if (date == null) return null;
		return new SimpleDateFormat(DATE_MARKER).format(date);
	}

	//распарсить строку вида dd.MM.yyyy
	public static Date parseHumanDate(String value) throws ParseException {
		if (value == null) return null;
		return new SimpleDateFormat(HUMAN_DATE_FORMAT).parse(value);
	}

	//распарсить строку вида dd.MM.yyyy HH:mm:ss
	public static Date parseHumanDateTime(String value) throws ParseException {
		if (value == null) return null;
		return new SimpleDateFormat(HUMAN_DATE_TIME_FORMAT).parse(value);
	}

	//распарсить строку вида dd.mm.yyyy
	public static Date parseDDMMYYYY(String value) throws ParseException {
		if (value == null) return null;
		return new SimpleDateFormat(DDMMYYYY).parse(value);
	}

	//распарсить строку вида dd.MM.yyyy HH:mm
	public static Date parseDateHourMinute(String value) throws ParseException {
		if (value == null) return null;
		return new SimpleDateFormat(DATE_HOUR_MINUTE_FORMAT).parse(value);
	}

	private static Calendar getCalendar() {
		return Calendar.getInstance();
	}

	private static Calendar getCalendar(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		return c;
	}

	/**
	 * добавить к текущей дате переданное кол-во минут
	 */
	public static Date addMinutes(int minutes) {
		return addMinutes(new Date(), minutes);
	}

	/**
	 * добавить к дате переданное кол-во минут
	 */
	public static Date addMinutes(Date date, int minutes) {
		if (date == null) {
			return null;
		}
		Calendar c = getCalendar();
		c.setTime(date);
		c.add(Calendar.MINUTE, minutes);
		return c.getTime();
	}

	/**
	 * добавить к текущей дате переданное кол-во дней
	 */
	public static Date addDays(int days) {
		return addDays(new Date(), days);
	}

	/**
	 * добавить к дате переданное кол-во дней
	 */
	public static Date addDays(Date date, int days) {
		if (date == null) return null;
		Calendar c = getCalendar();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}

	public static OffsetDateTime offseted(Date date) {
		if (date == null) {
			return null;
		}
		return OffsetDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	public static OffsetDateTime offseted(Long millis) {
		if (millis == null) {
			return null;
		}
		return OffsetDateTime.ofInstant(new Date(millis).toInstant(), ZoneId.systemDefault());
	}

	/**
	 * добавить к дате переданное кол-во месяцев
	 */
	public static Date addMonth(Date date, int months) {
		if (date == null) return null;
		Calendar c = getCalendar();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		return c.getTime();
	}

	public static Date addMonth(int months) {
		return addMonth(new Date(), months);
	}

	/**
	 * добавить к дате переданное кол-во часов
	 */
	public static Date addHours(Date date, int hours) {
		if (date == null) {
			return null;
		}
		Calendar c = getCalendar();
		c.setTime(date);
		c.add(Calendar.HOUR, hours);
		return c.getTime();
	}

	public static Date addMillis(Date date, long millis) {
		if (date == null) {
			return null;
		}
		Calendar c = getCalendar();
		c.setTimeInMillis(date.getTime() + millis);
		return c.getTime();
	}

	/**
	 * парсинг формата DATE_MARKER
	 */
	public static Date parseDateMarker(String text) throws ParseException {
		if (StringUtils.isEmptyString(text)) return null;
		return new SimpleDateFormat(DATE_MARKER).parse(text);
	}

	/**
	 * вернуть год из переданной даты
	 */
	public static Integer getYear(Date date) {
		if (date == null) return null;
		Calendar c = getCalendar(date);
		return c.get(Calendar.YEAR);
	}

	/**
	 * вернуть день месяца
	 */
	public static Integer getDay(Date date) {
		if (date == null) return null;
		Calendar c = getCalendar(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Вернуть индекс месяца от 0 до 11
	 */
	public static Integer getMonth(Date date) {
		if (date == null) return null;
		Calendar c = getCalendar(date);
		return c.get(Calendar.MONTH);
	}

	/**
	 * Наименование месяца на русском языке
	 *
	 * @param monthIndex индекс месяца от 0 до 11
	 * @return название месяца
	 */
	public static String getMonthNameRu(Integer monthIndex) {
		if (monthIndex == null) return null;
		return MONTH_RU_NAMES[monthIndex];
	}

	/**
	 * Наименование месяца на казахском языке
	 *
	 * @param monthIndex индекс месяца от 0 до 11
	 * @return название месяца
	 */
	public static String getMonthNameKk(Integer monthIndex) {
		if (monthIndex == null) return null;
		return MONTH_KK_NAMES[monthIndex];
	}

	public static long daysToSeconds(int days) {
		return days * SECONDS_IN_DAY;
	}

	/**
	 * @param year  год
	 * @param month месяц от 1 до 12
	 * @param day   день
	 * @return сформированная дата
	 */
	public static Date createDate(int year, int month, int day) {
		return createDateTime(year, month, day, 0, 0, 0, 0);
	}

	/**
	 * @param year   год
	 * @param month  месяц от 1 до 12
	 * @param day    день
	 * @param hour   час (24-х часовой формат)
	 * @param minute минута
	 * @param second секунда
	 * @param millis миллисекунда
	 * @return сформированная дата
	 */
	public static Date createDateTime(int year, int month, int day,
									  int hour, int minute, int second, int millis) {
		Calendar c = getCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		c.set(Calendar.MILLISECOND, millis);
		c.setLenient(false);//запретить эвристическое интерпретирование
		return c.getTime();
	}

	public static Date createDateTime(int hour, int minute, int second) {
		Calendar c = getCalendar();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		c.setLenient(false);//запретить эвристическое интерпретирование
		return c.getTime();
	}

	/**
	 * Обнуление времени в дате
	 *
	 * @param date дата и время
	 * @return дата с обнуленным временем
	 */
	public static Date eraseTime(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * Заполнение времени в дате
	 *
	 * @param date дата и время
	 * @return дата с конечным временем
	 */
	public static Date fillTime(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	/**
	 * Обнуление месяцев в году
	 *
	 * @param date дата и время
	 * @return дата с обнуленным временем
	 */
	public static Date eraseMonth(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * Заполнение месяцев в году
	 *
	 * @param date дата и время
	 * @return дата с конечным временем
	 */
	public static Date fillMonth(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		c.set(Calendar.MONTH, 11);
		c.set(Calendar.DAY_OF_MONTH, 31);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	/**
	 * Разница между датами в миллисекундах
	 *
	 * @param date1 первая дата
	 * @param date2 вторая дата
	 * @return кол-во миллисекунд
	 */
	public static long diffInMillis(Date date1, Date date2) {
		return date2.getTime() - date1.getTime();
	}

	/**
	 * Разница между датами в секундах
	 *
	 * @param date1 первая дата
	 * @param date2 вторая дата
	 * @return кол-во секунд
	 */
	public static long diffInSeconds(Date date1, Date date2) {
		return diffInMillis(date1, date2) / 1000;
	}

	/**
	 * Разница между датами в днях
	 *
	 * @param date1 первая дата
	 * @param date2 вторая дата
	 * @return кол-во дней
	 */
	public static int diffInDays(Date date1, Date date2) {
		return (int) (diffInMillis(date1, date2) / MILLIS_IN_DAY);
	}

	public static int getCurrentYear() {
		return getYear(new Date());
	}

	public static long convertToMillis(long value, TimeUnit timeUnit) {
		return TimeUnit.MILLISECONDS.convert(value, timeUnit);
	}

	public static boolean isSameDay(Date first, Date second) {
		Calendar cal1 = getCalendar();
		Calendar cal2 = getCalendar();
		cal1.setTime(first);
		cal2.setTime(second);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
			cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}

	public static Date getDate(Long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	public static java.sql.Date getSqlDate(Long millis) {
		return new java.sql.Date(millis);
	}

	public static String toJsonArray(List<Date> dates) {
		List<String> stringDates = dates.stream().map(d -> String.valueOf(d.getTime())).collect(Collectors.toList());
		return "[" + String.join(",", stringDates) + "]";
	}

	//получить дату в формате yyyy-MM-dd HH:mm:ss
	public static String getDateTimeMarkerExt(Date date) {
		if (date == null) return "";
		return new SimpleDateFormat(DATE_TIME_MARKER_EXT).format(date);
	}

	public static Date getEaiasDate(String source) throws ParseException {
		SimpleDateFormat eaiasDatetimeFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss,SSSX");
		eaiasDatetimeFormat.setLenient(false);
		return eaiasDatetimeFormat.parse(source);
	}

	public static long getDateWithDayActualMaximum(int year, int month) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		return calendar.getTimeInMillis();
	}

	public static long getFromDate() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2006);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.MONTH, 1);

		return calendar.getTimeInMillis();
	}

	public static Date addSeconds(Date date, int seconds) {
		if (date == null) return null;
		Calendar c = getCalendar();
		c.setTime(date);
		c.add(Calendar.SECOND, seconds);
		return c.getTime();
	}

	public static Date addSeconds(int seconds) {
		return addSeconds(new Date(), seconds);
	}

	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	public static Timestamp dateToTimestamp(Date date){
		return new Timestamp(date.getTime());
	}

	public static int getHour(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.HOUR_OF_DAY);
	}

	public static int getHalfYear(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		if (c.get(Calendar.MONTH) < 6) {
			return 1;
		} else if (c.get(Calendar.MONTH) >= 6) {
			return 2;
		}
		return 0;
	}

	public static int getQuarter(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		if (c.get(Calendar.MONTH) <= 2) {
			return 1;
		} else if (c.get(Calendar.MONTH) > 2 && c.get(Calendar.MONTH) <= 5) {
			return 2;
		} else if (c.get(Calendar.MONTH) > 5 && c.get(Calendar.MONTH) <= 8) {
			return 3;
		} else if (c.get(Calendar.MONTH) > 8) {
			return 4;
		}
		return 0;
	}
}