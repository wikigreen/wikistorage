package com.wikigreen.wikistorage.repository;

import com.wikigreen.wikistorage.model.File;
import com.wikigreen.wikistorage.model.FileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    @Modifying
    @Query("update File f " +
            "set f.fileStatus = :status " +
            "where f.id = :id")
    void setStatusById(@Param("id") Long id,
                       @Param("status") FileStatus fileStatus);

    Optional<File> findByIdAndFileStatusNot(Long id, FileStatus fileStatus);

    List<File> findByFileStatusNot(FileStatus fileStatus);

    @Query("from File f " +
            "where f.owner.id = :id ")
    List<File> findByUserId(@Param("id") Long id);

    boolean existsByIdAndFileStatus(Long id, FileStatus fileStatus);

    @Query("from File f " +
            "where f.owner.nickName = :nickName ")
    List<File> findByUserNickName(@Param("nickName") String name);
}
