package ru.litvak.giftcardparser.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.litvak.giftcardparser.manager.ParserManager;
import ru.litvak.giftcardparser.model.responce.GiftCardResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoldAppleParserManager implements ParserManager {

    @Override
    public GiftCardResponse parse(String cardUrl) {
        return null;
    }

    @Override
    public boolean isSupport(String url) {
//        return url.startsWith(GOLD_APPLE.getDomain());
        return false;
    }
}
