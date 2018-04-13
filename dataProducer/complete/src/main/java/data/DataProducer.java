package data;

import java.util.Arrays;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DataProducer  implements ApplicationRunner {

    private  static int noTimes=10;
    private  static int interval=100;
        
	private static final Logger logger = LoggerFactory.getLogger(DataProducer.class);

	
	@Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
        logger.info("NonOptionArgs: {}", args.getNonOptionArgs());
        logger.info("OptionNames: {}", args.getOptionNames());

        for (String name : args.getOptionNames()){
            logger.info("arg-" + name + "=" + args.getOptionValues(name));
        }

        boolean containsOption = args.containsOption("noTimes");
        if (containsOption)
            noTimes = Integer.parseInt(args.getOptionValues("noTimes").get(0));
        
        containsOption = args.containsOption("interval");
        if (containsOption)
            interval = Integer.parseInt(args.getOptionValues("interval").get(0));
    }

	
	public static void main(String args[]) {
		SpringApplication.run(DataProducer.class,args);
		
		publishPx(DataProducer.interval, noTimes);
	}

    private static void publishPx(int interval, int noTimes) {

        
        RestTemplate restTemplate = new RestTemplate();
        for (int i=0; i< noTimes; i++ ) {
            long startTime = System.currentTimeMillis();
            Response response= restTemplate.getForObject("http://localhost:8080/PxUpdateService?px="+new Random().nextDouble()*100, Response.class);
            logger.info(response.toString());
            long endTime = System.currentTimeMillis();
            long  duration = endTime - startTime;
            long sleep = interval - duration;
            if (sleep > 0 ) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}