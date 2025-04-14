package io.eddie.domain.eg4._1;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coffee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coffee_name")
    private String name;
    private Integer amount;
    private Integer price;

    @Builder
    public Coffee(String name, Integer amount, Integer price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

}
