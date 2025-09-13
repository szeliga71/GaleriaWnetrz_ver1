package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.ProductService;

@RestController
@RequestMapping("/api/admin/import")
public class ImportController {

    private final ProductService productService;

    @Autowired
    public ImportController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(
            value = "/products",
            consumes = {"multipart/form-data"}
    )

    public ResponseEntity<String> importProducts(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        int count = productService.importProductsFromCsv(file);
        return ResponseEntity.ok("Imported " + count + " products");
    }


}