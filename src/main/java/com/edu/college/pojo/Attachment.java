package com.edu.college.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Attachment {
    private Integer id;

    @NonNull
    private Integer achievementId;

    @NonNull
    private String url;
}