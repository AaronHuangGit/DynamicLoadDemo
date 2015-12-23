package com.aaron.android.codelibrary.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author hu.cao
 * @version 1.0.0
 */
public class SecurityUtils {

    private static final int ROUND_8BITS = 0x100;
    private static final int ROUND_4BITS = 0x10;
    private static final int RADIX_16 = 16;

    /**
     * Md5 Encryption
     * @author hu.cao
     * @version 1.0.0
     */
    public static class MD5 {
        private static final String ALGORITHM = "MD5";

        /**
         * 对字符串进行md5加密处理
         * @param string 原始字符串
         * @return 加密后的字符串 16位
         */
        public static String get16MD5String(String string) {
            byte[] md5 = stringToMD5(string);
            return to16HexString(md5);
        }

        /**
         * 对字符串进行md5加密处理
         * @param string 原始字符串
         * @return 加密后的字符串 32位
         */
        public static String get32MD5String(String string) {
            byte[] md5 = stringToMD5(string);
            return to32HexString(md5);
        }

        private static byte[] stringToMD5(String string) {
            if (string == null) {
                return null;
            }

            try {
                MessageDigest digest = java.security.MessageDigest.getInstance(ALGORITHM);
                digest.update(string.getBytes());
                return digest.digest();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private static String to16HexString(byte[] md5) {
            if (md5 == null) {
                return null;
            }

            final int beginConvertIndex = 4;
            final int endConvertIndex = 11;
            final int intValueFF = 0xFF;
            StringBuilder stringBuffer = new StringBuilder();
            for (int i = beginConvertIndex; i <= endConvertIndex; ++i) {
                stringBuffer.append(Integer.toHexString(intValueFF & md5[i]));
            }
            return stringBuffer.toString();
        }

        private static String to32HexString(byte[] md5) {
            if (md5 == null) {
                return null;
            }

            int val;
            StringBuilder stringBuffer = new StringBuilder("");
            for (byte b : md5) {
                val = b;
                if (val < 0) {
                    val += ROUND_8BITS;
                }
                if (val < ROUND_4BITS) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(val));
            }

            return stringBuffer.toString();
        }
    }

    /**
     * TEA encrypt
     * @author ke.di
     * @version 2.0.0
     * @since Aug 12, 2011
     */
    public static final class TEA {
        private static final int DELTA = 0x9E3779B9;
        private static final int LOWER_LIMIT = 48;
        private static final int UPPER_LIMIT = 59;

        private static final int CONSTANT_5 = 5;
        private static final int CONSTANT_8 = 8;
        private static final int CONSTANT_10 = 10;
        private static final int CONSTANT_16 = 16;
        private static final int CONSTANT_32 = 32;
        private static final int INIT_SUM_ROUND32 = 0xC6EF3720;
        private static final int INIT_SUM_ROUND16 = 0xE3779B90;

        private static final int[] TEA_KEY = {0x091458856, 0x0FD2845C6, 0x0B681F893, 0x02F7C49B7};

        private static final int DEFAULT_ROUND = 16;

        /**
         * 对字符串进行加密
         *
         * @param string 原始字符串
         * @return 加密后的字符串
         */
        public static String encrypt(String string) {
            if (StringUtils.isEmpty(string)) {
                return ConstantUtils.BLANK_STRING;
            }

            int[] plain = new int[2];
            try {
                plain = preProcess(string);
            } catch (IllegalArgumentException e) {
                return ConstantUtils.BLANK_STRING;
            }

            int[] ret = teaEncrypt(plain, TEA_KEY, DEFAULT_ROUND);

            String values = String.format("%08x%08x", ret[0], ret[1]).toLowerCase();
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < values.length(); i++) {
                String buffer = values.substring(i, i + 1);
                int nValues = Integer.valueOf(buffer, CONSTANT_16);
                if (nValues >= CONSTANT_10) {
                    result.append(nValues - CONSTANT_10);
                } else {
                    result.append(nValues);
                }
            }

            return result.toString();
        }

