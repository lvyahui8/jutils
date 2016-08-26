package org.claret.component;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DownLoader {
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

    public DownLoader(final String uri) {
        this.uri = uri;
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
                                DownLoader.this.size = conn.getContentLength();
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
        cdAnswer = new CountDownLatch(threadCount);
    }

    public void setFileFullName(String fileFullName) {
        this.fileFullName = fileFullName;
        File f = new File(fileFullName);
        filePath = f.getParent();
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
    }

    private class DownLoadRunnable implements Runnable {
        private long startIndex;
        private long endIndex;
        private String uri;
        private String tmpFile;

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
                    finishR++;
                    if (finishR == threadCount) {
                        downFinish = true;
                    }
                }
            }
        }


        private void download(HttpURLConnection conn) throws IOException{
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

    private void delTmpFile() {
        System.out.println("删除临时文件");
        for (Long id : ids) {
            File deleteFile = new File(filePath + File.separator + String.valueOf(id));
            if (deleteFile.exists()) deleteFile.delete();
        }
    }

    public void startDownload() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    {
                        long startTime = System.currentTimeMillis();
                        long currentTime = System.currentTimeMillis();
                        while (!getSizeOk && currentTime - startTime < MAX_WAIT_TIME) {
                            currentTime = System.currentTimeMillis();
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
        }).start();
    }

    public long getSize() {
        return this.size;
    }
}
