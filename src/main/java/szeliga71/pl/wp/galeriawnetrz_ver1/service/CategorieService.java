package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Categories;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoriesRepo;

import java.util.List;

@Service
public class CategorieService {

    @Autowired
    CategoriesRepo categoriesRepo;

    public List<Categories> getAllCategories() {
        return categoriesRepo.findAll();
    }

    public Categories getCategoryById(Long id) {
        return categoriesRepo.findById(id).get();
    }
}
