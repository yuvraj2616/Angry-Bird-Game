package com.Game.Levels;

import com.Game.Birds.BirdData;
import com.Game.Pigs.PigData;
import com.Game.Blocks.BlockData;
import java.io.Serializable;
import java.util.List;

public class LevelData implements Serializable {
    private static final long serialVersionUID = 3L;

    private List<BirdData> birds;
    private List<PigData> pigs;
    private List<BlockData> blocks;
    private int currentBirdIndex;
    private boolean isBirdInFlight;
    private BirdData birdOnCatapultData;
    private boolean[] birdUsed; // New field to track used birds

    public LevelData(List<BirdData> birds, List<PigData> pigs, List<BlockData> blocks,
                     int currentBirdIndex, boolean isBirdInFlight, BirdData birdOnCatapultData,
                     boolean[] birdUsed) {
        this.birds = birds;
        this.pigs = pigs;
        this.blocks = blocks;
        this.currentBirdIndex = currentBirdIndex;
        this.isBirdInFlight = isBirdInFlight;
        this.birdOnCatapultData = birdOnCatapultData;
        this.birdUsed = birdUsed;
    }

    // Getters
    public List<BirdData> getBirds() { return birds; }
    public List<PigData> getPigs() { return pigs; }
    public List<BlockData> getBlocks() { return blocks; }
    public int getCurrentBirdIndex() { return currentBirdIndex; }
    public boolean isBirdInFlight() { return isBirdInFlight; }
    public BirdData getBirdOnCatapultData() { return birdOnCatapultData; }
    public boolean[] getBirdUsed() { return birdUsed; }

    // Setters (if needed)
    public void setBirdUsed(boolean[] birdUsed) {
        this.birdUsed = birdUsed;
    }
}
