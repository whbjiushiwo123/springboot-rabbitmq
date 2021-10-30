package com.whb.service.impl;

import com.whb.service.IStudentService;
import com.whb.vo.StudentEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class StudentService implements IStudentService {
    @Override
    @Cacheable(value = "student", key = "#id", unless = "#result eq null")
    public StudentEntity queryStudent(String id) {
        StudentEntity entity = new StudentEntity();
        entity.setAge(10);
        entity.setId("1");
        entity.setName("张三");
        return entity;
    }
}
