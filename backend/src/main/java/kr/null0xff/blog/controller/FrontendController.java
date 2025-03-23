package kr.null0xff.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

  // This mapping catches routes that do not contain a period (e.g. '.js', '.css')
  // so that deep links are correctly forwarded to index.html.
  @RequestMapping(value = {"/{path:[^.]*}"})
  public String redirect(@PathVariable String path) {
    return "forward:/index.html";
  }
}