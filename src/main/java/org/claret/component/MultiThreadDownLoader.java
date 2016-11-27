package org.claret.component;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 多线程下载器
 *
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 */
public class MultiThreadDownLoader {
    public static final int MAX_WAIT_TIME = 5000;
    public static final String SUFFIX = ".dtmp";

    // 文件路径，例如D:/xampp/
    private String filePath;

    private String fileFullName;

    private List<Long> ids = new ArrayList<Long>();

    private int finishR = 0;

    private boolean downFinish = false;

    private String uri;

    private long size = 0;

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    // 吹哨任务
    private CountDownLatch whistler = new CountDownLatch(1);

    // 下载线程
    private int threadCount = 1;
    private CountDownLatch downloadLatchs = new CountDownLatch(threadCount);

    public MultiThreadDownLoader(final String uri) {
        this.uri = uri;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
        // 设置三个运动员
        downloadLatchs = new CountDownLatch(threadCount);
    }

    public boolean setFileFullName(String fileFullName) {
        this.fileFullName = fileFullName;
        File f = new File(fileFullName);
        filePath = f.getParent();
        File fileDir = new File(filePath);
        return !fileDir.exists() && fileDir.mkdir();
    }

    private boolean removeTempFiles() {
        boolean isRemoved = true;
        for (Long id : ids) {
            File deleteFile = new File(filePath + File.separator + String.valueOf(id) + SUFFIX);
            if (!(deleteFile.exists() && deleteFile.delete())) {
                isRemoved = false;
            }
        }
        return isRemoved;
    }

    public Boolean download() {
        Callable<Boolean> initTask = new Callable<Boolean>() {
            /*
             * 确认下载文件的大小，不一定可以拿到的，有的http响应并没有指明content length，
             * 如果拿不到，只能单线程下载了
             */
            public boolean initTotalSize(){
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(MAX_WAIT_TIME);
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if (200 == code) {
                        MultiThreadDownLoader.this.size = conn.getContentLength();
                        return true;
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    // 确认大小失败
                    return false;
                }
            }

            /*
             * 创建待下载的随机读写文件
             */
            public boolean createDownloadFile(){
                try {
                    RandomAccessFile raf;
                    raf = new RandomAccessFile(fileFullName, "rw");
                    // 创建一个一样大的空文件
                    raf.setLength(size);
                    raf.close();
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }

            public Boolean call() {

                if( ! initTotalSize()){
                    // 使用一个线程下载
                    threadCount = 1;
                }

                if(!createDownloadFile()){
                    return false;
                }

                // 每个线程应该下载的大小
                long blockSize = size / threadCount;
                for (long i = 0; i < threadCount; i++) {
                    long startIndex = i * blockSize;
                    long endIndex = startIndex + blockSize - 1;
                    if (i == threadCount - 1) {
                        // 最后一个线程下载到文件尾即可
                        endIndex = size;
                    }
                    // 记录每个线程的id
                    ids.add(i);
                    String tmpFile = filePath + File.separator + i + SUFFIX;
                    DownLoadRunnable dr = new DownLoadRunnable(fileFullName, startIndex, endIndex, uri, tmpFile);
                    dr.setDownLoader(MultiThreadDownLoader.this);
                    dr.setNotifyLatch(downloadLatchs);
                    // 扔到线程池运行
                    threadPool.execute(dr);
                }
                // 吹哨
                whistler.countDown();
                //等待线程下载完毕
                try {
                    // 使用jdk的吹哨机制，会不会存在一旦其中某个线程线程异常挂掉，致使整个下载阻塞？
                    downloadLatchs.await();
                    // 保证线程执行完毕后关闭线程池
                    threadPool.shutdown();
                    if (downFinish) {
                        // 如果下载完成，删除临时文件，否则保留做断点续传
                        removeTempFiles();
                        return true;
                    }
                } catch (InterruptedException e) {
                    return false;
                }
                return false;
            }
        };

        FutureTask<Boolean> future = new FutureTask<Boolean>(initTask);
        new Thread(future).start();
        try {
            return future.get();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 下载线程通知下载器，有一个线程下载完成了
     * 加对象锁
     */
    public synchronized void notifyFinish() {
        finishR++;
        if (finishR == threadCount) {
            downFinish = true;
        }
    }

}
