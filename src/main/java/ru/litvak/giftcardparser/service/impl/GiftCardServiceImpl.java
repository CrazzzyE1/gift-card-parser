package ru.litvak.giftcardparser.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.litvak.giftcardparser.exception.RequestParameterException;
import ru.litvak.giftcardparser.manager.ParserManager;
import ru.litvak.giftcardparser.model.request.SourceLinkRequest;
import ru.litvak.giftcardparser.model.responce.GiftCardResponse;
import ru.litvak.giftcardparser.service.GiftCardParserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GiftCardServiceImpl implements GiftCardParserService {

    private final List<ParserManager> parsers;

    @Override
    public GiftCardResponse parse(SourceLinkRequest request) {
        String link = request.getLink();
        ParserManager parser = parsers.stream()
                .filter(p -> p.isSupport(link))
                .findFirst()
                .orElseThrow(() ->
                        new RequestParameterException("link", "Данный сайт не поддерживается для автозаполнения, " +
                                "заполните карточку вручную"));
        return parser.parse(link);
    }
}