package ru.practicum.ewmservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewUserRequest {

    @NotBlank(message = "Parameter name is empty")
    @Length(min = 2, max = 250)
    private String name;

    @NotBlank(message = "Parameter name is empty")
    @Length(min = 6, max = 254)
    @Email(message = "Format the passed email is wrong")
//    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
//            message = "Format the passed email is not correct")
    private String email;

    @Override
    public String toString() {
        return "{name=" + name + ", email=" + email + "}";
    }
}
