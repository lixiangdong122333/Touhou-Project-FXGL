package top.lixiangdong;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import top.lixiangdong.component.HealthBarComponent;
import top.lixiangdong.component.HealthComponent;
import top.lixiangdong.component.MovementComponent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class App extends GameApplication {

    private Entity player;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("FXGL Demo");
        settings.setVersion("1.0");
        settings.setWidth(800);
        settings.setHeight(600);
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                player.translateX(5); // move right
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                player.translateX(-5); // move left
            }
        }, KeyCode.A);
        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                player.translateY(-5); // move up
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                player.translateY(5); // move down
            }
        }, KeyCode.S);
    }

    @Override
    protected void initGame() {
        GameWorld world = getGameWorld();
        world.addEntityFactory(new Factory());
        player = world.spawn("player", 100, 100);
        world.spawn("boss", 200, 200);

        getGameTimer().runAtInterval(this::spawnBullets, Duration.seconds(1.0));
    }

    private void spawnBullets() {
        Entity boss = getGameWorld().getEntitiesByType(EntityType.BOSS).get(0);
        double bossX = boss.getX();
        double bossY = boss.getY();

        // 生成多方向的弹幕
        for (int i = 0; i < 8; i++) {
            double angle = i * 45; // 每个方向间隔45度
            getGameWorld().spawn("bullet", new SpawnData(bossX, bossY).put("speed", 200D).put("angle", angle));
        }
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BOSS) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                player.getComponent(HealthComponent.class).changeHealth(-10);
                System.out.println("Player collided with wall!");
                if (player.getComponent(HealthComponent.class).isDead()) {
                    getGameWorld().removeEntity(player);
                }
            }
        });
    }

    @Override
    protected void initGameVars(java.util.Map<String, Object> vars) {
        vars.put("score", 0);
    }

    public enum EntityType {
        PLAYER, BOSS, BULLET
    }

    public static class Factory implements EntityFactory {
        @Spawns("player")
        public Entity newPlayer(SpawnData data) {
            return FXGL.entityBuilder(data)
                    .type(EntityType.PLAYER)
                    .viewWithBBox(new Rectangle(40, 40, Color.BLUE))
                    .with(new CollidableComponent(true))
                    .with(new HealthComponent(100))
                    .with(new HealthBarComponent())
                    .build();
        }

        @Spawns("boss")
        public Entity newBoss(SpawnData data) {
            return FXGL.entityBuilder(data)
                    .type(EntityType.BOSS)
                    .viewWithBBox(new Rectangle(40, 40, Color.RED))
                    .with(new CollidableComponent(true))
                    .build();

        }

        @Spawns("bullet")
        public Entity newBullet(SpawnData data) {
            double speed = data.get("speed");
            double angle = data.get("angle");
            return FXGL.entityBuilder(data)
                    .type(EntityType.BULLET)
                    .viewWithBBox(new Circle(10, Color.ORANGE))
                    .with(new CollidableComponent(true))
                    .with(new MovementComponent(speed, angle))
                    .build();

        }
    }
}


