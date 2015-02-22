package edu.neu.madcourse.cognitosample;

import com.amazonaws.regions.Regions;

/**
 * Created by kevin on 1/14/15.
 */
public class AwsConstants {
    public static final String AWS_ACCOUNT_ID = "";
    public static final String AWS_IDENTITY_POOL_ID = "";
    public static final String AWS_UNAUTH_ROLE_ARN = "";
    public static final String AWS_AUTH_ROLE_ARN = "";
    public static final Regions AWS_REGION = Regions.US_EAST_1;
    public static final String AWS_DATASET_NAME = "TestDataSet";

    // AWS Developer credentials.
    public static final String AWS_DEVELOPER_CREDENTIAL_KEY = "";
    public static final String AWS_DEVELOPER_CREDENTIAL_SECRETE = "";
    // Cognito Identity Pool's developer provider name, must match the identity pool's developper provider name you set.
    public static final String AWS_DEVELOPER_PROVIDER_NAME = "";
    // Set the developer authentication mode's domain. It is just String value set by you.
    public static final String AWS_DEV_AUTH_DOMAIN = "domain.developer.com";
}
