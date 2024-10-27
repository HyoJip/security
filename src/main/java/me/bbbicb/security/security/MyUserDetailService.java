package me.bbbicb.security.security;

import lombok.RequiredArgsConstructor;
import me.bbbicb.security.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (username.equals("username")) {
      return new MyUserDetails(username, "124", Set.of("ROLE_USER", "ROLE_ADMIN"));
    }

    return null;
  }
}
