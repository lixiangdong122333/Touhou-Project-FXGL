package top.lixiangdong.component;

import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

public class FireComponent extends Component {
    private double speed;
    private double angle;

    public FireComponent(double speed, double angle) {
        this.speed = speed;
        this.angle = angle;
    }

    @Override
    public void onAdded() {
        getGameTimer().runAtInterval(this::spawnBullets, Duration.seconds(0.2));
    }

    private void spawnBullets() {
        getGameWorld().spawn("bullet", new SpawnData(getEntity().getX(), getEntity().getY() - getEntity().getHeight()).put("speed", speed).put("angle", angle));
    }

}