        private static int[] preProcess(String string) {
             StringBuilder stringBuilder = new StringBuilder(string.trim());

            if (stringBuilder.toString().equals("0")) {
                throw new IllegalArgumentException();
            }

            boolean hasCharacter = false;
            for (int i = 0; i < stringBuilder.length(); i++) {
                int c = stringBuilder.charAt(i);
                if ((c < LOWER_LIMIT) || c > UPPER_LIMIT) {
                    hasCharacter = true;
                    break;
                }
            }

            if (stringBuilder.length() < CONSTANT_16) {
                while (stringBuilder.length() < CONSTANT_16) {
                    stringBuilder.append("0");
                }
            }

            long l1;
            long l2;
            if (hasCharacter) {
                Long tmp1 = Long.parseLong(stringBuilder.substring(0, CONSTANT_8), RADIX_16);
                Long tmp2 = Long.parseLong(stringBuilder.substring(CONSTANT_8, CONSTANT_16), RADIX_16);
                l1 = tmp1;
                l2 = tmp2;
            } else {
                l1 = Long.valueOf(stringBuilder.substring(0, CONSTANT_8), RADIX_16);
                l2 = Long.valueOf(stringBuilder.substring(CONSTANT_8, CONSTANT_16), RADIX_16);
            }

            int[] plain = new int[2];
            plain[0] = (int) l1;
            plain[1] = (int) l2;

            return plain;
        }

        /**
         * encrypt with tea algorithm
         *
         * @param aIn  input 4-bytes
         * @param aKey key used for encrypt
         * @param aRound loop count
         * @return encrypted output 4-bytes
         */
        public static int[] teaEncrypt(int[] aIn, int[] aKey, int aRound) {
            int y = aIn[0], z = aIn[1], sum = 0; /* set up */
            int a = aKey[0], b = aKey[1], c = aKey[2], d = aKey[3]; /* cache key */
            int i;
            for (i = 0; i < aRound; i++) { /* basic cycle start */
                sum += DELTA;

                y += ((z << 4) + a) ^ (z + sum) ^ ((z >>> CONSTANT_5) + b);
                z += ((y << 4) + c) ^ (y + sum) ^ ((y >>> CONSTANT_5) + d); /* end cycle */
            }
            int[] out = new int[2];
            out[0] = y;
            out[1] = z;
            return out;
        }

        /**
         * decrypt with tea algorithm
         *
         * @param aOut decrypt output 4-bytes
         * @param aIn encrypted input 4-bytes
         * @param aKey key used for decrypt
         * @param aRound loop count
         */
        public static void teaDecrypt(int[] aOut, int[] aIn, int[] aKey, int aRound) {
            int y = aIn[0], z = aIn[1], sum;
            int a = aKey[0], b = aKey[1], c = aKey[2], d = aKey[3];

            if (aRound == CONSTANT_32) {
                sum = INIT_SUM_ROUND32;
            } else if (aRound == CONSTANT_16) {
                sum = INIT_SUM_ROUND16;
            } else {
                sum = DELTA * aRound;
            }

            for (int i = 0; i < aRound; i++) {
                z -= (((y << 4) + c) ^ (y + sum) ^ ((y >>> CONSTANT_5) + d));
                y -= (((z << 4) + a) ^ (z + sum) ^ ((z >>> CONSTANT_5) + b));
                sum -= DELTA;
            }
            aOut[0] = y;
            aOut[1] = z;
        }
    }


    /**
     * CR4 加密简单版
     * @author  yu.liu
     * @version 1.0.0
     */
    public static class RC4 {
        private static final int BYTE_MASK = 0xFF;

