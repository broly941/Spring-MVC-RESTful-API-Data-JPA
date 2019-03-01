package com.intexsoft.devi.controller;

import com.intexsoft.devi.service.interfaces.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/aspect")
public class DemoAspectController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/list")
    public void setList() {
        List<String> strList = new ArrayList<>();
        strList.add("controller");
        demoService.setList(strList, true);
    }

    @GetMapping("bool")
    public void getTrue() {
        boolean answer = demoService.getTrue(false);
    }

    @GetMapping("annotation")
    public void annotation() {
        demoService.methodWithAnnotation();
    }
}
