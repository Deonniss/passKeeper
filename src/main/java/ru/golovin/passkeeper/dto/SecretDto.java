package ru.golovin.passkeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecretDto {

    private Integer id;

    private String serviceName;

    private String serviceLogin;

    private String servicePassword;
}
