package com.github.bladeehl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Pokemon implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    @Nullable Integer health;
    @Nullable Integer damage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    @JsonIgnore
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

    public abstract int specialAttack(final @NonNull Pokemon target);

    public abstract void defensiveAbility();

    public void takeDamage(final int damage) {
        if (immuneNextTurn) {
            immuneNextTurn = false;
            return;
        }

        val finalDamage = Math.max(0, damage - damageReduction);
        health = Math.max(0, health - finalDamage);
    }
}
