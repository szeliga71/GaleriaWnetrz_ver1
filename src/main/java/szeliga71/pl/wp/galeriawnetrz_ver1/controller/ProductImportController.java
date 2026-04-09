package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.ProductImportService;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/import")
@RequiredArgsConstructor // Automatyczny konstruktor dla productImportService
public class ProductImportController {

    private final ProductImportService productImportService;

    @PostMapping(value = "/products-from-csv", consumes = "multipart/form-data")
    public ResponseEntity<String> importProducts(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Błąd: Plik CSV jest pusty.");
        }

        try {
            // Przekazujemy InputStream do serwisu
            productImportService.importCsv(file.getInputStream());
            return ResponseEntity.ok("Import zakończony sukcesem.");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Błąd odczytu pliku: " + e.getMessage());
        } catch (Exception e) {
            // Obsługa błędów formatowania CSV lub zapisu do bazy
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Błąd przetwarzania danych: " + e.getMessage());
        }
    }
}
