package com.raman.recruitr.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class PasswordGeneratorTest {
    int length = 20;

    @Test
    void testCustomLengthPasswordGeneration() {
        String password = PasswordGenerator.generate(length);
        log.info(password);
        assertThat(password.length()).isEqualTo(length);
    }

    @Test
    void testPasswordsAreUnique() {
        String pwd1 = PasswordGenerator.generate(length);
        String pwd2 = PasswordGenerator.generate(length);

        log.info("1: {}, 2: {}" ,pwd1, pwd2);

        assertThat(pwd1).isNotEqualTo(pwd2);
    }
}
