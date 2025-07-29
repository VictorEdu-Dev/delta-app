package org.deltacore.delta.domains.activity.servive;

import org.deltacore.delta.domains.activity.dto.*;
import org.deltacore.delta.domains.activity.dto.ActivityDTO.ActivityRegister.LinkActivityDTO;
import org.deltacore.delta.domains.activity.model.ActivityFiles;
import org.deltacore.delta.domains.activity.repository.ActivityFilesDAO;
import org.deltacore.delta.shared.exception.ConflictException;
import org.deltacore.delta.shared.exception.ResourceNotFoundException;
import org.deltacore.delta.domains.activity.model.Activity;
import org.deltacore.delta.domains.activity.repository.ActivityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ActivitiesSectionService {
    private final ActivityMapper activityMapper;
    private final ActivityDAO activityDAO;
    private ActivityFilesDAO activityFilesDAO;
    private ActivityFilesMapper activityFilesMapper;
    private final MessageSource messageSource;

    @Autowired
    public ActivitiesSectionService(ActivityDAO activityDAO,
                                    ActivityMapper activityMapper,
                                    MessageSource messageSource) {
        this.activityMapper = activityMapper;
        this.activityDAO = activityDAO;
        this.messageSource = messageSource;
    }

    @Transactional
    public ActivityDTO updateActivity(Long id, ActivityDTO updatedActivity) {
        if (id == null || id <= 0) {
            String msg = messageSource.getMessage(
                    "error.activity.id.invalid",
                    null,
                    LocaleContextHolder.getLocale());
            throw new ConflictException(msg);
        }
        if (!updatedActivity.id().equals(id)) {
            String msg = messageSource.getMessage(
                    "conflict.activity.id.unexpected",
                    null,
                    LocaleContextHolder.getLocale());
            throw new ConflictException(msg);
        }
        Activity activity = activityDAO.findById(id)
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage(
                            "error.activity.not.found",
                            null,
                            LocaleContextHolder.getLocale());
                    return new ResourceNotFoundException(msg);
                });

        activityMapper.updateEntityFromDto(updatedActivity, activity);
        return activityMapper.toDTO(activity);
    }

    public void deleteActivity(Long id) {

        Activity activityToBeDeleted = activityDAO.findById(id)
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage(
                            "error.activity.not.found",
                            null,
                            LocaleContextHolder.getLocale());
                    return new ResourceNotFoundException(msg);
                });

        activityDAO.delete(activityToBeDeleted);
    }

    @Transactional
    public ActivityDTO loadActivityData(Long id) {
        Activity activityToBeLoaded = activityDAO.findById(id)
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage(
                            "error.activity.not.found",
                            null,
                            LocaleContextHolder.getLocale());
                    return new ResourceNotFoundException(msg);
                });
        ActivityDTO activityDTO = activityMapper.toDTO(activityToBeLoaded);
        List<ActivityFiles> activityFiles = activityFilesDAO.findByActivityId(id);
        List<ActivityFilesDTO> activityFilesDTO = activityFiles.stream().map(activityFilesMapper::toDTO).toList();

        List<LinkActivityDTO> linkActivityDTO = activityDAO.findLinksByActivityId(id);

        return activityDTO
                .toBuilder()
                .files(activityFilesDTO)
                .links(linkActivityDTO)
                .build();
    }

    @Transactional
    public ActivityDTO completeActivity(Long activityId) {
        Activity activity = activityDAO.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("error.activity.not.found"));

        if (activity.isCompleted()) throw new ConflictException("conflict.activity.completed");
        activity.markAsCompleted();

        return activityMapper.toDTO(activity);
    }

    @Autowired
    private void setActivityFilesDAO(ActivityFilesDAO activityFilesDAO) {
        this.activityFilesDAO = activityFilesDAO;
    }

    @Autowired
    private void setActivityFilesMapper(ActivityFilesMapper activityFilesMapper) {
        this.activityFilesMapper = activityFilesMapper;
    }
}
