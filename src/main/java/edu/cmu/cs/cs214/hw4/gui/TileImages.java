package edu.cmu.cs.cs214.hw4.gui;

import java.awt.image.BufferedImage;

public class TileImages {
    private TileImage[] images;
    public TileImage[] getImages() {return images;}
    public void setImages(TileImage[] is) {images = is;}

    /**
     * Returns the TileImage whose name is name
     * @param name name of desired TileImage
     * @return TileImage with name name
     */
    TileImage getTileImage(String name) {
        for (TileImage i : images) {
            if (name.equals(i.getName())) {
                return i;
            }
        }
        return null;
    }

}
