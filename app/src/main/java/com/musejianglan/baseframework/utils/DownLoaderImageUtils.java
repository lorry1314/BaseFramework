package com.musejianglan.baseframework.utils;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by liulei on 2015/12/13.
 */
public class DownLoaderImageUtils {
    public static final String TAG = "DownLoaderImageUtils";
    private boolean isContinue = true;

    private static DownLoaderImageUtils instance = new DownLoaderImageUtils();

    private DownLoaderImageUtils () { }

    public static DownLoaderImageUtils getInstance() {
        return instance;
    }

    public void download(String urlStr, String localPath, DownloadListener downloadListener) throws Exception {
        DownloadInfo info = new DownloadInfo(urlStr, localPath, downloadListener);
        new DownloadTask(info).start();
    }

    public void stop() {
        isContinue = false;
    }

    class DownloadTask {

        private DownloadInfo info;

        private int progressMax;

        private int currentProgress = 0;

        public DownloadTask(DownloadInfo info) {
            super();
            this.info = info;
        }

        public void start() throws Exception {
            isContinue = true;
            URL url = null;
            HttpURLConnection conn = null;
            try {
                url = new URL(info.urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(100 * 1000);
                conn.setRequestMethod("GET");
                progressMax = conn.getContentLength();
                InputStream in = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(in);
                File file = new File(info.localPath);
                if(!file.exists()){
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                byte[] buffer = new byte[1024];
                int count = -1;
                while (((count = bis.read(buffer)) != -1) && isContinue) {
                    bos.write(buffer, 0, count);
                    currentProgress+=count;
                    if(info.downloadListener != null) {
                        info.downloadListener.onDownNow(progressMax,currentProgress);
                    }
                }
                bos.close();
                bis.close();
                if((info.downloadListener != null) && isContinue) {
                    info.downloadListener.onSuccess(info.localPath);
                    Log.e("baidai", info.localPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if((info.downloadListener != null) && isContinue) {
                    info.downloadListener.onFail();
                }
                throw e;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }

    class DownloadInfo {

        private String urlStr;
        private String localPath;
        private DownloadListener downloadListener;

        public DownloadInfo(String urlStr, String localPath, DownloadListener downloadListener) {
            super();
            this.urlStr = urlStr;
            this.localPath = localPath;
            this.downloadListener = downloadListener;
        }
    }

    public interface DownloadListener {
        public void onDownNow(int progressMax, int currentProgress) throws Exception;
        public void onSuccess(String localPath) throws Exception;
        public void onFail() throws Exception;
    }
}
