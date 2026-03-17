package com.Game.Blocks;

import java.io.Serializable;

public class BlockData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private float x, y;

    public BlockData(String type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    // Getters
    public String getType() { return type; }
    public float getX() { return x; }
    public float getY() { return y; }

    public void setType(String type) {
        this.type = type;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getGravityScale() {
        return 1.0f;
    }

    public float getVelocityX() {
        return 0;
    }

    public boolean isFixedRotation() {
        return true;
    }

    public float getVelocityY() {
        return 0;
    }

    public float getScale() {
        return 1.0f;
    }
}
