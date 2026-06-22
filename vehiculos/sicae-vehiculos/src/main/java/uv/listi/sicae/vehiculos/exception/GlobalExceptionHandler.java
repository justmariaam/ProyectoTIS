/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.listi.sicae.vehiculos.exception;

/**
 *
 * @author leona
 */

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> manejarErrorFormatoJson(HttpMessageNotReadableException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Formato de datos inválido. Verifica que los campos numéricos sean números válidos.");
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<?> manejarErrorPathVariable(MissingPathVariableException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "El parámetro '" + e.getParameter().getParameterName() + "' es obligatorio en la URL.");
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<?> manejarErrorParametroFaltante(MissingServletRequestParameterException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "El parámetro '" + e.getParameterName() + "' es obligatorio.");
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<?> manejarErrorTipoDato(MethodArgumentTypeMismatchException e) {
    Map<String, String> error = new HashMap<>();
    String nombreParametro = e.getName();
    String tipoEsperado = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "desconocido";
    error.put("error", "El parámetro '" + nombreParametro + "' debe ser de tipo " + tipoEsperado + ".");
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(InvalidFormatException.class)
  public ResponseEntity<?> manejarErrorFormatoInvalido(InvalidFormatException e) {
    Map<String, String> error = new HashMap<>();
    String campo = e.getPath().isEmpty() ? "desconocido" : e.getPath().get(0).getFieldName();
    error.put("error", "Formato inválido para el campo '" + campo + "'. Verifica el tipo de dato.");
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> manejarErrorValidacion(IllegalArgumentException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", e.getMessage());
    return ResponseEntity.badRequest().body(error);
  }
}
