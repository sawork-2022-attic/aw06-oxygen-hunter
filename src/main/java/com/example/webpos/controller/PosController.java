package com.example.webpos.controller;

import com.example.webpos.service.PosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PosController {

    private final PosService posService;

    @Autowired
    PosController(PosService posService) {
        this.posService = posService;
    }

    @ModelAttribute
    public void fillData(Model model) {
        model.addAttribute("products", posService.products(0));
        model.addAttribute("cart", posService.content());
        model.addAttribute("tax", posService.getTax());
        model.addAttribute("discount", posService.getDiscount());
        model.addAttribute("subTotal", posService.getSubTotal());
        model.addAttribute("total", posService.getTotal());
    }

    @GetMapping("/")
    public String pos(Model model, HttpServletRequest request) {
        request.getSession();
        return "index";
    }

    @GetMapping("/add")
    public String addProduct(@RequestParam("id") String id) {
        try {
            posService.addProduct(id, 1);
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/remove")
    public String removeProduct(@RequestParam("id") String id) {
        try {
            posService.removeProduct(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/sub")
    public String subProduct(@RequestParam("id") String id) {
        try {
            posService.addProduct(id, -1);
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/empty")
    public String emptyCart() {
        posService.resetCart();
        return "redirect:/";
    }

    @GetMapping("/charge")
    public String charge() {
        posService.charge();
        return "redirect:/";
    }
}
