package ai.cyberpolis.platform.service;

import ai.cyberpolis.platform.entity.Course;
import ai.cyberpolis.platform.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getCoursesService(){
        return courseRepository.findAll();
    }

    public Course getCourseService(String courseId) throws Exception {
        Optional<Course> c = courseRepository.findById(courseId);
        if(c.isPresent()){
            return c.get();
        } else {
            throw new Exception("Course does not exist");
        }
    }

//    public Course addCourseService(String name){
//        String courseId = UUID.randomUUID().toString().substring(0, 4);
//        Course course = new Course(courseId, name);
//        return courseRepository.save(course);
//    }

}
