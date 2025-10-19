package ru.litvak.giftcardparser.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SourceLinkRequest {

    @NotNull
    @NotEmpty
    private String link;
}
