package com.example.jangboo.infra.aws;

import com.example.jangboo.global.dto.ResultDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheck {

    @GetMapping("/healt")
    public ResponseEntity<ResultDto<Void>> healtCheck(
    ){
        return ResponseEntity.ok(ResultDto.of(200,"health status : 200",null));
    }

}
