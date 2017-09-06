# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Applications/Android Studio.app/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
# Add any project specific keep options here:
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# Obfuscation parameters:
#-dontobfuscate
-keepattributes Signature
-keepattributes *Annotation*

-useuniqueclassmembernames
-allowaccessmodification
-keep class com.google.** { *; }
-keep class com.journeyapps.** { *; }
-keep class com.nostra13.** { *; }
-keep class com.makeramen.** { *; }
-keep class com.github.** { *; }
-keep class org.apache.** { *; }
-keep class com.flipboard.** { *; }
-keep class com.android.** { *; }
-keep class com.mikepenz.** { *; }
-keep class junit.** { *; }
-keep class org.mockito.** { *; }
-keep class com.squareup.** { *; }
-keep class android.support.v7.widget.SearchView { *; }
-keep class com.priyo.go.Model.** { *; }
-keep class com.priyo.people.database.model.** { *; }
-keep class org.greenrobot.eventbus.** { *; }
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keepclassmembers class com.priyo.people.database.model.** { *; }
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keepattributes Signature
-keepattributes *Annotation*

-dontwarn com.google.**
-dontwarn com.nostra13.**
-dontwarn org.apache.**
-dontwarn android.support.**
-dontwarn org.junit.**
-dontwarn org.mockito.**
-dontwarn com.makeramen.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.greenrobot.**

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** w(...);
    public static *** i(...);
    public static *** e(...);
}


