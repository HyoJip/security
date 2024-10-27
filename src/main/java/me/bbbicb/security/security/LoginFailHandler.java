package me.bbbicb.security.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class LoginFailHandler implements AuthenticationFailureHandler {

  private final ObjectMapper om;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    // TODO 로그인 실패한 예외 클래스별로 적절하게 응답
    if (exception instanceof BadCredentialsException) {
      om.writeValue(response.getWriter(), "Invalid username or password");
      return;
    }

    om.writeValue(response.getWriter(), "Authentication failed");
  }
}
