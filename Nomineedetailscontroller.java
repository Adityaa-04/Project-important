package com.loanorigination.controller;

import com.loanorigination.entity.NomineeDetails;
import com.loanorigination.service.NomineeDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications/{applicationId}/nominee")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class NomineeDetailsController {

    private final NomineeDetailsService nomineeService;

    public NomineeDetailsController(NomineeDetailsService nomineeService) {
        this.nomineeService = nomineeService;
    }

    @Operation(summary = "Get nominee for an application")
    @GetMapping
    public ResponseEntity<?> get(@PathVariable Long applicationId) {
        return nomineeService.getByApplicationId(applicationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create or update nominee for an application")
    @PostMapping
    public ResponseEntity<NomineeDetails> upsert(
            @PathVariable Long applicationId,
            @RequestBody NomineeDetails request
    ) {
        NomineeDetails saved = nomineeService.upsertNominee(applicationId, request);
        return ResponseEntity.ok(saved);
    }
}
