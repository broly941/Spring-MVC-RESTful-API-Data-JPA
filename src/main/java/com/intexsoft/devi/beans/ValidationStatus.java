package com.intexsoft.devi.beans;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DEVIAPHAN on 10.01.2019
 * @project university
 */
@Component
public class ValidationStatus {

    private int errorsCount;
    private List<String> status;

    public ValidationStatus() {
        status = new ArrayList<>();
    }

    public void append(String value) {
        status.add(value);
    }

    public void setErrorCount(int errorCount) {
        this.errorsCount = errorCount;
    }

    public int getErrorsCount() {
        return errorsCount;
    }

    public List<String> getValidationStatus() {
        return status;
    }
}
