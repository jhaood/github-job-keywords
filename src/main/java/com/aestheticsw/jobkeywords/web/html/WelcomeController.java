/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com) Apache Version 2
 * license: http://www.apache.org/licenses/LICENSE-2.0
 */

package com.aestheticsw.jobkeywords.web.html;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Basic controller to confirm that Thymeleaf is wired up correctly.
 */
@Controller
public class WelcomeController {

    @Value("${app.message:Hello World}")
    private String message = "Hello World";

    @RequestMapping(value = { "/", "/welcome" })
    public String welcome(Map<String, Object> model) {
        model.put("time", new Date());
        model.put("message", this.message);
        return "welcome";
    }

    @RequestMapping("/foo")
    public String foo(Map<String, Object> model) {
        throw new RuntimeException("Foo");
    }

}
