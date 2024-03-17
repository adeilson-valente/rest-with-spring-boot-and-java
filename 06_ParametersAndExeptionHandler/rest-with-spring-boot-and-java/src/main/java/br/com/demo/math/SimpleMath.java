package br.com.demo.math;

import br.com.demo.converters.NumberConverter;
import br.com.demo.exeptions.UnsupportedMathOperationExeption;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class SimpleMath {
    public Double sum(Double numberOne,Double numberTwo) {
        return numberOne + numberTwo;
    }

    public Double subtraction(Double numberOne,Double numberTwo){
     return numberOne - numberTwo;
    }

    public Double mult(Double numberOne,Double numberTwo) {
        return numberOne * numberTwo;
    }

    public Double div(Double numberOne,Double numberTwo){
        return numberOne / numberTwo;
    }

    public Double raiz(Double numberOne) {
        return Math.sqrt(numberOne);
    }
}
