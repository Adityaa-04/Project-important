package com.loanorigination.controller;

import com.loanorigination.entity.NomineeDetails;
import com.loanorigination.service.NomineeDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications/{applicationId}/nominee")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class NomineeDetailsController {

    private final NomineeDetailsService nomineeService;

    public NomineeDetailsController(NomineeDetailsService nomineeService) {
        this.nomineeService = nomineeService;
    }

    // 🔹 READ all nominees for an application
    @Operation(summary = "Get all nominees for an application")
    @GetMapping
    public ResponseEntity<List<NomineeDetails>> getAll(@PathVariable Long applicationId) {
        List<NomineeDetails> nominees = nomineeService.getAllByApplicationId(applicationId);
        return ResponseEntity.ok(nominees);
    }

    // 🔹 READ single nominee by nomineeId
    @Operation(summary = "Get single nominee by ID")
    @GetMapping("/{nomineeId}")
    public ResponseEntity<NomineeDetails> getOne(
            @PathVariable Long applicationId,
            @PathVariable Long nomineeId
    ) {
        return nomineeService.getOne(applicationId, nomineeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 CREATE nominee
    @Operation(summary = "Create new nominee for an application")
    @PostMapping
    public ResponseEntity<NomineeDetails> create(
            @PathVariable Long applicationId,
            @RequestBody NomineeDetails request
    ) {
        NomineeDetails saved = nomineeService.createNominee(applicationId, request);
        return ResponseEntity.ok(saved);
    }

    // 🔹 UPDATE nominee
    @Operation(summary = "Update existing nominee")
    @PutMapping("/{nomineeId}")
    public ResponseEntity<NomineeDetails> update(
            @PathVariable Long applicationId,
            @PathVariable Long nomineeId,
            @RequestBody NomineeDetails request
    ) {
        NomineeDetails updated = nomineeService.updateNominee(applicationId, nomineeId, request);
        return ResponseEntity.ok(updated);
    }

    // 🔹 DELETE nominee
    @Operation(summary = "Delete nominee by ID")
    @DeleteMapping("/{nomineeId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long applicationId,
            @PathVariable Long nomineeId
    ) {
        nomineeService.deleteNominee(applicationId, nomineeId);
        return ResponseEntity.noContent().build();
    }
}
