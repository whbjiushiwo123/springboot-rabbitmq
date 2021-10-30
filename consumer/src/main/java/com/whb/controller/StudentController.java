package com.whb.controller;

import com.whb.service.IStudentService;
import com.whb.vo.StudentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private IStudentService studentService;

    @ResponseBody
    @RequestMapping("/query")
    public StudentEntity queryStudent(){
        return studentService.queryStudent("1");

    }
}
