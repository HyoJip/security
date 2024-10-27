package me.bbbicb.security.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class CommonAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper om;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setCharacterEncoding("UTF-8");

    PrintWriter writer = response.getWriter();
    writer.print("{\"success\" : false, \"message\" : \"" + accessDeniedException.getMessage() + "\"}");
    writer.flush();
  }
}