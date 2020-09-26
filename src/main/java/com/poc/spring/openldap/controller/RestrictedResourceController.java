package com.poc.spring.openldap.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RestrictedResourceController {

    @GetMapping(value = "/restricted")
    public ModelAndView viewRestrictedPage() {

        ModelAndView model = new ModelAndView();
        model.addObject("message", "This is a highly confidential message");
        model.setViewName("restricted");
        return model;
    }
}