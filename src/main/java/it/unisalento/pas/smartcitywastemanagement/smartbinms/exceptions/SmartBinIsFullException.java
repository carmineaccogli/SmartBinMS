package it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class SmartBinIsFullException extends Exception{
}
