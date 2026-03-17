//package com.Game;
//
//import java.io.*;
//
//public class SavedGame implements Serializable {
//    private Level level;
//    private int score;
//    private int lives;
//    private String difficulty;
//
//    public SavedGame(Level level, int score, int lives, String difficulty) {
//        this.level = level;
//        this.score = score;
//        this.lives = lives;
//        this.difficulty = difficulty;
//    }
//
//    public String getDifficulty() {
//        return difficulty;
//    }
//
//    public void setDifficulty(String difficulty) {
//        this.difficulty = difficulty;
//    }
//
//    public Level getLevel() {
//        return level;
//    }
//
//    public void setLevel(Level level) {
//        this.level = level;
//    }
//
//    public int getLives() {
//        return lives;
//    }
//
//    public void setLives(int lives) {
//        this.lives = lives;
//    }
//
//    public int getScore() {
//        return score;
//    }
//
//    public void setScore(int score) {
//        this.score = score;
//    }
//
//    @Override
//    public byte[] serialize() {
//        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
//            objectOutputStream.writeObject(this);
//            return byteArrayOutputStream.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public Object deserialize(byte[] data) {
//        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
//             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
//            return objectInputStream.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    // Getters and setters
//}
