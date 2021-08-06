package com.wikigreen.wikistorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class UserProfileUpdateDto {
    @Pattern(regexp = "^[A-Za-z,.'-]+$",
            message = "First name can contain only English alphabet letters, commas, dots, apostrophes and dashes")
    @NotNull(message = "First name can not be null")
    @Size(max = 50, message = "Maximum size of first name is 50 characters")
    private String firstName;

    @Pattern(regexp = "^[A-Za-z,.'-]+$",
            message = "Last name can contain only English alphabet letters, commas, dots, apostrophes and dashes")
    @NotNull(message = "Last name can not be null")
    @Size(max = 50, message = "Maximum size of last name is 50 characters")
    private String lastName;

    @Email(regexp = "(?!.*\\.\\.)(^[^\\.][^@\\s]+@[^@\\s]+\\.[^@\\s\\.]+$)",
            message = "Invalid email")
    @NotNull(message = "Email can not be null")
    @Size(max = 50, message = "Maximum size of email is 255 characters")
    private String email;

    @Pattern(regexp = "^[A-Za-z_0-9]+$",
            message = "Nickname can contain only English alphabet letters, underscores and digits")
    @NotNull(message = "Nick name can not be null")
    @Size(max = 50, message = "Maximum size of nick name is 32 characters")
    private String nickName;
}
