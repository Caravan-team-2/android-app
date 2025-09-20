# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep public class smartcaravans.constat.client.auth.presentation.navigation.NavRoutes
-keep public class smartcaravans.constat.client.auth.presentation.navigation.NavRoutes$*
-keep public class smartcaravans.constat.client.main.presentation.navigation.NavRoutes
-keep public class smartcaravans.constat.client.main.presentation.navigation.NavRoutes$*
-dontwarn org.slf4j.impl.StaticLoggerBinder
-keep class androidx.credentials.playservices.** {
  *;
}
-keepnames class smartcaravans.constat.client.auth.presentation.navigation.NavRoutes
-keepnames class smartcaravans.constat.client.main.presentation.navigation.NavRoutes
-keepnames class * extends smartcaravans.constat.client.auth.presentation.navigation.NavRoutes
-keepnames class * extends smartcaravans.constat.client.main.presentation.navigation.NavRoutes
-keep public class smartcaravans.constat.client.core.domain.models$*
-keep public class smartcaravans.constat.client.auth.domain.dto.AuthResponse
-keep public class smartcaravans.constat.client.auth.domain.dto.AuthResult
-keep public class smartcaravans.constat.client.auth.domain.dto$*