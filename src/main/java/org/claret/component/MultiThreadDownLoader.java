package org.claret.component;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程下载器
 *
 * @author lvyahui (lvyahui8@gmail.com)
 */
public class MultiThreadDownLoader {
    public static final long MAX_WAIT_TIME = 5000;
    private String filePath;            //文件路径，例如D:/xampp/
    private String fileFullName;        //文件全名，例如D:/xampp/start.exe
    private List<Long> ids = new ArrayList<Long>();

    private int threadCount = 1;

    private int finishR = 0;
    private boolean downFinish = false;

    private String uri;
    private long size = 0;
    private boolean getSizeOk = false;

    private CountDownLatch cdAnswer = new CountDownLatch(1);

    private final int timeOut = 6000;

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    // 吹哨任务
    private CountDownLatch whistler = new CountDownLatch(1);

    public MultiThreadDownLoader(final String uri) {
        this.uri = uri;
        // 确认文件总大小
        new Thread(
                new Runnable() {
                    public void run() {
                        // 设置文件大小
                        try {
                            URL url = new URL(uri);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(timeOut);
                            conn.setRequestMethod("GET");
                            int code = conn.getResponseCode();
                            if (200 == code) {
                                MultiThreadDownLoader.this.size = conn.getContentLength();
                                getSizeOk = true;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
        // 设置三个运动员
        cdAnswer = new CountDownLatch(threadCount);
    }

    public boolean setFileFullName(String fileFullName) {
        this.fileFullName = fileFullName;
        File f = new File(fileFullName);
        filePath = f.getParent();
        File fileDir = new File(filePath);
        return !fileDir.exists() && fileDir.mkdir();
    }


    private void delTmpFile() {
        System.out.println("删除临时文件");
        for (Long id : ids) {
            File deleteFile = new File(filePath + File.separator + String.valueOf(id));
            if (deleteFile.exists()) deleteFile.delete();
        }
    }

    public void start() {
        try {
            // 等待获取大小完成，这里类似于实现了jdk中的吹哨员机智，等待吹哨员线程，新版本直接使用jdk中的吹哨员来做
            {
                long startTime = System.currentTimeMillis();
                long currentTime = System.currentTimeMillis();
                while (!getSizeOk && currentTime - startTime < MAX_WAIT_TIME) {
                    currentTime = System.currentTimeMillis();
                }
                if(!getSizeOk){
                    // 直到最后都没有获取到总大小，则进行单线程下载
                }
            }
            RandomAccessFile raf;
            raf = new RandomAccessFile(fileFullName, "rw");
            // 创建一个一样大的空文件
            raf.setLength(size);
            raf.close();
            long blockSize = size / threadCount;
            // ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);

            for (long i = 0; i < threadCount; i++) {
                long startIndex = i * blockSize;
                long endIndex = startIndex + blockSize - 1;
                if (i == threadCount - 1) {
                    endIndex = size;
                }
                // 记录每个线程的id
                ids.add(i);
                String tmpFile = filePath + File.separator + i + ".dtmp";
                DownLoadRunnable dr = new DownLoadRunnable(startIndex, endIndex, uri, tmpFile);
                new Thread(dr).start();
            }
            cdAnswer.await();//等待线程下载完毕
            // 保证线程执行完毕后关闭线程池
            //threadPool.shutdown();
            // 删除临时文件
            if (downFinish) delTmpFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public long getSize() {
        return this.size;
    }
}
