package net.lanet.vollmed.infra.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderUtil {
    @Autowired
    PasswordEncoder passwordEncoder;
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
