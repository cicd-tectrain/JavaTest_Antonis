package at.tectrain.cicd.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {


    private CalculatorService service;

    public CalculatorController(CalculatorService service) {
        this.service = service;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/add")
    public int add(@RequestParam int a, @RequestParam int b) {
        return service.add(a, b);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/subtract")
    public int subtract(@RequestParam int a, @RequestParam int b) {
        return service.subtract(a, b);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/multiply")
    public int multiply(@RequestParam int a, @RequestParam int b) {
        return service.multiply(a, b);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/divide")
    public int divide(@RequestParam int a, @RequestParam int b) {
        return service.divide(a, b);
    }
}
