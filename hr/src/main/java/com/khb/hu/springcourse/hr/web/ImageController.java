package com.khb.hu.springcourse.hr.web;

import com.khb.hu.springcourse.hr.api.ImageControllerApi;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController implements ImageControllerApi {
    @Override
    public ResponseEntity<Resource> getImage(String id, String imageid) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.IMAGE_JPEG)
                .body(new FileSystemResource("/temp/kh-hr/employee/" + id + "/" + imageid));
    }
}
