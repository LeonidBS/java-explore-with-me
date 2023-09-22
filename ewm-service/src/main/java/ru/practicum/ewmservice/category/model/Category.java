package ru.practicum.ewmservice.category.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "categories", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Category {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}

