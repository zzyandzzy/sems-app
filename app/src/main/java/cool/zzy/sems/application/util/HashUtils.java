package cool.zzy.sems.application.util;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/31 16:20
 * @since 1.0
 */
public class HashUtils {
    private static final int PASSWORD_HASH_LENGTH = 32;
    private static final int SALT_LENGTH = 32;

    /**
     * 去除掉md5中的随机盐
     *
     * @param md5 md5
     * @return passwordHash
     */
    public static String removeSalt(String md5) {
        if (md5.length() != PASSWORD_HASH_LENGTH + SALT_LENGTH) {
            return md5;
        }
        char[] passwordHash = new char[PASSWORD_HASH_LENGTH];
        for (int i = 0; i < PASSWORD_HASH_LENGTH + SALT_LENGTH; i += 2) {
            passwordHash[i / 2] = md5.charAt(i);
        }
        return new String(passwordHash);
    }
}
