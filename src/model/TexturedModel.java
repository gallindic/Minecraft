package model;



public class TexturedModel {

    private Model model;
    private ModelTexture modelTexture;

    public TexturedModel(Model model, ModelTexture modelTexture) {
        this.model = model;
        this.modelTexture = modelTexture;
    }

    public Model getModel() {
        return model;
    }

    public ModelTexture getModelTexture() {
        return modelTexture;
    }
}
