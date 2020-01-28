package shadows;

import java.util.List;
import java.util.Map;

import entity.Camera;
import entity.DirectionalLight;
import model.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import shaders.ShadowShader;
import terrain.TerrainChunk;


public class ShadowMapMasterRenderer {

	private static final int SHADOW_MAP_SIZE = 2048;

	private ShadowShader shader;
	private ShadowBox shadowBox;
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f lightViewMatrix = new Matrix4f();
	private Matrix4f projectionViewMatrix = new Matrix4f();
	private Matrix4f offset = createOffset();
	private ShadowFrameBuffer shadowFbo;
	private ShadowMapEntityRenderer entityRenderer;
	private ShadowMapTerrainRenderer terrainRenderer;

	public ShadowMapMasterRenderer(Camera camera) {
		shader = new ShadowShader();
		shadowBox = new ShadowBox(lightViewMatrix, camera);
		shadowFbo = new ShadowFrameBuffer(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE);
		entityRenderer = new ShadowMapEntityRenderer(shader, projectionViewMatrix);
		terrainRenderer = new ShadowMapTerrainRenderer(shader, projectionViewMatrix);
	}


	public void render(Map<TexturedModel, List<TerrainChunk>> terrainChunks, DirectionalLight sun) {
		shadowBox.update();
		//Vector3f sunPosition = sun.getPosition();
		Vector3f lightDirection = sun.getDirection();
		prepare(lightDirection, shadowBox);

		//entityRenderer.render(entities);
		terrainRenderer.render(terrainChunks);
		finish();
	}


	public Matrix4f getToShadowMapSpaceMatrix() {
		return Matrix4f.mul(offset, projectionViewMatrix, null);
	}

	public void cleanUp() {
		shader.cleanUp();
		shadowFbo.cleanUp();
	}

	public int getShadowMap() {
		return shadowFbo.getShadowMap();
	}

	protected Matrix4f getLightSpaceTransform() {
		return lightViewMatrix;
	}

	private void prepare(Vector3f lightDirection, ShadowBox box) {
		updateOrthoProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
		updateLightViewMatrix(lightDirection, box.getCenter());
		Matrix4f.mul(projectionMatrix, lightViewMatrix, projectionViewMatrix);
		shadowFbo.bindFrameBuffer();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		shader.startProgram();
	}

	/**
	 * Finish the shadow render pass. Stops the shader and unbinds the shadow
	 * FBO, so everything rendered after this point is rendered to the screen,
	 * rather than to the shadow FBO.
	 */
	private void finish() {
		shader.stopProgram();
		shadowFbo.unbindFrameBuffer();
	}

	/**
	 * Updates the "view" matrix of the light. This creates a view matrix which
	 * will line up the direction of the "view cuboid" with the direction of the
	 * light. The light itself has no position, so the "view" matrix is centered
	 * at the center of the "view cuboid". The created view matrix determines
	 * where and how the "view cuboid" is positioned in the world. The size of
	 * the view cuboid, however, is determined by the projection matrix.
	 * 
	 * @param direction
	 *            - the light direction, and therefore the direction that the
	 *            "view cuboid" should be pointing.
	 * @param center
	 *            - the center of the "view cuboid" in world space.
	 */
	private void updateLightViewMatrix(Vector3f direction, Vector3f center) {
		direction.normalise();
		center.negate();
		lightViewMatrix.setIdentity();
		float pitch = (float) Math.acos(new Vector2f(direction.x, direction.z).length());
		Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), lightViewMatrix, lightViewMatrix);
		float yaw = (float) Math.toDegrees(((float) Math.atan(direction.x / direction.z)));
		yaw = direction.z > 0 ? yaw - 180 : yaw;
		Matrix4f.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0), lightViewMatrix,
				lightViewMatrix);
		Matrix4f.translate(center, lightViewMatrix, lightViewMatrix);
	}

	/**
	 * Creates the orthographic projection matrix. This projection matrix
	 * basically sets the width, length and height of the "view cuboid", based
	 * on the values that were calculated in the {@link ShadowBox} class.
	 * 
	 * @param width
	 *            - shadow box width.
	 * @param height
	 *            - shadow box height.
	 * @param length
	 *            - shadow box length.
	 */
	private void updateOrthoProjectionMatrix(float width, float height, float length) {
		projectionMatrix.setIdentity();
		projectionMatrix.m00 = 2f / width;
		projectionMatrix.m11 = 2f / height;
		projectionMatrix.m22 = -2f / length;
		projectionMatrix.m33 = 1;
	}

	/**
	 * Create the offset for part of the conversion to shadow map space. This
	 * conversion is necessary to convert from one coordinate system to the
	 * coordinate system that we can use to sample to shadow map.
	 * 
	 * @return The offset as a matrix (so that it's easy to apply to other matrices).
	 */
	private static Matrix4f createOffset() {
		Matrix4f offset = new Matrix4f();
		offset.translate(new Vector3f(0.5f, 0.5f, 0.5f));
		offset.scale(new Vector3f(0.5f, 0.5f, 0.5f));
		return offset;
	}
}
