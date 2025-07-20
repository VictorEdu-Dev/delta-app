package org.deltacore.delta.domains.activity.servive;

import org.deltacore.delta.domains.activity.dto.ActivityDTO;
import org.deltacore.delta.domains.activity.dto.ActivityMapper;
import org.deltacore.delta.domains.activity.model.Activity;
import org.deltacore.delta.domains.activity.repository.ActivityDAO;
import org.deltacore.delta.domains.tutoring.model.Subject;
import org.deltacore.delta.domains.tutoring.repository.SubjectDAO;
import org.deltacore.delta.shared.exception.ConflictException;
import org.deltacore.delta.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityCreation {
    private ActivityDAO activityDAO;
    private SubjectDAO subjectDAO;
    private ActivityMapper mapper;
    private MessageSource messageSource;

    public ActivityDTO saveActivity(ActivityDTO.ActivityRegister activity) {
        if (activityDAO.findActByTitle(activity.title()).isPresent())
            throw new ConflictException(getMessage("error.activity.duplicate"));

        Optional<Subject> subject = subjectDAO.findByCode(activity.subjectCode().toUpperCase());
        if (subject.isEmpty())
            throw new ResourceNotFoundException(getMessage("error.subject.not.found"));
        if(!subject.get().getIsActive())
            throw new ConflictException(getMessage("error.subject.inactive"));

        Activity entity = mapper.toEntity(activity);
        entity.setDefaultValues();
        entity.setMaxScore();
        entity.setImageUrl();
        entity.setLastAccess();
        entity.setSubject(subject.get());

        Activity saved = activityDAO.save(entity);
        return mapper.toDTO(saved);
    }

    private String getMessage(String local) {
        return messageSource.getMessage(local, null, LocaleContextHolder.getLocale());
    }

    @Autowired
    private void setActivityDAO(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    @Autowired
    private void setActivityMapper(ActivityMapper activityMapper) {
        this.mapper = activityMapper;
    }

    @Autowired
    private void setSubjectDAO(SubjectDAO subjectDAO)  {
        this.subjectDAO = subjectDAO;
    }

    @Autowired @Lazy
    private void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
