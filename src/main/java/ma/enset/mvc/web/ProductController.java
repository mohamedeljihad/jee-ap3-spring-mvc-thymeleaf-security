package ma.enset.mvc.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.enset.mvc.entities.Product;
import ma.enset.mvc.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;

    @GetMapping("/")
    public String home() {
        return "redirect:/user/index";
    }

    /** Liste paginée + recherche par mot-clé. */
    @GetMapping("/user/index")
    @PreAuthorize("hasRole('USER')")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Product> products = productRepository.findByNameContainsIgnoreCase(keyword, PageRequest.of(page, size));
        model.addAttribute("productList", products.getContent());
        model.addAttribute("pages", new int[products.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "products";
    }

    @PostMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@RequestParam Long id,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "") String keyword) {
        productRepository.deleteById(id);
        return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/admin/newProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "new-product";
    }

    @GetMapping("/admin/editProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String editProduct(@RequestParam Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable : " + id));
        model.addAttribute("product", product);
        return "edit-product";
    }

    /** Ajout et mise à jour (si id renseigné) avec validation du formulaire. */
    @PostMapping("/admin/saveProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return product.getId() == null ? "new-product" : "edit-product";
        }
        productRepository.save(product);
        return "redirect:/user/index";
    }

    @GetMapping("/notAuthorized")
    public String notAuthorized() {
        return "notAuthorized";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
