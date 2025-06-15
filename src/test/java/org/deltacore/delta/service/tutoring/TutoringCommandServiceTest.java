package org.deltacore.delta.service.tutoring;

import org.deltacore.delta.dto.tutoring.MonitorMapper;
import org.deltacore.delta.dto.tutoring.SubjectMapper;
import org.deltacore.delta.dto.tutoring.TutoringDTO;
import org.deltacore.delta.dto.tutoring.TutoringMapper;
import org.deltacore.delta.model.tutoring.Modality;
import org.deltacore.delta.model.tutoring.Tutoring;
import org.deltacore.delta.repositorie.tutoring.DayTimeEntryDAO;
import org.deltacore.delta.repositorie.tutoring.SubjectDAO;
import org.deltacore.delta.repositorie.tutoring.TutoringDAO;
import org.deltacore.delta.repositorie.user.MonitorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TutoringCommandServiceUnitTest {

    @Mock
    private TutoringDAO tutoringDAO;

    @Mock
    private TutoringMapper tutoringMapper;

    @Mock
    private SubjectDAO subjectDAO;

    @Mock
    private DayTimeEntryDAO dayTimeEntryDAO;

    @Mock
    private MonitorDAO monitorDAO;

    @Mock
    private SubjectMapper subjectMapper;

    @Mock
    private MonitorMapper monitorMapper;

    @InjectMocks
    private TutoringCommandService service;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void registerMonitoring_shouldSaveAndReturnDTO() {
        TutoringDTO inputDTO = TutoringDTO
                .builder()
                .description("Description 01")
                .mode(Modality.HYBRID.name())
                .location("Sala 10 - Bloco I")
                .isActive(true)
                .urlThumbnail("https://example.com/thumbnail.jpg")
                .vacancies(10)
                .createdAt(LocalDateTime.now())
                .build();
        Tutoring entity = new Tutoring();
        Tutoring savedEntity = Tutoring
                .builder()
                .id(1L)
                .description("Description 01")
                .mode(Modality.HYBRID)
                .location("Sala 10 - Bloco I")
                .isActive(true)
                .urlThumbnail("https://example.com/thumbnail.jpg")
                .vacancies(10)
                .createdAt(LocalDateTime.now())
                .build();
        TutoringDTO returnedDTO = TutoringDTO
                .builder()
                .id(1L)
                .description("Description 01")
                .mode(Modality.HYBRID.name())
                .location("Sala 10 - Bloco I")
                .isActive(true)
                .urlThumbnail("https://example.com/thumbnail.jpg")
                .vacancies(10)
                .createdAt(LocalDateTime.now())
                .build();

        when(tutoringMapper.toEntity(inputDTO)).thenReturn(entity);
        when(tutoringDAO.save(entity)).thenReturn(savedEntity);
        when(tutoringMapper.toDTO(savedEntity)).thenReturn(returnedDTO);

        TutoringDTO result = service.registerMonitoring(inputDTO);

        assertNotNull(result);
        assertEquals(returnedDTO, result);

        verify(tutoringMapper).toEntity(inputDTO);
        verify(tutoringDAO).save(entity);
        verify(tutoringMapper).toDTO(savedEntity);
    }

    @Test
    void registerMonitoring_shouldPropagateException_whenSaveFails() {
        TutoringDTO inputDTO = TutoringDTO.builder()
                .description("Description 01")
                .mode(Modality.HYBRID.name())
                .location("Sala 10 - Bloco I")
                .isActive(true)
                .urlThumbnail("https://example.com/thumbnail.jpg")
                .vacancies(10)
                .createdAt(LocalDateTime.now())
                .build();

        Tutoring entity = new Tutoring();

        when(tutoringMapper.toEntity(inputDTO)).thenReturn(entity);
        when(tutoringDAO.save(entity)).thenThrow(new RuntimeException("DB error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.registerMonitoring(inputDTO);
        });

        assertEquals("DB error", exception.getMessage());

        verify(tutoringMapper).toEntity(inputDTO);
        verify(tutoringDAO).save(entity);
        verifyNoMoreInteractions(tutoringMapper);
    }
}
