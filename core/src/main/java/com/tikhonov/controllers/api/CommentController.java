package com.tikhonov.controllers.api;

import com.tikhonov.models.dto.CommentRequestDto;
import com.tikhonov.models.dto.CommentResponseDto;
import com.tikhonov.services.facades.CommentFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class CommentController {

    private final CommentFacade commentFacade;

    public CommentController(CommentFacade commentFacade) {
        this.commentFacade = commentFacade;
    }

    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> create(@Valid @RequestBody CommentRequestDto comment,
                                                     Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentFacade.create(comment, authentication));
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> getAllByTaskId(@RequestParam @Min(1) Long taskId) {
        return ResponseEntity.ok(commentFacade.findAllByTaskId(taskId));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteById(@PathVariable @Min(1) Long id) {
        commentFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
