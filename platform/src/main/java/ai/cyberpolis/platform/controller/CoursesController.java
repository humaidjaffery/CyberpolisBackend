package ai.cyberpolis.platform.controller;

import ai.cyberpolis.platform.entity.Course;
import ai.cyberpolis.platform.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CoursesController {

    @Autowired
    CourseService courseService;

    @GetMapping("/hello")
    public String getHello(){
        return "hello";
    }

    @GetMapping("/courses")
    public List<Course> getCourses(){
        return courseService.getCoursesService();
    }

    @GetMapping("/courses/{name}")
    public Course getCourse(@PathVariable String name){
        return courseService.getCourseService(name);
    }

}
