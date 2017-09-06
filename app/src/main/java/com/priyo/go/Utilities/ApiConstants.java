package com.priyo.go.Utilities;

/**
 * Created by sajid.shahriar on 4/25/17.
 */

public final class ApiConstants {

    public final class Url {
        public static final String IMAGE_BASE_URI = "https://spider.priyo.com";
        private static final String SPIDER_API_LIVE = "https://tarantula.priyo.com/";
        public static final String SPIDER_API = SPIDER_API_LIVE;
        private static final String SPIDER_API_TEST = "http://10.10.10.200/";
        private static final String PRIYO_API_LIVE = "https://api.priyo.com/api/articles/";
        public static final String PRIYO_API = PRIYO_API_LIVE;


    }

    public final class EndPoint {
        public static final String SIGN_UP_INIT_ENDPOINT = "auth/signup/init/";
        public static final String SIGN_UP_ENDPOINT = "auth/signup/";
        public static final String LOG_IN_ENDPOINT = "auth/login/";
        public static final String PROFILE_DETAILS_ENDPOINT = "profile/basic-profile/%s/";
        public static final String PROFILE_PICTURE_UPLOAD_ENDPOINT = "profile/upload/profile-photo/%s/";

        public static final String PEOPLE_ENDPOINT = "people/";
        public static final String THANA_LIST_FETCH_ENDPOINT = "thana/list/";
        public static final String DISTRICT_LIST_FETCH_ENDPOINT = "district/list/";
        public static final String PHONE_BOOK_LIST_FETCH_ENDPOINT = "phonebook/list/";
        public static final String PHONE_BOOK_LIST_SEARCH_ENDPOINT = "phonebook/search/";
        public static final String PROFESSION_LIST_FETCH_ENDPOINT = "profession/list/";
        public static final String BUSINESS_LIST_ENDPOINT = "business/list/";
        public static final String BUSINESS_CATEGORY_LIST_ENDPOINT = "business/category/list/";
        public static final String BUSINESS_SEARCH_ENDPOINT = "business/search/";
        public static final String HOROSCOPE_FIND_ENDPOINT = "horoscope/find/";

        public static final String BIRTHDAYS_ENDPOINT = "contacts/birthdays/";
        public static final String USER_WISH_ADD_ENDPOINT = "wish/add/";
        public static final String WISH_FIND_ENDPOINT = "wish/find/";
        public static final String NOTIFICATION_REGISTER_ENDPOINT = "notification/register/";
        public static final String URL_STRANGERS_CONTACT_ENDPOINT = "contacts/strangers/";
        public static final String FCM_TOKEN_UPDATE_ENDPOINT = "mq/register-fcm/%s/";
        public static final String USER_LOCATION_SETTINGS_UPDATE_ENDPOINT = "user/%s/";
        public static final String USER_LOCATION_UPDATE_ENDPOINT = "user/";
        public static final String FEEDBACK_ADD_ENDPOINT = "feedback/add/";
        public static final String ALL_HOROSCOPE_ENDPOINT = "horoscope/allHoroscope/";
    }

    public final class Parameter {
        public static final String LABEL_PARAMETER = "label";
        public static final String PAGE_SIZE_PARAMETER = "pageSize";
        public static final String PAGE_NUMBER_PARAMETER = "pageNumber";
        public static final String SORTING_PARAMETER = "sortingParam";
        public static final String SORTING_ORDER_PARAMETER = "sortingOrder";
        public static final String RELATED_TO_ID_PARAMETER = "relatedToID";
        public static final String RELATIONSHIP_PARAMETER = "relationship";
        public static final String TYPE_PARAMETER = "type";
        public static final String COUNT_PARAMETER = "count";
        public static final String QUERY_STRING_PARAMETER = "queryString";
        public static final String MOBILE_NUMBER_PARAMETER = "mobileNumber";
        public static final String DATE_PARAMETER = "date";
        public static final String IS_COMPLETED_PARAMETER = "isCompleted";
        public static final String PHONE_NUMBER_PARAMETER = "phoneNumber";
        public static final String NODE_ID_PARAMETER = "nodeID";
    }

    public final class Module {
        public static final String PROFILE_MODULE = "profile/api/v1/";
        public static final String GRAPH_MODULE = "graph/api/v1/";
        public static final String UTILITY_MODULE = "utility/api/v1/";
        public static final String LOCATION_MODULE = "location/api/v1/";

    }

    public final class Header {
        public static final String DEVICE_TOKEN = "Device-Token";
        public static final String ACCESS_TOKEN = "Access-Token";
        public static final String TOKEN = "Token";
        public static final String META_DATA = "Meta-Data";

    }

    public final class Command {
        //Commands for Profile Module
        public static final String SIGN_UP_INIT = "signUpInit";
        public static final String SIGN_UP = "signUp";
        public static final String LOGIN = "login";
        public static final String PROFILE_FETCH = "profileFetch";
        public static final String PROFILE_UPDATE = "profileUpdate";
        public static final String PROFILE_PHOTO_UPLOAD = "profilePhotoUpload";
        public static final String USER_HOROSCOPE_REQUEST = "userHoroscopeRequest";
        public static final String USER_WISH_ADD_REQUEST = "userAddWishRequest";

        //Commands for Graph Module
        public static final String PHONEBOOK_ADD = "phoneBookAdd";

        public static final String BUSINESS_LIST_FETCH = "businessListFetch";
        public static final String BUSINESS_LIST_UPDATE = "businessListUpdate";
        public static final String BUSINESS_CATEGORY_LIST_FETCH = "businessCategoryListFetch";
        public static final String PHONEBOOK_LIST_FETCH = "phonebookListFetch";
        public static final String DISTRICT_LIST_FETCH = "districtListFetch";
        public static final String THANA_LIST_FETCH = "thanaListFetch";
        public static final String PROFESSION_LIST_FETCH = "professionListFetch";
        public static final String BUSINESS_LIST_SEARCH = "businessListSearch";

        public static final String UPCOMING_BIRTHDAY_LIST_FETCH = "upComingBirthdayList";
        public static final String WISH_LIST_FETCH = "wishListFetch";
        public static final String NOTIFICATION_REGISTER = "notificationRegister";
        public static final String STRANGER_LIST_FETCH = "strangerListFetch";
        public static final String FCM_TOKEN_UPDATE = "fcmTokenUpdate";
        public static final String SHARE_LOCATION_SETTINGS_UPDATE = "shareLocationSettingsUpdate";
        public static final String FEEDBACK_ADD = "feedbackAdd";
    }

    public final class StatusCode {
        public static final int S_OK = 200;
        public static final int S_ACCEPTED = 202;
        public static final int S_NO_CONTENT = 204;

        public static final int S_BAD_REQUEST = 400;
        public static final int S_UNAUTHORIZED = 401;
        public static final int S_FORBIDDEN = 403;
        public static final int S_NOT_FOUND = 404;
        public static final int S_METHOD_NOT_ALLOWED = 405;
        public static final int S_NOT_ACCEPTED = 406;

        public static final int S_SERVER_ERROR = 500;
    }

}
