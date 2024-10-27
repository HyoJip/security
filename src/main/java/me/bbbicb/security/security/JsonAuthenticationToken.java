package me.bbbicb.security.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JsonAuthenticationToken extends UsernamePasswordAuthenticationToken {

  public JsonAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
  }

  public static JsonAuthenticationToken fromUserDetails(UserDetails user) {
    return new JsonAuthenticationToken(user, user.getPassword(), user.getAuthorities());
  }
}
