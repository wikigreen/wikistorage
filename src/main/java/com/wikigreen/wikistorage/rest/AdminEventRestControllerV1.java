package com.wikigreen.wikistorage.rest;

import com.wikigreen.wikistorage.dto.EventDto;
import com.wikigreen.wikistorage.model.Event;
import com.wikigreen.wikistorage.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/admin/event")
public class AdminEventRestControllerV1 {
    private EventService eventService;
    private ModelMapper modelMapper;

    @Autowired
    public AdminEventRestControllerV1(EventService eventService,
                                      ModelMapper modelMapper) {
        this.eventService = eventService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents(){
        List<Event> events = eventService.getAll();
        List<EventDto> eventDtos = events.stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(eventDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@RequestParam("id") Long id){
        Event event = eventService.getById(id);
        EventDto eventDto = modelMapper.map(event, EventDto.class);

        return new ResponseEntity<>(eventDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EventDto> deleteEventByID(@RequestParam("id") Long id){
        eventService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
