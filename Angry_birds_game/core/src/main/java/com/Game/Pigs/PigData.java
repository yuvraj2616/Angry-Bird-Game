package com.Game.Pigs;

import java.io.Serializable;

public class PigData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private float x, y;
    private int health;
    private float velocityX, velocityY;
    private String bodyType;

    public PigData(String type, float x, float y, int health, float velocityX, float velocityY, String bodyType) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.health = health;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.bodyType = bodyType;
    }

    public String getType() { return type; }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getHealth() { return health; }
    public float getVelocityX() { return velocityX; }
    public float getVelocityY() { return velocityY; }
    public String getBodyType() { return bodyType; }


    public float getGravityScale() {
        return 1.0f; // default gravity scale
    }

    public boolean isFixedRotation() {
        return false; // default fixed rotation
    }

}
