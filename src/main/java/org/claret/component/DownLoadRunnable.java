package org.claret.component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 *
 * Created by lvyahui on 2016/8/27.
 */
public class DownLoadRunnable implements Runnable {
    private long startIndex;
    private long endIndex;
    private String uri;
    private String tmpFile;

    private CountDownLatch cdAnswer;
    private int timeOut;

    public void setCdAnswer(CountDownLatch cdAnswer) {
        this.cdAnswer = cdAnswer;
    }



    public DownLoadRunnable(long startIndex, long endIndex, String uri, String tmpFile) {
        super();
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.uri = uri;
        this.tmpFile = tmpFile;
    }

    public void run() {
        try {
            // 确认起点，从断点下载
            confirmStart();
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeOut);
            conn.setRequestMethod("GET");
            // 重点，设置下载部分数据
            conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
            if (206 == conn.getResponseCode()) {
                download(conn);
            }
            // 表示此线程下载完毕
            cdAnswer.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            synchronized (DownLoadRunnable.class) {
                // 修改公共数据
                finishR++;
                if (finishR == threadCount) {
                    downFinish = true;
                }
            }
        }
    }


    private void download(HttpURLConnection conn) throws IOException {
        InputStream is = conn.getInputStream();
        RandomAccessFile raf = new RandomAccessFile(fileFullName, "rw");
        raf.seek(startIndex);//定位文件

        int len;
        byte[] buffer = new byte[1024];
        // 此次下载的数据量
        long total = 0;
        while ((len = is.read(buffer)) != -1) {
            // 保证覆盖原tmp文件，以防数字尾部不被覆盖
            RandomAccessFile tmpRaf = new RandomAccessFile(tmpFile, "rwd");
            raf.write(buffer, 0, len);
            total += len;
            tmpRaf.writeUTF(total + startIndex + "");

            tmpRaf.close();
        }
        is.close();
        raf.close();
    }

    private void confirmStart() throws IOException {
        File f = new File(tmpFile);
        if (f.exists()) {
            RandomAccessFile tmpRaf = new RandomAccessFile(f, "r");
            startIndex = Long.parseLong(tmpRaf.readUTF());
            tmpRaf.close();
        }
    }
}
