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
@Table(name = "Teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TeacherId")
    private Long id;

    @Column(name = "FirstName")
    private String firstName;

//    @NotEmpty(message = "{email.notempty}")
    @Column(name = "LastName")
    private String lastName;

    @JsonIgnore
    @OneToOne(
            cascade = CascadeType.ALL,
            mappedBy = "teacher")
    private Group group;

    @JsonIgnore
    @ManyToMany(mappedBy = "teachers")
    private List<Group> groups;

    public Teacher() {
    }

    public Teacher(String firstName, String lastName, Group group, List<Group> groups) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.groups = groups;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", group=" + group +
                ", groups=" + groups +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) &&
                Objects.equals(firstName, teacher.firstName) &&
                Objects.equals(lastName, teacher.lastName) &&
                Objects.equals(group, teacher.group) &&
                Objects.equals(groups, teacher.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, group, groups);
    }
}
