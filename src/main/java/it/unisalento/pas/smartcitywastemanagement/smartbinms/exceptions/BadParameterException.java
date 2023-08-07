package it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST)
public class BadParameterException extends Exception{
}
