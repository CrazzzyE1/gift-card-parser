package ru.litvak.giftcardparser.enumerated;

import lombok.Getter;

@Getter
public enum ProviderType {
    YANDEX("https://market.yandex.ru/card"),
    YANDEX_MOBILE("https://market.yandex.ru/cc"),
    WILDBERRIES("https://www.wildberries.ru/catalog"),
    OZON("https://www.ozon.ru/product"),
    OZON_MOBILE("https://ozon.ru/t"),
    UNKNOWN("unknown");
    private final String domain;
    ProviderType(String domain) {
        this.domain = domain;
    }
}
