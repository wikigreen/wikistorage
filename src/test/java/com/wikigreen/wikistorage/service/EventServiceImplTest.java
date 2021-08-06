package com.wikigreen.wikistorage.service;

import com.wikigreen.wikistorage.repository.EventRepository;
import com.wikigreen.wikistorage.repository.UserRepository;
import com.wikigreen.wikistorage.service.exceptions.CustomEntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @InjectMocks
    EventServiceImpl eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void testGetByIdIfEventDoesNotExist() {
        when(eventRepository.existsById(Mockito.anyLong())).thenReturn(false);

        assertThrows(CustomEntityNotFoundException.class, () -> eventService.getById(1L));
    }

    @Test
    void testDeleteIfEventDoesNotExist() {
        when(eventRepository.existsById(Mockito.anyLong())).thenReturn(false);

        assertThrows(CustomEntityNotFoundException.class, () -> eventService.deleteById(1L));
    }

    @Test
    void getByUserIdIfUserDoesNotFound() {
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);

        assertThrows(CustomEntityNotFoundException.class, () -> eventService.getByUserId(1L));
    }

    @Test
    void getByUserNickNameIfUserDoesNotFound() {
        when(userRepository.existsByNickName(Mockito.anyString())).thenReturn(false);

        assertThrows(CustomEntityNotFoundException.class, () -> eventService.getAllByUserNickName("wiki"));
    }

}
