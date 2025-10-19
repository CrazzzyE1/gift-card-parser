package ru.litvak.giftcardparser.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import ru.litvak.giftcardparser.enumerated.ProviderType;
import ru.litvak.giftcardparser.manager.ParserManager;
import ru.litvak.giftcardparser.model.responce.GiftCardResponse;

import java.io.IOException;
import java.net.URL;

import static ru.litvak.giftcardparser.enumerated.ProviderType.YANDEX;

@Slf4j
@Component
@RequiredArgsConstructor
public class YandexParserManager implements ParserManager {

    private static final ProviderType TYPE = YANDEX;

    @Override
    public GiftCardResponse parse(String cardUrl) {
        String title = "Не найдено";
        String link = "Не найдено";
//        String description = "Не найдено";
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

            Element image = doc.select("img[alt=%s]".formatted(title)).first();
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
                            .userAgent("Mozilla/5.0...")
                            .timeout(10000)
                            .execute()
                            .bodyAsBytes();
                } catch (Exception e) {
                    log.warn("Не удалось загрузить изображение: {}", link, e);
                }
            }

            Element priceContainer = doc.select("span[data-auto=snippet-price-current][class=ds-valueLine]").first();

            if (priceContainer != null) {
                Element priceNumber = priceContainer.select("span.ds-text_typography_headline-3").first();
                if (priceNumber != null) {
                    price = Double.parseDouble(priceNumber.text().trim().replace(" ", ""));
                }
            }

//            Element descriptionContainer = doc.select("div[id=product-description]").first();
//            if (descriptionContainer != null) {
//                Element descriptionElement = descriptionContainer.select(
//                        "div[class=ds-text ds-text_weight_reg ds-text_typography_text xt_vL ds-text_text_loose ds-text_text_reg]"
//                ).first();
//                if (descriptionElement != null) {
//                    description = descriptionElement.text();
//                }
//
//            }
        } catch (IOException e) {
            log.info("Ooops");
            e.printStackTrace();
        }
        return new GiftCardResponse(title, link, price, null, img);
    }

    @Override
    public boolean isSupport(String url) {
        return url.startsWith(TYPE.getDomain());
    }
}
