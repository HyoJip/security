package me.bbbicb.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping(value = "/test")
  public String test() {
    return "success";
  }

  @GetMapping(value = "/ext/test")
  public String externalTest() {
    return "success";
  }
}
