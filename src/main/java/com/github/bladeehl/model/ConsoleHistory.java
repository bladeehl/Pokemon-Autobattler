package com.github.bladeehl.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;
import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "console_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsoleHistory {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NonNull
    private Instant timestamp;

    @Column(nullable = false, length = 1000)
    @NonNull
    private String input;

    @Column(nullable = false, length = 10000)
    @NonNull
    private String output;
}
