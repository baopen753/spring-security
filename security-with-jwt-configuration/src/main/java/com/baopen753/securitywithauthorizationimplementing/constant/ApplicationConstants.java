package com.baopen753.securitywithauthorizationimplementing.constant;

/**
 *  This class is created for containing JWT_KEY defined by ourselves
 *  ? Why we need to define by ourselves ?  + for security
 *                                          + secret should be unique and unpredictable to prevent unauthorized access
 */

public final class ApplicationConstants {
    public static final String JWT_SECRET_KEY = "JWT_SECRET";
    public static final String JWT_SECRET_DEFAULT_VALUE = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4";
    public static final String JWT_HEADER = "Authorization";
}
