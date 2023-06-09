package com.poproject.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.poproject.game.map.CollisionArea;

import static com.poproject.game.ProjectGame.*;

public class BodyFactory {
    public World world;
    private static BodyFactory instance;
    private static BodyDef bodyDef;
    private static FixtureDef fixtureDef;

    private BodyFactory() {
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
    }

    public static void setWorld(World world) {
        getInstance().world = world;
    }

    public static BodyFactory getInstance() {
        if (instance == null) instance = new BodyFactory();
        return instance;
    }

    public Body createProjectileBody(Vector2 startPos) {
        final float projectileRadius = 0.2f;

        resetBodyAndFixtureDef();
        bodyDef.position.set(startPos);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = false;
        bodyDef.angularDamping = 0f;
        Body projectileBody = world.createBody(bodyDef);

        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_ENEMY;

        final CircleShape circleShape = new CircleShape();
        circleShape.setRadius(projectileRadius);

        fixtureDef.shape = circleShape;
        projectileBody.createFixture(fixtureDef);
        circleShape.dispose();

        return projectileBody;
    }

    public Body makeEntityBody(Vector2 startPosition, short categoryBits, short maskBits) {
        resetBodyAndFixtureDef();
        bodyDef.position.set(startPosition);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        Body playerBody = world.createBody(bodyDef);
        playerBody.setUserData("PLAYER");

        fixtureDef.density = 1;
        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.69f;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = maskBits;

        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(0.5f, 0.5f);
        fixtureDef.shape = pShape;
        playerBody.createFixture(fixtureDef);
        pShape.dispose();
        return playerBody;
    }

    public Body makeTowerBody(Vector2 position) {
        return makeEntityBody(position, (short) 0, (short) 0);
    }

    public Body makePlayerBody(Vector2 playerStartPosition) {
        return makeEntityBody(playerStartPosition, BIT_PLAYER, (short) (BIT_GROUND | BIT_ENEMY));
    }

    public Body makeEnemyBody(Vector2 startPos) {
        return makeEntityBody(startPos, BIT_ENEMY, (short) (BIT_GROUND | BIT_PLAYER));
    }

    private void createCollisionArea(CollisionArea ca) {
        resetBodyAndFixtureDef();
        bodyDef.position.set(ca.getStartX(), ca.getStartY());
        bodyDef.gravityScale = 0;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        final Body body = world.createBody(bodyDef);

        fixtureDef.filter.categoryBits = BIT_GROUND;
        fixtureDef.filter.maskBits = -1;
        final ChainShape chainShape = new ChainShape();
        chainShape.createChain(ca.getVertices());
        fixtureDef.shape = chainShape;
        body.createFixture(fixtureDef);

        chainShape.dispose();
    }

    public void createCollisionAreas(Array<CollisionArea> collisionAreas) {
        for (CollisionArea ca : collisionAreas) createCollisionArea(ca);
    }

    private void resetBodyAndFixtureDef() {
        bodyDef.gravityScale = 1;
        bodyDef.position.set(0, 0);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.angularDamping = 0;

        fixtureDef.density = 1;
        fixtureDef.filter.maskBits = 0;
        fixtureDef.filter.categoryBits = 0;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.isSensor = false;
        fixtureDef.shape = null;
    }
}
