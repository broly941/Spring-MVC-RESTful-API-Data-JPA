package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.Group;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Locale;

/**
 * @author DEVIAPHAN on 21.12.2018
 * @project university
 */
public interface GroupService {
    List<Group> getAll(Locale locale);

    Group getById(Long id, Locale locale) throws EntityNotFoundException;

    @Transactional
    Group save(Group group, Long curatorId, Long[] teacherIdList, Locale locale);

    @Transactional
    Group updateById(Group group, Long groupId, Long curatorId, Long[] teacherIdList, Locale locale) throws EntityNotFoundException;

    void deleteById(Long id, Locale locale);
}
