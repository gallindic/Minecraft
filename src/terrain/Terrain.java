package terrain;

import block.Block;
import chunk.Chunk;
import chunk.ChunkMesh;
import chunk.VegetationChunk;
import engineTester.Minecraft;
import entity.Camera;
import entity.Player;
import model.Model;
import model.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import utils.PerlinNoiseGenerator;
import vegetation.*;

import java.util.*;

public class Terrain {
    public static final int WORLD_SIZE = 5 * 32;

    public static int index = 0;
    private static int vegetationIndex = 0;

    private PerlinNoiseGenerator perlinNoiseGenerator;
    private Player player;
    private MasterRenderer renderer;
    private Loader loader;
    private Random random = new Random();


    static List<Vector3f> used = Collections.synchronizedList(new ArrayList<>());
    static List<Vector3f> usedTreePositions = new ArrayList<>();
    static List<TerrainChunk> terrainChunks = new ArrayList<>();
    static List<ChunkMesh> chunks = Collections.synchronizedList(new ArrayList<>());

    static List<VegetationChunk> vegetationChunks = new ArrayList<>();
    static List<VegetationMesh> vegetationMeshes = new ArrayList<>();

    static Map<String, Float> heights = Collections.synchronizedMap(new HashMap<>());

    public Terrain(MasterRenderer renderer, Loader loader, Player player) {
        this.perlinNoiseGenerator = new PerlinNoiseGenerator();
        this.renderer = renderer;
        this.player = player;
        this.loader = loader;
    }

    private void treeBlocks(List<Block> blocks, Vector3f position){
        for (int k = 0; k < 10; k++) {
            if(k == 9){
                blocks.add(new Block(new Vector3f(position.x, position.y + k, position.z), Block.TREELEAF));
            }

            if(k < 9)
                blocks.add(new Block(new Vector3f(position.x, position.y + k, position.z), Block.TREEBARK));

            if(k >= 6){
                blocks.add(new Block(new Vector3f(position.x + 1, position.y + k, position.z), Block.TREELEAF));
                blocks.add(new Block(new Vector3f(position.x + 1, position.y + k, position.z + 1), Block.TREELEAF));
                blocks.add(new Block(new Vector3f(position.x, position.y + k, position.z + 1), Block.TREELEAF));
                blocks.add(new Block(new Vector3f(position.x - 1, position.y + k, position.z + 1), Block.TREELEAF));
                blocks.add(new Block(new Vector3f(position.x - 1, position.y + k, position.z), Block.TREELEAF));
                blocks.add(new Block(new Vector3f(position.x - 1, position.y + k, position.z - 1), Block.TREELEAF));
                blocks.add(new Block(new Vector3f(position.x, position.y + k, position.z - 1), Block.TREELEAF));
                blocks.add(new Block(new Vector3f(position.x + 1, position.y + k, position.z - 1), Block.TREELEAF));
            }
        }
    }

    private void generateTerrain(){
        for (int x = (int) (Camera.camPos.x - WORLD_SIZE) / 32; x < (Camera.camPos.x + WORLD_SIZE) / 32; x++) {
            for (int z = (int) (Camera.camPos.z - WORLD_SIZE) / 32; z < (Camera.camPos.z + WORLD_SIZE) / 32; z++) {
                if (!used.contains(new Vector3f(x * 32, 0, z * 32))) {
                    List<Block> blocks = new ArrayList<>();
                    int terrainChunkLimit = 3;
                    int trees = 0;
                    for (int i = 0; i < 32; i++) {
                        for (int j = 0; j < 32; j++) {
                            int y = (int) perlinNoiseGenerator.generateHeight(i + (x * 32), j + (z * 32)) + 20;
                            int blockType = Block.GRASS;
                            Vector3f position = new Vector3f(i, y, j);

                            if(y <= -2) {
                                blockType = Block.SAND;
                            }

                            blocks.add(new Block(position, blockType));
                            addHeightToMap(position);

                            if(Math.random() > 0.99 && trees < terrainChunkLimit && blockType == Block.GRASS){
                                treeBlocks(blocks, position);
                                trees++;
                            }
                        }
                    }

                    Chunk chunk = new Chunk(blocks, new Vector3f(x * 32, 0, z * 32));
                    ChunkMesh chunkMesh = new ChunkMesh(chunk);

                    chunks.add(chunkMesh);
                    used.add(new Vector3f(x * 32, 0, z * 32));
                }
            }
        }
    }

