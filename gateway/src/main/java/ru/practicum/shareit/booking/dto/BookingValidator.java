package ru.practicum.shareit.booking.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingValidator implements ConstraintValidator<BookingValid, BookItemRequestDto> {
    @Override
    public boolean isValid(BookItemRequestDto bookItemRequestDto, ConstraintValidatorContext context) {
        if (bookItemRequestDto.getStart() != null
                && bookItemRequestDto.getEnd() != null
                && (bookItemRequestDto.getStart().isBefore(LocalDateTime.now())
                || bookItemRequestDto.getEnd().isBefore(LocalDateTime.now())
                || bookItemRequestDto.getStart().isAfter(bookItemRequestDto.getEnd())
                || bookItemRequestDto.getStart().isEqual(bookItemRequestDto.getEnd()))
        ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Check end/start date")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
