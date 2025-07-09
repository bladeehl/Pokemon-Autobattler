package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    int health;
    int damage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    Trainer trainer;

    @Transient
    boolean immuneNextTurn;

    @Transient
    int damageReduction;

    @Transient
    int counterDamage;

    @Transient
    Pokemon lastAttacker;

    public abstract void attack(Pokemon target);

    public abstract void defend();

    public abstract void evolve();

    public abstract void ability();

    public void takeDamage(final int damage) {
        if (immuneNextTurn) {
            immuneNextTurn = false;
            return;
        }

        val finalDamage = Math.max(0, damage - damageReduction);
        health = Math.max(0, health - finalDamage);
    }

    public void unImmune() {
        immuneNextTurn = false;
    }
}
