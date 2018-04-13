package service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController
{

    /**
     * 
     * Return the average price
     * 
     * @param x
     * @return
     */
    @RequestMapping("/AvgPxService")
    public AvgPxService avgPxService(@RequestParam(value = "x", defaultValue = "1") String x) {
        return new AvgPxService(Integer.parseInt(x));
    }

    /**
     * 
     * Update the publish price
     * 
     * @param px
     * @return
     */
    @RequestMapping("/PxUpdateService")
    public PxUpdateService pxUpdateService(@RequestParam(value = "px", defaultValue = "-1") String px) {
        return new PxUpdateService(Double.parseDouble(px));
    }
}
