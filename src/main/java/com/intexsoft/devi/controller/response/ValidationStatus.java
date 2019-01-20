package com.intexsoft.devi.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * The class contains information about the file and validation status.
 *
 * @author DEVIAPHAN on 10.01.2019
 * @project university
 */
public class ValidationStatus {

    private int rowCount = 0;

    private int validRow = 0;

    private int errorsCount = 0;

    private List<String> errors;

    public ValidationStatus() {
        errors = new ArrayList<>();
    }

    public void append(String value) {
        errors.add(value);
    }

    public void validRowInc() {
        validRow++;
    }

    public void errorRowInc() {
        errorsCount++;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    @JsonIgnore
    public boolean isValid() {
        return errors.isEmpty();
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getValidRow() {
        return validRow;
    }

    public int getErrorsCount() {
        return errorsCount;
    }

    public List<String> getErrors() {
        return errors;
    }
}
