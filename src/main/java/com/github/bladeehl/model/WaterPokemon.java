package com.github.bladeehl.model;

import jakarta.persistence.*;
import lombok.*;

import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("Water")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WaterPokemon extends Pokemon {

    int waterResistance;
    int waterPower;

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
        return "Water | %-10s | HP:%-4d | DMG:%-3d | WaterRes:%-3d | WaterPwr:%-3d"
            .formatted(getName(), getHealth(), getDamage(), waterResistance, waterPower);
    }

}
