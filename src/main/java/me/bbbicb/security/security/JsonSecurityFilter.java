package me.bbbicb.security.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.bbbicb.security.MyUserDetails;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonSecurityFilter extends AbstractAuthenticationProcessingFilter {

  private static final String DEFAULT_LOGIN_PATH = "/login";

  private final ObjectMapper om;

  public JsonSecurityFilter(ObjectMapper om) {
    super(DEFAULT_LOGIN_PATH);
    this.om = om;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException, IOException {

    if (!HttpMethod.POST.name().equals(request.getMethod())) {
      throw new RuntimeException("Authentication method not supported");
    }

    MyUserDetails loginRequest = om.readValue(request.getReader(), MyUserDetails.class);

    if (!StringUtils.hasText(loginRequest.getUsername()) || !StringUtils.hasText(loginRequest.getPassword())) {
      throw new AuthenticationServiceException("Username or Password not provided");
    }

    JsonAuthenticationToken loginToken = new JsonAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), null);

    return this.getAuthenticationManager().authenticate(loginToken);
  }
}
