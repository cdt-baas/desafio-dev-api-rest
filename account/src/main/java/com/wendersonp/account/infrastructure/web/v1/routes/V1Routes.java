package com.wendersonp.account.infrastructure.web.v1.routes;


public class V1Routes {

    private V1Routes() {}

    public static final String BASE_PATH = "/api/v1";

    public static final String ACCOUNT_PATH = BASE_PATH + "/account";

    public static final String ACCOUNT_MOVEMENT_PATH = "/movement";

    public static final String ACCOUNT_BLOCK_PATH = "/{identifier}/block";

    public static final String ACCOUNT_UNBLOCK_PATH = "/{identifier}/unblock";

    public static final String MOVEMENT_PATH = BASE_PATH + "/movement";
}
