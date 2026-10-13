package dev.soughtxp.wamodx.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Environment;

import dev.soughtxp.wamodx.App;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public final class CacheUtils {

    private CacheUtils() {
    }

    // Pastas de cache em Android/media/{package}/{appName}/
    private static final String[] EXTERNAL_CACHE_SUBPATHS = {
            ".Shared",
            ".trash",
            ".Thumbs",
            ".StickerThumbs",
            ".CommonThumbs",
            ".StatusThumbs",
            ".Avatars",
            "Logs",
            "Backups",
            "Databases"
    };

    // Pastas de mídia em Android/media/{package}/{appName}/Media/
    private static final String[] MEDIA_SUBPATHS = {
            "%s Images",
            "%s Images/Sent",
            "%s Images/Private",
            "%s Video",
            "%s Video/Sent",
            "%s Video/Private",
            "%s Audio",
            "%s Audio/Sent",
            "%s Documents",
            "%s Documents/Sent",
            "%s Voice Notes",
            "%s Stickers",
            "%s Animated Gifs",
            "%s Video Notes",
            "%s Profile Photos",
            "%s Calls",
            "%s Wallpaper",
            "Cover Photos",
            "Payment Backgrounds",
            ".Statuses",
            ".StatusTranscoding",
            ".Links",
            ".Previews",
            "%s Backup Excluded Stickers",
            "%s Bug Report Attachments"
    };

    // Pastas internas em /data/data/{package}/
    private static final String[] INTERNAL_CACHE_PATHS = {
            "files/decompressed",
            "files/stickers_cache",
            "files/Logs",
            "files/Avatars",
            "files/.Shared",
            "files/.trash",
            "files/secure_shared",
            "files/tempStickers",
            "files/Ringtones",
            "files/crashLogs",
            "files/anr"
    };

    public interface CacheCallback {
        void onSizeCalculated(long totalSize, String formattedSize);
        void onClearComplete(boolean success);
    }

    private static String getAppName(Context context) {
        try {
            Context appContext = context != null ? context : App.getContext();
            if (appContext != null) {
                ApplicationInfo appInfo = appContext.getApplicationInfo();
                return appContext.getPackageManager().getApplicationLabel(appInfo).toString();
            }
        } catch (Exception e) {
        }
        return "WhatsApp";
    }

    private static List<File> getCacheDirectories(Context context) {
        List<File> dirs = new ArrayList<>();
        Context appContext = context != null ? context : App.getContext();
        if (appContext == null) {
            return dirs;
        }
        
        // Cache padrão do Android
        File cacheDir = appContext.getCacheDir();
        if (cacheDir != null && cacheDir.exists()) {
            dirs.add(cacheDir);
        }

        // Code cache
        File codeCacheDir = appContext.getCodeCacheDir();
        if (codeCacheDir != null && codeCacheDir.exists()) {
            dirs.add(codeCacheDir);
        }

        // Cache externo
        File externalCacheDir = appContext.getExternalCacheDir();
        if (externalCacheDir != null && externalCacheDir.exists()) {
            dirs.add(externalCacheDir);
        }

        String packageName = appContext.getPackageName();
        String appName = getAppName(appContext);

        // Base: Android/media/{package}/{appName}/
        File externalMedia = new File(Environment.getExternalStorageDirectory(),
                "Android/media/" + packageName + "/" + appName);

        // Pastas de cache externas
        for (String subpath : EXTERNAL_CACHE_SUBPATHS) {
            File dir = new File(externalMedia, subpath);
            if (dir.exists()) {
                dirs.add(dir);
            }
        }

        // Pastas de mídia em Media/
        File mediaFolder = new File(externalMedia, "Media");
        for (String subpath : MEDIA_SUBPATHS) {
            String path = subpath.contains("%s") ? String.format(subpath, appName) : subpath;
            File dir = new File(mediaFolder, path);
            if (dir.exists()) {
                dirs.add(dir);
            }
        }

        // Pastas internas
        File dataDir = appContext.getDataDir();
        if (dataDir != null) {
            for (String path : INTERNAL_CACHE_PATHS) {
                File dir = new File(dataDir, path);
                if (dir.exists()) {
                    dirs.add(dir);
                }
            }
        }

        return dirs;
    }

    public static void calculateCacheSize(final Context context, final CacheCallback callback) {
        new AsyncTask<Void, Void, long[]>() {
            @Override
            protected long[] doInBackground(Void... voids) {
                long totalSize = 0;
                for (File dir : getCacheDirectories(context)) {
                    totalSize += getDirectorySize(dir);
                }
                return new long[]{totalSize};
            }

            @Override
            protected void onPostExecute(long[] result) {
                if (callback != null) {
                    callback.onSizeCalculated(result[0], formatSize(result[0]));
                }
            }
        }.execute();
    }

    private static long getDirectorySize(File dir) {
        long size = 0;
        if (dir == null || !dir.exists()) {
            return 0;
        }

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    size += getDirectorySize(file);
                } else {
                    size += file.length();
                }
            }
        }
        return size;
    }

    public static String formatSize(long bytes) {
        if (bytes <= 0) {
            return "0 B";
        }

        final String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));

        if (digitGroups >= units.length) {
            digitGroups = units.length - 1;
        }

        DecimalFormat df = new DecimalFormat("#,##0.##");
        return df.format(bytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static void clearCache(final Context context, final ProgressDialog progressDialog, final CacheCallback callback) {
        new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                List<File> allFiles = new ArrayList<>();

                for (File dir : getCacheDirectories(context)) {
                    collectFiles(dir, allFiles);
                }

                int total = allFiles.size();
                int done = 0;

                for (File file : allFiles) {
                    try {
                        file.delete();
                    } catch (Exception ignored) {
                    }
                    done++;
                    int percent = total > 0 ? (int) ((long) done * 100 / total) : 100;
                    publishProgress(percent);
                }

                for (File dir : getCacheDirectories(context)) {
                    deleteRecursive(dir);
                }

                return Boolean.TRUE;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                if (progressDialog != null && progressDialog.isShowing() && values.length > 0) {
                    progressDialog.setProgress(values[0]);
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {
                    }
                }
                if (callback != null) {
                    callback.onClearComplete(success);
                }
            }
        }.execute();
    }

    private static void collectFiles(File dir, List<File> fileList) {
        if (dir == null || !dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    collectFiles(file, fileList);
                }
                fileList.add(file);
            }
        }
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory == null || !fileOrDirectory.exists()) {
            return;
        }

        if (fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursive(child);
                }
            }
        }
        fileOrDirectory.delete();
    }
}
