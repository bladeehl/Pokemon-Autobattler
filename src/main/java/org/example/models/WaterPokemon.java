package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("Water")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WaterPokemon extends Pokemon {

    int waterResistance;
    int waterPower;

    public WaterPokemon(
        final String name,
        final int health,
        final int damage,
        final int waterResistance,
        final int waterPower
    ) {
        super(null, name, health, damage, null, false, 0, 0, null);
        this.waterResistance = Math.max(0, waterResistance);
        this.waterPower = Math.max(0, waterPower);
    }

    public void waterHide() {
        setImmuneNextTurn(true);
    }

    public void waveAttack(final Pokemon target) {
        target.takeDamage(waterPower + 10);
    }

    @Override
    public void attack(final Pokemon target) {
        target.takeDamage(getDamage());
    }

    @Override
    public void defend() {
        setDamage(getDamage() - waterResistance / 3);
    }

    @Override
    public void evolve() {
        setDamage((int) (getDamage() * 1.5));
    }

    @Override
    public void ability() {
        setHealth(getHealth() + waterPower);
    }

    @Override
    public String toString() {
        return String.format(
            "Water | %-10s | HP:%-4d | DMG:%-3d | WaterRes:%-3d | WaterPwr:%-3d",
            getName(), getHealth(), getDamage(), waterResistance, waterPower
        );
    }
}
