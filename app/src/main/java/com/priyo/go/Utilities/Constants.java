package com.priyo.go.Utilities;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.priyo.go.Model.PriyoNews.Categories.Cat;
import com.priyo.go.Model.PriyoNews.PriyoNews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constants {
    public static final String ApplicationTag = "priyo_People_v36";
    public static final String ApplicationPackage = "bd.com.ipay.people";

    public static final String USER_ADDRESS = "USER_ADDRESS";
    public static final String USER_DOB = "USER_DOB";
    public static final String USER_PROFESSION = "USER_PROFESSION";
    public static final String USER_THANA = "USER_THANA";
    public static final String USER_DISTRICT = "USER_DISTRICT";
    public static final String USER_ORGANIZATION = "USER_ORGANIZATION";
    public static final String USER_ADMIN_TAG = "USER_ADMIN_TAG";
    public static final String USER_ACCESS_LIST = "USER_ACCESS_LIST";


    public static final String COMMAND_POST_NEWS = "COMMAND_POST_NEWS";

    public static final String COMMAND_PROFILE_UPDATE = "COMMAND_PROFILE_UPDATE";
    public static final String COMMAND_ADD_FRIENDS = "COMMAND_ADD_FRIENDS";


    public static final String COMMAND_GET_BUSINESS_CATEGORY = "COMMAND_GET_BUSINESS_CATEGORY";
    public static final String COMMAND_GET_BUSINESS_DETAILS = "COMMAND_GET_BUSINESS_DETAILS";

    public static final String COMMAND_GET_PEOPLE_DETAILS = "COMMAND_GET_PEOPLE_DETAILS";

    public static final String COMMAND_GET_HOROSCOPE = "COMMAND_GET_HOROSCOPE";

    public static final String COMMAND_SEND_FEEDBACK = "COMMAND_SEND_FEEDBACK";

    public static final String COMMAND_LOCATION_SEND = "COMMAND_LOCATION_SEND";


    public static final String ERROR = "ERROR";
    public static final String LOGGED_IN = "LOGGED_IN";
    public static final String TARGET_FRAGMENT = "TARGET_FRAGMENT";
    public static final String SIGN_IN = "SIGN_IN";

    public static final String SMS_READER_BROADCAST_RECEIVER_PDUS = "pdus";


    public static final String RESOURCE_TOKEN = "resource-token";
    public static final String MESSAGE = "message";
    /**
     * All requests and responses to server, as well as token is printed when debug flag is enabled.
     * Besides, for safety measures, all later flags won't work unless DEBUG flag is set.
     */
    public static final boolean DEBUG = true;

    public static final String PRIYO_BASE_URL;

    // Signup Rest
    public static final String URL_PROFILE_UPDATE = "node/update";

    // User Rest (Profile Picture)
    public static final String URL_ADD_FRIENDS = "contacts";
    public static final String URL_FILTER_SLUG = "filter[where][slug]";
    public static final String URL_FILTER_CATEGORY = "filter[where][categories.slug]";
    public static final String URL_FILTER_TAG = "filter[where][tags.slug]";
    public static final String URL_FILTER_PEOPLE = "filter[where][people.slug]";
    public static final String URL_FILTER_LIMIT = "filter[limit]";
    public static final String URL_FILTER_SKIP = "filter[skip]";
    public static final int HTTP_RESPONSE_STATUS_NOT_ACCEPTABLE = 406;
    public static final int HTTP_RESPONSE_STATUS_NOT_FOUND = 404;
    public static final int HTTP_RESPONSE_STATUS_OK = 200;
    public static final int HTTP_RESPONSE_STATUS_PROCESSING = 102;
    public static final int HTTP_RESPONSE_STATUS_UNAUTHORIZED = 401;
    public static final int HTTP_RESPONSE_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_RESPONSE_STATUS_ACCEPTED = 202;
    public static final int HTTP_RESPONSE_STATUS_INTERNAL_ERROR = 500;
    public static final String RESULT = "Result";
    public static final String GET_REQUEST = "GET_RESULT: ";
    public static final String GET_URL = "GET_URL: ";
    public static final String DELETE_URL = "DELETE_URL: ";
    public static final String VERIFIED_USERS_ONLY = "VERIFIED_USERS_ONLY";
    public static final String IPAY_MEMBERS_ONLY = "IPAY_MEMBERS_ONLY";
    public static final String BUSINESS_ACCOUNTS_ONLY = "BUSINESS_ACCOUNTS_ONLY";
    public static final String SHOW_INVITED_ONLY = "SHOW_INVITED_ONLY";
    public static final String SHOW_NON_INVITED_NON_MEMBERS_ONLY = "SHOW_NON_INVITED_NON_MEMBERS_ONLY";
    public static final String HIDE_STATUSES = "HIDE_STATUSES";
    public static final String IMAGE_DIV = "<style type='text/css'>"
            + "img"
            + "{"
            + "    width:100%;"
            + "    height:auto;"
            + "    padding-top:5px;"
            + "}"
            + "p"
            + "{"
            + "    font-size: 16px;"
            + "}"
            + "iframe"
            + "{"
            + "    width:100%;"
            + "    padding-top:5px;"
            + "}"
            + "</style>";
    //Keys
    public static final String DEVICE_TOKEN_KEY = "device_token";
    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String USER_MOBILE_NUMBER_KEY = "mobile_number";
    public static final String USER_FULL_NAME_KEY = "name";
    public static final String OTP_TRIES_LEFT_KEY = "tries_left";
    public static final String USER_NODE_ID_KEY = "nodeID";
    public static final String USER_GENDER_KEY = "gender";
    public static final String USER_DESCRIPTION_KEY = "description";
    public static final String USER_PHOTO_URL_KEY = "profile_photo_uri";
    public static final String LATITUDE_KEY = "latitude";
    public static final String LONGITUDE_KEY = "longitude";
    public static final String USER_DATE_OF_BIRTH_KEY = "dob_date";
    public static final String USER_THANA_KEY = "thana";
    public static final String USER_DISTRICT_KEY = "district";
    public static final String USER_PROFESSION_KEY = "profession";
    public static final String USER_ORGANIZATION_KEY = "organization";
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String RECEIVER = ApplicationPackage + ".RECEIVER";
    public static final String RESULT_DATA_KEY = ApplicationPackage + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = ApplicationPackage + ".LOCATION_DATA_EXTRA";
    public static final String LAST_UPDATE_TIME_KEY = "lastUpdate";
    public static final String FCM_TOKEN_KEY = "fcmToken";
    public static final String IS_FCM_TOKEN_UPDATED_KEY = "isFcmTokenUpdated";
    public static final String USER_PUBLIC_LIST_KEY = "userPublicListKey";
    public static final Set<String> PUBLIC_ACCESS_LIST = new HashSet<>(Arrays.asList("name", "bengali_name", "profession", "description", "organization", "gender", "dob_date", "dob_display", "photo_uri", "thana", "district"));
    public static final String SHARE_LOCATION_KEY = "shareLocationKey";
    public static final String SHARE_DOB_KEY = "shareDateOfBirthKey";
    public static final String NOTIFICATION_TYPE_NEARBY_BUSINESS = "nearby_business";
    public static final String NOTIFICATION_TYPE_NEARBY_PEOPLE = "nearby_people";
    public static final String NOTIFICATION_TYPE_INTERNAL = "internal";
    public static final String NOTIFICATION_TYPE_EXTERNAL = "external";
    public static final String DATABASE_NAME = "spider-db";

    public static ArrayList<PriyoNews> priyoNewsList = new ArrayList<>();
    public static ArrayList<PriyoNews> favNewsList = new ArrayList<>();
    public static ArrayList<PriyoNews> priyoCatNewsList = new ArrayList<>();
    public static ArrayList<PriyoNews> dashboardNewsList = new ArrayList<>();
    public static ArrayList<Cat> catList = new ArrayList<>();
    public static DisplayImageOptions options;

    static {
        PRIYO_BASE_URL = "https://api.priyo.com/api/articles/?";
    }


}

