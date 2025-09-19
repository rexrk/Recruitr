package com.raman.recruitr;

import com.raman.recruitr.utils.Constants;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
        name = Constants.SCHEME_NAME,
        scheme = Constants.SCHEME,
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class RecruitrApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecruitrApplication.class, args);
	}

}
