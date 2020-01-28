package chunk;

import block.Block;
import block.Vertex;
import model.CubeModel;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ChunkMesh {

    private List<Vertex> vertices;
    private List<Float> positionsList;
    private List<Float> uvsList;
    private List<Float> normalsList;

    private float[] positions, normals, uvs;
    private Chunk chunk;

    public ChunkMesh(Chunk chunk) {
        this.chunk = chunk;

        vertices = new ArrayList<>();
        positionsList = new ArrayList<>();
        uvsList = new ArrayList<>();
        normalsList = new ArrayList<>();

        buildMesh();
        populateLists();
    }

    private void buildMesh(){
        for (int i = 0; i < this.chunk.getBlocks().size(); i++) {
            Block blockI = this.chunk.getBlocks().get(i);

            boolean px = false, nx = false, py = false, ny = false, pz = false, nz = false;

            for (int j = 0; j < this.chunk.getBlocks().size(); j++) {
                if(this.chunk.getBlocks().get(j) == blockI)
                    continue;

                Block blockJ = this.chunk.getBlocks().get(j);

                //px
                if((blockI.getOrigin().x + 1) == (blockJ.getOrigin().x) && (blockI.getOrigin().y) == (blockJ.getOrigin().y) && (blockI.getOrigin().z) == (blockJ.getOrigin().z))
                    px = true;

                //nx
                if((blockI.getOrigin().x -1) == (blockJ.getOrigin().x) && (blockI.getOrigin().y) == (blockJ.getOrigin().y) && (blockI.getOrigin().z) == (blockJ.getOrigin().z))
                    nx = true;

                //py
                if((blockI.getOrigin().x) == (blockJ.getOrigin().x) && (blockI.getOrigin().y + 1) == (blockJ.getOrigin().y) && (blockI.getOrigin().z) == (blockJ.getOrigin().z))
                    py = true;

                //ny
                if((blockI.getOrigin().x) == (blockJ.getOrigin().x) && (blockI.getOrigin().y - 1) == (blockJ.getOrigin().y) && (blockI.getOrigin().z) == (blockJ.getOrigin().z))
                    ny = true;

                //pz
                if((blockI.getOrigin().x) == (blockJ.getOrigin().x) && (blockI.getOrigin().y) == (blockJ.getOrigin().y) && (blockI.getOrigin().z + 1) == (blockJ.getOrigin().z))
                    pz = true;

                //nz
                if((blockI.getOrigin().x) == (blockJ.getOrigin().x) && (blockI.getOrigin().y) == (blockJ.getOrigin().y) && (blockI.getOrigin().z - 1) == (blockJ.getOrigin().z))
                    nz = true;
            }

            if(!px){
                for (int j = 0; j < 6; j++) {
                    vertices.add(new Vertex(new Vector3f(CubeModel.PX_POS[j].x + blockI.getOrigin().x, CubeModel.PX_POS[j].y + blockI.getOrigin().y, CubeModel.PX_POS[j].z + blockI.getOrigin().z), new Vector3f(1, 0, 0), CubeModel.UV_PX[(blockI.getType() * 6) + j]));
                }
            }

            if(!py){
                for (int j = 0; j < 6; j++) {
                    vertices.add(new Vertex(new Vector3f(CubeModel.PY_POS[j].x + blockI.getOrigin().x, CubeModel.PY_POS[j].y + blockI.getOrigin().y, CubeModel.PY_POS[j].z + blockI.getOrigin().z), new Vector3f(0, 1, 0), CubeModel.UV_PY[(blockI.getType() * 6) + j]));
                }
            }

            if(!nx){
                for (int j = 0; j < 6; j++) {
                    vertices.add(new Vertex(new Vector3f(CubeModel.NX_POS[j].x + blockI.getOrigin().x, CubeModel.NX_POS[j].y + blockI.getOrigin().y, CubeModel.NX_POS[j].z + blockI.getOrigin().z), new Vector3f(-1, 0, 0), CubeModel.UV_NX[(blockI.getType() * 6) + j]));
                }
            }

            if(!ny){
                for (int j = 0; j < 6; j++) {
                    vertices.add(new Vertex(new Vector3f(CubeModel.NY_POS[j].x + blockI.getOrigin().x, CubeModel.NY_POS[j].y + blockI.getOrigin().y, CubeModel.NY_POS[j].z + blockI.getOrigin().z), new Vector3f(0, -1, 0), CubeModel.UV_NY[(blockI.getType() * 6) + j]));
                }
            }

            if(!pz){
                for (int j = 0; j < 6; j++) {
                    vertices.add(new Vertex(new Vector3f(CubeModel.PZ_POS[j].x + blockI.getOrigin().x, CubeModel.PZ_POS[j].y + blockI.getOrigin().y, CubeModel.PZ_POS[j].z + blockI.getOrigin().z), new Vector3f(0, 0, 1), CubeModel.UV_PZ[(blockI.getType() * 6) + j]));
                }
            }

            if(!nz){
                for (int j = 0; j < 6; j++) {
                    vertices.add(new Vertex(new Vector3f(CubeModel.NZ_POS[j].x + blockI.getOrigin().x, CubeModel.NZ_POS[j].y + blockI.getOrigin().y, CubeModel.NZ_POS[j].z + blockI.getOrigin().z), new Vector3f(0, 0, -1), CubeModel.UV_NZ[(blockI.getType() * 6) + j]));
                }
            }
        }
    }

    private void populateLists(){
        for (int i = 0; i < vertices.size(); i++) {
            positionsList.add(vertices.get(i).getPositions().x);
            positionsList.add(vertices.get(i).getPositions().y);
            positionsList.add(vertices.get(i).getPositions().z);

            uvsList.add(vertices.get(i).getUvs().x);
            uvsList.add(vertices.get(i).getUvs().y);

            normalsList.add(vertices.get(i).getNormals().x);
            normalsList.add(vertices.get(i).getNormals().y);
            normalsList.add(vertices.get(i).getNormals().z);
        }

        positions = new float[positionsList.size()];
        uvs = new float[uvsList.size()];
        normals = new float[normalsList.size()];

        for (int i = 0; i < positions.length; i++) {
            positions[i] = positionsList.get(i);
        }

        for (int i = 0; i < uvs.length; i++) {
            uvs[i] = uvsList.get(i);
        }

        for (int i = 0; i < normals.length; i++) {
            normals[i] = normalsList.get(i);
        }

        positionsList.clear();
        uvsList.clear();
        normalsList.clear();
    }

    public void update(Chunk chunk){
        this.chunk = chunk;

        buildMesh();
        populateLists();
    }

    public float[] getPositions() {
        return positions;
    }

    public void setPositions(float[] position){
        this.positions = position;
    }

    public void setNormals(float[] normal){
        this.normals = normal;
    }

    public void setUV(float[] uv){
        this.uvs = uv;
    }

    public float[] getNormals() {
        return normals;
    }

    public float[] getUvs() {
        return uvs;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
