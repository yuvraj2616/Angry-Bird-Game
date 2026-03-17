package com.Game.Birds;

import java.io.Serializable;

public class BirdData implements Serializable {
    private static final long serialVersionUID = 2L; // Updated serialVersionUID to reflect changes

    private String type;
    private float x, y;
    private boolean used;
    private float velocityX, velocityY;
    private String bodyType;
    private float scale; // Added scale field
    private boolean fixedRotation; // Added fixedRotation field
    private float gravityScale; // Added gravityScale field

    public BirdData(String type, float x, float y, boolean used,
                    float velocityX, float velocityY, String bodyType,
                    float scale, boolean fixedRotation, float gravityScale) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.used = used;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.bodyType = bodyType;
        this.scale = scale;
        this.fixedRotation = fixedRotation;
        this.gravityScale = gravityScale;
    }

    // Getters
    public String getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isUsed() {
        return used;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public String getBodyType() {
        return bodyType;
    }

    public float getScale() {
        return scale;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public float getGravityScale() {
        return gravityScale;
    }

    // Setters
    public void setType(String type) {
        this.type = type;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
    }

    // Optionally, override toString() for better logging and debugging
    @Override
    public String toString() {
        return "BirdData{" +
            "type='" + type + '\'' +
            ", x=" + x +
            ", y=" + y +
            ", used=" + used +
            ", velocityX=" + velocityX +
            ", velocityY=" + velocityY +
            ", bodyType='" + bodyType + '\'' +
            ", scale=" + scale +
            ", fixedRotation=" + fixedRotation +
            ", gravityScale=" + gravityScale +
            '}';
    }
}
