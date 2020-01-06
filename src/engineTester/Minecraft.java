package engineTester;

import entity.Camera;
import entity.DirectionalLight;
import entity.Light;
import entity.Player;
import model.Model;
import model.ModelTexture;
import model.TexturedModel;
import objLoader.ModelData;
import objLoader.OBJFileLoader;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.Terrain;

public class Minecraft {

    public static ModelTexture texture;

    public static void main(String[] args) {
        DisplayManager.createDisplay();


        Loader loader = new Loader();

        texture = new ModelTexture(new Loader().loadTexture("textures/DefaultPack"));

        ModelData modelData = OBJFileLoader.loadOBJ("models/steve");
        Model model = loader.loadToVAO(modelData.getVertices(), modelData.getIndices(), modelData.getTextureCoords());
        TexturedModel steve = new TexturedModel(model, new ModelTexture(loader.loadTexture("textures/steveTexture")));

        Player player = new Player(steve, new Vector3f(),0,0,0,1);
        Camera camera = new Camera(new Vector3f(), player);
        DirectionalLight light = new DirectionalLight(new Vector3f(1, 1, 1), new Vector3f(-1, -1, -1), 40);
        MasterRenderer renderer = new MasterRenderer();

        Terrain terrain = new Terrain(renderer, loader, player);
        terrain.buildTerrain();
        terrain.addNewChunks();

        while(!Display.isCloseRequested()){
            camera.move();
            player.logic();

            terrain.processTerrain();
            renderer.processEntity(player);
            renderer.render(camera, light);

            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
