package engineTester;

import entity.Camera;
import entity.Player;
import model.Model;
import model.ModelTexture;
import model.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import model.OBJLoader;
import terrain.Terrain;

public class Minecraft {

    public static ModelTexture texture;

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        MasterRenderer renderer = new MasterRenderer();
        Loader loader = new Loader();

        texture = new ModelTexture(new Loader().loadTexture("textures/DefaultPack"));
        Model model = OBJLoader.loadObjModel("models/steve", loader);
        TexturedModel steve = new TexturedModel(model, new ModelTexture(loader.loadTexture("textures/steveTexture")));

        Player player = new Player(steve, new Vector3f(),0,0,0,1);
        Camera camera = new Camera(new Vector3f(), player);

        Terrain terrain = new Terrain(renderer, loader, player);
        terrain.buildTerrain();
        terrain.addNewChunks();

        while(!Display.isCloseRequested()){
            camera.move();
            player.logic();

            terrain.processTerrain();
            renderer.processEntity(player);
            renderer.render(camera);

            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
