package io.foodapp.server.controllers.Ai;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Ai.IntentTypeRequest;
import io.foodapp.server.dtos.Ai.IntentTypeResponse;
import io.foodapp.server.services.Ai.IntentTypeService;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/intent-types")
public class IntentTypeController {
    private final IntentTypeService intentTypeService;
    
    @GetMapping
    public ResponseEntity<List<IntentTypeResponse>> getAllIntentTypes() {
        List<IntentTypeResponse> intentTypeResponses = intentTypeService.getAllIntentTypes();

        return ResponseEntity.ok(intentTypeResponses);
    }

    @PostMapping
    public ResponseEntity<IntentTypeResponse> createIntentType(@RequestBody IntentTypeRequest request) {
        IntentTypeResponse created = intentTypeService.createIntentType(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntentTypeResponse> updateIntentType(@PathVariable Integer id, @RequestBody IntentTypeRequest request) {
        IntentTypeResponse updated = intentTypeService.updateIntentType(id, request);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIntentType(@PathVariable Integer id) {
        intentTypeService.deleteIntentType(id);
        return ResponseEntity.noContent().build();
    }
}
