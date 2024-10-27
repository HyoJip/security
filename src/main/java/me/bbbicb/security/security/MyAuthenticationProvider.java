package me.bbbicb.security.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@RequiredArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    JsonAuthenticationToken requestToken = (JsonAuthenticationToken) authentication;

    UserDetails userDetails = userDetailsService.loadUserByUsername(Objects.toString(requestToken.getPrincipal(), ""));
    if (userDetails == null || !passwordEncoder.matches(userDetails.getPassword(), userDetails.getPassword())) {
      throw new BadCredentialsException("Invalid password");
    }
    // TODO LoginFailHandler에서 catch할 예외 던지기
//    if (!userDetails.isEnabled()) {
//      throw new BadCredentialsException("not user confirm");
//    }

    return JsonAuthenticationToken.fromUserDetails(userDetails);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(JsonAuthenticationToken.class);
  }
}