        private static final byte[] RAW_KEY = {99 , 7 , 66 , 74 , -81 , -36 , 45 , -18 , 106 , 25 , 126 , 6 , 71 , -67 , 47 , 108 ,
                116 , 117 , 113 , 121 , 78 , 109 , -119 , 62 , 29 , 23 , 24 , 76 , 125 , -62 , -89 , 0 ,
                -72 , 82 , 102 , 50 , -51 , 100 , 81 , 65 , -105 , -26 , -65 , 88 , -74 , 89 ,
                -69 , 18 , 87 , 44 , -23 , -20 , -63 , 36 , -88 , 79 , -32 , 26 , -115 , -10 , -127 , -49 ,
                124 , -122 , -6 , 122 , 91 , 12 , 70 , -29 , -31 , 14 , -2 , -12 , -123 , 84 , 27 , 97 , -87 ,
                -84 , 98 , 41 , 120 , 59 , 39 , -104 , 52 , 101 , 33 , 35 , -17 , 105 , -86 , -57 , 75 , -76 ,
                -128 , 64 , 3 , -33 , 119 , 95 , 57 , -73 , 60 , -68 , 1 , 69 , 46 , 86 , 42 , -98 , -99 , -9 , 118 ,
                -95 , -16 , -41 , 54 , -124 , 17 , -107 , 10 , -121 , -97 , -106 , 55 , -14 , -96 , -116 , 110 ,
                -4 , -94 , 56 , -21 , 30 , -55 , -47 , 31 , 58 , 37 , -46 , 83 , -30 , -93 , 61 , 123 , -120 , -85 ,
                -111 , -66 , -102 , -113 , -50 , -110 , -8 , -91 , 68 , 107 , 112 , -27 , -64 , 90 , -45 , 4 , -118 ,
                115 , -92 , 72 , -15 , 67 , -83 , -56 , -52 , 51 , -39 , -19 , -13 , 114 , 2 , -53 , -1 , 16 , -48 , -7 ,
                -58 , -117 , -77 , 93 , 53 , -101 , -38 , -126 , -44 , 11 , -59 , 103 , 5 , -80 , 63 , -82 , -114 , -5 ,
                -71 , -28 , -78 , -42 , -103 , 19 , 48 , -43 , 49 , 34 , -25 , 22 , 92 , 80 , -22 , 32 , 127 , 104 , -125 ,
                20 , 38 , 15 , 94 , -11 , -100 , 8 , -3 , -79 , 43 , -24 , 73 , 9 , -54 , -108 , -35 , 96 , -109 , 111 , -34 ,
                -40 , -60 , 77 , 40 , 28 , 13 , -70 , 21 , -75 , -90 , -37 , -112 , -61 , 85};

