package com.intexsoft.devi.service.Impl.aspect;

import com.intexsoft.devi.annotation.Annotation;
import com.intexsoft.devi.service.interfaces.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoServiceImpl implements DemoService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public boolean getTrue(Boolean bool) {
        logger.info("Demo Service invoke getTrue();");
        return true;
    }

    @Override
    public void setList(List<String> list, Boolean anything) {
        list.add("service");
        logger.info("Service invoke setList(), list.size: " + list.size());
    }

    @Override
    @Annotation
    public void methodWithAnnotation() {
        logger.info("Demo Service invoke methodWithAnnotation();");
    }
}
