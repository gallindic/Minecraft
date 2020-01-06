package vegetation;

import org.lwjgl.util.vector.Vector3f;

public class Grass extends Vegetation{
    private Vector3f origin;

    public Grass(Vector3f origin){
        this.origin = origin;
    }

    @Override
    public Vector3f getOrigin() {
        return this.origin;
    }

}
