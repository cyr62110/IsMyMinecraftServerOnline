#We need this to debug the obfuscated application, must be commented for production build
#-renamesourcefileattribute SourceFile
#-keepattributes SourceFile,LineNumberTable

#RxJava : We ignore the unsafe from sun.misc package since it is not included in Android framework
-dontwarn sun.misc.Unsafe

# OrmLite
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }

-keepclassmembers public class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper {
    public <init>(android.content.Context);
}

-keep public class * extends com.j256.ormlite.dao.BaseDaoImpl
-keepclassmembers public class * extends com.j256.ormlite.dao.BaseDaoImpl {
    public <init>(java.lang.Class);
    public <init>(com.j256.ormlite.support.ConnectionSource, java.lang.Class);
    public <init>(com.j256.ormlite.support.ConnectionSource, com.j256.ormlite.table.DatabaseTableConfig);
}

-keep @com.j256.ormlite.table.DatabaseTable public class *
-keepclassmembers @com.j256.ormlite.table.DatabaseTable public class * {
    public <init>();
    @com.j256.ormlite.field.DatabaseField *;
}

#AndroidAnnotations
-dontwarn org.w3c.**
-dontwarn org.springframework.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#Jackson
-keepnames class com.fasterxml.jackson.** { *; }

#Minecraft Tools library
-keep class fr.cvlaminck.immso.minecraft.**
-keepclassmembers class fr.cvlaminck.immso.minecraft.** { *; }
-keep enum fr.cvlaminck.immso.minecraft.**
-keepclassmembers enum fr.cvlaminck.immso.minecraft.** { *; }
-keep interface fr.cvlaminck.immso.minecraft.**
-keepclassmembers interface fr.cvlaminck.immso.minecraft.** { *; }