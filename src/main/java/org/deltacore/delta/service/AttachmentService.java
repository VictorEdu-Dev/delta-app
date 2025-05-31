package org.deltacore.delta.service;

import org.deltacore.delta.dto.AttachmentDTO;
import org.deltacore.delta.dto.AttachmentMapper;
import org.deltacore.delta.model.Attachment;
import org.deltacore.delta.repositorie.AttachmentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttachmentService {

    private final AttachmentDAO attachmentDAO;
    private final AttachmentMapper attachmentMapper;

    @Autowired
    public AttachmentService(AttachmentDAO attachmentDAO, AttachmentMapper attachmentMapper) {
        this.attachmentDAO = attachmentDAO;
        this.attachmentMapper = attachmentMapper;
    }

    public List<AttachmentDTO> getAttachmentsForActivity(Long activityId) {
        List<Attachment> attachments = attachmentDAO.findByActivityId(activityId);
        return attachments.stream()
                .map(attachmentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
