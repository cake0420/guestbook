package com.cake7.guestbook.controller;

import com.cake7.guestbook.dto.TestUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "User API", description = "사용자 관련 기능 제공")
@RestController
@RequestMapping(value = "/api/test/users", produces = "application/json")
public class TestUserController {

    @Operation(summary = "모든 사용자 조회", description = "등록된 모든 사용자의 이름을 조회합니다.")
    @GetMapping("/test")
    public List<String> getUsers() {
        return List.of("Alice", "Bob", "Charlie");
    }

    @PostMapping("/post")
    @Operation(summary = "사용자 등록", description = "사용자 이름과 나이를 등록합니다.")
    public ResponseEntity<TestUserDTO> createUser(@RequestBody TestUserDTO userDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "사용자 아이디 조회", description = "사용자 아이디로 사용자를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Long>> getPost(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("id",id));
    }
}
