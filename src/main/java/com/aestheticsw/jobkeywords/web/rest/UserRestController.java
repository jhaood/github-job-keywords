/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com)
 * Apache Version 2 license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.web.rest;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aestheticsw.jobkeywords.shared.config.Log;

/**
 * This class experiments with the JSON serializer. It restricts the output to ONLY JSON instead of
 * allowing the HTTP client to request either JSON or XML.
 */
@RestController
@RequestMapping(value = "/rest/user")
public class UserRestController {
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