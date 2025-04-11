package io.eddie.domain.eg3._1;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "players")
public class Player {

    @Id
    @Column(name = "player_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;

//    @Column(name = "team_id")
//    private Integer teamId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

}
