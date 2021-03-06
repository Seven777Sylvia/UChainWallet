package yooco.uchain.secretproject.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author : Seven
 * @date : 2018/11/2
 */
public class WalletUtils {
    private static final String TAG = WalletUtils.class.getSimpleName();

    public static String toDecString(String oxHexString, String assetDecimal) {
        if (TextUtils.isEmpty(oxHexString) || TextUtils.isEmpty(assetDecimal)) {
            CpLog.e(TAG, "oxHexString or assetDecimal is null!");
            return null;
        }

        int length = oxHexString.length();
        if (length < 3) {
            CpLog.w(TAG, "oxHexString.length < 3!");
            return "0";
        }

        String decString;
        try {
            String hexString = oxHexString.substring(2);
            String dec = new BigInteger(hexString, 16).toString(10);
            decString = new BigDecimal(dec).divide(new BigDecimal(10).pow(Integer.parseInt(assetDecimal)))
                    .stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            CpLog.e(TAG, "toDecString exception:" + e.getMessage());
            return null;
        }

        return decString;
    }

    public static String toHexString(String decString, String decimal) {
        if (TextUtils.isEmpty(decString) || TextUtils.isEmpty(decimal)) {
            CpLog.e(TAG, "decString or decimal is null!");
            return "";
        }

        String hexString;
        try {
            String bigDecString = new BigDecimal(decString).multiply(new BigDecimal(10).pow(Integer.valueOf(decimal)))
                    .setScale(0, BigDecimal.ROUND_DOWN).toPlainString();
            CpLog.i(TAG, "bigDecString:" + bigDecString);
            hexString = new BigInteger(bigDecString, 10).toString(16);
        } catch (Exception e) {
            CpLog.e(TAG, "toHexString Exception:" + e.getMessage());
            return "";
        }

        return "0x" + hexString;
    }

    public static String toLegalDecString(String decString, int decimal) {
        CpLog.i(TAG, "toLegalDecString, decString:" + decString);
        if (TextUtils.isEmpty(decString)) {
            CpLog.e(TAG, "decString or decimal is null!");
            return "";
        }

        String legalDecString;
        try {
            legalDecString = new BigDecimal(decString).setScale(decimal, BigDecimal.ROUND_DOWN).stripTrailingZeros()
                    .toPlainString();
        } catch (Exception e) {
            CpLog.e(TAG, "toLegalDecString Exception:" + e.getMessage());
            return "";
        }

        CpLog.i(TAG, "toLegalDecString, legalDecString:" + legalDecString);
        return legalDecString;
    }

    public static String toLegalGasPrice(String decString, int decimal) {
        if (TextUtils.isEmpty(decString)) {
            CpLog.e(TAG, "decString or decimal is null!");
            return "0";
        }

        String legalGasPrice;
        try {
            legalGasPrice = new BigDecimal(decString).setScale(decimal, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
                    .toPlainString();
        } catch (Exception e) {
            CpLog.e(TAG, "toLegalGasPrice Exception:" + e.getMessage());
            return "0";
        }

        return legalGasPrice;
    }

    public static byte[] reverseArray(String string) {
        if ("0".equals(string)) {
            byte[] zero = new byte[1];
            return zero;
        }
        byte[] array = hexStringToBytes(string);
        byte[] array_list = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            array_list[i] = (array[array.length - i - 1]);
        }
        return array_list;
    }

    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
