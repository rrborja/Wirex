package test.customcomponent;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.SwingUtilities;

/**
 * Class for commonly used static functions
 * <p>
 * <strong>Fun classes:</strong>
 * <ul>
 * <li>java.awt.Desktop
 * <li>java.awt.MouseInfo
 * <li>java.awt.Robot
 * <li>java.awt.SystemColor
 * <li>java.awt.Toolkit
 * <li>java.io.Console
 * <li>java.lang.Math
 * <li>java.lang.Runtime
 * <li>java.lang.StringBuilder
 * <li>java.math.BigInteger
 * <li>java.util.Arrays
 * <li>java.util.Collections
 * <li>java.util.Properties
 * <li>java.util.prefs.Preferences
 * <li>javax.imageio.ImageIO
 * <li>javax.swing.SwingUtilities
 * <li>javax.swing.UIManager
 * <li>javax.swing.text.DefaultEditorKit
 * </ul>
 * </p>
 *
 * @deprecated
 */
public final class Utils {

    public static final String UTF8 = "UTF-8"; //Unicode Transformation Format
    public static final String sqlDateFormat = "yyyy-MM-dd";
    public static final String wordDateFormat = "MMM dd, yyyy";
    public static final DateFormat dateFormatterSql = new SimpleDateFormat(sqlDateFormat);
    public static final DateFormat dateFormatterWord = new SimpleDateFormat(wordDateFormat);
    /**
     * 8 kB default buffer size
     */
    public static int DEFAULT_BUFFER_SIZE = 8192;

    public static String toUpperCase(String str) {
	if (str == null) {
	    throw new IllegalArgumentException("string must not be null");
	} else if (str.isEmpty()) {
	    return str;
	}
	StringBuilder sb = new StringBuilder(str);
	int index = 0;
	char ch = sb.charAt(index);
	if (Character.isLowerCase(ch)) {
	    ch = Character.toUpperCase(ch);
	    sb.setCharAt(index, ch);
	}
	return sb.toString();
    }

