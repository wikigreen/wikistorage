package com.wikigreen.wikistorage.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class File extends BaseEntity{
    @Column(name = "file_name")
    private String fileName;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonView(File.class)
    private User owner;

    @Column(name = "file_status")
    @Enumerated(value = EnumType.STRING)
    private FileStatus fileStatus;

    @Column(name = "upload_date")
    private Date uploadDate;

    @Column(name = "update_date")
    private Date updateDate;
}
