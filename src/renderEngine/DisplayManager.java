package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final int FPS_CAP = 120;

    private static long lastFrameTime;
    private static float delta;
    /** frames per second */
    private static int fps;
    /** last fps time */
    private static long lastFPS;

    public static void createDisplay(){
        ContextAttribs attribs = new ContextAttribs(3,2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("Minecraft");
            lastFPS = getTime();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0,0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }

    /**
     * Get the accurate system time
     *
     * @return The system time in milliseconds
     */
    private static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    /**
     * Calculate the FPS and set it in the title bar
     */
    private static void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            System.out.println(fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    public static void updateDisplay(){

        updateFPS();
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds(){
        return delta;
    }

    public static void closeDisplay(){

        Display.destroy();

    }

    private static long getCurrentTime(){
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }
}
