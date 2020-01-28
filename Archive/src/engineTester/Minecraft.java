package engineTester;

import Inventory.Inventory;
import entity.*;
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

import java.util.ArrayList;
import java.util.List;

public class Minecraft {

    public static ModelTexture texture;
    public static boolean endGame = false;

    public static void startGame() {
        DisplayManager.createDisplay();

        Loader loader = new Loader();

        texture = new ModelTexture(new Loader().loadTexture("textures/DefaultPack"));

        List<Entity> entities = new ArrayList<>();

        ModelData modelData = OBJFileLoader.loadOBJ("models/player");
        Model model = loader.loadToVAO(modelData.getVertices(), modelData.getIndices(), modelData.getTextureCoords());
        TexturedModel steve = new TexturedModel(model, new ModelTexture(loader.loadTexture("textures/playerTexture")));

        Player player = new Player(steve, new Vector3f(),0,0,0,1);
        entities.add(player);

        ModelData zombieModelData = OBJFileLoader.loadOBJ("models/zombie");
        model = loader.loadToVAO(zombieModelData.getVertices(), zombieModelData.getIndices(), zombieModelData.getTextureCoords());
        ModelTexture zombieModelTexture = new ModelTexture(new Loader().loadTexture("textures/zombieTexture"));
        TexturedModel texturedModel = new TexturedModel(model, zombieModelTexture);
        Zombie zombie = new Zombie(player, texturedModel, new Vector3f(), 0, 0, 0, 1);
        entities.add(zombie);

        Camera camera = new Camera(new Vector3f(), player);
        DirectionalLight light = new DirectionalLight(new Vector3f(1, 1, 1), new Vector3f(-1, -1, -1));
        MasterRenderer renderer = new MasterRenderer(camera);

        EntityController entityController = new EntityController(player, zombie, camera);

        Terrain terrain = new Terrain(renderer, loader, player, zombie);
        terrain.buildTerrain();
        terrain.addNewChunks();
        entityController.collisonChecker();

        while(!Display.isCloseRequested() && !endGame){
            entityController.gameLogic();

            terrain.processTerrain();
            for( Entity entity : entities)
                renderer.processEntity(entity);

            renderer.render(camera, light);

            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
