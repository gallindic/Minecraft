package chunk;

import org.lwjgl.util.vector.Vector3f;
import vegetation.Vegetation;

import java.util.List;

public class VegetationChunk {

    private List<Vegetation> vegetations;
    private Vector3f origin;

    public VegetationChunk(List<Vegetation> vegetations, Vector3f origin) {
        this.vegetations = vegetations;
        this.origin = origin;
    }

    public List<Vegetation> getVegetations() {
        return vegetations;
    }

    public Vector3f getOrigin() {
        return origin;
    }
}
