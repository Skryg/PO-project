package com.poproject.game.etcs;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.poproject.game.Assets;
import com.poproject.game.ProjectGame;
import com.poproject.game.etcs.components.BodyComponent;
import com.poproject.game.etcs.components.CameraComponent;
import com.poproject.game.etcs.components.PlayerComponent;
import com.poproject.game.etcs.components.TextureComponent;

public class ComponentBuilder {
    private GameEngine engine;
    public ComponentBuilder(GameEngine engine){
        this.engine = engine;
    }
    public TextureComponent createTextureComponent(String assetName){
        Texture texture = ProjectGame.getInstance().getAssetManager().get(assetName,Texture.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        textureComponent.region = new TextureRegion(texture);
        return textureComponent;
    }

    public PlayerComponent createPlayerComponent(){
        return engine.createComponent(PlayerComponent.class);
    }

    public BodyComponent createBodyComponent(Body body, Vector2 scale){
        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        bodyComponent.body = body;
        bodyComponent.scale.set(scale);
        return bodyComponent;
    }

    public CameraComponent createCameraComponent(Camera camera){
        CameraComponent cameraComponent = engine.createComponent(CameraComponent.class);
        cameraComponent.camera = camera;
        return cameraComponent;
    }
}
