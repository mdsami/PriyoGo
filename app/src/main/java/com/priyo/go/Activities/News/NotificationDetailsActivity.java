package com.priyo.go.Activities.News;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.DatabaseHelper.DataHelper;
import com.priyo.go.Fragments.DetailsNews.HistoryFragment;
import com.priyo.go.Fragments.DetailsNews.MoreBusFragment;
import com.priyo.go.Fragments.DetailsNews.MoreLocFragment;
import com.priyo.go.Fragments.DetailsNews.MoreNewsFragment;
import com.priyo.go.Fragments.DetailsNews.MorePeopleFragment;
import com.priyo.go.Model.PriyoNews.NewsAuthorResponse;
import com.priyo.go.Model.PriyoNews.NewsBusinessResponse;
import com.priyo.go.Model.PriyoNews.NewsCategoryResponse;
import com.priyo.go.Model.PriyoNews.NewsImageResponse;
import com.priyo.go.Model.PriyoNews.NewsLocationResponse;
import com.priyo.go.Model.PriyoNews.NewsPersonResponse;
import com.priyo.go.Model.PriyoNews.NewsTagsResponse;
import com.priyo.go.Model.PriyoNews.NewsTopicsResponse;
import com.priyo.go.Model.PriyoNews.PriyoNews;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.JSONPerser;
import com.priyo.go.Utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotificationDetailsActivity extends AppCompatActivity implements HttpResponseListener {

    private String slug="";
    private HttpRequestGetAsyncTask mNotificationDetailsTask = null;
    private ProgressDialog mProgressDialog;
    private Menu mOptionsMenu;
    private static ArrayList<PriyoNews> contentsList;

    private NewsAuthorResponse mAuthoreResponse;
    private NewsImageResponse mNewsImageResponse;
    private List<NewsCategoryResponse> mNewsCategory ;

    private List<NewsTagsResponse> mNewsTags ;
    private List<NewsPersonResponse> mNewsPerson ;
    private List<NewsBusinessResponse> mNewsBusiness;
    private List<NewsLocationResponse> mNewsLocation;
    private List<NewsTopicsResponse> mNewsTopics;

    Gson gson;
    private FlexboxLayout cat_flex;
    private FlexboxLayout tag_flex;
    private FlexboxLayout person_flex;
    private FlexboxLayout bus_flex;
    private ImageView news_image;
    private LinearLayout cat_layout;
    private LinearLayout tag_layout;
    private LinearLayout person_layout;
    private LinearLayout business_layout;
    private TextView news_title;
    private TextView txtDate;
    private TextView news_author;
    private TextView answer_banner;
    private WebView news_detail1;
    private WebView news_answer;


    private int position;
    private String date_="";
    private String title_="";
    private String authors_="";
    private String description_="";
    private String answer_="";
    private String imgUrl_="";
    private String postUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gson = new Gson();


        initViews();

        Intent i = getIntent();
        slug = i.getStringExtra("Slug");

        fetchDetails();
    }

    private void initViews() {
        txtDate=(TextView)findViewById(R.id.news_date);
        news_title=(TextView)findViewById(R.id.news_title);
        news_image=(ImageView)findViewById(R.id.news_image);
        news_author=(TextView)findViewById(R.id.news_author);
        news_detail1=(WebView)findViewById(R.id.news_detail);

        cat_layout =(LinearLayout)findViewById(R.id.cat_ll);
        tag_layout =(LinearLayout)findViewById(R.id.tag_ll);
        person_layout =(LinearLayout)findViewById(R.id.people_ll);
        business_layout =(LinearLayout)findViewById(R.id.bus_ll);

        answer_banner=(TextView)findViewById(R.id.answer_ll);
        news_answer=(WebView)findViewById(R.id.answer_detail);

        cat_flex = (FlexboxLayout) findViewById(R.id.cat_layout);
        tag_flex = (FlexboxLayout) findViewById(R.id.tag_layout);
        person_flex = (FlexboxLayout) findViewById(R.id.person_layout);
        bus_flex = (FlexboxLayout) findViewById(R.id.business_layout);
        mProgressDialog = new ProgressDialog(NotificationDetailsActivity.this);
        mProgressDialog.setMessage(getString(R.string.progress_dialog_text_fetching_business));
    }

    private void fetchDetails(){
        mProgressDialog.show();

        mNotificationDetailsTask = new HttpRequestGetAsyncTask("GET_DETAILS",
                Constants.PRIYO_BASE_URL+Constants.URL_FILTER_SLUG+"=" +slug, getApplicationContext());
        mNotificationDetailsTask.mHttpResponseListener =NotificationDetailsActivity.this;
        mNotificationDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void showContents() {



        mAuthoreResponse = gson.fromJson(contentsList.get(position).getAuthor(), NewsAuthorResponse.class);
        mNewsImageResponse = gson.fromJson(contentsList.get(position).getImage(), NewsImageResponse.class);
        NewsCategoryResponse[] cat = gson.fromJson(contentsList.get(position).getCategories(), NewsCategoryResponse[].class);
        mNewsCategory = new ArrayList<>(Arrays.asList(cat));
        NewsTagsResponse[] tags = gson.fromJson(contentsList.get(position).getTags(), NewsTagsResponse[].class);
        mNewsTags = new ArrayList<>(Arrays.asList(tags));
        NewsPersonResponse[] persons = gson.fromJson(contentsList.get(position).getPeople(), NewsPersonResponse[].class);
        mNewsPerson = new ArrayList<>(Arrays.asList(persons));
        NewsBusinessResponse[] businesses = gson.fromJson(contentsList.get(position).getBusinesses(), NewsBusinessResponse[].class);
        mNewsBusiness = new ArrayList<>(Arrays.asList(businesses));
        NewsLocationResponse[] locations = gson.fromJson(contentsList.get(position).getLocations(), NewsLocationResponse[].class);
        mNewsLocation = new ArrayList<>(Arrays.asList(locations));
        NewsTopicsResponse[] topics = gson.fromJson(contentsList.get(position).getTopics(), NewsTopicsResponse[].class);
        mNewsTopics = new ArrayList<>(Arrays.asList(topics));

        title_ = contentsList.get(position).getTitle();
        date_ = contentsList.get(position).getUpdatedAt();
        authors_ = mAuthoreResponse.getFirstName();
        description_ = Utilities.removeUnnecessaryHtmlTag(contentsList.get(position).getDescription());
        answer_ =contentsList.get(position).getAnswer();
        imgUrl_ = mNewsImageResponse.getHostName()+""+mNewsImageResponse.getOriginal();

        news_title.setText(Html.fromHtml(title_));
        txtDate.setText(Html.fromHtml(Utilities.convertIntoBanglaDate(Utilities.changeDateFormatGMTtoBST(date_))));
        news_author.setText(authors_);
        news_detail1.loadData(Utilities.getWebCss(NotificationDetailsActivity.this)+" "+description_,"text/html; charset=utf-8", "UTF-8");

        if(!answer_.equals("") && !answer_.equals("[]") &&answer_!=null) {
            answer_banner.setVisibility(View.VISIBLE);
            news_answer.setVisibility(View.VISIBLE);
            String ans="";

            try{
                JSONArray aa = new JSONArray(answer_);
                JSONObject oo = aa.getJSONObject(0);// new JSONObject(answer_);
                ans = oo.getString("description");
            }catch (Exception e){e.printStackTrace();}

            ans = Utilities.removeUnnecessaryHtmlTag(ans);
            news_answer.loadData(Constants.IMAGE_DIV  + " " + ans, "text/html; charset=utf-8", "UTF-8");
        }
        else{
            answer_banner.setVisibility(View.GONE);
            news_answer.setVisibility(View.GONE);
        }

        if(!imgUrl_.trim().equals("")&&!imgUrl_.trim().equals("[]")) {
            Glide.with(NotificationDetailsActivity.this).load(imgUrl_).into(news_image);

        }


        if(mNewsCategory.size()>0)
        {
            final List<NewsCategoryResponse> categories = mNewsCategory;
            JsonArray slugArrays = new JsonArray();
            JsonArray titleArrays = new JsonArray();
            for (int j = 0; j < categories.size(); j++) {

                final NewsCategoryResponse cc = categories.get(j);
                RelativeLayout tr_head = (RelativeLayout) NotificationDetailsActivity.this.getLayoutInflater().inflate(R.layout.tag_layout, null);

                TextView label_date = (TextView) tr_head.findViewById(R.id.tag_name);
                label_date.setText(categories.get(j).getTitle());
                cat_flex.addView(tr_head);

                tr_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NotificationDetailsActivity.this, PriyoCategoryNewsActivity.class);
                        intent.putExtra("Slug", cc.getSlug());
                        intent.putExtra("Type", "Category");
                        intent.putExtra("Title", cc.getTitle());
                        startActivity(intent);
                    }
                });
                slugArrays.add(new JsonPrimitive(categories.get(j).getSlug()));
                titleArrays.add(new JsonPrimitive(categories.get(j).getTitle()));
            }

            switchToMoreNewsFragment(slugArrays.toString(),titleArrays.toString());
        }
        else{
            cat_layout.setVisibility(View.GONE);
        }


        if(mNewsTags.size()>0)
        {
            List<NewsTagsResponse> categories = mNewsTags;
            for (int j = 0; j < categories.size(); j++) {

                final NewsTagsResponse cc = categories.get(j);
                RelativeLayout tr_head = (RelativeLayout) NotificationDetailsActivity.this.getLayoutInflater().inflate(R.layout.tag_layout, null);

                TextView label_date = (TextView) tr_head.findViewById(R.id.tag_name);
                label_date.setText(categories.get(j).getTitle());

                tag_flex.addView(tr_head);

                tr_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(NotificationDetailsActivity.this, PriyoCategoryNewsActivity.class);
                        intent.putExtra("Slug", cc.getSlug());
                        intent.putExtra("Type", "Tags");
                        intent.putExtra("Title", cc.getTitle());
                        startActivity(intent);

                    }
                });
            }
        }
        else{
            tag_layout.setVisibility(View.GONE);
        }

        if(mNewsPerson.size()>0)
        {
            JsonArray slugArrays = new JsonArray();
            JsonArray titleArrays = new JsonArray();
            List<NewsPersonResponse> categories = mNewsPerson;
            for (int j = 0; j < categories.size(); j++) {
                final NewsPersonResponse cc = categories.get(j);
                RelativeLayout tr_head = (RelativeLayout) NotificationDetailsActivity.this.getLayoutInflater().inflate(R.layout.tag_layout, null);

                TextView label_date = (TextView) tr_head.findViewById(R.id.tag_name);
                label_date.setText(categories.get(j).getTitle());
                person_flex.addView(tr_head);

                tr_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NotificationDetailsActivity.this, PriyoCategoryNewsActivity.class);
                        intent.putExtra("Slug", cc.getSlug());
                        intent.putExtra("Type", "People");
                        intent.putExtra("Title", cc.getTitle());
                        startActivity(intent);
                    }
                });

                slugArrays.add(new JsonPrimitive(categories.get(j).getSlug()));
                titleArrays.add(new JsonPrimitive(categories.get(j).getTitle()));
            }

            switchToMorePeopleFragment(slugArrays.toString(),titleArrays.toString());
        }
        else{
            person_layout.setVisibility(View.GONE);
        }

        if(mNewsBusiness.size()>0)
        {
            List<NewsBusinessResponse> categories = mNewsBusiness;
            JsonArray slugArrays = new JsonArray();
            JsonArray titleArrays = new JsonArray();
            for (int j = 0; j < categories.size(); j++) {
                final NewsBusinessResponse cc = categories.get(j);
                RelativeLayout tr_head = (RelativeLayout) NotificationDetailsActivity.this.getLayoutInflater().inflate(R.layout.tag_layout, null);

                TextView label_date = (TextView) tr_head.findViewById(R.id.tag_name);
                label_date.setText(categories.get(j).getTitle());
                bus_flex.addView(tr_head);
                tr_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NotificationDetailsActivity.this, PriyoCategoryNewsActivity.class);
                        intent.putExtra("Slug", cc.getSlug());
                        intent.putExtra("Type", "Business");
                        intent.putExtra("Title", cc.getTitle());
                        startActivity(intent);
                    }
                });
                slugArrays.add(new JsonPrimitive(categories.get(j).getSlug()));
                titleArrays.add(new JsonPrimitive(categories.get(j).getTitle()));
            }
            switchToMoreBusFragment(slugArrays.toString(),titleArrays.toString());
        }
        else{
            business_layout.setVisibility(View.GONE);
        }

        if(mNewsLocation.size()>0)
        {
            List<NewsLocationResponse> categories = mNewsLocation;

            JsonArray slugArrays = new JsonArray();
            JsonArray titleArrays = new JsonArray();
            for (int j = 0; j < categories.size(); j++) {
                slugArrays.add(new JsonPrimitive(categories.get(j).getSlug()));
                titleArrays.add(new JsonPrimitive(categories.get(j).getTitle()));
            }
            switchToMoreLocFragment(slugArrays.toString(),titleArrays.toString());
        }

        if(mNewsTopics.size()>0)
        {
            List<NewsTopicsResponse> categories = mNewsTopics;
            JsonArray arrays = new JsonArray();
            for (int j = 0; j < categories.size(); j++) {
                arrays.add(new JsonPrimitive(categories.get(j).getSlug()));
            }
            switchToMoreHistoryFragment(arrays.toString());
        }



    }

    @Override
    public void httpResponseReceiver(HttpResponseObject result) {

        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            mProgressDialog.dismiss();
            mNotificationDetailsTask = null;
            if (getApplicationContext() != null)
                Toast.makeText(getApplicationContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }


        Gson gson = new Gson();

        switch (result.getApiCommand()) {


            case "GET_DETAILS":
                try {
                    String message = result.getJsonString();
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {

                        contentsList = JSONPerser.prepareNews(result.getJsonString());
                        showContents();

                    } else {
                        if (getApplicationContext() != null)
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (getApplicationContext() != null)
                        Toast.makeText(getApplicationContext(), R.string.service_not_available, Toast.LENGTH_LONG).show();
                }

                mProgressDialog.dismiss();
                mNotificationDetailsTask = null;

                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.priyo_home, menu);
        mOptionsMenu = menu;


        DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
        int c= dataHelper.getNotOpenedPush();

        if(c>0){
            updateNotificationBadgeCount(c);
        }
        return true;


    }

    private void updateNotificationBadgeCount(int badgeCount) {
        Log.d("Notification Count", badgeCount + "");
        if (mOptionsMenu != null) {
            if (badgeCount > 0) {
                ActionItemBadge.update(this, mOptionsMenu.findItem(R.id.nav_notification), getResources().getDrawable(R.mipmap.notifications), ActionItemBadge.BadgeStyles.DARK_GREY, badgeCount);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_notification) {
            startActivity(new Intent(NotificationDetailsActivity.this,NotificationActivity.class));
            return true;
        }
        if (id == R.id.saved_news) {
            startActivity(new Intent(NotificationDetailsActivity.this,SavedNewsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void switchToMoreNewsFragment(String key,String title) {
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        bundle.putString("title", title);
        MoreNewsFragment fragobj = new MoreNewsFragment();
        fragobj.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.more_frame, fragobj).addToBackStack(null).commit();
    }
    public void switchToMorePeopleFragment(String key,String title) {
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        bundle.putString("title", title);
        MorePeopleFragment fragobj = new MorePeopleFragment();
        fragobj.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.more_people_frame, fragobj).addToBackStack(null).commit();
    }

    public void switchToMoreBusFragment(String key,String title) {
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        bundle.putString("title", title);
        MoreBusFragment fragobj = new MoreBusFragment();
        fragobj.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.more_bus_frame, fragobj).addToBackStack(null).commit();
    }

    public void switchToMoreLocFragment(String key,String title) {
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        bundle.putString("title", title);
        MoreLocFragment fragobj = new MoreLocFragment();
        fragobj.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.more_loc_frame, fragobj).addToBackStack(null).commit();
    }

    public void switchToMoreHistoryFragment(String key) {
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        HistoryFragment fragobj = new HistoryFragment();
        fragobj.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.history_frame, fragobj).addToBackStack(null).commit();
    }
}
