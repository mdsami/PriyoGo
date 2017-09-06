package com.priyo.go.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.priyo.go.Model.HoroscopeNode;
import com.priyo.go.Model.PriyoNews.Categories.Cat;
import com.priyo.go.Model.PriyoNews.Categories.Child;
import com.priyo.go.Model.PriyoNews.NewsAuthorResponse;
import com.priyo.go.Model.PriyoNews.NewsBusinessResponse;
import com.priyo.go.Model.PriyoNews.NewsCategoryResponse;
import com.priyo.go.Model.PriyoNews.NewsImageResponse;
import com.priyo.go.Model.PriyoNews.NewsPersonResponse;
import com.priyo.go.Model.PriyoNews.NewsTagsResponse;
import com.priyo.go.Model.PriyoNews.PriyoNews;
import com.priyo.go.PriyoContentProvider.NewsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


public class JSONPerser {

    public static ArrayList<PriyoNews> prepareNews(String jsonData){

        ArrayList<PriyoNews> priyo_news = new ArrayList<>();

        String id="", title="", description="", answer="",summary="",slug="", updatedAt="", createddAt="",priority="",publishedAt="";

        String img = "";
        String cats = "";
        String tags = "";
        String buss = "";
        String locs = "";
        String pers = "";
        String tops = "";
        String auth = "";

        try {
            JSONArray arr = new JSONArray(jsonData);
            for(int i=0;i<arr.length();i++){

                JSONObject obj = arr.getJSONObject(i);

                try { id = obj.getString("id"); }catch (Exception e){}
                try { title = obj.getString("title"); }catch (Exception e){}
                try { description = obj.getString("description"); }catch (Exception e){}
                try { answer = obj.getString("answers"); }catch (Exception e){}
                try { summary = obj.getString("summary"); }catch (Exception e){}
                try { slug = obj.getString("slug"); }catch (Exception e){}
                try { updatedAt = obj.getString("updatedAt"); }catch (Exception e){}
                try { createddAt = obj.getString("createdAt"); }catch (Exception e){}
                try { priority = obj.getString("articlePriority"); }catch (Exception e){}
                try { publishedAt = obj.getString("publishedAt"); }catch (Exception e){}
                try { img = obj.getString("image"); }catch (Exception e){}
                try { cats = obj.getString("categories"); }catch (Exception e){}
                try { tags = obj.getString("tags"); }catch (Exception e){}
                try { buss = obj.getString("businesses"); }catch (Exception e){}
                try { locs = obj.getString("locations"); }catch (Exception e){}
                try { pers = obj.getString("people"); }catch (Exception e){}
                try { tops = obj.getString("topics"); }catch (Exception e){}
                try { auth = obj.getString("author"); }catch (Exception e){}


                System.out.println("WWWWWWWW  "+title +" "+createddAt +" "+updatedAt+" "+publishedAt);

                PriyoNews priyoNews = new PriyoNews();
                priyoNews.setId(id);
                priyoNews.setTitle(title);
                priyoNews.setDescription(description);
                priyoNews.setAnswer(answer);
                priyoNews.setSummary(summary);
                priyoNews.setSlug(slug);
                priyoNews.setUpdatedAt(updatedAt);
                priyoNews.setCreatedAt(createddAt);
                priyoNews.setPriority(priority);
                priyoNews.setPublishedAt(publishedAt);
                priyoNews.setImage(img);
                priyoNews.setCategories(cats);
                priyoNews.setTags(tags);
                priyoNews.setBusinesses(buss);
                priyoNews.setLocations(locs);
                priyoNews.setPeople(pers);
                priyoNews.setTopics(tops);
                priyoNews.setAuthor(auth);
                priyo_news.add(priyoNews);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return priyo_news;
    }

    public static ArrayList<PriyoNews> prepareTopNews(String jsonData){

        ArrayList<PriyoNews> priyo_news = new ArrayList<>();

        String id="", title="", description="", answer="",summary="",slug="", updatedAt="", createddAt="",priority="",publishedAt="";

        String img = "";
        String cats = "";
        String tags = "";
        String buss = "";
        String locs = "";
        String pers = "";
        String tops = "";
        String auth = "";

        try {
            JSONObject jjj = new JSONObject(jsonData);
            JSONArray arr = jjj.getJSONArray("top_story");//new JSONArray(jsonData);
            for(int i=0;i<arr.length();i++){

                JSONObject obj = arr.getJSONObject(i);

                try { id = obj.getString("id"); }catch (Exception e){}
                try { title = obj.getString("title"); }catch (Exception e){}
                try { description = obj.getString("description"); }catch (Exception e){}
                try { answer = obj.getString("answers"); }catch (Exception e){}
                try { summary = obj.getString("summary"); }catch (Exception e){}
                try { slug = obj.getString("slug"); }catch (Exception e){}
                try { updatedAt = obj.getString("updatedAt"); }catch (Exception e){}
                try { createddAt = obj.getString("createdAt"); }catch (Exception e){}
                try { priority = obj.getString("articlePriority"); }catch (Exception e){}
                try { publishedAt = obj.getString("publishedAt"); }catch (Exception e){}
                try { img = obj.getString("image"); }catch (Exception e){}
                try { cats = obj.getString("categories"); }catch (Exception e){}
                try { tags = obj.getString("tags"); }catch (Exception e){}
                try { buss = obj.getString("businesses"); }catch (Exception e){}
                try { locs = obj.getString("locations"); }catch (Exception e){}
                try { pers = obj.getString("people"); }catch (Exception e){}
                try { tops = obj.getString("topics"); }catch (Exception e){}
                try { auth = obj.getString("author"); }catch (Exception e){}


                System.out.println("WWWWWWWW  "+title +" "+createddAt +" "+updatedAt+" "+publishedAt);

                PriyoNews priyoNews = new PriyoNews();
                priyoNews.setId(id);
                priyoNews.setTitle(title);
                priyoNews.setDescription(description);
                priyoNews.setAnswer(answer);
                priyoNews.setSummary(summary);
                priyoNews.setSlug(slug);
                priyoNews.setUpdatedAt(updatedAt);
                priyoNews.setCreatedAt(createddAt);
                priyoNews.setPriority(priority);
                priyoNews.setPublishedAt(publishedAt);
                priyoNews.setImage(img);
                priyoNews.setCategories(cats);
                priyoNews.setTags(tags);
                priyoNews.setBusinesses(buss);
                priyoNews.setLocations(locs);
                priyoNews.setPeople(pers);
                priyoNews.setTopics(tops);
                priyoNews.setAuthor(auth);
                priyo_news.add(priyoNews);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return priyo_news;
    }

    public static ArrayList<PriyoNews> prepareLatestNews(String jsonData){

        ArrayList<PriyoNews> priyo_news = new ArrayList<>();

        String id="", title="", description="", answer="",summary="",slug="", updatedAt="", createddAt="",priority="",publishedAt="";

        String img = "";
        String cats = "";
        String tags = "";
        String buss = "";
        String locs = "";
        String pers = "";
        String tops = "";
        String auth = "";

        try {
            JSONObject jjj = new JSONObject(jsonData);
            JSONArray arr = jjj.getJSONArray("latest");//new JSONArray(jsonData);
            for(int i=0;i<arr.length();i++){

                JSONObject obj = arr.getJSONObject(i);

                try { id = obj.getString("id"); }catch (Exception e){}
                try { title = obj.getString("title"); }catch (Exception e){}
                try { description = obj.getString("description"); }catch (Exception e){}
                try { answer = obj.getString("answers"); }catch (Exception e){}
                try { summary = obj.getString("summary"); }catch (Exception e){}
                try { slug = obj.getString("slug"); }catch (Exception e){}
                try { updatedAt = obj.getString("updatedAt"); }catch (Exception e){}
                try { createddAt = obj.getString("createdAt"); }catch (Exception e){}
                try { priority = obj.getString("articlePriority"); }catch (Exception e){}
                try { publishedAt = obj.getString("publishedAt"); }catch (Exception e){}
                try { img = obj.getString("image"); }catch (Exception e){}
                try { cats = obj.getString("categories"); }catch (Exception e){}
                try { tags = obj.getString("tags"); }catch (Exception e){}
                try { buss = obj.getString("businesses"); }catch (Exception e){}
                try { locs = obj.getString("locations"); }catch (Exception e){}
                try { pers = obj.getString("people"); }catch (Exception e){}
                try { tops = obj.getString("topics"); }catch (Exception e){}
                try { auth = obj.getString("author"); }catch (Exception e){}


                System.out.println("WWWWWWWW  "+title +" "+createddAt +" "+updatedAt+" "+publishedAt);

                PriyoNews priyoNews = new PriyoNews();
                priyoNews.setId(id);
                priyoNews.setTitle(title);
                priyoNews.setDescription(description);
                priyoNews.setAnswer(answer);
                priyoNews.setSummary(summary);
                priyoNews.setSlug(slug);
                priyoNews.setUpdatedAt(updatedAt);
                priyoNews.setCreatedAt(createddAt);
                priyoNews.setPriority(priority);
                priyoNews.setPublishedAt(publishedAt);
                priyoNews.setImage(img);
                priyoNews.setCategories(cats);
                priyoNews.setTags(tags);
                priyoNews.setBusinesses(buss);
                priyoNews.setLocations(locs);
                priyoNews.setPeople(pers);
                priyoNews.setTopics(tops);
                priyoNews.setAuthor(auth);
                priyo_news.add(priyoNews);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return priyo_news;
    }

    public static ArrayList<NewsCategoryResponse> parseNewsCategory(String jsonData){

        String id="", title="",  titleInEnglish="", parentId="",slug="";

        ArrayList<NewsCategoryResponse> cat= new ArrayList<>();

        try{
            JSONArray catArray = new JSONArray(jsonData);
            for(int j=0;j<catArray.length();j++){

                JSONObject objs = catArray.getJSONObject(j);

                try { id = objs.getString("id"); }catch (Exception e){}
                try { title = objs.getString("title"); }catch (Exception e){}
                try { titleInEnglish = objs.getString("titleInEnglish"); }catch (Exception e){}
                try { parentId = objs.getString("parentId"); }catch (Exception e){}
                try { slug = objs.getString("slug"); }catch (Exception e){}

                NewsCategoryResponse category = new NewsCategoryResponse();
                category.setId(id);
                category.setTitle(title);
                category.setTitleInEnglish(titleInEnglish);
                category.setParentId(parentId);
                category.setSlug(slug);



                cat.add(category);
            }
        }catch (Exception e){e.printStackTrace();}

        return cat;
    }

    public static ArrayList<Child> prepareChild(String jsonData){

        String id="", title="", description="", titleInEnglish="", parentId="",weight="",slug="";

        ArrayList<Child> children = new ArrayList<Child>();
        try{
            JSONArray catArray = new JSONArray(jsonData);
            for(int j=0;j<catArray.length();j++){

                JSONObject obj = catArray.getJSONObject(j);

                try { id = obj.getString("id"); }catch (Exception e){}
                try { title = obj.getString("title"); }catch (Exception e){}
                try { description = obj.getString("description"); }catch (Exception e){}
                try { weight = obj.getString("weight"); }catch (Exception e){}
                try { slug = obj.getString("slug"); }catch (Exception e){}
                try { titleInEnglish = obj.getString("titleInEnglish"); }catch (Exception e){}
                try { parentId = obj.getString("parentId"); }catch (Exception e){}


                Child priyoNews1 = new Child();
                priyoNews1.setId(id);
                priyoNews1.setTitle(title);
                priyoNews1.setDescription(description);
                priyoNews1.setWeight(weight);
                priyoNews1.setSlug(slug);
                priyoNews1.setTitleInEnglish(titleInEnglish);
                priyoNews1.setParentId(parentId);




                children.add(priyoNews1);
            }
        }catch (Exception e){}



        return children;
    }

    public static ArrayList<PriyoNews> prepareNewsFromCursor(Cursor cursor){

        ArrayList<PriyoNews> priyo_news = new ArrayList<>();

        String id="", title="", description="", answer="",titleInEnglish="",summary="",slug="", updatedAt="", createdAt="",priority="",publishedAt="",img_obj="",
                cat_array="", tag_array="", bus_array="", loc_array="",person_array="", top_array="",auth_obj="";

        NewsImageResponse image= new NewsImageResponse();
        ArrayList<NewsCategoryResponse> cat= new ArrayList<>();
        ArrayList<NewsTagsResponse> tag= new ArrayList<>();
        ArrayList<NewsBusinessResponse> businesses= new ArrayList<>();
        ArrayList<NewsPersonResponse> persons= new ArrayList<>();
        NewsAuthorResponse author= new NewsAuthorResponse();

        try {
            for(int i =0;i<cursor.getCount();i++)
            {
                cursor.moveToPosition(i);

                id = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_ID));
                title = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE));
                description = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_DESCRIPTION));
                answer = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_ANSWER));
                summary = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_SUMMERY));
                slug = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_SLUG));
                updatedAt = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_UPDATE_AT));
                publishedAt = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_CREATE_AT));
                priority = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_PRIORITY));
                createdAt = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_PUBLISHED_AT));

                img_obj = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_IMG));
                cat_array = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_CATEGORY));
                tag_array = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TAGS));
                bus_array = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_BUSINESS));
                loc_array = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_LOCATIONS));
                person_array = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_PERSONS));
                top_array = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TOPICS));
                auth_obj = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_AUTHOR));


                PriyoNews priyoNews = new PriyoNews();
                priyoNews.setId(id);
                priyoNews.setTitle(title);
                priyoNews.setDescription(description);
                priyoNews.setAnswer(answer);
                priyoNews.setSummary(summary);
                priyoNews.setSlug(slug);
                priyoNews.setUpdatedAt(updatedAt);
                priyoNews.setCreatedAt(createdAt);
                priyoNews.setPriority(priority);
                priyoNews.setPublishedAt(publishedAt);
                priyoNews.setImage(img_obj);
                priyoNews.setCategories(cat_array);
                priyoNews.setTags(tag_array);
                priyoNews.setBusinesses(bus_array);
                priyoNews.setLocations(loc_array);
                priyoNews.setPeople(person_array);
                priyoNews.setTopics(top_array);
                priyoNews.setAuthor(auth_obj);
                priyo_news.add(priyoNews);




                System.out.println("MKLMKL  23   "+id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return priyo_news;
    }

    public static ArrayList<Cat> prepareCatFromCursor(Cursor cursor){

        ArrayList<Cat> priyo_news = new ArrayList<>();

        String id="", title="", description="", titleInEnglish="", parentId="",weight="",slug="";

        ArrayList<Child> children = new ArrayList<Child>();


        JSONObject objs;

        try {
            for(int i =0;i<cursor.getCount();i++)
            {
                cursor.moveToPosition(i);

                id = cursor.getString(cursor.getColumnIndex(NewsContract.CategoryEntry.COLUMN_CAT_ID));
                title = cursor.getString(cursor.getColumnIndex(NewsContract.CategoryEntry.COLUMN_CAT_TITLE));
                description = cursor.getString(cursor.getColumnIndex(NewsContract.CategoryEntry.COLUMN_CAT_DESC));
                titleInEnglish = cursor.getString(cursor.getColumnIndex(NewsContract.CategoryEntry.COLUMN_CAT_TITLEINENGLISH));
                parentId = cursor.getString(cursor.getColumnIndex(NewsContract.CategoryEntry.COLUMN_CAT_PARENT_ID));
                weight = cursor.getString(cursor.getColumnIndex(NewsContract.CategoryEntry.COLUMN_CAT_WEIGHT));
                slug = cursor.getString(cursor.getColumnIndex(NewsContract.CategoryEntry.COLUMN_CAT_SLUG));
                String child_array = cursor.getString(cursor.getColumnIndex(NewsContract.CategoryEntry.COLUMN_CAT_CHILD));


                Cat priyoNews = new Cat();
                priyoNews.setId(id);
                priyoNews.setTitle(title);
                priyoNews.setDescription(description);
                priyoNews.setWeight(0);
                priyoNews.setSlug(slug);
                priyoNews.setTitleInEnglish(titleInEnglish);
                priyoNews.setParentId(parentId);

                if(child_array!=null && !child_array.equals("null") && !child_array.equals(""))
                {
                    children = prepareChild(child_array);
                }


                priyoNews.setChildren(children);
                priyo_news.add(priyoNews);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return priyo_news;
    }

    public static void updateNewsDatabase(Context c, ArrayList<PriyoNews> priyo_news){


        try {
            Vector<ContentValues> cVVector = new Vector<ContentValues>(priyo_news.size());
            for(int i=0;i<priyo_news.size();i++){
                ContentValues newsValues = new ContentValues();
                Date date = new Date();

                PriyoNews pn = priyo_news.get(i);

                System.out.println("MKLMKL 1  "+pn.getId()+" "+pn.getCreatedAt() +" "+pn.getUpdatedAt()+" "+pn.getPublishedAt());
                newsValues.put(NewsContract.NewsEntry.COLUMN_NEWS_ID, pn.getId());
                newsValues.put(NewsContract.NewsEntry.COLUMN_TITLE, pn.getTitle());
                newsValues.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, pn.getDescription());
                newsValues.put(NewsContract.NewsEntry.COLUMN_ANSWER, pn.getAnswer());
                newsValues.put(NewsContract.NewsEntry.COLUMN_SUMMERY, pn.getSummary());
                newsValues.put(NewsContract.NewsEntry.COLUMN_SLUG, pn.getSlug());
                newsValues.put(NewsContract.NewsEntry.COLUMN_UPDATE_AT, pn.getUpdatedAt());
                newsValues.put(NewsContract.NewsEntry.COLUMN_CREATE_AT, pn.getCreatedAt());
                newsValues.put(NewsContract.NewsEntry.COLUMN_PRIORITY, pn.getPriority());
                newsValues.put(NewsContract.NewsEntry.COLUMN_PUBLISHED_AT, pn.getPublishedAt());
                newsValues.put(NewsContract.NewsEntry.COLUMN_CATEGORY, pn.getCategories());
                newsValues.put(NewsContract.NewsEntry.COLUMN_AUTHOR, pn.getAuthor());
                newsValues.put(NewsContract.NewsEntry.COLUMN_TAGS, pn.getTags());
                newsValues.put(NewsContract.NewsEntry.COLUMN_BUSINESS, pn.getBusinesses());
                newsValues.put(NewsContract.NewsEntry.COLUMN_LOCATIONS, pn.getLocations());
                newsValues.put(NewsContract.NewsEntry.COLUMN_PERSONS, pn.getPeople());
                newsValues.put(NewsContract.NewsEntry.COLUMN_TOPICS, pn.getTopics());
                newsValues.put(NewsContract.NewsEntry.COLUMN_IMG, pn.getImage());
                newsValues.put(NewsContract.NewsEntry.COLUMN_INSERT_DATE_TIME, String.valueOf(date.getTime()));

                cVVector.add(newsValues);

            }
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);


                Cursor cursor = c.getContentResolver().query(
                        NewsContract.NewsEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );

                if(cursor.getCount()>200)
                {
                    String query = "( SELECT "+ NewsContract.NewsEntry.COLUMN_INSERT_DATE_TIME+" FROM "+
                            NewsContract.NewsEntry.TABLE_NAME+" ORDER BY "+NewsContract.NewsEntry.COLUMN_PRIORITY+" DESC, "+ NewsContract.NewsEntry.COLUMN_INSERT_DATE_TIME+" DESC LIMIT -1 OFFSET 200)";

                    c.getContentResolver().delete(NewsContract.NewsEntry.CONTENT_URI,
                            NewsContract.NewsEntry.COLUMN_INSERT_DATE_TIME + " IN "+query,
                            null);
                }

                if(cursor.getCount()>1)
                {

                    for(int i=0;i<cVVector.size();i++)
                    {
                        String nId = cvArray[i].getAsString(NewsContract.NewsEntry.COLUMN_NEWS_ID);
                        c.getContentResolver().delete(NewsContract.NewsEntry.CONTENT_URI,
                                NewsContract.NewsEntry.COLUMN_NEWS_ID + " = ?",
                                new String[] {nId});
                    }
                }

                c.getContentResolver().bulkInsert(NewsContract.NewsEntry.CONTENT_URI,
                            cvArray);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<PriyoNews> prepareRelatedNews(String jsonData){

        ArrayList<PriyoNews> priyo_news = new ArrayList<>();

        String id="", title="", slug="", publishedAt="";

        String img = "";

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray arr = jsonObject.getJSONArray("related");
            for(int i=0;i<arr.length();i++){

                JSONObject obj = arr.getJSONObject(i);

                try { id = obj.getString("id"); }catch (Exception e){}
                try { title = obj.getString("title"); }catch (Exception e){}
                try { slug = obj.getString("slug"); }catch (Exception e){}
                try { publishedAt = obj.getString("publishedAt"); }catch (Exception e){}
                try { img = obj.getString("image"); }catch (Exception e){}

                PriyoNews priyoNews = new PriyoNews();
                priyoNews.setId(id);
                priyoNews.setTitle(title);
                priyoNews.setSlug(slug);
                priyoNews.setPublishedAt(publishedAt);
                priyoNews.setImage(img);
                priyo_news.add(priyoNews);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return priyo_news;
    }

    public static List<HoroscopeNode> pasrseHorscope(String jsonValue){
        List<HoroscopeNode> temp = new ArrayList<>();
        try {
            JSONObject questionMark = new JSONObject(jsonValue);
            Iterator keys = questionMark.keys();

            while(keys.hasNext()) {
                // loop to get the dynamic key
                String currentDynamicKey = (String)keys.next();
                if(!currentDynamicKey.equals("date")) {
                    String currentDynamicValue = questionMark.getString(currentDynamicKey);
                    temp.add(new HoroscopeNode(currentDynamicKey, currentDynamicValue));
                }
            }
        }catch (Exception e){e.printStackTrace();}
        return temp;

    }
}
