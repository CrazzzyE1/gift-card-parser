package ru.litvak.giftcardparser.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import ru.litvak.giftcardparser.manager.ParserManager;
import ru.litvak.giftcardparser.model.responce.GiftCardResponse;

import java.io.IOException;
import java.net.URL;

import static ru.litvak.giftcardparser.enumerated.ProviderType.YANDEX;
import static ru.litvak.giftcardparser.enumerated.ProviderType.YANDEX_MOBILE;

@Slf4j
@Component
@RequiredArgsConstructor
public class YandexParserManager implements ParserManager {

    @Override
    public GiftCardResponse parse(String cardUrl) {
        String title = "Не найдено";
        String link = "Не найдено";
        double price = 0;
        byte[] img = null;

        try {
            Document doc = Jsoup
                    .connect(cardUrl)
                    .userAgent("Mozilla/5.0...")
                    .timeout(10000)
                    .get();

            Element productCardTitle = doc.select("h1[data-auto=productCardTitle][data-additional-zone=title]").first();

            if (productCardTitle != null) {
                title = productCardTitle.text();
            }

            Element image = doc.select("img[src*=/orig]").first();
            if (image != null) {
                link = image.attr("src");
                try {
                    String imageUrl = link;
                    if (!imageUrl.startsWith("http")) {
                        URL baseUrl = new URL(cardUrl);
                        imageUrl = new URL(baseUrl, link).toString();
                    }

                    img = Jsoup.connect(imageUrl)
                            .ignoreContentType(true)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                                    + "(KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36")
                            .timeout(10000)
                            .execute()
                            .bodyAsBytes();
                } catch (Exception e) {
                    log.warn("Не удалось загрузить изображение: {}", link, e);
                }
            } else {
                log.warn("Изображение с /orig не найдено для {}", cardUrl);
            }

            Element priceContainer = doc.select("span[data-auto=snippet-price-current][class=ds-valueLine]").first();

            if (priceContainer != null) {
                Element priceNumber = priceContainer.select("span.ds-text_typography_headline-3").first();
                if (priceNumber != null) {
                    price = Double.parseDouble(priceNumber.text().trim().replace(" ", ""));
                }
            }
        } catch (IOException e) {
            log.info("Ooops");
            e.printStackTrace();
        }
        return new GiftCardResponse(title, link, price, null, img);
    }

    @Override
    public boolean isSupport(String url) {
        return url.startsWith(YANDEX.getDomain()) || url.startsWith(YANDEX_MOBILE.getDomain());
    }
}
