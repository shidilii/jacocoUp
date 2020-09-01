package org.jacoco.example.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 线程安全的DateUtil类
 * @Title: 线程安全的日期类型处理类
 * @Description: 为了做到线程安全的同时又使创建的SimpleDateFormat类型的对象最少
 * @author: pengjy2
 * @date: 2016年2月18日 下午2:45:56
 */
public class DateUtil {
	
	public final static String h_yyyy_MM_dd_HH_mm_ss="yyyy-MM-dd HH:mm:ss";
	public final static String h_yyyy_MM_dd="yyyy-MM-dd";
	public final static String X_yyyy_MM_dd="yyyy/MM/dd";
	public final static String x_MM_dd_HH_mm="MM/dd HH:mm";
	public final static String x_yyyy_MM_dd_HH_mm_ss = "yyyy/MM/dd HH:mm:ss";

    /** 锁对象 */
    private static final Object lockObj = new Object();

    /** 存放不同的日期模板格式的sdf的Map */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     * 
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {

                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {

                        @Override
                        protected SimpleDateFormat initialValue() {
                            System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    /**
     * 是用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateStr);
    }
}