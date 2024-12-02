package top.lixiangdong.component;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

public class MovementComponent extends Component {

    private double speed;
    private double angle;

    public MovementComponent(double speed, double angle) {
        this.speed = speed;
        this.angle = angle;
    }

    @Override
    public void onUpdate(double tpf) {
        // 计算移动的 dx 和 dy
        double dx = speed * tpf * FXGLMath.cos(FXGLMath.toRadians(angle));
        double dy = speed * tpf * FXGLMath.sin(FXGLMath.toRadians(angle));

        // 更新实体的位置
        entity.translateX(dx);
        entity.translateY(dy);

        // 检查实体是否超出屏幕范围
        if (entity.getX() < 0 || entity.getX() > getGameScene().getWidth() || entity.getY() < 0 || entity.getY() > getGameScene().getHeight()) {
            getGameWorld().removeEntity(entity); // 移除超出屏幕范围的实体
        }
    }
}
