package ru.litvak.giftcardparser.manager;

import ru.litvak.giftcardparser.model.responce.GiftCardResponse;

public interface ParserManager {
    GiftCardResponse parse(String link);

    boolean isSupport(String url);
}
