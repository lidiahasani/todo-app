package com.crispy.challenge.todoapp.dto;

import java.time.Instant;

public record DayBoundsDto(Instant start, Instant end) {
}
