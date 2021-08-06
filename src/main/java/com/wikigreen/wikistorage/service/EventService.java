package com.wikigreen.wikistorage.service;

import com.wikigreen.wikistorage.dto.EventDto;
import com.wikigreen.wikistorage.model.Event;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EventService {
    void save(Event event);
    Event getById(Long id);
    void deleteById(Long id);
    List<Event> getAll();
    List<Event> getByUserId(Long id);

    @Transactional
    List<Event> getAllByUserNickName(String nickName);
}
