package faang.school.postservice.controller;

import faang.school.postservice.dto.ad.AdDto;
import faang.school.postservice.service.ad.AdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Advertisement", description = "Operations related to advertisements")
@RequestMapping("/advertisements")
@RestController
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    @Operation(summary = "Create a new advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Advertisement created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid advertisement data"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred during processing")
    })
    @PostMapping
    public ResponseEntity<AdDto> createAd(@RequestBody AdDto adDto) {
        return ResponseEntity.ok(adService.createAd(adDto));
    }
}
