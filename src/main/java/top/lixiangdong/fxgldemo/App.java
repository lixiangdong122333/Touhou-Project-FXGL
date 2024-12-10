package top.lixiangdong.fxgldemo;

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
import top.lixiangdong.fxgldemo.component.FireComponent;
import top.lixiangdong.fxgldemo.component.HealthBarComponent;
import top.lixiangdong.fxgldemo.component.HealthComponent;
import top.lixiangdong.fxgldemo.component.MovementComponent;

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
        int width = getSettings().getWidth();
        int height = getSettings().getHeight();


        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                double playerWidth = player.getBoundingBoxComponent().getWidth();
                double newX = player.getX() + 5;
                if (newX + playerWidth / 2 <= width) {
                    player.translateX(5); // move right
                }
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                double playerWidth = player.getBoundingBoxComponent().getWidth();
                double newX = player.getX() - 5;
                if (newX - playerWidth / 2 >= 0) {
                    player.translateX(-5); // move left
                }
            }
        }, KeyCode.A);
        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                double playerHeight = player.getBoundingBoxComponent().getHeight();
                double newY = player.getY() - 5;
                if (newY - playerHeight / 2 >= 0) {
                    player.translateY(-5); // move up
                }
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                double playerHeight = player.getBoundingBoxComponent().getHeight();
                double newY = player.getY() + 5;
                if (newY + playerHeight / 2 <= height) {
                    player.translateY(5); // move down
                }
            }
        }, KeyCode.S);
    }

    @Override
    protected void initGame() {
        GameWorld world = getGameWorld();
        world.addEntityFactory(new Factory());
        int height = getSettings().getHeight();
        int width = getSettings().getWidth();
        player = world.spawn("player", width * 0.5, height * 0.75);

    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BULLET) {
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
        PLAYER, BULLET
    }

    public static class Factory implements EntityFactory {
        @Spawns("player")
        public Entity newPlayer(SpawnData data) {
            Rectangle playerView = new Rectangle(40, 40, Color.BLUE);
            playerView.setTranslateX(-playerView.getWidth() / 2);
            playerView.setTranslateY(-playerView.getHeight() / 2);
            return FXGL.entityBuilder(data)
                    .type(EntityType.PLAYER)
                    .viewWithBBox(playerView)
                    .with(new CollidableComponent(true))
                    .with(new HealthComponent(100))
                    .with(new HealthBarComponent())
                    .with(new FireComponent(700D,270D))
                    .build();
        }

        @Spawns("bullet")
        public Entity newBullet(SpawnData data) {
            Circle playerView = new Circle(10, Color.ORANGE);
            playerView.setTranslateX(-playerView.getRadius() / 2);
            playerView.setTranslateY(-playerView.getRadius() / 2);

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


