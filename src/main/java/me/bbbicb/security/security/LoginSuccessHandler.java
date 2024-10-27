package me.bbbicb.security.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper om;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    UserDetails user = (UserDetails) authentication.getPrincipal();

    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    om.writeValue(response.getWriter(), user);

    clearAuthenticationAttributes(request);
  }

  protected final void clearAuthenticationAttributes(HttpServletRequest request) {
    HttpSession session = request.getSession(false);

    if (session == null) {
      return;
    }

    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
  }
}
