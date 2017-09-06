package com.priyo.go.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.iid.InstanceID;
import com.priyo.go.Model.DeviceInformation;
import com.priyo.go.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Utilities {

    public static boolean isConnectionAvailable(Context context) {
        if (context == null) return false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static boolean isTabletDevice(Context context) {
        boolean large = (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE;
        boolean xlarge = (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE;
        return large || xlarge;
    }


    public static String getLongLatWithoutGPS(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        double latitude;
        double longitude;

        if (ContextCompat.checkSelfPermission(context,
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        } else {
            Location location = lm
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                return longitude + ", " + latitude;
            } else return null;
        }
    }

    public static String getFilePath(Context context, Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        String filePath = null;
        if (cursor != null && cursor.moveToFirst()) {
            filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            cursor.close();
        }

        return filePath;
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyboard(Context context, View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void setKeyboardHide(Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }


    public static void showKeyboard(Context context) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void showKeyboard(Context context, final EditText editText) {

        final InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (!editText.hasFocus()) {
            editText.requestFocus();
        }

        editText.post(new Runnable() {
            @Override
            public void run() {
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    public static String getDateFormat(long time) {
        return new SimpleDateFormat("MMM d, yyyy, h:mm a").format(time);
    }

    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);

        return resultCode == ConnectionResult.SUCCESS;
    }

    public static String getExtension(String filePath) {
        if (filePath != null && filePath.lastIndexOf('.') >= 0)
            return filePath.substring(0, filePath.lastIndexOf('.')).toLowerCase();
        else
            return "";
    }

    public static String convertIntoBanglaDate(String englishDate) {
        System.out.println("Time diff " + englishDate);
        if (englishDate.contains("0"))
            englishDate = englishDate.replaceAll("0", "০");
        if (englishDate.contains("1"))
            englishDate = englishDate.replaceAll("1", "১");
        if (englishDate.contains("2"))
            englishDate = englishDate.replaceAll("2", "২");
        if (englishDate.contains("3"))
            englishDate = englishDate.replaceAll("3", "৩");
        if (englishDate.contains("4"))
            englishDate = englishDate.replaceAll("4", "৪");
        if (englishDate.contains("5"))
            englishDate = englishDate.replaceAll("5", "৫");
        if (englishDate.contains("6"))
            englishDate = englishDate.replaceAll("6", "৬");
        if (englishDate.contains("7"))
            englishDate = englishDate.replaceAll("7", "৭");
        if (englishDate.contains("8"))
            englishDate = englishDate.replaceAll("8", "৮");
        if (englishDate.contains("9"))
            englishDate = englishDate.replaceAll("9", "৯");

        if (englishDate.contains("January"))
            englishDate = englishDate.replaceAll("January", "জানুয়ারী");
        if (englishDate.contains("February"))
            englishDate = englishDate.replaceAll("February", "ফেব্রুয়ারি");
        if (englishDate.contains("March"))
            englishDate = englishDate.replaceAll("March", "মার্চ");
        if (englishDate.contains("April"))
            englishDate = englishDate.replaceAll("April", "এপ্রিল");
        if (englishDate.contains("May"))
            englishDate = englishDate.replaceAll("May", "মে");
        if (englishDate.contains("June"))
            englishDate = englishDate.replaceAll("June", "জুন");
        if (englishDate.contains("July"))
            englishDate = englishDate.replaceAll("July", "জুলাই");
        if (englishDate.contains("August"))
            englishDate = englishDate.replaceAll("August", "আগস্ট");
        if (englishDate.contains("September"))
            englishDate = englishDate.replaceAll("September", "সেপ্টেম্বর");
        if (englishDate.contains("October"))
            englishDate = englishDate.replaceAll("October", "অক্টোবর");
        if (englishDate.contains("November"))
            englishDate = englishDate.replaceAll("November", "নভেম্বর");
        if (englishDate.contains("December"))
            englishDate = englishDate.replaceAll("December", "ডিসেম্বর");

        if (englishDate.contains("year"))
            englishDate = englishDate.replaceAll("year", "বছর");

        if (englishDate.contains("month"))
            englishDate = englishDate.replaceAll("month", "মাস");

        if (englishDate.contains("day"))
            englishDate = englishDate.replaceAll("day", "দিন");

        if (englishDate.contains("hour"))
            englishDate = englishDate.replaceAll("hour", "ঘন্টা");

        if (englishDate.contains("minute"))
            englishDate = englishDate.replaceAll("minute", "মিনিট");

        if (englishDate.contains("second"))
            englishDate = englishDate.replaceAll("second", "সেকেন্ড");

        System.out.println("Time diff " + englishDate);
        return englishDate;
    }

    public static String changeDateFormatGMTtoBST(String englishDate) {

        //String dtc = "2014-04-02T07:59:02.111Z";
        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
        Date date = null;
        try {
            date = readDate.parse(englishDate);
        } catch (Exception e) {
        }
        SimpleDateFormat writeDate = new SimpleDateFormat("dd MMMM yyyy সময়-HH:mm");
        writeDate.setTimeZone(TimeZone.getTimeZone("GMT+06:00"));
        String s = writeDate.format(date);
        return s;

    }

    public static String changeDateFormat4(String englishDate) {

        //String dtc = "2014-04-02T07:59:02.111Z";
        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        readDate.setTimeZone(TimeZone.getTimeZone("GMT+06:00")); // missing line
        Date date = null;
        try {
            date = readDate.parse(englishDate);
        } catch (Exception e) {
        }
        SimpleDateFormat writeDate = new SimpleDateFormat("MMM dd, yyyy");
        writeDate.setTimeZone(TimeZone.getTimeZone("GMT+06:00"));
        String s = writeDate.format(date);
        return s;

    }

    public static String changeDateFormat1(String englishDate) {

        //String dtc = "2014-04-02T07:59:02.111Z";

        try {
            SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
            Date date = null;
            date = readDate.parse(englishDate);
            SimpleDateFormat writeDate = new SimpleDateFormat("dd MMMM yyyy");
            writeDate.setTimeZone(TimeZone.getTimeZone("GMT+06:00"));
            String s = writeDate.format(date);
            return s;
        } catch (Exception e) {
            return englishDate;
        }
    }

    public static String changeDateFormat12(String englishDate) {

        //String dtc = "2014-04-02T07:59:02.111Z";

        try {
            SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
            Date date = null;
            date = readDate.parse(englishDate);
            SimpleDateFormat writeDate = new SimpleDateFormat("yyyy MMMM dd");
            writeDate.setTimeZone(TimeZone.getTimeZone("GMT+06:00"));
            String s = writeDate.format(date);
            return s;
        } catch (Exception e) {
            return englishDate;
        }
    }

    public static String changeDateFormat2(String englishDate) {

        //String dtc = "2014-04-02T07:59:02.111Z";

        try {
            SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
            Date date = null;
            date = readDate.parse(englishDate);
            SimpleDateFormat writeDate = new SimpleDateFormat("dd MMMM");
            writeDate.setTimeZone(TimeZone.getTimeZone("GMT+06:00"));
            String s = writeDate.format(date);
            return s;
        } catch (Exception e) {
            return englishDate;
        }


    }

    public static String changeDateFormat3(String englishDate) {

        //String dtc = "2014-04-02T07:59:02.111Z";

        try {
            SimpleDateFormat readDate = new SimpleDateFormat("dd/MM/yyyy");
            readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
            Date date = null;
            date = readDate.parse(englishDate);
            SimpleDateFormat writeDate = new SimpleDateFormat("dd MMMM");
            writeDate.setTimeZone(TimeZone.getTimeZone("GMT+06:00"));
            String s = writeDate.format(date);
            return s;
        } catch (Exception e) {
            return englishDate;
        }


    }

    public static String TimeDifferenceWithCurrentDate(String englishDate) {

        //String dtc = "2014-04-02T07:59:02.111Z";
        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        readDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        String gmtTime = readDate.format(new Date());

        //Date now = new Date();

        Date date = null, strDate = null;
        try {
            strDate = readDate.parse(gmtTime);
            date = readDate.parse(englishDate);
        } catch (Exception e) {
        }

        long timeDiff = Math.abs(date.getTime() - strDate.getTime());

        System.out.println("Time diff " + timeDiff);

        long seconds = timeDiff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;
        long years = days / 12;

        String time = days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;

        System.out.println("Time diff " + time);

        long diffYears = years;
        long diffMonths = months % 12;
        long diffDays = days % 30;
        long diffHours = hours % 24;
        long diffMinutes = minutes % 60;
        long diffSeconds = seconds % 60;


//        long diffSeconds =
// (timeDiff / (60 * 60 * 1000 *24 *30 *12));

//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(timeDiff);
//        int diffYears = c.get(Calendar.YEAR)-1970;
//        int diffMonths = c.get(Calendar.MONTH);
//        int diffDays = c.get(Calendar.DAY_OF_MONTH)-1;
//        int diffHours = c.get(Calendar.HOUR);
//        int diffMinutes = c.get(Calendar.MINUTE);
//        int diffSeconds = c.get(Calendar.SECOND);

        String s = "";

        if (diffYears != 0)
            s = String.valueOf(diffYears) + " year";
        else if (diffMonths != 0)
            s = String.valueOf(diffMonths) + " month";
        else if (diffDays != 0)
            s = String.valueOf(diffDays) + " day";
        else if (diffHours != 0)
            s = String.valueOf(diffHours) + " hour";
        else if (diffMinutes != 0)
            s = String.valueOf(diffMinutes) + " minute";
        else if (diffSeconds != 0)
            s = String.valueOf(diffSeconds) + " second";
        return s;

    }

    public static String getTodaysdate(String format) {
        SimpleDateFormat sdfDate = new SimpleDateFormat(format);
        Date now = new Date();
        return sdfDate.format(now);
    }

    public static String removeUnnecessaryHtmlTag(String cleanedText) {
        cleanedText = cleanedText.replace("&Acirc;", " ");
        cleanedText = cleanedText.replaceAll("<style([\\s\\S]+?)</style>", "");
        cleanedText = cleanedText.replaceAll("<span([\\s\\S]+?)>", "");
        cleanedText = cleanedText.replaceAll("</span>", "");
        return cleanedText;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public static void setWebViewSettings(WebView webView) {
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    public static String getWebCss(Context c) {
        String textSize = c.getResources().getString(R.string.web_view_text_size);
        String IMAGE_DIV = "<style type='text/css'>"
                + "img"
                + "{"
                + "    width:100%;"
                + "    height:auto;"
                + "    padding-top:5px;"
                + "}"
                + "p"
                + "{"
                + "    font-size: " + textSize + ";"
                + "}"
                + "iframe"
                + "{"
                + "    width:100%;"
                + "    padding-top:5px;"
                + "}"
                + "</style>";


        return IMAGE_DIV;
    }


    public static Bitmap getImageBitmap(final String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(filePath, options);
    }

    @SuppressLint("HardwareIds")
    public static String getAndroidID(ContentResolver contentResolver) {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static String getDeviceID() {
        return Build.ID;
    }

    public static String generateIID(Context context) {
        return InstanceID.getInstance(context).getId();
    }

    public static String generateGIUD() {
        return UUID.randomUUID().toString();
    }

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    @SuppressWarnings("MissingPermission")
    public static Location getLastKnownLocation(LocationManager locationManager) {
        Location bestLocation = null;
        List<String> providers = locationManager.getProviders(true);
        if (providers != null) {
            for (String provider : providers) {
                Location tempBestLocation = locationManager.getLastKnownLocation(provider);
                if (tempBestLocation != null) {
                    if (bestLocation != null) {
                        if (bestLocation.getAccuracy() > tempBestLocation.getAccuracy()) {
                            bestLocation = tempBestLocation;
                        }
                    } else {
                        bestLocation = tempBestLocation;
                    }
                }
            }
        }
        return bestLocation;
    }

    public static DeviceInformation getDeviceInformation(Context context) {
        String androidID = getAndroidID(context.getContentResolver());
        String deviceManufacturer = getDeviceManufacturer();
        String deviceName = getDeviceName();
        String deviceID = getDeviceID();
        String IID = generateIID(context);
        String GIUD = generateGIUD();
        return new DeviceInformation(androidID, deviceManufacturer, deviceID, deviceName, IID, GIUD);
    }

    public static boolean hasPermission(Context context, final String permissionTag) {
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                permissionTag);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean hasBirthdayEvent(Context context, String eventTitle) {
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.android.calendar/events"),
                new String[]{"title"}, " title = ? ",
                new String[]{eventTitle}, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }
}
