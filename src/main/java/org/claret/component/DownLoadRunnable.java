package org.claret.component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 * 下载任务
 *
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 */
public class DownLoadRunnable implements Runnable {
    private long startIndex;
    private long endIndex;
    private String uri;
    private String tmpFile;

    public void setNotifyLatch(CountDownLatch notifyLatch) {
        this.notifyLatch = notifyLatch;
    }

    private CountDownLatch notifyLatch;
    private String saveFile;

    private MultiThreadDownLoader downLoader;


    public DownLoadRunnable(String saveFile, long startIndex, long endIndex, String uri, String tmpFile) {
        super();
        this.saveFile = saveFile;
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
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            // 重点，设置下载部分数据
            conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
            if (206 == conn.getResponseCode()) {
                download(conn);
            }
            // 表示此线程下载完毕
            if (downLoader != null) {
                // 告诉多线程下载器，本线程下载完成了
                downLoader.notifyFinish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(notifyLatch != null){
                // 出现异常时不能让其余线程痴痴的等
                notifyLatch.countDown();
            }
        }
    }

    private void download(HttpURLConnection conn) throws IOException {
        InputStream is = conn.getInputStream();
        RandomAccessFile raf = new RandomAccessFile(saveFile, "rw");
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
            tmpRaf.writeUTF(String.valueOf(total + startIndex));

            tmpRaf.close();
        }
        is.close();
        raf.close();
    }

    /**
     * 确认起点，如果存在断点，从上次下载的位置的后一个字节开始下载
     *
     * @throws IOException
     */
    private void confirmStart() throws IOException {
        File f = new File(tmpFile);
        if (f.exists()) {
            RandomAccessFile tmpRaf = new RandomAccessFile(f, "r");
            startIndex = Long.parseLong(tmpRaf.readUTF());
            tmpRaf.close();
        }
    }

    /**
     * 持有下载器的引用
     *
     * @param downLoader 多线程下载器
     */
    public void setDownLoader(MultiThreadDownLoader downLoader) {
        this.downLoader = downLoader;
    }
}
