package com.poproject.game.etcs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.poproject.game.etcs.GameEngine;
import com.poproject.game.etcs.components.BodyComponent;
import com.poproject.game.etcs.components.PlayerComponent;
import com.poproject.game.etcs.components.PlayerWeaponComponent;

public class PlayerAttackSystem extends IteratingSystem {
    private final Camera camera;
    private final GameEngine gameEngine;

    public PlayerAttackSystem(Camera camera, GameEngine gameEngine) {
        super(Family.all(PlayerComponent.class, PlayerWeaponComponent.class).get());
        this.camera = camera;
        this.gameEngine = gameEngine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerWeaponComponent pwc = GameEngine.playerWeaponComponentMapper.get(entity);
        if (pwc == null || !pwc.readyToAttack(deltaTime)) return;
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) return;

        attack(pwc, entity.getComponent(BodyComponent.class).body.getPosition());
    }

    private void attack(PlayerWeaponComponent pwc, Vector2 playerPosition) {
        if (pwc.isProjectile()) {
            //spawn projectile, with parameters based on mouse world position
            gameEngine.entityBuilder.createProjectileEntity(worldMousePosition().x, worldMousePosition().y, playerPosition);
            pwc.fire();
        }
    }

    private Vector2 worldMousePosition() {
        Vector3 ans = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        return new Vector2(ans.x, ans.y);
    }
}
