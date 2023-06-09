package com.poproject.game.etcs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.poproject.game.ProjectGame;
import com.poproject.game.etcs.components.TextureComponent;
import com.poproject.game.etcs.components.BodyComponent;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {

    static final float PPM = 32.0f;
    public static final float PIXELS_TO_METRES = 1.0f / PPM;

    private static final Vector2 meterDimensions = new Vector2();
    private static final Vector2 pixelDimensions = new Vector2();

    public static float PixelsToMeters(float pixelValue) {
        return pixelValue * PIXELS_TO_METRES;
    }

    private final SpriteBatch batch; // a reference to our spritebatch
    private final Array<Entity> renderQueue; // an array used to allow sorting of images allowing us to draw images on top of each other
    private final OrthographicCamera cam; // a reference to our camera
    private final OrthogonalTiledMapRenderer mapRenderer;
    // component mappers to get components from entities
    private final ComponentMapper<TextureComponent> textureM;
    private final ComponentMapper<BodyComponent> transformM;

    @SuppressWarnings("unchecked")
    public RenderingSystem(OrthogonalTiledMapRenderer mapRenderer, OrthographicCamera camera) {
        super(Family.all(BodyComponent.class, TextureComponent.class).get(), new ZComparator());
        this.mapRenderer = mapRenderer;
        //creates out componentMappers
        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(BodyComponent.class);
        // create the array for sorting entities
        renderQueue = new Array<Entity>();

        this.batch = ProjectGame.getInstance().getSpriteBatch();

        cam = camera;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        mapRenderer.setView(cam);
        mapRenderer.render();

        // update camera and sprite batch
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();
        batch.begin();

        // loop through each entity in our render queue
        for (Entity entity : renderQueue) {
            TextureComponent tex = textureM.get(entity);
            BodyComponent t = transformM.get(entity);
            if (tex.region == null || !t.body.isActive()) {
                continue;
            }

            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();

            float originX = width / 2f;
            float originY = height / 2f;

            batch.draw(tex.region, t.body.getPosition().x - originX, t.body.getPosition().y - originY, originX, originY, width, height, PixelsToMeters(t.scale.x), PixelsToMeters(t.scale.y), t.body.getAngle());
        }

        batch.end();
        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    private static class ZComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity entityA, Entity entityB) {
            if (entityA.getComponent(TextureComponent.class).priority) return 1;
            if (entityB.getComponent(TextureComponent.class).priority) return -1;

            return (int) Math.signum(ComponentMapper.getFor(BodyComponent.class).get(entityA).positionZ - ComponentMapper.getFor(BodyComponent.class).get(entityB).positionZ);
        }
    }
}
