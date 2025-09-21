// LevelPrototypeDemo.java
import java.util.ArrayList;
import java.util.List;

interface LevelPrototype extends Cloneable {
    LevelPrototype clone();
    void describe();
}

class Terrain {
    String type;
    int width, height;
    Terrain(String type, int width, int height) {
        this.type = type; this.width = width; this.height = height;
    }
    Terrain copy() { return new Terrain(type, width, height); }
    @Override
    public String toString() { return "Terrain[" + type + " " + width + "x" + height + "]"; }
}

class Obstacle {
    String kind;
    int x, y;
    Obstacle(String kind, int x, int y) { this.kind = kind; this.x = x; this.y = y; }
    Obstacle copy() { return new Obstacle(kind, x, y); }
    @Override public String toString() { return kind + "@" + x + "," + y; }
}

class Enemy {
    String type;
    int hp;
    int x, y;
    Enemy(String type, int hp, int x, int y) { this.type = type; this.hp = hp; this.x = x; this.y = y; }
    Enemy copy() { return new Enemy(type, hp, x, y); }
    @Override public String toString() { return type + "(hp=" + hp + ")@" + x + "," + y; }
}

class Level implements LevelPrototype {
    String name;
    Terrain terrain;
    List<Obstacle> obstacles;
    List<Enemy> enemies;

    Level(String name, Terrain terrain) {
        this.name = name;
        this.terrain = terrain;
        this.obstacles = new ArrayList<>();
        this.enemies = new ArrayList<>();
    }

    void addObstacle(Obstacle o) { obstacles.add(o); }
    void addEnemy(Enemy e) { enemies.add(e); }

    @Override
    public Level clone() {
        Level copy = new Level(this.name, this.terrain.copy());
        List<Obstacle> newObs = new ArrayList<>();
        for (Obstacle o : this.obstacles) newObs.add(o.copy());
        copy.obstacles = newObs;
        List<Enemy> newEnemies = new ArrayList<>();
        for (Enemy e : this.enemies) newEnemies.add(e.copy());
        copy.enemies = newEnemies;
        return copy;
    }

    @Override
    public void describe() {
        System.out.println("Level: " + name);
        System.out.println(" " + terrain);
        System.out.println(" Obstacles: " + obstacles);
        System.out.println(" Enemies: " + enemies);
    }
}

// Client / demo
public class LevelPrototypeDemo {
    public static void main(String[] args) {
        // Create base level prototype
        Terrain t = new Terrain("Forest", 2000, 1200);
        Level baseLevel = new Level("Forest Outpost", t);
        baseLevel.addObstacle(new Obstacle("Rock", 100, 200));
        baseLevel.addObstacle(new Obstacle("Tree", 300, 400));
        baseLevel.addEnemy(new Enemy("Goblin", 30, 500, 600));

        // Clone for a variant
        Level variantA = baseLevel.clone();
        variantA.name = "Forest Outpost - Night";
        variantA.addEnemy(new Enemy("Wolf", 50, 150, 220));
        variantA.enemies.get(0).hp = 40; // change the copied Goblin's hp (doesn't affect prototype)

        // Clone another variant and change obstacle position
        Level variantB = baseLevel.clone();
        variantB.name = "Forest Outpost - Challenge";
        variantB.obstacles.get(0).x = 120; // moves rock in variant only

        // Describe all
        System.out.println("Prototype (base level):");
        baseLevel.describe();
        System.out.println("\nVariant A:");
        variantA.describe();
        System.out.println("\nVariant B:");
        variantB.describe();
    }
}
