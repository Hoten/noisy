package hoten.perlin;

import hoten.perlin.interpolator.CosineInterpolator;
import hoten.perlin.interpolator.Interpolator;
import hoten.perlin.interpolator.LinearInterpolator;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Perlin1dTest.java
 *
 * @author Hoten
 */
public class Perlin1dTest {

    public Perlin1dTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test if the tiled noise generated is interpolated smoothly at the edges.
     * A difference of less than or equal to 0.1 is considered good enough. Uses
     * reasonable values for persistence and octaves.
     */
    @Test
    public void testCreateTiledArray() {
        System.out.println("createTiledArray");
        CosineInterpolator cosInterp = new CosineInterpolator();
        LinearInterpolator linInterp = new LinearInterpolator();
        for (int i = 0; i < 100; i++) {
            int size = 500;
            double p = Math.random() * .25 + .25;
            int octaves = 1 + (int) (Math.random() * 10);
            Interpolator interp = Math.random() > .5 ? cosInterp : linInterp;
            Perlin1d instance = new Perlin1d(p, octaves, (int) (Math.random() * Integer.MAX_VALUE), interp);
            double[] result = instance.createTiledArray(size);
            assertEquals(result[0], result[result.length - 1], 0.1);
        }
    }

    /**
     * Test if the noise generated is interpolated smoothly. A difference of
     * less than or equal to 0.1 between each consecutive data point is
     * considered good enough. Uses reasonable values for persistence and
     * octaves.
     */
    @Test
    public void testCreateArray() {
        System.out.println("createArray");
        CosineInterpolator cosInterp = new CosineInterpolator();
        LinearInterpolator linInterp = new LinearInterpolator();
        for (int i = 0; i < 100; i++) {
            int size = 500;
            double p = Math.random() * .25 + .25;
            int octaves = 1 + (int) (Math.random() * 100);
            Interpolator interp = Math.random() > .5 ? cosInterp : linInterp;
            Perlin1d instance = new Perlin1d(p, octaves, (int) (Math.random() * Integer.MAX_VALUE), interp);
            double[] result = instance.createArray(size);
            for (int j = 1; j < size; j++) {
                assertEquals(result[j], result[j - 1], 0.15);
            }
        }
    }
}
