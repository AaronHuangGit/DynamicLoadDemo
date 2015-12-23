package com.aaron.android.codelibrary.utils;

/**
 * Created on 15/7/7.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class ValidateUtils {
    /**
     * 验证是否是正常手机号码
     * @param number
     * @return
     */
    public static boolean isMobile(String number) {
/*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、(1349卫通)
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3578]\\d{9}";
        if (StringUtils.isEmpty(number)) {
            return false;
        }
        return number.matches(telRegex);
    }
}
