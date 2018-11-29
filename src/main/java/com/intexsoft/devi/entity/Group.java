package com.intexsoft.devi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * @author DEVIAPHAN
 * The class that stores the state of the entity.
 */
@Entity
@Table(name = "GroupOfUniversity")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GroupId")
    private Long id;

    @Column(name = "Number")
    private String number;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TeacherId")
    private Teacher teacher;

    @JsonIgnore
    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.ALL
    )
    private List<Student> students;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "GroupTeacher",
            joinColumns = @JoinColumn(name = "GroupId"),
            inverseJoinColumns = @JoinColumn(name = "TeacherId"))
    private List<Teacher> teachers;

    public Group() {
    }

    public Group(String number, Teacher teacher, List<Student> students, List<Teacher> teachers) {
        this.number = number;
        this.teacher = teacher;
        this.students = students;
        this.teachers = teachers;
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

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", teacher=" + teacher +
                ", students=" + students +
                ", teachers=" + teachers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) &&
                Objects.equals(number, group.number) &&
                Objects.equals(teacher, group.teacher) &&
                Objects.equals(students, group.students) &&
                Objects.equals(teachers, group.teachers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, teacher, students, teachers);
    }
}
