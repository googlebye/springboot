package com.lang.springboot.controller;

import java.awt.print.Pageable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lang.springboot.constants.MediaTypes;
import com.lang.springboot.dto.InfoDto;

@RestController
public class CenterController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello() {
        return "hello world";
    }

    @RequestMapping(value = "/api/books", produces = MediaTypes.JSON_UTF_8)
    public InfoDto listAllBook(Pageable pageable) {

        return null;
    }

}
