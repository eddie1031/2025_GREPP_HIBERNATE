package io.eddie.domain.eg2._3;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class GymMemberShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
//    @Enumerated(EnumType.ORDINAL)
    @Enumerated(EnumType.STRING)
    private Level membershipLevel;

}
