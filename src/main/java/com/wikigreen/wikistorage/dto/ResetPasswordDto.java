package com.wikigreen.wikistorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ResetPasswordDto {
    @NotNull(message = "Old password is required")
    @Size(min = 8, message = "Old Password must be at least 8 characters long")
    private String oldPassword;
    @NotNull(message = "New password is required")
    @Size(min = 8, message = "New Password must be at least 8 characters long")
    private String newPassword;
}
