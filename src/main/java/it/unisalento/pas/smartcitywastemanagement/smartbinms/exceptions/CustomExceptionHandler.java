package it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AllocationRequestNotFound.class)
    public ResponseEntity<Object> handleSpecificException(AllocationRequestNotFound ex) {
        // Creare un oggetto di risposta personalizzato per l'eccezione specifica
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDTO(
                        6,
                        AllocationRequestNotFound.class.getSimpleName(),
                        "Allocation request not found"
                ));
    }

    @ExceptionHandler(RemovalRequestNotFound.class)
    public ResponseEntity<Object> handleSpecificException(RemovalRequestNotFound ex) {
        // Creare un oggetto di risposta personalizzato per l'eccezione specifica
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDTO(
                        7,
                        RemovalRequestNotFound.class.getSimpleName(),
                        "Removal request not found"
                ));
    }

    @ExceptionHandler(SmartBinStateInvalidException.class)
    public ResponseEntity<Object> handleAnotherException(SmartBinStateInvalidException ex) {
        // Creare un oggetto di risposta personalizzato per un'altra eccezione specifica
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO(
                        8,
                        SmartBinStateInvalidException.class.getSimpleName(),
                        "SmartBin state is invalid"
                ));
    }

    @ExceptionHandler(SmartBinTypeNotFoundException.class)
    public ResponseEntity<Object> handleAnotherException(SmartBinTypeNotFoundException ex) {
        // Creare un oggetto di risposta personalizzato per un'altra eccezione specifica
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDTO(
                        9,
                        SmartBinTypeNotFoundException.class.getSimpleName(),
                        "SmartBin type not found"
                ));
    }

    @ExceptionHandler(SmartBinAlreadyAllocatedException.class)
    public ResponseEntity<Object> handleAnotherException(SmartBinAlreadyAllocatedException ex) {
        // Creare un oggetto di risposta personalizzato per un'altra eccezione specifica
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionDTO(
                        10,
                        SmartBinAlreadyAllocatedException.class.getSimpleName(),
                        "SmartBin of the same type is already allocated in that position"
                ));
    }


    @ExceptionHandler(SmartBinNotFoundException.class)
    public ResponseEntity<Object> handleAnotherException(SmartBinNotFoundException ex) {
        // Creare un oggetto di risposta personalizzato per un'altra eccezione specifica
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionDTO(
                        11,
                        SmartBinNotFoundException.class.getSimpleName(),
                        "SmartBin not found"
                ));
    }

    @ExceptionHandler(RequestInvalidStatusException.class)
    public ResponseEntity<Object> handleAnotherException(RequestInvalidStatusException ex) {
        // Creare un oggetto di risposta personalizzato per un'altra eccezione specifica
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionDTO(
                        12,
                        RequestInvalidStatusException.class.getSimpleName(),
                        "Request state is invalid"
                ));
    }




}

