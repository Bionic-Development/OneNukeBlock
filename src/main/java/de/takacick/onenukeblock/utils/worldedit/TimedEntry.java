package de.takacick.onenukeblock.utils.worldedit;

public class TimedEntry<V> {

    private final V value;
    private long timestamp;

    public TimedEntry(V value) {
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public V get() {
        this.timestamp = System.currentTimeMillis();
        return this.value;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean isExpired(Long currentTime) {
        return this.timestamp - currentTime > 100000;
    }
}
