package lab1.cwang87.lab1;

import java.util.LinkedList;
import java.util.Queue;

public class TimeSlidingWindow {

    private static final int timeWindowSize = 10;

    private Queue<Joy> timeWinQueue;
    private float timeSum;
    private float currTimeStamp;

    public TimeSlidingWindow() {
        timeWinQueue = new LinkedList<>();
        timeSum = (float) 0.0;
        currTimeStamp = (float) 0.0;
    }

    public void add(float joyData, float timeStamp) {
        timeWinQueue.offer(new Joy(joyData, timeStamp));
        timeSum += joyData;
        currTimeStamp = timeStamp;
    }

    public float getSMA() {
        float sma = timeSum / timeWinQueue.size();

        Joy joy = timeWinQueue.poll();
        timeSum -= joy.getJoyData();

        return sma;
    }

    public boolean reachWindow(){
        return (currTimeStamp - timeWinQueue.peek().getTimeStamp()) >= timeWindowSize;
    }

}
