package module2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tigerff
 * @version 1.0
 * @date 2020/12/5 14:29
 */
@RestController
public class HelloController {
    @GetMapping("/module2/hello")
    public String getHello()
    {
        return "module2 hello";
    }
}
