package lab1.cwang87.lab1;

import java.util.LinkedList;
import java.util.Queue;

public class NumSlidingWindow {

    private static final int numWindowSize = 100;

    private Queue<Float> numWinQueue;
    private float numSum;
    private float wNumSum;
    private int wTotal;

    public NumSlidingWindow() {
        numWinQueue = new LinkedList<>();
        numSum = 0;
        wNumSum = 0;
        wTotal = (1 + numWindowSize) * numWindowSize / 2;
    }

    public float getSMA() {
        return numSum / numWindowSize;
    }

    public float getWMA() {
        return wNumSum / wTotal;
    }

    public int getSize() {
        return numWinQueue.size();
    }

    public void update(float joyData) {
        numWinQueue.offer(joyData);

        wNumSum = wNumSum - numSum + joyData * 100;
        numSum += joyData;

        if (numWinQueue.size() > numWindowSize) {
            numSum -= numWinQueue.poll();
        }
    }
}
