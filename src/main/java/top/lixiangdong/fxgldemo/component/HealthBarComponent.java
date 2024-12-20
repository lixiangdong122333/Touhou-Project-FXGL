package top.lixiangdong.fxgldemo.component;

import com.almasb.fxgl.entity.component.Component;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBarComponent extends Component {
    private Rectangle healthBar;
    private HealthComponent healthComponent;

    @Override
    public void onAdded() {
        healthComponent = entity.getComponent(HealthComponent.class);

        // 创建血条的图形
        Rectangle background = new Rectangle(100, 10, Color.BLACK);
        //居中
        background.setTranslateX(-background.getWidth() / 2);
        background.setTranslateY(-background.getHeight() / 2);

        healthBar = new Rectangle(100, 10, Color.GREEN);
        //居中
        healthBar.setTranslateX(-healthBar.getWidth() / 2);
        healthBar.setTranslateY(-healthBar.getHeight() / 2);

        // 将血条和背景放置在一个 Group 中
        Group healthBarGroup = new Group(background, healthBar);

        // 将血条 Group 添加到玩家实体的视图中
        entity.getViewComponent().addChild(healthBarGroup);

        // 将血条 Group 设置在玩家实体的上方
        healthBarGroup.setLayoutY(-((entity.getViewComponent().getEntity().getHeight() / 2) + 10) ); // 调整 Y 坐标，使其位于玩家实体的上方
    }

    @Override
    public void onUpdate(double tpf) {
        double maxHealth = healthComponent.getMaxHealth();
        double health = healthComponent.getHealth();
        healthBar.setWidth(health / maxHealth * 100);
    }
}
