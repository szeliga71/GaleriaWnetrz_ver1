package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserCreateDto {
    private String username;
    private String password;
    private List<String> roles; // np. ["ADMIN", "USER"]
}
