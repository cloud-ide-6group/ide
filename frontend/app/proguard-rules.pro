-keep class io.ktor.** { *; }
-keeppackagenames io.ktor.**
-keepdirectories META-INF/**
-keep class kotlinx.coroutines.** { *; }

-dontwarn net.bytebuddy.**
-dontwarn org.objectweb.asm.**

-dontwarn io.mockk.**
-dontwarn org.junit.**

-keep class okio.** { *; }
-dontwarn okio.**

-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-keepattributes Signature, InnerClasses, AnnotationDefault

-keep class coil3.** { *; }
-dontwarn coil3.**

-ignorewarnings