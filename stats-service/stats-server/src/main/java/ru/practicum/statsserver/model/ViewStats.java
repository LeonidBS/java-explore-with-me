package ru.practicum.statsserver.model;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ViewStats {
    private String app;
    private String uri;
    private long hits;
}
