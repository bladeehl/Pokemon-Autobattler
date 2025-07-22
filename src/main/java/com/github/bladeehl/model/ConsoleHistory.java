package com.github.bladeehl.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "console_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsoleHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Instant timestamp;

    @Column(nullable = false, length = 1000)
    String input;

    @Column(nullable = false, length = 10000)
    String output;
}
