package io.eddie.domain.eg3._1;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "players")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Player {

    @Id
    @Column(name = "player_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;

//    @Column(name = "team_id")
//    private Integer teamId;

    @Setter
    @JoinColumn(name = "team_id", nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    private Team team;

    public Player(String name) {
        this.name = name;
    }
}
