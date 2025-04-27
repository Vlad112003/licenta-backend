package com.elearning.backend.service;

import com.elearning.backend.model.Text;
import com.elearning.backend.repository.TextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TextService {

    private final TextRepository textRepository;

    @Autowired
    public TextService(TextRepository textRepository) {
        this.textRepository = textRepository;
    }

    public void addText(Text textContent) {
        textRepository.save(textContent);
    }

    public List<Text> getAllTexts() {
        return textRepository.findAll();
    }

    public Text updateText(Long id, Text textContent) {
        Optional<Text> existingText = textRepository.findById(id);
        if (existingText.isPresent()) {
            Text text = existingText.get();
            text.setTitle(textContent.getTitle());
            text.setContent(textContent.getContent());
            return textRepository.save(text);
        }
        return null;
    }

    public boolean deleteText(Long id) {
        if (textRepository.existsById(id)) {
            textRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
