package com.github.bladeehl.model;

import jakarta.persistence.*;
import lombok.*;

import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("Fire")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class FirePokemon extends Pokemon {

    int fireResistance;
    int firePower;

    public void fireBall(final Pokemon target) {
        target.takeDamage(firePower + 20);
    }

    public void fireThorns() {
        fireResistance += 20;
    }

    @Override
    public void attack(final Pokemon target) {
        target.takeDamage(getDamage());
    }

    @Override
    public void defend() {
        setDamage(getDamage() - fireResistance / 3);
    }

    @Override
    public void evolve() {
        setDamage((int) (getDamage() * 1.5));
    }

    @Override
    public void ability() {
        setDamage(getDamage() + 5);
    }

    @Override
    public String toString() {
        return "Fire  | %-10s | HP:%-4d | DMG:%-3d | FireRes:%-3d | FirePwr:%-3d"
            .formatted(getName(), getHealth(), getDamage(), fireResistance, firePower);
    }
}
