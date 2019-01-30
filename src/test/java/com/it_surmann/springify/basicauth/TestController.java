package com.it_surmann.springify.basicauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * A controller to provide an endpoint for testing.
 */
@Slf4j
@Controller
public class TestController {

    @RequestMapping("/test.html")
    @ResponseBody
    public String test() {
        log.info("Called test.html");

        return "test result";
    }

}
