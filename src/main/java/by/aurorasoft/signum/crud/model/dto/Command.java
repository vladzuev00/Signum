package by.aurorasoft.signum.crud.model.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;

import lombok.Value;

@Value
public class Command implements AbstractDto<Long> {
    Long id;
    String text;
    Tracker tracker;

    public Command(String text, Tracker tracker) {
        this.id = null;
        this.text = text;
        this.tracker = tracker;
    }

    public Command(Long id, String text, Tracker tracker) {
        this.id = id;
        this.text = text;
        this.tracker = tracker;
    }
}
