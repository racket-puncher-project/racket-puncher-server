package com.example.demo.siteuser.dto;

        import jakarta.validation.constraints.NotBlank;
        import lombok.AllArgsConstructor;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReissueDto {
    @NotBlank(message = "accessToken을 입력해주세요.")
    private String accessToken;

    @NotBlank(message = "refreshToken을 입력해주세요.")
    private String refreshToken;
}
