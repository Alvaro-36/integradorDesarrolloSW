package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.DnaRequestDTO;
import org.example.dto.ResponseStatsDTO;
import org.example.service.MutantDetector;
import org.example.service.MutantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mutant")
@Tag(name ="Controlador de Mutantes")
public class MutantController {

    private final MutantService mutantService;


    public MutantController(MutantService mutantService) {
        this.mutantService = mutantService;
    }

    @Operation(summary = "Comprueba si el adn ingresado es de un mutante y devuelve un booleano")
    @ApiResponse(responseCode = "200", description = "El ADN pertenece a un mutante")
    @ApiResponse(responseCode = "403", description = "El ADN no pertenece a un mutante")
    @PostMapping("/")
    public ResponseEntity<Void> checkMutant(@RequestBody DnaRequestDTO dnaRequest){
        return mutantService.analyzeDna(dnaRequest.getDna()) ? ResponseEntity.ok().build() : ResponseEntity.status(403).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<ResponseStatsDTO> getStats() {
        ResponseStatsDTO response = ResponseStatsDTO.builder()
                .cantHumanos(mutantService.countAllHumans())
                .cantMutantes(mutantService.countAllMutants())
                .ratio(mutantService.findAllMutants().size()/(double)mutantService.findAllHumans().size())
                .build();
        return ResponseEntity.ok(response);
    }



}
