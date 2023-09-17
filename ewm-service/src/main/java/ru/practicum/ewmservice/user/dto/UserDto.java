package ru.practicum.ewmservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.validation.ValidationGroups;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Integer id;

    @NotBlank(message = "Parameter name is empty", groups = ValidationGroups.Create.class)
    private String name;

    @NotBlank(message = "Parameter name is empty", groups = ValidationGroups.Create.class)
    @Email(message = "Format the passed email is wrong")
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
            message = "Format the passed email is not correct")
    private String email;

    @Override
    public String toString() {
        return "{id=" + id
                + ", name=" + name
                + ", email=" + email + "}";
    }
}
