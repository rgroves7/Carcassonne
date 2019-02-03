package edu.cmu.cs.cs214.hw4.gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class representing the image on a tile
 */
public class TileImage {
    private String name;
    public String getName() {return name;}
    public void setName(String n) {name = n;}

    private int x;
    public int getX() {return x;}
    public void setX(int X) {x = X;}

    private int y;
    public int getY() {return y;}
    public void setY(int Y) {y = Y;}

    private String IMAGE_FILENAME = "src/main/resources/Carcassonne.png";
    private BufferedImage image;

    {
        try {
            image = ImageIO.read(new File(IMAGE_FILENAME));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when reading " + IMAGE_FILENAME + "!");
        }
    }

    private BufferedImage tileImage;

    TileImage copyTileImage() {
        TileImage t = new TileImage();
        t.setName(name);
        t.setX(x);
        t.setY(y);
        t.tileImage = image.getSubimage(x, y, 90, 90);
        return t;
    }

    BufferedImage getImage() {return tileImage;}

    /**
     * Rotates the image and sets tileImage equal to the rotated image
     * @return the rotated image
     */
    BufferedImage rotateImage() {
        int weight = tileImage.getWidth();
        int height = tileImage.getHeight();

        AffineTransform at = AffineTransform.getQuadrantRotateInstance(1, weight / 2.0, height / 2.0);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage dest = new BufferedImage(weight, height, tileImage.getType());
        op.filter(tileImage, dest);
        tileImage = dest;
        return tileImage;
    }

    /**
     * Returns the tileImage with a circle added to it at location tileX, tileY
     * in color c
     * @param tileX the x coordinate of the circle out of 3
     * @param tileY the y coordinate of the circle out of 3
     * @param c the color of the circle
     * @return
     */
    BufferedImage addCircle(int tileX, int tileY, Color c) {
        BufferedImage addCircle = new BufferedImage(90, 90,
                tileImage.getType());
        Graphics2D g = (Graphics2D) addCircle.getGraphics();
        g.drawImage(tileImage, 0, 0, null);
        g.setColor(c);
        int x = 15 + ((30) * tileX);
        int y = 15 + ((30) * tileY);
        g.fillOval(x - 10, y - 10, 20, 20);
        g.dispose();
        return addCircle;
    }


}
