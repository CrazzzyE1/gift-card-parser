package ru.litvak.giftcardparser.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.litvak.giftcardparser.enumerated.ProviderType;
import ru.litvak.giftcardparser.manager.ParserManager;
import ru.litvak.giftcardparser.model.responce.GiftCardResponse;

import static ru.litvak.giftcardparser.enumerated.ProviderType.OZON;

@Slf4j
@Component
@RequiredArgsConstructor
public class OzonParserManager implements ParserManager {

    private static final ProviderType TYPE = OZON;

    @Override
    public GiftCardResponse parse(String cardUrl) {
        return null;
    }

    @Override
    public boolean isSupport(String url) {
        return false;
    }
}
