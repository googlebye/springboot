package com.lang.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lang.constants.MediaTypes;

@RestController(value = "/ctl")
public class CenterController {

    @RequestMapping(value = "/register", produces = MediaTypes.JSON_UTF_8)
    public void register() {
    }


}
