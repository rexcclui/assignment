package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provde the data persistence and average price calculation
 * @author rexcc
 *
 */
public class PxSvr
{
    static ArrayList<Price> pxList;

    private static BufferedWriter fileOutput;

    private static Logger logger;

    private static final String dataFile = "price.txt";

    private static Object persistLock = new Object();

    private static Object pxLock = new Object();

    private static ThreadPoolExecutor threadExecutor;

    private static int slotSize=1000000;

    static {
        try {
            pxList = new ArrayList<>(100);
            logger = LoggerFactory.getLogger(PxSvr.class);
            initPriceFromStore();
            threadExecutor = new ThreadPoolExecutor(2,2,100, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * 
     * Store the price to file and update to cache
     * 
     * @param px
     * @throws IOException
     */
    public static void update(double px) throws IOException {
        Price price = new Price(px, System.currentTimeMillis());
        synchronized (persistLock) {
            persist(price);
        }
        synchronized (pxLock) {
            pxList.add(price);
        }

        logger.info("Receive " + price);
    }

    public static double retrieveAvgPx(int lastX) {
        int sizeSnapShot = pxList.size(); // ensure the pxList is not update during average price calculation
        if (lastX > sizeSnapShot)
            return -1.0;
        double sumAvg = 0;
        int i;
        ArrayList<AvgPxRunnable> allRunnable = new ArrayList<>(10);
        for (i = sizeSnapShot -lastX; i < sizeSnapShot; i+=slotSize) {
            //split the calculation into sub task for huge volume e.g. lastX=9999999999999
            AvgPxRunnable run ;
            if (i+ slotSize < sizeSnapShot)
                run = new PxSvr.AvgPxRunnable(i, slotSize, lastX);
            else
                run = new PxSvr.AvgPxRunnable(i, sizeSnapShot-i, lastX);
            allRunnable.add(run);    
            threadExecutor.execute(run);
        }
        
        try {
            threadExecutor.shutdown();
            threadExecutor.awaitTermination(1000,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        threadExecutor = new ThreadPoolExecutor(2,2,100, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        for (AvgPxRunnable run: allRunnable)
            sumAvg += run.getSum();
        return sumAvg ;
    }

    
    static class AvgPxRunnable implements Runnable
    {

        double sum = 0;
        private int fromPos;
        public double getSum() {
            return sum;
        }

        private int slotSize;
        private int lastX;

        public AvgPxRunnable(int fromPos, int slotSize2, int lastX) {
            this.fromPos = fromPos;
            this.slotSize=slotSize2;
            this.lastX = lastX;
        }

        @Override
        public void run() {
            for (int i = fromPos; i < fromPos + slotSize; i++) {
                sum += pxList.get(i).getValue();
            }
            sum /= lastX;
        }

    }

    

    /**
     * 
     * Load the historical price at startup
     * 
     * @throws IOException
     */
    private static void initPriceFromStore() throws IOException {
        BufferedReader fileInput;

        try {
            fileInput = new BufferedReader(new FileReader(dataFile));
            String csvLine = null;
            while ((csvLine = fileInput.readLine()) != null) {
                try {
                    Price price = new Price();
                    price.importCSVString(csvLine);
                    pxList.add(price);
                } catch (Exception e) {
                    logger.error(csvLine + ":" + e.toString());
                    // corrupted line, continue
                }
            }
            fileInput.close();
            logger.info("initPriceFromStore done: records loaded " + pxList.size());
        } catch (FileNotFoundException e1) {
            // empty file
        } catch (IOException e) {
            throw e;
        }
        fileOutput = new BufferedWriter(new FileWriter(dataFile, true));

    }

    private static void persist(Price price) throws IOException {
        fileOutput.write(price.toCSVString() + "\n");
        fileOutput.flush();

    }
}
