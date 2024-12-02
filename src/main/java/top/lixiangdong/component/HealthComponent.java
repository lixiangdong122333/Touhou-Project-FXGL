package top.lixiangdong.component;

import com.almasb.fxgl.entity.component.Component;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class HealthComponent extends Component {
    private final IntegerProperty health = new SimpleIntegerProperty();
    private final IntegerProperty maxHealth = new SimpleIntegerProperty();
    public HealthComponent(int initialHealth) {
        this.health.set(initialHealth);
        this.maxHealth.set(initialHealth);

    }

    public int getHealth() {
        return health.get();
    }
    public int getMaxHealth() {
        return maxHealth.get();
    }


    public void setHealth(int health) {
        this.health.set(health);
    }

    public void changeHealth(int amount) {
        this.health.set(this.health.get() + amount);
        System.out.println("Health changed to " + this.health.get());
    }

    public boolean isDead() {
        return health.get() <= 0;
    }

}
