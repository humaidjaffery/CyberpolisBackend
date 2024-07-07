package ai.cyberpolis.platform.service;

import ai.cyberpolis.platform.entity.Course;
import ai.cyberpolis.platform.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    public List<Course> getCoursesService(){
        return courseRepository.findAll();
    }

    public Course getCourseService(String name){
        return courseRepository.getReferenceById(name);
    }

}
