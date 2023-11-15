package it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *  Quando una eccezione viene sollevata, questa classe si occupa di restituire nel body
 *  un json contenente le informazioni sull'eccezione.
 *  Informazioni eccezione:
 *      - codice eccezione (fa riferimento ad un elenco di eccezioni già stilato su un documento excel)
 *      - nome eccezione (nome della Classe Java relativa all'eccezione)
 *      - descrizione eccezione
 */
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<Object> requestNotFoundHandler(RequestNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDTO(
                        8,
                        RequestNotFoundException.class.getSimpleName(),
                        "Request not found"
                ));
    }

    @ExceptionHandler(CleaningPathNotFoundException.class)
    public ResponseEntity<Object> cleaningPathHandler(CleaningPathNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDTO(
                        9,
                        CleaningPathNotFoundException.class.getSimpleName(),
                        "Cleaning path not found"
                ));
    }


    @ExceptionHandler(SmartBinStateInvalidException.class)
    public ResponseEntity<Object> smartBinInvalidStateHandler(SmartBinStateInvalidException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(
                        10,
                        SmartBinStateInvalidException.class.getSimpleName(),
                        "SmartBin state is invalid"
                ));
    }

    @ExceptionHandler(SmartBinTypeNotFoundException.class)
    public ResponseEntity<Object> smartBinTypeNotFoundHandler(SmartBinTypeNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDTO(
                        11,
                        SmartBinTypeNotFoundException.class.getSimpleName(),
                        "SmartBin type not found"
                ));
    }

    @ExceptionHandler(SmartBinAlreadyAllocatedException.class)
    public ResponseEntity<Object> smartBinAlreadyAllocatedHandler(SmartBinAlreadyAllocatedException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionDTO(
                        12,
                        SmartBinAlreadyAllocatedException.class.getSimpleName(),
                        "SmartBin of the same type is already allocated in that position"
                ));
    }

    @ExceptionHandler(SmartBinAlreadyRemovedException.class)
    public ResponseEntity<Object> smartBinAlreadyRemovedHandler(SmartBinAlreadyRemovedException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionDTO(
                        13,
                        SmartBinAlreadyRemovedException.class.getSimpleName(),
                        "The smart bin selected is already deallocated"
                ));
    }


    @ExceptionHandler(SmartBinNotFoundException.class)
    public ResponseEntity<Object> smartBinNotFoundHandler(SmartBinNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDTO(
                        14,
                        SmartBinNotFoundException.class.getSimpleName(),
                        "SmartBin not found"
                ));
    }

    @ExceptionHandler(RequestInvalidStatusException.class)
    public ResponseEntity<Object> requestInvalidStatusHandler(RequestInvalidStatusException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionDTO(
                        15,
                        RequestInvalidStatusException.class.getSimpleName(),
                        "Request state is invalid"
                ));
    }

    @ExceptionHandler(InvalidPositionException.class)
    public ResponseEntity<Object> invalidPositionHandler(InvalidPositionException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(
                        16,
                        InvalidPositionException.class.getSimpleName(),
                        "The position indicated is outside the municipality boundaries"
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        StringBuilder errorString = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errorString.append(errorMessage).append(".");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(
                        17,
                        MethodArgumentNotValidException.class.getSimpleName(),
                        errorString.deleteCharAt(errorString.length() - 1).toString()
                ));
    }


    @ExceptionHandler(RequestAlreadyExistsException.class)
    public ResponseEntity<Object> requestAlreadyExistsHandler(RequestAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionDTO(
                        18,
                        RequestAlreadyExistsException.class.getSimpleName(),
                        "The descripted request already exists"
                ));
    }

    @ExceptionHandler(RequestAlreadyConfirmedException.class)
    public ResponseEntity<Object> requestAlreadyConfirmedHandler(RequestAlreadyConfirmedException ex) {

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ExceptionDTO(
                        19,
                        RequestAlreadyConfirmedException.class.getSimpleName(),
                        "The selected request has already been confirmed and cannot be modified"
                ));
    }

    @ExceptionHandler(InvalidScheduledDateException.class)
    public ResponseEntity<Object> invalidScheduledDateHandler(InvalidScheduledDateException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(
                        20,
                        InvalidScheduledDateException.class.getSimpleName(),
                        "Scheduled Date must be after today date and time."
                ));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> noHandlerFoundHandler(NoHandlerFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getBody());
    }





}

