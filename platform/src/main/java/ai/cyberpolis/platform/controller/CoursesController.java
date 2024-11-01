package ai.cyberpolis.platform.controller;

import ai.cyberpolis.platform.entity.Course;
import ai.cyberpolis.platform.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/course")
public class CoursesController {

    @Autowired
    CourseService courseService;

    @GetMapping("/getAll")
    public List<Course> getCourses(){
        return courseService.getCoursesService();
    }

    @GetMapping("/get/{name}")
    public Course getCourse(@PathVariable String name) throws Exception {
        return courseService.getCourseService(name);
    }

//    @PostMapping("/add")
//    public Course addCourse(@RequestBody String courseName){
//        return courseService.addCourseService(courseName);
//    }

}
