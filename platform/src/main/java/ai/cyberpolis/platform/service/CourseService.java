package ai.cyberpolis.platform.service;

import ai.cyberpolis.platform.entity.Course;
import ai.cyberpolis.platform.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    public List<Course> getCoursesService(){
        return courseRepository.findAll();
    }

    public Course getCourseService(String name) throws Exception {
        Optional<Course> c = courseRepository.findById(name);
        if(c.isPresent()){
            return c.get();
        } else {
            throw new Exception("Course does not exist");
        }
    }

    public Course addCourseService(String name){
        Course course = new Course(name);
        return courseRepository.insert(course);
    }

}
