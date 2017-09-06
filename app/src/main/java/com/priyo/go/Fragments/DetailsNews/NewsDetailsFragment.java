package com.priyo.go.Fragments.DetailsNews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.priyo.go.Activities.News.PriyoCategoryNewsActivity;
import com.priyo.go.Model.PriyoNews.NewsBusinessResponse;
import com.priyo.go.Model.PriyoNews.NewsAuthorResponse;
import com.priyo.go.Model.PriyoNews.NewsCategoryResponse;
import com.priyo.go.Model.PriyoNews.NewsImageResponse;
import com.priyo.go.Model.PriyoNews.NewsLocationResponse;
import com.priyo.go.Model.PriyoNews.NewsTagsResponse;

import com.priyo.go.Model.PriyoNews.NewsPersonResponse;
import com.priyo.go.Model.PriyoNews.PriyoNews;

import com.priyo.go.Model.PriyoNews.NewsTopicsResponse;
import com.priyo.go.PriyoContentProvider.NewsContract;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

public class NewsDetailsFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static ArrayList<PriyoNews> contentsList;
    private static final int NUMBER_OF_COMMENTS = 5;

    private NewsAuthorResponse mAuthoreResponse;
    private NewsImageResponse mNewsImageResponse;
    private List<NewsCategoryResponse> mNewsCategory ;

    private List<NewsTagsResponse> mNewsTags ;
    private List<NewsPersonResponse> mNewsPerson ;
    private List<NewsBusinessResponse> mNewsBusiness;
    private List<NewsLocationResponse> mNewsLocation;
    private List<NewsTopicsResponse> mNewsTopics;

    Gson gson;

    private Context context;
    private FlexboxLayout cat_flex;
    private FlexboxLayout tag_flex;
    private FlexboxLayout person_flex;
    private FlexboxLayout bus_flex;
    private FrameLayout mContainer;
    private ImageView news_image;
    private ImageView shareNews;
    private ImageView saveNews;
    private LinearLayout cat_layout;
    private LinearLayout tag_layout;
    private LinearLayout person_layout;
    private LinearLayout business_layout;
    private ProgressBar progressBar;
    private TextView news_title;
    private TextView txtDate;
    private TextView news_author;
    private TextView answer_banner;
    private View rootView;
    private View view;
    private WebView mWebViewComments;
    private WebView mWebviewPop;
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

    //String txt = "";

    public static NewsDetailsFragment newInstance(int position, ArrayList<PriyoNews> cont, String ctitle) {
        NewsDetailsFragment f = new NewsDetailsFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        contentsList=cont;
        f.setArguments(b);
        return f;
    }

    public NewsDetailsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        position = getArguments().getInt(ARG_POSITION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        position = getArguments().getInt(ARG_POSITION);
        view=inflater.inflate(R.layout.fragment_detail_web, null);
        gson = new Gson();


        initViews(view);
        showContents(position,view);
        loadComments();

        return view;
    }

    private void initViews(View v) {

        rootView =v;

        cat_flex = (FlexboxLayout) rootView.findViewById(R.id.cat_layout);
        tag_flex = (FlexboxLayout) rootView.findViewById(R.id.tag_layout);
        person_flex = (FlexboxLayout) rootView.findViewById(R.id.person_layout);
        bus_flex = (FlexboxLayout) rootView.findViewById(R.id.business_layout);

        mContainer = (FrameLayout) rootView.findViewById(R.id.webview_frame);

        cat_layout =(LinearLayout)rootView.findViewById(R.id.cat_ll);
        tag_layout =(LinearLayout)rootView.findViewById(R.id.tag_ll);
        person_layout =(LinearLayout)rootView.findViewById(R.id.people_ll);
        business_layout =(LinearLayout)rootView.findViewById(R.id.bus_ll);

        news_image=(ImageView)rootView.findViewById(R.id.news_image);
        shareNews = (ImageView) view.findViewById(R.id.share_news);
        saveNews = (ImageView) view.findViewById(R.id.save_news);

        txtDate=(TextView)rootView.findViewById(R.id.news_date);
        news_title=(TextView)rootView.findViewById(R.id.news_title);
        news_author=(TextView)rootView.findViewById(R.id.news_author);
        answer_banner=(TextView)rootView.findViewById(R.id.answer_ll);

        news_answer=(WebView)rootView.findViewById(R.id.answer_detail);
        Utilities.setWebViewSettings(news_answer);
        news_detail1=(WebView)rootView.findViewById(R.id.news_detail);
        Utilities.setWebViewSettings(news_detail1);
        mWebViewComments = (WebView) rootView.findViewById(R.id.commentsView);
        Utilities.setWebViewSettings(mWebViewComments);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showContents(final int position, final View v) {

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
        news_detail1.loadData(Utilities.getWebCss(getActivity())+" "+description_,"text/html; charset=utf-8", "UTF-8");

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
            Glide.with(context).load(imgUrl_).into(news_image);

        }


        if(mNewsCategory.size()>0)
        {
            final List<NewsCategoryResponse> categories = mNewsCategory;
            JsonArray slugArrays = new JsonArray();
            JsonArray titleArrays = new JsonArray();
            for (int j = 0; j < categories.size(); j++) {

                final NewsCategoryResponse cc = categories.get(j);
                RelativeLayout tr_head = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.tag_layout, null);

                TextView label_date = (TextView) tr_head.findViewById(R.id.tag_name);
                label_date.setText(categories.get(j).getTitle());
                cat_flex.addView(tr_head);

                tr_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PriyoCategoryNewsActivity.class);
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
                RelativeLayout tr_head = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.tag_layout, null);

                TextView label_date = (TextView) tr_head.findViewById(R.id.tag_name);
                label_date.setText(categories.get(j).getTitle());

                tag_flex.addView(tr_head);

                tr_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(), PriyoCategoryNewsActivity.class);
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
                RelativeLayout tr_head = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.tag_layout, null);

                TextView label_date = (TextView) tr_head.findViewById(R.id.tag_name);
                label_date.setText(categories.get(j).getTitle());
                person_flex.addView(tr_head);

                tr_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PriyoCategoryNewsActivity.class);
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
                RelativeLayout tr_head = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.tag_layout, null);

                TextView label_date = (TextView) tr_head.findViewById(R.id.tag_name);
                label_date.setText(categories.get(j).getTitle());
                bus_flex.addView(tr_head);
                tr_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PriyoCategoryNewsActivity.class);
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


        boolean isFav= false;
        String ids = contentsList.get(position).getId();

        Cursor cursor = context.getContentResolver().query(
                NewsContract.FavEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                String id = cursor.getString(cursor.getColumnIndex(NewsContract.FavEntry.COLUMN_NEWS_ID));

                if(ids.equals(id)) {
                    isFav = true;
                    break;
                }
                else
                    isFav=false;
            }
        }
        if(isFav){
            saveNews.setVisibility(View.INVISIBLE);
        }
        else
        {
            saveNews.setVisibility(View.VISIBLE);
        }


        shareNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PriyoNews model =contentsList.get(position);
                SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date date=null;
                Calendar cal=null;
                try {
                    date = utcFormat.parse(model.getPublishedAt());
                    cal = Calendar.getInstance();
                    cal.setTime(date);

                }catch (Exception e){e.printStackTrace();}


                final String month = checkDigit(cal.get(Calendar.MONTH)+1);
                final String dates = checkDigit(cal.get(Calendar.DATE));
                final String year = checkDigit(cal.get(Calendar.YEAR));
                shareNews(position,year,month, dates,model.getSlug());
            }
        });

        saveNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNews(contentsList.get(position));
            }
        });

        postUrl = "https://www.priyo.com/articles/"+contentsList.get(position).getSlug();



    }

    public void switchToMoreNewsFragment(String key,String title) {
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        bundle.putString("title", title);
        MoreNewsFragment fragobj = new MoreNewsFragment();
        fragobj.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.more_frame, fragobj).addToBackStack(null).commit();
    }
    public void switchToMorePeopleFragment(String key,String title) {
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        bundle.putString("title", title);
        MorePeopleFragment fragobj = new MorePeopleFragment();
        fragobj.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.more_people_frame, fragobj).addToBackStack(null).commit();
    }

    public void switchToMoreBusFragment(String key,String title) {
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        bundle.putString("title", title);
        MoreBusFragment fragobj = new MoreBusFragment();
        fragobj.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.more_bus_frame, fragobj).addToBackStack(null).commit();
    }

    public void switchToMoreLocFragment(String key,String title) {
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        bundle.putString("title", title);
        MoreLocFragment fragobj = new MoreLocFragment();
        fragobj.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.more_loc_frame, fragobj).addToBackStack(null).commit();
    }

    public void switchToMoreHistoryFragment(String key) {
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        HistoryFragment fragobj = new HistoryFragment();
        fragobj.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.history_frame, fragobj).addToBackStack(null).commit();
    }


    public void saveNews(PriyoNews model){

        Vector<ContentValues> cVVector = new Vector<ContentValues>(1);
        ContentValues newsValues = new ContentValues();

        Date date = new Date();

        newsValues.put(NewsContract.FavEntry.COLUMN_NEWS_ID, model.getId());
        newsValues.put(NewsContract.FavEntry.COLUMN_TITLE, model.getTitle());
        newsValues.put(NewsContract.FavEntry.COLUMN_DESCRIPTION, model.getDescription());
        newsValues.put(NewsContract.FavEntry.COLUMN_SUMMERY, model.getSummary());
        newsValues.put(NewsContract.FavEntry.COLUMN_SLUG, model.getSlug());
        newsValues.put(NewsContract.FavEntry.COLUMN_UPDATE_AT, model.getUpdatedAt());
        newsValues.put(NewsContract.FavEntry.COLUMN_CREATE_AT, model.getCreatedAt());
        newsValues.put(NewsContract.FavEntry.COLUMN_PUBLISHED_AT, model.getPublishedAt());
        newsValues.put(NewsContract.FavEntry.COLUMN_CATEGORY, model.getCategories());
        newsValues.put(NewsContract.FavEntry.COLUMN_AUTHOR, model.getAuthor());
        newsValues.put(NewsContract.FavEntry.COLUMN_TAGS, model.getTags());
        newsValues.put(NewsContract.FavEntry.COLUMN_BUSINESS, model.getBusinesses());
        newsValues.put(NewsContract.FavEntry.COLUMN_PERSONS, model.getPeople());
        newsValues.put(NewsContract.FavEntry.COLUMN_IMG, model.getImage());
        newsValues.put(NewsContract.FavEntry.COLUMN_INSERT_DATE_TIME, String.valueOf(date.getTime()));

        cVVector.add(newsValues);


        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);


            Cursor cursor = context.getContentResolver().query(
                    NewsContract.FavEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor.getCount() > 0) {
                for (int i = 0; i < cVVector.size(); i++) {
                    String ids = cVVector.get(i).getAsString(NewsContract.FavEntry.COLUMN_NEWS_ID);
                    context.getContentResolver().delete(NewsContract.FavEntry.CONTENT_URI,
                            NewsContract.FavEntry.COLUMN_NEWS_ID + " =?",
                            new String[]{ids});

                }
            }

            context.getContentResolver().bulkInsert(NewsContract.FavEntry.CONTENT_URI,
                    cvArray);

            saveNews.setVisibility(View.INVISIBLE);
        }
    }


    private void loadComments() {
        mWebViewComments.setWebViewClient(new UriWebViewClient());
        mWebViewComments.setWebChromeClient(new UriChromeClient());
        mWebViewComments.getSettings().setJavaScriptEnabled(true);
        mWebViewComments.getSettings().setAppCacheEnabled(true);
        mWebViewComments.getSettings().setDomStorageEnabled(true);
        mWebViewComments.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebViewComments.getSettings().setSupportMultipleWindows(true);
        mWebViewComments.getSettings().setSupportZoom(false);
        mWebViewComments.getSettings().setBuiltInZoomControls(false);
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= 21) {
            mWebViewComments.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebViewComments, true);
        }

        // facebook comment widget including the article url
        String html = "<!doctype html> <html lang=\"en\"> <head></head> <body> " +
                "<div id=\"fb-root\"></div> <script>(function(d, s, id) { var js, fjs = d.getElementsByTagName(s)[0]; if (d.getElementById(id)) return; js = d.createElement(s); js.id = id; js.src = \"//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.6\"; fjs.parentNode.insertBefore(js, fjs); }(document, 'script', 'facebook-jssdk'));</script> " +
                "<div class=\"fb-comments\" data-href=\"" + postUrl + "\" " +
                "data-numposts=\"" + NUMBER_OF_COMMENTS + "\" data-order-by=\"reverse_time\">" +
                "</div> </body> </html>";


        System.out.println("FB MSG "+html);

        mWebViewComments.loadDataWithBaseURL("http://www.nothing.com", html, "text/html", "UTF-8", null);
        mWebViewComments.setMinimumHeight(200);
    }

    private void setLoading(boolean isLoading) {
        if (isLoading)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    private class UriWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            String host = Uri.parse(url).getHost();

            return !host.equals("m.facebook.com");

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String host = Uri.parse(url).getHost();
            setLoading(false);
            if (url.contains("/plugins/close_popup.php?reload")) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mContainer.removeView(mWebviewPop);
                        loadComments();
                    }
                }, 600);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            setLoading(false);
        }
    }

    class UriChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            mWebviewPop = new WebView(getContext());
            mWebviewPop.setVerticalScrollBarEnabled(false);
            mWebviewPop.setHorizontalScrollBarEnabled(false);
            mWebviewPop.setWebViewClient(new UriWebViewClient());
            mWebviewPop.setWebChromeClient(this);
            mWebviewPop.getSettings().setJavaScriptEnabled(true);
            mWebviewPop.getSettings().setDomStorageEnabled(true);
            mWebviewPop.getSettings().setSupportZoom(false);
            mWebviewPop.getSettings().setBuiltInZoomControls(false);
            mWebviewPop.getSettings().setSupportMultipleWindows(true);
            mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mContainer.addView(mWebviewPop);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            Log.i("FB MSG", "onConsoleMessage: " + cm.message());
            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
        }
    }


    public void shareNews(int position, final String year, final String month, final String dates, final String slug){

        //String link = "http://www.priyo.com/articles/"+year+"/"+month+"/"+dates+"/"+slug;

        String link = "http://www.priyo.com/articles/"+slug;

        System.out.println("RTRT  "+link);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, link);
        context.startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        news_detail1.saveState(outState);
        super.onSaveInstanceState(outState);
    }


    public String checkDigit (int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}