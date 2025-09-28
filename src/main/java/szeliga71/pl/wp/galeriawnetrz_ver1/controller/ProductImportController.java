package szeliga71.pl.wp.galeriawnetrz_ver1.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.ProductImportService;

@RestController
@RequestMapping("/api/admin/import")

public class ProductImportController {

    private final ProductImportService productImportService;

    public ProductImportController(ProductImportService productImportService) {
        this.productImportService = productImportService;
    }

    @PostMapping(value="/products-from-csv",consumes = "multipart/form-data")
    public ResponseEntity<String> importProducts(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Plik CSV jest pusty");
        }

        try {
            productImportService.importCsv(file.getInputStream());
            return ResponseEntity.ok("Import zakończony sukcesem");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Błąd podczas importu: " + e.getMessage());
        }
    }
}

