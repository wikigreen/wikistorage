package com.wikigreen.wikistorage.repository;

import com.wikigreen.wikistorage.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("from Event e " +
            "where e.owner.id = :id ")
    List<Event> findByUserId(@Param("id") Long id);

    @Query("from Event e " +
            "where e.owner.nickName = :nickName ")
    List<Event> findByUserNickName(@Param("nickName") String nickName);


}