        /**
         * 加密
         * @param string 需要加密的字符串
         * @return 加密后的数据
         */
        public static String encrypt(String string) {
            if (string == null) {
                return null;
            }

            try {
               string = asString(rc4Base(string.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
               string = ConstantUtils.BLANK_STRING;
            }
            return toHexString(string);
        }

        /**
         * 解密
         * @param string 需要解密的字符串
         * @return 解密后的数据，错误返回 null
         */
        public static String decrypt(String string) {
            if (string == null) {
                return null;
            }

            return new String(rc4Base(hexString2Bytes(string)));
        }

        private static String asString(byte[] buf) {
            StringBuffer stringBuffer = new StringBuffer(buf.length);
            for (int i = 0; i < buf.length; i++) {
                stringBuffer.append((char) buf[i]);
            }
            return stringBuffer.toString();
        }

        private static String toHexString(String s) {
            String str = "";
            for (int i = 0; i < s.length(); i++) {
                int ch = (int) s.charAt(i);
                String s4 = Integer.toHexString(ch & BYTE_MASK);
                if (s4.length() == 1) {
                    s4 = '0' + s4;
                }
                str = str + s4;
            }
            return str;
        }

        private static byte[] hexString2Bytes(String src) {
            int size = src.length();
            byte[] ret = new byte[size / 2];
            byte[] tmp = src.getBytes();
            for (int i = 0; i < size / 2; i++) {
                ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
            }
            return ret;
        }

        private static byte uniteBytes(byte src0, byte src1) {
            char b0 = (char)Byte.decode("0x" + new String(new byte[] {src0}))
                    .byteValue();
            final int bitOffset = 4;
            b0 = (char) (b0 << bitOffset);
            char b1 = (char)Byte.decode("0x" + new String(new byte[] {src1}))
                    .byteValue();
            byte ret = (byte) (b0 ^ b1);
            return ret;
        }


        private static byte[] rc4Base(byte[] input) {
            int x = 0;
            int y = 0;
            byte[] key = RAW_KEY.clone();

            int xorIndex;
            byte[] result = new byte[input.length];

            for (int i = 0; i < input.length; i++) {
                x = (x + 1) & BYTE_MASK;
                y = ((key[x] & BYTE_MASK) + y) & BYTE_MASK;
                byte tmp = key[x];
                key[x] = key[y];
                key[y] = tmp;
                xorIndex = ((key[x] & BYTE_MASK) + (key[y] & BYTE_MASK)) & BYTE_MASK;
                result[i] = (byte) (input[i] ^ key[xorIndex]);
            }
            return result;
        }
    }

    /**
     * @version 1.0.0
     * @author yu.liu
     * @since 2012-10-17
     * 注意： 加密后的byte数组是不能强制转换成字符串的，
     * 换言之：字符串和byte数组在这种情况下不是互逆的；
     * 要避免这种情况，方便外界调用
     * 可以考虑将二进制数据转换成十六进制表示，
     * 故增加了 toHex 等几个函数进行封装
     */
    public static class AES {
        private static final String VIPARA = "0102030405060708";
        private static final byte[] RAW_KEY = {84, 65, -33, 92, 57, 90, -104, 0, 112, 74, 69, -53,
                106, -122, -107, -95};
        private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

        /**
         * 对字符串进行AES加密
         * @param string  未加密的字符串
         * @return 加密后的字符串
         * @throws Exception e
         */
        public static String encrypt(String string) throws Exception {
            byte[] encrypted = encrypt(RAW_KEY, string.getBytes());
            return toHex(encrypted);
        }

        /**
         * 解密
         * @param string 已加密的字符串
         * @return 解密后的字符串
         * @throws Exception e
         */
        public static String decrypt(String string) throws Exception {
            byte[] encrypted = toByte(string);
            byte[] result = decrypt(RAW_KEY, encrypted);
            return new String(result);
        }

        private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, zeroIv);
            byte[] encrypted = cipher.doFinal(clear);
            return encrypted;
        }

        private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, zeroIv);
            byte[] decrypted = cipher.doFinal(encrypted);
            return decrypted;
        }

        private static byte[] toByte(String hexString) {
            int len = hexString.length() / 2;
            byte[] result = new byte[len];
            for (int i = 0; i < len; i++) {
                result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), RADIX_16).byteValue();
            }
            return result;
        }

        private static String toHex(byte[] buf) {
            if (buf == null) {
                return "";
            }

            StringBuffer result = new StringBuffer(2 * buf.length);
            for (int i = 0; i < buf.length; i++) {
                appendHex(result, buf[i]);
            }
            return result.toString();
        }
        private final static String HEX = "0123456789ABCDEF";
        private static void appendHex(StringBuffer sb, byte b) {
            final int flagOfLowByte = 0x0f;
            final int shift = 4;
            sb.append(HEX.charAt((b >> shift) & flagOfLowByte)).append(HEX.charAt(b & flagOfLowByte));
        }

        /**
         * 为避免每次都生成rawkey, 提高效率，记录了一次生成的rawkey，
         *  客户端和服务器端采用相同rawkey就可以加密解密了。
         */
    //    private static byte[] getRawKey(byte[] seed) throws Exception {
    //        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    //        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    //        sr.setSeed(seed);
    //        final int kBitsOfAes = 128; // aes 的密钥长度
    //        keyGenerator.init(kBitsOfAes, sr); // 192 and 256 bits may not be available
    //        SecretKey secretKey = keyGenerator.generateKey();
    //        byte[] raw = secretKey.getEncoded();
    //        return raw;
    //    }
    }
}