    public void buildTerrain(){
        generateTerrain();
        setCameraOrigin();
    }

    private void setCameraOrigin(){
        for (int i = 0; i < chunks.get(55).getChunk().getBlocks().size(); i++) {
            Block block = chunks.get(55).getChunk().getBlocks().get(i);
            if(block.getOrigin().x == 0.0f && block.getOrigin().z == 0.0f) {
                Vector3f pos = block.getOrigin();
                pos.y += 0.5;
                player.setPlayerOrigin(pos);
                break;
            }
        }
    }

    private void buildVegetationChunk(ChunkMesh chunk){
        List<Vegetation> vegetations = new ArrayList<>();

        for (int i = 0; i < chunk.getChunk().getBlocks().size(); i++) {
            Block block = chunk.getChunk().getBlocks().get(i);

            if(block.getType() == Block.GRASS) {
                Vector3f pos = new Vector3f();

                if (Math.random() > 0.98) {
                    Vector3f.add(block.getOrigin(), chunk.getChunk().getOrigin(), pos);
                    pos.y++;

                    vegetations.add(new Grass(pos));
                }
            }
        }

        vegetationChunks.add(new VegetationChunk(vegetations, chunk.getChunk().getOrigin()));
    }

    public void processTerrain(){
        if(index < chunks.size()){
            buildVegetationChunk(chunks.get(index));
            createVegetationInstances(vegetationChunks.get(index));

            Model model = loader.loadToVAO(chunks.get(index).getPositions(), chunks.get(index).getUvs());
            TexturedModel texturedModel = new TexturedModel(model, Minecraft.texture);
            terrainChunks.add(new TerrainChunk(texturedModel, chunks.get(index).getChunk().getOrigin()));

            chunks.get(index).setPositions(null);
            chunks.get(index).setNormals(null);
            chunks.get(index).setUV(null);
            index++;
        }

        for(int i = 0; i < terrainChunks.size(); i++) {
            Vector3f origin = terrainChunks.get(i).getPosition();

            int distX = (int) Math.abs(Camera.camPos.x - origin.x);
            int distZ = (int) Math.abs(Camera.camPos.z - origin.z);

            if(distX <= WORLD_SIZE && distZ <= WORLD_SIZE){
                renderer.processTerrain(terrainChunks.get(i));
                renderer.processVegetation(vegetationMeshes.get(i));
            }
        }
    }

    private void createVegetationInstances(VegetationChunk vegetationChunk) {
        Iterator<Vegetation> vegetationIterator = vegetationChunk.getVegetations().iterator();
        int index = 0, instanceCount = vegetationChunk.getVegetations().size();
        float[] modelPositions = new float[instanceCount * 3];

        while(vegetationIterator.hasNext()){
            Vector3f pos = vegetationIterator.next().getOrigin();

            modelPositions[index++] = pos.x;
            modelPositions[index++] = pos.y;
            modelPositions[index++] = pos.z;
        }

        Model model = loader.loadToVAO(Vegetation.vertices, Vegetation.indices, Vegetation.uvs, modelPositions);
        TexturedModel texturedModel = new TexturedModel(model, Minecraft.texture);

        vegetationMeshes.add(new VegetationMesh(texturedModel, vegetationChunk.getOrigin(), instanceCount));
    }

    public static float getCurrentPositionHeight(float x, float z){
        for (String position : heights.keySet()){
            if(position.equals((x + " " + z)))
                return heights.get(position);
        }

        return -100;
    }

    private void addHeightToMap(Vector3f position){
        heights.put(position.x + " " + position.z, position.y + 0.5f);
    }

    public void addNewChunks(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Display.isCloseRequested()){
                    generateTerrain();
                }
            }
        }).start();
    }
}
