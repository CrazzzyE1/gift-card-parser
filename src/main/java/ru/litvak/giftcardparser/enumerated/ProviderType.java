package ru.litvak.giftcardparser.enumerated;

import lombok.Getter;

@Getter
public enum ProviderType {
    YANDEX("https://market.yandex.ru/card"),
    UNKNOWN("unknown");
    private final String domain;
    ProviderType(String domain) {
        this.domain = domain;
    }
}
