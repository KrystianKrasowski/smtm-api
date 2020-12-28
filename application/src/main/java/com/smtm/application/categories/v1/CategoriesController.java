package com.smtm.application.categories.v1;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/categories")
public class CategoriesController {

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryDto category) {
        CategoryDto newCategory = CategoryDto.of(1L, category.getName(), category.getIcon());
        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(CategoriesController.class).get(newCategory.getId()));
        EntityModel<CategoryDto> entity = EntityModel.of(newCategory).add(linkBuilder.withSelfRel());
        return ResponseEntity.created(linkBuilder.toUri())
            .contentType(MediaTypes.CATEGORY)
            .body(entity);
    }
}
