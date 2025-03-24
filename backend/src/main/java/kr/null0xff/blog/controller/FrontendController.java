package kr.null0xff.blog.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to handle frontend routing for single-page application. This ensures that deep links
 * and refreshing the page works correctly.
 */
@Controller
@Tag(name = "Frontend Routing", description = "Handles SPA routing for deep linking and page refresh support")
public class FrontendController {

  /**
   * This mapping catches routes that do not contain a period (e.g. '.js', '.css') so that deep
   * links are correctly forwarded to index.html.
   *
   * @param path The requested path
   * @return Forward to index.html for the SPA to handle routing
   */
  @Operation(summary = "Forward to index.html",
      description = "Forwards all non-asset requests to index.html for SPA routing")
  @Hidden  // Hide this endpoint from the API documentation as it's not part of the REST API
  @RequestMapping(value = {"/{path:[^.]*}"})
  public String redirect(@PathVariable String path) {
    return "forward:/index.html";
  }
}