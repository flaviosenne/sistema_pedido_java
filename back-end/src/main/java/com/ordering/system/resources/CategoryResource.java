package com.ordering.system.resources;



import com.ordering.system.domains.Category;
import com.ordering.system.dto.CategoryDTO;
import com.ordering.system.services.CategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "Buscar todas categorias")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll(){

        return this.categoryService.findAllCategory();
    }

    @ApiOperation(value = "Busca por id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Category> findById(@PathVariable Integer id){

        return this.categoryService.findCategoryById(id);
    }

    @ApiOperation(value = "Salvar categorias")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CategoryDTO category){
        Category categoryConverted = this.categoryService.toCategory(category);
        Category categorySaved = this.categoryService.saveCategory(categoryConverted);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(categorySaved.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Atualizar categoria")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @Valid @RequestBody CategoryDTO category){
        category.setId(id);
        Category categoryConverted = this.categoryService.toCategory(category);
        Category categoryUpdated = this.categoryService.updateCategory(categoryConverted);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(categoryUpdated.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Não épossível excluir uma categoria que possui produtos"),
            @ApiResponse(code = 404, message = "Código inexistente") })
    @ApiOperation(value = "Deletar categoria")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id){

        this.categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/page")
    public ResponseEntity<Page<CategoryDTO>> findPage(
            @RequestParam(value = "page", defaultValue ="0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue ="24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue ="name") String orderBy,
            @RequestParam(value = "direction", defaultValue ="asc") String direction){

        Page<Category> list = this.categoryService.findPage(page, linesPerPage, orderBy, direction);
        Page<CategoryDTO> listDTO = list.map(obj -> new CategoryDTO(obj));
        return ResponseEntity.ok(listDTO);
    }
}
