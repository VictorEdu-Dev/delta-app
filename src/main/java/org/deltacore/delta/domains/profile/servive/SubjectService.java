package org.deltacore.delta.domains.profile.servive;

import org.deltacore.delta.domains.tutoring.dto.SubjectDTO;
import org.deltacore.delta.domains.tutoring.repository.SubjectDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    private SubjectDAO subjectDAO;

    @Autowired
    private void setSubjectDAO(SubjectDAO subjectDAO) {
        this.subjectDAO = subjectDAO;
    }

    public List<SubjectDTO.SubjectInfoDTO> findAvailableSubjects() {
        return subjectDAO.findAvailableSubjects();
    }
}
