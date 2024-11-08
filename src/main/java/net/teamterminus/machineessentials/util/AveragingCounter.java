package net.teamterminus.machineessentials.util;

import net.minecraft.world.World;

import java.util.Arrays;

/**
 * A simple average value over time tracker. Make sure to call update on each tick.
 */
public class AveragingCounter {

    /**
     * The default value to populate the array with. Most of the time, this is 0.
     */
    private final long defaultValue;
    /**
     * The current values tracked by the counter.
     */
    private final long[] values;
    /**
     * The last time this successfully ran an update.
     */
    private long lastUpdatedWorldTime = 0;
    private int currentIndex = 0;
    private double lastAverage = 0;

    public AveragingCounter() {
        this(0, 20);
    }

    public AveragingCounter(int ticksToTrack) {
        this(0, ticksToTrack);
    }

    public AveragingCounter(long defaultValue, int ticksToTrack) {
        this.defaultValue = defaultValue;
        values = new long[ticksToTrack];
        Arrays.fill(values, defaultValue);
    }

    private void update(World world) {
        if (world == null) return;
        long currentWorldTime = world.getTime();
        if (currentWorldTime != lastUpdatedWorldTime) {
            long dif = currentWorldTime - lastUpdatedWorldTime;
            if (dif >= values.length || dif < 0) {
                Arrays.fill(values, defaultValue);
                currentIndex = 0;
            } else {
                currentIndex += (int) dif;
                if (currentIndex > values.length - 1)
                    currentIndex -= values.length;
                int offsetIndex;
                for (int index = 0, valuesSize = values.length; index < dif; index++) {
                    offsetIndex = index + currentIndex;
                    if (offsetIndex >= valuesSize)
                        offsetIndex -= valuesSize;
                    values[offsetIndex] = defaultValue;
                }
            }
            lastUpdatedWorldTime = currentWorldTime;
            lastAverage = Arrays.stream(values).sum() / (double) (values.length);
        }
    }

    public long getLast(World world) {
        update(world);
        return values[currentIndex];
    }

    public double getAverage(World world) {
        update(world);
        return lastAverage;
    }

    public void increment(World world, long value) {
        update(world);
        values[currentIndex] += value;
    }

    public void set(World world, long value) {
        update(world);
        values[currentIndex] = value;
    }
}
