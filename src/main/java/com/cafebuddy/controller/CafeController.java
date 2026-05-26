package com.cafebuddy.controller;

import com.cafebuddy.dto.CafeDto;
import com.cafebuddy.dto.UploadResultDto;
import com.cafebuddy.service.CafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/cafes")
@CrossOrigin(origins = "*")
public class CafeController {

    @Autowired
    private CafeService cafeService;

    /** GET /api/cafes or /api/cafes?q=search */
    @GetMapping
    public ResponseEntity<List<CafeDto>> list(
            @RequestParam(name = "q", required = false) String query) {
        List<CafeDto> result = (query != null && !query.isBlank())
                ? cafeService.searchCafes(query)
                : cafeService.getAllCafes();
        return ResponseEntity.ok(result);
    }

    /** GET /api/cafes/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<CafeDto> get(@PathVariable Long id) {
        return cafeService.getCafeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /api/cafes — add a single cafe (requires auth) */
    @PostMapping
    public ResponseEntity<CafeDto> addCafe(@RequestBody CafeDto cafeDto) {
        return ResponseEntity.ok(cafeService.addCafe(cafeDto));
    }

    /** PUT /api/cafes/{id} — edit a cafe (requires auth) */
    @PutMapping("/{id}")
    public ResponseEntity<CafeDto> updateCafe(@PathVariable Long id,
                                               @RequestBody CafeDto cafeDto) {
        return ResponseEntity.ok(cafeService.updateCafe(id, cafeDto));
    }

    /** DELETE /api/cafes/{id} — requires auth */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cafeService.deleteCafe(id);
        return ResponseEntity.noContent().build();
    }

    /** POST /api/cafes/upload — upload Excel (requires auth) */
    @PostMapping("/upload")
    public ResponseEntity<UploadResultDto> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(cafeService.importFromExcel(file));
    }
}