    public static String toProperCase(String str) {
	return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String csvEncode(Object o) {
	return csvEncode(def(o));
    }

    public static String csvEncode(String s) {
	if (s == null || s.isEmpty()) {
	    return "\"\"";
	}

	s = s.replaceAll("\"", "\"\"");
	char[] escapeCharacters = new char[]{',', '"', '\r', '\n'};
	for (char c : escapeCharacters) {
	    if (s.contains(String.valueOf(c))) {
		return "\"" + s + "\"";
	    }
	}
	return s;
    }

    public static boolean isPrimitive(Object o) {
	Class<?>[] wrappers = new Class<?>[]{Byte.class, Short.class,
	    Integer.class, Long.class, Float.class, Double.class,
	    Character.class, Boolean.class, String.class};
	for (Class<?> wrapper : wrappers) {
	    if (wrapper.isInstance(o)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * @param dividend top
     * @param divisor bottom
     * @return
     * @deprecated
     */
    @Deprecated
    public static double percentOf(double dividend, double divisor) {
	return round((dividend / divisor) * 100, 2);
    }

    /**
     * <tt>return Thread.getAllStackTraces().get(Thread.currentThread());</tt>
     */
    public static StackTraceElement[] getStackTrace() {
	return Thread.getAllStackTraces().get(Thread.currentThread());
    }

    /**
     *
     * @param skip
     * @return
     */
    public static StackTraceElement[] getStackTrace(int skip) {
	StackTraceElement[] stackTrace = getStackTrace();
	return Utils.subarray(stackTrace, skip, stackTrace.length - skip);
    }

    public static void sleep(long time) {
	throwSwingThreadException();
	try {
	    Thread.sleep(time);
	} catch (InterruptedException ex) {
	    throw new RuntimeException(ex);
	}
    }

    /**
     * Throws an
     * <code>IllegalThreadStateException</code> if called from the event
     * dispatch thread.
     *
     * @throws IllegalThreadStateException if called from the event dispatch
     * thread
     * @see #requiresSwingThreadException()
     */
    public static void throwSwingThreadException() {
	if (SwingUtilities.isEventDispatchThread()) {
	    RuntimeException ex = new IllegalThreadStateException(
		    "must not be called from the event dispatch thread");
	    int skip = 5;
	    ex.setStackTrace(getStackTrace(skip));
	    throw ex;
	}
    }

    /**
     * Throws an
     * <code>IllegalThreadStateException</code> if called from outside the event
     * dispatch thread.
     *
     * @throws IllegalThreadStateException if called from outside the event
     * dispatch thread
     * @see #throwSwingThreadException()
     */
    public static void requiresSwingThreadException() {
	if (!SwingUtilities.isEventDispatchThread()) {
	    RuntimeException ex = new IllegalThreadStateException(
		    "must be called from the event dispatch thread");
	    int skip = 5;
	    ex.setStackTrace(getStackTrace(skip));
	    throw ex;
	}
    }

    public static String pad(int length) {
	char space = ' ';
	return pad(space, length);
    }

    public static String pad(char c, int length) {
	StringBuilder sb = new StringBuilder(length);
	while (sb.length() < length) {
	    sb.append(c);
	}
	return sb.toString();
    }

    public static String padLeft(String str, char c, int length) {
	if (str == null) {
	    throw new IllegalArgumentException("str must not be null");
	} else if (str.length() >= length) {
	    return str;
	}
	StringBuilder sb = new StringBuilder(length);
	while (sb.length() < length - str.length()) {
	    sb.append(c);
	}
	sb.append(str);
	return sb.toString();
    }

    public static String padRight(String str, char c, int length) {
	if (str == null) {
	    throw new IllegalArgumentException("str must not be null");
	} else if (str.length() >= length) {
	    return str;
	}
	StringBuilder sb = new StringBuilder(length);
	sb.append(str);
	while (sb.length() < length) {
	    sb.append(c);
	}
	return sb.toString();
    }

    /**
     * This method is slightly slower than <tt>String.trim()</tt>
     * so it should not be used to remove spaces.
     *
     * @param s the String to be trimmed
     * @param ch the character to trim
     * @return
     */
    public static String trim(String s, char ch) {
	int i = 0, length = s.length();
	for (; i < length && s.charAt(i) == ch; i++)
			;
	for (; i < length && s.charAt(length - 1) == ch; length--)
			;
	return i > 0 || length < s.length() ? s.substring(i, length) : s;
    }

    public static String trim(String s, char... chars) {
	Character[] characters = toObject(chars);
	int i = 0, length = s.length();
	for (; i < length && inArray(s.charAt(i), characters); i++)
			;
	for (; i < length && inArray(s.charAt(length - 1), characters); length--)
			;
	return i > 0 || length < s.length() ? s.substring(i, length) : s;
    }

    public static String trimLeft(String s, char ch) {
	int i = 0;
	for (; i < s.length() && s.charAt(i) == ch; i++)
			;
	return i > 0 ? s.substring(i) : s;
    }

    public static String trimLeft(String s, char... chars) {
	Character[] characters = toObject(chars);
	int i = 0;
	for (; i < s.length() && inArray(s.charAt(i), characters); i++)
			;
	return i > 0 ? s.substring(i) : s;
    }

    public static String trimRight(String s, char ch) {
	int len = s.length();
	for (; len > 0 && s.charAt(len - 1) == ch; len--)
			;
	return len < s.length() ? s.substring(0, len) : s;
    }

    public static String trimRight(String s, char... chars) {
	Character[] characters = toObject(chars);
	int len = s.length();
	for (; len > 0 && inArray(s.charAt(len - 1), characters); len--)
			;
	return len < s.length() ? s.substring(0, len) : s;
    }

    /**
     *
     * @param s
     * @param start
     * @param end
     * @param lastOccurrence use the last occurrence of <code>end</code>
     * @return the string between <code>start</code> and <code>end</code>
     * @deprecated use a regular expression instead
     */
    @Deprecated
    public static String getStringBetween(String s, String start, String end,
	    boolean lastOccurrence) {
	int i = s.indexOf(start) + start.length();
	int len = (lastOccurrence ? s.lastIndexOf(end) : s.indexOf(end, i)) - i;
	return s.substring(i, i + len);
    }

    public static <N extends Number> Number min(N... numbers) {
	if (numbers == null) {
	    throw new IllegalArgumentException("numbers must not be null");
	} else if (numbers.length == 0) {
	    throw new IllegalArgumentException("numbers must not be empty");
	}
	N base = numbers[0];
	for (int i = 1; i < numbers.length; i++) {
	    if (numbers[i].doubleValue() < base.doubleValue()) {
		base = numbers[i];
	    }
	}
	return base;
    }

    public static <N extends Number> Number max(N... numbers) {
	if (numbers == null) {
	    throw new IllegalArgumentException("numbers must not be null");
	} else if (numbers.length == 0) {
	    throw new IllegalArgumentException("numbers must not be empty");
	}
	N base = numbers[0];
	for (int i = 1; i < numbers.length; i++) {
	    if (numbers[i].doubleValue() > base.doubleValue()) {
		base = numbers[i];
	    }
	}
	return base;
    }

    public static int getOppositeOf(int i) {
	String s = Integer.toString(i);
	if (i == 0 || s.matches("^[-]?1[0]+$")) { //-10, 100, ect...
	    return 0;
	}
	String n = padRight((i < 0 ? "-1" : "1"), '0', s.length() + 1);
	int high = Integer.valueOf(n);
	return (int) getOppositeOf(i, 0, high);
    }

    /**
     * Returns the value opposite to
     * <code>i</code> in the range between
     * <code>num1</code> and
     * <code>num2</code>.<br>
     * e.g. getOppositeOf(1, 1, 3) = 3<br>
     * getOppositeOf(-5, -10, 10) = 5<br>
     * getOppositeOf(1, 1, 1) = 1<br>
     * getOppositeOf(0, -3, 5) = 2<br>
     * It is worth noting that the order of
     * <code>num1</code> and
     * <code>num2</code> doesn't matter.
     *
     * @param i the number to invert
     * @param num1 one end of the range
     * @param num2 the other end of the range
     * @return the inverted number
     * @throws IllegalArgumentException
     * <tt>if i < low || i > high || low > high</tt>
     */
    public static double getOppositeOf(double i, double num1, double num2) {
	double low = Math.min(num1, num2), high = Math.max(num1, num2);
	if (i < low || i > high) {
	    String message = "i must be greater than or equal to low and less than or equal to high";
	    throw new IllegalArgumentException(message);
	}
	if (i < 0) {
	    return high - Math.abs(Math.abs(i) - Math.abs(low));
	}
	return low + Math.abs(Math.abs(high) - Math.abs(i));
    }

    public static List<File> listFiles() throws IOException {
	return listFiles(null);
    }

    public static List<File> listFiles(File dir) throws IOException {
	return listFiles(dir, null);
    }

    public static List<File> listFiles(File dir, FileFilter filter)
	    throws IOException {
	return listFiles(dir, filter, null);
    }

    public static List<File> listFiles(File dir, FileFilter filter,
	    List<File> list) throws IOException {
	if (dir == null) {
	    dir = new File(".");
	}
	File[] files = dir.listFiles(filter);
	if (files == null) {
	    String message = dir + " is not a directory or could not be read";
	    throw new IOException(message);
	}
	if (list == null) {
	    list = new ArrayList<File>(files.length);
	}
	for (File file : files) {
	    list.add(file);
	    if (file.isDirectory()) {
		listFiles(file, filter, list);
	    }
	}
	return list;
    }

    public static File createTempFile(String filename) {
	String tmpdir = System.getProperty("java.io.tmpdir");
	if (!tmpdir.endsWith(File.separator)) {
	    tmpdir += File.separator;
	}
	if (filename.startsWith(File.separator)) {
	    filename = filename.substring(1);
	}
	return new File(tmpdir + filename);
    }

    /**
     * Attempts to undo a call made to {@link File#deleteOnExit()}.
     *
     * @return <code>true</code> upon success success, otherwise
     * <code>false</code>
     */
    public static boolean makePermanent(File file) {
	String className = "java.io.DeleteOnExitHook";
	String fieldName = "files";
	try {
	    Class<?> c = Class.forName(className);
	    Field field = c.getDeclaredField(fieldName);
	    field.setAccessible(true);
	    Object o = field.get(null);
	    if (o instanceof Set<?>) {
		@SuppressWarnings("unchecked")
		Set<String> files = (Set) o;
		synchronized (files) {
		    return files.remove(file.getPath());
		}
	    }
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	} catch (SecurityException e) {
	    e.printStackTrace();
	} catch (NoSuchFieldException e) {
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
	return false;
    }

    public static String getFileContents(String filename) throws IOException {
	return getFileContents(new File(filename));
    }

    public static String getFileContents(File file) throws IOException {
	Reader in = new FileReader(file);
	try {
	    return readAll(in).toString();
	} finally {
	    close(in);
	}
    }

    public static StringBuilder readAll(Reader in) throws IOException {
	return readAll(in, DEFAULT_BUFFER_SIZE);
    }

    public static StringBuilder readAll(Reader in, int size) throws IOException {
	if (in == null) {
	    throw new IllegalArgumentException("in must not be null");
	}
	StringBuilder sb = new StringBuilder();
	int read;
	char[] buf = new char[size];
	while ((read = in.read(buf)) != -1) {
	    sb.append(buf, 0, read);
	}
	return sb;
    }

    /**
     * Copies bytes from
     * <code>in</code> to
     * <code>out</code> until the end of the
     * <code>InputStream</code> stream has been reached.
     *
     * @see #copy(InputStream, OutputStream, long, int)
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
	return copy(in, out, -1);
    }

    /**
     * Copies
     * <code>length</code> bytes from
     * <code>in</code> to
     * <code>out</code> .
     *
     * @see #copy(InputStream, OutputStream, long, int)
     */
    public static int copy(InputStream in, OutputStream out, long length)
	    throws IOException {
	return copy(in, out, length, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Copies
     * <code>length</code> bytes from
     * <code>in</code> to
     * <code>out</code> .
     *
     * @param in <code>InputStream</code> to copy from
     * @param out <code>OutputStream</code> to copy to
     * @param length the number of bytes to copy, or <code>-1</code> to wait
     * until the end of the stream has been reached
     * @param size buffer size (in bytes)
     * @return the total number of bytes copied
     * @throws IOException if an I/O error occurs. In particular, an
     * <code>IOException</code> is thrown if the output stream is closed.
     */
    public static int copy(InputStream in, OutputStream out, long length,
	    int size) throws IOException {
	int copied = 0;
	int read;
	byte[] buf = new byte[copyDefault(length, size)];
	while (length != 0
		&& (read = in.read(buf, 0, copyDefault(length, buf.length))) != -1) {
	    out.write(buf, 0, read);
	    copied += read;
	    length -= read;
	}
	return copied;
    }

    /**
     * helper method which handles special case where length is -1
     */
    private static int copyDefault(long length, int size) {
	return length < 0 ? size : (int) Math.min(length, size);
    }

    /**
     * <a
     * href="http://www2.java.net/article/2006/04/04/exception-handling-antipatterns#throwFromWithinFinally"
     * > Throw from Within Finally</a>
     *
     * @return <code>true</code> upon success success, otherwise
     * <code>false</code>
     */
    public static boolean close(Closeable c) {
	if (c != null) {
	    try {
		c.close();
		return true;
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return false;
    }

    public static Throwable getRootCause(Throwable t) {
	if (t == null) {
	    throw new IllegalArgumentException("throwable must not be null");
	}
	Throwable cause = t;
	while ((t = t.getCause()) != null) {
	    cause = t;
	}
	return cause;
    }

    public static void writeToFile(String pathname, String str, boolean append)
	    throws IOException {
	writeToFile(new File(pathname), str, append);
    }

    public static void writeToFile(File file, String str, boolean append)
	    throws IOException {
	FileWriter out = null;
	try {
	    out = new FileWriter(file, append);
	    out.write(str);
	} finally {
	    close(out);
	}
    }

    public static <T, E extends T> boolean inArray(T needle, E... haystack) {
	if (needle == null || haystack == null) {
	    throw new IllegalArgumentException(
		    "neither needle nor haystack may be null");
	}
	for (E blade : haystack) {
	    if (needle.equals(blade)) {
		return true;
	    }
	}
	return false;
    }

    public static <E> int getIndex(E[] array, E element) {
	if (array == null) {
	    throw new IllegalArgumentException("array must not be null");
	}
	List<E> list = Arrays.asList(array);
	return getIndex(list, element);
    }

    /**
     *
     * @param <E>
     * @param list
     * @param e
     * @return
     * @throws IllegalArgumentException
     */
    public static <E> int getIndex(List<E> list, E element) {
	if (list == null) {
	    throw new IllegalArgumentException("list must not be null");
	}
	for (int i = 0; i < list.size(); i++) {
	    E e = list.get(i);
	    if (element == e || element.equals(e)) {
		return i;
	    }
	}
	return -1;
    }

    public static <K, V> K getKey(Map<K, V> map, V value) {
	if (map == null) {
	    throw new IllegalArgumentException("map must not be null");
	}
	for (K key : map.keySet()) {
	    V v = map.get(key);
	    if (value == v || (value != null && value.equals(v))) {
		return key;
	    }
	}
	return null;
    }

    public static String implode(Object... pieces) {
	String glue = "";
	return implode(glue, pieces);
    }

    public static String implode(String glue, Object... pieces) {
	StringBuilder out = new StringBuilder();
	for (int i = 0; i < pieces.length; i++) {
	    if (i > 0) {
		out.append(glue);
	    }
	    out.append(pieces[i]);
	}
	return out.toString();
    }

    //Client URL Request Library (Yes I stole the name!)
    public static String cURL(String spec) throws IOException {
	return cURL(spec, null);
    }

    /**
     * This method works in its entirety, however, it should only be used as a
     * template or example.
     */
    public static String cURL(String spec, CharSequence post)
	    throws IOException {
	//System.out.println(uri);
	URL url = new URL(spec);
	URLConnection con = url.openConnection();
	con.setUseCaches(false);

	if (post != null) {
	    con.setDoOutput(true);
	    OutputStreamWriter out = null;
	    try {
		out = new OutputStreamWriter(con.getOutputStream());
		out.write(post.toString());
	    } finally {
		Utils.close(out);
	    }
	}

	Reader in = null;
	try {
	    in = new InputStreamReader(con.getInputStream());
	    return Utils.readAll(in).toString();
	} finally {
	    Utils.close(in);
	}
    }

    /**
     * @return the percent of memory being used by the JVM
     */
    public static double getUsedMemory() {
	Runtime runtime = Runtime.getRuntime();
	long memory = runtime.totalMemory() - runtime.freeMemory();
	memory = runtime.maxMemory() - memory;
	memory = runtime.maxMemory() - memory;
	return Utils.percentOf(memory, runtime.maxMemory());
    }

    /**
     * @return the free memory available to the JVM
     */
    public static long getFreeMemory() {
	Runtime runtime = Runtime.getRuntime();
	long memory = runtime.totalMemory() - runtime.freeMemory();
	return runtime.maxMemory() - memory;
    }
    
    public static byte[] encrypt(byte[] b, String password) throws IOException,
	    InvalidKeyException, InvalidAlgorithmParameterException,
	    InvalidKeySpecException, NoSuchPaddingException,
	    NoSuchAlgorithmException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	CipherOutputStream out = null;
	try {
	    out = new CipherOutputStream(baos, getCipher(password,
		    Cipher.ENCRYPT_MODE));
	    out.write(b);
	} finally {
	    close(out);
	}
	return baos.toByteArray();
    }

    public static byte[] decrypt(byte[] b, String password) throws IOException,
	    InvalidKeyException, InvalidAlgorithmParameterException,
	    InvalidKeySpecException, NoSuchPaddingException,
	    NoSuchAlgorithmException {
	return decrypt(b, DEFAULT_BUFFER_SIZE, password);
    }

    public static byte[] decrypt(byte[] b, int size, String password)
	    throws IOException, InvalidKeyException,
	    InvalidAlgorithmParameterException, InvalidKeySpecException,
	    NoSuchPaddingException, NoSuchAlgorithmException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	CipherInputStream in = null;
	try {
	    in = new CipherInputStream(new ByteArrayInputStream(b), getCipher(
		    password, Cipher.DECRYPT_MODE));
	    int read;
	    byte[] buf = new byte[size];
	    while ((read = in.read(buf)) != -1) {
		baos.write(buf, 0, read);
	    }
	} finally {
	    close(in);
	}
	return baos.toByteArray();
    }

    public static byte[] compress(byte[] b) throws IOException {
	GZIPOutputStream out = null;
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	try {
	    out = new GZIPOutputStream(baos);
	    out.write(b);
	} finally {
	    close(out);
	}
	return baos.toByteArray();
    }

    public static byte[] decompress(byte[] b) throws IOException {
	return decompress(b, DEFAULT_BUFFER_SIZE);
    }

    public static byte[] decompress(byte[] b, int size) throws IOException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	GZIPInputStream in = null;
	try {
	    in = new GZIPInputStream(new ByteArrayInputStream(b));
	    copy(in, baos);
	} finally {
	    close(in);
	}
	return baos.toByteArray();
    }

    public static String encode(String s) throws UnsupportedEncodingException {
	return URLEncoder.encode(s, UTF8);
    }

    public static String decode(String s) throws UnsupportedEncodingException {
	return URLDecoder.decode(s, UTF8);
    }

    public static String xmlEncode(Object o) throws IOException {
	ByteArrayOutputStream baos = null;
	XMLEncoder out = null;
	try {
	    out = new XMLEncoder(baos = new ByteArrayOutputStream());
	    out.writeObject(o);
	} finally {
	    if (out != null) {
		out.close();
	    }
	}
	return baos.toString(UTF8);
    }

    public static Object xmlDecode(String s) throws IOException {
	XMLDecoder in = null;
	try {
	    in = new XMLDecoder(new ByteArrayInputStream(s.getBytes(UTF8)));
	    return in.readObject();
	} finally {
	    if (in != null) {
		in.close();
	    }
	}
    }

    public static byte[] serialize(Object o) throws IOException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	ObjectOutputStream out = null;
	try {
	    out = new ObjectOutputStream(baos);
	    out.writeObject(o);
	    return baos.toByteArray();
	} finally {
	    close(out);
	}
    }

    public static Object deserialize(byte[] b) throws ClassNotFoundException,
	    IOException {
	ObjectInputStream in = null;
	try {
	    in = new ObjectInputStream(new ByteArrayInputStream(b));
	    return in.readObject();
	} finally {
	    close(in);
	}
    }

    /**
     * @param num1 inclusive
     * @param num2 exclusive
     * @return
     */
    public static int random(int num1, int num2) {
	if (num1 == num2) {
	    return num1;
	}
	int range = (int) difference(num1, num2);
	int low = Math.min(num1, num2);
	return low + (int) (Math.random() * range);
    }

    /**
     * @param num1 inclusive
     * @param num2 exclusive
     * @param place places to round past the decimal
     * @return
     */
    public static double random(double num1, double num2, int place) {
	if (num1 == num2) {
	    return num1;
	}
	double range = difference(num1, num2);
	double low = Math.min(num1, num2);
	return round(low + (range * Math.random()), place);
    }

    public static String randomString(int length) {
	String alphabet = "abcdefghijklmnopqrstuvwxyz"
		+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
	return randomString(alphabet, length);
    }

    public static String randomString(String characters, int length) {
	String r = shuffle(characters);
	StringBuilder sb = new StringBuilder();
	while (sb.length() < length) {
	    sb.append(r.charAt(random(0, r.length())));
	}
	return sb.toString();
    }

    public static String shuffle(String s) {
	List<Character> chars = Arrays.asList(toObject(s.toCharArray()));
	Collections.shuffle(chars);
	Character[] characters = chars.toArray(new Character[chars.size()]);
	return String.valueOf(toPrimitive(characters));
    }

    /**
     * @return the absolute difference between <tt>num1</tt> and <tt>num2</tt>
     * @deprecated use <code>Math.abs(num1 - num2)</code> instead
     */
    @Deprecated
    public static double difference(double num1, double num2) {
	if (num1 == num2) {
	    return 0;
	}
	double low = Math.min(num1, num2), high = Math.max(num1, num2);
	if (high < 0) { //both are negative
	    return Math.abs(low - high);
	} else if (low < 0) { //only low is negative
	    return Math.abs(low) + high;
	} else { //both are positive
	    return high - low;
	}
    }

    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
	int place = 4;
	return dividend.divide(divisor, place, RoundingMode.HALF_UP);
    }

    /**
     * Returns a "default" cipher for use with cipher IO streams
     *
     * @param mode <code>Cipher.ENCRYPT_MODE</code> or
     * <code>Cipher.DECRYPT_MODE</code>
     * @see javax.crypto.CipherInputStream
     * @see javax.crypto.CipherOutputStream
     * @return
     * @deprecated this method provides no real security
     */
    @Deprecated
    public static Cipher getDefaultCipher(int mode)
	    throws InvalidAlgorithmParameterException, InvalidKeySpecException,
	    NoSuchPaddingException, NoSuchAlgorithmException,
	    InvalidKeyException {
	final String password = "If you are reading this, you are too damn close!";
	return getCipher(password, mode);
    }

    /**
     * Returns a "default" cipher for use with cipher IO streams
     *
     * @param password the (<code>String<code>) password to use for this cipher
     * @param mode <code>Cipher.ENCRYPT_MODE</code> or
     * <code>Cipher.DECRYPT_MODE</code>
     * @see javax.crypto.CipherInputStream
     * @see javax.crypto.CipherOutputStream
     * @return
     */
    public static Cipher getCipher(String password, int mode)
	    throws InvalidAlgorithmParameterException, InvalidKeySpecException,
	    NoSuchPaddingException, NoSuchAlgorithmException,
	    InvalidKeyException {

	//prepare password
	char[] characters = null;
	if (password != null) {
	    characters = password.toCharArray();
	}

	// create the key
	KeySpec keySpec = new PBEKeySpec(characters);
	//http://java.sun.com/javase/6/docs/technotes/guides/security/SunProviders.html#SunJCEProvider
	String algorithm = "PBEWithSHA1AndDESede";
	SecretKey key = SecretKeyFactory.getInstance(algorithm).generateSecret(
		keySpec);
	Cipher cipher = Cipher.getInstance(key.getAlgorithm());

	//8-byte salt
	final byte[] salt = {(byte) 'v', (byte) 'i', (byte) 's', (byte) 'p',
	    (byte) '.', (byte) 'n', (byte) 'e', (byte) 't',};
	final int iterationCount = 8192;
	AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
		iterationCount);

	// create the cipher
	cipher.init(mode, key, paramSpec);

	return cipher;
    }

    /**
     * 93% faster than using DecimalFormat
     *
     * @param d
     * @param place
     * @return
     */
    public static double round(double d, int place) {
	if (place < 1) {
	    return (long) d;
	}
	int base = 10;
	double precision = Math.pow(base, place);
	return Math.round(d * precision) / precision;
    }

    /**
     * Formats a floating point number to two places past the decimal with
     * grouping disabled. This is the same as calling
     * <tt>Utils.format(d, 2);</tt>
     *
     * @param d the number to be formatted
     * @return the formatted number
     * @see Utils#format(double d, int decimal)
     */
    public static String format(double d) {
	return format(d, 2);
    }

    /**
     * Formats a floating point number to
     * <code>d</code> places past the decimal with grouping disabled.
     *
     * @param d the number to be formatted
     * @param decimal places past the decimal point to truncate the number
     * @return the formatted number
     * @see Utils#format(double d, int decimal, boolean useGrouping)
     */
    public static String format(double d, int decimal) {
	return format(d, decimal, false);
    }

    /**
     * Formats a floating point number to
     * <code>d</code> places past the decimal.
     *
     * @param d the number to be formatted
     * @param decimal places past the decimal point to truncate the number
     * @param useGrouping whether or not to use grouping (thousands, millions,
     * ect...)
     * @return the formatted number
     */
    public static String format(double d, int decimal, boolean useGrouping) {
	d = round(d, decimal); //pre-round to avoid -0.00
	NumberFormat formatter = NumberFormat.getNumberInstance();
	formatter.setMinimumFractionDigits(decimal);
	formatter.setMaximumFractionDigits(decimal);
	formatter.setGroupingUsed(useGrouping);
	return formatter.format(d);
    }

    public static String format(BigDecimal d) {
	NumberFormat formatter = NumberFormat.getNumberInstance();
	return formatter.format(d);
    }

    /**
     * Formats a number to
     * <code>d</code> places past the decimal. Trims trailing zeroes
     *
     * @param d the number to be formatted
     * @param useGrouping whether or not to use grouping (thousands, millions,
     * ect...)
     * @return the formatted number
     */
    public static String format(Object d, boolean useGrouping) {
	DecimalFormat formatter = new DecimalFormat();
	formatter.setGroupingUsed(useGrouping);
	return formatter.format(d);
    }

    /**
     * alias of
     * <code>Toolkit.getDefaultToolkit().beep();</code>
     *
     * @see java.awt.Toolkit#beep
     * @deprecated this is more for reference than anything else
     */
    @Deprecated
    public static void beep() {
	Toolkit.getDefaultToolkit().beep();
    }

    public static BufferedImage screenCapture() throws AWTException {
	return screenCapture(new Robot());
    }

    public static BufferedImage screenCapture(Robot robot) throws AWTException {
	if (robot == null) {
	    throw new IllegalArgumentException("robot must not be null");
	}
	Rectangle bounds = getScreenDeviceBounds();
//		int area = bounds.width * bounds.height;
//		int bytesPerInt = 4;
//		double kb = (area * bytesPerInt) / 1024d;
//		int place = 2;
//		double mb = round(kb / 1024d, place);
//		System.err.println(bounds + "\t" + area + " px sq\t" + mb + " mB");
	return robot.createScreenCapture(bounds);
    }

    public static Rectangle getScreenDeviceBounds() {
	GraphicsEnvironment ge = GraphicsEnvironment
		.getLocalGraphicsEnvironment();
	GraphicsDevice[] devices = ge.getScreenDevices();
	Rectangle bounds = devices[0].getDefaultConfiguration().getBounds();
	for (int i = 1; i < devices.length; i++) {
	    bounds.add(devices[i].getDefaultConfiguration().getBounds());
	}
	return bounds;
    }

    public static BufferedImage screenCapture(GraphicsDevice device) throws AWTException {
	return screenCapture(new Robot(), device);
    }

    /**
     *
     * @param robot
     * @param device
     * @return
     * @throws AWTException
     * @throws IndexOutOfBoundsException
     */
    public static BufferedImage screenCapture(Robot robot, GraphicsDevice device)
	    throws AWTException {
	if (robot == null) {
	    throw new IllegalArgumentException("robot must not be null");
	}
	GraphicsConfiguration gc = device.getDefaultConfiguration();
	return robot.createScreenCapture(gc.getBounds());
    }

    public static BufferedImage screenCaptureDefault() throws AWTException {
	return screenCaptureDefault(new Robot());
    }

    public static BufferedImage screenCaptureDefault(Robot robot)
	    throws AWTException {
	if (robot == null) {
	    String message = "robot must not be null";
	    throw new IllegalArgumentException(message);
	}
	GraphicsEnvironment ge = GraphicsEnvironment
		.getLocalGraphicsEnvironment();
	GraphicsDevice gd = ge.getDefaultScreenDevice();
	GraphicsConfiguration gc = gd.getDefaultConfiguration();
	return robot.createScreenCapture(gc.getBounds());
    }

    public static BufferedImage capture(Component c) {
	return capture(c, null);
    }

    public static BufferedImage capture(Component c, Color background) {
	if (c == null) {
	    throw new IllegalArgumentException("component must not be null");
	}
	Rectangle bounds = c.getBounds(); // cache bounds
	c.setBounds(new Rectangle(c.getPreferredSize()));

	BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(),
		BufferedImage.TYPE_INT_RGB);
	Graphics g = image.getGraphics();
	if (background != null) {
	    g.setColor(background);
	    int x = 0, y = 0;
	    g.fillRect(x, y, image.getWidth(), image.getHeight());
	}
	c.paint(g);
	g.dispose();

	c.setBounds(bounds); // restore bounds
	return image;
    }

    public static BufferedImage screenCapture(Component c) throws AWTException {
	return screenCapture(new Robot(), c);
    }

    public static BufferedImage screenCapture(Robot robot, Component c)
	    throws AWTException {
	if (robot == null) {
	    throw new IllegalArgumentException("robot must not be null");
	}
	Rectangle bounds = new Rectangle(c.getLocationOnScreen(), c.getSize());
	return robot.createScreenCapture(bounds);
    }

    public static <C extends CharSequence> C chop(C sequence) {
	if (sequence == null) {
	    throw new IllegalArgumentException("sequence must not be null");
	} else if (sequence.length() == 0) {
	    return sequence;
	}
	int start = 0, end = sequence.length() - 1;
	return (C) sequence.subSequence(start, end);
    }

    public static String toMD5(String input) {
	return toMD5(input.getBytes());
    }

    public static String toMD5(byte[] input) {
	String algorithm = "MD5";
	try {
	    return hash(input, algorithm);
	} catch (NoSuchAlgorithmException ex) {
	    throw new RuntimeException(ex);
	}
    }

    public static String toSHA1(byte[] input) {
	String algorithm = "SHA-1";
	try {
	    return hash(input, algorithm);
	} catch (NoSuchAlgorithmException ex) {
	    throw new RuntimeException(ex);
	}
    }

    public static String hash(byte[] input, String algorithm)
	    throws NoSuchAlgorithmException {
	MessageDigest message = MessageDigest.getInstance(algorithm);
	message.reset(); //reset previous use, if any
	message.update(input);
	return hash(message.digest());
    }

    /**
     *
     * @param val
     * @return
     * @throws IllegalArgumentException if <code>val</code> is <code>null</code>
     */
    public static String hash(byte[] bytes) {
	if (bytes == null) {
	    throw new IllegalArgumentException("bytes must not be null");
	}
	final int length = 2;
	StringBuilder hash = new StringBuilder();
	for (byte b : bytes) {
	    String hex = Integer.toHexString(0xFF & b);
	    hex = padLeft(hex, '0', length);
	    hash.append(hex);
	}
	return hash.toString();
    }

    public static boolean instanceOf(Class<?> obj, final Class<?> c) {
	if (obj == null || c == null) {
	    throw new IllegalArgumentException("neither obj nor c may be null");
	}
	do {
	    if (obj == c) {
		return true;
	    }
	    for (Class<?> i : obj.getInterfaces()) { //implemented interfaces
		if (i == c) {
		    return true;
		}
		for (Class<?> si : i.getInterfaces()) { //superinterfaces
		    if (si == c) {
			return true;
		    }
		}
	    }
	} while ((obj = obj.getSuperclass()) != null); //parent classes
	return false;
    }

    public static String def(Object o) {
	return o == null ? "" : o.toString();
    }

    public static <T, E extends T> T def(T obj, E def) {
	if (def == null) {
	    String message = "def must not be null";
	    throw new IllegalArgumentException(message);
	}
	return obj == null ? def : obj;
    }

    public <T, E extends T> T def(T obj, E def, Class<E> cast) {
	return cast.cast(def(obj, def));
    }

    /**
     * @deprecated use {@link Arrays#copyOfRange(Object[], int, int)} instead
     * @param array the source array
     * @param srcPos starting position in the source array
     * @param length the number of array elements to be copied
     * @return
     * @throws NullPointerException if <code>array</code> is <code>null</code>
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static <E> E[] subarray(E[] array, int srcPos, int length) {
	Class<E> componentType = (Class<E>) array.getClass().getComponentType();
	E[] dest = (E[]) Array.newInstance(componentType, length);
	System.arraycopy(array, srcPos, dest, 0, length);
	return dest;
    }

    public static void invokeNowOrLater(Runnable runnable) {
	if (EventQueue.isDispatchThread()) {
	    runnable.run();
	} else {
	    EventQueue.invokeLater(runnable);
	}
    }

    /**
     * A drop-in replacement for {@link Matcher#quoteReplacement(String)}.
     *
     * @see Matcher#quoteReplacement(String)
     * @return a literal string replacement
     */
    public static String escapeRegularExpression(String s) {
	String regex = "[\\W]";
	Pattern pattern = Pattern.compile(regex);
	Matcher matcher = pattern.matcher(s);
	StringBuffer sb = new StringBuffer();
	while (matcher.find()) {
	    int start = matcher.start(), end = matcher.end();
	    String match = s.substring(start, end);
	    String replacement = "\\\\\\" + match;
	    matcher.appendReplacement(sb, replacement);
	}
	matcher.appendTail(sb);
	return sb.toString();
    }

    /**
     * primitive array <-> Object array conversion
     */
    public static byte[] toPrimitive(Byte... bytes) {
	byte[] buf = new byte[bytes.length];
	for (int i = 0; i < bytes.length; i++) {
	    buf[i] = bytes[i].byteValue();
	}
	return buf;
    }

    public static short[] toPrimitive(Short... shorts) {
	short[] buf = new short[shorts.length];
	for (int i = 0; i < shorts.length; i++) {
	    buf[i] = shorts[i].shortValue();
	}
	return buf;
    }

    public static int[] toPrimitive(Integer... integers) {
	int[] buf = new int[integers.length];
	for (int i = 0; i < integers.length; i++) {
	    buf[i] = integers[i].intValue();
	}
	return buf;
    }

    public static long[] toPrimitive(Long... longs) {
	long[] buf = new long[longs.length];
	for (int i = 0; i < longs.length; i++) {
	    buf[i] = longs[i].longValue();
	}
	return buf;
    }

    public static float[] toPrimitive(Float... floats) {
	float[] buf = new float[floats.length];
	for (int i = 0; i < floats.length; i++) {
	    buf[i] = floats[i].floatValue();
	}
	return buf;
    }

    public static double[] toPrimitive(Double... doubles) {
	double[] buf = new double[doubles.length];
	for (int i = 0; i < doubles.length; i++) {
	    buf[i] = doubles[i].longValue();
	}
	return buf;
    }

    public static boolean[] toPrimitive(Boolean... booleans) {
	boolean[] buf = new boolean[booleans.length];
	for (int i = 0; i < booleans.length; i++) {
	    buf[i] = booleans[i].booleanValue();
	}
	return buf;
    }

    public static char[] toPrimitive(Character... characters) {
	char[] buf = new char[characters.length];
	for (int i = 0; i < characters.length; i++) {
	    buf[i] = characters[i].charValue();
	}
	return buf;
    }

    public static Byte[] toObject(byte... bytes) {
	Byte[] buf = new Byte[bytes.length];
	for (int i = 0; i < bytes.length; i++) {
	    buf[i] = Byte.valueOf(bytes[i]);
	}
	return buf;
    }

    public static Short[] toObject(short... shorts) {
	Short[] buf = new Short[shorts.length];
	for (int i = 0; i < shorts.length; i++) {
	    buf[i] = Short.valueOf(shorts[i]);
	}
	return buf;
    }

    public static Integer[] toObject(int... ints) {
	Integer[] buf = new Integer[ints.length];
	for (int i = 0; i < ints.length; i++) {
	    buf[i] = Integer.valueOf(ints[i]);
	}
	return buf;
    }

    public static Long[] toObject(long... longs) {
	Long[] buf = new Long[longs.length];
	for (int i = 0; i < longs.length; i++) {
	    buf[i] = Long.valueOf(longs[i]);
	}
	return buf;
    }

    public static Float[] toObject(float... floats) {
	Float[] buf = new Float[floats.length];
	for (int i = 0; i < floats.length; i++) {
	    buf[i] = Float.valueOf(floats[i]);
	}
	return buf;
    }

    public static Double[] toObject(double... doubles) {
	Double[] buf = new Double[doubles.length];
	for (int i = 0; i < doubles.length; i++) {
	    buf[i] = Double.valueOf(doubles[i]);
	}
	return buf;
    }

    public static Boolean[] toObject(boolean... booleans) {
	Boolean[] buf = new Boolean[booleans.length];
	for (int i = 0; i < booleans.length; i++) {
	    buf[i] = Boolean.valueOf(booleans[i]);
	}
	return buf;
    }

    public static Character[] toObject(char... characters) {
	Character[] buf = new Character[characters.length];
	for (int i = 0; i < characters.length; i++) {
	    buf[i] = Character.valueOf(characters[i]);
	}
	return buf;
    }

    /**
     * @deprecated Path separator (":" on UNIX)
     * @return Path separator (":" on UNIX)
     * @throws SecurityException if a security manager exists and its
     * checkPropertyAccess method doesn't allow access to the specified system
     * property.
     * @see java.lang.System#getProperty(String)
     */
    @Deprecated
    public static String getPathSeparator() {
	return System.getProperty("path.separator");
    }

    /**
     * @deprecated Line separator ("\n" on UNIX)
     * @return Line separator ("\n" on UNIX)
     * @throws SecurityException if a security manager exists and its
     * checkPropertyAccess method doesn't allow access to the specified system
     * property.
     * @see java.lang.System#getProperty(String)
     */
    @Deprecated
    public static String getLineSeparator() {
	return System.getProperty("line.separator");
    }

    /**
     * @deprecated @param value
     * @param def
     * @return
     */
    @Deprecated
    public static int getInteger(Object value, int def) {
	if (value == null) {
	    return def;
	} else if (value instanceof Integer) {
	    return (Integer) value;
	}
	return Integer.parseInt(value.toString());
    }

    /**
     *
     * @param <E>
     * @param <C>
     * @param collections
     * @return elements common to all collections
     */
    public static <E> Set<E> getCommonElements(Collection<E>... collections) {
	Set<E> common = new LinkedHashSet<E>();
	for (Collection<E> collection : collections) {
	    common.addAll(collection);
	}
	for (Collection<E> collection : collections) {
	    common.retainAll(collection);
	}
	return common;
    }

    /**
     *
     * @param <E>
     * @param <C>
     * @param collections
     * @return elements which exist in only one collection
     */
    public static <E> Set<E> getUniqueElements(Collection<E>... collections) {
	Set<E> uniqueSet = new LinkedHashSet<E>();
	Set<E> tempSet = new LinkedHashSet<E>();
	for (int i = 0; i < collections.length; i++) {
	    tempSet.addAll(collections[i]);
	    for (int j = 0; j < collections.length; j++) {
		if (i == j) {
		    continue;
		}
		tempSet.removeAll(collections[j]);
	    }
	    uniqueSet.addAll(tempSet);
	    tempSet.clear();
	}
	return uniqueSet;
    }

    public static boolean isValidEmail(String email) {
	String regex = "^(([0-9a-zA-Z]|[0-9a-zA-Z][-.\\w]*[0-9a-zA-Z])@(([0-9a-zA-Z]|[0-9a-zA-Z][-\\w]*[0-9a-zA-Z])\\.)+[a-zA-Z]{2,9})$";
	return email.matches(regex);
    }

    public static boolean isValidEmails(String multipleEmails) {
	String[] emails = multipleEmails.split(",");
	boolean valid = (emails.length > 0);
	for (String email : emails) {
	    email = email.trim();
	    if (email.isEmpty() || !isValidEmail(email)) {
		valid = false;
		break;
	    }
	}
	return valid;
    }

    public static String formatDate(Date date, String pattern) {
	SimpleDateFormat formatter = new SimpleDateFormat(pattern);
	return formatter.format(date);
    }

    public static Date addMonth(Calendar calendarDate, int months) {
	calendarDate.add(Calendar.MONTH, months);
	return calendarDate.getTime();
    }

    public static Date addMonth(Date date, int months) {
	Calendar calendarDate = Calendar.getInstance();
	if (date == null) {
	    return null;
	} else {
	    calendarDate.setTime(date);
	}
	calendarDate.add(Calendar.MONTH, months);
	return calendarDate.getTime();
    }

    public static Date stringToDate(String date, String format) {
	final SimpleDateFormat formatter = new SimpleDateFormat(format);
	try {
	    return formatter.parse(date);
	} catch (ParseException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static String formatMACAddress(String macAddress) {
	macAddress = macAddress.trim().replaceAll("[^0-9a-zA-Z]", "");
	if (macAddress.length() < 12) {
	    return "";
	}
	StringBuilder sb = new StringBuilder();
	Formatter formatter = new Formatter(sb);
	String pattern = "%2s:%2s:%2s:%2s:%2s:%2s";
	formatter.format(pattern, macAddress.substring(0, 2), macAddress
		.substring(2, 4), macAddress.substring(4, 6), macAddress
		.substring(6, 8), macAddress.substring(8, 10), macAddress
		.substring(10, 12));
	macAddress = sb.toString();
	return macAddress.trim();
    }

    public static String formatZip(String value) {
	return formatPostal(value, "US");
    }

    public static String formatPostal(String value, String countryCode) {
	StringBuilder sb = new StringBuilder();
	if (countryCode.equalsIgnoreCase("CA")) {
	    final int LENGTH = 6;
	    Formatter formatter = new Formatter(sb, Locale.CANADA);
	    value = value.trim().replaceAll("[^0-9a-zA-Z]", "");
	    if (value.length() == LENGTH) {
		String first = value.substring(0, 3);
		String last = value.substring(3);
		formatter.format("%3s-%3s", first, last);
		value = sb.toString();
	    }
	} else if (countryCode.equalsIgnoreCase("ZA")) {
	    final int ZIP = 4;
	    final int ZIP_PLUS_FOUR = 8;
	    Formatter formatter = new Formatter(sb, new Locale("za", "ZA"));
	    value = value.trim().replaceAll("[^0-9]", "");
	    if (value.length() == ZIP || value.endsWith("0000")) {
		String zip = value.substring(0, 4);
		formatter.format("%4s", zip);
		value = sb.toString();
	    } else if (value.length() == ZIP_PLUS_FOUR) {
		String zip = value.substring(0, 4);
		String plusFour = value.substring(4);
		formatter.format("%4s-%4s", zip, plusFour);
		value = sb.toString();
	    }
	} else {
	    final int ZIP = 5;
	    final int ZIP_PLUS_FOUR = 9;
	    Formatter formatter = new Formatter(sb, Locale.US);
	    value = value.trim().replaceAll("[^0-9]", "");
	    if (value.length() == ZIP || value.endsWith("0000")) {
		String zip = value.substring(0, 5);
		formatter.format("%5s", zip);
		value = sb.toString();
	    } else if (value.length() == ZIP_PLUS_FOUR) {
		String zip = value.substring(0, 5);
		String plusFour = value.substring(5);
		formatter.format("%5s-%4s", zip, plusFour);
		value = sb.toString();
	    }
	}
	return value;
    }

    public static boolean isMac() {
	return System.getProperty("os.name").toLowerCase().startsWith("mac");
    }

    public static boolean isLinux() {
	return System.getProperty("os.name").toLowerCase().startsWith("Linux");
    }

    public static boolean isWindows() {
	return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    public static String toOrdinal(String s) {
	if (s.endsWith("1") && !s.endsWith("11")) {
	    s += "st";
	} else if (s.endsWith("2") && !s.endsWith("12")) {
	    s += "nd";
	} else if (s.endsWith("3") && !s.endsWith("13")) {
	    s += "rd";
	} else {
	    s += "th";
	}
	return s;
    }

    public static long getIpAddress(String ipAddress) {
	try {
	    int signedVal = InetAddress.getByName(ipAddress).hashCode();
	    return toUnsigned(signedVal);
	} catch (Exception e) {
	    // Invalid IP address!
	    e.printStackTrace();
	}
	return 0;
    }

    public static long toUnsigned(int val) {
	long retval = 0;
	if (val > 0) {
	    retval = val;
	} else {
	    // val is negative, and the sign bit is set.
	    // so, we use the hex representation to get things right.
	    retval = Long.parseLong(Integer.toHexString(val), 16); // hex
	}
	return retval;
    }

    public static String getClipboard() {
	Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
	try {
	    if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
		String text = (String) t.getTransferData(DataFlavor.stringFlavor);
		return text;
	    }
	} catch (UnsupportedFlavorException e) {
	} catch (IOException e) {
	}
	return null;
    }

    public static interface VCallback {

	void run();
    }

    public static String[] removeDuplicates(String[] array) {
	List<String> list = Arrays.asList(array);
	Set<String> set = new HashSet<String>(list);
	return set.toArray(new String[set.size()]);
    }
}