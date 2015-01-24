package com.aestheticsw.jobkeywords.web.rest;

import net.exacode.spring.logging.inject.Log;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Log
    private Logger log;

    private User user;

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET, produces = "application/json")
    public User getUser(@PathVariable String userName) {
        log.error("/user endpoint");
        if (user == null) {
            user = new User();
            user.setName(userName);
            user.setPassword(userName);
        }

        return user;
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.DELETE, produces = "application/json")
    public User deleteUser(@PathVariable Long userName) {
        log.error("/user DELETE endpoint");

        return user;
    }
}