package ru.practicum.ewmservice.compilation.model;

import lombok.*;
import ru.practicum.ewmservice.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Compilation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "event_compilation",
            joinColumns = @JoinColumn(name = "compilation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
    @ToString.Exclude
    List<Event> events;

    @Column
    private Boolean pinned;

    @Column
    private String title;
}
