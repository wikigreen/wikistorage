package com.wikigreen.wikistorage.service;

import com.wikigreen.wikistorage.dto.EventDto;
import com.wikigreen.wikistorage.model.Event;
import com.wikigreen.wikistorage.repository.EventRepository;
import com.wikigreen.wikistorage.repository.FileRepository;
import com.wikigreen.wikistorage.repository.UserRepository;
import com.wikigreen.wikistorage.service.exceptions.CustomEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private FileRepository fileRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, FileRepository fileRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    @Transactional
    public void save(Event event) {
        eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event getById(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new CustomEntityNotFoundException(id.toString(), "Event", "id");
        }

        return eventRepository.getById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new CustomEntityNotFoundException(id.toString(), "Event", "id");
        }
        eventRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    @Transactional
    public List<Event> getByUserId(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomEntityNotFoundException(id.toString(), "User", "id");
        }

        return eventRepository.findByUserId(id);
    }

    @Override
    @Transactional
    public List<Event> getAllByUserNickName(String nickName) {
        if (!userRepository.existsByNickName(nickName)) {
            throw new CustomEntityNotFoundException(nickName, "User", "nickname");
        }

        return eventRepository.findByUserNickName(nickName);
    }


}
