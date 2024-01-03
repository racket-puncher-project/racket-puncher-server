package com.example.demo.oauth.dto;

        import jakarta.validation.constraints.NotBlank;
        import lombok.AllArgsConstructor;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenDto {
    @NotBlank(message = "accessToken을 입력해주세요.")
    private String accessToken;
}
