package ru.litvak.giftcardparser.service;

import jakarta.validation.Valid;
import ru.litvak.giftcardparser.model.request.SourceLinkRequest;
import ru.litvak.giftcardparser.model.responce.GiftCardResponse;

public interface GiftCardService {
    GiftCardResponse parse(@Valid SourceLinkRequest request);
}
