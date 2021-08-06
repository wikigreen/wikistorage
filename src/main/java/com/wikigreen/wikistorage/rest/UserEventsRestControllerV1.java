package com.wikigreen.wikistorage.rest;

import com.wikigreen.wikistorage.dto.EventDto;
import com.wikigreen.wikistorage.model.Event;
import com.wikigreen.wikistorage.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/profile/events")
public class UserEventsRestControllerV1 {
    private EventService eventService;
    private ModelMapper modelMapper;

    @Autowired
    public UserEventsRestControllerV1(EventService eventService,
                                      ModelMapper modelMapper) {
        this.eventService = eventService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents(Principal principal){
        List<Event> events = eventService.getAllByUserNickName(principal.getName());
        List<EventDto> eventDtos = events.stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(eventDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable("id") Long id, Principal principal){
        Event event = eventService.getById(id);

        if (!event.getOwner().getNickName().equals(principal.getName())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        EventDto eventDto = modelMapper.map(event, EventDto.class);

        return new ResponseEntity<>(eventDto, HttpStatus.OK);
    }
}
