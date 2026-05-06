package com.example.tuwaiqcapstone1.Controller;

import com.example.tuwaiqcapstone1.ApiResponse.ApiResponse;
import com.example.tuwaiqcapstone1.Model.Category;
import com.example.tuwaiqcapstone1.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    //BASIC CRUD ENDPOINTS
    @GetMapping("/get")
    public ResponseEntity<?> getCategories(){
        return ResponseEntity.status(200).body(categoryService.getCategories());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody @Valid Category category, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        boolean isDone = categoryService.addCategory(category);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("Category added successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("ID: " + category.getId() + " already used"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody @Valid Category category, Errors errors){
        if(errors.hasErrors())
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));

        boolean isDone = categoryService.updateCategory(id, category);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("Category updated successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("No category with ID: " + id + " found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id){
        boolean isDone = categoryService.deleteCategory(id);
        if(isDone)
            return ResponseEntity.status(200).body(new ApiResponse("Category deleted successfully"));
        return ResponseEntity.status(400).body(new ApiResponse("No category with ID: " + id + " found"));
    }


    //EXTRA ENDPOINTS
    @GetMapping("/get-name/{name}")
    public ResponseEntity<?> searchByName(@PathVariable String name){
        Category category = categoryService.searchByName(name);
        if(category == null)
            return ResponseEntity.status(400).body(new ApiResponse("No category with name: " + name + " found"));
        return ResponseEntity.status(200).body(category);
    }
}
