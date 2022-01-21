package br.com.furlam.products.model;

import br.com.furlam.products.enums.EventType;
import lombok.Data;

@Data
public class Envelope {

    private EventType eventType;

    private String data;
}
