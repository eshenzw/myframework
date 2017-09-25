package com.myframework.util;

/**
 * Created by zw on 2017/9/21.
 */

import com.myframework.config.MyframeworkConfig;
import com.myframework.core.common.utils.LocalhostIpFetcher;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IdUtil {

    private final static String DATACENTER_ID = MyframeworkConfig.getValue("datacenterId", "");

    private final static String WORKER_ID = MyframeworkConfig.getValue("workerId", "");

    final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z'};

    final static Map<Character, Integer> digitMap = new HashMap<Character, Integer>();

    final static SnowflakeIdWorker idWorker;

    static {
        for (int i = 0; i < digits.length; i++) {
            digitMap.put(digits[i], (int) i);
        }
        //
        if(StringUtil.isNullOrEmpty(DATACENTER_ID) || StringUtil.isNullOrEmpty(WORKER_ID)){
            /**
             * 这里在配置文件没有配置机房，机器id时，默认采用机器ip的二进制的后10位，其中高5位用作机房id(0-31)，低5位用作机器id(0-31)
             */
            String ipAddress = LocalhostIpFetcher.fetchLocalIP();
            String[] ipAddressByteArray = ipAddress.split("\\.");
            long dataCenterId = (long)(((Short.valueOf(ipAddressByteArray[ipAddressByteArray.length - 2]) & 0B11) << 3)
                                        | ((Short.valueOf(ipAddressByteArray[ipAddressByteArray.length - 1]) & 0B11100000) >> 5));
            long workerId = (long)(Short.valueOf(ipAddressByteArray[ipAddressByteArray.length - 1]) & 0B00011111);
            idWorker = new SnowflakeIdWorker(dataCenterId,workerId);
        }else{
            idWorker = new SnowflakeIdWorker(Long.valueOf(DATACENTER_ID), Long.valueOf(WORKER_ID));
        }
    }

    /**
     * 支持的最大进制数
     */
    private static final int MAX_RADIX = digits.length;

    /**
     * 支持的最小进制数
     */
    private static final int MIN_RADIX = 2;

    /**
     * 将长整型数值转换为指定的进制数（最大支持62进制，字母数字已经用尽）
     *
     * @param i
     * @param radix
     * @return
     */
    private static String toString(long i, int radix) {
        if (radix < MIN_RADIX || radix > MAX_RADIX)
            radix = 10;
        if (radix == 10)
            return Long.toString(i);

        final int size = 65;
        int charPos = 64;

        char[] buf = new char[size];
        boolean negative = (i < 0);

        if (!negative) {
            i = -i;
        }

        while (i <= -radix) {
            buf[charPos--] = digits[(int) (-(i % radix))];
            i = i / radix;
        }
        buf[charPos] = digits[(int) (-i)];

        if (negative) {
            buf[--charPos] = '-';
        }

        return new String(buf, charPos, (size - charPos));
    }

    private static NumberFormatException forInputString(String s) {
        return new NumberFormatException("For input string: \"" + s + "\"");
    }

    /**
     * 将字符串转换为长整型数字
     *
     * @param s     数字字符串
     * @param radix 进制数
     * @return
     */
    private static long toNumber(String s, int radix) {
        if (s == null) {
            throw new NumberFormatException("null");
        }

        if (radix < MIN_RADIX) {
            throw new NumberFormatException("radix " + radix
                    + " less than Numbers.MIN_RADIX");
        }
        if (radix > MAX_RADIX) {
            throw new NumberFormatException("radix " + radix
                    + " greater than Numbers.MAX_RADIX");
        }

        long result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        long limit = -Long.MAX_VALUE;
        long multmin;
        Integer digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') {
                if (firstChar == '-') {
                    negative = true;
                    limit = Long.MIN_VALUE;
                } else if (firstChar != '+')
                    throw forInputString(s);

                if (len == 1) {
                    throw forInputString(s);
                }
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
                digit = digitMap.get(s.charAt(i++));
                if (digit == null) {
                    throw forInputString(s);
                }
                if (digit < 0) {
                    throw forInputString(s);
                }
                if (result < multmin) {
                    throw forInputString(s);
                }
                result *= radix;
                if (result < limit + digit) {
                    throw forInputString(s);
                }
                result -= digit;
            }
        } else {
            throw forInputString(s);
        }
        return negative ? result : -result;
    }


    public static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return toString(hi | (val & (hi - 1)), MAX_RADIX).substring(1);
    }

    /**
     * 获取字符32位字符串的UUID（唯一）.
     *
     * @return the UUID
     */
    public static String getUUID()
    {
        String uuid = java.util.UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /**
     * 获取UUID的HashCode.
     *
     * @return the uUID hash code
     */
    public static long getUUIDHashCode()
    {
        return getUUID().hashCode();
    }

    /**
     * 获取Long型的UUID（唯一）.
     *
     * @return the UUID least bits
     */
    public static long getUUID2Long()
    {
        return java.util.UUID.randomUUID().getLeastSignificantBits() * -1;
    }

    /**
     * 产生19位的UUID
     *
     * @return
     */
    public static String getUUID19() {
        //产生UUID
        UUID uuid = UUID.randomUUID();
        StringBuilder sb = new StringBuilder();
        //分区转换
        sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
        sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
        sb.append(digits(uuid.getMostSignificantBits(), 4));
        sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
        sb.append(digits(uuid.getLeastSignificantBits(), 12));
        return sb.toString();
    }

    /**
     * 产生Twitter_Snowflake 递增唯一
     *
     * @param
     */
    public static long getSnowflakeId(){
        return idWorker.nextId();
    }

    public static void main(String[] args) {
        Long u1 = IdUtil.getSnowflakeId();
        Long u2 = IdUtil.getSnowflakeId();
        System.out.println(u1 + " " + (u1.compareTo(u2) < 0) + " " + u2);
    }

}