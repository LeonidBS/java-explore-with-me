package ru.practicum.ewmservice.rating.model;

import lombok.*;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.user.model.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rate {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

  //  @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "event_id")
    private Event event;

 //   @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User rater;

  //  @Enumerated(EnumType.STRING)
    @Column
    private String emoji;
}
