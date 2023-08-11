package com.erp.webtoon.token;

public class TokenConst {
    public static final Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 60;        // ACCESS 토큰 만료 시간 (1시간)
    public static final Long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 6;   // REFRESH 토큰 만료 시간 (6시간)
    public static final Long REISSUE_EXPIRE_TIME = 1000L * 60 * 60 * 3;         // Reissue 만료 시간 (3시간)

    // header
    public static final String ALG_KEY = "alg";
    public static final String TYPE_KEY = "typ";
    public static final String TYPE_VALUE = "JWT";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String TOKEN_TYPE_REFRESH = "refresh_token";
    public static final String AUTHORITIES_KEY = "auth";

}
