package me.notpseudo.lexiclient.features;

import net.minecraft.util.AxisAlignedBB;

public class PositionalMessage {

    private double x1;

    private double y1;

    private double z1;

    private double x2;

    private double y2;

    private double z2;

    private String message;

    private int timeout;

    private transient long lastSentTime;

    private transient AxisAlignedBB aabb;

    public PositionalMessage() {

    }

    public PositionalMessage(double x1, double y1, double z1, double x2, double y2, double z2, String message, int timeout) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.message = message;
        this.timeout = timeout;
        lastSentTime = 0;
        aabb = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
    }

    public String getMessage() {
        return message;
    }

    public int getTimeout() {
        return timeout;
    }

    public long getLastSentTime() {
        return lastSentTime;
    }

    public void setLastSentTime(long lastSentTime) {
        this.lastSentTime = lastSentTime;
    }

    public AxisAlignedBB getArea() {
        if (aabb == null) {
            aabb = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
        }
        return aabb;
    }

}
