# Keep your classes but exclude R and BuildConfig
-keep class !dev.soughtxp.wamodx.R, !dev.soughtxp.wamodx.R$*, !dev.soughtxp.wamodx.BuildConfig, dev.soughtxp.wamodx.** { *; }

# Remove Kotlin metadata and annotations
-dontwarn kotlin.**
-dontwarn org.jetbrains.**
-dontwarn org.intellij.**
-dontwarn javax.annotation.**

# Aggressive shrinking and optimization
-repackageclasses ''
-allowaccessmodification
-overloadaggressively

# Ignore notes and warnings about missing classes
-dontnote X.**
-dontwarn X.**
-dontnote **
-dontwarn **
