/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.priyo.go.PriyoContentProvider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

public class  NewsContract {
    public static final String CONTENT_AUTHORITY = "com.priyo.go";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NEWS = "priyo_home";
    public static final String PATH_CAT = "priyo_category";
    public static final String PATH_FAV = "priyo_fav";


    public static final String DATE_FORMAT = "yyyyMMdd";

    public static final class NewsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String TABLE_NAME = "priyo_home";

        public static final String COLUMN_NEWS_ID = "news_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_SUMMERY = "summary";
        public static final String COLUMN_SLUG = "slug";
        public static final String COLUMN_PRIORITY = "article_priority";
        public static final String COLUMN_UPDATE_AT = "updated_at";
        public static final String COLUMN_CREATE_AT = "created_at";
        public static final String COLUMN_PUBLISHED_AT = "published_at";
        public static final String COLUMN_IMG = "image";

        public static final String COLUMN_CATEGORY = "categories";
        public static final String COLUMN_TAGS = "tags";
        public static final String COLUMN_BUSINESS = "businesses";
        public static final String COLUMN_LOCATIONS = "locations";
        public static final String COLUMN_PERSONS = "persons";
        public static final String COLUMN_TOPICS = "topics";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_INSERT_DATE_TIME = "insert_date_time";


        public static Uri buildNewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildNewsId(String newsId) {
            return CONTENT_URI.buildUpon().appendPath(newsId).build();
        }
        public static Uri buildNews() {
            return CONTENT_URI;
        }

//        public static Uri buildNewsCatId(String catId) {
//            return CONTENT_URI.buildUpon().appendPath(catId).build();
//        }

//        public static Uri buildNewsIdAndCat(String newsId, String cat) {
//            return CONTENT_URI.buildUpon().appendPath(newsId).appendPath(cat).build();
//        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

//        public static String getCatFromUri(Uri uri) {
//            System.out.println("FCCCCCCCSS  "+uri.getPathSegments().get(2));
//            return uri.getPathSegments().get(2);
//        }

    }


    public static final class FavEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_FAV;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_FAV;
        public static final String TABLE_NAME = "priyo_fav";

        public static final String COLUMN_NEWS_ID = "news_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_SUMMERY = "summary";
        public static final String COLUMN_SLUG = "slug";
        public static final String COLUMN_PRIORITY = "article_priority";
        public static final String COLUMN_UPDATE_AT = "updated_at";
        public static final String COLUMN_CREATE_AT = "created_at";
        public static final String COLUMN_PUBLISHED_AT = "published_at";
        public static final String COLUMN_IMG = "image";

        public static final String COLUMN_CATEGORY = "categories";
        public static final String COLUMN_TAGS = "tags";
        public static final String COLUMN_BUSINESS = "businesses";
        public static final String COLUMN_LOCATIONS = "locations";
        public static final String COLUMN_PERSONS = "persons";
        public static final String COLUMN_TOPICS = "topics";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_INSERT_DATE_TIME = "insert_date_time";



        public static Uri buildNewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildNewsId(String newsId) {
            return CONTENT_URI.buildUpon().appendPath(newsId).build();
        }

//        public static Uri buildNewsCatId(String catId) {
//            return CONTENT_URI.buildUpon().appendPath(catId).build();
//        }
//
//        public static Uri buildNewsIdAndCat(String newsId, String cat) {
//            return CONTENT_URI.buildUpon().appendPath(newsId).appendPath(cat).build();
//        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

//        public static String getCatFromUri(Uri uri) {
//            System.out.println("FCCCCCCCSS  "+uri.getPathSegments().get(2));
//            return uri.getPathSegments().get(2);
//        }

    }


    public static final class CategoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CAT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CAT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CAT;

        // Table name
        public static final String TABLE_NAME = "priyo_category";

        public static final String COLUMN_CAT_ID = "catid";
        public static final String COLUMN_CAT_TITLE = "cat_name";
        public static final String COLUMN_CAT_DESC = "cat_desc";
        public static final String COLUMN_CAT_WEIGHT = "cat_weight";
        public static final String COLUMN_CAT_SLUG = "cat_slug";
        public static final String COLUMN_CAT_TITLEINENGLISH = "cat_titleinenglish";
        public static final String COLUMN_CAT_PARENT_ID = "cat_parentid";
        public static final String COLUMN_CAT_CHILD = "cat_child";



        public static Uri buildCatUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildCatId(String newsId) {
            return CONTENT_URI.buildUpon().appendPath(newsId).build();
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static String getDbDateString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }
}
