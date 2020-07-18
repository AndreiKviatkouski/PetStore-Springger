package tms.c32.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tms.c32.entity.ApiResponse;
import tms.c32.entity.Pet;
import tms.c32.entity.Status;
import tms.c32.service.PetService;

import java.util.List;

@RestController
@RequestMapping(path = "/pet")
public class PetResource {
    private final PetService petService;

    @Autowired
    public PetResource(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createPet(@RequestBody Pet pet) {
        petService.setPet(pet);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), "pet", "successful operation"), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateExistingPet(@RequestBody Pet pet) {
        petService.updatePet(pet, 0);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), "pet", "successful operation"), HttpStatus.CREATED);
    }

    @GetMapping(path = "/findByStatus")
    public ResponseEntity<List<Pet>> findByStatus(Status status) {
        petService.checkStatus(status);
        List<Pet> pets = petService.returnPerByStatus(status);
        return new ResponseEntity<>(pets, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Pet> findPet(@PathVariable(name = "id") int id) {
        Pet pet = petService.returnPet(id);
        return new ResponseEntity<>(pet, HttpStatus.CREATED);
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<ApiResponse> updatePetInDB(@PathVariable(name = "id") int id, @RequestBody Pet pet) {
        String name = pet.getName();
        Status status = pet.getStatus();
        petService.updateNameAndStatusById(id, name, status);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), "pet", "successful operation"), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse> findByStatus(@PathVariable(name = "id") int id) {
        petService.deletedPetById(id);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), "pet", "successful operation"), HttpStatus.CREATED);
    }
}
