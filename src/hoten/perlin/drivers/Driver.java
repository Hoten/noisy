package hoten.perlin.drivers;

import com.kroo.gif.GifSequenceWriter;
import hoten.perlin.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Driver.java
 *
 * @author Hoten
 */
public class Driver {

    public static void main(String[] args) {
        drawTiled1d();
        drawTiled2d();
        drawTiled1dAnimated();
    }

    public static void drawTiled1d() {
        int displayWidth = 800;
        int displayHeight = 600;
        int displayBufferHeight = 20;
        int size = 5000;
        double p = .5;
        int seed = (int) (Integer.MAX_VALUE * Math.random());
        int n = 5;
        Perlin1d pn = new Perlin1d(p, n, seed);
        double[] y = pn.createTiledArray(size);

        //draw
        final BufferedImage result = new BufferedImage(displayWidth, displayHeight + displayBufferHeight, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics rg = result.getGraphics();
        rg.setColor(Color.red);
        for (int i = 0; i < size; i++) {
            rg.fillOval((int) (i * 1.0 * displayWidth / size), displayBufferHeight / 2 + (int) (y[i] * displayHeight), 2, 2);
        }
        
        //save as .png
        Driver.savePng(result, "1d tiled - " + seed + ".png");

        //display
        final JPanel panel = new JPanel() {
            public int count;

            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(result, 0, 0, null);
            }
        };
        panel.setPreferredSize(new Dimension(displayWidth, displayHeight + displayBufferHeight));
        Driver.display(panel);        
    }
    
    private static void drawTiled1dAnimated() {
        final int size = 1000;
        final int scale = 400;
        final int frames = 75;
        double p = 1.0 / 3;
        int seed = (int) (Integer.MAX_VALUE * Math.random());
        int n = 6;
        Perlin2d pn = new Perlin2d(p, n, seed);
        final double[][] y = pn.createTiledArray(size, frames);
        
        final BufferedImage[] images = new BufferedImage[frames];
        for (int i = 0; i < frames; i++) {
            BufferedImage b = new BufferedImage(scale, scale, BufferedImage.TYPE_INT_RGB);
            Graphics bg = b.getGraphics();
            bg.setColor(Color.red);
            for (int j = 0; j < size; j++) {
                bg.fillOval((int) (j * 1.0 * scale / size), (int) (y[j][i] * scale), 2, 2);
            }
            images[i] = b;
        }
        
        //save as .gif
        Driver.saveGif(images, "1d tiled animation - " + seed + ".gif");

        //display
        final JPanel panel = new JPanel() {
            public int count;
            
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(images[count / 10], 0, 0, null);
                count = (count + 1) % (frames * 10);
            }
        };
        panel.setPreferredSize(new Dimension(scale, scale));
        Driver.display(panel);        
    }
    
    private static void drawTiled2d() {
        int width = 800;
        int height = 600;
        double p = 1.0 / 2;
        int seed = (int) (Integer.MAX_VALUE * Math.random());
        int n = 4;
        Perlin2d pn = new Perlin2d(p, n, seed);
        double[][] y = pn.createTiledArray(width, height);

        //draw
        final BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int v = (int) (255 * .5 * y[i][j]) + 255 / 2;
                //result.setRGB(i, j, new Color(v, v, v).getRGB());
                result.setRGB(i, j, new Color(255 - v, 255 - v, v).getRGB());
            }
        }
        
        //save as .png
        Driver.savePng(result, "2d tiled - " + seed + ".png");

        //display
        final JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(result, 0, 0, null);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));
        Driver.display(panel);        
    }

    public static void display(JPanel panel) {
        final JFrame frame = new JFrame();
        frame.add(panel);
        frame.setVisible(true);
        Dimension panelDim = panel.getPreferredSize();
        frame.setSize(new Dimension(panelDim.width + frame.getInsets().left + frame.getInsets().right, panelDim.height + frame.getInsets().top + frame.getInsets().bottom));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        new Thread("Draw loop") {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Perlin2d.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    frame.repaint();
                }
            }
        }.start();
    }

    public static void savePng(BufferedImage image, String fname) {
        new File("output images/").mkdir();
        try {
            ImageIO.write(image, "png", new File("output images/" + fname));
        } catch (IOException ex) {
            Logger.getLogger(Perlin2d.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveGif(BufferedImage[] images, String fname) {
        new File("output images/").mkdir();
        try {
            new File("output images").mkdir();
            ImageOutputStream output = new FileImageOutputStream(new File("output images/" + fname));
            GifSequenceWriter writer = new GifSequenceWriter(output, images[0].getType(), 100, true);
            for (BufferedImage bi : images) {
                writer.writeToSequence(bi);
            }
            writer.close();
            output.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Perlin2d.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Perlin2d.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
