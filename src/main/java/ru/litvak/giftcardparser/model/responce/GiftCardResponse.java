package ru.litvak.giftcardparser.model.responce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GiftCardResponse {
    private String title;
    private String link;
    private Double price;
    private String description;
    private byte[] img;
}
