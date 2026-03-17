package com.Game;

import com.Game.Birds.Bird;
import com.Game.Blocks.Block;
import com.Game.Pigs.Pig;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

public class MyContactListener implements ContactListener {
    private List<Body> bodiesToDestroy = new ArrayList<>();

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object userDataA = fixtureA.getUserData();
        Object userDataB = fixtureB.getUserData();

        if (userDataA instanceof Pig && (userDataB instanceof Bird)) {
            Pig pig = (Pig) userDataA;
            pig.applyDamage(10);
            System.out.println("Pig hit by Bird! Health reduced to: " + pig.getHealth());
            if (pig.getHealth() <= 0) {
                System.out.println("Pig is being marked for destruction.");
                bodiesToDestroy.add(fixtureA.getBody()); // Mark for destruction
            }
        } else if (userDataB instanceof Pig && (userDataA instanceof Bird)) {
            Pig pig = (Pig) userDataB;
            pig.applyDamage(10);
            System.out.println("Pig hit by Bird! Health reduced to: " + pig.getHealth());
            if (pig.getHealth() <= 0) {
                System.out.println("Pig is being marked for destruction.");
                bodiesToDestroy.add(fixtureB.getBody()); // Mark for destruction
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Handle end of contact
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Handle pre-solve logic
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Handle post-solve logic
    }

    public void destroyBodies(World world) {
        for (Body body : bodiesToDestroy) {
            world.destroyBody(body);  // Destroy the body
        }
        bodiesToDestroy.clear();  // Clear the list after destruction
    }
}
