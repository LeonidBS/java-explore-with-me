package ru.practicum.ewmservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Integer id;

    private String name;

    private String email;

    private Integer rating;

    @Override
    public String toString() {
        return "{id=" + id
                + ", name=" + name
                + ", email=" + email + "}";
    }
}
