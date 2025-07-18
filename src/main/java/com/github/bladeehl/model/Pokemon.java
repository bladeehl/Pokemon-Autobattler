package com.github.bladeehl.model;

import jakarta.persistence.*;
import lombok.*;

import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Pokemon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    Integer health;
    Integer damage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    Trainer trainer;

    @Transient
    boolean immuneNextTurn;

    @Transient
    int damageReduction;

    @Transient
    int counterDamage;

    public abstract int attack(Pokemon target);

    public abstract void defend();

    public abstract void evolve();

    public abstract int ability();

    public void takeDamage(final int damage) {
        if (immuneNextTurn) {
            immuneNextTurn = false;
            return;
        }

        val finalDamage = Math.max(0, damage - damageReduction);
        health = Math.max(0, health - finalDamage);
    }
}
