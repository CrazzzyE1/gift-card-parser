package ru.litvak.giftcardparser.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import ru.litvak.giftcardparser.manager.ParserManager;
import ru.litvak.giftcardparser.model.responce.GiftCardResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

import static ru.litvak.giftcardparser.enumerated.ProviderType.OZON;
import static ru.litvak.giftcardparser.enumerated.ProviderType.OZON_MOBILE;

@Slf4j
@Component
@RequiredArgsConstructor
public class OzonParserManager implements ParserManager {

    private final WebDriver webDriver;

    @Override
    public GiftCardResponse parse(String cardUrl) {
        try {
            log.debug("Opening URL: {}", cardUrl);
            webDriver.get(cardUrl);
            new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(driver -> ((JavascriptExecutor) driver)
                            .executeScript("return document.readyState").equals("complete"));
            Thread.sleep(1500);
            String pageSource = webDriver.getPageSource();
            return parseWithJsoup(pageSource);
        } catch (Exception exception) {
            log.error("Exception while parsing Ozon product with Selenium: ", exception);
            return null;
        }
    }

    private GiftCardResponse parseWithJsoup(String pageSource) {
        try {
            Document doc = Jsoup.parse(pageSource);
            Element schemaScript = doc.select("script[type=application/ld+json]").first();
            if (schemaScript != null) {
                String jsonLd = schemaScript.html();
                JSONObject schema = new JSONObject(jsonLd);

                String name = schema.optString("name");
                String price = schema.getJSONObject("offers").optString("price");
                String url = schema.optString("image");
                byte[] imageBytes = new byte[0];
                try {
                    imageBytes = downloadImageWithCommonsIO(url);
                } catch (IOException e) {
                    log.error("Не удалось загрузить изображение: " + e.getMessage());
                }

                return GiftCardResponse.builder()
                        .title(name)
                        .price(price != null ? Double.parseDouble(price) : null)
                        .link(url)
                        .img(imageBytes)
                        .build();
            }

            Element ogTitle = doc.select("meta[property=og:title]").first();
            if (ogTitle != null) {
                String name = ogTitle.attr("content");
                return GiftCardResponse.builder()
                        .title(name)
                        .build();
            }

        } catch (Exception e) {
            log.error("Error parsing with Jsoup: ", e);
        }
        return null;
    }

    @Override
    public boolean isSupport(String url) {
//        return url.startsWith(OZON.getDomain()) || url.startsWith(OZON_MOBILE.getDomain());
        return false;
    }

    private byte[] downloadImageWithCommonsIO(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream()) {
            return IOUtils.toByteArray(in);
        }
    }
}