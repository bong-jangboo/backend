package com.bongjangboo.legacy.file.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "uploaded_file")
public class File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String name;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "file_url", length = 1024)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Enumerated(EnumType.STRING)
    private FileStatus fileStatus;


    @Builder
    public File(String name, Long deptId, String fileUrl, FileType fileType, FileStatus fileStatus) {
        this.name = name;
        this.deptId = deptId;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.fileStatus = fileStatus;
        this.uploadedAt = LocalDateTime.now();
    }




}
