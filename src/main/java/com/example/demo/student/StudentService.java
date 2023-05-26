package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if(studentOptional.isPresent()){
            throw new IllegalStateException("Email taken.");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if(exists == true) {
            studentRepository.deleteById(studentId);
        }
        else{
            throw new IllegalStateException("Student ID does not exist");
        }
    }

/*    @Transactional
    public void updateStudent(Student student){
        Optional<Student> oldStudent = studentRepository.findById(student.getId());
        if(!oldStudent.isPresent()){
            throw new IllegalStateException("Student does not exist");
        }

        oldStudent.get().setEmail(student.getEmail());
        oldStudent.get().setName(student.getName());
    }*/

    @Transactional
    public void updateStudent(Long studentId, String name, String email){
        Optional<Student> oldStudent = studentRepository.findById(studentId);
        if(!oldStudent.isPresent()){
            throw new IllegalStateException("Student does not exist");
        }

        if(name!=null && name.length() > 0 && !Objects.equals(oldStudent.get().getName(), name)){
            oldStudent.get().setName(name);
        }

        if(email!=null && email.length()>0 && !Objects.equals(oldStudent.get().getEmail(), email)){
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("email already taken");
            }
            oldStudent.get().setEmail(email);
        }
    }
}
