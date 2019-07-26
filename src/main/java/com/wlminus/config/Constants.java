package com.wlminus.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";
    
    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "vi";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String TIME_ZONE = "GMT+7";
    public static final String ALLOWED_FILE_TYPE[] = {"image/png","image/jpeg","image/gif"};
    public static final int MAX_FILE_NAME_SIZE = 1900;

    private Constants() {
    }
}
