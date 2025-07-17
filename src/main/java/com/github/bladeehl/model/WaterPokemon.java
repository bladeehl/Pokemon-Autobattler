package com.github.bladeehl.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.NonNull;

import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("Water")
@Data
@NoArgsConstructor
@SuperBuilder
public class WaterPokemon extends Pokemon {
    int waterResistance;
    int waterPower;

    public void waterHide() {
        setImmuneNextTurn(true);
    }

    public int waveAttack(final @NonNull Pokemon target) {
        val hpBefore = target.getHealth();
        target.takeDamage(waterPower + 10);

        return hpBefore - target.getHealth();
    }

    @Override
    public int attack(final @NonNull Pokemon target) {
        val hpBefore = target.getHealth();
        target.takeDamage(getDamage());

        return hpBefore - getDamage();
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
    public int ability() {
        val hpBefore = getHealth();
        setHealth(getHealth() + waterPower);

        return getHealth() - hpBefore;
    }

    @Override
    public String toString() {
        return "Water | %-10s | HP:%-4d | DMG:%-3d | WaterRes:%-3d | WaterPwr:%-3d"
            .formatted(getName(), getHealth(), getDamage(), waterResistance, waterPower);
    }
}
