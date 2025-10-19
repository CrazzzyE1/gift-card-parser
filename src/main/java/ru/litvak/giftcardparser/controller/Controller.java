package ru.litvak.giftcardparser.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.litvak.giftcardparser.model.request.SourceLinkRequest;
import ru.litvak.giftcardparser.model.responce.GiftCardResponse;
import ru.litvak.giftcardparser.service.GiftCardParserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parse")
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class Controller {

    private final GiftCardParserService service;

    @PostMapping()
    public GiftCardResponse parse(@RequestBody @Valid SourceLinkRequest request) {
        return service.parse(request);
    }
}
