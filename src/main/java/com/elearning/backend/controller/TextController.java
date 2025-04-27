package com.elearning.backend.controller;

import com.elearning.backend.model.Category;
import com.elearning.backend.model.Text;
import com.elearning.backend.service.TextService;
import com.elearning.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/texts")
public class TextController {

    private final TextService textService;
    private final CategoryService categoryService;

    ///
    /// Constructor for TextController
    /// params: TextService textService, CategoryService categoryService
    /// return: None
    /// description: This constructor initializes the TextController with the provided TextService and CategoryService.
    @Autowired
    public TextController(TextService textService, CategoryService categoryService) {
        this.textService = textService;
        this.categoryService = categoryService;
    }


    ///
    /// Creates a new text
    /// params: Text text
    /// return: String
    /// description: This method creates a new text using the provided Text object.
    @PostMapping(consumes = "text/plain")
    public String createText(@RequestBody String content) {
        Text text = new Text();
        text.setTitle("Default Title");
        text.setContent(content);
        text.setCategory(categoryService.getDefaultCategory()); // or set category manually
        textService.addText(text);
        return "Text created successfully";
    }

    ///
    /// Retrieves all texts
    /// params: None
    /// return: List<Text>
    /// description: This method retrieves all texts from the database.
    @GetMapping
    public List<Text> getAllTexts() {
        return textService.getAllTexts();
    }

    // other endpoints...
}
