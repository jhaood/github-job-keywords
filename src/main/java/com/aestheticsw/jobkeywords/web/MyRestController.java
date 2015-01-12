package com.aestheticsw.jobkeywords.web;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class MyRestController {
    @Log
    private Logger log;
    
    private User user;

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public User getUser(@PathVariable String userName) {
        log.error("/user endpoint");
        if (user == null) {
            user = new User();
            user.setName(userName);
        }

        return user;
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.DELETE)
    public User deleteUser(@PathVariable Long userName) {
        log.error("/user endpoint");

        return user;
    }

}