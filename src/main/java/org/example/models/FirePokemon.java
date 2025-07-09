package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("Fire")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FirePokemon extends Pokemon {

    int fireResistance;
    int firePower;

    public FirePokemon(
        final String name,
        final int health,
        final int damage,
        final int fireResistance,
        final int firePower
    ) {
        super(null, name, health, damage, null, false, 0, 0, null);
        this.fireResistance = Math.max(0, fireResistance);
        this.firePower = Math.max(0, firePower);
    }

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
        return String.format(
            "Fire  | %-10s | HP:%-4d | DMG:%-3d | FireRes:%-3d | FirePwr:%-3d",
            getName(), getHealth(), getDamage(), fireResistance, firePower
        );
    }
}
