package com.intexsoft.devi.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author DEVIAPHAN
 * The class that stores the state of the entity.
 */
@Entity
@Table(name = "GroupOfUniversity")
public class Group implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GroupId")
    private Long id;

    @Column(name = "Number")
    private String number;

    @OneToOne(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinColumn(name = "TeacherId")
    private Teacher teacher;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "group")
    private List<Student> students;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "GroupTeacher", joinColumns = @JoinColumn(name = "GroupId"), inverseJoinColumns = @JoinColumn(name = "TeacherId"))
    private List<Teacher> teachers;

    public Group() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
}
